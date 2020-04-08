package com.lineage.server.timecontroller.npc;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldMob;

/**
 * Npc HP自然回复时间轴(对怪物)
 * 
 * @author dexc
 * 
 */
public class NpcHprTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(NpcHprTimer.class);

    private ScheduledFuture<?> _timer;

    private static int _time = 0;

    public void start() {
        _time = 0;
        final int timeMillis = 1000;// 1.2秒
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            _time++;

            final Collection<L1MonsterInstance> allMob = WorldMob.get().all();
            // 不包含元素
            if (allMob.isEmpty()) {
                return;
            }

            for (final Iterator<L1MonsterInstance> iter = allMob.iterator(); iter
                    .hasNext();) {
                final L1MonsterInstance mob = iter.next();
                // HP是否具备回复条件
                if (mob.isHpR()) {
                    hpUpdate(mob);
                    //Thread.sleep(50); 取消这个,修正NPC回复HP时间错误hjx1000
                }
            }

        } catch (final Exception e) {
            _log.error("Npc HP自然回复时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final NpcHprTimer npcHprTimer = new NpcHprTimer();
            npcHprTimer.start();
        }
    }

    /**
     * 判断是否执行回复
     * 
     * @param mob
     */
    private static void hpUpdate(final L1MonsterInstance mob) {
        int hprInterval = mob.getNpcTemplate().get_hprinterval();
        // 无特别指定时间 每10秒回复一次
        if (hprInterval <= 0) {
            hprInterval = 10;
        }

        if ((_time % hprInterval) == 0) {
            // 无特别指定回复量 每次回复20
            int hpr = mob.getNpcTemplate().get_hpr();
            if (hpr <= 0) {
                hpr = mob.getLevel();
            }
            
            hprInterval(mob, hpr);
        }
    }

    /**
     * 执行回复HP
     * 
     * @param mob
     * @param hpr
     */
    private static void hprInterval(final L1MonsterInstance mob, final int hpr) {
        try {
            if (mob.isHpRegenerationX()) {
                mob.setCurrentHp(mob.getCurrentHp() + hpr);
            }

        } catch (final Exception e) {
            _log.error("Npc 执行回复HP发生异常", e);
            mob.deleteMe();
        }
    }
}
