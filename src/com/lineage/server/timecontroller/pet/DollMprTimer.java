package com.lineage.server.timecontroller.pet;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;

/**
 * 魔法娃娃处理时间轴(娃娃效果:MP恢复(指定时间))
 * 
 * @author dexc
 * 
 */
public class DollMprTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(DollMprTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 1000;// 1秒
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            final Collection<L1PcInstance> all = World.get().getAllPlayers();
            // 不包含元素
            if (all.isEmpty()) {
                return;
            }

            for (final Iterator<L1PcInstance> iter = all.iterator(); iter
                    .hasNext();) {
                final L1PcInstance tgpc = iter.next();
                if (tgpc.get_doll_mpr_time_src() <= 0) {
                    continue;
                }
                if (!checkErr(tgpc)) {
                    continue;
                }
                final int newMp = tgpc.getCurrentMp() + tgpc.get_doll_mpr();
                tgpc.setCurrentMp(newMp);
                Thread.sleep(50);
            }

        } catch (final Exception e) {
            _log.error("魔法娃娃处理时间轴(MPR)异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final DollMprTimer dollTimer = new DollMprTimer();
            dollTimer.start();
        }
    }

    /**
     * 该PC是否执行恢复
     * 
     * @param tgpc
     * @return true:正常 false:异常
     */
    private static boolean checkErr(final L1PcInstance tgpc) {
        try {
            if (tgpc == null) {
                return false;
            }
            if (tgpc.getOnlineStatus() == 0) {
                return false;
            }
            if (tgpc.getNetConnection() == null) {
                return false;
            }
            if (tgpc.isTeleport()) {
                return false;
            }
            if (!tgpc.getMpRegeneration()) {
                return false;
            }
            // HP已满
            if (tgpc.getCurrentMp() >= tgpc.getMaxMp()) {
                return false;
            }

            final int newtime = tgpc.get_doll_mpr_time() - 1;
            tgpc.set_doll_mpr_time(newtime);
            if (newtime <= 0) {
                tgpc.set_doll_mpr_time(tgpc.get_doll_mpr_time_src());
                return true;
            }

        } catch (final Exception e) {
            return false;
        }
        return false;
    }
}
