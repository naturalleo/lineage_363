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
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 指定地图可待等级
 * 
 * @author dexc
 * 
 */
public class MapLevelTable {

    private static final Log _log = LogFactory.getLog(MapLevelTable.class);

    private static MapLevelTable _instance;

    private static final Map<Integer, int[]> _level = new HashMap<Integer, int[]>();

    public static MapLevelTable get() {
        if (_instance == null) {
            _instance = new MapLevelTable();
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
            ps = co.prepareStatement("SELECT * FROM `mapids_level`");
            rs = ps.executeQuery();

            while (rs.next()) {
                final int mapid = rs.getInt("mapid");

                int[] level = new int[5];
                final int min = rs.getInt("min");
                final int max = rs.getInt("max");
                final int locx = rs.getInt("locx");
                final int locy = rs.getInt("locy");
                final int tomapid = rs.getInt("tomapid");

                level[0] = min;
                level[1] = max;
                level[2] = locx;
                level[3] = locy;
                level[4] = tomapid;
                _level.put(new Integer(mapid), level);
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
        _log.info("载入地图等极限制资料数量: " + _level.size() + "(" + timer.get() + "ms)");
    }

    /**
     * 等级吻合要求
     * 
     * @param mapid
     * @param pc
     */
    public void get_level(final int mapid, final L1PcInstance pc) {
        final int[] levelX = _level.get(new Integer(mapid));
        if (levelX == null) {
            return;
        }
        if (pc.getLevel() >= levelX[0] && pc.getLevel() < levelX[1]) {
            return;
        } else {
            if (!pc.isGm()) {
                L1Teleport.teleport(pc, levelX[2], levelX[3],
                        (short) levelX[4], 5, true);
            }
        }
    }
}
