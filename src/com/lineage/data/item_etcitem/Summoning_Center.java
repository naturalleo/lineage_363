package com.lineage.data.item_etcitem;

import java.util.Random;

import com.lineage.config.ConfigRate;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 召唤球之核41029
 */
public class Summoning_Center extends ItemExecutor {

    /**
	 *
	 */
    private Summoning_Center() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Summoning_Center();
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
        final Random _random = new Random();
        if (itemId == 41029) {
            final int dantesId = item1.getItem().getItemId();
            if ((dantesId >= 41030) && (41034 >= dantesId)) {
                if ((_random.nextInt(99) + 1) < ConfigRate.CREATE_CHANCE_DANTES) {
                    CreateNewItem.createNewItem(pc, dantesId + 1, 1);

                } else {
                    pc.sendPackets(new S_ServerMessage(158, item1.getName()));
                }
                pc.getInventory().removeItem(item1, 1);
                pc.getInventory().removeItem(item, 1);
            } else {
                pc.sendPackets(new S_ServerMessage(79));
            }
        }
    }
}
