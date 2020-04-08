package com.lineage.server.timecontroller.npc;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldNpc;

/**
 * NPC动作暂停时间轴
 * 
 * @author dexc
 * 
 */
public class NpcRestTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(NpcRestTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 5000;
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            final Collection<L1NpcInstance> allMob = WorldNpc.get().all();
            // 不包含元素
            if (allMob.isEmpty()) {
                return;
            }

            for (final Iterator<L1NpcInstance> iter = allMob.iterator(); iter
                    .hasNext();) {
                final L1NpcInstance npc = iter.next();
                if (npc == null) {
                    continue;
                }
                if (npc.get_stop_time() < 0) {
                    continue;
                }
                final int time = npc.get_stop_time();
                npc.set_stop_time(time - 5);

                if (npc.get_stop_time() <= 0) {
                    npc.set_stop_time(-1);
                    npc.setRest(false);
                }
                Thread.sleep(50);
            }

        } catch (final Exception e) {
            _log.error("NPC动作暂停时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final NpcRestTimer restTimer = new NpcRestTimer();
            restTimer.start();
        }
    }
}
