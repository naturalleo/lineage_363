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
 * 掉落物品机率资料
 * 
 * @author dexc
 * 
 */
public final class DropItemTable {

    private static final Log _log = LogFactory.getLog(DropItemTable.class);

    private class DropItemData {
        public double dropRate = 1;
        public double dropAmount = 1;
    }

    private static DropItemTable _instance;

    private static final Map<Integer, DropItemData> _dropItem = new HashMap<Integer, DropItemData>();

    public static DropItemTable get() {
        if (_instance == null) {
            _instance = new DropItemTable();
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
            pstm = con.prepareStatement("SELECT * FROM `drop_item`");

            for (rs = pstm.executeQuery(); rs.next();) {
                final int itemid = rs.getInt("item_id");
                if (ItemTable.get().getTemplate(itemid) == null) {
                    _log.error("掉落物品机率资料错误: 没有这个编号的道具:" + itemid);
                    errorItem(itemid);
                    continue;
                }
                final DropItemData data = new DropItemData();
                data.dropRate = rs.getDouble("drop_rate");
                data.dropAmount = rs.getDouble("drop_amount");

                _dropItem.put(new Integer(itemid), data);
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入掉落物品机率资料数量: " + _dropItem.size() + "(" + timer.get()
                + "ms)");
    }

    /**
     * 删除错误物品资料
     * 
     * @param objid
     */
    private static void errorItem(int itemid) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("DELETE FROM `drop_item` WHERE `item_id`=?");
            pstm.setInt(1, itemid);
            pstm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public double getDropRate(final int itemId) {
        final DropItemData data = _dropItem.get(itemId);
        if (data == null) {
            return 1;
        }
        return data.dropRate;
    }

    public double getDropAmount(final int itemId) {
        final DropItemData data = _dropItem.get(itemId);
        if (data == null) {
            return 1;
        }
        return data.dropAmount;
    }

}
