package com.lineage.server.timecontroller.pc;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxParty;
import com.lineage.server.thread.PcOtherThreadPool;
import com.lineage.server.world.World;

/**
 * 队伍更新时间轴(优化完成LOLI 2012-05-30)
 * 
 * @author KZK
 * 
 */
public class PartyTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(PartyTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 5000;// 5秒
        _timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    /*
     * private final L1PcInstance _pc;
     * 
     * public PartyTimer(final L1PcInstance pc) { _pc = pc; }
     */

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
                if (tgpc.getParty() != null) {
                    rp(tgpc);
                    Thread.sleep(1);
                }
            }

        } catch (final Exception e) {
            _log.error("队伍更新时间轴异常重启", e);
            PcOtherThreadPool.get().cancel(_timer, false);
            final PartyTimer partyTimer = new PartyTimer();
            partyTimer.start();
        }
        /*
         * try { if (_pc.getParty() == null) { _pc.stopRP(); return; } rp(); }
         * catch (final Exception e) { _pc.stopRP();
         * _log.error(e.getLocalizedMessage(), e); }
         */
    }

    /**
     * 队伍更新封包发送
     */
    private void rp(final L1PcInstance pc) {
        try {
            pc.sendPackets(new S_PacketBoxParty(pc.getParty(), pc));

        } catch (final Exception e) {
        }
    }
}
