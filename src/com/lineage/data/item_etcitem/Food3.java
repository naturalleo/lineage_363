package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1Cooking;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>食物3</font><BR>
 * 41277~41292 49049~49064 49244~49259
 */
public class Food3 extends ItemExecutor {

    /**
	 *
	 */
    private Food3() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Food3();
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
        L1Cooking.useCookingItem(pc, item);
    }
}
