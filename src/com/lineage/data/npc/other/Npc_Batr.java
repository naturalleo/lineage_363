package com.lineage.data.npc.other;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 铁匠^巴特尔<BR>
 * 85025<BR>
 * 
 * @author dexc
 * 
 */
public class Npc_Batr extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Batr.class);

    private Npc_Batr() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Batr();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            // 欢迎光临～请问有什么事呢？
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "batr1"));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;

        if (cmd.equalsIgnoreCase("request sapphire kiringku")) {// 提供希莲恩的推荐书与特别的原石/兑换蓝宝石奇古兽
            /*
             * 高品质蓝宝石3个 特别的原石 49205 希莲恩的推荐书 49181
             */
            int[] items = new int[] { 40054, 49205, 49181 };
            int[] counts = new int[] { 3, 1, 1 };
            int[] gitems = new int[] { 270 };
            int[] gcounts = new int[] { 1 };
            // 需要物件不足
            if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                    counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
                // 高品质蓝宝石</font>3个
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "batr4"));

            } else {// 需要物件充足
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, 1, gcounts);// 给予蓝宝石奇古兽 270
                isCloseList = true;
            }

        } else if (cmd.equalsIgnoreCase("request obsidian kiringku")) {// 制作黑曜石奇古兽
            /*
             * 高品质钻石10个 高品质红宝石10个 高品质蓝宝石10个 高品质绿宝石10个 龟裂之核2个 原石碎片30个 精灵粉末30个
             * 金币1000000个
             */
            int[] items = new int[] { 40052, 40053, 40054, 40055, 40520, 49092,
                    40308 };
            int[] counts = new int[] { 10, 10, 10, 10, 30, 2, 1000000 };
            int[] gitems = new int[] { 271 };
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

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
