package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.storage.CharOtherStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1PcOther;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 额外纪录资料
 * 
 * @author dexc
 * 
 */
public class CharOtherTable implements CharOtherStorage {

    private static final Log _log = LogFactory.getLog(CharOtherTable.class);

    private static final Map<Integer, L1PcOther> _otherMap = new HashMap<Integer, L1PcOther>();

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
            ps = cn.prepareStatement("SELECT * FROM `character_other`");
            rs = ps.executeQuery();
            while (rs.next()) {
                final int char_obj_id = rs.getInt("char_obj_id");
                // 检查该资料所属是否遗失
                if (CharObjidTable.get().isChar(char_obj_id) != null) {
                    final int hpup = rs.getInt("hpup");// HP人参，玩家已增加的HP值
                    final int mpup = rs.getInt("mpup");// MP人参，玩家已增加的MP值
                    final int score = rs.getInt("score");// 设置积分
                    // final int title = rs.getInt("title");// 头衔
                    final int color = rs.getInt("color");// 名称色彩
                    final int usemap = rs.getInt("usemap");// 计时地图编号
                    final int usemaptime = rs.getInt("usemaptime");// 计时地图可用时间
                    final int clanskill = rs.getInt("clanskill");// 设置血盟技能
                    final int killCount = rs.getInt("killCount");// 设置杀人次数
                    final int deathCount = rs.getInt("deathCount");// 设置被杀次数

                    final L1PcOther other = new L1PcOther();
                    other.set_objid(char_obj_id);
                    other.set_addhp(hpup);// HP人参，玩家已增加的HP值
                    other.set_addmp(mpup);// MP人参，玩家已增加的MP值
                    other.set_score(score);// 设置积分
                    // other.set_title(title);// 头衔
                    other.set_color(color);// 名称色彩

                    if (usemaptime <= 0) {
                        other.set_usemap(-1);// 计时地图编号
                        other.set_usemapTime(0);// 计时地图可用时间

                    } else {
                        other.set_usemap(usemap);// 计时地图编号
                        other.set_usemapTime(usemaptime);// 计时地图可用时间
                    }

                    other.set_clanskill(clanskill);// 设置血盟技能
                    other.set_killCount(killCount);// 设置杀人次数
                    other.set_deathCount(deathCount);// 设置被杀次数

                    addMap(char_obj_id, other);

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
        _log.info("载入额外纪录资料数量: " + _otherMap.size() + "(" + timer.get() + "ms)");
    }

    /**
     * 删除遗失额外纪录资料
     * 
     * @param objid
     */
    private static void delete(final int objid) {
        // 清空资料库纪录
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `character_other` WHERE `char_obj_id`=?");
            ps.setInt(1, objid);
            ps.execute();

            _otherMap.remove(objid);

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    /**
     * 加入额外纪录清单
     * 
     * @param objId
     * @param buffTmp
     */
    private static void addMap(final int objId, final L1PcOther other) {
        final L1PcOther otherTmp = _otherMap.get(objId);
        if (otherTmp == null) {
            _otherMap.put(objId, other);
        }
    }

    /**
     * 取回保留额外纪录
     * 
     * @param pc
     */
    @Override
    public L1PcOther getOther(final L1PcInstance pc) {
        final L1PcOther otherTmp = _otherMap.get(pc.getId());
        return otherTmp;
    }

    /**
     * 增加/更新 保留额外纪录
     * 
     * @param objId
     * @param other
     */
    @Override
    public void storeOther(final int objId, final L1PcOther other) {
        final L1PcOther otherTmp = _otherMap.get(objId);
        // System.out.println(objId + " / " + other);
        if (otherTmp == null) {
            addMap(objId, other);
            addNewOther(other);

        } else {
            updateOther(other);
        }
    }

    /**
     * 更新纪录
     * 
     * @param other
     */
    private void updateOther(final L1PcOther other) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            final int hpup = other.get_addhp();
            final int mpup = other.get_addmp();
            final int score = other.get_score();
            // final int title = other.get_title();
            final int color = other.get_color();
            final int usemap = other.get_usemap();
            final int usemapTime = other.get_usemapTime();
            final int clanskill = other.get_clanskill();
            final int killCount = other.get_killCount();
            final int deathCount = other.get_deathCount();

            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("UPDATE `character_other` SET "
                    + "`hpup`=?,`mpup`=?,`"
                    + "score`=?,`color`=?,`usemap`=?,`usemaptime`=?,"
                    + "`clanskill`=?,`killCount`=?,`deathCount`=?"
                    + " WHERE `char_obj_id`=?");

            int i = 0;
            ps.setInt(++i, hpup);
            ps.setInt(++i, mpup);
            ps.setInt(++i, score);
            ps.setInt(++i, color);
            ps.setInt(++i, usemap);
            ps.setInt(++i, usemapTime);
            ps.setInt(++i, clanskill);
            ps.setInt(++i, killCount);
            ps.setInt(++i, deathCount);

            ps.setInt(++i, other.get_objid());

            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    /**
     * 增加纪录
     * 
     * @param other
     */
    private void addNewOther(final L1PcOther other) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            final int oid = other.get_objid();
            final int hpup = other.get_addhp();
            final int mpup = other.get_addmp();
            final int score = other.get_score();
            final int color = other.get_color();
            final int usemap = other.get_usemap();
            final int usemapTime = other.get_usemapTime();
            final int clanskill = other.get_clanskill();
            final int killCount = other.get_killCount();
            final int deathCount = other.get_deathCount();

            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `character_other` SET `char_obj_id`=?,`hpup`=?,"
                    + "`mpup`=?,`score`=?,`color`=?,`usemap`=?,`usemaptime`=?,"
                    + "`clanskill`=?,`killCount`=?,`deathCount`=?");

            int i = 0;
            ps.setInt(++i, oid);
            ps.setInt(++i, hpup);
            ps.setInt(++i, mpup);
            ps.setInt(++i, score);
            ps.setInt(++i, color);
            ps.setInt(++i, usemap);
            ps.setInt(++i, usemapTime);
            ps.setInt(++i, clanskill);
            ps.setInt(++i, killCount);
            ps.setInt(++i, deathCount);

            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    /**
     * 归0杀人次数(全部玩家)
     */
    @Override
    public void tam() {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            for (L1PcOther other : _otherMap.values()) {
                other.set_killCount(0);
                other.set_deathCount(0);
            }
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("UPDATE `character_other` SET `killCount`='0' AND `deathCount`='0'");
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }
}
