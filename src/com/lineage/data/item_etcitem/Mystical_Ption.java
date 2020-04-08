package com.lineage.data.item_etcitem;

import java.util.Random;

import com.lineage.config.ConfigRate;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 神秘药水1阶40926<br>
 * 神秘药水2阶40927<br>
 * 神秘药水3阶40928<br>
 * 神秘药水4阶40929<br>
 */
public class Mystical_Ption extends ItemExecutor {

    /**
	 *
	 */
    private Mystical_Ption() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Mystical_Ption();
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

        final L1ItemInstance tgItem = pc.getInventory().getItem(itemobj);
        if (tgItem == null) {
            return;
        }

        final Random random = new Random();
        final int earing2Id = tgItem.getItem().getItemId();
        int potion1 = 0;
        int potion2 = 0;
        if ((earing2Id >= 41173) && (41184 >= earing2Id)) {
            if (itemId == 40926) {
                potion1 = 247;
                potion2 = 249;
            } else if (itemId == 40927) {
                potion1 = 249;
                potion2 = 251;
            } else if (itemId == 40928) {
                potion1 = 251;
                potion2 = 253;
            } else if (itemId == 40929) {
                potion1 = 253;
                potion2 = 255;
            }
            if ((earing2Id >= (itemId + potion1))
                    && ((itemId + potion2) >= earing2Id)) {
                if ((random.nextInt(99) + 1) < ConfigRate.CREATE_CHANCE_MYSTERIOUS) {
                    CreateNewItem.createNewItem(pc, (earing2Id - 12), 1);
                    pc.getInventory().removeItem(tgItem, 1);
                    pc.getInventory().removeItem(item, 1);
                } else {
                    pc.sendPackets(new S_ServerMessage(160, tgItem.getName()));
                    pc.getInventory().removeItem(item, 1);
                }
            } else {
                pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
            }
        } else {
            pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
        }
    }
}
