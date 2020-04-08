package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.L1Spawn;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1QuestMobSpawn;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * QUEST NPC召唤资料
 * 
 * @author dexc
 * 
 */
public class QuesttSpawnTable {

    private static final Log _log = LogFactory.getLog(QuesttSpawnTable.class);

    private static QuesttSpawnTable _instance;

    // 一般招换资讯
    private static final Map<Integer, L1Spawn> _spawntable = new HashMap<Integer, L1Spawn>();

    // 副本召唤资讯
    private static final Map<Integer, L1QuestMobSpawn> _spawntable_X1 = new HashMap<Integer, L1QuestMobSpawn>();

    public static QuesttSpawnTable get() {
        if (_instance == null) {
            _instance = new QuesttSpawnTable();
        }
        return _instance;
    }

    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        int spawnCount = 0;
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `server_quest_spawn`");
            rs = pstm.executeQuery();

            while (rs.next()) {
                final int id = rs.getInt("id");
                final int questid = rs.getInt("questid");
                final boolean is_spawn = rs.getBoolean("is_spawn");// true:一般召唤
                                                                   // false:副本用召唤

                final int npc_templateid = rs.getInt("npc_templateid");
                final L1Npc template1 = NpcTable.get().getTemplate(
                        npc_templateid);

                if (template1 == null) {
                    _log.error("召唤NPC编号: " + npc_templateid
                            + " 不存在资料库中!(server_quest_spawn)");
                    delete(npc_templateid);
                    continue;
                }

                if (!is_spawn) {// 不执行一般召唤的副本NPC
                    final int count = rs.getInt("count");
                    final int group_id = rs.getInt("group_id");
                    final int locx1 = rs.getInt("locx1");
                    final int locx2 = rs.getInt("locx2");
                    final int locy1 = rs.getInt("locy1");
                    final int locy2 = rs.getInt("locy2");
                    final int mapid = rs.getInt("mapid");
                    final int heading = rs.getInt("heading");

                    final L1QuestMobSpawn mobSpawn = new L1QuestMobSpawn();
                    mobSpawn.set_questid(questid);
                    mobSpawn.set_count(count);
                    mobSpawn.set_npc_templateid(npc_templateid);
                    mobSpawn.set_group_id(group_id);
                    mobSpawn.set_locx1(locx1);
                    mobSpawn.set_locy1(locy1);
                    mobSpawn.set_locx2(locx2);
                    mobSpawn.set_locy2(locy2);
                    mobSpawn.set_heading(heading);
                    mobSpawn.set_mapid(mapid);

                    _spawntable_X1.put(id, mobSpawn);

                } else {
                    // 检查任务编号 是否启用
                    if (QuestTable.get().getTemplate(questid) != null) {
                        final int count = rs.getInt("count");
                        if (count == 0) {
                            continue;
                        }

                        final int group_id = rs.getInt("group_id");

                        final int locx1 = rs.getInt("locx1");
                        final int locx2 = rs.getInt("locx2");
                        final int locy1 = rs.getInt("locy1");
                        final int locy2 = rs.getInt("locy2");
                        final int mapid = rs.getInt("mapid");
                        final int heading = rs.getInt("heading");
                        final int respawn_delay = rs.getInt("respawn_delay");
                        final int movement_distance = rs
                                .getInt("movement_distance");
                        final int near_spawn = rs.getInt("near_spawn");

                        // 座标数据为0 忽略
                        if ((locx1 == 0) && (locx2 == 0) && (locy1 == 0)
                                && (locy2 == 0)) {
                            continue;
                        }

                        final L1Spawn spawnDat = new L1Spawn(template1);
                        spawnDat.setId(id);
                        spawnDat.setAmount(count);
                        spawnDat.setGroupId(group_id);

                        if ((locx2 == 0) && (locy2 == 0)) {
                            spawnDat.setLocX(locx1);
                            spawnDat.setLocY(locy1);
                            spawnDat.setLocX1(0);
                            spawnDat.setLocY1(0);
                            spawnDat.setLocX2(0);
                            spawnDat.setLocY2(0);

                        } else {
                            spawnDat.setLocX(locx1);
                            spawnDat.setLocY(locy1);
                            spawnDat.setLocX1(locx1);
                            spawnDat.setLocY1(locy1);
                            spawnDat.setLocX2(locx2);
                            spawnDat.setLocY2(locy2);
                        }

                        // 召唤数量2以上
                        if (count > 1 && spawnDat.getLocX1() == 0) {
                            final int range = Math.min(count * 6, 20);
                            spawnDat.setLocX1(spawnDat.getLocX() - range);
                            spawnDat.setLocY1(spawnDat.getLocY() - range);
                            spawnDat.setLocX2(spawnDat.getLocX() + range);
                            spawnDat.setLocY2(spawnDat.getLocY() + range);
                        }

                        if ((locx2 < locx1) && locx2 != 0) {
                            _log.error("server_quest_spawn : locx2 < locx1: "
                                    + id);
                            continue;
                        }

                        if ((locy2 < locy1) && locy2 != 0) {
                            _log.error("server_quest_spawn : locy2 < locy1: "
                                    + id);
                            continue;
                        }

                        spawnDat.setRandomx(0);
                        spawnDat.setRandomy(0);

                        spawnDat.setMinRespawnDelay(respawn_delay);
                        spawnDat.setMovementDistance(movement_distance);

                        spawnDat.setHeading(heading);
                        spawnDat.setMapId((short) mapid);
                        spawnDat.setSpawnType(near_spawn);// 是否闪避玩家召唤

                        spawnDat.setTime(SpawnTimeTable.getInstance().get(
                                spawnDat.getId()));

                        spawnDat.setName(template1.get_name());

                        spawnDat.init();
                        spawnCount += spawnDat.getAmount();
                        _spawntable
                                .put(new Integer(spawnDat.getId()), spawnDat);
                    }
                }
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入召唤QUEST NPC资料数量: " + spawnCount + "(" + timer.get()
                + "ms)");
    }

    /**
     * 删除错误资料
     * 
     * @param npc_id
     */
    public static void delete(final int npc_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `server_quest_spawn` WHERE `npc_templateid`=?");
            ps.setInt(1, npc_id);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    /**
     * QUEST NPC/MOB召唤列表中物件
     * 
     * @param id
     * @return
     */
    public L1Spawn getTemplate(final int id) {
        return _spawntable.get(new Integer(id));
    }

    /**
     * QUEST NPC/MOB 副本 召唤列表中物件清单
     * 
     * @param questid
     * @return
     */
    public ArrayList<L1QuestMobSpawn> getMobSpawn(final int questid) {
        final ArrayList<L1QuestMobSpawn> spawnList = new ArrayList<L1QuestMobSpawn>();
        for (L1QuestMobSpawn mobSpawn : _spawntable_X1.values()) {
            if (mobSpawn.get_questid() == questid) {
                spawnList.add(mobSpawn);
            }
        }
        return spawnList;
    }
}
