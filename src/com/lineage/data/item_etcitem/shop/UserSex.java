package com.lineage.data.item_etcitem.shop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChangeShape;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 变性果实 41217
 * 
 * @author dexc
 * 
 */
public class UserSex extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(UserSex.class);

    /**
	 *
	 */
    private UserSex() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new UserSex();
    }

    /**
     * 道具物件执行
     * 
     * @param data
     *            参数
     * @param pc
     *            执行者
     * @param item
     *            物件
     */
    @Override
    public void execute(final int[] data, final L1PcInstance pc,
            final L1ItemInstance item) {
        // 例外状况:物件为空
        if (item == null) {
            return;
        }

        // 例外状况:人物为空
        if (pc == null) {
            return;
        }

        // 性别
        int sex = pc.get_sex();

        // 删除药水
        pc.getInventory().removeItem(item, 1);

        int newSex = -1;
        int newType = -1;

        // 更换性别
        if (sex == 0) {
            newSex = 1;
            if (pc.isCrown()) {
                newType = L1PcInstance.CLASSID_PRINCESS;

            } else if (pc.isKnight()) {
                newType = L1PcInstance.CLASSID_KNIGHT_FEMALE;

            } else if (pc.isElf()) {
                newType = L1PcInstance.CLASSID_ELF_FEMALE;

            } else if (pc.isWizard()) {
                newType = L1PcInstance.CLASSID_WIZARD_FEMALE;

            } else if (pc.isDarkelf()) {
                newType = L1PcInstance.CLASSID_DARK_ELF_FEMALE;

            } else if (pc.isDragonKnight()) {
                newType = L1PcInstance.CLASSID_DRAGON_KNIGHT_FEMALE;

            } else if (pc.isIllusionist()) {
                newType = L1PcInstance.CLASSID_ILLUSIONIST_FEMALE;
            }

        } else {
            newSex = 0;
            if (pc.isCrown()) {
                newType = L1PcInstance.CLASSID_PRINCE;

            } else if (pc.isKnight()) {
                newType = L1PcInstance.CLASSID_KNIGHT_MALE;

            } else if (pc.isElf()) {
                newType = L1PcInstance.CLASSID_ELF_MALE;

            } else if (pc.isWizard()) {
                newType = L1PcInstance.CLASSID_WIZARD_MALE;

            } else if (pc.isDarkelf()) {
                newType = L1PcInstance.CLASSID_DARK_ELF_MALE;

            } else if (pc.isDragonKnight()) {
                newType = L1PcInstance.CLASSID_DRAGON_KNIGHT_MALE;

            } else if (pc.isIllusionist()) {
                newType = L1PcInstance.CLASSID_ILLUSIONIST_MALE;
            }
        }

        try {
            pc.sendPacketsX8(new S_SkillSound(pc.getId(), 196));
            Thread.sleep(50);
            pc.sendPacketsX8(new S_SkillSound(pc.getId(), 197));
            Thread.sleep(50);
            pc.sendPacketsX8(new S_SkillSound(pc.getId(), 198));

            pc.sendPacketsAll(new S_ChangeShape(pc, newType));
            final L1ItemInstance weapon = pc.getWeapon();
            if (weapon != null) {
                pc.sendPacketsAll(new S_CharVisualUpdate(pc));
            }

            // 设置新代号
            pc.set_sex(newSex);
            pc.setClassId(newType);

            pc.save();

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
