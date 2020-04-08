package com.lineage.data.npc.other;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv50_2;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_ItemCount;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 卡立普<BR>
 * 70762<BR>
 * 说明:暗黑的武器，死神之证 (黑暗妖精50级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Karif extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Karif.class);

    /**
	 *
	 */
    private Npc_Karif() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Karif();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 你怎么知道我在这里？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif9"));

            } else if (pc.isKnight()) {// 骑士
                // 你怎么知道我在这里？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif9"));

            } else if (pc.isElf()) {// 精灵
                // 你怎么知道我在这里？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif9"));

            } else if (pc.isWizard()) {// 法师
                // 你怎么知道我在这里？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif9"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // LV50-2任务已经完成
                if (pc.getQuest().isEnd(DarkElfLv50_2.QUEST.get_id())) {
                    // 你怎么知道我在这里？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif9"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= DarkElfLv50_2.QUEST.get_questlevel()) {
                    // 嗯～你收集了足够的材料，你想制作什么呢！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif3a"));

                } else {
                    // 你怎么知道我在这里？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif9"));
                }

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 你怎么知道我在这里？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif9"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 你怎么知道我在这里？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif9"));

            } else {
                // 你怎么知道我在这里？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif9"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;

        if (pc.isDarkelf()) {// 黑暗精灵
            if (cmd.equalsIgnoreCase("quest 32 karif4")) {// 请帮忙制作暗黑双刀
                isCloseList = getItem1(pc);

            } else if (cmd.equalsIgnoreCase("quest 32 karif5")) {// 请帮忙制作暗黑钢爪
                isCloseList = getItem2(pc);

            } else if (cmd.equalsIgnoreCase("quest 32 karif6")) {// 请帮忙制作暗黑十字弓
                isCloseList = getItem3(pc);

            } else if (cmd.equalsIgnoreCase("request darkness dualblade")) {// 制作暗黑双刀
                isCloseList = getItem1(pc);

            } else if (cmd.equalsIgnoreCase("request darkness claw")) {// 制作暗黑钢爪
                isCloseList = getItem2(pc);

            } else if (cmd.equalsIgnoreCase("request darkness crossbow")) {// 制作暗黑十字弓
                isCloseList = getItem3(pc);
            }
        }
        if (isCloseList) {
            // 你怎么知道我在这里？
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "karif9"));
            return;
        }
        // 一般交换
        if (cmd.equalsIgnoreCase("request karif bag1")) {// 将钻石送给他
            final int[] items = new int[] { 40044 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 49005 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = requestKarifBag(pc, npc, items, counts, gitems,
                    gcounts, "a1");

        } else if (cmd.equalsIgnoreCase("a1")) {
            final int[] items = new int[] { 40044 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 49005 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = getKarifBag(pc, items, counts, gitems, gcounts,
                    amount);

        } else if (cmd.equalsIgnoreCase("request karif bag2")) {// 将绿宝石送给他
            final int[] items = new int[] { 40047 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 49008 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = requestKarifBag(pc, npc, items, counts, gitems,
                    gcounts, "a2");

        } else if (cmd.equalsIgnoreCase("a2")) {
            final int[] items = new int[] { 40047 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 49008 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = getKarifBag(pc, items, counts, gitems, gcounts,
                    amount);

        } else if (cmd.equalsIgnoreCase("request karif bag3")) {// 将红宝石送给他
            final int[] items = new int[] { 40045 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 49006 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = requestKarifBag(pc, npc, items, counts, gitems,
                    gcounts, "a3");

        } else if (cmd.equalsIgnoreCase("a3")) {
            final int[] items = new int[] { 40045 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 49006 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = getKarifBag(pc, items, counts, gitems, gcounts,
                    amount);

        } else if (cmd.equalsIgnoreCase("request karif bag4")) {// 将蓝宝石送给他
            final int[] items = new int[] { 40046 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 49007 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = requestKarifBag(pc, npc, items, counts, gitems,
                    gcounts, "a4");

        } else if (cmd.equalsIgnoreCase("a4")) {
            final int[] items = new int[] { 40046 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 49007 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = getKarifBag(pc, items, counts, gitems, gcounts,
                    amount);

        } else if (cmd.equalsIgnoreCase("request karif bag5")) {// 将品质钻石送给他
            final int[] items = new int[] { 40048 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 49009 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = requestKarifBag(pc, npc, items, counts, gitems,
                    gcounts, "a5");

        } else if (cmd.equalsIgnoreCase("a5")) {
            final int[] items = new int[] { 40048 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 49009 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = getKarifBag(pc, items, counts, gitems, gcounts,
                    amount);

        } else if (cmd.equalsIgnoreCase("request karif bag6")) {// 将品质绿宝石送给他
            final int[] items = new int[] { 40051 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 49010 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = requestKarifBag(pc, npc, items, counts, gitems,
                    gcounts, "a6");

        } else if (cmd.equalsIgnoreCase("a6")) {
            final int[] items = new int[] { 40051 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 49010 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = getKarifBag(pc, items, counts, gitems, gcounts,
                    amount);

        } else if (cmd.equalsIgnoreCase("request karif bag7")) {// 将品质红宝石送给他
            final int[] items = new int[] { 40049 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 49011 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = requestKarifBag(pc, npc, items, counts, gitems,
                    gcounts, "a7");

        } else if (cmd.equalsIgnoreCase("a7")) {
            final int[] items = new int[] { 40049 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 49011 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = getKarifBag(pc, items, counts, gitems, gcounts,
                    amount);

        } else if (cmd.equalsIgnoreCase("request karif bag8")) {// 将品质蓝宝石送给他
            final int[] items = new int[] { 40050 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 49012 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = requestKarifBag(pc, npc, items, counts, gitems,
                    gcounts, "a8");

        } else if (cmd.equalsIgnoreCase("a8")) {
            final int[] items = new int[] { 40050 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 49012 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = getKarifBag(pc, items, counts, gitems, gcounts,
                    amount);
        } else if (cmd.equalsIgnoreCase("request karif bag9")) {// 将高品质钻石送给他
            final int[] items = new int[] { 40052 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 54001 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = requestKarifBag(pc, npc, items, counts, gitems,
                    gcounts, "a9");

        } else if (cmd.equalsIgnoreCase("a9")) {
            final int[] items = new int[] { 40052 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 54001 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = getKarifBag(pc, items, counts, gitems, gcounts,
                    amount);

        } else if (cmd.equalsIgnoreCase("request karif bag10")) {// 将高品质绿宝石送给他
            final int[] items = new int[] { 40055 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 54002 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = requestKarifBag(pc, npc, items, counts, gitems,
                    gcounts, "a10");

        } else if (cmd.equalsIgnoreCase("a10")) {
            final int[] items = new int[] { 40055 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 54002 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = getKarifBag(pc, items, counts, gitems, gcounts,
                    amount);

        } else if (cmd.equalsIgnoreCase("request karif bag11")) {// 将高品质红宝石送给他
            final int[] items = new int[] { 40053 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 54003 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = requestKarifBag(pc, npc, items, counts, gitems,
                    gcounts, "a11");

        } else if (cmd.equalsIgnoreCase("a11")) {
            final int[] items = new int[] { 40053 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 54003 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = getKarifBag(pc, items, counts, gitems, gcounts,
                    amount);

        } else if (cmd.equalsIgnoreCase("request karif bag12")) {// 将高品质蓝宝石送给他
            final int[] items = new int[] { 40054 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 54004 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = requestKarifBag(pc, npc, items, counts, gitems,
                    gcounts, "a12");

        } else if (cmd.equalsIgnoreCase("a12")) {
            final int[] items = new int[] { 40054 };
            final int[] counts = new int[] { 1 };
            final int[] gitems = new int[] { 54004 };
            final int[] gcounts = new int[] { 1 };
            isCloseList = getKarifBag(pc, items, counts, gitems, gcounts,
                    amount);
        }
        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    /**
     * 制作暗黑十字弓
     * 
     * @param pc
     * @return
     */
    private boolean getItem3(L1PcInstance pc) {
        // LV50-2任务已经完成
        if (pc.getQuest().isEnd(DarkElfLv50_2.QUEST.get_id())) {
            return true;
        }
        // 等级未达成要求
        if (pc.getLevel() < DarkElfLv50_2.QUEST.get_questlevel()) {
            return true;
        }
        final int[] items = new int[] { 40466, 40054, 40308, 40403, 40413,
                40525, 177 };
        final int[] counts = new int[] { 1, 3, 100000, 10, 9, 3, 1 };
        final int[] gitems = new int[] { 189 };
        final int[] gcounts = new int[] { 1 };

        return getItem(pc, items, counts, gitems, gcounts, 1);
    }

    /**
     * 暗黑钢爪
     * 
     * @param pc
     * @return
     */
    private boolean getItem2(L1PcInstance pc) {
        // LV50-2任务已经完成
        if (pc.getQuest().isEnd(DarkElfLv50_2.QUEST.get_id())) {
            return true;
        }
        // 等级未达成要求
        if (pc.getLevel() < DarkElfLv50_2.QUEST.get_questlevel()) {
            return true;
        }
        final int[] items = new int[] { 40466, 40055, 40308, 40404, 40413,
                40525, 162 };
        final int[] counts = new int[] { 1, 3, 100000, 10, 9, 3, 1 };
        final int[] gitems = new int[] { 164 };
        final int[] gcounts = new int[] { 1 };

        return getItem(pc, items, counts, gitems, gcounts, 1);
    }

    /**
     * 制作暗黑双刀
     * 
     * @param pc
     * @return
     */
    private boolean getItem1(L1PcInstance pc) {
        // LV50-2任务已经完成
        if (pc.getQuest().isEnd(DarkElfLv50_2.QUEST.get_id())) {
            return true;
        }
        // 等级未达成要求
        if (pc.getLevel() < DarkElfLv50_2.QUEST.get_questlevel()) {
            return true;
        }
        final int[] items = new int[] { 40466, 40053, 40308, 40402, 40413,
                40525, 81 };
        final int[] counts = new int[] { 1, 3, 100000, 10, 9, 3, 1 };
        final int[] gitems = new int[] { 84 };
        final int[] gcounts = new int[] { 1 };

        return getItem(pc, items, counts, gitems, gcounts, 1);
    }

    /**
     * 交换道具
     * 
     * @param pc
     * @param items
     * @param counts
     * 
     * @return 是否关闭现有视窗
     */
    private boolean getItem(final L1PcInstance pc, int[] items, int[] counts,
            int[] gitems, int[] gcounts, final long amount) {
        // 需要物件不足
        if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
            return true;

        } else {// 需要物件充足
            // 收回需要物件 给予完成物件
            CreateNewItem.createNewItem(pc, items, counts, // 需要
                    gitems, amount, gcounts);// 给予
            // 将任务设置为执行中
            QuestClass.get().startQuest(pc, DarkElfLv50_2.QUEST.get_id());
            // 将任务设置为结束
            QuestClass.get().endQuest(pc, DarkElfLv50_2.QUEST.get_id());
            return true;
        }
    }

    /**
     * 交换宝石
     * 
     * @param pc
     * @param items
     * @param counts
     * @param gitems
     * @param gcounts
     * @param amount
     * @return
     */
    private boolean getKarifBag(final L1PcInstance pc, final int[] items,
            final int[] counts, final int[] gitems, final int[] gcounts,
            long amount) {
        // 可制作数量
        long xcount = CreateNewItem.checkNewItem(pc, items, counts);
        if (xcount >= amount) {
            // 收回需要物件 给予完成物件
            CreateNewItem.createNewItem(pc, items, counts, // 需要
                    gitems, amount, gcounts);// 给予
        }
        return true;
    }

    /**
     * 交换宝石
     * 
     * @param pc
     * @param npc
     * @param items
     * @param counts
     * @param gitems
     * @param gcounts
     * @param string
     * @return
     */
    private boolean requestKarifBag(final L1PcInstance pc,
            final L1NpcInstance npc, final int[] items, final int[] counts,
            final int[] gitems, final int[] gcounts, final String string) {
        boolean isCloseList = false;
        // 可制作数量
        long xcount = CreateNewItem.checkNewItem(pc, items, counts);
        if (xcount == 1) {
            // 收回需要物件 给予完成物件
            CreateNewItem.createNewItem(pc, items, counts, // 需要
                    gitems, 1, gcounts);// 给予
            isCloseList = true;

        } else if (xcount > 1) {
            pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, string));

        } else if (xcount < 1) {
            isCloseList = true;
        }
        return isCloseList;
    }
}
