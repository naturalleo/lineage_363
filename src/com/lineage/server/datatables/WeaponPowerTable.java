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
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1Weapon;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 武器额外伤害资料
 * 
 * @author dexc
 * 
 */
public class WeaponPowerTable {

    private static final Log _log = LogFactory.getLog(WeaponPowerTable.class);

    private static WeaponPowerTable _instance;

    private static final Map<Integer, int[]> _weaponPower = new HashMap<Integer, int[]>();

    public static WeaponPowerTable get() {
        if (_instance == null) {
            _instance = new WeaponPowerTable();
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
            pstm = con.prepareStatement("SELECT * FROM `weapon_power`");
            rs = pstm.executeQuery();
            this.fillWeaponSkillTable(rs);

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        set_weapon_power();
        _log.info("载入武器额外伤害资料数量: " + _weaponPower.size() + "(" + timer.get()
                + "ms)");
    }

    private void set_weapon_power() {
        try {
            for (Integer key : _weaponPower.keySet()) {
                final L1Item item = ItemTable.get().getTemplate(key);
                if (item instanceof L1Weapon) {
                    int[] power = _weaponPower.get(key);
                    final L1Weapon weapon = (L1Weapon) item;
                    weapon.set_add_dmg(power[0], power[1]);

                } else {
                    _log.error("武器额外伤害资料错误: 这个编号不是武器:" + key);
                    delete(key);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void fillWeaponSkillTable(final ResultSet rs) throws SQLException {
        while (rs.next()) {
            final int weapon_id = rs.getInt("weapon_id");
            if (ItemTable.get().getTemplate(weapon_id) == null) {
                _log.error("武器额外伤害资料错误: 没有这个编号的道具:" + weapon_id);
                delete(weapon_id);
                continue;
            }
            final int add_dmg_min = rs.getInt("add_dmg_min");
            final int add_dmg_max = rs.getInt("add_dmg_max");

            if (add_dmg_min >= add_dmg_max) {
                _log.error("武器额外伤害资料错误: 伤害质设置异常:" + weapon_id);
                delete(weapon_id);
                continue;
            }

            _weaponPower.put(weapon_id, new int[] { add_dmg_min, add_dmg_max });
        }
    }

    /**
     * 删除错误资料
     * 
     * @param clan_id
     */
    private static void delete(final int weapon_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `weapon_power` WHERE `weapon_id`=?");
            ps.setInt(1, weapon_id);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    public int[] getTemplate(final int weaponId) {
        return _weaponPower.get(weaponId);
    }
}
