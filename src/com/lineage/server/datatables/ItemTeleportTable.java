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
 * 传送卷轴传送点定义
 * 
 * @author dexc
 * 
 */
public class ItemTeleportTable {

    private static final Log _log = LogFactory.getLog(ItemTeleportTable.class);

    private static final Map<Integer, int[]> _teleportList = new HashMap<Integer, int[]>();

    private static ItemTeleportTable _instance;

    public static ItemTeleportTable get() {
        if (_instance == null) {
            _instance = new ItemTeleportTable();
        }
        return _instance;
    }

    /**
     * 初始化载入
     */
    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `etcitem_teleport`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                final int item_id = rs.getInt("item_id");
                final int locx = rs.getInt("locx");
                final int locy = rs.getInt("locy");
                final int mapid = rs.getInt("mapid");
                final int time = rs.getInt("time");

                _teleportList.put(new Integer(item_id), new int[] { locx, locy,
                        mapid, time });
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入传送卷轴传送点定义数量: " + _teleportList.size() + "(" + timer.get()
                + "ms)");
    }

    /**
     * 传回指定传送卷轴传送点
     */
    public int[] getLoc(final int item_id) {
        return _teleportList.get(item_id);
    }

}
