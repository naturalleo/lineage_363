package com.lineage.server.timecontroller.npc;

import java.util.HashMap;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1BowInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldBow;

/**
 * 固定攻击器时间轴
 * 
 * @author dexc
 * 
 */
public class NpcBowTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(NpcBowTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 1500;
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            final HashMap<Integer, L1BowInstance> allBow = WorldBow.get().map();
            // 不包含元素
            if (allBow.isEmpty()) {
                return;
            }

            for (final Object iter : allBow.values().toArray()) {
                final L1BowInstance bowNpc = (L1BowInstance) iter;
                if (bowNpc == null) {
                    continue;
                }
                if (bowNpc.isDead()) {
                    continue;
                }
                // AI动作中
                if (bowNpc.get_start()) {
                    if (bowNpc.checkPc()) {
                        bowNpc.atkTrag();
                    }
                }
                Thread.sleep(50);
            }

        } catch (final Exception e) {
            _log.error("固定攻击器时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final NpcBowTimer bow = new NpcBowTimer();
            bow.start();
        }
    }
}
