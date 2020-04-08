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
 * Npc MP自然回复时间轴(对怪物)
 * 
 * @author dexc
 * 
 */
public class NpcMprTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(NpcMprTimer.class);

    private ScheduledFuture<?> _timer;

    private static int _time = 0;

    public void start() {
        _time = 0;
        final int timeMillis = 1000;
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
                if (mob.isMpR()) {
                    mpUpdate(mob);
                    //Thread.sleep(50); //hjx1000
                }
            }

        } catch (final Exception e) {
            _log.error("Npc MP自然回复时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final NpcMprTimer npcMprTimer = new NpcMprTimer();
            npcMprTimer.start();
        }
    }

    /**
     * 判断是否执行回复
     * 
     * @param mob
     */
    private static void mpUpdate(final L1MonsterInstance mob) {
        int mprInterval = mob.getNpcTemplate().get_mprinterval();
        // 无特别指定时间 每10秒回复一次
        if (mprInterval <= 0) {
            mprInterval = 10;
        }

        if ((_time % mprInterval) == 0) {
            // 无特别指定回复量 每次回复20
            int mpr = mob.getNpcTemplate().get_mpr();
            if (mpr <= 0) {
                mpr = 20;
            }

            mprInterval(mob, mpr);
        }
    }

    /**
     * 执行回复MP
     * 
     * @param mob
     * @param mpr
     */
    private static void mprInterval(final L1MonsterInstance mob, final int mpr) {
        try {
            if (mob.isMpRegenerationX()) {
                mob.setCurrentMp(mob.getCurrentMp() + mpr);
            }

        } catch (final Exception e) {
            _log.error("Npc 执行回复MP发生异常", e);
            mob.deleteMe();
        }
    }
}
