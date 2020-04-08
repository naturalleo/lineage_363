package com.lineage.data.npc.quest;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.timecontroller.event.ranking.RankingHeroTimer;

/**
 * 英雄风云榜 80029 UPDATE `npc` SET `nameid`='英雄风云榜' WHERE `npcid`='80029';#
 * 
 * @author dexc
 * 
 */
public class Npc_HeroRanking extends NpcExecutor {

    /**
     * 英雄风云榜
     */
    private Npc_HeroRanking() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_HeroRanking();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_h_1"));
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        final String[] userName = new String[11];
        if (cmd.equals("c")) {// 王族风云榜
            String[] names = RankingHeroTimer.userNameC();
            for (int i = 0; i < names.length; i++) {
                userName[i] = names[i];
            }
            userName[10] = "王族";

        } else if (cmd.equals("k")) {// 骑士风云榜
            final String[] names = RankingHeroTimer.userNameK();
            for (int i = 0; i < names.length; i++) {
                userName[i] = names[i];
            }
            userName[10] = "骑士";

        } else if (cmd.equals("e")) {// 精灵风云榜
            final String[] names = RankingHeroTimer.userNameE();
            for (int i = 0; i < names.length; i++) {
                userName[i] = names[i];
            }
            userName[10] = "精灵";

        } else if (cmd.equals("w")) {// 法师风云榜
            final String[] names = RankingHeroTimer.userNameW();
            for (int i = 0; i < names.length; i++) {
                userName[i] = names[i];
            }
            userName[10] = "法师";

        } else if (cmd.equals("d")) {// 黑妖风云榜
            final String[] names = RankingHeroTimer.userNameD();
            for (int i = 0; i < names.length; i++) {
                userName[i] = names[i];
            }
            userName[10] = "黑暗精灵";

        } else if (cmd.equals("g")) {// 龙骑风云榜
            final String[] names = RankingHeroTimer.userNameG();
            for (int i = 0; i < names.length; i++) {
                userName[i] = names[i];
            }
            userName[10] = "龙骑士";

        } else if (cmd.equals("i")) {// 幻术风云榜
            final String[] names = RankingHeroTimer.userNameI();
            for (int i = 0; i < names.length; i++) {
                userName[i] = names[i];
            }
            userName[10] = "幻术师";

        } else if (cmd.equals("a")) {// 全职业风云榜
            final String[] names = RankingHeroTimer.userNameAll();
            for (int i = 0; i < names.length; i++) {
                userName[i] = names[i];
            }
            userName[10] = "全职业";
        }

        if (userName != null) {
            final String htmlid = "y_h_2";
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), htmlid, userName));
        }
    }
}
