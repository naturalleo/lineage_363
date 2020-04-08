package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 涂着胶水的航海日志第1页41048 涂着胶水的航海日志第2页41049 涂着胶水的航海日志第3页41050 涂着胶水的航海日志第4页41051
 * 涂着胶水的航海日志第5页41052 涂着胶水的航海日志第6页41053 涂着胶水的航海日志第7页41054 涂着胶水的航海日志第8页41055
 */
public class Log_Book extends ItemExecutor {

    /**
	 *
	 */
    private Log_Book() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Log_Book();
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
        final int itemId = item.getItemId();
        final L1ItemInstance tgItem = pc.getInventory().getItem(itemobj);

        if (tgItem == null) {
            return;
        }

        // 涂着胶水的航海日志1~8页
        final int logbookId = tgItem.getItem().getItemId();
        if (logbookId == (itemId + 8034)) {
            CreateNewItem.createNewItem(pc, logbookId + 2, 1);
            pc.getInventory().removeItem(tgItem, 1);
            pc.getInventory().removeItem(item, 1);

        } else {
            pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
        }
    }
}
