package com.lineage.server.timecontroller.event.ranking;

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
 * 杀手风云榜时间轴
 * 
 * @author dexc
 * 
 */
public class RankingKillTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(RankingKillTimer.class);

    private ScheduledFuture<?> _timer;

    private static boolean _load = false;

    private static String[] _killName = new String[] { " ", " ", " ", " ", " ",
            " ", " ", " ", " ", " ", };

    private static String[] _deathName = new String[] { " ", " ", " ", " ",
            " ", " ", " ", " ", " ", " ", };

    public void start() {
        final int timeMillis = 600 * 1000;// 10分钟
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    /**
     * 杀手排行榜
     * 
     * @return
     */
    public static String[] killName() {
        if (!_load) {
            load();
        }
        return _killName;
    }

    /**
     * 死者排行榜
     * 
     * @return
     */
    public static String[] deathName() {
        if (!_load) {
            load();
        }
        return _deathName;
    }

    @Override
    public void run() {
        try {
            load();

        } catch (final Exception e) {
            _log.error("杀手风云榜时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final RankingKillTimer killTimer = new RankingKillTimer();
            killTimer.start();

        } finally {
        }
    }

    private static void load() {
        try {
            final Collection<L1PcInstance> allPc = World.get().getAllPlayers();
            // 不包含元素
            if (allPc.isEmpty()) {
                return;
            }
            _load = true;
            // 重置所有排行
            restart();

            for (final Iterator<L1PcInstance> iter = allPc.iterator(); iter
                    .hasNext();) {
                final L1PcInstance tgpc = iter.next();
                if (tgpc == null) {
                    continue;
                }

                if (tgpc.getOnlineStatus() == 0) {
                    continue;
                }

                if (tgpc.getNetConnection() == null) {
                    continue;
                }

                // 杀手排行榜
                final int killCount = tgpc.get_other().get_killCount();
                if (killCount > 0) {
                    _killName = intTree(killCount, tgpc.getName(), _killName);
                }

                // 死者排行榜
                final int deathCount = tgpc.get_other().get_deathCount();
                if (deathCount > 0) {
                    _deathName = intTree(deathCount, tgpc.getName(), _deathName);
                }
                Thread.sleep(1);
            }

            /*
             * for (final L1PcInstance tgpc : allPc) { if (tgpc == null) {
             * continue; }
             * 
             * if (tgpc.getOnlineStatus() == 0) { continue; }
             * 
             * if (tgpc.getNetConnection() == null) { continue; }
             * 
             * // 杀手排行榜 final int killCount = tgpc.get_other().get_killCount();
             * if (killCount > 0) { _killName = intTree(killCount,
             * tgpc.getName(), _killName); }
             * 
             * // 死者排行榜 final int deathCount =
             * tgpc.get_other().get_deathCount(); if (deathCount > 0) {
             * _deathName = intTree(deathCount, tgpc.getName(), _deathName); }
             * Thread.sleep(1); }
             */

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 重置所有排行
     */
    private static void restart() {
        _killName = new String[] { " ", " ", " ", " ", " ", " ", " ", " ", " ",
                " ", };

        _deathName = new String[] { " ", " ", " ", " ", " ", " ", " ", " ",
                " ", " ", };
    }

    /**
     * @param killCount
     * @param name
     * @param userName
     * @return
     */
    private static String[] intTree(final int killCount, final String name,
            final String[] userName) {
        if (userName[0].equals(" ")) {// NO 1
            userName[0] = name + "," + killCount;
            return userName;

        } else {
            final String[] set = userName[0].split(",");
            final int srcCount = Integer.parseInt(set[1]);
            if (srcCount < killCount && !set[0].equals(name)) {
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = userName[4];
                userName[4] = userName[3];
                userName[3] = userName[2];
                userName[2] = userName[1];
                userName[1] = userName[0];
                userName[0] = name + "," + killCount;
                return userName;
            }
        }

        if (userName[1].equals(" ")) {// NO 2
            userName[1] = name + "," + killCount;
            return userName;

        } else {
            final String[] set = userName[1].split(",");
            final int srcCount = Integer.parseInt(set[1]);
            if (srcCount < killCount && !set[0].equals(name)) {
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = userName[4];
                userName[4] = userName[3];
                userName[3] = userName[2];
                userName[2] = userName[1];
                userName[1] = name + "," + killCount;
                return userName;
            }
        }

        if (userName[2].equals(" ")) {// NO 3
            userName[2] = name + "," + killCount;
            return userName;

        } else {
            final String[] set = userName[2].split(",");
            final int srcCount = Integer.parseInt(set[1]);
            if (srcCount < killCount && !set[0].equals(name)) {
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = userName[4];
                userName[4] = userName[3];
                userName[3] = userName[2];
                userName[2] = name + "," + killCount;
                return userName;
            }
        }

        if (userName[3].equals(" ")) {// NO 4
            userName[3] = name + "," + killCount;
            return userName;

        } else {
            final String[] set = userName[3].split(",");
            final int srcCount = Integer.parseInt(set[1]);
            if (srcCount < killCount && !set[0].equals(name)) {
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = userName[4];
                userName[4] = userName[3];
                userName[3] = name + "," + killCount;
                return userName;
            }
        }

        if (userName[4].equals(" ")) {// NO 5
            userName[4] = name + "," + killCount;
            return userName;

        } else {
            final String[] set = userName[4].split(",");
            final int srcCount = Integer.parseInt(set[1]);
            if (srcCount < killCount && !set[0].equals(name)) {
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = userName[4];
                userName[4] = name + "," + killCount;
                return userName;
            }
        }

        if (userName[5].equals(" ")) {// NO 6
            userName[5] = name + "," + killCount;
            return userName;

        } else {
            final String[] set = userName[5].split(",");
            final int srcCount = Integer.parseInt(set[1]);
            if (srcCount < killCount && !set[0].equals(name)) {
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = name + "," + killCount;
                return userName;
            }
        }

        if (userName[6].equals(" ")) {// NO 7
            userName[6] = name + "," + killCount;
            return userName;

        } else {
            final String[] set = userName[6].split(",");
            final int srcCount = Integer.parseInt(set[1]);
            if (srcCount < killCount && !set[0].equals(name)) {
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = name + "," + killCount;
                return userName;
            }
        }

        if (userName[7].equals(" ")) {// NO 8
            userName[7] = name + "," + killCount;
            return userName;

        } else {
            final String[] set = userName[7].split(",");
            final int srcCount = Integer.parseInt(set[1]);
            if (srcCount < killCount && !set[0].equals(name)) {
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = name + "," + killCount;
                return userName;
            }
        }

        if (userName[8].equals(" ")) {// NO 9
            userName[8] = name + "," + killCount;
            return userName;

        } else {
            final String[] set = userName[8].split(",");
            final int srcCount = Integer.parseInt(set[1]);
            if (srcCount < killCount && !set[0].equals(name)) {
                userName[9] = userName[8];
                userName[8] = name + "," + killCount;
                return userName;
            }
        }

        if (userName[9].equals(" ")) {// NO 10
            userName[9] = name + "," + killCount;
            return userName;

        } else {
            final String[] set = userName[9].split(",");
            final int srcCount = Integer.parseInt(set[1]);
            if (srcCount < killCount && !set[0].equals(name)) {
                userName[9] = name + "," + killCount;
                return userName;
            }
        }
        return userName;
    }

}
