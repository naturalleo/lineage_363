package com.lineage.data.npc.other;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv50_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 奇马的刺客<BR>
 * 70907<BR>
 * 
 * @author dexc
 * 
 */
public class Npc_Kimadum extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Kimadum.class);

    /**
	 *
	 */
    private Npc_Kimadum() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Kimadum();
    }

    @Override
    public int type() {
        return 1;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.getQuest().isStart(DarkElfLv50_1.QUEST.get_id())) {
                // 最近觉得奇马和伦得之间怪怪的。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kimadums"));

            } else {
                // 我只将性命奉献给奇马
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kimadum"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
