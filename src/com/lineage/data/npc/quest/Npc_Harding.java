package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 哈汀<BR>
 * 91330<BR>
 * 说明:魔法师．哈汀(故事)
 * 
 * @author dexc
 * 
 */
public class Npc_Harding extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Harding.class);

    private Npc_Harding() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Harding();
    }

    @Override
    public int type() {
        return 1;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.get_hardinR() != null) {
                if (pc.get_hardinR().get_time() > 0
                        && pc.get_hardinR().get_time() <= 180) {
                    // 欧林，时间不多了。 再给你10秒时间
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_ep002"));

                } else {
                    // 回来了？ 欧林，出发之前给你打招呼的时间。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_ep001"));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
