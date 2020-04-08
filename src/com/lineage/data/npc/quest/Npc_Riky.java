package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.KnightLv15_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 瑞奇<BR>
 * 70798<BR>
 * 瑞奇的抵抗 (骑士15级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Riky extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Riky.class);

    private Npc_Riky() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Riky();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 我已经不属于反王的势力...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "riky2"));

            } else if (pc.isKnight()) {// 骑士
                // 任务已经完成
                if (pc.getQuest().isEnd(KnightLv15_1.QUEST.get_id())) {
                    // 喔，你终于做到了! 以后应该称你为红骑士了。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "rikycg"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= KnightLv15_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(KnightLv15_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                               // 关于黑骑士的誓约
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "riky1"));
                            // 将任务设置为执行中
                            QuestClass.get().startQuest(pc,
                                    KnightLv15_1.QUEST.get_id());
                            break;

                        case 1:// 达到1(任务开始)
                               // 交给黑骑士的誓约
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "riky3"));
                            break;

                        case 2:// 达到2
                               // 你现在看起来也有骑士的威严了。哈哈哈...
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "riky5"));
                            break;

                        default:// 其他
                            // 喔，你终于做到了! 以后应该称你为红骑士了。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "rikycg"));
                            break;
                    }
                } else {
                    // 你是...想当骑士的自愿者吗？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "riky6"));
                }

            } else if (pc.isElf()) {// 精灵
                // 我已经不属于反王的势力...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "riky2"));

            } else if (pc.isWizard()) {// 法师
                // 我已经不属于反王的势力...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "riky2"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 我已经不属于反王的势力...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "riky2"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 我已经不属于反王的势力...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "riky2"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 我已经不属于反王的势力...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "riky2"));

            } else {
                // 我已经不属于反王的势力...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "riky2"));
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
            // 任务已经完成
            if (pc.getQuest().isEnd(KnightLv15_1.QUEST.get_id())) {
                return;
            }
            if (cmd.equalsIgnoreCase("request hood of knight")) {// 交给黑骑士的誓约
                // 任务已经开始
                if (pc.getQuest().isStart(KnightLv15_1.QUEST.get_id())) {
                    // 需要物件不足
                    if (CreateNewItem.checkNewItem(pc, 40608, // 任务完成需要物件(黑骑士的誓约
                                                              // x 1)
                            1) < 1) {// 传回可交换道具数小于1(需要物件不足)
                        isCloseList = true;

                    } else {// 需要物件充足
                        // 收回任务需要物件 给予任务完成物件
                        CreateNewItem.createNewItem(pc, 40608, 1, // 需要黑骑士的誓约 x
                                                                  // 1
                                20005, 1);// 给予骑士头巾 x 1
                        // 嗯...我记得我之前对我朋友做了一些不好的事情...
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "riky4"));
                        // 提升任务进度
                        pc.getQuest().set_step(KnightLv15_1.QUEST.get_id(), 2);
                    }
                } else {
                    isCloseList = true;
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
