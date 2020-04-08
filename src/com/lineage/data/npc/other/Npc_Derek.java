package com.lineage.data.npc.other;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ShopBuyList;
import com.lineage.server.serverpackets.S_ShopSellList;

/**
 * 戴芮克<BR>
 * 70026
 * 
 * @author dexc
 * 
 */
public class Npc_Derek extends NpcExecutor {

    /**
	 *
	 */
    private Npc_Derek() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Derek();
    }

    @Override
    public int type() {
        return 19;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        if (pc.getLawful() < 0) {// 邪恶
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "derek2"));

        } else {// 一般
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "derek1"));
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        if (cmd.equalsIgnoreCase("buy")) {// 买
            // 出售物品列表
            pc.sendPackets(new S_ShopSellList(npc.getId()));

        } else if (cmd.equalsIgnoreCase("sell")) {// 卖
            // PC的可卖出物件列表
            pc.sendPackets(new S_ShopBuyList(npc.getId(), pc));
        }
    }

    @Override
    public int workTime() {
        return 15;
    }

    private static boolean _spr = false;

    @Override
    public void work(final L1NpcInstance npc) {
        if (_spr) {
            _spr = false;
            npc.broadcastPacketX8(new S_DoActionGFX(npc.getId(), 7));

        } else {
            _spr = true;
            npc.broadcastPacketX8(new S_DoActionGFX(npc.getId(), 17));
        }
    }
}
