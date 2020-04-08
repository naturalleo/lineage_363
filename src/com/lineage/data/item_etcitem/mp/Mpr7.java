package com.lineage.data.item_etcitem.mp;

import static com.lineage.server.model.skill.L1SkillId.STATUS_BLUE_POTION;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 加速魔力回复药水40015<br>
 * 加速魔力回复药水140015<br>
 * 智慧货币40736<br>
 */
public class Mpr7 extends ItemExecutor {

    /**
	 *
	 */
    private Mpr7() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Mpr7();
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
            final int itemId = item.getItemId();
            this.useBluePotion(pc, itemId);
            pc.getInventory().removeItem(item, 1);
        }
    }

    private void useBluePotion(final L1PcInstance pc, final int item_id) {
        // 解除魔法技能绝对屏障
        L1BuffUtil.cancelAbsoluteBarrier(pc);

        int time = 0;
        if ((item_id == 40015) || (item_id == 40736)) {
            time = 600;

        } else if (item_id == 140015) {
            time = 700;

        } else {
            return;
        }

        pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_BLUEPOTION, time));

        pc.sendPacketsX8(new S_SkillSound(pc.getId(), 190));

        pc.setSkillEffect(STATUS_BLUE_POTION, time * 1000);

        pc.sendPackets(new S_ServerMessage(1007)); // 你感觉到魔力恢复速度加快。。
    }
}
