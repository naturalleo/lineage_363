package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Letter;

/**
 * 各种信纸(未开封的) 49016 49018 49020 49022 49024
 */
public class Letter_closed extends ItemExecutor {

    /**
	 *
	 */
    private Letter_closed() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Letter_closed();
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
        pc.sendPackets(new S_Letter(item));
        item.setItemId(itemId + 1);
        pc.getInventory().updateItem(item, L1PcInventory.COL_ITEMID);
        pc.getInventory().saveItem(item, L1PcInventory.COL_ITEMID);
    }
}
