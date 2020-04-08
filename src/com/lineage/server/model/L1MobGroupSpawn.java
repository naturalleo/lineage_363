package com.lineage.server.model;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.MobGroupTable;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.templates.L1MobGroup;
import com.lineage.server.templates.L1NpcCount;
import com.lineage.server.world.World;

/**
 * 召唤NPC队员
 * 
 * @author daien
 * 
 */
public class L1MobGroupSpawn {

    private static final Log _log = LogFactory.getLog(L1MobGroupSpawn.class);

    private static L1MobGroupSpawn _instance;

    private static Random _random = new Random();

    private boolean _isRespawnScreen;// 召唤位置不可与队长重叠

    private boolean _isInitSpawn;// AI启用

    /**
     * 召唤NPC队员
     */
    private L1MobGroupSpawn() {
    }

    public static L1MobGroupSpawn getInstance() {
        if (_instance == null) {
            _instance = new L1MobGroupSpawn();
        }
        return _instance;
    }

    /**
     * 召唤NPC队员
     * 
     * @param leader
     *            队长
     * @param groupId
     *            队伍编号
     * @param isRespawnScreen
     *            召唤位置重叠队长 true:不重叠 false:重叠
     * @param isInitSpawn
     *            AI启用
     */
    public void doSpawn(final L1NpcInstance leader, final int groupId,
            final boolean isRespawnScreen, final boolean isInitSpawn) {

        final L1MobGroup mobGroup = MobGroupTable.get().getTemplate(groupId);
        if (mobGroup == null) {
            return;
        }

        L1NpcInstance mob;
        this._isRespawnScreen = isRespawnScreen;
        this._isInitSpawn = isInitSpawn;

        final L1MobGroupInfo mobGroupInfo = new L1MobGroupInfo();

        mobGroupInfo.setRemoveGroup(mobGroup.isRemoveGroupIfLeaderDie());
        mobGroupInfo.addMember(leader);// 加入队长

        for (final L1NpcCount minion : mobGroup.getMinions()) {
            if (minion.isZero()) {
                continue;
            }
            for (int i = 0; i < minion.getCount(); i++) {
                mob = this.spawn(leader, minion.getId());
                if (mob != null) {
                    mobGroupInfo.addMember(mob);
                }
            }
        }
    }

    private L1NpcInstance spawn(final L1NpcInstance leader, final int npcId) {
        L1NpcInstance mob = null;
        try {
            mob = NpcTable.get().newNpcInstance(npcId);

            mob.setId(IdFactoryNpc.get().nextId());

            mob.setHeading(leader.getHeading());
            mob.setMap(leader.getMapId());
            mob.setMovementDistance(leader.getMovementDistance());
            mob.setRest(leader.isRest());

            mob.setX(leader.getX() + _random.nextInt(5) - 2);
            mob.setY(leader.getY() + _random.nextInt(5) - 2);
            // 判断召唤位置
            if (!this.canSpawn(mob)) {
                // 该位置判断不成立 位置设置为主人位置
                mob.setX(leader.getX());
                mob.setY(leader.getY());
            }
            mob.setHomeX(mob.getX());
            mob.setHomeY(mob.getY());

            if (mob instanceof L1MonsterInstance) {
                ((L1MonsterInstance) mob).initHideForMinion(leader);
            }

            // mob.setSpawn(leader.getSpawn());
            // mob.setreSpawn(leader.isReSpawn());
            // mob.setSpawnNumber(leader.getSpawnNumber());
            mob.setreSpawn(false);// 队员不重新召唤

            // 地狱不掉落物品
            if (mob instanceof L1MonsterInstance) {
                if (mob.getMapId() == 666) {
                    ((L1MonsterInstance) mob).set_storeDroped(true);
                }
            }

            // 设置副本编号 TODO
            mob.set_showId(leader.get_showId());

            World.get().storeObject(mob);
            World.get().addVisibleObject(mob);

            // 队长具有删除时间 同时设置队员一并删除
            if (leader.is_spawnTime()) {
                mob.set_spawnTime(leader.get_spawnTime());
            }

            if (mob instanceof L1MonsterInstance) {
                if (!this._isInitSpawn && (mob.getHiddenStatus() == 0)) {
                    mob.onNpcAI(); // AI启用
                }
            }
            mob.turnOnOffLight();
            mob.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 出现

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        }
        return mob;
    }

    private boolean canSpawn(final L1NpcInstance mob) {
        // 召唤位置未超出地图可用位置
        if (mob.getMap().isInMap(mob.getLocation())
                && mob.getMap().isPassable(mob.getLocation(), mob)) {
            if (this._isRespawnScreen) {
                return true;
            }
            if (World.get().getVisiblePlayer(mob).size() == 0) {
                return true;
            }
        }
        return false;
    }

}
