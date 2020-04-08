package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ItemName;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 灯油40003
 */
public class Lamp_Oil extends ItemExecutor {

    /**
	 *
	 */
    private Lamp_Oil() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Lamp_Oil();
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
        for (final L1ItemInstance lightItem : pc.getInventory().getItems()) {
            if (lightItem.getItem().getItemId() == 40002) {
                lightItem.setRemainingTime(item.getItem().getLightFuel());
                pc.sendPackets(new S_ItemName(lightItem));
                // 你在灯笼里加满了新的灯油。
                pc.sendPackets(new S_ServerMessage(230));
                break;
            }
        }
        pc.getInventory().removeItem(item, 1);

    }
}
