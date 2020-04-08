package com.lineage.data.npc;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ShopBuyListAll;

/**
 * 亚丁商团<BR>
 * 99999
 * 
 * @author dexc
 * 
 */
public class Npc_Recycling extends NpcExecutor {

    /**
	 *
	 */
    private Npc_Recycling() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Recycling();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tzmerchant"));
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        if (cmd.equalsIgnoreCase("sell")) {
            pc.sendPackets(new S_ShopBuyListAll(pc, npc));
        }
    }
}
