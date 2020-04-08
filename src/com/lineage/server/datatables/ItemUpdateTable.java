package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1ItemUpdate;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 物品升级资料
 * 
 * @author loli
 * 
 */
public class ItemUpdateTable {

    private static final Log _log = LogFactory.getLog(ItemUpdateTable.class);

    private static Map<Integer, ArrayList<L1ItemUpdate>> _updateMap = new HashMap<Integer, ArrayList<L1ItemUpdate>>();    

    private static ItemUpdateTable _instance;

    public static ItemUpdateTable get() {
        if (_instance == null) {
            _instance = new ItemUpdateTable();
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
            pstm = con
                    .prepareStatement("SELECT * FROM `server_item_update` ORDER BY `id`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                final int id = rs.getInt("id");// 流水号

                final int item_id = rs.getInt("itemid");
                if (ItemTable.get().getTemplate(item_id) == null) {
                    _log.error("物品升级资料错误: 没有这个编号的道具:" + item_id);
                    delete(id);
                    continue;
                }

                final String needids_c = rs.getString("needids").replaceAll(
                        " ", "");// 取代空白
                final String[] needids_tmp = needids_c.split(",");

                final String needcounts_c = rs.getString("needcounts")
                        .replaceAll(" ", "");// 取代空白
                final String[] needcounts_tmp = needcounts_c.split(",");

                if (needids_tmp.length != needcounts_tmp.length) {
                    _log.error("物品升级资料错误: 交换物品需要道具数量不吻合" + item_id);
                    continue;
                }

                final int toid = rs.getInt("toid");
                if (ItemTable.get().getTemplate(toid) == null) {
                    _log.error("物品升级资料错误: 没有这个编号的对象道具:" + toid);
                    delete(id);
                    continue;
                }

                final int[] needids = new int[needids_tmp.length];
                for (int i = 0; i < needids_tmp.length; i++) {
                    needids[i] = Integer.parseInt(needids_tmp[i]);
                }

                final int[] needcounts = new int[needcounts_tmp.length];
                for (int i = 0; i < needcounts_tmp.length; i++) {
                    needcounts[i] = Integer.parseInt(needcounts_tmp[i]);
                }
                
                final int probability = rs.getInt("probability");

                final L1ItemUpdate tmp = new L1ItemUpdate();
                tmp.set_item_id(item_id);
                tmp.set_toid(toid);
                tmp.set_needids(needids);
                tmp.set_needcounts(needcounts);
                tmp.set_probability(probability);

                ArrayList<L1ItemUpdate> value = _updateMap.get(item_id);
                if (value == null) {
                    value = new ArrayList<L1ItemUpdate>();
                    value.add(tmp);

                } else {
                    value.add(tmp);
                }
                _updateMap.put(item_id, value);
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入物品升级资料数量: " + _updateMap.size() + "(" + timer.get()
                + "ms)");
    }

    /**
     * 删除错误资料
     * 
     * @param id
     */
    public static void delete(final int id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `server_item_update` WHERE `id`=?");
            ps.setInt(1, id);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    /**
     * 资讯
     * 
     * @param key
     * @return
     */
    public ArrayList<L1ItemUpdate> get(final int key) {
        return _updateMap.get(key);
    }

    /**
     * 资讯
     * 
     * @param key
     * @return
     */
    public Map<Integer, ArrayList<L1ItemUpdate>> map() {
        return _updateMap;
    }
}
