package com.lineage.data.item_etcitem.event;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_PacketBoxCooking;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 经验书
 */
public class ExpBook extends ItemExecutor {

    /**
	 *
	 */
    private ExpBook() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new ExpBook();
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
        pc.getInventory().removeItem(item, 1);
        // 判断经验加倍技能
        if (L1BuffUtil.cancelExpSkill(pc)) {
            final int time = 600;
            pc.setSkillEffect(L1SkillId.EXP13, time * 1000);
            pc.getInventory().removeItem(item, 1);
            pc.sendPackets(new S_ServerMessage("经验质提升1.3倍(600秒)"));
            // 狩猎的经验职将会增加
            pc.sendPackets(new S_PacketBoxCooking(pc, 32, time));
        }
    }
}
