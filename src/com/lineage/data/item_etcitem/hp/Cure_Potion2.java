package com.lineage.data.item_etcitem.hp;

import static com.lineage.server.model.skill.L1SkillId.ADLV80_2_1;
import static com.lineage.server.model.skill.L1SkillId.ADLV80_2_2;
import static com.lineage.server.model.skill.L1SkillId.POLLUTE_WATER;

import java.util.Random;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_PacketBoxHpMsg;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 治愈药水140010
 */
public class Cure_Potion2 extends ItemExecutor {

    /**
	 *
	 */
    private Cure_Potion2() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Cure_Potion2();
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
        if (L1BuffUtil.stopPotion(pc)) {
            this.UseHeallingPotion(pc, 25, 189);
            pc.getInventory().removeItem(item, 1);
        }
    }

    private void UseHeallingPotion(final L1PcInstance pc, int healHp,
            final int gfxid) {
        // 解除魔法技能绝对屏障
        L1BuffUtil.cancelAbsoluteBarrier(pc);

        final Random random = new Random();

        pc.sendPacketsX8(new S_SkillSound(pc.getId(), gfxid));

        healHp *= (random.nextGaussian() / 5.0D) + 1.0D;
        if (pc.get_up_hp_potion() > 0) {
            healHp += (healHp * pc.get_up_hp_potion()) / 100;
        }
        if (pc.hasSkillEffect(POLLUTE_WATER)) {
            healHp = (healHp >> 1);
        }
        if (pc.hasSkillEffect(ADLV80_2_2)) {// 污浊的水流(水龙副本 回复量1/2倍)
            healHp = (healHp >> 1);
        }
        if (pc.hasSkillEffect(ADLV80_2_1)) {
            healHp *= -1;
        }
        if (healHp > 0) {
            // 你觉得舒服多了讯息
            pc.sendPackets(new S_PacketBoxHpMsg());
        }
        pc.setCurrentHp(pc.getCurrentHp() + healHp);
    }
}
