package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.CrownLv15_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 杰罗<BR>
 * 70554<BR>
 * 王族的自知 (王族15级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Zero extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Zero.class);

    private Npc_Zero() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Zero();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 任务已经完成
                if (pc.getQuest().isEnd(CrownLv15_1.QUEST.get_id())) {
                    // 你也是为了对抗反王之势力，而来自远方陆地的人吗？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zero6"));

                } else {
                    // 等级达成要求
                    if (pc.getLevel() >= CrownLv15_1.QUEST.get_questlevel()) {
                        // 任务进度
                        switch (pc.getQuest().get_step(
                                CrownLv15_1.QUEST.get_id())) {
                            case 0:// 任务尚未开始
                                   // 杰罗的课题
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "zero1"));
                                // 将任务设置为执行中
                                QuestClass.get().startQuest(pc,
                                        CrownLv15_1.QUEST.get_id());
                                break;

                            case 1:// 达到1(任务开始)
                                   // 杰罗的课题
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "zero1"));
                                break;

                            default:// 达到2以上
                                // 有关甘特
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "zero5"));
                                break;
                        }

                    } else {
                        // 你也是为了对抗反王之势力，而来自远方陆地的人吗？
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zero6"));
                    }
                }

            } else if (pc.isKnight()) {// 骑士
                // 是否在寻找可跟随的王族呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zero2"));

            } else if (pc.isElf()) {// 精灵
                // 是否在寻找可跟随的王族呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zero2"));

            } else if (pc.isWizard()) {// 法师
                // 是否在寻找可跟随的王族呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zero2"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 是否在寻找可跟随的王族呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zero2"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 是否在寻找可跟随的王族呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zero2"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 是否在寻找可跟随的王族呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zero2"));

            } else {
                // 是否在寻找可跟随的王族呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zero2"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;

        if (pc.isCrown()) {// 王族
            if (cmd.equalsIgnoreCase("request cloak of red")) {// 给予搜索状。
                // 任务进度
                switch (pc.getQuest().get_step(CrownLv15_1.QUEST.get_id())) {
                    case 0:// 任务尚未开始
                        break;

                    case 1:// 达到1(任务开始)
                           // 需要物件不足
                        if (CreateNewItem.checkNewItem(pc, 40565, // 任务完成需要物件(搜索状
                                                                  // x 1)
                                1) < 1) {// 传回可交换道具数小于1(需要物件不足)
                            isCloseList = true;

                        } else {// 需要物件充足
                            // 收回任务需要物件 给予任务完成物件
                            CreateNewItem.createNewItem(pc, 40565, 1, // 需要搜索状 x
                                                                      // 1
                                    20065, 1);// 给予红色斗篷 x 1
                            // 提升任务进度
                            pc.getQuest().set_step(CrownLv15_1.QUEST.get_id(),
                                    2);
                            // 有关甘特
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "zero5"));
                        }
                        break;

                    default:// 达到2以上
                        // 有关甘特
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zero5"));
                        break;
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
