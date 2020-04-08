package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.CharItemsTimeStorage;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;

/**
 * 物品使用期限
 * 
 * @author dexc
 * 
 */
public class CharItemsTimeTable implements CharItemsTimeStorage {

    private static final Log _log = LogFactory.getLog(CharBookTable.class);

    // private static final Map<Integer, Timestamp> _timeMap = new
    // HashMap<Integer, Timestamp>();

    /**
     * 初始化载入
     */
    @Override
    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int size = 0;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `character_items_time`");
            rs = ps.executeQuery();
            while (rs.next()) {
                final int itemr_obj_id = rs.getInt("itemr_obj_id");
                final Timestamp usertime = rs.getTimestamp("usertime");

                addValue(itemr_obj_id, usertime);
                size++;
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("载入物品使用期限资料数量: " + size + "(" + timer.get() + "ms)");
    }

    /**
     * 初始化建立资料
     * 
     * @param itemr_obj_id
     * @param usertime
     */
    private static void addValue(final int itemr_obj_id,
            final Timestamp usertime) {
        L1Object obj = World.get().findObject(itemr_obj_id);
        boolean isError = true;
        if (obj != null) {
            if (obj instanceof L1ItemInstance) {
                L1ItemInstance item = (L1ItemInstance) obj;
                item.set_time(usertime);
                // _timeMap.put(itemr_obj_id, usertime);
                isError = false;
            }
        }

        if (isError) {
            delete(itemr_obj_id);
        }
    }

    /**
     * 删除遗失物品纪录资料
     * 
     * @param objid
     */
    private static void delete(final int objid) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `character_items_time` WHERE `itemr_obj_id`=?");
            ps.setInt(1, objid);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);

        }
    }

    /**
     * 增加物品使用期限记录
     * 
     * @param s
     */
    @Override
    public void addTime(final int itemr_obj_id, final Timestamp usertime) {
        // _timeMap.put(itemr_obj_id, usertime);

        Connection co = null;
        PreparedStatement ps = null;

        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("INSERT INTO `character_items_time` SET `itemr_obj_id`=?,`usertime`=?");

            int i = 0;
            ps.setInt(++i, itemr_obj_id);
            ps.setTimestamp(++i, usertime);

            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }
}
