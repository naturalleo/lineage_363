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
 * 英雄风云榜时间轴
 * 
 * @author dexc
 * 
 */
public class RankingHeroTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(RankingHeroTimer.class);

    private ScheduledFuture<?> _timer;

    private static boolean _load = false;

    private static String[] _userNameAll;

    private static String[] _userNameC;

    private static String[] _userNameK;

    private static String[] _userNameE;

    private static String[] _userNameW;

    private static String[] _userNameD;

    private static String[] _userNameG;

    private static String[] _userNameI;

    public void start() {
        restart();
        final int timeMillis = 600 * 1000;// 10分钟
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    /**
     * 全职业风云榜
     * 
     * @return
     */
    public static String[] userNameAll() {
        if (!_load) {
            load();
        }
        String[] newUserName = new String[10];
        for (int i = 0; i < 10; i++) {
            final String[] set = _userNameAll[i].split(",");
            newUserName[i] = set[0];
        }
        return newUserName;
    }

    /**
     * 王族风云榜
     * 
     * @return
     */
    public static String[] userNameC() {
        if (!_load) {
            load();
        }
        String[] newUserName = new String[10];
        for (int i = 0; i < 10; i++) {
            final String[] set = _userNameC[i].split(",");
            newUserName[i] = set[0];
        }
        return newUserName;
    }

    /**
     * 骑士风云榜
     * 
     * @return
     */
    public static String[] userNameK() {
        if (!_load) {
            load();
        }
        String[] newUserName = new String[10];
        for (int i = 0; i < 10; i++) {
            final String[] set = _userNameK[i].split(",");
            newUserName[i] = set[0];
        }
        return newUserName;
    }

    /**
     * 精灵风云榜
     * 
     * @return
     */
    public static String[] userNameE() {
        if (!_load) {
            load();
        }
        String[] newUserName = new String[10];
        for (int i = 0; i < 10; i++) {
            final String[] set = _userNameE[i].split(",");
            newUserName[i] = set[0];
        }
        return newUserName;
    }

    /**
     * 法师风云榜
     * 
     * @return
     */
    public static String[] userNameW() {
        if (!_load) {
            load();
        }
        String[] newUserName = new String[10];
        for (int i = 0; i < 10; i++) {
            final String[] set = _userNameW[i].split(",");
            newUserName[i] = set[0];
        }
        return newUserName;
    }

    /**
     * 黑妖风云榜
     * 
     * @return
     */
    public static String[] userNameD() {
        if (!_load) {
            load();
        }
        String[] newUserName = new String[10];
        for (int i = 0; i < 10; i++) {
            final String[] set = _userNameD[i].split(",");
            newUserName[i] = set[0];
        }
        return newUserName;
    }

    /**
     * 龙骑风云榜
     * 
     * @return
     */
    public static String[] userNameG() {
        if (!_load) {
            load();
        }
        String[] newUserName = new String[10];
        for (int i = 0; i < 10; i++) {
            final String[] set = _userNameG[i].split(",");
            newUserName[i] = set[0];
        }
        return newUserName;
    }

    /**
     * 幻术风云榜
     * 
     * @return
     */
    public static String[] userNameI() {
        if (!_load) {
            load();
        }
        String[] newUserName = new String[10];
        for (int i = 0; i < 10; i++) {
            final String[] set = _userNameI[i].split(",");
            newUserName[i] = set[0];
        }
        return newUserName;
    }

    @Override
    public void run() {
        try {
            load();

        } catch (final Exception e) {
            _log.error("英雄风云榜时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final RankingHeroTimer heroTimer = new RankingHeroTimer();
            heroTimer.start();

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

                if (tgpc.isGm()) {
                    continue;
                }

                // 取回等级
                final int count = tgpc.getLevel();
                final long pcexp = tgpc.getExp();
                if (count > 0) {
                    if (tgpc.isCrown()) {
                        _userNameC = intTree(pcexp, tgpc.getName() + " LV" + count, _userNameC);

                    } else if (tgpc.isKnight()) {
                        _userNameK = intTree(pcexp, tgpc.getName() + " LV" + count, _userNameK);

                    } else if (tgpc.isElf()) {
                        _userNameE = intTree(pcexp, tgpc.getName() + " LV" + count, _userNameE);

                    } else if (tgpc.isWizard()) {
                        _userNameW = intTree(pcexp, tgpc.getName() + " LV" + count, _userNameW);

                    } else if (tgpc.isDarkelf()) {
                        _userNameD = intTree(pcexp, tgpc.getName() + " LV" + count, _userNameD);

                    } else if (tgpc.isDragonKnight()) {
                        _userNameG = intTree(pcexp, tgpc.getName() + " LV" + count, _userNameG);

                    } else if (tgpc.isIllusionist()) {
                        _userNameI = intTree(pcexp, tgpc.getName() + " LV" + count, _userNameI);

                    }
                    _userNameAll = intTree(pcexp, tgpc.getName() + " LV" + count, _userNameAll);
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
             * if (tgpc.isGm()) { continue; }
             * 
             * // 取回等级 final int count = tgpc.getLevel(); if (count > 0) { if
             * (tgpc.isCrown()) { _userNameC = intTree(count, tgpc.getName(),
             * _userNameC);
             * 
             * } else if (tgpc.isKnight()) { _userNameK = intTree(count,
             * tgpc.getName(), _userNameK);
             * 
             * } else if (tgpc.isElf()) { _userNameE = intTree(count,
             * tgpc.getName(), _userNameE);
             * 
             * } else if (tgpc.isWizard()) { _userNameW = intTree(count,
             * tgpc.getName(), _userNameW);
             * 
             * } else if (tgpc.isDarkelf()) { _userNameD = intTree(count,
             * tgpc.getName(), _userNameD);
             * 
             * } else if (tgpc.isDragonKnight()) { _userNameG = intTree(count,
             * tgpc.getName(), _userNameG);
             * 
             * } else if (tgpc.isIllusionist()) { _userNameI = intTree(count,
             * tgpc.getName(), _userNameI);
             * 
             * } _userNameAll = intTree(count, tgpc.getName(), _userNameAll); }
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
        _userNameAll = new String[] { " ", " ", " ", " ", " ", " ", " ", " ",
                " ", " ", };

        _userNameC = new String[] { " ", " ", " ", " ", " ", " ", " ", " ",
                " ", " ", };

        _userNameK = new String[] { " ", " ", " ", " ", " ", " ", " ", " ",
                " ", " ", };

        _userNameE = new String[] { " ", " ", " ", " ", " ", " ", " ", " ",
                " ", " ", };

        _userNameW = new String[] { " ", " ", " ", " ", " ", " ", " ", " ",
                " ", " ", };

        _userNameD = new String[] { " ", " ", " ", " ", " ", " ", " ", " ",
                " ", " ", };

        _userNameG = new String[] { " ", " ", " ", " ", " ", " ", " ", " ",
                " ", " ", };

        _userNameI = new String[] { " ", " ", " ", " ", " ", " ", " ", " ",
                " ", " ", };

    }

    /**
     * @param level
     * @param name
     * @param userName
     * @return
     */
    private static String[] intTree(final long pcexp, final String name,
            final String[] userName) {
        if (userName[0].equals(" ")) {// NO 1
            userName[0] = name + "," + pcexp;
            return userName;

        } else {
            final String[] set = userName[0].split(",");
            final int srcLevel = Integer.parseInt(set[1]);
            if (srcLevel < pcexp && !set[0].equals(name)) {
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = userName[4];
                userName[4] = userName[3];
                userName[3] = userName[2];
                userName[2] = userName[1];
                userName[1] = userName[0];
                userName[0] = name + "," + pcexp;
                return userName;
            }
        }

        if (userName[1].equals(" ")) {// NO 2
            userName[1] = name + "," + pcexp;
            return userName;

        } else {
            final String[] set = userName[1].split(",");
            final int srcLevel = Integer.parseInt(set[1]);
            if (srcLevel < pcexp && !set[0].equals(name)) {
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = userName[4];
                userName[4] = userName[3];
                userName[3] = userName[2];
                userName[2] = userName[1];
                userName[1] = name + "," + pcexp;
                return userName;
            }
        }

        if (userName[2].equals(" ")) {// NO 3
            userName[2] = name + "," + pcexp;
            return userName;

        } else {
            final String[] set = userName[2].split(",");
            final int srcLevel = Integer.parseInt(set[1]);
            if (srcLevel < pcexp && !set[0].equals(name)) {
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = userName[4];
                userName[4] = userName[3];
                userName[3] = userName[2];
                userName[2] = name + "," + pcexp;
                return userName;
            }
        }

        if (userName[3].equals(" ")) {// NO 4
            userName[3] = name + "," + pcexp;
            return userName;

        } else {
            final String[] set = userName[3].split(",");
            final int srcLevel = Integer.parseInt(set[1]);
            if (srcLevel < pcexp && !set[0].equals(name)) {
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = userName[4];
                userName[4] = userName[3];
                userName[3] = name + "," + pcexp;
                return userName;
            }
        }

        if (userName[4].equals(" ")) {// NO 5
            userName[4] = name + "," + pcexp;
            return userName;

        } else {
            final String[] set = userName[4].split(",");
            final int srcLevel = Integer.parseInt(set[1]);
            if (srcLevel < pcexp && !set[0].equals(name)) {
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = userName[4];
                userName[4] = name + "," + pcexp;
                return userName;
            }
        }

        if (userName[5].equals(" ")) {// NO 6
            userName[5] = name + "," + pcexp;
            return userName;

        } else {
            final String[] set = userName[5].split(",");
            final int srcLevel = Integer.parseInt(set[1]);
            if (srcLevel < pcexp && !set[0].equals(name)) {
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = userName[5];
                userName[5] = name + "," + pcexp;
                return userName;
            }
        }

        if (userName[6].equals(" ")) {// NO 7
            userName[6] = name + "," + pcexp;
            return userName;

        } else {
            final String[] set = userName[6].split(",");
            final int srcLevel = Integer.parseInt(set[1]);
            if (srcLevel < pcexp && !set[0].equals(name)) {
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = userName[6];
                userName[6] = name + "," + pcexp;
                return userName;
            }
        }

        if (userName[7].equals(" ")) {// NO 8
            userName[7] = name + "," + pcexp;
            return userName;

        } else {
            final String[] set = userName[7].split(",");
            final int srcLevel = Integer.parseInt(set[1]);
            if (srcLevel < pcexp && !set[0].equals(name)) {
                userName[9] = userName[8];
                userName[8] = userName[7];
                userName[7] = name + "," + pcexp;
                return userName;
            }
        }

        if (userName[8].equals(" ")) {// NO 9
            userName[8] = name + "," + pcexp;
            return userName;

        } else {
            final String[] set = userName[8].split(",");
            final int srcLevel = Integer.parseInt(set[1]);
            if (srcLevel < pcexp && !set[0].equals(name)) {
                userName[9] = userName[8];
                userName[8] = name + "," + pcexp;
                return userName;
            }
        }

        if (userName[9].equals(" ")) {// NO 10
            userName[9] = name + "," + pcexp;
            return userName;

        } else {
            final String[] set = userName[9].split(",");
            final int srcLevel = Integer.parseInt(set[1]);
            if (srcLevel < pcexp && !set[0].equals(name)) {
                userName[9] = name + "," + pcexp;
                return userName;
            }
        }
        return userName;
    }

}
