package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ElfLv45_2;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 罗宾孙<BR>
 * 71256<BR>
 * 说明:妖精族传说中的弓 (妖精45级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Robinhood extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Robinhood.class);

    private Npc_Robinhood() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Robinhood();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 你是怎么到这里来的？这里是精灵才能来的地方。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "robinhood2"));

            } else if (pc.isKnight()) {// 骑士
                // 你是怎么到这里来的？这里是精灵才能来的地方。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "robinhood2"));

            } else if (pc.isElf()) {// 精灵
                // LV45任务已经完成
                if (pc.getQuest().isEnd(ElfLv45_2.QUEST.get_id())) {
                    // 完成了! 太棒了! 可不可以捏我一下
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                            "robinhood12"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= ElfLv45_2.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(ElfLv45_2.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                               // 你有事要找我吗?
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "robinhood1"));
                            break;

                        case 1:// 达到1(任务开始)
                               // 你所说的传说中的长弓好像是炽炎天使弓
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "robinhood5"));
                            break;

                        case 2:// 达到2
                        case 3:// 达到3
                        case 4:// 达到4
                        case 5:// 达到5
                               // 你到伊娃的圣地去找神官 '知布烈'。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "robinhood13"));
                            break;

                        case 6:// 达到6
                               // 月光之气息
                            if (pc.getInventory().checkItem(41351, 1)) {
                                // 拿出材料和便条纸
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "robinhood9"));

                            } else {
                                // 你到伊娃的圣地去找神官 '知布烈'。
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "robinhood13"));
                            }
                            break;

                        case 7:// 达到7
                               // 罗宾孙之戒
                            if (pc.getInventory().checkItem(41350, 1)) {
                                // 将罗宾孙的戒指和材料交给他
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "robinhood11"));

                            } else {
                                // 为了制作炽炎天使弓的弓架，材料我都写在清单上，那材料都在哪里呢?
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "robinhood18"));
                            }
                            break;
                    }

                } else {
                    // 哼哼...我口好渴，这时候如果有苹果汁会更好
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                            "robinhood19"));
                }

            } else if (pc.isWizard()) {// 法师
                // 你是怎么到这里来的？这里是精灵才能来的地方。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "robinhood2"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 你是怎么到这里来的？这里是精灵才能来的地方。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "robinhood2"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 你是怎么到这里来的？这里是精灵才能来的地方。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "robinhood2"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 你是怎么到这里来的？这里是精灵才能来的地方。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "robinhood2"));

            } else {
                // 你是怎么到这里来的？这里是精灵才能来的地方。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "robinhood2"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        if (pc.isElf()) {// 精灵
            // LV45任务已经完成
            if (pc.getQuest().isEnd(ElfLv45_2.QUEST.get_id())) {
                return;
            }
            // 任务进度
            switch (pc.getQuest().get_step(ElfLv45_2.QUEST.get_id())) {
                case 0:// 达到0
                    if (cmd.equals("A")) {// 同步客户端修改为精灵饼干 hjx1000
                        final L1ItemInstance item = pc.getInventory()
                                .findItemId(40068);
                        if (item != null) {
                            pc.getInventory().removeItem(item, 1);// 删除道具(苹果汁)
                            // 将任务设置为执行中
                            QuestClass.get().startQuest(pc,
                                    ElfLv45_2.QUEST.get_id());
                            // 这不是苹果汁吗...好吧，你想知道什么?
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "robinhood4"));

                        } else {
                            // 哼哼...我口好渴，这时候如果有苹果汁会更好
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "robinhood19"));
                        }
                    }
                    break;

                case 1:// 达到1
                    if (cmd.equals("B")) {// 我一定会拿回来的
                        // 提升任务进度
                        pc.getQuest().set_step(ElfLv45_2.QUEST.get_id(), 2);
                        // 取得任务道具
                        CreateNewItem.getQuestItem(pc, npc, 41348, 1);// 罗宾孙的推荐书
                        // 取得任务道具
                        CreateNewItem.getQuestItem(pc, npc, 41346, 1);// 罗宾孙的便条纸
                        // 你到伊娃的圣地去找神官 '知布烈'。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "robinhood13"));
                    }
                    break;

                case 2:// 达到2
                case 3:// 达到3
                case 4:// 达到4
                case 5:// 达到5
                    break;

                case 6:// 达到6
                    if (cmd.equals("C")) {// 拿出材料和便条纸
                        // 需要物件不足
                        if (CreateNewItem.checkNewItem(pc, new int[] { 41352,
                                40618, 40643, 40645, 40651, 40676, 40514,
                                41351, 41346 }, new int[] { 4, 30, 30, 30, 30,
                                30, 20, 1, 1 }) < 1) {
                            // 传回可交换道具数小于1(需要物件不足)

                            // 月光之气息 神圣独角兽之角 4个
                            if (CreateNewItem.checkNewItem(pc, new int[] {
                                    41352, 41351 }, new int[] { 4, 1 }) < 1) {
                                // 月光之气息</font>和 <font fg=ffffaf>神圣独角兽之角</font>
                                // 4个已经都准备好了吗？
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "robinhood15"));

                            } else {
                                // 你有带各 <font fg=ffffaf>元素的气息</font> 各30个？
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "robinhood14"));
                            }

                        } else {// 需要物件充足
                            // 收回任务需要物件 给予任务完成物件
                            CreateNewItem
                                    .createNewItem(pc,
                                    // 材料
                                            new int[] { 41352, 40618, 40643,
                                                    40645, 40651, 40676, 40514,
                                                    41351, 41346 }, new int[] {
                                                    4, 30, 30, 30, 30, 30, 20,
                                                    1, 1 },

                                            new int[] { 41347, 41350 }, // 罗宾孙的便条纸,罗宾孙之戒
                                            1, new int[] { 1, 1 });// 给予
                                                                   // 提升任务进度
                            pc.getQuest().set_step(ElfLv45_2.QUEST.get_id(), 7);
                            // 很好! 材料已经都有了，就让我来做做看吧。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "robinhood10"));
                        }
                    }
                    break;

                case 7:// 达到7
                    if (cmd.equals("E")) {// 将罗宾孙的戒指和材料交给他
                        // 需要物件不足
                        if (CreateNewItem.checkNewItem(pc, new int[] { 40491,
                                40495, 100, 40509, 40052, 40053, 40054, 40055,
                                41347, 41350 }, new int[] { 30, 40, 1, 12, 1,
                                1, 1, 1, 1, 1 }) < 1) {
                            // 传回可交换道具数小于1(需要物件不足)

                            // 高品质红宝石，高品质蓝宝石，高品质绿宝石，高品质钻石
                            if (CreateNewItem.checkNewItem(pc, new int[] {
                                    40052, 40053, 40054, 40055 }, new int[] {
                                    1, 1, 1, 1 }) < 1) {
                                // 最顶尖的弓当然也要稍微包装一下才好看阿!
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "robinhood16"));

                            } else {
                                // 糟糕糟糕...材料不够？就让我再说一次吧。
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "robinhood17"));
                            }

                        } else {// 需要物件充足
                            // 收回任务需要物件 给予任务完成物件
                            CreateNewItem.createNewItem(pc,
                            // 材料
                                    new int[] { 40491, 40495, 100, 40509,
                                            40052, 40053, 40054, 40055, 41347,
                                            41350 }, new int[] { 30, 40, 1, 12,
                                            1, 1, 1, 1, 1, 1 },

                                    new int[] { 205 }, // 炽炎天使弓
                                    1, new int[] { 1 });// 给予
                                                        // 将任务设置为结束
                            QuestClass.get().endQuest(pc,
                                    ElfLv45_2.QUEST.get_id());
                            // 完成了! 太棒了! 可不可以捏我一下？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "robinhood12"));
                        }
                    }
                    break;

                default:// 其他
                    isCloseList = true;
                    break;
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
