package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.CKEWLv50_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.world.WorldQuest;

/**
 * 被遗弃的肉身<BR>
 * 80137<BR>
 * 说明:不死魔族再生的秘密 (王族,骑士,妖精,法师50级以上官方任务-50级后半段)
 * 
 * @author dexc
 * 
 */
public class Npc_RottingCorpse extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_RottingCorpse.class);

    private Npc_RottingCorpse() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_RottingCorpse();
    }

    @Override
    public int type() {
        return 1;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (!pc.isInParty()) {// 未加入队伍
                return;
            }

            int i = 0;
            // 队伍成员
            for (final L1PcInstance otherPc : pc.getParty().partyUsers()
                    .values()) {
                if (otherPc.isCrown()) {// 王族
                    i += 1;
                } else if (otherPc.isKnight()) {// 骑士
                    i += 2;
                } else if (otherPc.isElf()) {// 精灵
                    i += 4;
                } else if (otherPc.isWizard()) {// 法师
                    i += 8;
                }
            }
            if (i != CKEWLv50_1.USER) {// 人数异常
                final L1QuestUser quest = WorldQuest.get().get(pc.get_showId());
                quest.endQuest();
                return;
            }
            if (pc.isCrown()) {// 王族
                // 任务开始
                if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id())) {
                    // 取得任务道具
                    if (!pc.getInventory().checkItem(49239)) {
                        CreateNewItem.getQuestItem(pc, npc, 49239, 1);// 消灭之意志
                    }
                }

            } else if (pc.isKnight()) {// 骑士
                // 任务开始
                if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id())) {
                    // 取得任务道具
                    if (!pc.getInventory().checkItem(49239)) {
                        CreateNewItem.getQuestItem(pc, npc, 49239, 1);// 消灭之意志
                    }
                }

            } else if (pc.isElf()) {// 精灵
                // 任务开始
                if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id())) {
                    // 取得任务道具
                    if (!pc.getInventory().checkItem(49239)) {
                        CreateNewItem.getQuestItem(pc, npc, 49239, 1);// 消灭之意志
                    }
                }

            } else if (pc.isWizard()) {// 法师
                // 任务开始
                if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id())) {
                    // 取得任务道具
                    if (!pc.getInventory().checkItem(49239)) {
                        CreateNewItem.getQuestItem(pc, npc, 49239, 1);// 消灭之意志
                    }
                }

            } else if (pc.isDarkelf()) {// 黑暗精灵

            } else if (pc.isDragonKnight()) {// 龙骑士

            } else if (pc.isIllusionist()) {// 幻术师

            } else {
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
