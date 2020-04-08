package com.lineage.server.timecontroller.event;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.model.L1Clan;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldClan;

/**
 * 血盟技能计时时间轴
 * 
 * @author dexc
 * 
 */
public class ClanSkillTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(ClanSkillTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 60 * 60 * 1000;// 1小时
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            final Collection<L1Clan> allClan = WorldClan.get().getAllClans();
            // 不包含元素
            if (allClan.isEmpty()) {
                return;
            }

            for (final Iterator<L1Clan> iter = allClan.iterator(); iter
                    .hasNext();) {
                final L1Clan clan = iter.next();
                // 不具备血盟技能
                if (!clan.isClanskill()) {
                    continue;
                }
                // 取回血盟技能时间
                final Timestamp skilltime = clan.get_skilltime();
                if (skilltime == null) {
                    // 移出清单
                    clan.set_clanskill(false);
                    continue;
                }
                // 目前时间
                final Timestamp ts = new Timestamp(System.currentTimeMillis());
                // 指示此 skilltime 对象是否早于给定的 ts 对象。
                if (skilltime.before(ts)) {
                    // 移出清单
                    clan.set_clanskill(false);
                    clan.set_skilltime(null);

                    // 更新血盟资料
                    ClanReading.get().updateClan(clan);
                }
                Thread.sleep(1);
            }

            /*
             * for (final L1Clan clan : allClan) { // 不具备血盟技能 if
             * (!clan.isClanskill()) { continue; } // 取回血盟技能时间 final Timestamp
             * skilltime = clan.get_skilltime(); if (skilltime == null) { //
             * 移出清单 clan.set_clanskill(false); continue; } // 目前时间 final
             * Timestamp ts = new Timestamp(System.currentTimeMillis()); // 指示此
             * skilltime 对象是否早于给定的 ts 对象。 if (skilltime.before(ts)) { // 移出清单
             * clan.set_clanskill(false); clan.set_skilltime(null);
             * 
             * // 更新血盟资料 ClanReading.get().updateClan(clan); } Thread.sleep(1);
             * }
             */

        } catch (final Exception e) {
            _log.error("血盟技能计时时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final ClanSkillTimer skillTimer = new ClanSkillTimer();
            skillTimer.start();
        }
    }

}
