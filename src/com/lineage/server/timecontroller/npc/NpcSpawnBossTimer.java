package com.lineage.server.timecontroller.npc;

import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.thread.GeneralThreadPool;

/**
 * NPC(BOSS)召唤时间时间轴
 * 
 * @author dexc
 * 
 */
public class NpcSpawnBossTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(NpcSpawnBossTimer.class);

    private ScheduledFuture<?> _timer;

    public static final Map<L1NpcInstance, Long> MAP = new ConcurrentHashMap<L1NpcInstance, Long>();

    /*
     * private static final ArrayList<L1NpcInstance> REMOVE = new
     * ArrayList<L1NpcInstance>();
     */

    public void start() {
        final int timeMillis = 60 * 1000;// 1分钟
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            // 不包含元素
            if (MAP.isEmpty()) {
                return;
            }
            for (final L1NpcInstance npc : MAP.keySet()) {
                final Long time = MAP.get(npc);
                long t = time - 60;

                if (time > 0) {
                    // 更新时间
                    MAP.put(npc, t);

                } else {
                    // 召唤
                    spawn(npc);
                    MAP.remove(npc);
                }
                Thread.sleep(50);
            }

        } catch (final Exception e) {
            _log.error("NPC(BOSS)召唤时间时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final NpcSpawnBossTimer bossTimer = new NpcSpawnBossTimer();
            bossTimer.start();

        } finally {
            // ListMapUtil.clear(REMOVE);
        }
    }

    /**
     * 召唤BOSS
     * 
     * @param npc
     */
    private static void spawn(L1NpcInstance npc) {
        try {
            npc.getSpawn().executeSpawnTask(npc.getSpawnNumber(), npc.getId());

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
