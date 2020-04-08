package com.lineage.server.command.executor;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.list.OnlineUser;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.AccountReading;
import com.lineage.server.datatables.lock.DwarfReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Account;

/**
 * 指定仓库加入指定物品
 * 
 * @author dexc
 * 
 */
public class L1CN implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1CN.class);

    private L1CN() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1CN();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            if (pc == null) {
                _log.warn("这个命令只能在游戏中执行!");
            }
            final StringTokenizer st = new StringTokenizer(arg);
            final String accname = st.nextToken();

            L1Account acc = AccountReading.get().getAccount(
                    accname.toLowerCase());
            if (acc != null) {
                final int item_id = Integer.parseInt(st.nextToken());// ITEMID
                int count = 0;
                try {
                    count = Integer.parseInt(st.nextToken());// COUNT
                } catch (final Exception e) {
                    count = 1;
                }
                // 产生新物件
                final L1ItemInstance item = ItemTable.get().createItem(item_id);
                if (item != null) {
                    item.setCount(count);
                    pc.sendPackets(new S_ServerMessage(166, "指定帐号("
                            + acc.get_login() + ") 加入物品" + item_id + " " + item.getName() + " 数量"
                            + count));
                    DwarfReading.get().insertItem(acc.get_login(), item);

                    ClientExecutor cl = OnlineUser.get().get(acc.get_login());
                    if (cl != null) {
                        final L1PcInstance tgpc = cl.getActiveChar();
                        if (tgpc != null) {
                            tgpc.sendPackets(new S_ServerMessage(166,
                                    "GM在您的仓库加入物品" + item.getName() + " 数量"
                                            + count));
                            // 重整仓库数据
                            tgpc.getDwarfInventory().loadItems();
                        }
                    }
                }

            } else {
                pc.sendPackets(new S_ServerMessage(166, "指令异常: 没有该帐号("
                        + accname + ")!!"));
            }

        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
