package com.lineage.server.timecontroller.pc;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.thread.PcOtherThreadPool;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;

/**
 * PC Lawful更新处理 时间轴<BR>
 * 血盟技能光环 显示处理<BR>
 * 
 * @author dexc
 * 
 */
public class LawfulTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(LawfulTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 650;
        _timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    private static int _time = 0;// 计时器

    @Override
    public void run() {
        try {
            final Collection<L1PcInstance> all = World.get().getAllPlayers();
            // 不包含元素
            if (all.isEmpty()) {
                return;
            }

            for (final Iterator<L1PcInstance> iter = all.iterator(); iter
                    .hasNext();) {
                final L1PcInstance tgpc = iter.next();
                if (check(tgpc)) {
                    tgpc.onChangeLawful();
                    tgpc.onChangeKarma(); //更新好友度 hjx1000

                    if (_time % 20 == 0) {
                        switch (tgpc.getMapId()) {
                            case 340:// 古鲁丁商店村
                            case 350:// 奇岩商店村
                            case 360:// 欧瑞商店村
                            case 370:// 银骑士商店村
                                break;

                            default:
                                // 检查城堡战争状态
                                if (ServerWarExecutor.get().checkCastleWar() <= 0) {
                                    if (checkC(tgpc)) {
                                        showClan(tgpc);
                                    }
                                }
                                break;
                        }
                    }
                    Thread.sleep(1);
                }
            }

            /*
             * for (final L1PcInstance tgpc : all) { Thread.sleep(1); if
             * (check(tgpc)) { tgpc.onChangeLawful();
             * 
             * if (_time % 20 == 0) { switch (tgpc.getMapId()) { case 340://
             * 古鲁丁商店村 case 350:// 奇岩商店村 case 360:// 欧瑞商店村 case 370:// 银骑士商店村
             * break;
             * 
             * default: // 检查城堡战争状态 if (ServerWarExecutor.get().checkCastleWar()
             * <= 0) { if (checkC(tgpc)) { showClan(tgpc); } } break; } } } }
             */
            _time++;

        } catch (final Exception e) {
            _log.error("Lawful更新处理时间轴异常重启", e);
            PcOtherThreadPool.get().cancel(_timer, false);
            final LawfulTimer lawfulTimer = new LawfulTimer();
            lawfulTimer.start();
        }
    }

    /**
     * 主判断
     * 
     * @param tgpc
     * @return true:执行 false:不执行
     */
    private static boolean check(L1PcInstance tgpc) {
        try {
            if (tgpc == null) {
                return false;
            }

            if (tgpc.getOnlineStatus() == 0) {
                return false;
            }

            if (tgpc.getNetConnection() == null) {
                return false;
            }

            if (tgpc.isTeleport()) {
                return false;
            }

            if (tgpc.isDead()) {// 死亡
                return false;
            }

            if (tgpc.getCurrentHp() <= 0) {// 目前HP小于0
                return false;
            }

        } catch (final Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 血盟技能光环系统 判断
     * 
     * @return true:执行 false:不执行
     */
    private static boolean checkC(L1PcInstance tgpc) {
        try {
            if (tgpc.getClan() == null) {// 无血盟
                return false;
            }

            if (!tgpc.getClan().isClanskill()) {// 血盟无血盟技能
                return false;
            }

            final int count = tgpc.getClan().getOnlineClanMemberSize();
            if (count < 16) {// 血盟人数16人以下
                return false;
            }

            if (tgpc.get_other().get_clanskill() == 0) {// 无血盟技能
                return false;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * 展示效果
     */
    private static void showClan(L1PcInstance tgpc) {
        try {
            final int count = tgpc.getClan().getOnlineClanMemberSize();

            if (count >= 30) {
                // 送出封包
                tgpc.sendPacketsX8(new S_SkillSound(tgpc.getId(), 5201));

            } else {
                // 送出封包
                tgpc.sendPacketsX8(new S_SkillSound(tgpc.getId(), 5263));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
