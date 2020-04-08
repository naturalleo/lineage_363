package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1PcQuest;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 火焰之影的契约书41121 炎魔的契约书41130
 */
public class Contract_Book extends ItemExecutor {

    /**
	 *
	 */
    private Contract_Book() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Contract_Book();
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
        if (itemId == 41130) { // 炎魔的契约书
            if ((pc.getQuest().get_step(L1PcQuest.QUEST_DESIRE) == L1PcQuest.QUEST_END)
                    || pc.getInventory().checkItem(41131, 1)) {
                pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生
            } else {
                CreateNewItem.createNewItem(pc, 41131, 1);
            }

        } else { // 火焰之影的契约书
            if ((pc.getQuest().get_step(L1PcQuest.QUEST_SHADOWS) == L1PcQuest.QUEST_END)
                    || pc.getInventory().checkItem(41122, 1)) {
                pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生
            } else {
                CreateNewItem.createNewItem(pc, 41122, 1);
            }
        }

    }
}
