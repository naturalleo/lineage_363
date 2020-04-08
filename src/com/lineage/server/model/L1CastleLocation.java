package com.lineage.server.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.lock.CastleReading;
import com.lineage.server.model.gametime.L1GameTime;
import com.lineage.server.model.gametime.L1GameTimeAdapter;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.templates.L1Castle;

/**
 * 城堡数据
 * 
 * @author dexc
 * 
 */
public class L1CastleLocation {

    private static final Log _log = LogFactory.getLog(L1CastleLocation.class);

    // castle_id
    public static final int KENT_CASTLE_ID = 1;// 肯特城

    public static final int OT_CASTLE_ID = 2;// 妖魔城

    public static final int WW_CASTLE_ID = 3;// 风木城

    public static final int GIRAN_CASTLE_ID = 4;// 奇岩城

    public static final int HEINE_CASTLE_ID = 5;// 海音城

    public static final int DOWA_CASTLE_ID = 6;// 侏儒城

    public static final int ADEN_CASTLE_ID = 7;// 亚丁城

    public static final int DIAD_CASTLE_ID = 8;// 狄亚得要塞

    // →↑がX轴、→↓がY轴
    // TODO 肯特城
    private static final int KENT_TOWER_X = 33139;

    private static final int KENT_TOWER_Y = 32768;

    private static final short KENT_TOWER_MAP = 4;

    private static final int KENT_X1 = 33089;

    private static final int KENT_X2 = 33219;

    private static final int KENT_Y1 = 32717;

    private static final int KENT_Y2 = 32827;

    private static final short KENT_MAP = 4;

    private static final short KENT_INNER_CASTLE_MAP = 15;

    // TODO 妖魔城
    private static final int OT_TOWER_X = 32798;

    private static final int OT_TOWER_Y = 32291;

    private static final short OT_TOWER_MAP = 4;

    private static final int OT_X1 = 32750;

    private static final int OT_X2 = 32850;

    private static final int OT_Y1 = 32250;

    private static final int OT_Y2 = 32350;

    private static final short OT_MAP = 4;

    // TODO 风木城
    private static final int WW_TOWER_X = 32623;

    private static final int WW_TOWER_Y = 33379;

    private static final short WW_TOWER_MAP = 4;

    private static final int WW_X1 = 32571;

    private static final int WW_X2 = 32721;

    private static final int WW_Y1 = 33350;

    private static final int WW_Y2 = 33460;

    private static final short WW_MAP = 4;

    private static final short WW_INNER_CASTLE_MAP = 29;

    // TODO 奇岩城
    private static final int GIRAN_TOWER_X = 33631;

    private static final int GIRAN_TOWER_Y = 32678;

    private static final short GIRAN_TOWER_MAP = 4;

    private static final int GIRAN_X1 = 33559;

    private static final int GIRAN_X2 = 33686;

    private static final int GIRAN_Y1 = 32615;

    private static final int GIRAN_Y2 = 32755;

    private static final short GIRAN_MAP = 4;

    private static final short GIRAN_INNER_CASTLE_MAP = 52;

    // TODO 海音城
    private static final int HEINE_TOWER_X = 33524;

    private static final int HEINE_TOWER_Y = 33396;

    private static final short HEINE_TOWER_MAP = 4;

    private static final int HEINE_X1 = 33458;

    private static final int HEINE_X2 = 33583;

    private static final int HEINE_Y1 = 33315;

    private static final int HEINE_Y2 = 33490;

    private static final short HEINE_MAP = 4;

    private static final short HEINE_INNER_CASTLE_MAP = 64;

    // TODO 侏儒城
    private static final int DOWA_TOWER_X = 32828;

    private static final int DOWA_TOWER_Y = 32818;

    private static final short DOWA_TOWER_MAP = 66;

    private static final int DOWA_X1 = 32755;

    private static final int DOWA_X2 = 32870;

    private static final int DOWA_Y1 = 32790;

