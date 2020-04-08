package com.lineage.data.npc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 瑞尔克<BR>
 * 85030<BR>
 * 
 * @author dexc
 * 
 */
public class Npc_Rark extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Rark.class);

    private Npc_Rark() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Rark();
    }

    @Override
    public int type() {
        return 1;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            // 我是负责护卫长老希莲恩的瑞尔克
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "rark1"));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
