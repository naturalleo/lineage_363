package com.lineage.data.item_etcitem.brave;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 活化药水 - 46509<br>
 */
public class LifeT extends ItemExecutor {

    /**
	 *
	 */
    private LifeT() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new LifeT();
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

        // 血之渴望
        if (pc.hasSkillEffect(L1SkillId.BLOODLUST)) {
            // 1,413：目前情况是无法使用。
            pc.sendPackets(new S_ServerMessage(1413));
            return;
        }

        // 具有生命之树果实效果
        if (pc.hasSkillEffect(L1SkillId.STATUS_RIBRAVE)) {
            // 1,413：目前情况是无法使用。
            pc.sendPackets(new S_ServerMessage(1413));
            return;
        }

        if (L1BuffUtil.stopPotion(pc)) {
            this.useBravePotion(pc);
            pc.getInventory().removeItem(item, 1);
        }
    }

    private void useBravePotion(final L1PcInstance pc) {
        // // 解除魔法技能绝对屏障
        L1BuffUtil.cancelAbsoluteBarrier(pc);

        pc.sendPacketsX8(new S_SkillSound(pc.getId(), 191));
        pc.sendPacketsX8(new S_SkillSound(pc.getId(), 751));

        final int time = 600;

        // 勇敢效果 抵销对应技能
        L1BuffUtil.braveStart(pc);

        pc.sendPackets(new S_SkillBrave(pc.getId(), 1, time));
        pc.broadcastPacketAll(new S_SkillBrave(pc.getId(), 1, 0));

        pc.setSkillEffect(L1SkillId.STATUS_BRAVE, time);

        pc.setBraveSpeed(1);

        if (pc.getHasteItemEquipped() > 0) {
            return;
        }
        // 解除醉酒状态
        pc.setDrink(false);

        // 加速效果 抵销对应技能
        L1BuffUtil.hasteStart(pc);

        // 解除各种被施加的减速魔法技能
        if (pc.hasSkillEffect(L1SkillId.SLOW)) {
            pc.killSkillEffectTimer(L1SkillId.SLOW);
            pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));

        } else if (pc.hasSkillEffect(L1SkillId.MASS_SLOW)) {
            pc.killSkillEffectTimer(L1SkillId.MASS_SLOW);
            pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));

        } else if (pc.hasSkillEffect(L1SkillId.ENTANGLE)) {
            pc.killSkillEffectTimer(L1SkillId.ENTANGLE);
            pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));

        } else {
            pc.sendPackets(new S_SkillHaste(pc.getId(), 1, time));
            pc.broadcastPacketAll(new S_SkillHaste(pc.getId(), 1, 0));
            pc.setMoveSpeed(1);
            pc.setSkillEffect(L1SkillId.STATUS_HASTE, time * 1000);
        }
    }
}