    private static final int DOWA_Y2 = 32920;

    private static final short DOWA_MAP = 66;

    // TODO 亚丁城
    private static final int ADEN_TOWER_X = 34090;

    private static final int ADEN_TOWER_Y = 33260;

    private static final short ADEN_TOWER_MAP = 4;

    private static final int ADEN_X1 = 34007;

    private static final int ADEN_X2 = 34162;

    private static final int ADEN_Y1 = 33172;

    private static final int ADEN_Y2 = 33332;

    private static final short ADEN_MAP = 4;

    private static final short ADEN_INNER_CASTLE_MAP = 300;

    private static final int ADEN_SUB_TOWER1_X = 34057; // 青

    private static final int ADEN_SUB_TOWER1_Y = 33291;

    private static final int ADEN_SUB_TOWER2_X = 34123; // 赤

    private static final int ADEN_SUB_TOWER2_Y = 33291;

    private static final int ADEN_SUB_TOWER3_X = 34057; // 绿

    private static final int ADEN_SUB_TOWER3_Y = 33230;

    private static final int ADEN_SUB_TOWER4_X = 34123; // 白

    private static final int ADEN_SUB_TOWER4_Y = 33230;

    // TODO 狄亚得要塞
    private static final int DIAD_TOWER_X = 33033;

    private static final int DIAD_TOWER_Y = 32895;

    private static final short DIAD_TOWER_MAP = 320;

    private static final int DIAD_X1 = 32888;

    private static final int DIAD_X2 = 33070;

    private static final int DIAD_Y1 = 32839;

    private static final int DIAD_Y2 = 32953;

    private static final short DIAD_MAP = 320;

    private static final short DIAD_INNER_CASTLE_MAP = 330;

    private static final Map<Integer, L1Location> _towers = new HashMap<Integer, L1Location>();

    private static final Map<Integer, L1MapArea> _areas = new HashMap<Integer, L1MapArea>();

    private static final Map<Integer, Integer> _innerTowerMaps = new HashMap<Integer, Integer>();

    private static final Map<Integer, L1Location> _subTowers = new HashMap<Integer, L1Location>();

    private static final Map<Integer, L1Clan> _isCastle = new HashMap<Integer, L1Clan>();

    static {
        _towers.put(KENT_CASTLE_ID, new L1Location(KENT_TOWER_X, KENT_TOWER_Y,
                KENT_TOWER_MAP));
        _towers.put(OT_CASTLE_ID, new L1Location(OT_TOWER_X, OT_TOWER_Y,
                OT_TOWER_MAP));
        _towers.put(WW_CASTLE_ID, new L1Location(WW_TOWER_X, WW_TOWER_Y,
                WW_TOWER_MAP));
        _towers.put(GIRAN_CASTLE_ID, new L1Location(GIRAN_TOWER_X,
                GIRAN_TOWER_Y, GIRAN_TOWER_MAP));
        _towers.put(HEINE_CASTLE_ID, new L1Location(HEINE_TOWER_X,
                HEINE_TOWER_Y, HEINE_TOWER_MAP));
        _towers.put(DOWA_CASTLE_ID, new L1Location(DOWA_TOWER_X, DOWA_TOWER_Y,
                DOWA_TOWER_MAP));
        _towers.put(ADEN_CASTLE_ID, new L1Location(ADEN_TOWER_X, ADEN_TOWER_Y,
                ADEN_TOWER_MAP));
        _towers.put(DIAD_CASTLE_ID, new L1Location(DIAD_TOWER_X, DIAD_TOWER_Y,
                DIAD_TOWER_MAP));
    }

