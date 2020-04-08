package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DragonKnightLv30_1;
import com.lineage.data.quest.DragonKnightLv45_1;
import com.lineage.data.quest.IllusionistLv15_1;
import com.lineage.data.quest.IllusionistLv30_1;
import com.lineage.data.quest.IllusionistLv45_1;
import com.lineage.data.quest.IllusionistLv50_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 长老 希莲恩<BR>
 * 85027<BR>
 * 
 * @author dexc
 * 
 */
public class Npc_Silrein extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Silrein.class);

    private Npc_Silrein() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Silrein();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 欢迎～我们是伺候死亡女神席琳的幻术士
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "silrein1"));

            } else if (pc.isKnight()) {// 骑士
                // 欢迎～我们是伺候死亡女神席琳的幻术士
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "silrein1"));

            } else if (pc.isElf()) {// 精灵
                // 欢迎～我们是伺候死亡女神席琳的幻术士
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "silrein1"));

            } else if (pc.isWizard()) {// 法师
                // 欢迎～我们是伺候死亡女神席琳的幻术士
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "silrein1"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 欢迎～我们是伺候死亡女神席琳的幻术士
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "silrein1"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                isDragonKnight(pc, npc);

            } else if (pc.isIllusionist()) {// 幻术师
                isIllusionist(pc, npc);

            } else {
                // 欢迎～我们是伺候死亡女神席琳的幻术士
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "silrein1"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 幻术师
     * 
     * @param pc
     * @param npc
     */
    private void isIllusionist(L1PcInstance pc, L1NpcInstance npc) {
        try {
            // LV50任务已经完成
            if (pc.getQuest().isEnd(IllusionistLv50_1.QUEST.get_id())) {
                // 修练的如何呢? 托你执行任务上的优秀结果，将复杂的事件渐渐解开了。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "silrein36"));
                return;
            }
            // LV45任务已经完成
            if (pc.getQuest().isEnd(IllusionistLv45_1.QUEST.get_id())) {
                // 等级达成要求
                if (pc.getLevel() >= IllusionistLv50_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(
                            IllusionistLv50_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                               // 执行希莲恩的第四次课题
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein27"));
                            break;

                        case 1:
                            // 交出时空裂痕碎片100个
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein29"));
                            break;

                        case 2:
                        case 3:
                        case 4:
                            if (pc.getInventory().checkItem(49206)) { // 塞维斯邪念碎片
                                                                      // 49206
                                // 安全回来了麻. 执行这次任务花的时间较久因此非常为你担心呢
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "silrein35"));

                            } else {
                                // 利用<font fg=ffffaf>时空裂痕邪念碎片</font>到过异界了吗?
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "silrein34"));
                            }
                            break;
                    }

                } else {
                    // 最近 <font fg=ffff0>亚丁</font>听说很多人看到 <font
                    // fg=ffffaf>时空裂痕</font>看到的人越来越多了.
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "silrein26"));
                }
                return;
            }
            // LV30任务已经完成
            if (pc.getQuest().isEnd(IllusionistLv30_1.QUEST.get_id())) {
                // 等级达成要求
                if (pc.getLevel() >= IllusionistLv45_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(
                            IllusionistLv45_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                               // 现在可是越来越有幻术士的样子喔。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein18"));
                            break;

                        case 1:
                            // 调查过白蚂蚁的痕迹吗？在那边到底发生什么事了？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein20"));
                            break;

                        case 2:
                        case 3:
                            if (pc.getInventory().checkItem(49201)) { // 完整的时间水晶球
                                // 已拿来了<font fg=ffffaf>完成的时间水晶球</font>啊。
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "silrein50"));
                                return;
                            }
                            if (pc.getInventory().checkItem(49202)) { // 时空裂痕邪念碎片
                                                                      // 49202
                                // 你手上拿的是 <font fg=ffffaf>时空裂痕邪念碎片</font>吗?
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "silrein48"));
                                return;
                            }
                            // 请到<font fg=ffff0>巴拉卡斯栖息地火龙窟</font>找寻白蚁的痕迹
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein23"));
                            break;
                    }

                } else {
                    // 辛苦了～
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "silrein13"));
                }
                return;
            }
            // LV15任务已经完成
            if (pc.getQuest().isEnd(IllusionistLv15_1.QUEST.get_id())) {
                // 等级达成要求
                if (pc.getLevel() >= IllusionistLv30_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(
                            IllusionistLv30_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                               // 执行希莲恩的第二课题
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein11"));
                            break;

                        case 1:
                        case 2:
                        case 3:
                            if (pc.getInventory().checkItem(49191)) { // 艾尔摩部队日记
                                // 手上拿的是什么书呢？
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "silrein46"));
                                return;
                            }
                            if (pc.getInventory().checkItem(49190)) { // 封印的索夏依卡遗物
                                // 你手里拿的东西是什么呢？
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "silrein52"));
                                return;
                            }
                            // 有查到欧瑞村庄附近出末的艾尔摩军团真面目到底是什么吗？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein14"));
                            break;
                    }

                } else {
                    // 目前已经熟悉该怎么使用魔法立方了吗？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "silrein5"));
                }

            } else {
                // 等级达成要求
                if (pc.getLevel() >= IllusionistLv15_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(
                            IllusionistLv15_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                               // 希莲恩的第一次课题。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein2"));
                            break;

                        case 1:// 达到1(任务开始)
                               // 交出分析污染原因的物品。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein4"));
                            break;
                    }

                } else {
                    // 欢迎光临～真年轻的幻术士啊....
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "silrein3"));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 龙骑士
     * 
     * @param pc
     * @param npc
     */
    private void isDragonKnight(L1PcInstance pc, L1NpcInstance npc) {
        try {
            // LV45任务已经完成
            if (pc.getQuest().isEnd(DragonKnightLv45_1.QUEST.get_id())) {
                // 时间停止的地方... 永恒的生命啊..
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "silrein43"));
                return;
            }
            // 等级达成要求(LV45)
            if (pc.getLevel() >= DragonKnightLv45_1.QUEST.get_questlevel()) {
                // 任务进度
                switch (pc.getQuest().get_step(
                        DragonKnightLv45_1.QUEST.get_id())) {
                    case 1:
                        // 旅途愉快吗? 风好凉喔. 伟大的光龙 <font fg=fff00>奥拉奇里亚</font>的子孙啊
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "silrein37"));
                        break;

                    case 2:
                        // 妖魔的行动非常不寻常，听说召开元老会议，以一起渡过这难关的同事表达点诚意。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "silrein38"));
                        break;

                    case 3:
                        // 交出雪怪之心 10个
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "silrein40"));
                        break;

                    default:
                        // 欢迎～我们是伺候死亡女神席琳的幻术士
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "silrein1"));
                        break;
                }

            } else {
                // 欢迎～我们是伺候死亡女神席琳的幻术士
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "silrein1"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;

        if (pc.isIllusionist()) {// 幻术师
            if (cmd.equalsIgnoreCase("a")) {// 希莲恩的第一次课题。 TODO
                // 等级达成要求
                if (pc.getLevel() >= IllusionistLv15_1.QUEST.get_questlevel()) {
                    // 给予任务道具(希莲恩的第一次信件)
                    CreateNewItem.getQuestItem(pc, npc, 49172, 1);
                    // 将任务设置为执行中
                    QuestClass.get().startQuest(pc,
                            IllusionistLv15_1.QUEST.get_id());
                    // 你对妖精森林的<font fg=ffffaf>世界树 迷幻森林之母</font>了解吗？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "silrein6"));

                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("b")) {// 交出分析污染原因的物品。 TODO
                // 等级达成要求
                if (pc.getLevel() >= IllusionistLv15_1.QUEST.get_questlevel()) {
                    // 需要物件不足
                    if (CreateNewItem.checkNewItem(pc, new int[] { 40510,// 污浊安特的树皮
                                                                         // x 1
                            40512,// 污浊安特的树枝 x 1
                            40511,// 污浊安特的水果 x 1
                            49169,// 污浊妖魔之心 x 10
                            49170,// 污浊精灵核晶 x 1
                    }, new int[] { 1, 1, 1, 10, 1, }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                        // 调查完毕了吗？请将可以分析被污染原因的物品交给我吧。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "silrein8"));

                    } else {// 需要物件充足
                        // 收回任务需要物件 给予任务完成物件
                        CreateNewItem.createNewItem(pc, new int[] { 40510,// 污浊安特的树皮
                                                                          // x 1
                                40512,// 污浊安特的树枝 x 1
                                40511,// 污浊安特的水果 x 1
                                49169,// 污浊妖魔之心 x 10
                                49170,// 污浊精灵核晶 x 1
                        }, new int[] { 1, 1, 1, 10, 1, }, new int[] { 49121,// 记忆水晶(立方：燃烧)
                                                                            // x
                                                                            // 1
                                269,// 幻术士魔杖 x 1
                        }, 1, new int[] { 1, 1, });// 给予

                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(49172, 1);// 希莲恩的第一次信件
                        if (item != null) {
                            pc.getInventory().removeItem(item, 1);// 删除道具
                        }

                        // 将任务设置为结束
                        QuestClass.get().endQuest(pc,
                                IllusionistLv15_1.QUEST.get_id());
                        // 辛苦了！透过这些可以查出<font fg=ffff0>眠龙洞穴</font>受污染事件的问题点了。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "silrein7"));
                    }

                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("c")) {// 执行希莲恩的第二课题 TODO
                // LV15任务已经完成
                if (pc.getQuest().isEnd(IllusionistLv15_1.QUEST.get_id())) {
                    // 等级达成要求
                    if (pc.getLevel() >= IllusionistLv30_1.QUEST
                            .get_questlevel()) {
                        // 给予任务道具(希莲恩的第二次信件)
                        CreateNewItem.getQuestItem(pc, npc, 49173, 1);
                        // 给予任务道具(希莲恩之袋)
                        CreateNewItem.getQuestItem(pc, npc, 49179, 1);
                        // 将任务设置为执行中
                        QuestClass.get().startQuest(pc,
                                IllusionistLv30_1.QUEST.get_id());
                        // 听说欧瑞村庄附近出现艾尔摩军团欺负亚丁骑士与修练中的幼小幻术士.
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "silrein12"));

                    } else {
                        isCloseList = true;
                    }
                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("d")) {// 交出艾尔摩部队日记 TODO
                // LV15任务已经完成
                if (pc.getQuest().isEnd(IllusionistLv15_1.QUEST.get_id())) {
                    // 等级达成要求
                    if (pc.getLevel() >= IllusionistLv30_1.QUEST
                            .get_questlevel()) {
                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(49191, 1);// 艾尔摩部队日记
                        if (item != null) {
                            pc.getInventory().removeItem(item, 1);// 删除道具

                            final L1ItemInstance item2 = pc.getInventory()
                                    .checkItemX(49173, 1);// 希莲恩的第二次信件
                            if (item2 != null) {
                                pc.getInventory().removeItem(item2, 1);// 删除道具
                            }

                            // 将任务设置为结束
                            QuestClass.get().endQuest(pc,
                                    IllusionistLv30_1.QUEST.get_id());
                            // 辛苦了～
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein13"));

                            // 给予任务道具(记忆水晶(立方：冲击))
                            CreateNewItem.getQuestItem(pc, npc, 49131, 1);
                            // 给予任务道具(幻术士法书)
                            CreateNewItem.getQuestItem(pc, npc, 21101, 1);

                        } else {
                            // 337 \f1%0不足%s。
                            pc.sendPackets(new S_ServerMessage(337, "$5634(1)"));
                            isCloseList = true;
                        }

                    } else {
                        isCloseList = true;
                    }
                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("o")) {// 重新接收生锈的笛子 TODO
                // LV15任务已经完成
                if (pc.getQuest().isEnd(IllusionistLv15_1.QUEST.get_id())) {
                    // 等级达成要求
                    if (pc.getLevel() >= IllusionistLv30_1.QUEST
                            .get_questlevel()) {
                        if (pc.getInventory().checkItem(49189)) { // 已经具有物品-索夏依卡灵魂之笛
                            // 应该已经给予了 <font
                            // fg=ffffaf>生锈的笛子</font>吧，请好好找一下我送你的袋子里面喔。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein17"));
                            return;
                        }
                        if (pc.getInventory().checkItem(49186)) { // 已经具有物品
                            // 应该已经给予了 <font
                            // fg=ffffaf>生锈的笛子</font>吧，请好好找一下我送你的袋子里面喔。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein17"));

                        } else {
                            // 给予任务道具(生锈的笛子)
                            CreateNewItem.getQuestItem(pc, npc, 49186, 1);
                            // 这边有 <font fg=ffffaf>生锈的笛子</font>，这是重要的物品请小心保管。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein16"));
                        }

                    } else {
                        isCloseList = true;
                    }

                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("e")) {// 执行希莲恩的第三课题 TODO
                // LV30任务已经完成
                if (pc.getQuest().isEnd(IllusionistLv30_1.QUEST.get_id())) {
                    // 等级达成要求
                    if (pc.getLevel() >= IllusionistLv45_1.QUEST
                            .get_questlevel()) {
                        // 给予任务道具(希莲恩的第三次信件)
                        CreateNewItem.getQuestItem(pc, npc, 49174, 1);
                        // 给予任务道具(希莲恩之袋)
                        CreateNewItem.getQuestItem(pc, npc, 49180, 1);
                        // 将任务设置为执行中
                        QuestClass.get().startQuest(pc,
                                IllusionistLv45_1.QUEST.get_id());
                        // 来，我刚给你的是<font fg=ffffaf>时空裂痕水晶(绿色)</font>。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "silrein19"));

                    } else {
                        isCloseList = true;
                    }

                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("f")) {// 交出记忆的碎片 TODO
                // LV30任务已经完成
                if (pc.getQuest().isEnd(IllusionistLv30_1.QUEST.get_id())) {
                    // 等级达成要求
                    if (pc.getLevel() >= IllusionistLv45_1.QUEST
                            .get_questlevel()) {
                        // 需要物件不足
                        if (CreateNewItem.checkNewItem(pc, new int[] { 49194,// 第一次记忆碎片
                                                                             // 49194
                                49195,// 第二次记忆碎片 49195
                                49196,// 第三次记忆碎片 49196
                        }, new int[] { 1, 1, 1, }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                            // <font fg=ffffaf>记忆碎片</font>在哪呢？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein21"));

                        } else {// 需要物件充足
                            // 收回任务需要物件 给予任务完成物件
                            CreateNewItem.createNewItem(pc, new int[] { 49194,// 第一次记忆碎片
                                                                              // 49194
                                    49195,// 第二次记忆碎片 49195
                                    49196,// 第三次记忆碎片 49196
                            }, new int[] { 1, 1, 1, }, new int[] { 49193,// 时空裂痕水晶(蓝色)-3
                                                                         // 49193
                                    }, 1, new int[] { 3, });// 给予

                            // 将任务进度提升为2
                            pc.getQuest().set_step(
                                    IllusionistLv45_1.QUEST.get_id(), 2);
                            // 你也看过这记忆碎片了吗？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein22"));
                        }

                    } else {
                        isCloseList = true;
                    }

                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("g")) {// 交出时空裂痕邪念碎片 TODO
                // LV30任务已经完成
                if (pc.getQuest().isEnd(IllusionistLv30_1.QUEST.get_id())) {
                    // 等级达成要求
                    if (pc.getLevel() >= IllusionistLv45_1.QUEST
                            .get_questlevel()) {
                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(49202, 1);// 时空裂痕邪念碎片 49202
                        if (item != null) {
                            pc.getInventory().removeItem(item, 1);// 删除道具

                            final L1ItemInstance item2 = pc.getInventory()
                                    .checkItemX(49174, 1);// 希莲恩的第三次信件 49174
                            if (item2 != null) {
                                pc.getInventory().removeItem(item2, 1);// 删除道具
                            }

                            // 将任务设置为结束
                            QuestClass.get().endQuest(pc,
                                    IllusionistLv45_1.QUEST.get_id());

                            // <font fg=ffffaf>时空裂痕</font>!! <font
                            // fg=fff00>吉尔塔斯</font>的出现引起 <font
                            // fg=ffff0>亚丁</font>区域变的很混乱.
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein25"));
                            // 给予任务道具(幻术士斗篷)
                            CreateNewItem.getQuestItem(pc, npc, 21100, 1);

                        } else {
                            // 有找到有关白蚁的真理吗？透过<font
                            // fg=ffffaf>时空裂痕水晶(绿色)</font>查看相关痕迹的记忆吗？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein24"));
                        }

                    } else {
                        isCloseList = true;
                    }

                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("h")) {// 执行希莲恩的第四次课题 TODO
                // LV45任务已经完成
                if (pc.getQuest().isEnd(IllusionistLv45_1.QUEST.get_id())) {
                    // 等级达成要求
                    if (pc.getLevel() >= IllusionistLv50_1.QUEST
                            .get_questlevel()) {
                        // 给予任务道具(希莲恩的第五次信件)
                        CreateNewItem.getQuestItem(pc, npc, 49176, 1);
                        // 将任务设置为执行中
                        QuestClass.get().startQuest(pc,
                                IllusionistLv50_1.QUEST.get_id());
                        // 在亚丁世界中听说 <font fg=ffffaf>时空裂痕</font>不规则的开启
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "silrein28"));

                    } else {
                        isCloseList = true;
                    }

                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("i")) {// 交出时空裂痕碎片100个 TODO
                // LV45任务已经完成
                if (pc.getQuest().isEnd(IllusionistLv45_1.QUEST.get_id())) {
                    // 等级达成要求
                    if (pc.getLevel() >= IllusionistLv50_1.QUEST
                            .get_questlevel()) {

                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(49101, 100);// 时空裂痕碎片49101
                        if (item != null) {
                            pc.getInventory().removeItem(item, 100);// 删除道具

                            final L1ItemInstance item2 = pc.getInventory()
                                    .checkItemX(49176, 1);// 希莲恩的第五次信件
                            if (item2 != null) {
                                pc.getInventory().removeItem(item2, 1);// 删除道具
                            }

                            // 啊! 这就是 <font fg=ffffaf>时空裂痕</font>中掉落的碎片
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein31"));

                            // 提升任务进度
                            pc.getQuest().set_step(
                                    IllusionistLv50_1.QUEST.get_id(), 2);
                            // 给予任务道具(希莲恩的第六次信件 49177)
                            CreateNewItem.getQuestItem(pc, npc, 49177, 1);
                            // 给予任务道具(时空裂痕邪念碎片)
                            CreateNewItem.getQuestItem(pc, npc, 49202, 1);
                            // 给予任务道具(希莲恩的护身符 49178)
                            CreateNewItem.getQuestItem(pc, npc, 49178, 1);

                        } else {
                            // 你找到<font fg=ffffaf>时空裂痕碎片100个</font>吗?
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein30"));
                        }

                    } else {
                        isCloseList = true;
                    }

                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("j")) {// 交出塞维斯邪念碎片与灵魂之火灰烬 TODO
                // LV45任务已经完成
                if (pc.getQuest().isEnd(IllusionistLv45_1.QUEST.get_id())) {
                    // 等级达成要求
                    if (pc.getLevel() >= IllusionistLv50_1.QUEST
                            .get_questlevel()) {

                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(49206, 1);// 塞维斯邪念碎片 49206
                        if (item != null) {
                            pc.getInventory().removeItem(item);// 删除道具

                            final L1ItemInstance item2 = pc.getInventory()
                                    .checkItemX(49177, 1);// 希莲恩的第六次信件
                            if (item2 != null) {
                                pc.getInventory().removeItem(item2);// 删除道具
                            }

                            final L1ItemInstance item3 = pc.getInventory()
                                    .checkItemX(49207, 1);// 灵魂之火灰烬
                            if (item3 != null) {
                                pc.getInventory().removeItem(item3);// 删除道具
                            }

                            final L1ItemInstance item4 = pc.getInventory()
                                    .checkItemX(49202, 1);// 时空裂痕邪念碎片
                            if (item4 != null) {
                                pc.getInventory().removeItem(item4);// 删除道具
                            }

                            final L1ItemInstance item5 = pc.getInventory()
                                    .checkItemX(49178, 1);// 希莲恩的护身符
                            if (item5 != null) {
                                pc.getInventory().removeItem(item5);// 删除道具
                            }

                            final L1ItemInstance item6 = pc.getInventory()
                                    .checkItemX(49203, 1);// 食腐兽之血
                            if (item6 != null) {
                                pc.getInventory().removeItem(item6);// 删除道具
                            }

                            final L1ItemInstance item7 = pc.getInventory()
                                    .checkItemX(49204, 1);// 翼龙之血
                            if (item7 != null) {
                                pc.getInventory().removeItem(item7);// 删除道具
                            }

                            // 这次你的功劳最大～来～拿这个去找 <font fg=fff00>铁匠 巴特尔</font>吧。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein45"));

                            // 将任务设置为结束
                            QuestClass.get().endQuest(pc,
                                    IllusionistLv50_1.QUEST.get_id());
                            // 给予任务道具(希莲恩的推荐书 49181)
                            CreateNewItem.getQuestItem(pc, npc, 49181, 1);
                            // 给予任务道具(特别的原石 49205)
                            CreateNewItem.getQuestItem(pc, npc, 49205, 1);

                        } else {
                            // 利用<font fg=ffffaf>时空裂痕邪念碎片</font>到过异界了吗?
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein34"));
                        }

                    } else {
                        isCloseList = true;
                    }

                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("k")) {// 需要再调查 TODO
                // LV45任务已经完成
                if (pc.getQuest().isEnd(IllusionistLv45_1.QUEST.get_id())) {
                    // 等级达成要求
                    if (pc.getLevel() >= IllusionistLv50_1.QUEST
                            .get_questlevel()) {
                        if (pc.getInventory().checkItem(49202)) { // 已经具有物品(时空裂痕邪念碎片)
                            // 不是已经交给你<font fg=ffffaf>时空裂痕邪念碎片</font>不是吗？
                            // pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                            // "prokel29"));
                            isCloseList = true;

                        } else {
                            if (!pc.getInventory().checkItem(49178)) { // 不具有物品
                                // 给予任务道具(希莲恩的护身符 49178)
                                CreateNewItem.getQuestItem(pc, npc, 49178, 1);
                            }
                            // 给予任务道具(时空裂痕邪念碎片)
                            CreateNewItem.getQuestItem(pc, npc, 49202, 1);

                            // 利用你所取来的 <font fg=ffffaf>时空裂痕碎片</font>可引发 <font
                            // fg=ffffaf>时空裂痕邪念碎片</font>的力量
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein32"));
                        }

                    } else {
                        isCloseList = true;
                    }

                } else {
                    isCloseList = true;
                }
            }

        } else if (pc.isDragonKnight()) {// 龙骑士
            // LV30任务已经完成
            if (pc.getQuest().isEnd(DragonKnightLv30_1.QUEST.get_id())) {
                // 等级达成要求(LV45任务)
                if (pc.getLevel() >= DragonKnightLv45_1.QUEST.get_questlevel()) {
                    if (cmd.equalsIgnoreCase("l")) {// 交出长老普洛凯尔的信件
                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(49209, 1);// 普洛凯尔的信件
                        if (item != null) {
                            // 将任务进度提升为2
                            pc.getQuest().set_step(
                                    DragonKnightLv45_1.QUEST.get_id(), 2);
                            pc.getInventory().removeItem(item, 1);// 删除道具
                            // 妖魔的行动非常不寻常，听说召开元老会议，以一起渡过这难关的同事表达点诚意。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein38"));
                        }

                    } else if (cmd.equalsIgnoreCase("m")) {// 接受希莲恩的请求
                        // 将任务进度提升为3
                        pc.getQuest().set_step(
                                DragonKnightLv45_1.QUEST.get_id(), 3);
                        // 为了加工我们特有的原石需要 <font fg=ffffaf>雪怪之心</font> 10个.
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "silrein39"));

                        // 给予任务道具(希莲恩的指令书)
                        CreateNewItem.getQuestItem(pc, npc, 49171, 1);

                    } else if (cmd.equalsIgnoreCase("n")) {// 交出雪怪之心 10个
                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(49225, 10);// 雪怪之心
                        if (item != null) {

                            final L1ItemInstance item2 = pc.getInventory()
                                    .checkItemX(49171, 1);// 希莲恩的指令书
                            if (item2 != null) {
                                pc.getInventory().removeItem(item2, 1);// 删除道具
                            }

                            // 将任务进度提升为4
                            pc.getQuest().set_step(
                                    DragonKnightLv45_1.QUEST.get_id(), 4);
                            pc.getInventory().removeItem(item);// 删除道具
                            // 谢谢. <font fg=ffffaf>雪怪之心</font>拥有神秘的力量.
                            // 为了让我们特有的水晶加工需要相当多的材料。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein41"));

                            // 给予任务道具(幻术士同盟徽印)
                            CreateNewItem.getQuestItem(pc, npc, 49224, 1);

                        } else {
                            // <font fg=ffffaf>雪怪之心</font> 10个在哪里呢？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "silrein42"));
                        }
                    }

                } else {
                    isCloseList = true;
                }

            } else {
                isCloseList = true;
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
