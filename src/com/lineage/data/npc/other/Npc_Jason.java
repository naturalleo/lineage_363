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
 * 杰森<BR>
 * 70040
 * 
 * @author dexc
 * 
 */
public class Npc_Jason extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Jason.class);

    /**
	 *
	 */
    private Npc_Jason() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Jason();
    }

    @Override
    public int type() {
        return 19;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        if (pc.getLawful() < 0) {// 邪恶
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jason2"));

        } else {// 一般
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jason1"));
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;

        if (cmd.equalsIgnoreCase("request timber")) {// 切割原木
            // 4根原木 = 1根木材
            final int[] items = new int[] { 42502 };// 原木
            final int[] counts = new int[] { 4 };
            final int[] gitems = new int[] { 42503 };
            final int[] gcounts = new int[] { 1 };
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

        } else if (cmd.equals("a1")) {// 切割原木
            final int[] items = new int[] { 42502 };// 原木
            final int[] counts = new int[] { 4 };
            final int[] gitems = new int[] { 42503 };
            final int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount >= amount) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, amount, gcounts);// 给予
            }
            isCloseList = true;

        } else if (cmd.equalsIgnoreCase("request blank box")) {// 制作木箱
            // 5根木材，及手工费500元
            final int[] items = new int[] { 42502, 40308 };// 原木
            final int[] counts = new int[] { 5, 500 };
            final int[] gitems = new int[] { 42504 };
            final int[] gcounts = new int[] { 1 };
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

        } else if (cmd.equals("a2")) {// 制作木箱
            // 5根木材，及手工费500元
            final int[] items = new int[] { 42502, 40308 };// 原木
            final int[] counts = new int[] { 5, 500 };
            final int[] gitems = new int[] { 42504 };
            final int[] gcounts = new int[] { 1 };
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

    @Override
    public int workTime() {
        return 25;
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

        private Point[] _point = new Point[] { new Point(33449, 32763),
                new Point(33449, 32762),// 砍材
                new Point(33450, 32764),// 锯木
                new Point(33452, 32765) };

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
                    if (this._npc.getLocation().isSamePoint(this._point[1])) {
                        this._npc.setHeading(6);
                        this._npc.broadcastPacketX8(new S_ChangeHeading(
                                this._npc));
                        Thread.sleep(this._spr);
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc
                                .getId(), 18));

                    } else if (this._npc.getLocation().isSamePoint(
                            this._point[2])) {
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
