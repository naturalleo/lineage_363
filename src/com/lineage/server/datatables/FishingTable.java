package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1Fishing;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 渔获资料暂存
 * 
 * @author dexc
 * 
 */
public class FishingTable {

    public static final Log _log = LogFactory.getLog(FishingTable.class);

    private static final Map<Integer, L1Fishing> _fishingMap = new HashMap<Integer, L1Fishing>();

    private static Random _random = new Random();

    private static FishingTable _instance;

    public static FishingTable get() {
        if (_instance == null) {
            _instance = new FishingTable();
        }
        return _instance;
    }

    private FishingTable() {
    }

    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `server_fishing`");
            rs = ps.executeQuery();

            while (rs.next()) {
                final int key = rs.getInt("itemid");
                final int randomint = rs.getInt("randomint");
                final int random = rs.getInt("random");
                final int count = rs.getInt("count");
                final int bait = rs.getInt("bait");

                if (ItemTable.get().getTemplate(key) == null) {
                    _log.error("渔获资料错误: 没有这个编号的道具:" + key);
                    delete(key);
                    continue;
                }
                if (count > 0) {
                    L1Fishing value = new L1Fishing();
                    value.set_itemid(key);
                    value.set_randomint(randomint);
                    value.set_random(random);
                    value.set_count(count);
                    value.set_bait(bait);
                    _fishingMap.put(key, value);
                }
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("载入渔获资料数量: " + _fishingMap.size() + "(" + timer.get() + "ms)");
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
            ps = cn.prepareStatement("DELETE FROM `server_fishing` WHERE `itemid`=?");
            ps.setInt(1, item_id);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    public L1Fishing get_item() {
        try {
            final Object[] objs = _fishingMap.values().toArray();
            final Object obj = objs[_random.nextInt(objs.length)];
            final L1Fishing fishing = (L1Fishing) obj;
            return fishing;

        } catch (final Exception e) {
        }
        return null;
    }
}
