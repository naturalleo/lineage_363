package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.WizardLv30_1;
import com.lineage.data.quest.WizardLv45_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 塔拉斯<BR>
 * 70763<BR>
 * 说明:不死族的叛徒 (法师30级以上官方任务)<BR>
 * 说明:法师的考验 (法师45级以上官方任务)<BR>
 * 
 * @author dexc
 * 
 */
public class Npc_Talass extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Talass.class);

    private Npc_Talass() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Talass();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 你为了找寻什么而来到这里呢？冒险者！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talass"));

            } else if (pc.isKnight()) {// 骑士
                // 你为了找寻什么而来到这里呢？冒险者！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talass"));

            } else if (pc.isElf()) {// 精灵
                // 你为了找寻什么而来到这里呢？冒险者！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talass"));

            } else if (pc.isWizard()) {// 法师
                // LV45任务已经完成
                if (pc.getQuest().isEnd(WizardLv45_1.QUEST.get_id())) {
                    // 你为了找寻什么而来到这里呢？冒险者！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talass"));
                    return;
                }
                // LV30任务已经完成
                if (pc.getQuest().isEnd(WizardLv30_1.QUEST.get_id())) {
                    // 等级达成要求
                    if (pc.getLevel() >= WizardLv45_1.QUEST.get_questlevel()) {
                        // 任务尚未开始
                        if (!pc.getQuest().isStart(WizardLv45_1.QUEST.get_id())) {
                            // 关于神奇的生命体
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "talassmq1"));

                        } else {// 任务已经开始
                            // 给予调查的结果
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "talassmq2"));
                        }

                    } else {
                        // 你为了找寻什么而来到这里呢？冒险者！
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "talass"));
                    }
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= WizardLv30_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(WizardLv30_1.QUEST.get_id())) {
                        case 4:// 达到4(关于不死族的骨头碎片)
                               // 关于不死族的骨头碎片
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "talassE1"));
                            break;

                        default:
                            // 你为了找寻什么而来到这里呢？冒险者！
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "talass"));
                            break;
                    }

                } else {
                    // 你为了找寻什么而来到这里呢？冒险者！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talass"));
                }

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 你为了找寻什么而来到这里呢？冒险者！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talass"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 你为了找寻什么而来到这里呢？冒险者！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talass"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 你为了找寻什么而来到这里呢？冒险者！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talass"));

            } else {
                // 你为了找寻什么而来到这里呢？冒险者！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talass"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        if (pc.isWizard()) {// 法师
            if (cmd.equalsIgnoreCase("quest 16 talassE2")) {// 关于不死族的骨头碎片
                // LV30任务已经完成
                if (pc.getQuest().isEnd(WizardLv30_1.QUEST.get_id())) {
                    return;
                }
                // 递给不死族的骨头碎片
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talassE2"));

            } else if (cmd.equalsIgnoreCase("request crystal staff")) {// 递给不死族的骨头碎片
                // LV30任务已经完成
                if (pc.getQuest().isEnd(WizardLv30_1.QUEST.get_id())) {
                    return;
                }
                int[] items = new int[] { 40580, 40569 }; // 不死族的骨头碎片 x 1 神秘魔杖 x
                                                          // 1
                int[] counts = new int[] { 1, 1 };
                int[] gitems = new int[] { 115 };// 水晶魔杖 x 1
                int[] gcounts = new int[] { 1 };

                // 需要物件不足
                if (CreateNewItem.checkNewItem(pc, items, counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
                    // 关闭对话窗
                    isCloseList = true;

                } else {// 需要物件充足
                    // 收回任务需要物件 给予任务完成物件
                    CreateNewItem.createNewItem(pc, items, // 不死族的骨头碎片 x 1 神秘魔杖
                                                           // x 1
                            counts, gitems, // 水晶魔杖 x 1
                            1, gcounts);// 给予

                    // 结束任务
                    pc.getQuest().set_end(WizardLv30_1.QUEST.get_id());
                    // 关闭对话窗
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("quest 18 talassmq2")) {// 关于神奇的生命体
                // LV45任务已经完成
                if (pc.getQuest().isEnd(WizardLv45_1.QUEST.get_id())) {
                    return;
                }
                // 将任务设置为执行中
                QuestClass.get().startQuest(pc, WizardLv45_1.QUEST.get_id());
                // 给予调查的结果
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talassmq2"));

            } else if (cmd.equalsIgnoreCase("request magic bag of talass")) {// 给予调查的结果
                // LV45任务已经完成
                if (pc.getQuest().isEnd(WizardLv45_1.QUEST.get_id())) {
                    return;
                }
                int[] items = new int[] { 40536 }; // 古代恶魔的记载 x 1
                int[] counts = new int[] { 1 };
                int[] gitems = new int[] { 40599 };// 塔拉斯的魔法袋 x 1
                int[] gcounts = new int[] { 1 };

                // 需要物件不足
                if (CreateNewItem.checkNewItem(pc, items, counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
                    // 关闭对话窗
                    isCloseList = true;

                } else {// 需要物件充足
                    // 收回任务需要物件 给予任务完成物件
                    CreateNewItem.createNewItem(pc, items, // 古代恶魔的记载 x 1
                            counts, gitems, // 塔拉斯的魔法袋 x 1
                            1, gcounts);// 给予

                    // 结束任务
                    pc.getQuest().set_end(WizardLv45_1.QUEST.get_id());
                    // 关闭对话窗
                    isCloseList = true;
                }
            }
        }

        if (cmd.equalsIgnoreCase("request bow of sayha")) {// 制造沙哈之弓
            int[] items = new int[] { 181, 40491, 40498, 40394 };//修正沙哈弓问题 hjx1000
            int[] counts = new int[] { 1, 30, 50, 15 };//修正沙哈弓问题 hjx1000
            int[] gitems = new int[] { 190 }; //修正沙哈弓问题 hjx1000
            int[] gcounts = new int[] { 1 };
            // 需要物件不足
            if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                    counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
                isCloseList = true;

            } else {// 需要物件充足
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, 1, gcounts);// 给予
                isCloseList = true;
            }
        }
        if (cmd.equalsIgnoreCase("Archmage cap")) {// 大法师之帽
            int[] items = new int[] { 20018, 20025, 20029, 20040, 41246 };//需要物品ID清单
            int[] counts = new int[] { 1, 1, 1, 1, 100000 };//需要物品数量清单
            int[] gitems = new int[] { 28007 }; //获得物品ID清单
            int[] gcounts = new int[] { 1 };//获得物品数量
            // 需要物件不足
            if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                    counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
                isCloseList = true;

            } else {// 需要物件充足
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, 1, gcounts);// 给予
                isCloseList = true;
            }
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
