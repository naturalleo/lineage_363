package com.lineage.server.timecontroller.pc;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.thread.PcOtherThreadPool;
import com.lineage.server.world.World;

/**
 * PC EXP更新处理 时间轴
 * 
 * @author dexc
 * 
 */
public class ExpTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(ExpTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 500;
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
                if (check(tgpc)) {
                    tgpc.onChangeExp();
                    Thread.sleep(1);
                }
                if (tgpc.hasSkillEffect(1010) && tgpc.getInventory().consumeItem(40895, 1)) {
                	final L1SkillUse l1skilluse = new L1SkillUse();
                    l1skilluse.handleCommands(tgpc, 37, tgpc.getId(), tgpc.getX(),
                            tgpc.getY(), 0, L1SkillUse.TYPE_SPELLSC);
                }
            }

            /*
             * for (final L1PcInstance tgpc : all) { if (check(tgpc)) {
             * tgpc.onChangeExp(); Thread.sleep(1); } }
             */

        } catch (final Exception e) {
            _log.error("EXP更新处理时间轴异常重启", e);
            PcOtherThreadPool.get().cancel(_timer, false);
            final ExpTimer expTimer = new ExpTimer();
            expTimer.start();
        }
    }

    /**
     * 判断
     * 
     * @param tgpc
     * @return true:执行 false:不执行
     */
    private static boolean check(L1PcInstance tgpc) {
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

            if (tgpc.getExp() == tgpc.getExpo()) {
                return false;
            }

        } catch (final Exception e) {
            return false;
        }
        return true;
    }
}
