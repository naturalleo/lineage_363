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
 * 物品可用时间指定
 * 
 * @author dexc
 * 
 */
public class ItemTimeTable {

    private static final Log _log = LogFactory.getLog(ItemTimeTable.class);

    private static ItemTimeTable _instance;

    // 物品编号 / 可用时间(小时)
    public static final Map<Integer, Integer> TIME = new HashMap<Integer, Integer>();

    public static ItemTimeTable get() {
        if (_instance == null) {
            _instance = new ItemTimeTable();
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
            pstm = con.prepareStatement("SELECT * FROM `server_item_time`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                final int key = rs.getInt("itemid");
                final int value = rs.getInt("h");
                TIME.put(key, value);
            }
        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入掉落时间限制道具: " + TIME.size() + "(" + timer.get() + "ms)");
    }
}
