package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.L1Spawn;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.utils.NumberUtil;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * MOB召唤资料
 * 
 * @author dexc
 * 
 */
public class SpawnTable {

    private static final Log _log = LogFactory.getLog(SpawnTable.class);

    private static SpawnTable _instance;

    private static final Map<Integer, L1Spawn> _spawntable = new HashMap<Integer, L1Spawn>();

    public static SpawnTable get() {
        if (_instance == null) {
            _instance = new SpawnTable();
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
            pstm = con.prepareStatement("SELECT * FROM `spawnlist`");
            rs = pstm.executeQuery();

            while (rs.next()) {
                final int npcTemplateId = rs.getInt("npc_templateid");
                final L1Npc template1 = NpcTable.get().getTemplate(
                        npcTemplateId);

                if (template1 == null) {
                    _log.error("召唤MOB编号: " + npcTemplateId + " 不存在资料库中!");
                    delete(npcTemplateId);
                    continue;
                }

                int count = rs.getInt("count");

                if (count == 0) {
                    continue;
                }

                final double amount_rate = MapsTable.get().getMonsterAmount(
                        rs.getShort("mapid"));
                count = calcCount(template1, count, amount_rate);

                if (count == 0) {
                    continue;
                }

                final L1Spawn spawnDat = new L1Spawn(template1);
                spawnDat.setId(rs.getInt("id"));
                spawnDat.setAmount(count);
                spawnDat.setGroupId(rs.getInt("group_id"));
                spawnDat.setLocX(rs.getInt("locx"));
                spawnDat.setLocY(rs.getInt("locy"));
                spawnDat.setRandomx(rs.getInt("randomx"));
                spawnDat.setRandomy(rs.getInt("randomy"));
                spawnDat.setLocX1(rs.getInt("locx1"));
                spawnDat.setLocY1(rs.getInt("locy1"));
                spawnDat.setLocX2(rs.getInt("locx2"));
                spawnDat.setLocY2(rs.getInt("locy2"));
                spawnDat.setHeading(rs.getInt("heading"));
                spawnDat.setMinRespawnDelay(rs.getInt("min_respawn_delay"));
                spawnDat.setMaxRespawnDelay(rs.getInt("max_respawn_delay"));
                spawnDat.setMapId(rs.getShort("mapid"));
                spawnDat.setRespawnScreen(rs.getBoolean("respawn_screen"));
                spawnDat.setMovementDistance(rs.getInt("movement_distance"));
                spawnDat.setRest(rs.getBoolean("rest"));// 召唤后动作暂停
                spawnDat.setSpawnType(rs.getInt("near_spawn"));
                spawnDat.setTime(SpawnTimeTable.getInstance()
                        .get(npcTemplateId));

                spawnDat.setName(template1.get_name());

                if ((count > 1) && (spawnDat.getLocX1() == 0)) {
                    // 复数かつ固定spawnの场合は、个体数 * 6 の范围spawnに变える。
                    // ただし范围が30を超えないようにする
                    final int range = Math.min(count * 6, 30);
                    spawnDat.setLocX1(spawnDat.getLocX() - range);
                    spawnDat.setLocY1(spawnDat.getLocY() - range);
                    spawnDat.setLocX2(spawnDat.getLocX() + range);
                    spawnDat.setLocY2(spawnDat.getLocY() + range);
                }

                // start the spawning
                spawnDat.init();
                spawnCount += spawnDat.getAmount();

                _spawntable.put(new Integer(spawnDat.getId()), spawnDat);
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入召唤MOB资料数量: " + spawnCount + "(" + timer.get() + "ms)");
    }

    /**
     * MOB召唤列表中物件
     * 
     * @param Id
     * @return
     */
    public L1Spawn getTemplate(final int Id) {
        return _spawntable.get(new Integer(Id));
    }

    /**
     * 删除错误资料
     * 
     * @param clan_id
     */
    public static void delete(final int npc_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `spawnlist` WHERE `npc_templateid`=?");
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
     * 手动增加SPAWN物件
     * 
     * @param pc
     * @param npc
     */
    public static void storeSpawn(final L1PcInstance pc, final L1Npc npc) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            final int count = 1;
            final int randomXY = 12;
            final int minRespawnDelay = 60;
            final int maxRespawnDelay = 120;
            final String note = npc.get_name();

            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("INSERT INTO `spawnlist` SET `location`=?,`count`=?,"
                            + "`npc_templateid`=?,`group_id`=?,`locx`=?,`locy`=?,`randomx`=?,"
                            + "`randomy`=?,`heading`=?,`min_respawn_delay`=?,"
                            + "`max_respawn_delay`=?,`mapid`=?");
            pstm.setString(1, note);
            pstm.setInt(2, count);
            pstm.setInt(3, npc.get_npcId());
            pstm.setInt(4, 0);
            pstm.setInt(5, pc.getX());
            pstm.setInt(6, pc.getY());
            pstm.setInt(7, randomXY);
            pstm.setInt(8, randomXY);
            pstm.setInt(9, pc.getHeading());
            pstm.setInt(10, minRespawnDelay);
            pstm.setInt(11, maxRespawnDelay);
            pstm.setInt(12, pc.getMapId());
            pstm.execute();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    private static int calcCount(final L1Npc npc, final int count,
            final double rate) {
        if (rate == 0) {
            return 0;
        }
        if ((rate == 1) || npc.isAmountFixed()) {
            return count;

        } else {
            return NumberUtil.randomRound((count * rate));
        }
    }
}
