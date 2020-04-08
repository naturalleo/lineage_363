package com.lineage.data.item_etcitem;

import java.util.Random;

import com.lineage.config.ConfigRate;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 净化药水40925
 */
public class Purification_Potion extends ItemExecutor {

    /**
	 *
	 */
    private Purification_Potion() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Purification_Potion();
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
        final int itemobj = data[0];
        final L1ItemInstance item1 = pc.getInventory().getItem(itemobj);
        if (item1 == null) {
            return;
        }
        final Random random = new Random();
        final int earingId = item1.getItem().getItemId();
        if ((earingId >= 40987) && (40989 >= earingId)) {
            if (random.nextInt(100) < ConfigRate.CREATE_CHANCE_RECOLLECTION) {
                CreateNewItem.createNewItem(pc, (earingId + 186), 1);

            } else {
                pc.sendPackets(new S_ServerMessage(158, item1.getName()));
            }
            pc.getInventory().removeItem(item1, 1);
            pc.getInventory().removeItem(item, 1);

        } else {
            pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
        }

    }
}
