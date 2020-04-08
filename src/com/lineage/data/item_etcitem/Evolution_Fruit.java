package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 进化果实40070
 */
public class Evolution_Fruit extends ItemExecutor {

    /**
	 *
	 */
    private Evolution_Fruit() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Evolution_Fruit();
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
        pc.sendPackets(new S_ServerMessage(76, item.getLogName()));
        pc.getInventory().removeItem(item, 1);
    }

}
