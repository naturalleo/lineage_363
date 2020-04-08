package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>占星术师的瓮</font><BR>
 * 
 * @author dexc
 * 
 */
public class Box_AstrologyTeacher extends ItemExecutor {

    /**
	 *
	 */
    private Box_AstrologyTeacher() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Box_AstrologyTeacher();
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
        // 删除道具
//        pc.getInventory().removeItem(item, 1); //不需要删除道具 hjx1000

        // 取得 占星术师的灵魂球
        CreateNewItem.createNewItem(pc, 41313, 1);
    }
}
