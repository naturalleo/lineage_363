package com.lineage.data.item_etcitem;

import static com.lineage.server.model.skill.L1SkillId.ENTANGLE;
import static com.lineage.server.model.skill.L1SkillId.GREATER_HASTE;
import static com.lineage.server.model.skill.L1SkillId.HASTE;
import static com.lineage.server.model.skill.L1SkillId.MASS_SLOW;
import static com.lineage.server.model.skill.L1SkillId.SLOW;
import static com.lineage.server.model.skill.L1SkillId.STATUS_HASTE;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * <font color=#00800>食物2</font><BR>
 * 41261~41262 41268~41273
 */
public class Food2 extends ItemExecutor {

    /**
	 *
	 */
    private Food2() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Food2();
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
        final int itemId = item.getItemId();
        this.useGreenPotion(pc, itemId);
        pc.getInventory().removeItem(item, 1);
    }

    private void useGreenPotion(final L1PcInstance pc, final int itemId) {
        if (pc.hasSkillEffect(71) == true) { // 药水霜化术
            pc.sendPackets(new S_ServerMessage(698)); // 喉咙灼热，无法喝东西。。
            return;
        }
        // 解除魔法技能绝对屏障
        L1BuffUtil.cancelAbsoluteBarrier(pc);
        final int time = 30;

        pc.sendPacketsX8(new S_SkillSound(pc.getId(), 191));

        if (pc.getHasteItemEquipped() > 0) {
        	//hjx1000 修改带有加速的装备时,使用绿水恢复到永久加速状态
            if (pc.getMoveSpeed() != 1) {
            	pc.setMoveSpeed(1);
            	pc.sendPackets(new S_SkillHaste(pc.getId(),
                        1, -1));
            	pc.broadcastPacketAll(new S_SkillHaste(pc
                        .getId(), 1, 0));
            }
            return;
        }
        // 解除醉酒状态
        pc.setDrink(false);

        // 技能与食物功能重复
        if (pc.hasSkillEffect(HASTE)) {
            pc.killSkillEffectTimer(HASTE);
            pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
            pc.setMoveSpeed(0);

        } else if (pc.hasSkillEffect(GREATER_HASTE)) {
            pc.killSkillEffectTimer(GREATER_HASTE);
            pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
            pc.setMoveSpeed(0);

        } else if (pc.hasSkillEffect(STATUS_HASTE)) {
            pc.killSkillEffectTimer(STATUS_HASTE);
            pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
            pc.setMoveSpeed(0);
        }

        // 解除所有被施加的减速魔法技能
        if (pc.hasSkillEffect(SLOW)) {
            pc.killSkillEffectTimer(SLOW);
            pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));

        } else if (pc.hasSkillEffect(MASS_SLOW)) {
            pc.killSkillEffectTimer(MASS_SLOW);
            pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));

        } else if (pc.hasSkillEffect(ENTANGLE)) {
            pc.killSkillEffectTimer(ENTANGLE);
            pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));

        } else {
            pc.sendPackets(new S_SkillHaste(pc.getId(), 1, time));
            pc.broadcastPacketAll(new S_SkillHaste(pc.getId(), 1, 0));
            pc.setMoveSpeed(1);
            pc.setSkillEffect(STATUS_HASTE, time * 1000);
        }
    }
}
