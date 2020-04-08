package com.lineage.server.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1BowInstance;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1FieldObjectInstance;
import com.lineage.server.model.Instance.L1FurnitureInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.gametime.L1GameTime;
import com.lineage.server.model.gametime.L1GameTimeAdapter;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1SpawnTime;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.timecontroller.npc.NpcSpawnBossTimer;
import com.lineage.server.types.Point;
import com.lineage.server.world.World;

/**
 * 召唤控制项
 * 
 * @author daien
 * 
 */
public class L1Spawn extends L1GameTimeAdapter {

    private static final Log _log = LogFactory.getLog(L1Spawn.class);

    private final L1Npc _template;

    private int _id;
    private String _location;
    private int _maximumCount;
    private int _npcid;
    private int _groupId;
    private int _locx;
    private int _locy;
    private int _tmplocx;// 本次召唤X座标
    private int _tmplocy;// 本次召唤Y座标
    private short _tmpmapid;
    private int _randomx;
    private int _randomy;
    private int _locx1;
    private int _locy1;
    private int _locx2;
    private int _locy2;
    private int _heading;
    private int _minRespawnDelay;
    private int _maxRespawnDelay;
    private short _mapid;
    private boolean _respaenScreen;
    private int _movementDistance;
    private boolean _rest;
    private int _spawnType;
    private int _delayInterval;
    private L1SpawnTime _time;
    private Calendar _nextSpawnTime = null;
    private long _spawnInterval = 0;
    private int _existTime = 0;

    private Map<Integer, Point> _homePoint = null; // initでspawnした个々のオブジェクトのホームポイント

//    private List<L1NpcInstance> _mobs = new ArrayList<L1NpcInstance>();

    private Random _random = new Random();

    private String _name;

    private class SpawnTask implements Runnable {

        private int _spawnNumber;

        private int _objectId;

        private long _delay;

        /**
         * 
         * @param spawnNumber
         *            召唤管理编号
         * @param objectId
         *            世界物件编号
         * @param delay
         *            延迟时间
         */
        private SpawnTask(final int spawnNumber, final int objectId, long delay) {
            this._spawnNumber = spawnNumber;
            this._objectId = objectId;
            this._delay = delay;
        }

        /**
         * 启动线程
         */
        public void getStart() {
            GeneralThreadPool.get().schedule(this, this._delay);
        }

        @Override
        public void run() {
            L1Spawn.this.doSpawn(this._spawnNumber, this._objectId);
        }
    }

    public L1Spawn(final L1Npc mobTemplate) {
        this._template = mobTemplate;
    }

    public String getName() {
        return this._name;
    }

    public void setName(final String name) {
        this._name = name;
    }

    public short getMapId() {
        return this._mapid;
    }

    public void setMapId(final short _mapid) {
        this._mapid = _mapid;
    }

    public boolean isRespawnScreen() {
        return this._respaenScreen;
    }

    public void setRespawnScreen(final boolean flag) {
        this._respaenScreen = flag;
    }

    /**
     * 移动距离
     * 
     * @return
     */
    public int getMovementDistance() {
        return this._movementDistance;
    }

    /**
     * 移动距离
     * 
     * @param i
     */
    public void setMovementDistance(final int i) {
        this._movementDistance = i;
    }

    /**
     * 数量
     * 
     * @return
     */
    public int getAmount() {
        return this._maximumCount;
    }

    /**
     * 队伍召唤编号
     * 
     * @return
     */
    public int getGroupId() {
        return this._groupId;
    }

    public int getId() {
        return this._id;
    }

    public String getLocation() {
        return this._location;
    }

    public int getLocX() {
        return this._locx;
    }

    public int getLocY() {
        return this._locy;
    }

    public int getNpcId() {
        return this._npcid;
    }

    public int getHeading() {
        return this._heading;
    }

    public int getRandomx() {
        return this._randomx;
    }

    public int getRandomy() {
        return this._randomy;
    }

    public int getLocX1() {
        return this._locx1;
    }

    public int getLocY1() {
        return this._locy1;
    }

    public int getLocX2() {
        return this._locx2;
    }

    public int getLocY2() {
        return this._locy2;
    }

    /**
     * 召唤延迟
     * 
     * @return 单位:秒
     */
    public int getMinRespawnDelay() {
        return this._minRespawnDelay;
    }

    /**
     * 召唤延迟
     * 
     * @return 单位:秒
     */
    public int getMaxRespawnDelay() {
        return this._maxRespawnDelay;
    }

    /**
     * 数量
     * 
     * @param amount
     */
    public void setAmount(final int amount) {
        this._maximumCount = amount;
    }

