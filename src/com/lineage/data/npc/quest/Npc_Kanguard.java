package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv15_2;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 康<BR>
 * 70885<BR>
 * 说明:妖魔的侵入 (黑暗妖精15级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Kanguard extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Kanguard.class);

    private Npc_Kanguard() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Kanguard();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 只有黑暗妖精才能住在这里！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kanguard4"));

            } else if (pc.isKnight()) {// 骑士
                // 只有黑暗妖精才能住在这里！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kanguard4"));

            } else if (pc.isElf()) {// 精灵
                // 只有黑暗妖精才能住在这里！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kanguard4"));

            } else if (pc.isWizard()) {// 法师
                // 只有黑暗妖精才能住在这里！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kanguard4"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 任务已经完成
                if (pc.getQuest().isEnd(DarkElfLv15_2.QUEST.get_id())) {
                    // 我现在觉得你有些事情处理得不错
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kanguard3"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= DarkElfLv15_2.QUEST.get_questlevel()) {
                    // 任务尚未开始
                    if (!pc.getQuest().isStart(DarkElfLv15_2.QUEST.get_id())) {
                        // 接受简单的任务
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "kanguard1"));

                    } else {
                        // 递给妖魔长老首级
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "kanguard2"));
                    }

                } else {
                    // 我现在非常地忙，所以我没空跟你闲扯蛋。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kanguard5"));
                }

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 只有黑暗妖精才能住在这里！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kanguard4"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 只有黑暗妖精才能住在这里！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kanguard4"));

            } else {
                // 只有黑暗妖精才能住在这里！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kanguard4"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;

        if (pc.isDarkelf()) {// 黑暗精灵
            // 等级达成要求
            if (pc.getLevel() >= DarkElfLv15_2.QUEST.get_questlevel()) {
                // 任务已经完成
                if (pc.getQuest().isEnd(DarkElfLv15_2.QUEST.get_id())) {
                    return;
                }
                if (cmd.equalsIgnoreCase("quest 11 kanguard2")) {// 接受简单的任务
                    // 将任务设置为执行中
                    QuestClass.get().startQuest(pc,
                            DarkElfLv15_2.QUEST.get_id());
                    // 递给妖魔长老首级
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kanguard2"));

                } else if (cmd.equalsIgnoreCase("request kanbag")) {// 递给妖魔长老首级
                    // 需要物件不足
                    if (CreateNewItem.checkNewItem(pc, 40585, // 任务完成需要物件(妖魔长老首级
                                                              // x 1)
                            1) < 1) {// 传回可交换道具数小于1(需要物件不足)
                        isCloseList = true;

                    } else {// 需要物件充足
                        // 收回任务需要物件 给予任务完成物件
                        CreateNewItem.createNewItem(pc, 40585, 1, // 需要妖魔长老首级 x
                                                                  // 1
                                40598, 1);// 给予康之袋 x 1
                        // 将任务设置为结束
                        QuestClass.get().endQuest(pc,
                                DarkElfLv15_2.QUEST.get_id());
                        // 我现在觉得你有些事情处理得不错
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "kanguard3"));
                    }
                }
            }

        } else {
            isCloseList = true;
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