    static {
        _areas.put(KENT_CASTLE_ID, new L1MapArea(KENT_X1, KENT_Y1, KENT_X2,
                KENT_Y2, KENT_MAP));
        _areas.put(OT_CASTLE_ID, new L1MapArea(OT_X1, OT_Y1, OT_X2, OT_Y2,
                OT_MAP));
        _areas.put(WW_CASTLE_ID, new L1MapArea(WW_X1, WW_Y1, WW_X2, WW_Y2,
                WW_MAP));
        _areas.put(GIRAN_CASTLE_ID, new L1MapArea(GIRAN_X1, GIRAN_Y1, GIRAN_X2,
                GIRAN_Y2, GIRAN_MAP));
        _areas.put(HEINE_CASTLE_ID, new L1MapArea(HEINE_X1, HEINE_Y1, HEINE_X2,
                HEINE_Y2, HEINE_MAP));
        _areas.put(DOWA_CASTLE_ID, new L1MapArea(DOWA_X1, DOWA_Y1, DOWA_X2,
                DOWA_Y2, DOWA_MAP));
        _areas.put(ADEN_CASTLE_ID, new L1MapArea(ADEN_X1, ADEN_Y1, ADEN_X2,
                ADEN_Y2, ADEN_MAP));
        _areas.put(DIAD_CASTLE_ID, new L1MapArea(DIAD_X1, DIAD_Y1, DIAD_X2,
                DIAD_Y2, DIAD_MAP));
    }

    static {
        _innerTowerMaps.put(KENT_CASTLE_ID, (int) KENT_INNER_CASTLE_MAP);
        _innerTowerMaps.put(WW_CASTLE_ID, (int) WW_INNER_CASTLE_MAP);
        _innerTowerMaps.put(GIRAN_CASTLE_ID, (int) GIRAN_INNER_CASTLE_MAP);
        _innerTowerMaps.put(HEINE_CASTLE_ID, (int) HEINE_INNER_CASTLE_MAP);
        _innerTowerMaps.put(ADEN_CASTLE_ID, (int) ADEN_INNER_CASTLE_MAP);
        _innerTowerMaps.put(DIAD_CASTLE_ID, (int) DIAD_INNER_CASTLE_MAP);
    }

    static {
        _subTowers.put(1, new L1Location(ADEN_SUB_TOWER1_X, ADEN_SUB_TOWER1_Y,
                ADEN_TOWER_MAP));
        _subTowers.put(2, new L1Location(ADEN_SUB_TOWER2_X, ADEN_SUB_TOWER2_Y,
                ADEN_TOWER_MAP));
        _subTowers.put(3, new L1Location(ADEN_SUB_TOWER3_X, ADEN_SUB_TOWER3_Y,
                ADEN_TOWER_MAP));
        _subTowers.put(4, new L1Location(ADEN_SUB_TOWER4_X, ADEN_SUB_TOWER4_Y,
                ADEN_TOWER_MAP));
    }

    private L1CastleLocation() {
    }

    /**
     * 世界城堡数据清单
     * 
     * @return
     */
    public static Map<Integer, L1Clan> mapCastle() {
        return _isCastle;
    }

    /**
     * 世界城堡数据清单(该城堡血盟资料)
     * 
     * @param key
     *            城堡编号
     * @return
     */
    public static L1Clan castleClan(final Integer key) {
        return _isCastle.get(key);
    }

