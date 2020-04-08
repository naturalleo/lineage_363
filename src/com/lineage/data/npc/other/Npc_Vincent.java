package com.lineage.data.npc.other;

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
 * 文森<BR>
 * 70034
 * 
 * @author dexc
 * 
 */
public class Npc_Vincent extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Vincent.class);

    /**
	 *
	 */
    private Npc_Vincent() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Vincent();
    }

    @Override
    public int type() {
        return 19;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        if (pc.getLawful() < 0) {// 邪恶
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "vincent2"));

        } else {// 一般
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "vincent1"));
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        long xcount = -1;

        if (cmd.equalsIgnoreCase("sell")) {// 贩卖皮革
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ladar2"));

        } else if (cmd.equalsIgnoreCase("request adena2")) {// 贩卖皮革
            int[] items = new int[] { 40405 };
            int[] counts = new int[] { 1 };
            xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount > 0) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, "A1"));

            } else {
                isCloseList = true;
            }

        } else if (cmd.equalsIgnoreCase("A1")) {// 贩卖皮革
            int[] items = new int[] { 40405 };
            int[] counts = new int[] { 1 };
            int[] gitems = new int[] { 40308 };
            int[] gcounts = new int[] { 2 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts, amount);

        } else if (cmd.equalsIgnoreCase("request adena30")) {// 贩卖高级皮革
            int[] items = new int[] { 40406 };
            int[] counts = new int[] { 1 };
            xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount > 0) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, "A2"));

            } else {
                isCloseList = true;
            }

        } else if (cmd.equalsIgnoreCase("A2")) {// 贩卖高级皮革
            int[] items = new int[] { 40406 };
            int[] counts = new int[] { 1 };
            int[] gitems = new int[] { 40308 };
            int[] gcounts = new int[] { 30 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts, amount);

            // 以20张皮革可精制出高级皮革
        } else if (cmd.equalsIgnoreCase("request hard leather")) {// 制造高级皮革
            int[] items = new int[] { 40405 };
            int[] counts = new int[] { 20 };
            xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount > 0) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, "A3"));

            } else {
                isCloseList = true;
            }

        } else if (cmd.equalsIgnoreCase("A3")) {// 制造高级皮革
            int[] items = new int[] { 40405 };
            int[] counts = new int[] { 20 };
            int[] gitems = new int[] { 40406 };
            int[] gcounts = new int[] { 1 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts, amount);

            // 以5张皮革、1块金属块可制造皮帽子。
        } else if (cmd.equalsIgnoreCase("request leather cap")) {// 制造皮帽子
            int[] items = new int[] { 40405, 40408 };
            int[] counts = new int[] { 5, 1 };
            int[] gitems = new int[] { 20001 };
            int[] gcounts = new int[] { 1 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts, 1);

            // 以6张皮革、2个金属块可制造皮凉鞋。
        } else if (cmd.equalsIgnoreCase("request leather sandal")) {// 制造皮凉鞋
            int[] items = new int[] { 40405, 40408 };
            int[] counts = new int[] { 6, 2 };
            int[] gitems = new int[] { 20193 };
            int[] gcounts = new int[] { 1 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts, 1);

            // 以10张皮革可制造出皮背心。
        } else if (cmd.equalsIgnoreCase("request leather vest")) {// 制造皮背心
            int[] items = new int[] { 40405 };
            int[] counts = new int[] { 10 };
            int[] gitems = new int[] { 20090 };
            int[] gcounts = new int[] { 1 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts, 1);

            // 以7张皮革就可制造皮盾牌。
        } else if (cmd.equalsIgnoreCase("request leather shield")) {// 制造皮盾牌
            int[] items = new int[] { 40405 };
            int[] counts = new int[] { 7 };
            int[] gitems = new int[] { 20219 };
            int[] gcounts = new int[] { 1 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts, 1);

            // 以1双银钉皮凉鞋，10张高级皮革及10个金属块可制造皮长靴。但需要给我手工费300块金币。
        } else if (cmd.equalsIgnoreCase("request leather boots")) {// 制造皮长靴
            int[] items = new int[] { 20212, 40406, 40408, 40308 };
            int[] counts = new int[] { 1, 10, 10, 300 };
            int[] gitems = new int[] { 20192 };
            int[] gcounts = new int[] { 1 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts, 1);

            // 以1顶钢盔、1顶皮帽子、5张高级皮革和5块金属块可制造出皮头盔。
        } else if (cmd.equalsIgnoreCase("request leather helmet")) {// 制造皮头盔
            int[] items = new int[] { 20043, 20001, 40406, 40408 };
            int[] counts = new int[] { 1, 1, 5, 5 };
            int[] gitems = new int[] { 20002 };
            int[] gcounts = new int[] { 1 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts, 1);

            // 若想制作硬皮背心，需要给我1件银钉皮背心、15张高级皮革、15块金属块。
        } else if (cmd.equalsIgnoreCase("request hard leather vest")) {// 制作硬皮背心
            int[] items = new int[] { 20148, 40406, 40408 };
            int[] counts = new int[] { 1, 15, 15 };
            int[] gitems = new int[] { 20145 };
            int[] gcounts = new int[] { 1 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts, 1);

            // 以皮背心和皮带可制造出皮盔甲。
        } else if (cmd.equalsIgnoreCase("request leather vest with belt")) {// 制造皮盔甲
            int[] items = new int[] { 20090, 40778 };
            int[] counts = new int[] { 1, 1 };
            int[] gitems = new int[] { 20120 };
            int[] gcounts = new int[] { 1 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts, 1);

            // 以5张高级皮革和2块金属块可制造皮带。
        } else if (cmd.equalsIgnoreCase("request belt")) {// 制造皮带
            int[] items = new int[] { 40406, 40408 };
            int[] counts = new int[] { 5, 2 };
            xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount > 0) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, "A4"));

            } else {
                isCloseList = true;
            }

        } else if (cmd.equalsIgnoreCase("A4")) {// 制造皮带
            int[] items = new int[] { 40406, 40408 };
            int[] counts = new int[] { 5, 2 };
            int[] gitems = new int[] { 40778 };
            int[] gcounts = new int[] { 1 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts, amount);
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
            int[] gitems, int[] gcounts, final long amount) {
        try {
            // 需要物件不足
            if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                    counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
                return true;

            } else {// 需要物件充足
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, amount, gcounts);// 给予
                return true;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return true;
    }

    @Override
    public int workTime() {
        return 35;
    }

    @Override
    public void work(final L1NpcInstance npc) {
        final Work work = new Work(npc);
        work.getStart();
    }

    private class Work implements Runnable {

        private L1NpcInstance _npc;

        private int _spr;

        private NpcWorkMove _npcMove;

        private Point[] _point = new Point[] { new Point(33480, 32777),
                new Point(33476, 32777) };

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
                // 前往工作点
                if (!this._npc.getLocation().isSamePoint(this._point[1])) {
                    point = this._point[1];
                }

                boolean isWork1 = true;
                while (isWork1) {
                    Thread.sleep(this._spr);

                    if (point != null) {
                        isWork1 = this._npcMove.actionStart(point);
                    } else {
                        isWork1 = false;
                    }
                }
                this._npc.setHeading(6);
                this._npc.broadcastPacketX8(new S_ChangeHeading(this._npc));

                // 执行工作
                this._npc.broadcastPacketX8(new S_DoActionGFX(
                        this._npc.getId(), 7));
                Thread.sleep(2000);

                // 返回
                if (!this._npc.getLocation().isSamePoint(this._point[0])) {
                    point = this._point[0];
                }

                boolean isWork2 = true;
                while (isWork2) {
                    Thread.sleep(this._spr);

                    if (point != null) {
                        isWork2 = this._npcMove.actionStart(point);
                    } else {
                        isWork2 = false;
                    }
                }
                this._npc.setHeading(4);
                this._npc.broadcastPacketX8(new S_ChangeHeading(this._npc));

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
