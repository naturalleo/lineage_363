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
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.types.Point;

/**
 * 安东<BR>
 * 70614
 * 
 * @author dexc
 * 
 */
public class Npc_Anton extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Anton.class);

    /**
	 *
	 */
    private Npc_Anton() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Anton();
    }

    @Override
    public int type() {
        return 19;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        // 请你不要妨碍我，我正在全力以赴的完成我一生中最伟大的作品。
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "anton1"));
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        if (cmd.equalsIgnoreCase("request ancient plate mail2")) {// 制作黑暗执行者金属盔甲
            // 可制作数量
            final long count = CreateNewItem.checkNewItem(pc, new int[] {
                    20095, 49015 }, // 古老的金属盔甲 x 1 黑色米索莉溶液 x 1
                    new int[] { 1, 1 });

            // 需要物件不足
            if (count < 1) {// 传回可交换道具数小于1(需要物件不足)
                isCloseList = true;

            } else {// 需要物件充足
                // 收回任务需要物件 给予任务完成物件
                CreateNewItem.createNewItem(pc, new int[] { 20095, 49015 },
                        new int[] { 1, 1 }, // 古老的金属盔甲 x 1 黑色米索莉溶液 x 1
                        new int[] { 20133 }, 1, new int[] { 1 });// 黑暗执行者金属盔甲 x
                                                                 // 1
                isCloseList = true;
            }
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    @Override
    public int workTime() {
        return 20;
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

        private Point[] _point = new Point[] { new Point(33451, 32741),
                new Point(33449, 32743), new Point(33448, 32742) // 雕刻
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
                    if (this._npc.getLocation().isSamePoint(this._point[2])) {
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
