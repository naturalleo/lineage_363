package com.lineage.server.timecontroller.pc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.lock.BuddyReading;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1BuddyTmp;
import com.lineage.server.thread.PcOtherThreadPool;
import com.lineage.server.world.World;

/**
 * 新增玩家定时执行任务(添加 hjx1000)
 * 
 * @author hjx1000
 * 
 */
public class Players_timing extends TimerTask {

    private static final Log _log = LogFactory.getLog(Players_timing.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 1100;// 1秒
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
                //新增外挂功能，遇到好友名单的玩家时 当前地图可以瞬移的时自动瞬移 hjx1000
                if (tgpc.isAiRunning() && tgpc.getMap().isTeleportable() && !tgpc.isParalyzedX()) { //当前如果在挂机  并且在可瞬移地图
                    final ArrayList<L1BuddyTmp> list = BuddyReading.get().userBuddy(
                    		tgpc.getId());
                    if (list != null) {
                    	for (final L1BuddyTmp BuddyTmp : list) {
                    		int charid = 0;
                            final Collection<L1PcInstance> otherPc = World.get()
                                    .getVisiblePlayer(tgpc, 10);
                            if (otherPc.size() <= 0) {
                            	break;
                            }
                            for (final L1PcInstance tg : otherPc) {
                            	charid = tg.getId();
                            }
                            if (BuddyTmp.get_buddy_id() == charid) {
                        		if (tgpc.getInventory().consumeItem(58026, 1)) { //地图可瞬移身上有白瞬卷 hjx1000
                            		L1Teleport.randomTeleport(tgpc, true);
                            		break;
                            	}
                            }
                    	}
                    }
                }
                //新增外挂功能，遇到好友名单的玩家时 当前地图可以瞬移的时自动瞬移 hjx1000
            }

        } catch (final Exception e) {
            _log.error("新增玩家定时执行任务出错", e);
            PcOtherThreadPool.get().cancel(_timer, false);
            final Players_timing playersTiming = new Players_timing();
            playersTiming.start();
        }
    }

}
