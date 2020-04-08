package com.lineage.data.npc.other;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.SprTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_NPCPack;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.thread.GeneralThreadPool;

/**
 * 多玛<BR>
 * 70536
 * 
 * @author dexc
 * 
 */
public class Npc_Touma extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Touma.class);

    /**
	 *
	 */
    private Npc_Touma() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Touma();
    }

    @Override
    public int type() {
        return 17;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        // 怎么啦？
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "touma1"));
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        // 目前无任务
    }

    @Override
    public int workTime() {
        return 20;
    }

    @Override
    public void work(final L1NpcInstance npc) {
        if (npc.getStatus() != 4) {
            npc.setStatus(4);
            npc.broadcastPacketAll(new S_NPCPack(npc));
        }
        final Work work = new Work(npc);
        work.getStart();
    }

    private class Work implements Runnable {

        private L1NpcInstance _npc;

        private int _spr;

        private Work(final L1NpcInstance npc) {
            this._npc = npc;
            this._spr = SprTable.get().getMoveSpeed(npc.getTempCharGfx(), 0);
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
                for (int i = 0; i < 5; i++) {
                    this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc
                            .getId(), 30));
                    Thread.sleep(this._spr);
                }

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
