package com.lineage.data.item_etcitem.quest;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 49216 普洛凯尔的护身符
 */
public class DK50_Teleport extends ItemExecutor {

    /**
	 *
	 */
    private DK50_Teleport() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new DK50_Teleport();
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

        pc.getInventory().removeItem(item, 1);// 移除道具
        // 传送任务执行者
        L1Teleport.teleport(pc, 33436, 32814, (short) 4, 5, true);
    }
}
