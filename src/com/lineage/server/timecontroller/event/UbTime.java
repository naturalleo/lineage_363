package com.lineage.server.timecontroller.event;

import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.UBTable;
import com.lineage.server.model.L1UltimateBattle;
import com.lineage.server.thread.GeneralThreadPool;

/**
 * 无线大赛 计时器
 * 
 * @author dexc
 * 
 */
public class UbTime extends TimerTask {

    private static final Log _log = LogFactory.getLog(UbTime.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 15 * 1100;// 15秒
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            checkUbTime(); // UB开始时间をチェック

        } catch (final Exception e) {
            _log.error("无线大赛时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final UbTime timer = new UbTime();
            timer.start();
        }
    }

    private static void checkUbTime() {
        for (final L1UltimateBattle ub : UBTable.getInstance().getAllUb()) {
            if (ub.checkUbTime() && !ub.isActive()) {
                ub.start(); // UB开始
            }
        }
    }
}
