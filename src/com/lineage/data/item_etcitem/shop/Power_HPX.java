package com.lineage.data.item_etcitem.shop;

import static com.lineage.server.model.skill.L1SkillId.ADLV80_2_1;
import static com.lineage.server.model.skill.L1SkillId.ADLV80_2_2;
import static com.lineage.server.model.skill.L1SkillId.POLLUTE_WATER;

import java.util.Random;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_PacketBoxHpMsg;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 魔幻体力徽章<BR>
 * 44170<BR>
 * 每次恢复的血量为50~100<BR>
 * 每次使用扣除人物身上3000金币,金币不足无法使用
 * 
 */
public class Power_HPX extends ItemExecutor {

    /**
	 *
	 */
    private Power_HPX() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Power_HPX();
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
        boolean isError = false;
        // 检查金币数量
        final L1ItemInstance adena = pc.getInventory().checkItemX(40308, 3000);
        // 金币不为空
        if (adena != null) {
            pc.getInventory().removeItem(adena, 3000);// 删除道具

        } else {
            isError = true;
        }

        // 异常发生
        if (isError) {
            // 189 \f1金币不足。
            pc.sendPackets(new S_ServerMessage(189));
            return;
        }

        if (L1BuffUtil.stopPotion(pc)) {
            // 解除魔法技能绝对屏障
            L1BuffUtil.cancelAbsoluteBarrier(pc);

            final Random random = new Random();

            pc.sendPacketsX8(new S_SkillSound(pc.getId(), 197));

            int healHp = random.nextInt(50) + 50;
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
}
