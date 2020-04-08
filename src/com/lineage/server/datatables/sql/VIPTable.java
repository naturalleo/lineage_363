package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.storage.VIPStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * VIP纪录资料
 * 
 * @author dexc
 * 
 */
public class VIPTable implements VIPStorage {

    private static final Log _log = LogFactory.getLog(VIPTable.class);

    private static final Map<Integer, Timestamp> _vipMap = new ConcurrentHashMap<Integer, Timestamp>();

    /**
     * 初始化载入
     */
    @Override
    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `character_vip`");
            rs = ps.executeQuery();
            while (rs.next()) {
                final int char_obj_id = rs.getInt("char_obj_id");

                // 检查该资料所属是否遗失
                if (CharObjidTable.get().isChar(char_obj_id) != null) {
                    final Timestamp overtime = rs.getTimestamp("overtime");

                    addMap(char_obj_id, overtime);

                } else {
                    delete(char_obj_id);
                }
            }
        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("载入VIP资料数量: " + _vipMap.size() + "(" + timer.get() + "ms)");
    }

    /**
     * 删除VIP系统纪录资料
     * 
     * @param objid
     */
    private static void delete(final int objid) {
        // 清空资料库纪录
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `character_vip` WHERE `char_obj_id`=?");
            ps.setInt(1, objid);
            ps.execute();

            _vipMap.remove(objid);

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);

        }
    }

    /**
     * 加入纪录清单
     * 
     * @param objId
     * @param buffTmp
     */
    private static void addMap(final int objId, final Timestamp overtime) {
        final Timestamp otherTmp = _vipMap.get(objId);
        if (otherTmp == null) {
            _vipMap.put(objId, overtime);
        }
    }

    /**
     * 全部VIP纪录
     * 
     * @return
     */
    @Override
    public Map<Integer, Timestamp> map() {
        return _vipMap;
    }

    /**
     * 取回VIP系统纪录
     * 
     * @param pc
     */
    @Override
    public Timestamp getOther(final L1PcInstance pc) {
        final Timestamp otherTmp = _vipMap.get(pc.getId());
        return otherTmp;
    }

    /**
     * 增加/更新 VIP系统纪录
     * 
     * @param key
     * @param value
     */
    @Override
    public void storeOther(final int key, final Timestamp value) {
        final Timestamp otherTmp = _vipMap.get(key);
        if (otherTmp == null) {
            addMap(key, value);
            addNewOther(key, value);

        } else {
            updateOther(key, value);
            _vipMap.put(key, value);
        }
    }

    /**
     * 删除VIP系统纪录
     * 
     * @param key
     *            PC OBJID
     */
    @Override
    public void delOther(final int key) {
        if (_vipMap.remove(key) != null) {
            delete(key);
        }
    }

    /**
     * 更新VIP系统纪录
     * 
     * @param objId
     * @param other
     */
    private static void updateOther(final int objId, final Timestamp other) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("UPDATE `character_vip` SET `overtime`=? WHERE `char_obj_id`=?");

            int i = 0;
            ps.setTimestamp(++i, other);

            ps.setInt(++i, objId);

            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    /**
     * 增加VIP系统纪录
     * 
     * @param objId
     * @param other
     */
    private static void addNewOther(final int objId, final Timestamp other) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `character_vip` SET `char_obj_id`=?,`overtime`=?");

            int i = 0;
            ps.setInt(++i, objId);
            ps.setTimestamp(++i, other);

            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }
}
