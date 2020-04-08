package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.CharShiftingStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.SQLUtil;

/**
 * 装备移转纪录资料
 * 
 * @author dexc
 * 
 */
public class CharShiftingTable implements CharShiftingStorage {

    private static final Log _log = LogFactory.getLog(CharShiftingTable.class);

    /**
     * 增加装备移转纪录
     * 
     * @param pc
     *            执行人物
     * @param tgId
     *            目标objid
     * @param tgName
     *            目标名称
     * @param srcObjid
     *            原始objid
     * @param srcItem
     *            原始物件
     * @param newItem
     *            新物件
     * @param mode
     *            模式<BR>
     *            0: 交换装备<BR>
     *            1: 装备升级<BR>
     *            2: 转移装备<BR>
     */
    @Override
    public void newShifting(final L1PcInstance pc, int tgId,
            final String tgName, final int srcObjid, final L1Item srcItem,
            final L1ItemInstance newItem, final int mode) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `other_shifting` SET "
                    + "`srcObjid`=?,`srcItemid`=?,`srcName`=?,"
                    + "`newObjid`=?,`newItemid`=?,`newName`=?,"
                    + "`enchantLevel`=?,`attrEnchant`=?,`weaponSkill`=?,"
                    + "`pcObjid`=?,`pcName`=?," + "`tgPcObjid`=?,`tgPcName`=?,"
                    + "`time`=?,`note`=?");

            int i = 0;
            if (srcItem != null) {
                ps.setInt(++i, srcObjid);
                ps.setInt(++i, srcItem.getItemId());
                ps.setString(++i, srcItem.getName());

            } else {
                ps.setInt(++i, 0);
                ps.setInt(++i, 0);
                ps.setString(++i, "");

            }

            ps.setInt(++i, newItem.getId());
            ps.setInt(++i, newItem.getItemId());

            ps.setString(++i, newItem.getItem().getName());
            // 强化值
            ps.setInt(++i, newItem.getEnchantLevel());
            // 物件地水火风 属性/强化值
            ps.setString(
                    ++i,
                    newItem.getAttrEnchantKind() + "/"
                            + newItem.getAttrEnchantLevel());

            /*
             * if (newItem.isCnSkill()) { L1WeaponCnSkill cnSkill =
             * newItem.get_CnSkill(); ps.setString(++i, cnSkill.get_none() + "/"
             * + cnSkill.get_mode() + "/" + cnSkill.get_level());
             * 
             * } else { ps.setString(++i, ""); }
             */
            final StringBuilder cnSkillInfo = new StringBuilder();

            if (cnSkillInfo.length() > 0) {
                ps.setString(++i, cnSkillInfo.toString());

            } else {
                ps.setString(++i, "无效果");
            }

            ps.setInt(++i, pc.getId());
            ps.setString(++i, pc.getName());

            // 取回时间
            final Timestamp lastactive = new Timestamp(
                    System.currentTimeMillis());

            // 取得模式
            switch (mode) {
                case 0:// 交换装备
                    ps.setInt(++i, 0);
                    ps.setString(++i, "");
                    ps.setTimestamp(++i, lastactive);
                    ps.setString(++i, "交换装备");
                    break;

                case 1:// 装备升级
                    ps.setInt(++i, 0);
                    ps.setString(++i, "");
                    ps.setTimestamp(++i, lastactive);
                    ps.setString(++i, "装备升级");
                    break;

                case 2:// 转移装备
                    ps.setInt(++i, tgId);
                    ps.setString(++i, tgName);
                    ps.setTimestamp(++i, lastactive);
                    ps.setString(++i, "转移装备");
                    break;
            }

            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

}
