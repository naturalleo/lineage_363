package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.NpcWorkMove;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.SprTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.types.Point;

/**
 * 任务管理员<BR>
 * 85014
 * 
 * @author dexc
 * 
 */
public class Npc_Quest extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Quest.class);

    private Npc_Quest() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Quest();
    }

    @Override
    public int type() {
        return 19;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            pc.isWindows();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        pc.getAction().action(cmd, 0);
    }

    @Override
    public int workTime() {
        return 120;
    }

    @Override
    public void work(final L1NpcInstance npc) {
        final Work work = new Work(npc);
        work.getStart();
    }

    private class Work implements Runnable {

        private final L1NpcInstance _npc;

        private final int _spr;

        private final NpcWorkMove _npcMove;

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
                final Point home = new Point(_npc.getX(), _npc.getY());
                final Point tgloc = new Point(_npc.getX() + 5, _npc.getY() + 5);
                boolean isMove = true;
                while (isMove) {
                    Thread.sleep(this._spr);

                    if (tgloc != null) {
                        isMove = this._npcMove.actionStart(tgloc);
                    }
                    if (this._npc.getLocation().isSamePoint(tgloc)) {
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc
                                .getId(), 1));
                        Thread.sleep(1500);
                    }
                }

                isMove = true;
                while (isMove) {
                    Thread.sleep(this._spr);

                    if (home != null) {
                        isMove = this._npcMove.actionStart(home);
                    }
                    if (this._npc.getLocation().isSamePoint(home)) {
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
