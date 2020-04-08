package com.lineage.server.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestMobExecutor;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.QuesttSpawnTable;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1MobGroupSpawn;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.serverpackets.S_HelpMessage;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.utils.ListMapUtil;
import com.lineage.server.utils.PerformanceTimer;

/**
 * 执行中副本组暂存
 * 
 * @author daien
 * 
 */
public class L1QuestUser {

    private static final Log _log = LogFactory.getLog(L1QuestUser.class);

    private final int _id;// 副本唯一编号

    private final int _questid;// 副本任务编号

    private final short _mapid;// 副本执行地图编号

    // private boolean _mobNull = true;// 怪物剩余0特殊处理

    private QuestMobExecutor _mobNull = null;// 怪物剩余0特殊处理

    private boolean _info = true;// 怪物剩余讯息

    private boolean _outStop = false;// 该副本参加者其中之一离开 是否立即结束

    private int _time = -1;// 进入时间限制

    private final ArrayList<L1PcInstance> _userList;// 参加副本的PC

    private final ArrayList<L1NpcInstance> _npcList;// 副本中召唤的NPC

    /**
     * 执行副本组暂存
     * 
     * @param id
     *            副本唯一编号
     * @param mapid
     *            副本执行地图编号
     * @param questid
     *            副本任务编号
     */
    public L1QuestUser(final int id, final int mapid, final int questid) {
        _id = id;
        _mapid = (short) mapid;
        _questid = questid;
        _userList = new ArrayList<L1PcInstance>();
        _npcList = new ArrayList<L1NpcInstance>();
    }

    /**
     * 副本唯一编号
     * 
     * @return
     */
    public int get_id() {
        return _id;
    }

    /**
     * 副本任务编号
     * 
     * @return
     */
    public int get_questid() {
        return _questid;
    }

    /**
     * 副本地图编号
     * 
     * @return
     */
    public int get_mapid() {
        return _mapid;
    }

