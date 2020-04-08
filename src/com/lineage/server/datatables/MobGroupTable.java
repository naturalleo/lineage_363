package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1MobGroup;
import com.lineage.server.templates.L1NpcCount;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.utils.collections.Lists;

/**
 * MOB队伍资料
 * 
 * @author dexc
 * 
 */
public class MobGroupTable {

    private static final Log _log = LogFactory.getLog(MobGroupTable.class);

    private static MobGroupTable _instance;

    private static final Map<Integer, L1MobGroup> _mobGroupIndex = new HashMap<Integer, L1MobGroup>();

    public static MobGroupTable get() {
        if (_instance == null) {
            _instance = new MobGroupTable();
        }
        return _instance;
    }

    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `mobgroup`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                final int mobGroupId = rs.getInt("id");
                final boolean isRemoveGroup = (rs
                        .getBoolean("remove_group_if_leader_die"));// 是否解散队伍
                final int leaderId = rs.getInt("leader_id");
                final List<L1NpcCount> minions = Lists.newArrayList();
                for (int i = 1; i <= 7; i++) {
                    final int id = rs.getInt("minion" + i + "_id");
                    final int count = rs.getInt("minion" + i + "_count");
                    minions.add(new L1NpcCount(id, count));
                }
                final L1MobGroup mobGroup = new L1MobGroup(mobGroupId,
                        leaderId, minions, isRemoveGroup);
                _mobGroupIndex.put(mobGroupId, mobGroup);
            }

            _log.info("载入MOB队伍资料数量: " + _mobGroupIndex.size() + "("
                    + timer.get() + "ms)");

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public L1MobGroup getTemplate(final int mobGroupId) {
        return _mobGroupIndex.get(mobGroupId);
    }

}
