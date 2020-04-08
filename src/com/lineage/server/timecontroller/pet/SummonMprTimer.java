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
 * Summon MP自然回复时间轴
 * 
 * @author dexc
 * 
 */
public class SummonMprTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(SummonMprTimer.class);

    private ScheduledFuture<?> _timer;

    private static int _time = 0;

    public void start() {
        _time = 0;
        final int timeMillis = 1000;// 1秒
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            _time++;

            final Collection<L1SummonInstance> allPet = WorldSummons.get()
                    .all();
            // 不包含元素
            if (allPet.isEmpty()) {
                return;
            }

            for (final Iterator<L1SummonInstance> iter = allPet.iterator(); iter
                    .hasNext();) {
                final L1SummonInstance summon = iter.next();
                if (MprPet.mpUpdate(summon, _time)) {
                    Thread.sleep(5);
                }
            }

        } catch (final Exception e) {
            _log.error("Summon MP自然回复时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final SummonMprTimer summonMprTimer = new SummonMprTimer();
            summonMprTimer.start();
        }
    }
}