    public void setId(final int id) {
        this._id = id;
    }

    /**
     * 队伍召唤编号
     * 
     * @param i
     */
    public void setGroupId(final int i) {
        this._groupId = i;
    }

    public void setLocation(final String location) {
        this._location = location;
    }

    public void setLocX(final int locx) {
        this._locx = locx;
    }

    public void setLocY(final int locy) {
        this._locy = locy;
    }

    public void setNpcid(final int npcid) {
        this._npcid = npcid;
    }

    public void setHeading(final int heading) {
        this._heading = heading;
    }

    /**
     * 召唤随机范围
     * 
     * @param randomx
     */
    public void setRandomx(final int randomx) {
        this._randomx = randomx;
    }

    /**
     * 召唤随机范围
     * 
     * @param randomy
     */
    public void setRandomy(final int randomy) {
        this._randomy = randomy;
    }

    public void setLocX1(final int locx1) {
        this._locx1 = locx1;
    }

    public void setLocY1(final int locy1) {
        this._locy1 = locy1;
    }

    public void setLocX2(final int locx2) {
        this._locx2 = locx2;
    }

    public void setLocY2(final int locy2) {
        this._locy2 = locy2;
    }

    /**
     * 召唤延迟
     * 
     * @param i
     *            单位:秒
     */
    public void setMinRespawnDelay(final int i) {
        this._minRespawnDelay = i;
    }

    /**
     * 召唤延迟
     * 
     * @param i
     *            单位:秒
     */
    public void setMaxRespawnDelay(final int i) {
        this._maxRespawnDelay = i;
    }

    public int getTmpLocX() {
        return this._tmplocx;
    }

    public int getTmpLocY() {
        return this._tmplocy;
    }

    public short getTmpMapid() {
        return this._tmpmapid;
    }

    /**
     * 抵达召唤时间
     * 
     * @param npcTemp
     * @param next_spawn_time
     * @return
     */
    private boolean isSpawnTime(L1NpcInstance npcTemp) {
        if (_nextSpawnTime != null) {
            // 取得目前时间
            final Calendar cals = Calendar.getInstance();
            long nowTime = System.currentTimeMillis();
            cals.setTimeInMillis(nowTime);

            if (cals.after(_nextSpawnTime)) {
                // System.out.println("抵达召唤时间");
                return true;

            } else {
                if (NpcSpawnBossTimer.MAP.get(npcTemp) == null) {
                    long spawnTime = _nextSpawnTime.getTimeInMillis();
                    // 加入等候清单(5秒误差补正)
                    long spa = ((spawnTime - nowTime) / 1000) + 5;
                    // 加入等候清单(5秒误差补正)
                    NpcSpawnBossTimer.MAP.put(npcTemp, spa);
                }
                return false;
            }
        }
        return true;
    }

    /**
     * 下次召唤时间
     * 
     * @return
     */
    public Calendar get_nextSpawnTime() {
        return _nextSpawnTime;
    }

    /**
     * 下次召唤时间
     * 
     * @param next_spawn_time
     */
    public void set_nextSpawnTime(Calendar next_spawn_time) {
        _nextSpawnTime = next_spawn_time;
    }

    /**
     * 差异时间(单位:分钟)
     * 
     * @param spawn_interval
     */
    public void set_spawnInterval(long spawn_interval) {
        _spawnInterval = spawn_interval;
    }

    /**
     * 差异时间(单位:分钟)
     * 
     * @param spawn_interval
     * @return
     */
    public long get_spawnInterval() {
        return _spawnInterval;
    }

    /**
     * 存在时间(单位:分钟)
     * 
     * @param exist_time
     */
    public void set_existTime(int exist_time) {
        _existTime = exist_time;
    }

    private int calcRespawnDelay() {
        int respawnDelay = this._minRespawnDelay * 1000;
        if (this._delayInterval > 0) {
            respawnDelay += _random.nextInt(this._delayInterval) * 1000;
        }

        final L1GameTime currentTime = L1GameTimeClock.getInstance()
                .currentTime();
        if ((this._time != null)
                && !this._time.getTimePeriod().includes(currentTime)) { // 指定时间外なら指定时间までの时间を足す
            long diff = (this._time.getTimeStart().getTime() - currentTime
                    .toTime().getTime());
            if (diff < 0) {
                diff += 24 * 1000L * 3600L;
            }
            diff /= 6; // real time to game time
            respawnDelay = (int) diff;
        }
        return respawnDelay;
    }

