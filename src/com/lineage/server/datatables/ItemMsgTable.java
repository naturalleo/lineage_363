package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 要公告的打宝物品记录
 * 
 * @author loli
 * 
 */
public class ItemMsgTable {

    private static final Log _log = LogFactory.getLog(ItemMsgTable.class);

    private static final ArrayList<Integer> _idList = new ArrayList<Integer>();

    private static ItemMsgTable _instance;

    public static ItemMsgTable get() {
        if (_instance == null) {
            _instance = new ItemMsgTable();
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
            pstm = con.prepareStatement("SELECT * FROM `server_msg_item_id`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                final int item_id = rs.getInt("itemid");
                if (!_idList.contains(item_id)) {
                    _idList.add(item_id);
                }
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入打宝公告物编号数量: " + _idList.size() + "(" + timer.get() + "ms)");
    }

    public boolean contains(final int item_id) {
        return _idList.contains(item_id);
    }
}
