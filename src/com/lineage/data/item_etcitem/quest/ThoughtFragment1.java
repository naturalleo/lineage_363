package com.lineage.data.item_etcitem.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.data.quest.IllusionistLv45_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/***
 * 第一次邪念碎片 49197
 */
public class ThoughtFragment1 extends ItemExecutor {

    /**
	 *
	 */
    private ThoughtFragment1() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new ThoughtFragment1();
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
        final int itemobj = data[0];
        final L1ItemInstance item1 = pc.getInventory().getItem(itemobj);
        if (item1 != null) {
            if (item1.getItemId() == 49198) {// 第二次邪念碎片 49198
                pc.getInventory().removeItem(item, 1);
                pc.getInventory().removeItem(item1, 1);
                // 任务已经开始
                if (pc.getQuest().isStart(IllusionistLv45_1.QUEST.get_id())) {
                    // 给予任务道具(未完成的时间水晶球 49200)
                    CreateNewItem.createNewItem(pc, 49200, 1);
                }

            } else {
                // 没有任何事情发生
                pc.sendPackets(new S_ServerMessage(79));
            }

        } else {
            // 没有任何事情发生
            pc.sendPackets(new S_ServerMessage(79));
        }
    }
}
