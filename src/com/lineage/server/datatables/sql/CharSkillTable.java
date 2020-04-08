package com.lineage.server.datatables.sql;

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
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.storage.CharSkillStorage;
import com.lineage.server.templates.L1UserSkillTmp;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 人物技能纪录
 * 
 * @author dexc
 * 
 */
public class CharSkillTable implements CharSkillStorage {

    private static final Log _log = LogFactory.getLog(CharSkillTable.class);

    private static final Map<Integer, ArrayList<L1UserSkillTmp>> _skillMap = new HashMap<Integer, ArrayList<L1UserSkillTmp>>();

    /**
     * 初始化载入
     */
    @Override
    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `character_skills`");
            rs = ps.executeQuery();
            while (rs.next()) {
                final int char_obj_id = rs.getInt("char_obj_id");

                // 检查该资料所属是否遗失
                if (CharObjidTable.get().isChar(char_obj_id) != null) {
                    final int skill_id = rs.getInt("skill_id");
                    final String skill_name = rs.getString("skill_name");
                    final int is_active = rs.getInt("is_active");
                    final int activetimeleft = rs.getInt("activetimeleft");

                    final L1UserSkillTmp userSkill = new L1UserSkillTmp();
                    userSkill.set_char_obj_id(char_obj_id);
                    userSkill.set_skill_id(skill_id);
                    userSkill.set_skill_name(skill_name);
                    userSkill.set_is_active(is_active);
                    userSkill.set_activetimeleft(activetimeleft);

                    addMap(char_obj_id, userSkill);

                } else {
                    deleteBuff(char_obj_id);
                }
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
        _log.info("载入人物技能纪录资料数量: " + _skillMap.size() + "(" + timer.get()
                + "ms)");
    }

    /**
     * 删除遗失资料
     * 
     * @param objid
     */
    private static void deleteBuff(final int objid) {
        // 清空资料库纪录
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `character_skills` WHERE `char_obj_id`=?");
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
     * 加入清单
     * 
     * @param objId
     * @param buffTmp
     */
    private static void addMap(final int objId, final L1UserSkillTmp skillTmp) {
        ArrayList<L1UserSkillTmp> list = _skillMap.get(objId);
        if (list == null) {
            list = new ArrayList<L1UserSkillTmp>();
            list.add(skillTmp);

        } else {
            list.add(skillTmp);
        }
        _skillMap.put(objId, list);
    }

    /**
     * 取回该人物技能列表
     * 
     * @param pc
     * @return
     */
    @Override
    public ArrayList<L1UserSkillTmp> skills(final int playerobjid) {
        return _skillMap.get(playerobjid);
    }

    /**
     * 增加技能
     */
    @Override
    public void spellMastery(final int playerobjid, final int skillid,
            final String skillname, final int active, final int time) {
        // 检查技能是否重复
        if (this.spellCheck(playerobjid, skillid)) {
            return;
        }

        final L1UserSkillTmp userSkill = new L1UserSkillTmp();
        userSkill.set_char_obj_id(playerobjid);
        userSkill.set_skill_id(skillid);
        userSkill.set_skill_name(skillname);
        userSkill.set_is_active(active);
        userSkill.set_activetimeleft(time);

        addMap(playerobjid, userSkill);

        Connection co = null;
        PreparedStatement ps = null;
        try {

            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("INSERT INTO `character_skills` SET "
                    + "`char_obj_id`=?,`skill_id`=?,`skill_name`=?,"
                    + "`is_active`=?,`activetimeleft`=?");
            ps.setInt(1, userSkill.get_char_obj_id());
            ps.setInt(2, userSkill.get_skill_id());
            ps.setString(3, userSkill.get_skill_name());
            ps.setInt(4, userSkill.get_is_active());
            ps.setInt(5, userSkill.get_activetimeleft());
            ps.execute();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }

    /**
     * 删除技能
     */
    @Override
    public void spellLost(final int playerobjid, final int skillid) {
        final ArrayList<L1UserSkillTmp> list = _skillMap.get(playerobjid);
        L1UserSkillTmp del = null;
        if (list != null) {
            for (final L1UserSkillTmp userSkillTmp : list) {
                if (userSkillTmp.get_skill_id() == skillid) {
                    del = userSkillTmp;
                }
            }
        }

        if (del == null) {
            return;
        }
        // 移出清单
        list.remove(del);

        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("DELETE FROM `character_skills` WHERE "
                    + "`char_obj_id`=? AND `skill_id`=?");
            ps.setInt(1, playerobjid);
            ps.setInt(2, skillid);
            ps.execute();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }

    /**
     * 检查技能是否重复
     */
    @Override
    public boolean spellCheck(final int playerobjid, final int skillid) {
        final ArrayList<L1UserSkillTmp> list = _skillMap.get(playerobjid);
        if (list != null) {
            for (final L1UserSkillTmp userSkillTmp : list) {
                if (userSkillTmp.get_skill_id() == skillid) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 设置自动技能状态
     */
    @Override
    public void setAuto(final int mode, final int objid, final int skillid) {
        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("UPDATE `character_skills` SET `is_active`=? WHERE `char_obj_id`=? AND `skill_id`=?");
            ps.setInt(1, mode);
            ps.setInt(2, objid);
            ps.setInt(3, skillid);
            ps.execute();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }
}
