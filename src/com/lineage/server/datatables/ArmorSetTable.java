package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.data.item_armor.set.ArmorSet;
import com.lineage.server.templates.L1ArmorSets;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 套装设置
 * 
 * @author dexc
 * 
 */
public class ArmorSetTable {

    private static final Log _log = LogFactory.getLog(ArmorSetTable.class);

    private static ArmorSetTable _instance;

    private static final ArrayList<L1ArmorSets> _armorSetList = new ArrayList<L1ArmorSets>();

    public static ArmorSetTable get() {
        if (_instance == null) {
            _instance = new ArmorSetTable();
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
            pstm = con.prepareStatement("SELECT * FROM `armor_set`");
            rs = pstm.executeQuery();
            this.fillTable(rs);

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }

        _log.info("载入套装设置数量: " + _armorSetList.size() + "(" + timer.get()
                + "ms)");

        // 套装设置加载
        ArmorSet.load();
    }

    private void fillTable(final ResultSet rs) throws SQLException {
        while (rs.next()) {
            final L1ArmorSets as = new L1ArmorSets();
            as.setId(rs.getInt("id"));// 套装编号
            as.setSets(rs.getString("sets"));// 套装组件编号
            as.setPolyId(rs.getInt("polyid"));// 变身代号
            as.setAc(rs.getInt("ac"));// 防御力增加
            as.setHp(rs.getInt("hp"));// HP增加
            as.setMp(rs.getInt("mp"));// MP增加
            as.setHpr(rs.getInt("hpr"));// HP回复增加
            as.setMpr(rs.getInt("mpr"));// MP回复增加
            as.setMr(rs.getInt("mr"));// 抗魔增加

            as.setStr(rs.getInt("str"));// 力量增加
            as.setDex(rs.getInt("dex"));// 敏捷增加
            as.setCon(rs.getInt("con"));// 体质增加
            as.setWis(rs.getInt("wis"));// 精神增加
            as.setCha(rs.getInt("cha"));// 魅力增加
            as.setIntl(rs.getInt("intl"));// 智力增加

            as.setDefenseWater(rs.getInt("defense_water"));// 水属性增加
            as.setDefenseWind(rs.getInt("defense_wind"));// 风属性增加
            as.setDefenseFire(rs.getInt("defense_fire"));// 火属性增加
            as.setDefenseEarth(rs.getInt("defense_earth"));// 地属性增加

            as.set_regist_stun(rs.getInt("regist_stun"));// 晕眩耐性增加
            as.set_regist_stone(rs.getInt("regist_stone"));// 石化耐性增加
            as.set_regist_sleep(rs.getInt("regist_sleep"));// 睡眠耐性增加
            as.set_regist_freeze(rs.getInt("regist_freeze"));// 寒冰耐性增加
            as.set_regist_sustain(rs.getInt("regist_sustain"));// 支撑耐性增加
            as.set_regist_blind(rs.getInt("regist_blind"));// 暗闇耐性增加

            as.set_modifier_dmg(rs.getInt("modifier_dmg"));// 套装增加物理伤害
            as.set_reduction_dmg(rs.getInt("reduction_dmg"));// 套装减免物理伤害
            as.set_magic_modifier_dmg(rs.getInt("magic_modifier_dmg"));// 套装增加魔法伤害
            as.set_magic_reduction_dmg(rs.getInt("magic_reduction_dmg"));// 套装减免魔法伤害
            as.set_bow_modifier_dmg(rs.getInt("bow_modifier_dmg"));// 套装增加弓的物理伤害

            String gfx = rs.getString("gfx");
            if (gfx != null && !gfx.equals("")) {
                String[] gfxs = gfx.replaceAll(" ", "").split(",");
                int[] out = new int[gfxs.length];
                for (int i = 0; i < gfxs.length; i++) {
                    out[i] = Integer.parseInt(gfxs[i]);
                }

                as.set_gfxs(out);// 套装效果动画
            }

            _armorSetList.add(as);
        }
    }

    public L1ArmorSets[] getAllList() {
        return _armorSetList.toArray(new L1ArmorSets[_armorSetList.size()]);
    }

}
