package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 纸人<BR>
 * 91331<BR>
 * 说明:穿越时空的探险(秘谭)
 * 
 * @author dexc
 * 
 */
public class Npc_PaperMan extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_PaperMan.class);

    private Npc_PaperMan() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_PaperMan();
    }

    @Override
    public int type() {
        return 1;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            // 和尤基好好谈过了吗？<
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_paper"));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
