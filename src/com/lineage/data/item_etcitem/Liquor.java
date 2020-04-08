package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Liquor;

/**
 * 酒40858
 */
public class Liquor extends ItemExecutor {

    /**
	 *
	 */
    private Liquor() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Liquor();
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
        pc.setDrink(true);
        pc.sendPackets(new S_Liquor(pc.getId()));
        pc.getInventory().removeItem(item, 1);
    }

}
