package com.lineage.server.command.executor;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Item;

/**
 * 创造物品(参数:物品编号 - 数量 - 追加质)
 * 
 * @author dexc
 * 
 */
public class L1CreateItem implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1CreateItem.class);

    private L1CreateItem() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1CreateItem();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            final StringTokenizer st = new StringTokenizer(arg);
            final String nameid = st.nextToken();
            // 数量
            long count = 1;
            if (st.hasMoreTokens()) {
                count = Long.parseLong(st.nextToken());
            }

            // 强化质
            int enchant = 0;
            if (st.hasMoreTokens()) {
                enchant = Integer.parseInt(st.nextToken());
            }

            // 物品编号
            int itemid = 0;
            try {
                itemid = Integer.parseInt(nameid);

            } catch (final NumberFormatException e) {
                itemid = ItemTable.get().findItemIdByNameWithoutSpace(nameid);
                if (itemid == 0) {
                    pc.sendPackets(new S_SystemMessage("没有找到条件吻合的物品。"));
                    return;
                }
            }

            // 物品资料
            final L1Item temp = ItemTable.get().getTemplate(itemid);
            if (temp != null) {
                if (temp.isStackable()) {
                    // 可以堆叠的物品
                    final L1ItemInstance item = ItemTable.get().createItem(
                            itemid);
                    item.setEnchantLevel(0);
                    item.setCount(count);
                    item.setIdentified(true);
                    if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
                        pc.getInventory().storeItem(item);
                        // 403:获得0%。
                        pc.sendPackets(new S_ServerMessage(403, item
                                .getLogName() + "(ID:" + itemid + ")"));
                    }
                } else {
                    // 不可以堆叠的物品
                    if (count > 10) {
                        pc.sendPackets(new S_SystemMessage(
                                "不可以堆叠的物品一次创造数量禁止超过10"));
                        return;
                    }

                    L1ItemInstance item = null;
                    int createCount;
                    for (createCount = 0; createCount < count; createCount++) {
                        item = ItemTable.get().createItem(itemid);
                        item.setEnchantLevel(enchant);
                        item.setIdentified(true);

                        if (pc.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
                            pc.getInventory().storeItem(item);
                        } else {
                            break;
                        }
                    }
                    if (createCount > 0) {
                        // 403:获得0%。
                        pc.sendPackets(new S_ServerMessage(403, item
                                .getLogName() + "(ID:" + itemid + ")"));
                    }
                }
            } else {
                pc.sendPackets(new S_SystemMessage("指定ID不存在"));
            }
        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