    /**
     * 加入世界城堡数据清单
     * 
     * @param key
     *            城堡编号
     * @param value
     *            所属血盟
     */
    public static void putCastle(final Integer key, final L1Clan value) {
        try {
            _isCastle.put(key, value);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 移出世界城堡数据清单
     * 
     * @param key
     *            城堡编号
     */
    public static void removeCastle(final Integer key) {
        try {
            _isCastle.remove(key);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static int getCastleId(final L1Location loc) {
        for (final Map.Entry<Integer, L1Location> entry : _towers.entrySet()) {
            if (entry.getValue().equals(loc)) {
                return entry.getKey();
            }
        }
        return 0;
    }

    /**
     * ガーディアンタワー、クラウンの座标からcastle_idを返す
     */
    public static int getCastleId(final int locx, final int locy,
            final short mapid) {
        return getCastleId(new L1Location(locx, locy, mapid));
    }

    public static int getCastleIdByArea(final L1Location loc) {
        for (final Map.Entry<Integer, L1MapArea> entry : _areas.entrySet()) {
            if (entry.getValue().contains(loc)) {
                return entry.getKey();
            }
        }
        for (final Map.Entry<Integer, Integer> entry : _innerTowerMaps
                .entrySet()) {
            if (entry.getValue() == loc.getMapId()) {
                return entry.getKey();
            }
        }
        return 0;
    }

    /**
     * 战争旗帜内的城堡ID返回
     */
    public static int getCastleIdByArea(final L1Character cha) {
        return getCastleIdByArea(cha.getLocation());
    }

    public static boolean checkInWarArea(final int castleId,
            final L1Location loc) {
        return castleId == getCastleIdByArea(loc);
    }

    /**
     * 指定した城の战争エリア（旗内）にいるか返す
     */
    public static boolean checkInWarArea(final int castleId,
            final L1Character cha) {
        return checkInWarArea(castleId, cha.getLocation());
    }

    public static boolean checkInAllWarArea(final L1Location loc) {
        return 0 != getCastleIdByArea(loc);
    }

    /**
     * いずれかの战争エリア（旗内）かどうかチェック
     */
    public static boolean checkInAllWarArea(final int locx, final int locy,
            final short mapid) {
        return checkInAllWarArea(new L1Location(locx, locy, mapid));
    }

    /**
     * 城堡编号 取回 皇冠召唤座标
     */
    public static int[] getTowerLoc(final int castleId) {
        final int[] result = new int[3];
        final L1Location loc = _towers.get(castleId);
        if (loc != null) {
            result[0] = loc.getX();
            result[1] = loc.getY();
            result[2] = loc.getMapId();
        }
        return result;
    }

    /**
     * castleIdから战争エリア（旗内）の座标を返す
     */
    public static int[] getWarArea(final int castleId) {
        final int[] loc = new int[5];
        switch (castleId) {
            case KENT_CASTLE_ID: // ケント城
                loc[0] = KENT_X1;
                loc[1] = KENT_X2;
                loc[2] = KENT_Y1;
                loc[3] = KENT_Y2;
                loc[4] = KENT_MAP;
                break;

            case OT_CASTLE_ID: // オークの森
                loc[0] = OT_X1;
                loc[1] = OT_X2;
                loc[2] = OT_Y1;
                loc[3] = OT_Y2;
                loc[4] = OT_MAP;
                break;

            case WW_CASTLE_ID: // ウィンダウッド城
                loc[0] = WW_X1;
                loc[1] = WW_X2;
                loc[2] = WW_Y1;
                loc[3] = WW_Y2;
                loc[4] = WW_MAP;
                break;

            case GIRAN_CASTLE_ID: // ギラン城
                loc[0] = GIRAN_X1;
                loc[1] = GIRAN_X2;
                loc[2] = GIRAN_Y1;
                loc[3] = GIRAN_Y2;
                loc[4] = GIRAN_MAP;
                break;

            case HEINE_CASTLE_ID: // ハイネ城
                loc[0] = HEINE_X1;
                loc[1] = HEINE_X2;
                loc[2] = HEINE_Y1;
                loc[3] = HEINE_Y2;
                loc[4] = HEINE_MAP;
                break;

            case DOWA_CASTLE_ID: // ドワーフ城
                loc[0] = DOWA_X1;
                loc[1] = DOWA_X2;
                loc[2] = DOWA_Y1;
                loc[3] = DOWA_Y2;
                loc[4] = DOWA_MAP;
                break;

            case ADEN_CASTLE_ID: // アデン城
                loc[0] = ADEN_X1;
                loc[1] = ADEN_X2;
                loc[2] = ADEN_Y1;
                loc[3] = ADEN_Y2;
                loc[4] = ADEN_MAP;
                break;

            case DIAD_CASTLE_ID: // ディアド要塞
                loc[0] = DIAD_X1;
                loc[1] = DIAD_X2;
                loc[2] = DIAD_Y1;
                loc[3] = DIAD_Y2;
                loc[4] = DIAD_MAP;
                break;
        }
        return loc;
    }

    public static int[] getCastleLoc(final int castle_id) { // castle_idから城内の座标を返す
        final int[] loc = new int[3];
        switch (castle_id) {
            case KENT_CASTLE_ID: // ケント城
                loc[0] = 32731;
                loc[1] = 32810;
                loc[2] = 15;
                break;

            case OT_CASTLE_ID: // オークの森
                loc[0] = 32800;
                loc[1] = 32277;
                loc[2] = 4;
                break;

            case WW_CASTLE_ID: // ウィンダウッド城
                loc[0] = 32730;
                loc[1] = 32814;
                loc[2] = 29;
                break;

            case GIRAN_CASTLE_ID: // ギラン城
                loc[0] = 32724;
                loc[1] = 32827;
                loc[2] = 52;
                break;

            case HEINE_CASTLE_ID: // ハイネ城
                loc[0] = 32568;
                loc[1] = 32855;
                loc[2] = 64;
                break;

            case DOWA_CASTLE_ID: // ドワーフ城
                loc[0] = 32853;
                loc[1] = 32810;
                loc[2] = 66;
                break;

            case ADEN_CASTLE_ID: // アデン城
                loc[0] = 32892;
                loc[1] = 32572;
                loc[2] = 300;
                break;

            case DIAD_CASTLE_ID: // ディアド要塞
                loc[0] = 32733;
                loc[1] = 32985;
                loc[2] = 330;
                break;
        }
        return loc;
    }

    /*
     * castle_idから归还先の座标をランダムに返す
     */
    public static int[] getGetBackLoc(final int castle_id) {
        int[] loc;
        switch (castle_id) {
            case KENT_CASTLE_ID: // 肯特城
                loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_KENT);
                break;

            case OT_CASTLE_ID: // 妖魔城
                loc = L1TownLocation
                        .getGetBackLoc(L1TownLocation.TOWNID_ORCISH_FOREST);
                break;

            case WW_CASTLE_ID: // 风木城
                loc = L1TownLocation
                        .getGetBackLoc(L1TownLocation.TOWNID_WINDAWOOD);
                break;

            case GIRAN_CASTLE_ID: // 奇岩城
                loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
                break;

            case HEINE_CASTLE_ID: // 海音城
                loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_HEINE);
                break;

            case DOWA_CASTLE_ID: // 侏儒城
                loc = L1TownLocation
                        .getGetBackLoc(L1TownLocation.TOWNID_WERLDAN);
                break;

            case ADEN_CASTLE_ID: // 亚丁城
                loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_ADEN);
                break;

            case DIAD_CASTLE_ID: // 狄亚得要塞
                // ディアド要塞の归还先は未调查
                final Random random = new Random();
                final int rnd = random.nextInt(3);
                final int[][] loctmp = new int[][] {
                        new int[] { 32792, 32807, 310 },
                        new int[] { 32816, 32820, 310 },
                        new int[] { 32823, 32797, 310 }, };
                loc = loctmp[rnd];
                /*
                 * loc = new int[3]; if (rnd == 0) { loc[0] = 32792; loc[1] =
                 * 32807; loc[2] = 310;
                 * 
                 * } else if (rnd == 1) { loc[0] = 32816; loc[1] = 32820; loc[2]
                 * = 310;
                 * 
                 * } else if (rnd == 2) { loc[0] = 32823; loc[1] = 32797; loc[2]
                 * = 310; }
                 */
                break;

            default: // 存在しないcastle_idが指定された场合はSKT
                loc = L1TownLocation
                        .getGetBackLoc(L1TownLocation.TOWNID_SILVER_KNIGHT_TOWN);
                break;
        }
        return loc;
    }

    /**
     * npcidからcastle_idを返す
     * 
     * @param npcid
     * @return
     */
    public static int getCastleIdByNpcid(final int npcid) {
        // アデン城：アデン王国全域
        // ケント城：ケント、グルーディン
        // ウィンダウッド城：ウッドベック、オアシス、シルバーナイトタウン
        // ギラン城：ギラン、话せる岛
        // ハイネ城：ハイネ
        // ドワーフ城：ウェルダン、象牙の塔、象牙の塔の村
        // オーク砦：火田村
        // ディアド要塞：战争税の一部
    	
    	//修改主城税收为奇岩 hjx1000

        /*int castle_id = 0;

        final int town_id = L1TownLocation.getTownIdByNpcid(npcid);

        switch (town_id) {
            case L1TownLocation.TOWNID_KENT:
            case L1TownLocation.TOWNID_GLUDIO:
                castle_id = KENT_CASTLE_ID; // ケント城
                break;

            case L1TownLocation.TOWNID_ORCISH_FOREST:
                castle_id = OT_CASTLE_ID; // オークの森
                break;

            case L1TownLocation.TOWNID_SILVER_KNIGHT_TOWN:
            case L1TownLocation.TOWNID_WINDAWOOD:
                castle_id = WW_CASTLE_ID; // ウィンダウッド城
                break;

            case L1TownLocation.TOWNID_TALKING_ISLAND:
            case L1TownLocation.TOWNID_GIRAN:
                castle_id = GIRAN_CASTLE_ID; // ギラン城
                break;

            case L1TownLocation.TOWNID_HEINE:
                castle_id = HEINE_CASTLE_ID; // ハイネ城
                break;

            case L1TownLocation.TOWNID_WERLDAN:
            case L1TownLocation.TOWNID_OREN:
                castle_id = DOWA_CASTLE_ID; // ドワーフ城
                break;

            case L1TownLocation.TOWNID_ADEN:
                castle_id = ADEN_CASTLE_ID; // アデン城
                break;

            case L1TownLocation.TOWNID_OUM_DUNGEON:
                castle_id = DIAD_CASTLE_ID; // ディアド要塞
                break;

            default:
                break;
        }
        return castle_id;*/
    	return GIRAN_CASTLE_ID;
    }

    // このメソッドはアデン时间で一日每に更新される税率を返却する。(リアルタイムな税率ではない)
    public static int getCastleTaxRateByNpcId(final int npcId) {
        final int castleId = getCastleIdByNpcid(npcId);
        if (castleId != 0) {
            return _castleTaxRate.get(castleId);
        }
        return 0;
    }

    // 各城の税率を保管しておくHashMap(ショップ用)
    private static Map<Integer, Integer> _castleTaxRate = new HashMap<Integer, Integer>();

    private static L1CastleTaxRateListener _listener;

    // GameServer#initialize,L1CastleTaxRateListener#onDayChangedだけに呼び出される予定。
    public static void setCastleTaxRate() {
        try {
            for (final L1Castle castle : CastleReading.get()
                    .getCastleTableList()) {
                _castleTaxRate.put(castle.getId(), castle.getTaxRate());
            }

            if (_listener == null) {
                _listener = new L1CastleTaxRateListener();
                L1GameTimeClock.getInstance().addListener(_listener);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private static class L1CastleTaxRateListener extends L1GameTimeAdapter {
        @Override
        public void onDayChanged(final L1GameTime time) {
            try {
                L1CastleLocation.setCastleTaxRate();

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    /**
     * サブタワー番号からサブタワーの座标を返す
     */
    public static int[] getSubTowerLoc(final int no) {
        final int[] result = new int[3];
        final L1Location loc = _subTowers.get(no);
        if (loc != null) {
            result[0] = loc.getX();
            result[1] = loc.getY();
            result[2] = loc.getMapId();
        }
        return result;
    }

}
