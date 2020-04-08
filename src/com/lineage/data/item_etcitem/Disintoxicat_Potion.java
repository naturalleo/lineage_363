package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 翡翠药水(解毒药水)40017 安特之树枝40507
 */
public class Disintoxicat_Potion extends ItemExecutor {

    /**
	 *
	 */
    private Disintoxicat_Potion() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Disintoxicat_Potion();
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
        if (pc.hasSkillEffect(71) == true) { // 药水箱化术
            pc.sendPackets(new S_ServerMessage(698)); // 喉咙灼热，无法喝东西。
        } else {
            // 解除魔法技能绝对屏障
            L1BuffUtil.cancelAbsoluteBarrier(pc);

            pc.sendPacketsX8(new S_SkillSound(pc.getId(), 192));

            pc.getInventory().removeItem(item, 1);
            pc.curePoison();
        }

    }
}
