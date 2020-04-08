package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 涂着胶水的航海日志第9页41056 涂着胶水的航海日志第10页41057
 */
public class Log_Book1 extends ItemExecutor {

    /**
	 *
	 */
    private Log_Book1() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Log_Book1();
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

        // 涂着胶水的航海日志9~10页
        final int logbookId = tgItem.getItem().getItemId();
        if (logbookId == (itemId + 8034)) {
            CreateNewItem.createNewItem(pc, 41058, 1);
            pc.getInventory().removeItem(tgItem, 1);
            pc.getInventory().removeItem(item, 1);
        } else {
            pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
        }
    }
}
