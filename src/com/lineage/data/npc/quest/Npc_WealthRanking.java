package com.lineage.data.npc.quest;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.timecontroller.event.ranking.RankingWealthTimer;

/**
 * 财富风云榜 80027 UPDATE `npc` SET `nameid`='财富风云榜' WHERE `npcid`='80027';#
 * 
 * @author dexc
 * 
 */
public class Npc_WealthRanking extends NpcExecutor {

    /**
     * 财富风云榜
     */
    private Npc_WealthRanking() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_WealthRanking();
    }

    @Override
    public int type() {
        return 1;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        final String[] userName = RankingWealthTimer.userName();
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_w_1", userName));
    }
}
