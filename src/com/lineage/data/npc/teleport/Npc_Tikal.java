package com.lineage.data.npc.teleport;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;

/**
 * 提卡尔 库库尔坎祭坛守门人<BR>
 * 81241
 * 
 * @author dexc
 * 
 */
public class Npc_Tikal extends NpcExecutor {

    /**
	 *
	 */
    private Npc_Tikal() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Tikal();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tikalgate1"));
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        if (cmd.equals("e")) {// 移动到库库尔坎祭坛
            final int itemid = 49273;// 提卡尔库库尔坎祭坛钥匙
            final L1ItemInstance item = pc.getInventory().checkItemX(itemid, 1);
            if (item != null) {
                pc.getInventory().removeItem(item, 1);// 删除道具

                L1Teleport.teleport(pc, 32732, 32862, (short) 784, 5, true);

            } else {
                // 找回物品
                final L1Item itemtmp = ItemTable.get().getTemplate(itemid);
                // 337 \f1不足。
                pc.sendPackets(new S_ServerMessage(337, itemtmp.getNameId()));
            }
        }
    }
}
