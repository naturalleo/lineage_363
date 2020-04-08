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
import com.lineage.server.model.drop.SetDrop;
import com.lineage.server.model.drop.SetDropExecutor;
import com.lineage.server.templates.L1DropMap;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 掉落物品资料(指定地图)
 * 
 * @author dexc
 * 
 */
public class DropMapTable {

    private static final Log _log = LogFactory.getLog(DropMapTable.class);

    private static DropMapTable _instance;

    public static DropMapTable get() {
        if (_instance == null) {
            _instance = new DropMapTable();
        }
        return _instance;
    }

    public void load() {
        final Map<Integer, HashMap<Integer, ArrayList<L1DropMap>>> droplists = this
                .allDropList();

        final SetDropExecutor setDropExecutor = new SetDrop();
        setDropExecutor.addDropMapX(droplists);
    }

    private Map<Integer, HashMap<Integer, ArrayList<L1DropMap>>> allDropList() {
        final PerformanceTimer timer = new PerformanceTimer();

        final Map<Integer, HashMap<Integer, ArrayList<L1DropMap>>> droplistMap = new HashMap<Integer, HashMap<Integer, ArrayList<L1DropMap>>>();

        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `droplist_map`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                final int mobId = rs.getInt("mobid");
                final int mapid = rs.getInt("mapid");

                HashMap<Integer, ArrayList<L1DropMap>> list = droplistMap
                        .get(mapid);
                if (list == null) {
                    list = new HashMap<Integer, ArrayList<L1DropMap>>();
                    droplistMap.put(mapid, list);
                }

                final int itemId = rs.getInt("itemid");
                final int min = rs.getInt("min");
                final int max = rs.getInt("max");
                final int chance = rs.getInt("chance");
                if (check_item(itemId)) {
                    final L1DropMap drop = new L1DropMap(mobId, mapid, itemId,
                            min, max, chance);

                    ArrayList<L1DropMap> dropList = list.get(drop.getMobid());
                    if (dropList == null) {
                        dropList = new ArrayList<L1DropMap>();
                        list.put(new Integer(drop.getMobid()), dropList);
                    }
                    dropList.add(drop);
                }
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入掉落物品资料数量(指定地图): " + droplistMap.size() + "(" + timer.get()
                + "ms)");
        return droplistMap;
    }

    private boolean check_item(int itemId) {
        final L1Item itemTemplate = ItemTable.get().getTemplate(itemId);
        if (itemTemplate == null) {
            // 无该物品资料 移除
            errorItem(itemId);
            return false;
        }
        return true;
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
                    .prepareStatement("DELETE FROM `droplist_map` WHERE `itemid`=?");
            pstm.setInt(1, itemid);
            pstm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
