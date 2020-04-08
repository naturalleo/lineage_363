package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.CrownLv30_1;
import com.lineage.data.quest.CrownLv45_1;
import com.lineage.data.quest.ElfLv30_1;
import com.lineage.data.quest.ElfLv45_1;
import com.lineage.data.quest.KnightLv30_1;
import com.lineage.data.quest.KnightLv45_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 马沙<BR>
 * 70653<BR>
 * 说明:王族的信念 (王族45级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Masha extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Masha.class);

    private Npc_Masha() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Masha();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // LV30任务未完成
                if (!pc.getQuest().isEnd(CrownLv30_1.QUEST.get_id())) {
                    // 你好，我是迪嘉勒廷公爵的侍从长 马沙。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "masha"));
                    return;
                }
                // LV45任务已经完成
                if (pc.getQuest().isEnd(CrownLv45_1.QUEST.get_id())) {
                    // 王族徽章片块代表着你君主的资格
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "masha4"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= CrownLv45_1.QUEST.get_questlevel()) {
                    // 任务尚未开始
                    if (!pc.getQuest().isStart(CrownLv45_1.QUEST.get_id())) {
                        // 我从艾莉亚那里已经听到了有关你在风木村的功迹
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "masha1"));

                    } else {// 任务已经开始
                        // 递给王族徽章片块
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "masha3"));
                    }

                } else {
                    // 你好，我是迪嘉勒廷公爵的侍从长 马沙。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "masha"));
                }

            } else if (pc.isKnight()) {// 骑士
                // LV30任务未完成
                if (!pc.getQuest().isEnd(KnightLv30_1.QUEST.get_id())) {
                    // 你好，我是迪嘉勒廷公爵的侍从长 马沙。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "masha"));
                    return;
                }
                // LV45任务已经完成
                if (pc.getQuest().isEnd(KnightLv45_1.QUEST.get_id())) {
                    // 谢谢你为了亚丁所做协助。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mashak3"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= KnightLv45_1.QUEST.get_questlevel()) {
                    // 任务尚未开始
                    if (!pc.getQuest().isStart(KnightLv45_1.QUEST.get_id())) {
                        // 我己经透过杰瑞德得知你的消息。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "mashak1"));

                    } else {// 任务已经开始
                        // 给予寻找调查员的结果
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "mashak2"));
                    }

                } else {
                    // 你好，我是迪嘉勒廷公爵的侍从长 马沙。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "masha"));
                }

            } else if (pc.isElf()) {// 精灵
                // LV30任务未完成
                if (!pc.getQuest().isEnd(ElfLv30_1.QUEST.get_id())) {
                    // 你好，我是迪嘉勒廷公爵的侍从长 马沙。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "masha"));
                    return;
                }
                // LV45任务已经完成
                if (pc.getQuest().isEnd(ElfLv45_1.QUEST.get_id())) {
                    // 谢谢你为了亚丁所做协助。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mashae3"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= ElfLv45_1.QUEST.get_questlevel()) {
                    // 任务尚未开始
                    if (!pc.getQuest().isStart(ElfLv45_1.QUEST.get_id())) {
                        // 我已收到森林之母送来的消息正等待着你的到来。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "mashae1"));

                    } else {// 任务已经开始
                        // 给予寻找调查员的结果
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "mashae2"));
                    }

                } else {
                    // 你好，我是迪嘉勒廷公爵的侍从长 马沙。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "masha"));
                }

            } else if (pc.isWizard()) {// 法师
                // 你好，我是迪嘉勒廷公爵的侍从长 马沙。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "masha"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 你好，我是迪嘉勒廷公爵的侍从长 马沙。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "masha"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 你好，我是迪嘉勒廷公爵的侍从长 马沙。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "masha"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 你好，我是迪嘉勒廷公爵的侍从长 马沙。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "masha"));

            } else {
                // 你好，我是迪嘉勒廷公爵的侍从长 马沙。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "masha"));
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
            // LV45任务已经完成
            if (pc.getQuest().isEnd(CrownLv45_1.QUEST.get_id())) {
                return;
            }
            if (cmd.equalsIgnoreCase("quest 15 masha2")) {// 接受马沙的试练
                // 将任务设置为执行中
                QuestClass.get().startQuest(pc, CrownLv45_1.QUEST.get_id());
                // 很久以前亚丁王国里有个只传给王族的王族徽章。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "masha2"));

            } else if (cmd.equalsIgnoreCase("request ring of guardian")) {// 递给王族徽章片块
                // 需要物件不足
                if (CreateNewItem.checkNewItem(pc, new int[] { 40586, 40587 },// 王族徽章的碎片A(背叛的妖魔队长)
                                                                              // x
                                                                              // 1
                                                                              // 王族徽章的碎片B
                                                                              // x
                                                                              // 1
                        new int[] { 1, 1 }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                    // 关闭对话窗
                    isCloseList = true;

                } else {// 需要物件充足
                    // 收回任务需要物件 给予任务完成物件
                    CreateNewItem.createNewItem(pc, new int[] { 40586, 40587 },// 王族徽章的碎片A
                                                                               // x
                                                                               // 1
                                                                               // 王族徽章的碎片B
                                                                               // x
                                                                               // 1
                            new int[] { 1, 1 }, new int[] { 20287 }, // 守护者的戒指 x
                                                                     // 1
                            1, new int[] { 1 });// 给予

                    // 将任务设置为结束
                    QuestClass.get().endQuest(pc, CrownLv45_1.QUEST.get_id());
                    // 王族徽章片块代表着你君主的资格
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "masha4"));
                }
            }

        } else if (pc.isKnight()) {// 骑士
            // LV45任务已经完成
            if (pc.getQuest().isEnd(KnightLv45_1.QUEST.get_id())) {
                return;
            }
            if (cmd.equalsIgnoreCase("quest 20 mashak2")) {// 关于邪恶势力
                // 将任务设置为执行中
                QuestClass.get().startQuest(pc, KnightLv45_1.QUEST.get_id());
                // 给予寻找调查员的结果
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mashak2"));

            } else if (cmd.equalsIgnoreCase("request belt of bravery")) {// 给予寻找调查员的结果
                // 需要物件不足
                if (CreateNewItem.checkNewItem(pc, new int[] { 40597, 40593 },// 破损的调查簿、调查簿的缺页(夜之视野
                                                                              // 可有可无)
                        new int[] { 1, 1 }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                    // 关闭对话窗
                    isCloseList = true;

                } else {// 需要物件充足
                    // 收回任务需要物件 给予任务完成物件
                    CreateNewItem.createNewItem(pc, new int[] { 40597, 40593 },// 破损的调查簿、调查簿的缺页(夜之视野
                                                                               // 可有可无)
                            new int[] { 1, 1 }, new int[] { 20318 }, // 勇敢皮带 x 1
                            1, new int[] { 1 });// 给予

                    // 夜之视野(夜之视野 可有可无)
                    final L1ItemInstance item = pc.getInventory().findItemId(
                            20026);
                    if (item != null) {
                        if (item.isEquipped()) {
                            // 脱除装备
                            pc.getInventory().setEquipped(item, false);
                        }
                        pc.getInventory().removeItem(item, 1);// 删除道具
                    }

                    // 将任务设置为结束
                    QuestClass.get().endQuest(pc, KnightLv45_1.QUEST.get_id());
                    // 谢谢你为了亚丁所做协助。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mashak3"));
                }
            }

        } else if (pc.isElf()) {// 精灵
            // LV45任务已经完成
            if (pc.getQuest().isEnd(ElfLv45_1.QUEST.get_id())) {
                return;
            }
            if (cmd.equalsIgnoreCase("quest 14 mashae2")) {// 关于邪恶势力
                // 将任务设置为执行中
                QuestClass.get().startQuest(pc, ElfLv45_1.QUEST.get_id());
                // 给予寻找调查员的结果
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mashae2"));

            } else if (cmd.equalsIgnoreCase("request bag of masha")) {// 给予调查的结果
                // 需要物件不足
                if (CreateNewItem.checkNewItem(pc, new int[] { 40533, 192 },// 古代钥匙、水之竖琴
                        new int[] { 1, 1 }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                    // 关闭对话窗
                    isCloseList = true;

                } else {// 需要物件充足
                    // 收回任务需要物件 给予任务完成物件
                    CreateNewItem.createNewItem(pc, new int[] { 40533, 192 },// 古代钥匙、水之竖琴
                            new int[] { 1, 1 }, new int[] { 40546 }, // 马沙之袋
                            1, new int[] { 1 });// 给予

                    // 神秘贝壳(神秘贝壳 可有可无)
                    final L1ItemInstance item = pc.getInventory().findItemId(
                            40566);
                    if (item != null) {
                        pc.getInventory().removeItem(item, 1);// 删除道具
                    }

                    // 将任务设置为结束
                    QuestClass.get().endQuest(pc, ElfLv45_1.QUEST.get_id());
                    // 谢谢你为了亚丁所做协助。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mashae3"));
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
