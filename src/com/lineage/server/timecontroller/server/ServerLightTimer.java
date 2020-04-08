package com.lineage.server.timecontroller.server;

import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.LightSpawnTable;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1FieldObjectInstance;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;

/**
 * 照明物件召唤时间轴
 * 
 * @author dexc
 * 
 */
public class ServerLightTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(ServerLightTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 60 * 1000;// 1分钟
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            checkLightTime();

        } catch (final Exception e) {
            _log.error("照明物件召唤时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final ServerLightTimer lightTimer = new ServerLightTimer();
            lightTimer.start();
        }
    }

    private static boolean isSpawn = false;

    private static void checkLightTime() {
        try {
            final int serverTime = L1GameTimeClock.getInstance().currentTime()
                    .getSeconds();

            final int nowTime = serverTime % 86400;
            // 移除照明
            if ((nowTime >= ((5 * 3600) + 3300))
                    && (nowTime < ((17 * 3600) + 3300))) { // 5:55~17:55
                if (isSpawn) {
                    isSpawn = false;
                    for (final L1Object object : World.get().getObject()) {
                        if (object instanceof L1FieldObjectInstance) {
                            final L1FieldObjectInstance npc = (L1FieldObjectInstance) object;
                            if (((npc.getNpcTemplate().get_npcId() == 81177)
                                    || (npc.getNpcTemplate().get_npcId() == 81178)
                                    || (npc.getNpcTemplate().get_npcId() == 81179)
                                    || (npc.getNpcTemplate().get_npcId() == 81180) || (npc
                                    .getNpcTemplate().get_npcId() == 81181))
                                    && ((npc.getMapId() == 0) || (npc
                                            .getMapId() == 4))) {
                                npc.deleteMe();
                            }
                        }
                    }
                }

                // 召唤照明
            } else if (((nowTime >= ((17 * 3600) + 3300)) && (nowTime <= 24 * 3600))
                    || ((nowTime >= 0 * 3600) && (nowTime < ((5 * 3600) + 3300)))) { // 17:55~24:00,0:00~5:55
                if (!isSpawn) {
                    isSpawn = true;
                    LightSpawnTable.getInstance();
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
