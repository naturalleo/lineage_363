package com.lineage.server.templates;

import java.sql.Timestamp;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.L1MobGroupSpawn;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldSpawnBoss;

/**
 * 召唤资料暂存(BOSS) 制作中 daien 2012-05-11
 * 
 * @author daien
 * 
 */
public class L1SpawnBoss extends L1SpawnEx {

    private static final long serialVersionUID = 6316427673712019172L;

    private static final Log _log = LogFactory.getLog(L1SpawnBoss.class);

    public L1SpawnBoss(final L1Npc mobTemplate) {
        this._template = mobTemplate;
    }

    @Override
    public L1Npc get_template() {
        return _template;
    }

    @Override
    public int get_id() {
        return _id;
    }

    @Override
    public void set_id(int id) {
        this._id = id;
    }

    @Override
    public int get_maximumCount() {
        return _maximumCount;
    }

    @Override
    public void set_maximumCount(int maximumCount) {
        this._maximumCount = maximumCount;
    }

    @Override
    public int get_npcid() {
        return _npcid;
    }

    @Override
    public void set_npcid(int npcid) {
        this._npcid = npcid;
    }

    @Override
    public int get_groupId() {
        return _groupId;
    }

    @Override
    public void set_groupId(int groupId) {
        this._groupId = groupId;
    }

    @Override
    public int get_tmplocx() {
        return _tmplocx;
    }

    @Override
    public void set_tmplocx(int tmplocx) {
        this._tmplocx = tmplocx;
    }

    @Override
    public int get_tmplocy() {
        return _tmplocy;
    }

    @Override
    public void set_tmplocy(int tmplocy) {
        this._tmplocy = tmplocy;
    }

    @Override
    public short get_tmpmapid() {
        return _tmpmapid;
    }

    @Override
    public void set_tmpmapid(short tmpmapid) {
        this._tmpmapid = tmpmapid;
    }

    @Override
    public int get_locx1() {
        return _locx1;
    }

    @Override
    public void set_locx1(int locx1) {
        this._locx1 = locx1;
    }

    @Override
    public int get_locy1() {
        return _locy1;
    }

    @Override
    public void set_locy1(int locy1) {
        this._locy1 = locy1;
    }

    @Override
    public int get_locx2() {
        return _locx2;
    }

    @Override
    public void set_locx2(int locx2) {
        this._locx2 = locx2;
    }

    @Override
    public int get_locy2() {
        return _locy2;
    }

    @Override
    public void set_locy2(int locy2) {
        this._locy2 = locy2;
    }

    @Override
    public int get_heading() {
        return _heading;
    }

    @Override
    public void set_heading(int heading) {
        this._heading = heading;
    }

    @Override
    public short get_mapid() {
        return _mapid;
    }

    @Override
    public void set_mapid(short mapid) {
        this._mapid = mapid;
    }

    @Override
    public Timestamp get_nextSpawnTime() {
        return _nextSpawnTime;
    }

    @Override
    public void set_nextSpawnTime(Timestamp nextSpawnTime) {
        this._nextSpawnTime = nextSpawnTime;
    }

    @Override
    public long get_spawnInterval() {
        return _spawnInterval;
    }

    @Override
    public void set_spawnInterval(int spawnInterval) {
        this._spawnInterval = spawnInterval;
    }

    @Override
    public int get_existTime() {
        return _existTime;
    }

    @Override
    public void set_existTime(int existTime) {
        this._existTime = existTime;
    }

    /**
     * 区域召唤
     * 
     * @return
     */
    private boolean isAreaSpawn() {
        return (_locx1 != 0) && (_locy1 != 0) && (_locx2 != 0) && (_locy2 != 0);
    }

