package com.lineage.data.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.templates.L1Event;
import com.lineage.server.timecontroller.event.ranking.RankingClanTimer;
import com.lineage.server.timecontroller.event.ranking.RankingHeroTimer;
import com.lineage.server.timecontroller.event.ranking.RankingKillTimer;
import com.lineage.server.timecontroller.event.ranking.RankingWealthTimer;

/**
 * 风云榜系统<BR>
 * 
 * @author dexc
 * 
 */
public class Ranking4 extends EventExecutor {

    private static final Log _log = LogFactory.getLog(Ranking4.class);

    /**
	 *
	 */
    private Ranking4() {
        // TODO Auto-generated constructor stub
    }

    public static EventExecutor get() {
        return new Ranking4();
    }

    @Override
    public void execute(final L1Event event) {
        try {
            // 杀手风云榜时间轴
            final RankingKillTimer killTimer = new RankingKillTimer();
            killTimer.start();

            // 财富风云榜时间轴
            Thread.sleep(500);
            final RankingWealthTimer wealthTimer = new RankingWealthTimer();
            wealthTimer.start();

            // 英雄风云榜时间轴
            Thread.sleep(500);
            final RankingHeroTimer heroTimer = new RankingHeroTimer();
            heroTimer.start();

            // 血盟风云榜时间轴
            Thread.sleep(500);
            final RankingClanTimer clanRTimer = new RankingClanTimer();
            clanRTimer.start();

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
