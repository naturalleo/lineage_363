package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>强盗的袋子41074</font><BR>
 * Bandit Pouch
 * 
 * @author dexc
 * 
 */
public class BanditPouch extends ItemExecutor {

    /**
	 *
	 */
    private BanditPouch() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new BanditPouch();
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
        pc.getInventory().removeItem(item, 1);

        final int k = (int) (Math.random() * 300);// 随机数字范围0~299

        final int count = 300 + k;
        // 取得 金币

        CreateNewItem.createNewItem(pc, 40308, count);
    }
}
