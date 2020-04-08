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
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 指定地图经验倍率
 * 
 * @author dexc
 * 
 */
public class MapExpTable {

    private static final Log _log = LogFactory.getLog(MapExpTable.class);

    private static MapExpTable _instance;

    private static final Map<Integer, Double> _exp = new HashMap<Integer, Double>();

    private static final Map<Integer, int[]> _level = new HashMap<Integer, int[]>();

    public static MapExpTable get() {
        if (_instance == null) {
            _instance = new MapExpTable();
        }
        return _instance;
    }

    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `mapids_exp`");
            rs = ps.executeQuery();

            while (rs.next()) {
                final int mapid = rs.getInt("mapid");
                final double exp = rs.getInt("exp");
                _exp.put(new Integer(mapid), new Double(exp));

                int[] level = new int[2];
                final int min = rs.getInt("min");
                final int max = rs.getInt("max");

                level[0] = min;
                level[1] = max;
                _level.put(new Integer(mapid), level);
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
        _log.info("载入地图经验值倍率资料数量: " + _exp.size() + "(" + timer.get() + "ms)");
    }

    /**
     * 等级吻合要求
     * 
     * @param mapid
     * @param level
     * @return true:吻合 false:不吻合
     */
    public boolean get_level(final int mapid, final int level) {
        // 非经验加倍地图
        if (_exp.get(new Integer(mapid)) == null) {
            // System.out.println("非经验加倍地图");
            return false;
        }

        final int[] levelX = _level.get(new Integer(mapid));
        if (level >= levelX[0] && level <= levelX[1]) {
            // System.out.println("经验加倍地图");
            return true;
        }
        // System.out.println("经验加倍地图  等级不吻合");
        return false;
    }

    /**
     * 经验倍率
     * 
     * @param mapid
     * @return 经验倍率
     */
    public Double get_exp(final int mapid) {
        return _exp.get(new Integer(mapid));
    }
}
