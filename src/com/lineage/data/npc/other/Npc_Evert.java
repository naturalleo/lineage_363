package com.lineage.data.npc.other;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.NpcWorkMove;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.SprTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ShopBuyList;
import com.lineage.server.serverpackets.S_ShopSellList;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.types.Point;

/**
 * 爱弗特<BR>
 * 70038
 * 
 * @author dexc
 * 
 */
public class Npc_Evert extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Evert.class);

    /**
	 *
	 */
    private Npc_Evert() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Evert();
    }

    @Override
    public int type() {
        return 19;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        if (pc.getLawful() < 0) {// 邪恶
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "evert2"));

        } else {// 一般
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "evert1"));
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        if (cmd.equalsIgnoreCase("buy")) {// 买
            // 出售物品列表
            pc.sendPackets(new S_ShopSellList(npc.getId()));

        } else if (cmd.equalsIgnoreCase("sell")) {// 卖
            // PC的可卖出物件列表
            pc.sendPackets(new S_ShopBuyList(npc.getId(), pc));
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

        private Point[] _point = new Point[] { new Point(33414, 32773),
                new Point(33417, 32773), new Point(33417, 32771) };

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
                        this._npc.setHeading(4);
                        this._npc.broadcastPacketX8(new S_ChangeHeading(
                                this._npc));
                        Thread.sleep(this._spr);
                        for (int i = 0; i < 3; i++) {
                            this._npc.broadcastPacketX8(new S_DoActionGFX(
                                    this._npc.getId(), 17));
                            Thread.sleep(this._spr);
                        }
                    }
                }

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
