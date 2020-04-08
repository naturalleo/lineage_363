package com.lineage.data.item_etcitem.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.data.quest.IllusionistLv45_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 希莲恩之袋 49180
 */
public class I45_SilreinBox extends ItemExecutor {

    /**
	 *
	 */
    private I45_SilreinBox() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new I45_SilreinBox();
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
        if (pc.getQuest().isStart(IllusionistLv45_1.QUEST.get_id())) {
            // 给予任务道具(风木村庄瞬间移动卷轴 49184)
            CreateNewItem.createNewItem(pc, 49184, 1);

            // 给予任务道具(时空裂痕水晶(绿色)-3 49192)
            CreateNewItem.createNewItem(pc, 49192, 1);
            // 给予任务道具(时空裂痕水晶(绿色)-3 49192)
            CreateNewItem.createNewItem(pc, 49192, 1);
            // 给予任务道具(时空裂痕水晶(绿色)-3 49192)
            CreateNewItem.createNewItem(pc, 49192, 1);
        }
    }
}