    /**
     * SpawnTask的启动
     * 
     * @param spawnNumber
     *            管理编号
     * @param objectId
     *            世界物件编号
     */
    public void executeSpawnTask(final int spawnNumber, final int objectId) {
        if (_nextSpawnTime != null) {
            this.doSpawn(spawnNumber, objectId);

        } else {
            final SpawnTask task = new SpawnTask(spawnNumber, objectId,
                    this.calcRespawnDelay());
            task.getStart();
        }
    }

    private boolean _initSpawn = false;

    private boolean _spawnHomePoint;

    public void init() {
        if ((this._time != null) && this._time.isDeleteAtEndTime()) {
            // 时间外削除が指定されているなら、时间经过の通知を受ける。
            L1GameTimeClock.getInstance().addListener(this);
        }
        this._delayInterval = this._maxRespawnDelay - this._minRespawnDelay;
        this._initSpawn = true;
        // ホームポイントを持たせるか
        if (ConfigAlt.SPAWN_HOME_POINT
                && (ConfigAlt.SPAWN_HOME_POINT_COUNT <= this.getAmount())
                && (ConfigAlt.SPAWN_HOME_POINT_DELAY >= this
                        .getMinRespawnDelay()) && this.isAreaSpawn()) {
            this._spawnHomePoint = true;
            this._homePoint = new HashMap<Integer, Point>();
        }

        int spawnNum = 0;
        while (spawnNum < this._maximumCount) {
            // spawnNumは1～maxmumCountまで
            this.doSpawn(++spawnNum);
        }
        this._initSpawn = false;
    }

    /**
     * ホームポイントがある场合は、spawnNumberを基にspawnする。 それ以外の场合は、spawnNumberは未使用。
     */
    protected void doSpawn(final int spawnNumber) { // 初期配置
        // 指定时间外であれば、次spawnを予约して终わる。
        if ((this._time != null)
                && !this._time.getTimePeriod().includes(
                        L1GameTimeClock.getInstance().currentTime())) {
            this.executeSpawnTask(spawnNumber, 0);
            return;
        }
        this.doSpawn(spawnNumber, 0);
    }

