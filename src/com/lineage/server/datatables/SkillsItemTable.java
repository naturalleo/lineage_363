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
import com.lineage.server.templates.L1SkillItem;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 购买技能 材料 设置资料
 * 
 * @author dexc
 * 
 */
public class SkillsItemTable {

    private static final Log _log = LogFactory.getLog(SkillsItemTable.class);

    private static SkillsItemTable _instance;

    private static final Map<Integer, L1SkillItem> _skills = new HashMap<Integer, L1SkillItem>();

    public static SkillsItemTable get() {
        if (_instance == null) {
            _instance = new SkillsItemTable();
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
            pstm = con.prepareStatement("SELECT * FROM `skills_item`");
            rs = pstm.executeQuery();
            this.itemTable(rs);

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入购买技能 材料 设置资料数量: " + _skills.size() + "(" + timer.get()
                + "ms)");
    }

    private void itemTable(final ResultSet rs) throws SQLException {
        while (rs.next()) {
            final L1SkillItem skillItem = new L1SkillItem();
            // 技能编号
            final int skill_id = rs.getInt("skill_id");
            skillItem.set_skill_id(skill_id);

            // 技能名称
            String name = rs.getString("name");
            skillItem.set_name(name);

            // 耗用物件组
            String items = rs.getString("itemids");
            if (items == null) {
                skillItem.set_items(null);

            } else {
                final String[] itemsX = items.split(",");
                int[] items_x = new int[itemsX.length];
                for (int i = 0; i < itemsX.length; i++) {
                    items_x[i] = Integer.parseInt(itemsX[i]);
                }
                skillItem.set_items(items_x);
            }

            // 耗用数量组
            String counts = rs.getString("counts");
            if (counts == null) {
                skillItem.set_counts(null);

            } else {
                final String[] countsX = counts.split(",");
                int[] counts_x = new int[countsX.length];
                for (int i = 0; i < countsX.length; i++) {
                    counts_x[i] = Integer.parseInt(countsX[i]);
                }
                skillItem.set_counts(counts_x);
            }

            // int adena = rs.getInt("adena");
            // skillItem.set_adena(adena);
            _skills.put(new Integer(skill_id), skillItem);

            if (skillItem.get_items() != null) {
                if (skillItem.get_items().length != skillItem.get_counts().length) {
                    _log.error("购买技能 材料 设置资料异常 技能编号: " + skill_id);
                    _skills.remove(new Integer(skill_id));
                }

            } else {
                _skills.remove(new Integer(skill_id));
            }
        }
    }

    /**
     * 传回指定组
     * 
     * @param i
     * @return
     */
    public L1SkillItem getTemplate(final int i) {
        return _skills.get(new Integer(i));
    }
}
