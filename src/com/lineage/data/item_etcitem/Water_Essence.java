package com.lineage.data.item_etcitem;

import static com.lineage.server.model.skill.L1SkillId.STATUS_UNDERWATER_BREATH;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillIconBlessOfEva;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 伊娃的祝福40032 人鱼之鳞40041 水中之水41344
 */
public class Water_Essence extends ItemExecutor {

    /**
	 *
	 */
    private Water_Essence() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Water_Essence();
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
        this.useBlessOfEva(pc, itemId);
        pc.getInventory().removeItem(item, 1);
    }

    private void useBlessOfEva(final L1PcInstance pc, final int item_id) {
        if (pc.hasSkillEffect(71) == true) { // 药水箱化术
            pc.sendPackets(new S_ServerMessage(698)); // 喉咙燥热，无法喝东西。
            return;
        }
        // 解除魔法技能绝对屏障
        L1BuffUtil.cancelAbsoluteBarrier(pc);

        int time = 0;
        if (item_id == 40032) { // 伊娃的祝福
            time = 1800;
        } else if (item_id == 40041) { // 人鱼之鳞
            time = 300;
        } else if (item_id == 41344) { // 水中之水
            time = 2100;
        } else {
            return;
        }

        if (pc.hasSkillEffect(STATUS_UNDERWATER_BREATH)) {
            final int timeSec = pc
                    .getSkillEffectTimeSec(STATUS_UNDERWATER_BREATH);
            time += timeSec;
            if (time > 3600) {
                time = 3600;
            }
        }
        pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), time));
        pc.sendPacketsX8(new S_SkillSound(pc.getId(), 190));
        pc.setSkillEffect(STATUS_UNDERWATER_BREATH, time * 1000);
    }
}
