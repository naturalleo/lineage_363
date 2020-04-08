package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.Chapter00;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 尤丽娅<BR>
 * 91327<BR>
 * 说明:穿越时空的探险(秘谭)
 * 
 * @author dexc
 * 
 */
public class Npc_YouLiya extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_YouLiya.class);

    private Npc_YouLiya() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_YouLiya();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.getInventory().checkItem(49312)) {// 身上有时空之瓮
                // 天啊！！ 很高兴见到你！！！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_html00"));

            } else {
                // 天啊！！ 很高兴见到你！！！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_html01"));
            }

        } catch (final Exception e) {
            // 该讯息只有发生错误时才会显示。
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_html05"));
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        try {
            boolean isCloseList = false;

            if (cmd.equalsIgnoreCase("a")) {// “交出捐款和时空之玉。”
                int[] items = new int[] { 40308, 49313 };
                int[] counts = new int[] { 10000, 1 };

                // 需要物件不足
                if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                        counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
                    // 收取捐款10,000金币和时空之玉。请确认身上有没有。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_html02"));

                } else {
                    // 需要的物件确认
                    final L1ItemInstance item1 = pc.getInventory().checkItemX(
                            items[0], counts[0]);
                    boolean error = false;
                    if (item1 != null) {
                        pc.getInventory().removeItem(item1, counts[0]);// 删除道具
                    } else {
                        // 删除失败
                        error = true;
                    }
                    // 需要的物件确认
                    final L1ItemInstance item2 = pc.getInventory().checkItemX(
                            items[1], counts[1]);
                    if (item2 != null && !error) {
                        long remove = counts[1];
                        if (item2.getCount() >= 4) {
                            remove = item2.getCount() - 2;
                        }
                        pc.getInventory().removeItem(item2, remove);// 删除道具
                    } else {
                        // 删除失败
                        error = true;
                    }
                    if (!error) {
                        L1PolyMorph.undoPoly(pc);
                        L1Teleport.teleport(pc, 32747, 32861, (short) 9100, 5,
                                true);

                        // 将任务设置结束
                        QuestClass.get().endQuest(pc, Chapter00.QUEST.get_id());
                    }
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("d")) {// “复原哈汀的日记本。”
                int[] items = new int[] { 49301, 49302, 49303, 49304, 49305,
                        49306, 49307, 49308, 49309, 49310 };
                int[] counts = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
                int[] gitems = new int[] { 49311 };// 黑暗哈汀的日记本
                int[] gcounts = new int[] { 1 };

                // 需要物件不足
                if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                        counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
                    // 制作日记本所需的日记不足
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_html06"));

                } else {// 需要物件充足
                    // 收回需要物件 给予完成物件
                    CreateNewItem.createNewItem(pc, items, counts, // 需要
                            gitems, 1, gcounts);// 给予
                    // 日记复原已完成
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_html04"));
                }

            } else if (cmd.equalsIgnoreCase("c")) {// “收下时空之瓮。”
                if (!pc.getInventory().checkItem(49312)) {// 身上没有时空之瓮
                    // 给予任务道具(时空之瓮 49312)
                    CreateNewItem.createNewItem(pc, 49312, 1);

                    // 将任务设置为启动
                    QuestClass.get().startQuest(pc, Chapter00.QUEST.get_id());
                    isCloseList = true;

                } else {
                    // 已经给你时空之瓮了。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_html03"));
                }
            } else if (cmd.equalsIgnoreCase("e")) {// “复原欧林的日记本。”
                int[] items = new int[] { 50001, 50002, 50003, 50004, 50005,
                        50006, 50007, 50008, 50009, 50010, 50011, 50012, 50013,
                        50014, 50015, 50016, 50017, 50018 };
                int[] counts = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
                		1, 1, 1, 1, 1};
                int[] gitems = new int[] { 50019 };// 欧林的日记本
                int[] gcounts = new int[] { 1 };

                // 需要物件不足
                if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                        counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
                    // 制作日记本所需的日记不足
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_html06"));

                } else {// 需要物件充足
                    // 收回需要物件 给予完成物件
                    CreateNewItem.createNewItem(pc, items, counts, // 需要
                            gitems, 1, gcounts);// 给予
                    // 日记复原已完成
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_html04"));
                }
            }

            if (isCloseList) {
                // 关闭对话窗
                pc.sendPackets(new S_CloseList(pc.getId()));
            }

        } catch (final Exception e) {
            // 该讯息只有发生错误时才会显示。
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_html05"));
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