    /**
     * 重新召唤
     * 
     * @param objectId
     *            世界物件编号
     */
    @Override
    public void doSpawn(final int objectId) {
        // 目前时间
        final Timestamp ts = new Timestamp(System.currentTimeMillis());
        // 未达召唤时间
        if (ts.before(_nextSpawnTime)) {
            return;
        }
        _tmplocx = 0;// 本次召唤X座标
        _tmplocy = 0;// 本次召唤Y座标
        _tmpmapid = 0;// 本次召唤地图编号
        try {
            int newlocx = _locx1;
            int newlocy = _locy1;
            int tryCount = 0;// 执行次数

            final L1NpcInstance npcTemp = NpcTable.get().newNpcInstance(
                    _template);

            if (objectId == 0) {
                npcTemp.setId(IdFactoryNpc.get().nextId());

            } else {
                npcTemp.setId(objectId); // 世界物件编号再利用
            }

            int heading = Math.min(_heading, 7);
            npcTemp.setHeading(heading);

            // npc召唤地图换位
            final int npcId = npcTemp.getNpcTemplate().get_npcId();
            if ((npcId == 45488) && (_mapid == 9)) { // 卡士伯
                npcTemp.setMap((short) (_mapid + _random.nextInt(2)));

            } else if ((npcId == 45601) && (_mapid == 11)) { // 死亡骑士
                npcTemp.setMap((short) (_mapid + _random.nextInt(3)));

            } else {
                npcTemp.setMap(_mapid);
            }

            npcTemp.setMovementDistance(80);// BOSS 移动距离预设80格
            npcTemp.setRest(false);

            // 设置召唤的XY座标位置
            while (tryCount <= 50) {
                if (this.isAreaSpawn()) { // 区域召唤
                    final int rangeX = _locx2 - _locx1;
                    final int rangeY = _locy2 - _locy1;
                    newlocx = _random.nextInt(rangeX) + _locx1;
                    newlocy = _random.nextInt(rangeY) + _locy1;

                } else { // 定点召唤
                    newlocx = _locx1;
                    newlocy = _locy1;
                }

                // BOSS 召唤不闪避PC
                npcTemp.setX(newlocx);
                npcTemp.setHomeX(newlocx);
                npcTemp.setY(newlocy);
                npcTemp.setHomeY(newlocy);

                // 该召换点位置可通行
                if (npcTemp.getMap().isInMap(npcTemp.getLocation())
                        && npcTemp.getMap().isPassable(npcTemp.getLocation(),
                                npcTemp)) {
                    break;
                }
                tryCount++;
            }

            if (npcTemp instanceof L1MonsterInstance) {
                ((L1MonsterInstance) npcTemp).initHide();
            }

            npcTemp.setSpawn(null);
            npcTemp.setreSpawn(true);
            npcTemp.setSpawnNumber(_id); // L1Spawn管理编号

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

            World.get().storeObject(npcTemp);
            World.get().addVisibleObject(npcTemp);

            if (npcTemp instanceof L1MonsterInstance) {
                final L1MonsterInstance mobtemp = (L1MonsterInstance) npcTemp;
                if (mobtemp.getHiddenStatus() == 0) {
                    mobtemp.onNpcAI(); // AI启用
                }

                if (_existTime > 0) {
                    // 存在时间(秒)
                    mobtemp.set_spawnTime(_existTime * 1000);
                }
            }

            // 具备NPC队伍
            if (_groupId != 0) {
                // 召唤队伍成员
                L1MobGroupSpawn.getInstance().doSpawn(npcTemp, _groupId, true,
                        true);
            }

            npcTemp.turnOnOffLight();
            npcTemp.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // チャット开始

            _tmplocx = newlocx;
            _tmplocy = newlocy;
            _tmpmapid = npcTemp.getMapId();

            L1WorldMap.get().getMap(npcTemp.getMapId())
                    .setPassable(npcTemp.getX(), npcTemp.getY(), false, 2);

            if (WorldSpawnBoss.get().get(npcTemp.getId()) == null) {
                WorldSpawnBoss.get().put(npcTemp.getId(), this);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void init() {
        try {
            int spawnNum = 0;
            while (spawnNum < _maximumCount) {
                setSpawn();
                spawnNum++;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void setSpawn() {
        try {
            doSpawn(0);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
