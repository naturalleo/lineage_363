package com.lineage.server.timecontroller.pc;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.lock.UpdateLocReading;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.thread.PcOtherThreadPool;
import com.lineage.server.world.World;

/**
 * 解除人物卡点计时时间轴
 * 
 * @author dexc
 * 
 */
public class UnfreezingTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(UnfreezingTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 1000;// 1秒
        _timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, timeMillis,
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
                if (tgpc.get_unfreezingTime() == 0) {
                    continue;
                }
                // 取回时间
                final int time = tgpc.get_unfreezingTime() - 1;
                tgpc.set_unfreezingTime(time);
                if (time <= 0) {
                    UpdateLocReading.get().setPcLoc(tgpc.getAccountName());
                    tgpc.sendPackets(new S_ServerMessage("\\fR帐号内其他人物传送回指定位置！"));

                    L1Teleport.teleport(tgpc, 32781, 32856, (short) 340, 5,
                            true);

                } else {
                    tgpc.sendPackets(new S_ServerMessage("\\fR" + (time)
                            + "秒后传送回指定位置！"));
                }
            }

            /*
             * for (final L1PcInstance tgpc : all) { if
             * (tgpc.get_unfreezingTime() == 0) { continue; } // 取回时间 final int
             * time = tgpc.get_unfreezingTime() - 1;
             * tgpc.set_unfreezingTime(time); if (time <= 0) {
             * UpdateLocReading.get().setPcLoc(tgpc.getAccountName());
             * tgpc.sendPackets(new S_ServerMessage("\\fR帐号内其他人物传送回指定位置！"));
             * 
             * L1Teleport.teleport(tgpc, 32781, 32856, (short) 340, 5, true);
             * 
             * } else { tgpc.sendPackets(new S_ServerMessage("\\fR" + (time) +
             * "秒后传送回指定位置！")); } Thread.sleep(5); }
             */

        } catch (final Exception e) {
            _log.error("解除人物卡点时间轴异常重启", e);
            PcOtherThreadPool.get().cancel(_timer, false);
            final UnfreezingTimer skillTimer = new UnfreezingTimer();
            skillTimer.start();
        }
    }

}
