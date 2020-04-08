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
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 人物变身资料
 * 
 * @author dexc
 * 
 */
public class PolyTable {

    private static final Log _log = LogFactory.getLog(PolyTable.class);

    private static PolyTable _instance;

    private static final Map<String, L1PolyMorph> _polymorphs = new HashMap<String, L1PolyMorph>();

    private static final Map<Integer, L1PolyMorph> _polyIdIndex = new HashMap<Integer, L1PolyMorph>();

    public static PolyTable get() {
        if (_instance == null) {
            _instance = new PolyTable();
        }
        return _instance;
    }

    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM polymorphs");
            rs = ps.executeQuery();
            this.fillPolyTable(rs);

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("载入人物变身资料数量: " + _polymorphs.size() + "(" + timer.get()
                + "ms)");
    }

    private void fillPolyTable(final ResultSet rs) throws SQLException {
        while (rs.next()) {
            final int id = rs.getInt("id");
            final String name = rs.getString("name");
            final int polyId = rs.getInt("polyid");
            final int minLevel = rs.getInt("minlevel");
            final int weaponEquipFlg = rs.getInt("weaponequip");
            final int armorEquipFlg = rs.getInt("armorequip");
            final boolean canUseSkill = rs.getBoolean("isSkillUse");
            final int causeFlg = rs.getInt("cause");

            final L1PolyMorph poly = new L1PolyMorph(id, name, polyId,
                    minLevel, weaponEquipFlg, armorEquipFlg, canUseSkill,
                    causeFlg);

            _polymorphs.put(name, poly);
            _polyIdIndex.put(polyId, poly);
        }
    }

    public L1PolyMorph getTemplate(final String name) {
        return _polymorphs.get(name);
    }

    public L1PolyMorph getTemplate(final int polyId) {
        return _polyIdIndex.get(polyId);
    }

}
