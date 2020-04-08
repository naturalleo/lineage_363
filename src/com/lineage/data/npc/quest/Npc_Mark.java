package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.KnightLv15_1;
import com.lineage.data.quest.KnightLv30_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 马克<BR>
 * 70775<BR>
 * 说明:拯救被幽禁的吉姆 (骑士30级以上官方任务)<BR>
 * 
 * @author dexc
 * 
 */
public class Npc_Mark extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Mark.class);

    private Npc_Mark() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Mark();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 你现在有没有能够真心为对方付出的朋友呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mark3"));

            } else if (pc.isKnight()) {// 骑士
                // LV15任务未完成
                if (!pc.getQuest().isEnd(KnightLv15_1.QUEST.get_id())) {
                    // 你现在有没有能够真心为对方付出的朋友呢？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mark3"));
                    return;
                }
                // LV30任务已经完成
                if (pc.getQuest().isEnd(KnightLv30_1.QUEST.get_id())) {
                    // 终于做到了。 真是恭喜你，愿殷海萨祝福你...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "markcg"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= KnightLv30_1.QUEST.get_questlevel()) {
                    // 任务尚未开始
                    if (!pc.getQuest().isStart(KnightLv30_1.QUEST.get_id())) {
                        // 关于吉姆
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mark1"));

                    } else {// 任务已经开始
                        // 但请帮助我的好朋友吉姆，减轻他的痛苦
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mark2"));
                    }

                } else {
                    // 你现在有没有能够真心为对方付出的朋友呢？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mark3"));
                }

            } else if (pc.isElf()) {// 精灵
                // 你现在有没有能够真心为对方付出的朋友呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mark3"));

            } else if (pc.isWizard()) {// 法师
                // 你现在有没有能够真心为对方付出的朋友呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mark3"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 你现在有没有能够真心为对方付出的朋友呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mark3"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 你现在有没有能够真心为对方付出的朋友呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mark3"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 你现在有没有能够真心为对方付出的朋友呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mark3"));

            } else {
                // 你现在有没有能够真心为对方付出的朋友呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mark3"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        if (pc.isKnight()) {// 骑士
            // LV30任务已经完成
            if (pc.getQuest().isEnd(KnightLv30_1.QUEST.get_id())) {
                return;
            }

            // 任务尚未开始
            if (!pc.getQuest().isStart(KnightLv30_1.QUEST.get_id())) {
                if (cmd.equalsIgnoreCase("quest 13 mark2")) {// 关于吉姆
                    // 将任务设置为执行中
                    QuestClass.get()
                            .startQuest(pc, KnightLv30_1.QUEST.get_id());
                    // 但请帮助我的好朋友吉姆，减轻他的痛苦
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mark2"));
                }

            } else {
                isCloseList = true;
            }
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
