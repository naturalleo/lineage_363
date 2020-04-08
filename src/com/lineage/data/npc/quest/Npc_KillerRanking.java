package com.lineage.data.npc.quest;

import com.lineage.config.ConfigKill;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.timecontroller.event.ranking.RankingKillTimer;

/**
 * 杀手风云榜 80026 UPDATE `npc` SET `nameid`='杀手风云榜' WHERE `npcid`='80026';#
 * 
 * @author dexc
 * 
 */
public class Npc_KillerRanking extends NpcExecutor {

    /**
     * 杀手风云榜
     */
    private Npc_KillerRanking() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_KillerRanking();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_k_1",
                new String[] { String.valueOf(ConfigKill.KILLLEVEL) }));
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        String[] userName = null;

        if (cmd.equalsIgnoreCase("1")) {// 杀手排行榜
            userName = RankingKillTimer.killName();
            if (userName != null) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_k_2",
                        userName));
            }

        } else if (cmd.equalsIgnoreCase("2")) {// 死者排行榜
            userName = RankingKillTimer.deathName();
            if (userName != null) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_k_3",
                        userName));
            }
        }
    }
}
