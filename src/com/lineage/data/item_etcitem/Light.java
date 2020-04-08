package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ItemName;

/**
 * 照明工具：<br>
 * 灯40001<br>
 * 灯笼40002<br>
 * 魔法灯笼40004<br>
 * 蜡烛40005<br>
 */
public class Light extends ItemExecutor {

    /**
	 *
	 */
    private Light() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Light();
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
        if (item.isNowLighting()) {
            item.setNowLighting(false);
            pc.turnOnOffLight();

        } else {
            item.setNowLighting(true);
            pc.turnOnOffLight();
        }
        pc.sendPackets(new S_ItemName(item));
    }
}
