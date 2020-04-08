package com.lineage.data.item_etcitem;

import static com.lineage.server.model.skill.L1SkillId.CURSE_BLIND;
import static com.lineage.server.model.skill.L1SkillId.DARKNESS;
import static com.lineage.server.model.skill.L1SkillId.STATUS_FLOATING_EYE;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_CurseBlind;

/**
 * 失明药水（黑暗药水）40025
 */
public class Blindness_Potion extends ItemExecutor {

    /**
	 *
	 */
    private Blindness_Potion() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Blindness_Potion();
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
        this.useBlindPotion(pc);
        pc.getInventory().removeItem(item, 1);
    }

    private void useBlindPotion(final L1PcInstance pc) {
        // 解除魔法技能绝对屏障
        L1BuffUtil.cancelAbsoluteBarrier(pc);

        final int time = 16;
        if (pc.hasSkillEffect(CURSE_BLIND)) {
            pc.killSkillEffectTimer(CURSE_BLIND);
        } else if (pc.hasSkillEffect(DARKNESS)) {
            pc.killSkillEffectTimer(DARKNESS);
        }

        if (pc.hasSkillEffect(STATUS_FLOATING_EYE)) {
            pc.sendPackets(new S_CurseBlind(2));
        } else {
            pc.sendPackets(new S_CurseBlind(1));
        }

        pc.setSkillEffect(CURSE_BLIND, time * 1000);
    }
}
