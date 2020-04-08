package com.lineage.data.npc.other;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 钓鱼小童 80082
 * 
 * @author dexc
 * 
 */
public class Npc_Fishing_1 extends NpcExecutor {

    /**
	 *
	 */
    private Npc_Fishing_1() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Fishing_1();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "fk_in_1"));
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        if (cmd.equals("a")) {
            L1PolyMorph.undoPoly(pc);
            L1Teleport.teleport(pc, 32794, 32795, (short) 5300, 6, true);
        }
    }
}
