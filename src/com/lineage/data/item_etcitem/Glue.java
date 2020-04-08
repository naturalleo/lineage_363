package com.lineage.data.item_etcitem;

import java.util.Random;

import com.lineage.config.ConfigRate;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 胶水41036
 */
public class Glue extends ItemExecutor {

    /**
	 *
	 */
    private Glue() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Glue();
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
        final int itemobj = data[0];

        final L1ItemInstance item1 = pc.getInventory().getItem(itemobj);
        if (item1 == null) {
            return;
        }
        final int diaryId = item1.getItem().getItemId();
        final Random random = new Random();
        if (itemId == 41036) { // 胶水
            if ((diaryId >= 41038) && (41047 >= diaryId)) {
                pc.getInventory().removeItem(item1, 1);
                pc.getInventory().removeItem(item, 1);

                if ((random.nextInt(99) + 1) <= ConfigRate.CREATE_CHANCE_DIARY) {
                    CreateNewItem.createNewItem(pc, diaryId + 10, 1);
                } else {
                    pc.sendPackets(new S_ServerMessage(158, item1.getName())); // \f1%0%s
                    // 消失。
                }

            } else {
                pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
            }
        }
    }
}
