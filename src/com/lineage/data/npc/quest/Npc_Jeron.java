package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ALv45_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 骑士团长 帝伦<BR>
 * 71199<BR>
 * 说明:毒蛇之牙的名号(全职业45级任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Jeron extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Jeron.class);

    private Npc_Jeron() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Jeron();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            // 任务已经完成
            if (pc.getQuest().isEnd(ALv45_1.QUEST.get_id())) {
                // 佣兵队长多文介绍你来的？我要怎么相信你呢?
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jeron10"));

            } else {
                // 等级达成要求
                if (pc.getLevel() >= ALv45_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(ALv45_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                        case 1:
                            // 佣兵队长多文介绍你来的？我要怎么相信你呢?
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "jeron10"));
                            break;

                        case 2:// 达到2
                               // 你来找我干么?
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "jeron1"));
                            break;

                        case 3:// 达到3
                               // 我不久前才把书给你了吧。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "jeron7"));
                            break;

                        default:// 达到2以上
                            // 佣兵队长多文介绍你来的？我要怎么相信你呢?
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "jeron10"));
                            break;
                    }

                } else {
                    // 佣兵队长多文介绍你来的？我要怎么相信你呢?
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jeron10"));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        int[] items = null;
        int[] counts = null;
        int[] gitems = null;
        int[] gcounts = null;

        if (cmd.equalsIgnoreCase("A")) {// 我是佣兵团长多文介绍过来的。
            items = new int[] { 41340 };// 佣兵团长多文的推荐书 41340
            counts = new int[] { 1 };
            // 需要物件不足
            if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                    counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
                // 佣兵队长多文介绍你来的？我要怎么相信你呢?
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jeron10"));
                return;
            }
            // 请告诉我关于毒蛇之牙的事情
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jeron2"));

        } else if (cmd.equalsIgnoreCase("B")) {// 花100万金币买下这本书
            items = new int[] { 40308 };
            counts = new int[] { 1000000 };
            // 需要物件不足
            if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                    counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
                // 你没有百万金币...如果没有钱，可以听我一个要求吗
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jeron8"));
                return;
            }

            gitems = new int[] { 41341 };// 帝伦的教本
            gcounts = new int[] { 1 };

            final L1ItemInstance item = pc.getInventory().checkItemX(41340, 1);// 佣兵团长多文的推荐书
                                                                               // 41340
            if (item != null) {
                pc.getInventory().removeItem(item, 1);
            }
            // 收回需要物件 给予完成物件
            CreateNewItem.createNewItem(pc, items, counts, // 需要
                    gitems, 1, gcounts);// 给予

            // 将任务进度提升为3
            pc.getQuest().set_step(ALv45_1.QUEST.get_id(), 3);
            // 哈哈哈哈。这是你的了。算你厉害。不错。
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jeron6"));

        } else if (cmd.equalsIgnoreCase("C")) {// 拿出梅杜莎之血。
            items = new int[] { 41342 };// 梅杜莎之血 x 1 41342
            counts = new int[] { 1 };
            // 需要物件不足
            if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                    counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
                // 你可以梅杜莎之血来换。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jeron9"));
                return;
            }

            gitems = new int[] { 41341 };// 帝伦的教本
            gcounts = new int[] { 1 };

            final L1ItemInstance item = pc.getInventory().checkItemX(41340, 1);// 佣兵团长多文的推荐书
                                                                               // 41340
            if (item != null) {
                pc.getInventory().removeItem(item, 1);
            }

            // 收回需要物件 给予完成物件
            CreateNewItem.createNewItem(pc, items, counts, // 需要
                    gitems, 1, gcounts);// 给予

            // 将任务进度提升为3
            pc.getQuest().set_step(ALv45_1.QUEST.get_id(), 3);
            // 嗯~ 太棒了。
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jeron5"));

        } else {
            isCloseList = true;
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
