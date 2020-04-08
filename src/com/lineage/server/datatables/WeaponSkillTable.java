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
import com.lineage.server.model.L1WeaponSkill;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 技能武器资料
 * 
 * @author dexc
 * 
 */
public class WeaponSkillTable {

    private static final Log _log = LogFactory.getLog(WeaponSkillTable.class);

    private static WeaponSkillTable _instance;

    private static final Map<Integer, L1WeaponSkill> _weaponIdIndex = new HashMap<Integer, L1WeaponSkill>();

    public static WeaponSkillTable get() {
        if (_instance == null) {
            _instance = new WeaponSkillTable();
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
            pstm = con.prepareStatement("SELECT * FROM `weapon_skill`");
            rs = pstm.executeQuery();
            this.fillWeaponSkillTable(rs);
        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入技能武器资料数量: " + _weaponIdIndex.size() + "(" + timer.get()
                + "ms)");
    }

    private void fillWeaponSkillTable(final ResultSet rs) throws SQLException {
        while (rs.next()) {
            final int weaponId = rs.getInt("weapon_id");
            final int probability = rs.getInt("probability");
            final int fixDamage = rs.getInt("fix_damage");
            final int randomDamage = rs.getInt("random_damage");
            final int area = rs.getInt("area");
            final int skillId = rs.getInt("skill_id");
            final int skillTime = rs.getInt("skill_time");
            final int effectId = rs.getInt("effect_id");
            final int effectTarget = rs.getInt("effect_target");
            final boolean isArrowType = rs.getBoolean("arrow_type");
            final int attr = rs.getInt("attr");
            final L1WeaponSkill weaponSkill = new L1WeaponSkill(weaponId,
                    probability, fixDamage, randomDamage, area, skillId,
                    skillTime, effectId, effectTarget, isArrowType, attr);
            _weaponIdIndex.put(weaponId, weaponSkill);
        }
    }

    public L1WeaponSkill getTemplate(final int weaponId) {
        return _weaponIdIndex.get(weaponId);
    }

}