    /**
     * 加入副本执行成员
     * 
     * @param pc
     */
    public void add(final L1PcInstance pc) {
        try {
            // 列表中不包含该项元素
            if (!_userList.contains(pc)) {
                _userList.add(pc);
                pc.set_showId(_id);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            _log.info("加入副本执行成员(" + _questid + "-" + _id + "):" + pc.getName());
        }
    }

    /**
     * 移出副本执行成员
     * 
     * @param pc
     */
    public void remove(final L1PcInstance pc) {
        try {
            // 列表中包含该项元素
            if (_userList.remove(pc)) {
                // _userList.remove(pc);
                pc.set_showId(-1);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            _log.info("移出副本执行成员(" + _questid + "-" + _id + "):" + pc.getName());
        }
    }

    /**
     * 进入时间限制(单位:秒)<BR>
     * -1 不限制
     * 
     * @param time
     */
    public void set_time(final int time) {
        this._time = time;
    }

    /**
     * 进入时间限制(单位:秒)<BR>
     * -1 不限制
     * 
     * @return
     */
    public int get_time() {
        return _time;
    }

    /**
     * 具有时间限制
     * 
     * @return true:有 false:没有
     */
    public boolean is_time() {
        return _time != -1;
    }

    /**
     * 该执行中副本剩余PC
     * 
     * @return
     */
    public ArrayList<L1PcInstance> pcList() {
        return _userList;
    }

    /**
     * 该执行中副本剩余人数
     * 
     * @return
     */
    public int size() {
        return _userList.size();
    }

    /**
     * 该执行中副本剩余NPC
     * 
     * @return
     */
    public List<L1NpcInstance> npcList() {
        return _npcList;
    }

    /**
     * 增加副本中NPC
     * 
     * @param door
     */
    public void addNpc(L1NpcInstance npc) {
        _npcList.add(npc);
    }

    /**
     * 该执行中副本中指定NPCID的NPC
     * 
     * @return
     */
    public ArrayList<L1NpcInstance> npcList(int npcid) {
        final ArrayList<L1NpcInstance> npcList = new ArrayList<L1NpcInstance>();
        for (L1NpcInstance npc : _npcList) {
            // ID相等 并且未死亡
            if (npc.getNpcId() == npcid && !npc.isDead()) {
                npcList.add(npc);
            }
        }
        if (npcList.size() <= 0) {
            return null;
        }
        return npcList;
    }

    /**
     * 该执行中副本剩余NPC(全部)数量
     * 
     * @return
     */
    public int npcSize() {
        return _npcList.size();
    }

    /**
     * 该执行中副本剩余NPC(怪物)数量
     * 
     * @return
     */
    public int mobSize() {
        int i = 0;
        for (L1NpcInstance npc : _npcList) {
            // 是怪物
            if (npc instanceof L1MonsterInstance) {
                i += 1;
            }
        }
        return i;
    }

    /**
     * 召唤副本怪物
     * 
     * @param get_id
     *            任务编号
     */
    public void spawnQuestMob() {
        final PerformanceTimer timer = new PerformanceTimer();
        try {
            // 取回召唤列表
            final ArrayList<L1QuestMobSpawn> spawnList = QuesttSpawnTable.get()
                    .getMobSpawn(_questid);

            if (spawnList.size() > 0) {// 列表中具有物件
                for (L1QuestMobSpawn mobSpawn : spawnList) {
                    // 指定地图编号
                    if (mobSpawn.get_mapid() == _mapid) {
                        // 召唤数量
                        final int count = mobSpawn.get_count();
                        if (count > 0) {
                            // 回圈召唤数量
                            for (int i = 0; i < count; i++) {
                                spawn(mobSpawn);
                            }
                        }
                    }
                }
            }
            spawnList.clear();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            _log.info("副本任务启动(" + _questid + "-" + _id + ") NPC完成召唤 数量:"
                    + _npcList.size() + "(" + timer.get() + "ms)");
        }
    }

    private Random _random = new Random();

    /**
     * 召唤NPC
     * 
     * @param mobSpawn
     */
    private void spawn(final L1QuestMobSpawn mobSpawn) {
        try {
            final int npcid = mobSpawn.get_npc_templateid();
            final int group_id = mobSpawn.get_group_id();
            final int locx1 = mobSpawn.get_locx1();
            final int locy1 = mobSpawn.get_locy1();
            final int locx2 = mobSpawn.get_locx2();
            final int locy2 = mobSpawn.get_locy2();
            final int heading = mobSpawn.get_heading();
            final int mapid = mobSpawn.get_mapid();
            final L1Npc template = NpcTable.get().getTemplate(npcid);
            if (template == null) {
                _log.error("召唤NPC编号: " + npcid + " 不存在资料库中!");

            } else {
                // 区域召唤
                if (locx1 != 0 && locy1 != 0 && locx2 != 0 && locy2 != 0) {
                    int x = 0;
                    int y = 0;

                    final L1Map map = L1WorldMap.get().getMap((short) mapid);
                    int tryCount = 0;
                    // 设置召唤的XY座标位置(50次 定位循环)
                    while (tryCount <= 50) {
                        x = _random.nextInt((locx2 - locx1)) + locx1;
                        y = _random.nextInt((locy2 - locy1)) + locy1;

                        // 座标可通行决定召唤位置
                        if (map.isInMap(x, y) && map.isPassable(x, y, null)) {
                            final L1Location loc = new L1Location(x, y, mapid);
                            final L1NpcInstance mob = L1SpawnUtil.spawn(npcid,
                                    loc, heading, _id);
                            /*
                             * if (mob instanceof L1MonsterInstance) {
                             * ((L1MonsterInstance) mob).set_storeDroped(false);
                             * }
                             */
                            // System.out.println(mob.get_showId() +
                            // " 任务地图内物件:"+mob.getNpcTemplate().get_name());
                            _npcList.add(mob);// 加入列表
                            groupSpawn(group_id, mob);// 召唤队伍成员
                            break;
                        }
                        tryCount++;
                    }

                } else {
                    final L1Location loc = new L1Location(locx1, locy1, mapid);
                    final L1NpcInstance mob = L1SpawnUtil.spawn(npcid, loc,
                            heading, _id);
                    // System.out.println(mob.get_showId() +
                    // " 任务地图内物件:"+mob.getNpcTemplate().get_name());
                    _npcList.add(mob);// 加入列表
                    groupSpawn(group_id, mob);// 召唤队伍成员
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 召唤队伍成员
     * 
     * @param group_id
     * @param mob
     */
    private void groupSpawn(final int group_id, final L1NpcInstance mob) {
        if (group_id != 0) {
            // 召唤队伍成员
            L1MobGroupSpawn.getInstance().doSpawn(mob, group_id, true, true);
        }
        // NPC具有队伍状态
        if (mob.getMobGroupInfo() != null) {
            for (L1NpcInstance mobx : mob.getMobGroupInfo().getList()) {
                if (!mobx.equals(mob)) {// 不是队长
                    _npcList.add(mobx);// 队员加入列表
                }
            }
        }
    }

    /**
     * 移除副本怪物
     * 
     * @param mob
     */
    public void removeMob(final L1NpcInstance mob) {
        try {
            // 移除NPC
            if (_npcList.remove(mob)) {
                if (is_info()) {
                    // 13:剩余怪物：
                    sendPackets(new S_HelpMessage("\\fY剩余怪物：" + mobSize()));
                }
            }
            if (mobSize() <= 0) {
                if (this._mobNull != null) {
                    this._mobNull.stopQuest(this);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 移除副本怪物
     */
    public void removeMob() {
        try {
            ArrayList<L1NpcInstance> allList = new ArrayList<L1NpcInstance>();
            allList.addAll(_npcList);
            // 移除NPC
            for (L1NpcInstance npc : allList) {
                npc.deleteMe();
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            // 清空副本NPC清单
            ListMapUtil.clear(_npcList);

            _log.info("副本任务结束(" + _questid + "-" + _id + ")");
        }
    }

    /**
     * 完成任务结束副本
     */
    public void endQuest() {
        try {
            // 移除玩家
            for (L1PcInstance pc : _userList) {
                if (pc.getMapId() == _mapid) {
                    // 传送成员离开(奇岩 十字架下)
                    L1Teleport.teleport(pc, 33430, 32814, (short) 4, 4, true);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            ListMapUtil.clear(_userList);
        }
    }

    /**
     * 发送封包资料给予任务中执行玩家
     * 
     * @param s_HelpMessage
     */
    public void sendPackets(final ServerBasePacket basePacket) {
        try {
            for (L1PcInstance pc : _userList) {
                pc.sendPackets(basePacket);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        }
    }

    /**
     * 怪物剩余讯息
     * 
     * @param _info
     */
    public void set_info(boolean _info) {
        this._info = _info;
    }

    /**
     * 怪物剩余讯息
     * 
     * @return
     */
    public boolean is_info() {
        return _info;
    }

    /**
     * 该副本参加者其中之一离开 是否立即结束
     * 
     * @param _outStop
     */
    public void set_outStop(boolean _outStop) {
        this._outStop = _outStop;
    }

    /**
     * 该副本参加者其中之一离开 是否立即结束
     * 
     * @return
     */
    public boolean is_outStop() {
        return _outStop;
    }

    /**
     * 怪物剩余0特殊处理
     * 
     * @param _outStop
     */
    public void set_object(QuestMobExecutor mobNull) {
        this._mobNull = mobNull;
    }

    /**
     * 怪物剩余0特殊处理
     * 
     * @return
     */
    public QuestMobExecutor get_object() {
        return _mobNull;
    }
}
