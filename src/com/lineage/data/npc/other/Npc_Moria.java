package com.lineage.data.npc.other;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.cmd.NpcWorkMove;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.SprTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ItemCount;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.types.Point;

/**
 * 莫丽雅<BR>
 * 70598
 * 
 * @author dexc
 * 
 */
public class Npc_Moria extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Moria.class);

    /**
	 *
	 */
    private Npc_Moria() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Moria();
    }

    @Override
    public int type() {
        return 19;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 冶炼术具有无限之可能性，你想不想尝试一下未知的力量？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "moria4"));

            } else if (pc.isKnight()) {// 骑士
                // 冶炼术具有无限之可能性，你想不想尝试一下未知的力量？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "moria4"));

            } else if (pc.isElf()) {// 精灵
                // 冶炼术具有无限之可能性，你想不想尝试一下未知的力量？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "moria4"));

            } else if (pc.isWizard()) {// 法师
                // 冶炼术具有无限之可能性，你想不想尝试一下未知的力量？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "moria1"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 冶炼术具有无限之可能性，你想不想尝试一下未知的力量？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "moria4"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 冶炼术具有无限之可能性，你想不想尝试一下未知的力量？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "moria4"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 冶炼术具有无限之可能性，你想不想尝试一下未知的力量？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "moria4"));

            } else {
                // 冶炼术具有无限之可能性，你想不想尝试一下未知的力量？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "moria4"));
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
            if (cmd.equalsIgnoreCase("request magician dress")) {// 制作魔法师长袍
                int[] items = new int[] { 40054, 40318, 40457, 40455 };
                int[] counts = new int[] { 1, 25, 2, 4 };
                int[] gitems = new int[] { 20111 };
                int[] gcounts = new int[] { 1 };
                isCloseList = getItem(pc, items, counts, gitems, gcounts);

            } else if (cmd.equalsIgnoreCase("request magician cap")) {// 制作魔法师之帽
                int[] items = new int[] { 40051, 40318, 40457, 40456, 40455 };
                int[] counts = new int[] { 2, 20, 1, 1, 1 };
                int[] gitems = new int[] { 20012 };
                int[] gcounts = new int[] { 1 };
                isCloseList = getItem(pc, items, counts, gitems, gcounts);
            }
        }
        if (cmd.equalsIgnoreCase("request swap potion")) {// 制作黑色米索莉溶液
            int[] items = new int[] { 40443, 40397, 40398, 40399, 40400, 40016 };
            int[] counts = new int[] { 5, 1000, 1000, 1000, 1000, 100 };
            int[] gitems = new int[] { 49015 };
            int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount == 1) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, 1, gcounts);// 给予
                isCloseList = true;

            } else if (xcount > 1) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, "a1"));

            } else if (xcount < 1) {
                isCloseList = true;
            }

        } else if (cmd.equalsIgnoreCase("a1")) {// 制作黑色米索莉溶液
            int[] items = new int[] { 40443, 40397, 40398, 40399, 40400, 40016 };
            int[] counts = new int[] { 5, 1000, 1000, 1000, 1000, 100 };
            int[] gitems = new int[] { 49015 };
            int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount >= amount) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, amount, gcounts);// 给予
            }
            isCloseList = true;
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
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
            int[] gitems, int[] gcounts) {
        // 需要物件不足
        if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
            return true;

        } else {// 需要物件充足
            // 收回需要物件 给予完成物件
            CreateNewItem.createNewItem(pc, items, counts, // 需要
                    gitems, 1, gcounts);// 给予
            return true;
        }
    }

    @Override
    public int workTime() {
        return 18;
    }

    @Override
    public void work(final L1NpcInstance npc) {
        final Work work = new Work(npc);
        work.getStart();
    }

    private static Random _random = new Random();

    private class Work implements Runnable {

        private L1NpcInstance _npc;

        private int _spr;

        private NpcWorkMove _npcMove;

        private Point[] _point = new Point[] { new Point(33405, 32836),// 药剂实验
                new Point(33406, 32833), new Point(33404, 32834) };

        private Work(final L1NpcInstance npc) {
            this._npc = npc;
            this._spr = SprTable.get().getMoveSpeed(npc.getTempCharGfx(), 0);
            this._npcMove = new NpcWorkMove(npc);
        }

        /**
         * 启动线程
         */
        public void getStart() {
            GeneralThreadPool.get().schedule(this, 10);
        }

        @Override
        public void run() {
            try {
                Point point = null;
                final int t = _random.nextInt(this._point.length);
                if (!this._npc.getLocation().isSamePoint(this._point[t])) {
                    point = this._point[t];

                }

                boolean isWork = true;
                while (isWork) {
                    Thread.sleep(this._spr);

                    if (point != null) {
                        isWork = this._npcMove.actionStart(point);
                    } else {
                        isWork = false;
                    }
                    if (this._npc.getLocation().isSamePoint(this._point[0])) {
                        this._npc.setHeading(6);
                        this._npc.broadcastPacketX8(new S_ChangeHeading(
                                this._npc));
                        Thread.sleep(this._spr);
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc
                                .getId(), 17));
                    }
                }

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
