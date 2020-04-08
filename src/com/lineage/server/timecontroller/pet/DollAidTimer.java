package com.lineage.server.timecontroller.pet;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldDoll;

/**
 * 魔法娃娃处理时间轴(娃娃效果:辅助技能)
 * 
 * @author dexc
 * 
 */
public class DollAidTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(DollAidTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 10 * 1000;// 10秒
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            final Collection<L1DollInstance> allDoll = WorldDoll.get().all();
            // 不包含元素
            if (allDoll.isEmpty()) {
                return;
            }

            for (final Iterator<L1DollInstance> iter = allDoll.iterator(); iter
                    .hasNext();) {
                final L1DollInstance doll = iter.next();
                if (doll.is_power_doll()) {// 辅助技能娃娃
                    doll.startDollSkill();
                }
                Thread.sleep(50);
            }

        } catch (final Exception e) {
            _log.error("魔法娃娃处理时间轴(辅助技能)异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final DollAidTimer dollTimer = new DollAidTimer();
            dollTimer.start();
        }
    }
}
