package com.lineage.data.item_etcitem.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.data.quest.IllusionistLv30_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 49179 希莲恩之袋
 */
public class I30_SilreinBox extends ItemExecutor {

    /**
	 *
	 */
    private I30_SilreinBox() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new I30_SilreinBox();
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
        // 任务已经开始
        if (pc.getQuest().isStart(IllusionistLv30_1.QUEST.get_id())) {
            // 给予任务道具(欧瑞村庄瞬间移动卷轴)
            CreateNewItem.createNewItem(pc, 49183, 1);
            // 给予任务道具(生锈的笛子)
            CreateNewItem.createNewItem(pc, 49186, 1);
        }
    }
}
