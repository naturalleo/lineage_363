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
 * 海克特<BR>
 * 70642
 * 
 * @author dexc
 * 
 */
public class Npc_Hector extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Hector.class);

    /**
	 *
	 */
    private Npc_Hector() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Hector();
    }

    @Override
    public int type() {
        return 19;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        if (pc.getLawful() < 0) {// 邪恶
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "hector2"));

        } else {// 一般
            // 我可以将你的防具加强让它更加坚固。
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "hector1"));
            // 妈的！我也很难养活我自己．．到底叫我怎办啊？
            // pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "hector10"));
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;

        if (cmd.equalsIgnoreCase("request fifth goods of war")) {// 给予材料
            // FIXME

            // <p>要制造钢铁手套，需要手套1双和金属块150块。加工费是25000枚金币。
        } else if (cmd.equalsIgnoreCase("request iron gloves")) {// 制造钢铁手套
            int[] items = new int[] { 20182, 40408, 40308 };
            int[] counts = new int[] { 1, 150, 25000 };
            int[] gitems = new int[] { 20163 };
            int[] gcounts = new int[] { 1 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts);

            // <p>要制造钢铁头盔，需要骑士面甲1顶和金属块120块。加工费是16500枚金币。
        } else if (cmd.equalsIgnoreCase("request iron visor")) {// 制造钢铁头盔
            int[] items = new int[] { 20006, 40408, 40308 };
            int[] counts = new int[] { 1, 120, 16500 };
            int[] gitems = new int[] { 20003 };
            int[] gcounts = new int[] { 1 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts);

            // <p>要制造钢铁盾牌，需要塔盾1面和金属块200块。加工费是16000枚金币。
        } else if (cmd.equalsIgnoreCase("request iron shield")) {// 制造钢铁盾牌
            int[] items = new int[] { 20231, 40408, 40308 };
            int[] counts = new int[] { 1, 200, 16000 };
            int[] gitems = new int[] { 20220 };
            int[] gcounts = new int[] { 1 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts);

            // <p>要制造钢铁长靴，需要长靴1双与金属块160块。加工费是8000枚金币。
        } else if (cmd.equalsIgnoreCase("request iron boots")) {// 制造钢铁长靴
            int[] items = new int[] { 20205, 40408, 40308 };
            int[] counts = new int[] { 1, 160, 8000 };
            int[] gitems = new int[] { 20194 };
            int[] gcounts = new int[] { 1 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts);

            // <p>若要制造钢铁金属盔甲，需要金属盔甲1件和金属块450个。而且需要付30000枚金币的加工费。
        } else if (cmd.equalsIgnoreCase("request iron plate mail")) {// 制造钢铁金属盔甲
            int[] items = new int[] { 20154, 40408, 40308 };
            int[] counts = new int[] { 1, 450, 30000 };
            int[] gitems = new int[] { 20091 };
            int[] gcounts = new int[] { 1 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts);

            // 制作薄金属板需要金属块 10 个与 500 金币
        } else if (cmd.equalsIgnoreCase("request slim plate")) {// 制作薄金属板
            int[] items = new int[] { 40408, 40308 };
            int[] counts = new int[] { 10, 500 };
            int[] gitems = new int[] { 40526 };
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

        } else if (cmd.equalsIgnoreCase("a1")) {// 制作薄金属板
            int[] items = new int[] { 40408, 40308 };
            int[] counts = new int[] { 10, 500 };
            int[] gitems = new int[] { 40526 };
            int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount >= amount) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, amount, gcounts);// 给予
            }
            isCloseList = true;

            // 制作一个钢铁块，需要：钢铁原石 5 个、金属块 5 个、以及 500 金币。
        } else if (cmd.equalsIgnoreCase("request lump of steel")) {// 制作钢铁块
            int[] items = new int[] { 40899, 40408, 40308 };
            int[] counts = new int[] { 5, 5, 500 };
            int[] gitems = new int[] { 40779 };
            int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount == 1) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, 1, gcounts);// 给予
                isCloseList = true;

            } else if (xcount > 1) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, "a2"));

            } else if (xcount < 1) {
                isCloseList = true;
            }

        } else if (cmd.equalsIgnoreCase("a2")) {// 制作钢铁块
            int[] items = new int[] { 40899, 40408, 40308 };
            int[] counts = new int[] { 5, 5, 500 };
            int[] gitems = new int[] { 40779 };
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
        return 15;
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

        private Point[] _point = new Point[] { new Point(33471, 32775),
                new Point(33471, 32773),// 翻炉
                new Point(33468, 32774) // 打铁
        };

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
                        this._npc.setHeading(4);
                        this._npc.broadcastPacketX8(new S_ChangeHeading(
                                this._npc));
                        Thread.sleep(this._spr);
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc
                                .getId(), 7));

                    } else if (this._npc.getLocation().isSamePoint(
                            this._point[1])) {
                        this._npc.setHeading(4);
                        this._npc.broadcastPacketX8(new S_ChangeHeading(
                                this._npc));
                        Thread.sleep(this._spr);
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc
                                .getId(), 19));
                        Thread.sleep(this._spr);
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc
                                .getId(), 19));
                        Thread.sleep(this._spr);
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc
                                .getId(), 19));

                    } else if (this._npc.getLocation().isSamePoint(
                            this._point[2])) {
                        this._npc.setHeading(4);
                        this._npc.broadcastPacketX8(new S_ChangeHeading(
                                this._npc));
                        Thread.sleep(this._spr);
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc
                                .getId(), 17));
                        Thread.sleep(this._spr);
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc
                                .getId(), 18));
                    }
                }

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
