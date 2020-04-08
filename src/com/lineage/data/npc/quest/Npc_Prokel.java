package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DragonKnightLv15_1;
import com.lineage.data.quest.DragonKnightLv30_1;
import com.lineage.data.quest.DragonKnightLv45_1;
import com.lineage.data.quest.DragonKnightLv50_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 长老 普洛凯尔<BR>
 * 85023<BR>
 * 
 * @author dexc
 * 
 */
public class Npc_Prokel extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Prokel.class);

    private Npc_Prokel() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Prokel();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 呵呵... 怎么会跑到这么远来了呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "prokel1"));

            } else if (pc.isKnight()) {// 骑士
                // 呵呵... 怎么会跑到这么远来了呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "prokel1"));

            } else if (pc.isElf()) {// 精灵
                // 呵呵... 怎么会跑到这么远来了呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "prokel1"));

            } else if (pc.isWizard()) {// 法师
                // 呵呵... 怎么会跑到这么远来了呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "prokel1"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 呵呵... 怎么会跑到这么远来了呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "prokel1"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // LV50任务已经完成
                if (pc.getQuest().isEnd(DragonKnightLv50_1.QUEST.get_id())) {
                    // 你来啦？有你这样的勇士，我就放心多了。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "prokel32"));
                    return;
                }
                // LV45任务已经完成
                if (pc.getQuest().isEnd(DragonKnightLv45_1.QUEST.get_id())) {
                    // 等级达成要求(LV50)
                    if (pc.getLevel() >= DragonKnightLv50_1.QUEST
                            .get_questlevel()) {
                        // 任务进度
                        switch (pc.getQuest().get_step(
                                DragonKnightLv50_1.QUEST.get_id())) {
                            case 0:// 任务尚未开始
                                   // 欢迎啊～勇士！
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "prokel21"));
                                break;

                            case 1:
                                // 交出时空裂痕碎片100个。
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "prokel24"));
                                break;

                            case 2:
                                if (pc.getInventory().checkItem(49202)) { // 已经具有物品(时空裂痕邪念碎片)
                                    // <font fg=ffffaf>时空裂痕邪念碎片</font>将会开启往
                                    // <font fg=ffff0>异界</font>次元之门的。
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "prokel33"));

                                } else {
                                    // 在<font fg=ffff0>异界
                                    // 奎斯特</font>调查得如何啊？有找到<font
                                    // fg=ffafff>灵魂之火</font>吗？
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "prokel27"));
                                }
                                break;

                            case 3:
                                if (pc.getInventory().checkItem(49202)) { // 已经具有物品(时空裂痕邪念碎片)
                                    // <font fg=ffffaf>时空裂痕邪念碎片</font>将会开启往
                                    // <font fg=ffff0>异界</font>次元之门的。
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "prokel33"));

                                } else {
                                    // 在<font fg=ffff0>异界
                                    // 奎斯特</font>调查得如何啊？有找到<font
                                    // fg=ffafff>灵魂之火</font>了吗？
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "prokel30"));
                                }
                                break;

                            case 4:
                                if (pc.getInventory().checkItem(49231)) { // 路西尔斯邪念碎片
                                    // 哇～哇～你手上拿着是什么呢？
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "prokel37"));

                                } else {
                                    // 在<font fg=ffff0>异界
                                    // 奎斯特</font>调查得如何啊？有找到<font
                                    // fg=ffafff>灵魂之火</font>吗？
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "prokel25"));
                                }
                                break;
                        }

                    } else {
                        // 修练做的如何了？过不久会给你新任务的。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "prokel20"));
                    }
                    return;
                }
                // LV30任务已经完成
                if (pc.getQuest().isEnd(DragonKnightLv30_1.QUEST.get_id())) {
                    // 等级达成要求(LV45)
                    if (pc.getLevel() >= DragonKnightLv45_1.QUEST
                            .get_questlevel()) {
                        // 任务进度
                        switch (pc.getQuest().get_step(
                                DragonKnightLv45_1.QUEST.get_id())) {
                            case 0:// 任务尚未开始
                                   // 欢迎啊～勇猛的龙骑士！
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "prokel15"));
                                break;

                            case 1:
                            case 2:
                            case 3:
                                // 幻术士的村庄应该不好找的
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "prokel16"));
                                break;

                            case 4:
                                if (pc.getInventory().checkItem(49224)) { // 幻术士同盟徽印
                                    // 交出幻术士同盟徽印
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "prokel17"));

                                } else {
                                    // <font fg=ffffaf>幻术士同盟徽印</font>在哪呢？
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "prokel19"));
                                }
                                break;
                        }

                    } else {
                        // 上次教你的<font fg=ffffaf>血之渴望</font>魔法都学会了吗？
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "prokel14"));
                    }
                    return;
                }
                // LV15任务已经完成
                if (pc.getQuest().isEnd(DragonKnightLv15_1.QUEST.get_id())) {
                    // 等级达成要求(LV30)
                    if (pc.getLevel() >= DragonKnightLv30_1.QUEST
                            .get_questlevel()) {
                        // 任务进度
                        switch (pc.getQuest().get_step(
                                DragonKnightLv30_1.QUEST.get_id())) {
                            case 0:// 任务尚未开始
                                   // 嗯～感觉与之前状况有所不同呢，修练得如何？
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "prokel8"));
                                break;

                            case 1:// 达到1(任务开始)
                                   // 交出妖魔密使首领间谍书
                                   // 需要普洛凯尔的矿物袋
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "prokel10"));
                                break;
                        }

                    } else {
                        // 上次，依你所找到的<font fg=ffffaf>妖魔搜索文件</font>为基础，正在调查相关事项
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "prokel7"));
                    }

                } else {
                    // 等级达成要求
                    if (pc.getLevel() >= DragonKnightLv15_1.QUEST
                            .get_questlevel()) {
                        // 任务进度
                        switch (pc.getQuest().get_step(
                                DragonKnightLv15_1.QUEST.get_id())) {
                            case 0:// 任务尚未开始
                                   // 执行普洛凯尔的课题
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "prokel2"));
                                break;

                            case 1:// 达到1(任务开始)
                                   // 将妖魔搜索文件交出
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "prokel4"));
                                break;
                        }

                    } else {
                        // 真正龙骑士是需拥有亲自去解决所有问题的勇气啊！
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "prokel22"));
                    }
                }

            } else if (pc.isIllusionist()) {// 幻术师
                // 呵呵... 怎么会跑到这么远来了呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "prokel1"));

            } else {
                // 呵呵... 怎么会跑到这么远来了呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "prokel1"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;

        if (pc.isDragonKnight()) {// 龙骑士
            if (cmd.equalsIgnoreCase("a")) {// 执行普洛凯尔的课题 TODO
                // 等级达成要求(LV15任务)
                if (pc.getLevel() >= DragonKnightLv15_1.QUEST.get_questlevel()) {
                    // 给予任务道具(普洛凯尔的第1次指令书)
                    CreateNewItem.getQuestItem(pc, npc, 49210, 1);
                    // 将任务设置为执行中
                    QuestClass.get().startQuest(pc,
                            DragonKnightLv15_1.QUEST.get_id());
                    // 这指令书上写着你要执行的任务，请快点完成它吧。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "prokel3"));

                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("b")) {// 将妖魔搜索文件交出 TODO
                // 等级达成要求(LV15任务)
                if (pc.getLevel() >= DragonKnightLv15_1.QUEST.get_questlevel()) {
                    // 需要物件不足
                    if (CreateNewItem.checkNewItem(pc, new int[] { 49217,// 49217
                                                                         // 妖魔搜索文件(妖魔森林)
                            49218,// 49218 妖魔搜索文件(古鲁丁)
                            49219,// 49219 妖魔搜索文件(风木)
                    }, new int[] { 1, 1, 1, }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                        // 妖魔搜索文件在哪？
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "prokel6"));

                    } else {// 需要物件充足
                        // 收回任务需要物件 给予任务完成物件
                        CreateNewItem.createNewItem(pc, new int[] { 49217,// 49217
                                                                          // 妖魔搜索文件(妖魔森林)
                                49218,// 49218 妖魔搜索文件(古鲁丁)
                                49219,// 49219 妖魔搜索文件(风木)
                        }, new int[] { 1, 1, 1, }, new int[] { 49102,// 龙骑士书板(龙之护铠)
                                                                     // x 1
                                275,// 龙骑士双手剑 x 1
                        }, 1, new int[] { 1, 1, });// 给予

                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(49210, 1);// 普洛凯尔的第一次指令书
                        if (item != null) {
                            pc.getInventory().removeItem(item, 1);// 删除道具
                        }

                        // 将任务设置为结束
                        QuestClass.get().endQuest(pc,
                                DragonKnightLv15_1.QUEST.get_id());
                        // 比想像中回来满快的嘛，应该很隐密进行的吧？
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "prokel5"));
                    }

                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("c")) {// 执行普洛凯尔的第二次课题 TODO
                // LV15任务已经完成
                if (pc.getQuest().isEnd(DragonKnightLv15_1.QUEST.get_id())) {
                    // 等级达成要求(LV30任务)
                    if (pc.getLevel() >= DragonKnightLv30_1.QUEST
                            .get_questlevel()) {
                        // 给予任务道具(普洛凯尔的第2次指令书)
                        CreateNewItem.getQuestItem(pc, npc, 49211, 1);
                        // 给予任务道具(普洛凯尔的矿物袋)
                        CreateNewItem.getQuestItem(pc, npc, 49215, 1);
                        // 将任务设置为执行中
                        QuestClass.get().startQuest(pc,
                                DragonKnightLv30_1.QUEST.get_id());
                        // 若想取得只有妖魔密使首领所知道的高级情报，需变装成他们的样子会比较好。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "prokel9"));

                    } else {
                        isCloseList = true;
                    }

                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("d")) {// 交出妖魔密使首领间谍书 TODO
                // LV15任务已经完成
                if (pc.getQuest().isEnd(DragonKnightLv15_1.QUEST.get_id())) {
                    // 等级达成要求(LV30任务)
                    if (pc.getLevel() >= DragonKnightLv30_1.QUEST
                            .get_questlevel()) {
                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(49221, 1);// 妖魔密使首领间谍书 49221
                        if (item != null) {
                            pc.getInventory().removeItem(item, 1);// 删除道具

                            final L1ItemInstance item2 = pc.getInventory()
                                    .checkItemX(49211, 1);// 普洛凯尔的第二次指令书
                            if (item2 != null) {
                                pc.getInventory().removeItem(item2, 1);// 删除道具
                            }

                            // 将任务设置为结束
                            QuestClass.get().endQuest(pc,
                                    DragonKnightLv30_1.QUEST.get_id());
                            // 哈哈哈！你一定可以达到目标的。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "prokel11"));

                            // 给予任务道具(普洛凯尔的第一次信件)
                            CreateNewItem.getQuestItem(pc, npc, 49213, 1);
                            // 给予任务道具(龙骑士书板(血之渴望))
                            CreateNewItem.getQuestItem(pc, npc, 49107, 1);

                        } else {
                            // 妖魔密使首领间谍书到底在哪呢？ 该不会任务失败吧？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "prokel12"));
                        }

                    } else {
                        isCloseList = true;
                    }

                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("e")) {// 需要普洛凯尔的矿物袋 TODO
                // LV15任务已经完成
                if (pc.getQuest().isEnd(DragonKnightLv15_1.QUEST.get_id())) {
                    // 等级达成要求(LV30任务)
                    if (pc.getLevel() >= DragonKnightLv30_1.QUEST
                            .get_questlevel()) {
                        if (pc.getInventory().checkItem(49223)) { // 已经具有物品-妖魔密使的徽印
                            // 矿物袋子不是已经给过了吗？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "prokel35"));
                            return;
                        }
                        if (pc.getInventory().checkItem(49215)) { // 已经具有物品
                            // 矿物袋子不是已经给过了吗？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "prokel35"));

                        } else {
                            // 给予任务道具(普洛凯尔的矿物袋)
                            CreateNewItem.getQuestItem(pc, npc, 49215, 1);
                            // 什么？？给你的矿物袋子遗失了？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "prokel13"));
                        }

                    } else {
                        isCloseList = true;
                    }

                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("f")) {// 执行普洛凯尔的第三次课题 TODO
                // LV30任务已经完成
                if (pc.getQuest().isEnd(DragonKnightLv30_1.QUEST.get_id())) {
                    // 等级达成要求(LV45任务)
                    if (pc.getLevel() >= DragonKnightLv45_1.QUEST
                            .get_questlevel()) {
                        // 给予任务道具(普洛凯尔的第三次指令书)
                        CreateNewItem.getQuestItem(pc, npc, 49212, 1);
                        // 给予任务道具(普洛凯尔的信件)
                        CreateNewItem.getQuestItem(pc, npc, 49209, 1);
                        // 给予任务道具(结盟瞬间移动卷轴)
                        CreateNewItem.getQuestItem(pc, npc, 49226, 1);
                        // 将任务设置为执行中
                        QuestClass.get().startQuest(pc,
                                DragonKnightLv45_1.QUEST.get_id());
                        // 幻术士的村庄应该不好找的
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "prokel16"));

                    } else {
                        isCloseList = true;
                    }

                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("g")) {// 交出幻术士同盟徽印 TODO
                // LV30任务已经完成
                if (pc.getQuest().isEnd(DragonKnightLv30_1.QUEST.get_id())) {
                    // 等级达成要求(LV45任务)
                    if (pc.getLevel() >= DragonKnightLv45_1.QUEST
                            .get_questlevel()) {
                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(49224, 1);// 幻术士同盟徽印 49224
                        if (item != null) {
                            pc.getInventory().removeItem(item, 1);// 删除道具

                            final L1ItemInstance item2 = pc.getInventory()
                                    .checkItemX(49212, 1);// 普洛凯尔的第三次指令书
                            if (item2 != null) {
                                pc.getInventory().removeItem(item2, 1);// 删除道具
                            }

                            // 将任务设置为结束
                            QuestClass.get().endQuest(pc,
                                    DragonKnightLv45_1.QUEST.get_id());
                            // 辛苦了～果然得以信任，我会持续观察的
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "prokel18"));

                            // 给予任务道具(普洛凯尔的第二次信件)
                            CreateNewItem.getQuestItem(pc, npc, 49214, 1);

                        } else {
                            // <font fg=ffffaf>幻术士同盟徽印</font>在哪呢？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "prokel19"));
                        }

                    } else {
                        isCloseList = true;
                    }

                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("h")) {// 执行普洛凯尔第四课题 TODO
                // LV45任务已经完成
                if (pc.getQuest().isEnd(DragonKnightLv45_1.QUEST.get_id())) {
                    // 等级达成要求(LV50)
                    if (pc.getLevel() >= DragonKnightLv50_1.QUEST
                            .get_questlevel()) {
                        // 给予任务道具(普洛凯尔的第四次指令书)
                        CreateNewItem.getQuestItem(pc, npc, 49546, 1);
                        // 将任务设置为执行中
                        QuestClass.get().startQuest(pc,
                                DragonKnightLv50_1.QUEST.get_id());
                        // 真正龙骑士是需拥有亲自去解决所有问题的勇气啊！
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "prokel22"));

                    } else {
                        isCloseList = true;
                    }

                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("i")) {// 交出时空裂痕碎片100个 TODO
                // LV45任务已经完成
                if (pc.getQuest().isEnd(DragonKnightLv45_1.QUEST.get_id())) {
                    // 等级达成要求(LV50)
                    if (pc.getLevel() >= DragonKnightLv50_1.QUEST
                            .get_questlevel()) {

                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(49101, 100);// 时空裂痕碎片49101
                        if (item != null) {
                            pc.getInventory().removeItem(item, 100);// 删除道具

                            final L1ItemInstance item2 = pc.getInventory()
                                    .checkItemX(49546, 1);// 普洛凯尔的第四次指令书
                            if (item2 != null) {
                                pc.getInventory().removeItem(item2, 1);// 删除道具
                            }

                            // <font fg=ffffaf>时空裂痕邪念碎片</font>将会开启往 <font
                            // fg=ffff0>异界</font>次元之门的。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "prokel33"));

                            // 提升任务进度
                            pc.getQuest().set_step(
                                    DragonKnightLv50_1.QUEST.get_id(), 2);
                            // 给予任务道具(普洛凯尔的第五次指令书)
                            CreateNewItem.getQuestItem(pc, npc, 49547, 1);
                            // 给予任务道具(时空裂痕邪念碎片)
                            CreateNewItem.getQuestItem(pc, npc, 49202, 1);
                            // 给予任务道具(普洛凯尔的护身符)
                            CreateNewItem.getQuestItem(pc, npc, 49216, 1);

                        } else {
                            // <font fg=ffffaf>时间裂痕碎片</font>100个在哪呢？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "prokel31"));
                        }

                    } else {
                        isCloseList = true;
                    }

                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("j")) {// 交出路西尔斯邪念碎片与灵魂之火灰烬 TODO
                // LV45任务已经完成
                if (pc.getQuest().isEnd(DragonKnightLv45_1.QUEST.get_id())) {
                    // 等级达成要求(LV50)
                    if (pc.getLevel() >= DragonKnightLv50_1.QUEST
                            .get_questlevel()) {

                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(49231, 1);// 路西尔斯邪念碎片 49231
                        if (item != null) {
                            pc.getInventory().removeItem(item);// 删除道具

                            final L1ItemInstance item2 = pc.getInventory()
                                    .checkItemX(49547, 1);// 普洛凯尔的第五次指令书
                            if (item2 != null) {
                                pc.getInventory().removeItem(item2);// 删除道具
                            }

                            final L1ItemInstance item3 = pc.getInventory()
                                    .checkItemX(49207, 1);// 灵魂之火灰烬
                            if (item3 != null) {
                                pc.getInventory().removeItem(item3);// 删除道具
                            }

                            final L1ItemInstance item4 = pc.getInventory()
                                    .checkItemX(49202, 1);// 空裂痕邪念碎片
                            if (item4 != null) {
                                pc.getInventory().removeItem(item4);// 删除道具
                            }

                            final L1ItemInstance item5 = pc.getInventory()
                                    .checkItemX(49216, 1);// 普洛凯尔的护身符
                            if (item5 != null) {
                                pc.getInventory().removeItem(item5);// 删除道具
                            }

                            final L1ItemInstance item6 = pc.getInventory()
                                    .checkItemX(49229, 1);// 异界邪念粉末
                            if (item6 != null) {
                                pc.getInventory().removeItem(item6);// 删除道具
                            }

                            final L1ItemInstance item7 = pc.getInventory()
                                    .checkItemX(49227, 1);// 红色之火碎片
                            if (item7 != null) {
                                pc.getInventory().removeItem(item7);// 删除道具
                            }

                            // 辛苦了，自豪的勇士啊！以你为荣啊。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "prokel26"));

                            // 将任务设置为结束
                            QuestClass.get().endQuest(pc,
                                    DragonKnightLv50_1.QUEST.get_id());
                            // 给予任务道具(发光的银块)
                            CreateNewItem.getQuestItem(pc, npc, 49228, 1);

                        } else {
                            // 337 \f1%0不足%s。
                            pc.sendPackets(new S_ServerMessage(337, "$5733(1)"));
                        }

                    } else {
                        isCloseList = true;
                    }

                } else {
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("k")) {// 取得时空裂痕碎片 TODO
                // LV45任务已经完成
                if (pc.getQuest().isEnd(DragonKnightLv45_1.QUEST.get_id())) {
                    // 等级达成要求(LV50)
                    if (pc.getLevel() >= DragonKnightLv50_1.QUEST
                            .get_questlevel()) {
                        if (pc.getInventory().checkItem(49202)) { // 已经具有物品(时空裂痕邪念碎片)
                            // 不是已经交给你<font fg=ffffaf>时空裂痕邪念碎片</font>不是吗？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "prokel29"));

                        } else {
                            if (!pc.getInventory().checkItem(49216)) { // 不具有物品
                                // 给予任务道具(普洛凯尔的护身符)
                                CreateNewItem.getQuestItem(pc, npc, 49216, 1);
                            }
                            // 给予任务道具(时空裂痕邪念碎片)
                            CreateNewItem.getQuestItem(pc, npc, 49202, 1);

                            // 这有<font fg=ffffaf>时空裂痕邪念碎片</font>与<font
                            // fg=ffffaf>普洛凯尔的护身符</font>。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "prokel28"));
                        }

                    } else {
                        isCloseList = true;
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
