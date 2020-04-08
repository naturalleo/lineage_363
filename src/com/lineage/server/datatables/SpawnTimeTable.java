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
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1SpawnTime;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 召唤时间资料(指定时间召唤NPC) 固定时间之间执行召唤
 * 
 * @author dexc
 * 
 */
public class SpawnTimeTable {

    private static final Log _log = LogFactory.getLog(SpawnTimeTable.class);

    private static SpawnTimeTable _instance;

    private static final Map<Integer, L1SpawnTime> _times = new HashMap<Integer, L1SpawnTime>();

    public static SpawnTimeTable getInstance() {
        if (_instance == null) {
            _instance = new SpawnTimeTable();
        }
        return _instance;
    }

    private SpawnTimeTable() {
        final PerformanceTimer timer = new PerformanceTimer();
        this.load();
        _log.info("载入召唤时间资料数量: " + _times.size() + "(" + timer.get() + "ms)");
    }

    public L1SpawnTime get(final int id) {
        return _times.get(id);
    }

    private void load() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `spawnlist_time`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                final int npc_id = rs.getInt("npc_id");
                final L1Npc l1npc = NpcTable.get().getTemplate(npc_id);
                if (l1npc == null) {
                    _log.error("召唤NPC编号: " + npc_id
                            + " 不存在资料库中!(spawnlist_time)");
                    delete(npc_id);
                    continue;
                }

                final L1SpawnTime.L1SpawnTimeBuilder builder = new L1SpawnTime.L1SpawnTimeBuilder(
                        npc_id);
                builder.setTimeStart(rs.getTime("time_start"));
                builder.setTimeEnd(rs.getTime("time_end"));
                builder.setDeleteAtEndTime(rs.getBoolean("delete_at_endtime"));

                _times.put(npc_id, builder.build());
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
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
            ps = cn.prepareStatement("DELETE FROM `spawnlist_time` WHERE `npc_id`=?");
            ps.setInt(1, npc_id);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }
}
