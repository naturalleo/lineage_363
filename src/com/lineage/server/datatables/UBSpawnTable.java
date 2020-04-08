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
import com.lineage.server.model.L1UbPattern;
import com.lineage.server.model.L1UbSpawn;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 无线大赛召唤资料
 * 
 * @author dexc
 * 
 */
public class UBSpawnTable {

    private static final Log _log = LogFactory.getLog(UBSpawnTable.class);

    private static UBSpawnTable _instance;

    private static final Map<Integer, L1UbSpawn> _spawnTable = new HashMap<Integer, L1UbSpawn>();;

    public static UBSpawnTable getInstance() {
        if (_instance == null) {
            _instance = new UBSpawnTable();
        }
        return _instance;
    }

    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();

        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {

            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `spawnlist_ub`");
            rs = pstm.executeQuery();

            while (rs.next()) {
                final int npcid = rs.getInt(6);
                final L1Npc npcTemp = NpcTable.get().getTemplate(npcid);
                if (npcTemp == null) {
                    _log.error("召唤NPC编号: " + npcid + " 不存在资料库中!(spawnlist_ub)");
                    delete(npcid);
                    continue;
                }

                final L1UbSpawn spawnDat = new L1UbSpawn();
                spawnDat.setId(rs.getInt(1));
                spawnDat.setUbId(rs.getInt(2));
                spawnDat.setPattern(rs.getInt(3));
                spawnDat.setGroup(rs.getInt(4));
                spawnDat.setName(npcTemp.get_name());
                spawnDat.setNpcTemplateId(npcid);
                spawnDat.setAmount(rs.getInt(7));
                spawnDat.setSpawnDelay(rs.getInt(8));
                spawnDat.setSealCount(rs.getInt(9));

                _spawnTable.put(spawnDat.getId(), spawnDat);
            }
        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入无线大赛召唤资料数量: " + _spawnTable.size() + "(" + timer.get()
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
            ps = cn.prepareStatement("DELETE FROM `spawnlist_ub` WHERE `npc_templateid`=?");
            ps.setInt(1, npc_id);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    public L1UbSpawn getSpawn(final int spawnId) {
        return _spawnTable.get(spawnId);
    }

    /**
     * 指定されたUBIDに对するパターンの最大数を返す。
     * 
     * @param ubId
     *            调べるUBID。
     * @return パターンの最大数。
     */
    public int getMaxPattern(final int ubId) {
        int n = 0;
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("SELECT MAX(pattern) FROM `spawnlist_ub` WHERE `ub_id`=?");
            pstm.setInt(1, ubId);
            rs = pstm.executeQuery();
            if (rs.next()) {
                n = rs.getInt(1);
            }
        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return n;
    }

    public L1UbPattern getPattern(final int ubId, final int patternNumer) {
        final L1UbPattern pattern = new L1UbPattern();
        for (final L1UbSpawn spawn : _spawnTable.values()) {
            if ((spawn.getUbId() == ubId)
                    && (spawn.getPattern() == patternNumer)) {
                pattern.addSpawn(spawn.getGroup(), spawn);
            }
        }
        pattern.freeze();

        return pattern;
    }
}
