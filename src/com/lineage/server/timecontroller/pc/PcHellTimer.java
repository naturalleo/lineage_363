package com.lineage.server.timecontroller.pc;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.thread.PcOtherThreadPool;
import com.lineage.server.world.World;

/**
 * PC 地狱模式处理 时间轴
 * 
 * @author dexc
 * 
 */
public class PcHellTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(PcHellTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 1100;
        _timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

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
                int time = tgpc.getHellTime();
                // 非地狱状态
                if (time <= 0) {
                    continue;
                }
                time--;
                check(tgpc, time);
                Thread.sleep(1);
            }

            /*
             * for (final L1PcInstance tgpc : all) { int time =
             * tgpc.getHellTime(); // 非地狱状态 if (time <= 0) { continue; } time--;
             * check(tgpc, time); Thread.sleep(1); }
             */

        } catch (final Exception e) {
            _log.error("PC 地狱模式处理时间轴异常重启", e);
            PcOtherThreadPool.get().cancel(_timer, false);
            final PcHellTimer hellTimer = new PcHellTimer();
            hellTimer.start();
        }
    }

    /**
     * 检查地狱模式时间
     * 
     * @param tgpc
     * @param time
     */
    private static void check(final L1PcInstance tgpc, final Integer time) {
        if (time > 0) {
            // 更新
            tgpc.setHellTime(time);

        } else {
            // 时间到
            tgpc.setHellTime(0);

            // 未断线移除状态
            if (tgpc.getNetConnection() != null) {
                outPc(tgpc);
            }
        }
    }

    /**
     * 离开地狱模式
     * 
     * @param tgpc
     */
    private static void outPc(final L1PcInstance tgpc) {
        try {
            if (tgpc != null) {
                tgpc.endHell();
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