    /**
     * 重新召唤
     * 
     * @param spawnNumber
     *            召唤管理编号
     * @param objectId
     *            世界物件编号
     */
    protected void doSpawn(final int spawnNumber, final int objectId) {
        _tmplocx = 0;
        _tmplocy = 0;
        _tmpmapid = 0;

        L1NpcInstance npcTemp = null;
        try {
            int newlocx = this.getLocX();
            int newlocy = this.getLocY();
            int tryCount = 0;

            npcTemp = NpcTable.get().newNpcInstance(this._template);
//            synchronized (this._mobs) {
//                this._mobs.add(npcTemp);
//            }

            if (objectId == 0) {
                npcTemp.setId(IdFactoryNpc.get().nextId());

            } else {
                npcTemp.setId(objectId); // 世界物件编号再利用
            }

            if ((0 <= this.getHeading()) && (this.getHeading() <= 7)) {
                npcTemp.setHeading(this.getHeading());
            } else {
                // heading值が正しくない
                npcTemp.setHeading(5);
            }

            // npc召唤地图换位
            final int npcId = npcTemp.getNpcTemplate().get_npcId();
            if ((npcId == 45488) && (this.getMapId() == 9)) { // 卡士伯
                npcTemp.setMap((short) (this.getMapId() + _random.nextInt(2)));

            } else if ((npcId == 45601) && (this.getMapId() == 11)) { // 死亡骑士
                npcTemp.setMap((short) (this.getMapId() + _random.nextInt(3)));

            } else {
                npcTemp.setMap(this.getMapId());
            }

            npcTemp.setMovementDistance(this.getMovementDistance());
            npcTemp.setRest(this.isRest());

            // 设置召唤的XY座标位置
            while (tryCount <= 50) {

                if (this.isAreaSpawn()) { // 区域召唤
                    Point pt = null;
                    if (this._spawnHomePoint
                            && (null != (pt = this._homePoint.get(spawnNumber)))) { // ホームポイントを元に再出现させる场合
                        final L1Location loc = new L1Location(pt,
                                this.getMapId()).randomLocation(
                                ConfigAlt.SPAWN_HOME_POINT_RANGE, false);
                        newlocx = loc.getX();
                        newlocy = loc.getY();

                    } else {
                        final int rangeX = this.getLocX2() - this.getLocX1();
                        final int rangeY = this.getLocY2() - this.getLocY1();
                        newlocx = _random.nextInt(rangeX) + this.getLocX1();
                        newlocy = _random.nextInt(rangeY) + this.getLocY1();
                    }

                    if (tryCount > 49) { // 已经召唤失败次数
                        if (_nextSpawnTime == null) {
                            newlocx = this.getLocX();
                            newlocy = this.getLocY();

                        } else {
                            // 延后5秒
                            final SpawnTask task = new SpawnTask(spawnNumber,
                                    npcTemp.getId(), 5000L);
                            task.getStart();
                            return;
                        }
                    }

                } else if (this.isRandomSpawn()) { // 范围召唤
                    newlocx = (this.getLocX() + ((int) (Math.random() * this
                            .getRandomx()) - (int) (Math.random() * this
                            .getRandomx())));
                    newlocy = (this.getLocY() + ((int) (Math.random() * this
                            .getRandomy()) - (int) (Math.random() * this
                            .getRandomy())));

                } else { // 定点召唤
                    newlocx = this.getLocX();
                    newlocy = this.getLocY();
                }

                if (this.getSpawnType() == SPAWN_TYPE_PC_AROUND) {// 周边PC躲避
                    final L1Location loc = new L1Location(newlocx, newlocy,
                            this.getMapId());
                    // 13格内PC物件
                    final ArrayList<L1PcInstance> pcs = World.get()
                            .getVisiblePc(loc);
                    if (pcs.size() > 0) {
                        final L1Location newloc = loc.randomLocation(20, false);
                        newlocx = newloc.getX();
                        newlocy = newloc.getY();
                    }
                }

                npcTemp.setX(newlocx);
                npcTemp.setHomeX(newlocx);
                npcTemp.setY(newlocy);
                npcTemp.setHomeY(newlocy);

                if (_nextSpawnTime == null) {
                    if (npcTemp.getMap().isInMap(npcTemp.getLocation())
                            && npcTemp.getMap().isPassable(
                                    npcTemp.getLocation(), npcTemp)) {
                        if (npcTemp instanceof L1MonsterInstance) {
                            if (this.isRespawnScreen()) {
                                break;
                            }

                            // 13格内PC物件
                            final ArrayList<L1PcInstance> pcs = World.get()
                                    .getVisiblePc(npcTemp.getLocation());
                            if (pcs.size() == 0) {
                                break;
                            }
                            /*
                             * final L1MonsterInstance mobtemp =
                             * (L1MonsterInstance) npcTemp; if
                             * (World.get().getVisiblePlayer(mobtemp).size() ==
                             * 0) { break; }
                             */
                            // 画面内具有PC物件 延后5秒
                            final SpawnTask task = new SpawnTask(spawnNumber,
                                    npcTemp.getId(), 5000L);
                            task.getStart();
                            return;
                        }
                    }
                } else {
                    // 座标可通行决定召唤位置
                    if (npcTemp.getMap().isPassable(npcTemp.getLocation(),
                            npcTemp)) {
                        break;
                    }
                }
                tryCount++;
            }

            if (npcTemp instanceof L1MonsterInstance) {
                ((L1MonsterInstance) npcTemp).initHide();
            }

            npcTemp.setSpawn(this);
            npcTemp.setreSpawn(true);
            npcTemp.setSpawnNumber(spawnNumber); // L1Spawnでの管理番号(ホームポイントに使用)
            if (this._initSpawn && this._spawnHomePoint) { // 初期配置でホームポイントを设定
                final Point pt = new Point(npcTemp.getX(), npcTemp.getY());
                this._homePoint.put(spawnNumber, pt); // ここで保存したpointを再出现时に使う
            }

            // 具备时间函数以时间为准
            if (_nextSpawnTime != null) {
                if (!isSpawnTime(npcTemp)) {
                    return;
                }
            }

            // 地狱不掉落物品
            if (npcTemp instanceof L1MonsterInstance) {
                final L1MonsterInstance mob = (L1MonsterInstance) npcTemp;
                if (mob.getMapId() == 666) {
                    mob.set_storeDroped(true);
                }
            }

            // 招换巴风特传出玩家到定点
            if ((npcId == 45573) && (npcTemp.getMapId() == 2)) { // 巴风特
                for (final L1PcInstance pc : World.get().getAllPlayers()) {
                    if (pc.getMapId() == 2) {
                        L1Teleport.teleport(pc, 32664, 32797, (short) 2, 0,
                                true);
                    }
                }
            }

            // 招换冰人恶魔传出玩家到定点
            if (((npcId == 46142) && (npcTemp.getMapId() == 73))
                    || ((npcId == 46141)// 冰人恶魔
                    && (npcTemp.getMapId() == 74))) {
                for (final L1PcInstance pc : World.get().getAllPlayers()) {
                    if ((pc.getMapId() >= 72) && (pc.getMapId() <= 74)) {
                        L1Teleport.teleport(pc, 32840, 32833, (short) 72,
                                pc.getHeading(), true);
                    }
                }
            }

            doCrystalCave(npcId);

            World.get().storeObject(npcTemp);
            World.get().addVisibleObject(npcTemp);

            if (npcTemp instanceof L1MonsterInstance) {
                final L1MonsterInstance mobtemp = (L1MonsterInstance) npcTemp;
                if (!this._initSpawn && (mobtemp.getHiddenStatus() == 0)) {
                    mobtemp.onNpcAI(); // AI启用
                }

                if (_existTime > 0) {
                    // 存在时间(秒)
                    mobtemp.set_spawnTime(_existTime * 60);
                }
            }

            // 具备NPC队伍
            if (this.getGroupId() != 0) {
                // 召唤队伍成员
                L1MobGroupSpawn.getInstance().doSpawn(npcTemp,
                        this.getGroupId(), this.isRespawnScreen(),
                        this._initSpawn);
            }

            npcTemp.turnOnOffLight();
            npcTemp.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // チャット开始

            _tmplocx = newlocx;
            _tmplocy = newlocy;
            _tmpmapid = npcTemp.getMapId();

            boolean setPassable = true;// 是否设置障碍
            if (npcTemp instanceof L1DollInstance) {// 魔法娃娃
                setPassable = false;
            }
            if (npcTemp instanceof L1EffectInstance) {// 效果
                setPassable = false;
            }
            if (npcTemp instanceof L1FieldObjectInstance) {// 景观
                setPassable = false;
            }
            if (npcTemp instanceof L1FurnitureInstance) {// 家具
                setPassable = false;
            }
            if (npcTemp instanceof L1DoorInstance) {// 门
                setPassable = false;
            }
            if (npcTemp instanceof L1BowInstance) {// 固定攻击器
                setPassable = false;
            }
            if (setPassable) {
                L1WorldMap.get().getMap(npcTemp.getMapId())
                        .setPassable(npcTemp.getX(), npcTemp.getY(), false, 2);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void setRest(final boolean flag) {
        this._rest = flag;
    }

    public boolean isRest() {
        return this._rest;
    }

    // private static final int SPAWN_TYPE_NORMAL = 0;

    private static final int SPAWN_TYPE_PC_AROUND = 1;

    // private static final int PC_AROUND_DISTANCE = 30;

    private int getSpawnType() {
        return this._spawnType;
    }

    /**
     * 召唤模式 0:无 1:闪避PC
     * 
     * @param type
     */
    public void setSpawnType(final int type) {
        this._spawnType = type;
    }

    /**
     * 区域召唤
     * 
     * @return
     */
    private boolean isAreaSpawn() {
        return (this.getLocX1() != 0) && (this.getLocY1() != 0)
                && (this.getLocX2() != 0) && (this.getLocY2() != 0);
    }

    /**
     * 范围召唤
     * 
     * @return
     */
    private boolean isRandomSpawn() {
        return (this.getRandomx() != 0) || (this.getRandomy() != 0);
    }

    public L1SpawnTime getTime() {
        return this._time;
    }

    public void setTime(final L1SpawnTime time) {
        this._time = time;
    }

    @Override
    public void onMinuteChanged(final L1GameTime time) {
        if (this._time.getTimePeriod().includes(time)) {
            return;
        }
//        synchronized (this._mobs) {
//            if (this._mobs.isEmpty()) {
//                return;
//            }
//            // 指定时间外になっていれば削除
//            for (final L1NpcInstance mob : this._mobs) {
//                mob.setCurrentHpDirect(0);
//                mob.setDead(true);
//                mob.setStatus(ActionCodes.ACTION_Die);
//                mob.deleteMe();
//            }
//            this._mobs.clear();
//        }
    }

    public static void doCrystalCave(final int npcId) {
        final int[] npcId2 = { 46143, 46144, 46145, 46146, 46147, 46148, 46149,
                46150, 46151, 46152 };
        final int[] doorId = { 5001, 5002, 5003, 5004, 5005, 5006, 5007, 5008,
                5009, 5010 };

        for (int i = 0; i < npcId2.length; i++) {
            if (npcId == npcId2[i]) {
                closeDoorInCrystalCave(doorId[i]);
            }
        }
    }

    private static void closeDoorInCrystalCave(final int doorId) {
        for (final L1Object object : World.get().getObject()) {
            if (object instanceof L1DoorInstance) {
                final L1DoorInstance door = (L1DoorInstance) object;
                if (door.getDoorId() == doorId) {
                    door.close();
                }
            }
        }
    }
}
