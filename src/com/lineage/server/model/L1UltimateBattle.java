package com.lineage.server.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.server.ActionCodes;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.UBSpawnTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.RangeInt;
import com.lineage.server.world.World;

public class L1UltimateBattle {

    private static final Log _log = LogFactory.getLog(L1UltimateBattle.class);

    private int _locX;
    private int _locY;
    private L1Location _location; // 中心点
    private short _mapId;
    private int _locX1;
    private int _locY1;
    private int _locX2;
    private int _locY2;

    private int _ubId;
    private int _pattern;
    private boolean _isNowUb;
    private boolean _active; // UB入场可能～竞技终了までtrue

    private int _minLevel;
    private int _maxLevel;
    private int _maxPlayer;

    private boolean _enterRoyal;
    private boolean _enterKnight;
    private boolean _enterMage;
    private boolean _enterElf;
    private boolean _enterDarkelf;
    private boolean _enterDragonKnight;
    private boolean _enterIllusionist;
    private boolean _enterMale;
    private boolean _enterFemale;
    private boolean _usePot;
    private int _hpr;
    private int _mpr;

    private static int BEFORE_MINUTE = 5; // 5分前から入场开始

    private Set<Integer> _managers = new HashSet<Integer>();
    private SortedSet<Integer> _ubTimes = new TreeSet<Integer>();

    private final ArrayList<L1PcInstance> _members = new ArrayList<L1PcInstance>();

    /**
     * ラウンド开始时のメッセージを送信する。
     * 
     * @param curRound
     *            开始するラウンド
     */
    private void sendRoundMessage(final int curRound) {
        // XXX - このIDは间违っている
        final int MSGID_ROUND_TABLE[] = { 893, 894, 895, 896 };

        this.sendMessage(MSGID_ROUND_TABLE[curRound - 1], "");
    }

    /**
     * ポーション等の补给アイテムを出现させる。
     * 
     * @param curRound
     *            现在のラウンド
     */
    private void spawnSupplies(final int curRound) {
        if (curRound == 1) {
            this.spawnGroundItem(L1ItemId.ADENA, 1000, 60);
            this.spawnGroundItem(L1ItemId.POTION_OF_CURE_POISON, 3, 20);
            this.spawnGroundItem(L1ItemId.POTION_OF_EXTRA_HEALING, 5, 20);
            this.spawnGroundItem(L1ItemId.POTION_OF_GREATER_HEALING, 3, 20);
            this.spawnGroundItem(40317, 1, 5); // 砥石
            this.spawnGroundItem(40079, 1, 20); // 归还スク

        } else if (curRound == 2) {
            this.spawnGroundItem(L1ItemId.ADENA, 5000, 50);
            this.spawnGroundItem(L1ItemId.POTION_OF_CURE_POISON, 5, 20);
            this.spawnGroundItem(L1ItemId.POTION_OF_EXTRA_HEALING, 10, 20);
            this.spawnGroundItem(L1ItemId.POTION_OF_GREATER_HEALING, 5, 20);
            this.spawnGroundItem(40317, 1, 7); // 砥石
            this.spawnGroundItem(40093, 1, 10); // ブランクスク(Lv4)
            this.spawnGroundItem(40079, 1, 5); // 归还スク

        } else if (curRound == 3) {
            this.spawnGroundItem(L1ItemId.ADENA, 10000, 30);
            this.spawnGroundItem(L1ItemId.POTION_OF_CURE_POISON, 7, 20);
            this.spawnGroundItem(L1ItemId.POTION_OF_EXTRA_HEALING, 20, 20);
            this.spawnGroundItem(L1ItemId.POTION_OF_GREATER_HEALING, 10, 20);
            this.spawnGroundItem(40317, 1, 10); // 砥石
            this.spawnGroundItem(40094, 1, 10); // ブランクスク(Lv5)
        }
    }

