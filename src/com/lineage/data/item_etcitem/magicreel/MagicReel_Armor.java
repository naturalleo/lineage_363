package com.lineage.data.item_etcitem.magicreel;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 魔法卷轴 (铠甲护持)
 * 
 * @author dexc
 * 
 */
public class MagicReel_Armor extends ItemExecutor {

    /**
	 *
	 */
    private MagicReel_Armor() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new MagicReel_Armor();
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
        if (pc == null) {
            return;
        }
        if (item == null) {
            return;
        }
        final int targetID = data[0];// 对象OBJID

        final L1ItemInstance tgItem = pc.getInventory().getItem(targetID);

        if (tgItem == null) {
            return;
        }

        if (tgItem.getItem().getType2() != 2) {
            pc.sendPackets(new S_ServerMessage(281)); // \f1施咒取消。
            return;
        }

        if (tgItem.getItem().getType() != 2) {
            pc.sendPackets(new S_ServerMessage(281)); // \f1施咒取消。
            return;
        }

        final int useCount = 1;
        if (pc.getInventory().removeItem(item, useCount) >= useCount) {
            L1BuffUtil.cancelAbsoluteBarrier(pc);

            final int itemId = item.getItemId();
            final int skillid = itemId - 40858;

            final L1SkillUse l1skilluse = new L1SkillUse();
            l1skilluse.handleCommands(pc, skillid, targetID, 0, 0, 0,
                    L1SkillUse.TYPE_SPELLSC);
        }
    }
}
