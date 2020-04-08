package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.CrownLv15_1;
import com.lineage.data.quest.KnightLv30_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 甘特<BR>
 * 70522<BR>
 * 王族的自知 (王族15级以上官方任务)<BR>
 * 拯救被幽禁的吉姆 (骑士30级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Qunter extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Qunter.class);

    private Npc_Qunter() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Qunter();
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
                    // 你终于成功了！你现在应该已经了解到生命的可贵吧！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterp11"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= CrownLv15_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(CrownLv15_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                               // 关于策略家杰罗
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gunterp9"));
                            break;

                        case 1:// 达到1(任务开始)
                               // 关于策略家杰罗
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gunterp9"));
                            break;

                        case 2:// 达到2
                               // 请求‘甘特的测试
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gunterp1"));
                            break;

                        case 3:// 达到3
                               // 学习精准目标魔法
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gunterpev1"));
                            break;
                    }

                } else {
                    // 难道你自己没有感觉以你目前的经验根本就不成气候吗！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterp12"));
                }

            } else if (pc.isKnight()) {// 骑士
                if (pc.getLawful() < 0) {// 邪恶
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterk12"));
                    return;
                }
                // LV30任务已经完成
                if (pc.getQuest().isEnd(KnightLv30_1.QUEST.get_id())) {
                    // 哈哈哈，你终于通过了成为红骑士所需要的所有过程。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                            "gunterkEcg"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= KnightLv30_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(KnightLv30_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                        case 1:// 达到1(任务开始)
                               // <P align=justify>眼神看来很不寻常喔...
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gunterk9"));
                            break;

                        case 2:// 达到2(交谈吉姆)
                               // 接受甘特的试练
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gunterkE1"));
                            break;

                        case 3:// 达到3(接受试练)
                               // 递给杨果里恩的爪子
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gunterkE2"));
                            break;

                        default:// 其他
                            // 恭喜你～你终于成为有资格带着武器保卫世界并负起责任的人啊。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gunterkE3"));
                            break;
                    }

                } else {
                    // <P align=justify>眼神看来很不寻常喔...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterk9"));
                }

            } else if (pc.isElf()) {// 精灵
                // 哦~<a link="guntere2">森林深处</a>的灵魂啊，
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "guntere1"));

            } else if (pc.isWizard()) {// 法师
                // 用人类的方法</a>实现<a link="gunterw3">神的旨意</a>的人啊
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterw1"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 存在于光与黑的警戒点，
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterdw1"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 欢迎你呀～拥有龙之力量的外地人！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterdwk1"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 欢迎拥有古老知识以及神之力量的你们来到这里
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunteriw1"));

            } else {
                // 呵呵... 怎么会跑到这么远来了呢？
                // pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "prokel1"));
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
            if (cmd.equalsIgnoreCase("guntertest")) {// 请求‘甘特的测试’
                // 任务未完成
                if (!pc.getQuest().isEnd(CrownLv15_1.QUEST.get_id())) {
                    // 任务进度
                    switch (pc.getQuest().get_step(CrownLv15_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                               // 关于策略家杰罗
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gunterp9"));
                            break;

                        case 1:// 达到1(任务开始)
                               // 关于策略家杰罗
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gunterp9"));
                            break;

                        case 2:
                            // 提升任务进度
                            pc.getQuest().set_step(CrownLv15_1.QUEST.get_id(),
                                    3);
                            // 学习精准目标魔法
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gunterpev1"));
                            break;
                    }
                }

            } else if (cmd.equalsIgnoreCase("request spellbook112")) {// 学习精准目标魔法
                // 任务进度达到3
                if (pc.getQuest().get_step(CrownLv15_1.QUEST.get_id()) == 3) {
                    // 需要物件不足
                    if (CreateNewItem.checkNewItem(pc, 40564, // 任务完成需要物件(生命的卷轴
                                                              // x 1)
                            1) < 1) {// 传回可交换道具数小于1(需要物件不足)
                        isCloseList = true;

                    } else {// 需要物件充足
                        // 收回任务需要物件 给予任务完成物件
                        CreateNewItem.createNewItem(pc, 40564, 1, // 需要生命的卷轴 x 1
                                40226, 1);// 给予魔法书(精准目标) x 1
                        // 将任务设置为结束
                        QuestClass.get().endQuest(pc,
                                CrownLv15_1.QUEST.get_id());
                        isCloseList = true;
                    }

                } else {
                    // 关于策略家杰罗
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterp9"));
                }
            }

        } else if (pc.isKnight()) {// 骑士
            if (cmd.equalsIgnoreCase("quest 14 gunterkE2")) {// 接受甘特的试练
                // 提升任务进度
                pc.getQuest().set_step(KnightLv30_1.QUEST.get_id(), 3);
                // 递给杨果里恩的爪子
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gunterkE2"));

            } else if (cmd.equalsIgnoreCase("request sword of red knights")) {// 递给杨果里恩的爪子
                // 任务进度达到3
                if (pc.getQuest().get_step(KnightLv30_1.QUEST.get_id()) == 3) {
                    // 需要物件不足
                    if (CreateNewItem.checkNewItem(pc, 40590, // 任务完成需要物件(杨果里恩之爪
                                                              // x 1)
                            1) < 1) {// 传回可交换道具数小于1(需要物件不足)
                        isCloseList = true;

                    } else {// 需要物件充足
                        // 收回任务需要物件 给予任务完成物件
                        CreateNewItem.createNewItem(pc, 40590, 1, // 需要杨果里恩之爪 x
                                                                  // 1
                                30, 1);// 红骑士之剑 x 1
                        // 提升任务进度
                        pc.getQuest().set_step(KnightLv30_1.QUEST.get_id(), 4);
                        // 恭喜你～你终于成为有资格带着武器保卫世界并负起责任的人啊。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "gunterkE3"));
                    }

                } else {
                    isCloseList = true;
                }
            }

        } else if (pc.isElf()) {// 精灵
            if (cmd.equalsIgnoreCase("guntertest")) {// 请求‘甘特的测试’
                // 你的教师不是那些‘永恒的树’与‘永久的大地’吗？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "guntereev1"));
            }
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