    /**
     * コロシアムから出たメンバーをメンバーリストから削除する。
     */
    private void removeRetiredMembers() {
        final L1PcInstance[] temp = this.getMembersArray();
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].getMapId() != this._mapId) {
                this.removeMember(temp[i]);
            }
        }
    }

    /**
     * UBに参加しているプレイヤーへメッセージ(S_ServerMessage)を送信する。
     * 
     * @param type
     *            メッセージタイプ
     * @param msg
     *            送信するメッセージ
     */
    private void sendMessage(final int type, final String msg) {
        for (final L1PcInstance pc : this.getMembersArray()) {
            pc.sendPackets(new S_ServerMessage(type, msg));
        }
    }

    /**
     * 召唤地面补给品
     * 
     * @param itemId
     *            物品编号
     * @param stackCount
     *            数量
     * @param count
     *            召唤次数
     */
    private void spawnGroundItem(final int itemId, final long stackCount,
            final int count) {
        final L1Item temp = ItemTable.get().getTemplate(itemId);
        if (temp == null) {
            return;
        }

        for (int i = 0; i < count; i++) {
            final L1Location loc = this._location.randomLocation(
                    (this.getLocX2() - this.getLocX1()) / 2, false);
            if (temp.isStackable()) {
                final L1ItemInstance item = ItemTable.get().createItem(itemId);
                item.setEnchantLevel(0);
                item.setCount(stackCount);
                final L1GroundInventory ground = World.get().getInventory(
                        loc.getX(), loc.getY(), this._mapId);
                if (ground.checkAddItem(item, stackCount) == L1Inventory.OK) {
                    ground.storeItem(item);
                }

            } else {
                L1ItemInstance item = null;
                for (int createCount = 0; createCount < stackCount; createCount++) {
                    item = ItemTable.get().createItem(itemId);
                    item.setEnchantLevel(0);
                    final L1GroundInventory ground = World.get().getInventory(
                            loc.getX(), loc.getY(), this._mapId);
                    if (ground.checkAddItem(item, stackCount) == L1Inventory.OK) {
                        ground.storeItem(item);
                    }
                }
            }
        }
    }

    /**
     * 删除怪物
     */
    private void clearColosseum() {
        for (final Object obj : World.get().getVisibleObjects(this._mapId)
                .values()) {
            if (obj instanceof L1MonsterInstance) {// モンスター削除
                final L1MonsterInstance mob = (L1MonsterInstance) obj;
                if (!mob.isDead()) {
                    mob.setDead(true);
                    mob.setStatus(ActionCodes.ACTION_Die);
                    mob.setCurrentHpDirect(0);
                    mob.deleteMe();

                }

            } else if (obj instanceof L1Inventory) {// アイテム削除
                final L1Inventory inventory = (L1Inventory) obj;
                inventory.clearItems();
            }
        }
    }

    /**
     * コンストラクタ。
     */
    public L1UltimateBattle() {
    }

    class UbThread implements Runnable {
        /**
         * 竞技开始までをカウントダウンする。
         * 
         * @throws InterruptedException
         */
        private void countDown() throws InterruptedException {
            // XXX - このIDは间违っている
            final int MSGID_COUNT = 637;
            final int MSGID_START = 632;

            for (int loop = 0; loop < BEFORE_MINUTE * 60 - 10; loop++) { // 开始10秒前まで待つ
                Thread.sleep(1000);
                // removeRetiredMembers();
            }
            L1UltimateBattle.this.removeRetiredMembers();

            L1UltimateBattle.this.sendMessage(MSGID_COUNT, "10"); // 10秒前

            Thread.sleep(5000);
            L1UltimateBattle.this.sendMessage(MSGID_COUNT, "5"); // 5秒前

            Thread.sleep(1000);
            L1UltimateBattle.this.sendMessage(MSGID_COUNT, "4"); // 4秒前

            Thread.sleep(1000);
            L1UltimateBattle.this.sendMessage(MSGID_COUNT, "3"); // 3秒前

            Thread.sleep(1000);
            L1UltimateBattle.this.sendMessage(MSGID_COUNT, "2"); // 2秒前

            Thread.sleep(1000);
            L1UltimateBattle.this.sendMessage(MSGID_COUNT, "1"); // 1秒前

            Thread.sleep(1000);
            L1UltimateBattle.this.sendMessage(MSGID_START, "无限大战 开始"); // スタート
            L1UltimateBattle.this.removeRetiredMembers();
        }

        /**
         * 全てのモンスターが出现した后、次のラウンドが始まるまでの时间を待机する。
         * 
         * @param curRound
         *            现在のラウンド
         * @throws InterruptedException
         */
        private void waitForNextRound(final int curRound)
                throws InterruptedException {
            final int WAIT_TIME_TABLE[] = { 6, 6, 2, 18 };

            final int wait = WAIT_TIME_TABLE[curRound - 1];
            for (int i = 0; i < wait; i++) {
                Thread.sleep(10000);
                // removeRetiredMembers();
            }
            L1UltimateBattle.this.removeRetiredMembers();
        }

        /**
         * スレッドプロシージャ。
         */
        @Override
        public void run() {
            try {
                L1UltimateBattle.this.setActive(true);
                this.countDown();
                L1UltimateBattle.this.setNowUb(true);
                for (int round = 1; round <= 4; round++) {
                    L1UltimateBattle.this.sendRoundMessage(round);

                    final L1UbPattern pattern = UBSpawnTable.getInstance()
                            .getPattern(L1UltimateBattle.this._ubId,
                                    L1UltimateBattle.this._pattern);

                    final ArrayList<L1UbSpawn> spawnList = pattern
                            .getSpawnList(round);

                    for (final L1UbSpawn spawn : spawnList) {
                        if (L1UltimateBattle.this.getMembersCount() > 0) {
                            spawn.spawnAll();
                        }

                        Thread.sleep(spawn.getSpawnDelay() * 1000);
                        // removeRetiredMembers();
                    }

                    if (L1UltimateBattle.this.getMembersCount() > 0) {
                        L1UltimateBattle.this.spawnSupplies(round);
                    }

                    this.waitForNextRound(round);
                }

                for (final L1PcInstance pc : L1UltimateBattle.this
                        .getMembersArray()) {// コロシアム内に居るPCを外へ出す
                    final Random random = new Random();
                    final int rndx = random.nextInt(4);
                    final int rndy = random.nextInt(4);
                    final int locx = 33503 + rndx;
                    final int locy = 32764 + rndy;
                    final short mapid = 4;
                    L1Teleport.teleport(pc, locx, locy, mapid, 5, true);
                    L1UltimateBattle.this.removeMember(pc);
                }
                L1UltimateBattle.this.clearColosseum();
                L1UltimateBattle.this.setActive(false);
                L1UltimateBattle.this.setNowUb(false);

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    /**
     * アルティメットバトルを开始する。
     * 
     * @param ubId
     *            开始するアルティメットバトルのID
     */
    public void start() {
        final int patternsMax = UBSpawnTable.getInstance().getMaxPattern(
                this._ubId);
        final Random random = new Random();
        this._pattern = random.nextInt(patternsMax) + 1; // 出现パターンを决める

        final UbThread ub = new UbThread();
        GeneralThreadPool.get().execute(ub);
    }

    /**
     * プレイヤーを参加メンバーリストへ追加する。
     * 
     * @param pc
     *            新たに参加するプレイヤー
     */
    public void addMember(final L1PcInstance pc) {
        if (!this._members.contains(pc)) {
            this._members.add(pc);
        }
    }

    /**
     * プレイヤーを参加メンバーリストから削除する。
     * 
     * @param pc
     *            削除するプレイヤー
     */
    public void removeMember(final L1PcInstance pc) {
        this._members.remove(pc);
    }

    /**
     * 参加メンバーリストをクリアする。
     */
    public void clearMembers() {
        this._members.clear();
    }

    /**
     * プレイヤーが、参加メンバーかを返す。
     * 
     * @param pc
     *            调べるプレイヤー
     * @return 参加メンバーであればtrue、そうでなければfalse。
     */
    public boolean isMember(final L1PcInstance pc) {
        return this._members.contains(pc);
    }

    /**
     * 参加メンバーの配列を作成し、返す。
     * 
     * @return 参加メンバーの配列
     */
    public L1PcInstance[] getMembersArray() {
        return this._members.toArray(new L1PcInstance[this._members.size()]);
    }

    /**
     * 参加メンバー数を返す。
     * 
     * @return 参加メンバー数
     */
    public int getMembersCount() {
        return this._members.size();
    }

    /**
     * UB中かを设定する。
     * 
     * @param i
     *            true/false
     */
    private void setNowUb(final boolean i) {
        this._isNowUb = i;
    }

    /**
     * UB中かを返す。
     * 
     * @return UB中であればtrue、そうでなければfalse。
     */
    public boolean isNowUb() {
        return this._isNowUb;
    }

    public int getUbId() {
        return this._ubId;
    }

    public void setUbId(final int id) {
        this._ubId = id;
    }

    public short getMapId() {
        return this._mapId;
    }

    public void setMapId(final short mapId) {
        this._mapId = mapId;
    }

    public int getMinLevel() {
        return this._minLevel;
    }

    public void setMinLevel(final int level) {
        this._minLevel = level;
    }

    public int getMaxLevel() {
        return this._maxLevel;
    }

    public void setMaxLevel(final int level) {
        this._maxLevel = level;
    }

    public int getMaxPlayer() {
        return this._maxPlayer;
    }

    public void setMaxPlayer(final int count) {
        this._maxPlayer = count;
    }

    public void setEnterRoyal(final boolean enterRoyal) {
        this._enterRoyal = enterRoyal;
    }

    public void setEnterKnight(final boolean enterKnight) {
        this._enterKnight = enterKnight;
    }

    public void setEnterMage(final boolean enterMage) {
        this._enterMage = enterMage;
    }

    public void setEnterElf(final boolean enterElf) {
        this._enterElf = enterElf;
    }

    public void setEnterDarkelf(final boolean enterDarkelf) {
        this._enterDarkelf = enterDarkelf;
    }

    public void setEnterDragonKnight(final boolean enterDragonKnight) {
        this._enterDragonKnight = enterDragonKnight;
    }

    public void setEnterIllusionist(final boolean enterIllusionist) {
        this._enterIllusionist = enterIllusionist;
    }

    public void setEnterMale(final boolean enterMale) {
        this._enterMale = enterMale;
    }

    public void setEnterFemale(final boolean enterFemale) {
        this._enterFemale = enterFemale;
    }

    public boolean canUsePot() {
        return this._usePot;
    }

    public void setUsePot(final boolean usePot) {
        this._usePot = usePot;
    }

    public int getHpr() {
        return this._hpr;
    }

    public void setHpr(final int hpr) {
        this._hpr = hpr;
    }

    public int getMpr() {
        return this._mpr;
    }

    public void setMpr(final int mpr) {
        this._mpr = mpr;
    }

    public int getLocX1() {
        return this._locX1;
    }

    public void setLocX1(final int locX1) {
        this._locX1 = locX1;
    }

    public int getLocY1() {
        return this._locY1;
    }

    public void setLocY1(final int locY1) {
        this._locY1 = locY1;
    }

    public int getLocX2() {
        return this._locX2;
    }

    public void setLocX2(final int locX2) {
        this._locX2 = locX2;
    }

    public int getLocY2() {
        return this._locY2;
    }

    public void setLocY2(final int locY2) {
        this._locY2 = locY2;
    }

    // setされたlocx1～locy2から中心点を求める。
    public void resetLoc() {
        this._locX = (this._locX2 + this._locX1) / 2;
        this._locY = (this._locY2 + this._locY1) / 2;
        this._location = new L1Location(this._locX, this._locY, this._mapId);
    }

    public L1Location getLocation() {
        return this._location;
    }

    public void addManager(final int npcId) {
        this._managers.add(npcId);
    }

    public boolean containsManager(final int npcId) {
        return this._managers.contains(npcId);
    }

    public void addUbTime(final int time) {
        this._ubTimes.add(time);
    }

    public String getNextUbTime() {
        return intToTimeFormat(this.nextUbTime());
    }

    private int nextUbTime() {
        final SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        final int nowTime = Integer
                .valueOf(sdf.format(getRealTime().getTime()));
        SortedSet<Integer> tailSet = this._ubTimes.tailSet(nowTime);
        if (tailSet.isEmpty()) {
            tailSet = this._ubTimes;
        }
        return tailSet.first();
    }

    private static String intToTimeFormat(final int n) {
        return n / 100 + ":" + n % 100 / 10 + "" + n % 10;
    }

    private static Calendar getRealTime() {
        final TimeZone _tz = TimeZone.getTimeZone(Config.TIME_ZONE);
        final Calendar cal = Calendar.getInstance(_tz);
        return cal;
    }

    public boolean checkUbTime() {
        final SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        final Calendar realTime = getRealTime();
        realTime.add(Calendar.MINUTE, BEFORE_MINUTE);
        final int nowTime = Integer.valueOf(sdf.format(realTime.getTime()));
        return this._ubTimes.contains(nowTime);
    }

    private void setActive(final boolean f) {
        this._active = f;
    }

    /**
     * @return UB入场可能～竞技终了まではtrue,それ以外はfalseを返す。
     */
    public boolean isActive() {
        return this._active;
    }

    /**
     * UBに参加可能か、レベル、クラスをチェックする。
     * 
     * @param pc
     *            UBに参加できるかチェックするPC
     * @return 参加出来る场合はtrue,出来ない场合はfalse
     */
    public boolean canPcEnter(final L1PcInstance pc) {
        // _log.log(Level.FINE, "pcname=" + pc.getName() + " ubid=" + _ubId
        // + " minlvl=" + _minLevel + " maxlvl=" + _maxLevel);
        // 参加可能なレベルか
        if (!RangeInt.includes(pc.getLevel(), this._minLevel, this._maxLevel)) {
            return false;
        }

        // 参加可能なクラスか
        if (!((pc.isCrown() && this._enterRoyal)
                || (pc.isKnight() && this._enterKnight)
                || (pc.isWizard() && this._enterMage)
                || (pc.isElf() && this._enterElf)
                || (pc.isDarkelf() && this._enterDarkelf)
                || (pc.isDragonKnight() && this._enterDragonKnight) || (pc
                .isIllusionist() && this._enterIllusionist))) {
            return false;
        }

        return true;
    }

    private String[] _ubInfo;

    public String[] makeUbInfoStrings() {
        if (this._ubInfo != null) {
            return this._ubInfo;
        }
        final String nextUbTime = this.getNextUbTime();
        // クラス
        final StringBuilder classesBuff = new StringBuilder();
        if (this._enterDarkelf) {
            classesBuff.append("黑暗妖精 ");
        }
        if (this._enterMage) {
            classesBuff.append("法师 ");
        }
        if (this._enterElf) {
            classesBuff.append("妖精 ");
        }
        if (this._enterKnight) {
            classesBuff.append("骑士 ");
        }
        if (this._enterRoyal) {
            classesBuff.append("王族 ");
        }
        if (this._enterDragonKnight) {
            classesBuff.append("龙骑士 ");
        }
        if (this._enterIllusionist) {
            classesBuff.append("幻术师 ");
        }
        final String classes = classesBuff.toString().trim();
        // 性别
        final StringBuilder sexBuff = new StringBuilder();
        if (this._enterMale) {
            sexBuff.append("男 ");
        }
        if (this._enterFemale) {
            sexBuff.append("女 ");
        }
        final String sex = sexBuff.toString().trim();
        final String loLevel = String.valueOf(this._minLevel);
        final String hiLevel = String.valueOf(this._maxLevel);
        final String teleport = this._location.getMap().isEscapable() ? "可能"
                : "不可能";
        final String res = this._location.getMap().isUseResurrection() ? "可能"
                : "不可能";
        final String pot = "可能";
        final String hpr = String.valueOf(this._hpr);
        final String mpr = String.valueOf(this._mpr);
        final String summon = this._location.getMap().isTakePets() ? "可能"
                : "不可能";
        final String summon2 = this._location.getMap().isRecallPets() ? "可能"
                : "不可能";
        this._ubInfo = new String[] { nextUbTime, classes, sex, loLevel,
                hiLevel, teleport, res, pot, hpr, mpr, summon, summon2 };
        return this._ubInfo;
    }
}
