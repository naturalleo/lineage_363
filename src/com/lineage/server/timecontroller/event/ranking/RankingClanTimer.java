package com.lineage.server.timecontroller.event.ranking;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Clan;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldClan;

/**
 * 血盟风云榜时间轴
 * 
 * @author dexc
 * 
 */
public class RankingClanTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(RankingClanTimer.class);

    private ScheduledFuture<?> _timer;

    private static boolean _load = false;

    private static String[] _clanName = new String[] { " ", " ", " ", " ", " ",
            " ", " ", " ", " ", " ", };

    public void start() {
        final int timeMillis = 600 * 1000;// 10分钟
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    /**
     * 血盟风云榜
     * 
     * @return
     */
    public static String[] userName() {
        if (!_load) {
            load();
        }
        return _clanName;
    }

    @Override
    public void run() {
        try {
            load();

        } catch (final Exception e) {
            _log.error("血盟风云榜时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final RankingClanTimer clanRTimer = new RankingClanTimer();
            clanRTimer.start();

        } finally {
        }
    }

    private static void load() {
        try {
            final Collection<L1Clan> allClan = WorldClan.get().getAllClans();
            // 不包含元素
            if (allClan.isEmpty()) {
                return;
            }
            _load = true;
            // 重置所有排行
            restart();

            for (final Iterator<L1Clan> iter = allClan.iterator(); iter
                    .hasNext();) {
                final L1Clan clan = iter.next();
                final String clanName = clan.getClanName();
                final int count = clan.getOnlineClanMemberSize();
                _clanName = intTree(count, clanName, _clanName);
                Thread.sleep(1);
            }

            /*
             * for (final L1Clan clan : allClan) { String clanName =
             * clan.getClanName(); int count = clan.getOnlineClanMemberSize();
             * _clanName = intTree(count, clanName, _clanName); Thread.sleep(1);
             * }
             */

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 重置所有排行
     */
    private static void restart() {
        _clanName = new String[] { " ", " ", " ", " ", " ", " ", " ", " ", " ",
                " ", };
    }

    /**
     * @param level
     * @param name
     * @param userName
     * @return
     */
    private static String[] intTree(final int count, final String clanName,
            final String[] userName) {
        if (userName[0].equals(" ")) {// NO 1
            // 名次为空直接加入
            userName[0] = clanName + "," + count;
            return userName;

        } else {
            final String[] set = userName[0].split(",");
            final int srcCount = Integer.parseInt(set[1]);
            if (srcCount < count && !set[0].equals(clanName)) {
                // 名次不为空 判断成立排名依续降低
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = userName[4];
                userName[4] = userName[3];
                userName[3] = userName[2];
                userName[2] = userName[1];
                userName[1] = userName[0];
                userName[0] = clanName + "," + count;
                return userName;
            }
        }

        if (userName[1].equals(" ")) {// NO 2
            // 名次为空直接加入
            userName[1] = clanName + "," + count;
            return userName;

        } else {
            final String[] set = userName[1].split(",");
            final int srcCount = Integer.parseInt(set[1]);
            if (srcCount < count && !set[0].equals(clanName)) {
                // 名次不为空 判断成立排名依续降低
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = userName[4];
                userName[4] = userName[3];
                userName[3] = userName[2];
                userName[2] = userName[1];
                userName[1] = clanName + "," + count;
                return userName;
            }
        }

        if (userName[2].equals(" ")) {// NO 3
            // 名次为空直接加入
            userName[2] = clanName + "," + count;
            return userName;

        } else {
            final String[] set = userName[2].split(",");
            final int srcCount = Integer.parseInt(set[1]);
            if (srcCount < count && !set[0].equals(clanName)) {
                // 名次不为空 判断成立排名依续降低
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = userName[4];
                userName[4] = userName[3];
                userName[3] = userName[2];
                userName[2] = clanName + "," + count;
                return userName;
            }
        }

        if (userName[3].equals(" ")) {// NO 4
            // 名次为空直接加入
            userName[3] = clanName + "," + count;
            return userName;

        } else {
            final String[] set = userName[3].split(",");
            final int srcCount = Integer.parseInt(set[1]);
            if (srcCount < count && !set[0].equals(clanName)) {
                // 名次不为空 判断成立排名依续降低
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = userName[4];
                userName[4] = userName[3];
                userName[3] = clanName + "," + count;
                return userName;
            }
        }

        if (userName[4].equals(" ")) {// NO 5
            // 名次为空直接加入
            userName[4] = clanName + "," + count;
            return userName;

        } else {
            final String[] set = userName[4].split(",");
            final int srcCount = Integer.parseInt(set[1]);
            if (srcCount < count && !set[0].equals(clanName)) {
                // 名次不为空 判断成立排名依续降低
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = userName[4];
                userName[4] = clanName + "," + count;
                return userName;
            }
        }

        if (userName[5].equals(" ")) {// NO 6
            // 名次为空直接加入
            userName[5] = clanName + "," + count;
            return userName;

        } else {
            final String[] set = userName[5].split(",");
            final int srcCount = Integer.parseInt(set[1]);
            if (srcCount < count && !set[0].equals(clanName)) {
                // 名次不为空 判断成立排名依续降低
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = clanName + "," + count;
                return userName;
            }
        }

        if (userName[6].equals(" ")) {// NO 7
            // 名次为空直接加入
            userName[6] = clanName + "," + count;
            return userName;

        } else {
            final String[] set = userName[6].split(",");
            final int srcCount = Integer.parseInt(set[1]);
            if (srcCount < count && !set[0].equals(clanName)) {
                // 名次不为空 判断成立排名依续降低
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = clanName + "," + count;
                return userName;
            }
        }

        if (userName[7].equals(" ")) {// NO 8
            // 名次为空直接加入
            userName[7] = clanName + "," + count;
            return userName;

        } else {
            final String[] set = userName[7].split(",");
            final int srcCount = Integer.parseInt(set[1]);
            if (srcCount < count && !set[0].equals(clanName)) {
                // 名次不为空 判断成立排名依续降低
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = clanName + "," + count;
                return userName;
            }
        }

        if (userName[8].equals(" ")) {// NO 9
            // 名次为空直接加入
            userName[8] = clanName + "," + count;
            return userName;

        } else {
            final String[] set = userName[8].split(",");
            final int srcCount = Integer.parseInt(set[1]);
            if (srcCount < count && !set[0].equals(clanName)) {
                // 名次不为空 判断成立排名依续降低
                userName[9] = userName[8];
                userName[8] = clanName + "," + count;
                return userName;
            }
        }

        if (userName[9].equals(" ")) {// NO 10
            // 名次为空直接加入
            userName[9] = clanName + "," + count;
            return userName;

        } else {
            final String[] set = userName[9].split(",");
            final int srcCount = Integer.parseInt(set[1]);
            if (srcCount < count && !set[0].equals(clanName)) {
                // 名次不为空 判断成立排名依续降低
                userName[9] = clanName + "," + count;
                return userName;
            }
        }
        return userName;
    }
}
