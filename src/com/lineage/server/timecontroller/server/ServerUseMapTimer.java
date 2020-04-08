package com.lineage.server.timecontroller.server;

import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxGame;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;

/**
 * 计时地图时间轴
 * 
 * @author dexc
 * 
 */
public class ServerUseMapTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(ServerUseMapTimer.class);

    private ScheduledFuture<?> _timer;

    public static final Map<L1PcInstance, Integer> MAP = new ConcurrentHashMap<L1PcInstance, Integer>();

    public void start() {
        final int timeMillis = 1100;// 1.1秒
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    /**
     * 加入计时地图物件清单<BR>
     * 同时更新地图使用时间<BR>
     * 送出时间封包
     * 
     * @param pc
     * @param time
     *            秒
     */
    public static void put(final L1PcInstance pc, final int time) {
        pc.sendPackets(new S_PacketBoxGame(S_PacketBoxGame.STARTTIME, time));
        pc.get_other().set_usemapTime(time);
        MAP.put(pc, new Integer(time));
    }

    @Override
    public void run() {
        try {
            // 包含元素
            if (!MAP.isEmpty()) {
                for (final L1PcInstance key : MAP.keySet()) {
                    // 取回剩余时间
                    Integer value = MAP.get(key);
                    value--;
                    if (value <= 0) {
                        teleport(key);
                        Thread.sleep(1);

                    } else {
                        // 更新可用时间
                        key.get_other().set_usemapTime(value);
                        MAP.put(key, value);
                    }
                }
            }

        } catch (final Exception e) {
            _log.error("计时地图时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final ServerUseMapTimer useMapTimer = new ServerUseMapTimer();
            useMapTimer.start();
        }
    }

    /**
     * 传出PC
     * 
     * @param item
     */
    public static void teleport(final L1PcInstance tgpc) {
        MAP.remove(tgpc);

        if (World.get().getPlayer(tgpc.getName()) == null) {
            // 人物离开世界
            return;
        }

        if (tgpc.getMapId() == tgpc.get_other().get_usemap()) {
            L1Teleport.teleport(tgpc, 33080, 33392, (short) 4, 5, true);
        }
        tgpc.get_other().set_usemapTime(0);
        tgpc.get_other().set_usemap(-1);
        tgpc.sendPackets(new S_PacketBoxGame(S_PacketBoxGame.STARTTIMECLEAR));
    }
}
