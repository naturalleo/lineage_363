package com.lineage.data.npc.other;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 钓鱼小童 80083
 * 
 * @author dexc
 * 
 */
public class Npc_Fishing_2 extends NpcExecutor {

    /**
	 *
	 */
    private Npc_Fishing_2() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Fishing_2();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "fk_out_1"));
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        // 离开
        if (cmd.equals("teleportURL")) {
            L1Teleport.teleport(pc, 32613, 32781, (short) 4, 4, true);
        }
    }
}
