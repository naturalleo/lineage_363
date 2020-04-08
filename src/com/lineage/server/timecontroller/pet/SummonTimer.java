package com.lineage.server.timecontroller.pet;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldSummons;

/**
 * 召唤兽处理时间轴
 * 
 * @author dexc
 * 
 */
public class SummonTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(SummonTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 60 * 1000;// 60秒
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            final Collection<L1SummonInstance> allPet = WorldSummons.get()
                    .all();
            // 不包含元素
            if (allPet.isEmpty()) {
                return;
            }

            for (final Iterator<L1SummonInstance> iter = allPet.iterator(); iter
                    .hasNext();) {
                final L1SummonInstance summon = iter.next();
                final int time = summon.get_time() - 60;
                // time -= 60;
                if (time <= 0) {
                    outSummon(summon);

                } else {
                    summon.set_time(time);
                }
                Thread.sleep(50);
            }

        } catch (final Exception e) {
            _log.error("召唤兽处理时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final SummonTimer summon_Timer = new SummonTimer();
            summon_Timer.start();
        }
    }

    /**
     * 移除召唤兽
     * 
     * @param tgpc
     */
    private static void outSummon(final L1SummonInstance summon) {
        try {
            if (summon != null) {
                if (summon.destroyed()) {
                    return;
                }

                if (summon.tamed()) {
                    // 召唤兽解放
                    summon.liberate();

                } else {
                    // 解散
                    summon.Death(null);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
