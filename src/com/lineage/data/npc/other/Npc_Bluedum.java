package com.lineage.data.npc.other;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 布鲁迪卡的刺客<BR>
 * 70896<BR>
 * 
 * @author dexc
 * 
 */
public class Npc_Bluedum extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Bluedum.class);

    /**
	 *
	 */
    private Npc_Bluedum() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Bluedum();
    }

    @Override
    public int type() {
        return 1;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            // 帮助布鲁迪卡是我的荣幸。
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedum"));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
