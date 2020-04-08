package com.lineage.data.item_etcitem;

import static com.lineage.server.model.skill.L1SkillId.STATUS_FLOATING_EYE;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * <font color=#00800>食物1</font><BR>
 * 40056~40057 40059~40065 40069 40072~40073 41252 41263~41267 41274~41276
 * 41296~41297 49040~49047 140061~140062 140065 140069 140072
 */
public class Food1 extends ItemExecutor {

    /**
	 *
	 */
    private Food1() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Food1();
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
        pc.getInventory().removeItem(item, 1);
        short foodvolume1 = (short) (item.getItem().getFoodVolume() / 10);
        short foodvolume2 = 0;
        if (foodvolume1 <= 0) {
            foodvolume1 = 5;
        }

        if (pc.get_food() >= 225) {
            // pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, (short)
            // pc.get_food()));

        } else {
            foodvolume2 = (short) (pc.get_food() + foodvolume1);
            if (foodvolume2 > 255) {
                foodvolume2 = 255;
            }
            pc.set_food(foodvolume2);
            pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, (short) pc
                    .get_food()));
        }

        if (itemId == 40057) { // 漂浮之眼肉
            pc.setSkillEffect(STATUS_FLOATING_EYE, 0);
        }
        pc.sendPackets(new S_ServerMessage(76, item.getItem().getNameId()));
    }
}
