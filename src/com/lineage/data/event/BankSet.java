package com.lineage.data.event;

import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.datatables.lock.AccountBankReading;
import com.lineage.server.templates.L1Bank;
import com.lineage.server.templates.L1Event;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.RangeLong;

/**
 * 银行管理员系统<BR>
 * 
 * #新增银行管理系统 DELETE FROM `server_event` WHERE `id`='52'; INSERT INTO
 * `server_event` VALUES ('52', '银行管理员系统', 'BankSet', '1',
 * '5000000000,6000000000,40308,5,0.05', '说明:银行储蓄上限,银行存款上限(加入利息),发放利息时间
 * (单位:分),银行利率(%)'); #新增银行管理员 UPDATE `npc` SET
 * `name`='银行管理员',`nameid`='银行管理员',`classname`='event.Npc_Bank',`note`='' WHERE
 * `npcid`='50002';# #新增银行管理员召换位置 DELETE FROM `server_event_spawn` WHERE
 * `id`='40307'; DELETE FROM `server_event_spawn` WHERE `id`='40308'; INSERT
 * INTO `server_event_spawn` VALUES ('40307', '52', '银行管理员', '1', '50002', '0',
 * '33449', '32792', '0', '0', '5', '0', '4', '0', '0'); INSERT INTO
 * `server_event_spawn` VALUES ('40308', '52', '银行管理员', '1', '50002', '0',
 * '33074', '33396', '0', '0', '5', '0', '4', '0', '0');
 * 
 * @author dexc
 * 
 */
public class BankSet extends EventExecutor {

    private static final Log _log = LogFactory.getLog(BankSet.class);

    // 银行储蓄上限
    public static long BANKMAX;

    // 银行存款达到多少之后不会给利息
    public static long BANK_INTEREST_OVER;

    // 银行存款达的到具编号
    public static int BANK_ITEMID;

    // 银行计算利息时间 (单位:分)
    public static int BANK_TIME = 1;

    // 银行利率 最低值0.1 利息公式 = 目前存款 * BankInterest
    public static double BANK_INTEREST = 0.01;

    /**
	 *
	 */
    private BankSet() {
        // TODO Auto-generated constructor stub
    }

    public static EventExecutor get() {
        return new BankSet();
    }

    @Override
    public void execute(final L1Event event) {
        try {
            final String[] set = event.get_eventother().split(",");

            try {
                BANKMAX = Long.parseLong(set[0]);

            } catch (Exception e) {
                BANKMAX = 1900000000;
                _log.error("未设定银行储蓄上限(使用预设19亿)");
            }

            try {
                BANK_INTEREST_OVER = Long.parseLong(set[1]);

            } catch (Exception e) {
                BANK_INTEREST_OVER = 2000000000;
                _log.error("未设定利息及现存款总额(使用预设20亿)");
            }

            try {
                BANK_ITEMID = Integer.parseInt(set[2]);

            } catch (Exception e) {
                BANK_ITEMID = 40308;
                _log.error("未设定存款物品编号(使用预设40308)");
            }

            try {
                BANK_TIME = Integer.parseInt(set[3]);

            } catch (Exception e) {
                BANK_TIME = 60;
                _log.error("未设银行计算利息时间(使用预设60分钟)");
            }

            try {
                BANK_INTEREST = Double.parseDouble(set[4]);

            } catch (Exception e) {
                BANK_INTEREST = 0.01D;
                _log.error("未设银行利息比率(使用预设0.01%)");
            }
            _log.info("银行设置\n 帐户储蓄上限: " + RangeLong.scount(BANKMAX)
                    + " \n 存款总额限制: " + RangeLong.scount(BANK_INTEREST_OVER)
                    + "\n 存款物品编号: " + RangeLong.scount(BANK_INTEREST_OVER)
                    + "\n 利息计算时间: " + BANK_TIME + "\n 利息比率: " + BANK_INTEREST);

            // 载入资料
            AccountBankReading.get().load();

            // 启动时间轴
            final BankTimer bankTime = new BankTimer();
            bankTime.start();

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private class BankTimer extends TimerTask {

        private ScheduledFuture<?> _timer;

        public void start() {
            final int timeMillis = BANK_TIME * 60 * 1000;// 10分钟
            _timer = GeneralThreadPool.get().scheduleAtFixedRate(this,
                    timeMillis, timeMillis);
        }

        @Override
        public void run() {
            try {
                final Map<String, L1Bank> map = AccountBankReading.get().map();
                if (map.isEmpty()) {
                    return;
                }

                for (final String key : map.keySet()) {
                    final L1Bank bank = map.get(key);
                    if (bank.isLan()) {
                        if (!bank.isEmpty()) {
                            long nwecount = bank.get_adena_count()
                                    + Math.round(bank.get_adena_count()
                                            * BANK_INTEREST);
                            bank.set_adena_count(nwecount);
                            AccountBankReading.get().updateAdena(
                                    bank.get_account_name(), nwecount);
                            Thread.sleep(5);
                        }
                    }
                }

            } catch (final Exception e) {
                _log.error("银行管理员时间轴异常重启", e);
                GeneralThreadPool.get().cancel(_timer, false);
                final BankTimer bankTime = new BankTimer();
                bankTime.start();

            } finally {

            }
        }
    }
}
