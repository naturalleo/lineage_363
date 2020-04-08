package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * NPC积分设置资料
 * 
 * @author dexc
 * 
 */
public class NpcScoreTable {

    private static final Log _log = LogFactory.getLog(NpcScoreTable.class);

    private static NpcScoreTable _instance;

    private static final Map<Integer, Integer> _scoreList = new TreeMap<Integer, Integer>();

    public static NpcScoreTable get() {
        if (_instance == null) {
            _instance = new NpcScoreTable();
        }
        return _instance;
    }

    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `npcscore`");
            rs = ps.executeQuery();
            while (rs.next()) {
                final int npcId = rs.getInt("npcid");
                final int score = rs.getInt("score");

                _scoreList.put(new Integer(npcId), new Integer(score));
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("载入NPC积分设置资料数量: " + _scoreList.size() + "(" + timer.get()
                + "ms)");
    }

    public Map<Integer, Integer> get_scoreList() {
        return _scoreList;
    }

    public int get_score(final int npcid) {
        if (_scoreList.get(npcid) != null) {
            return _scoreList.get(npcid);
        }
        return 0;
    }

}
