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
 * 禁止拍卖物品资料
 * 
 * @author dexc
 * 
 */
public class ShopXTable {

    private static final Log _log = LogFactory.getLog(ShopXTable.class);

    private static ShopXTable _instance;

    private static final Map<Integer, String> _notShopList = new HashMap<Integer, String>();

    public static ShopXTable get() {
        if (_instance == null) {
            _instance = new ShopXTable();
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
            pstm = con.prepareStatement("SELECT * FROM `server_shopx`");
            rs = pstm.executeQuery();

            while (rs.next()) {
                final int itemid = rs.getInt("itemid");
                if (ItemTable.get().getTemplate(itemid) == null) {
                    _log.error("禁止拍卖物品资料错误: 没有这个编号的道具:" + itemid);
                    delete(itemid);
                    continue;
                }
                final String note = rs.getString("note");
                _notShopList.put(new Integer(itemid), note);
            }

            _log.info("载入禁止拍卖物品资料数量: " + _notShopList.size() + "("
                    + timer.get() + "ms)");

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
    private static void delete(final int item_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `server_shopx` WHERE `itemid`=?");
            ps.setInt(1, item_id);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    public String getTemplate(final int itemid) {
        return _notShopList.get(new Integer(itemid));
    }

    public Map<Integer, String> getList() {
        return _notShopList;
    }

    public int size() {
        return _notShopList.size();
    }

}
