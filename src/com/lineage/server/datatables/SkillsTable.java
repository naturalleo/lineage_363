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
import com.lineage.server.templates.L1Skills;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 技能设置资料
 * 
 * @author dexc
 * 
 */
public class SkillsTable {

    private static final Log _log = LogFactory.getLog(SkillsTable.class);

    private static SkillsTable _instance;

    private static final Map<Integer, L1Skills> _skills = new HashMap<Integer, L1Skills>();

    public static SkillsTable get() {
        if (_instance == null) {
            _instance = new SkillsTable();
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
            pstm = con.prepareStatement("SELECT * FROM `skills`");
            rs = pstm.executeQuery();
            this.fillSkillsTable(rs);

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入技能设置资料数量: " + _skills.size() + "(" + timer.get() + "ms)");
    }

    private void fillSkillsTable(final ResultSet rs) throws SQLException {
        while (rs.next()) {
            final L1Skills l1skills = new L1Skills();
            final int skill_id = rs.getInt("skill_id");
            l1skills.setSkillId(skill_id);
            l1skills.setName(rs.getString("name"));
            l1skills.setSkillLevel(rs.getInt("skill_level"));
            l1skills.setSkillNumber(rs.getInt("skill_number"));
            l1skills.setMpConsume(rs.getInt("mpConsume"));
            l1skills.setHpConsume(rs.getInt("hpConsume"));
            l1skills.setItemConsumeId(rs.getInt("itemConsumeId"));
            l1skills.setItemConsumeCount(rs.getInt("itemConsumeCount"));
            l1skills.setReuseDelay(rs.getInt("reuseDelay"));
            l1skills.setBuffDuration(rs.getInt("buffDuration"));
            l1skills.setTarget(rs.getString("target"));
            l1skills.setTargetTo(rs.getInt("target_to"));
            l1skills.setDamageValue(rs.getInt("damage_value"));
            l1skills.setDamageDice(rs.getInt("damage_dice"));
            l1skills.setDamageDiceCount(rs.getInt("damage_dice_count"));
            l1skills.setProbabilityValue(rs.getInt("probability_value"));
            l1skills.setProbabilityDice(rs.getInt("probability_dice"));
            l1skills.setAttr(rs.getInt("attr"));
            l1skills.setType(rs.getInt("type"));
            l1skills.setLawful(rs.getInt("lawful"));
            l1skills.setRanged(rs.getInt("ranged"));
            l1skills.setArea(rs.getInt("area"));
            l1skills.setThrough(rs.getBoolean("through"));
            l1skills.setId(rs.getInt("id"));
            l1skills.setNameId(rs.getString("nameid"));
            l1skills.setActionId(rs.getInt("action_id"));
            l1skills.setCastGfx(rs.getInt("castgfx"));
            l1skills.setCastGfx2(rs.getInt("castgfx2"));
            l1skills.setSysmsgIdHappen(rs.getInt("sysmsgID_happen"));
            l1skills.setSysmsgIdStop(rs.getInt("sysmsgID_stop"));
            l1skills.setSysmsgIdFail(rs.getInt("sysmsgID_fail"));

            _skills.put(new Integer(skill_id), l1skills);
        }
    }

    public L1Skills getTemplate(final int i) {
        return _skills.get(new Integer(i));
    }

}
