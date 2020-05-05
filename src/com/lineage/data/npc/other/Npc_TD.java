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
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.types.Point;

/**
 * td怪物默认目标设定<BR>
 * 70839<BR>
 * 
 * @author dexc
 * 
 */
public class Npc_TD extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_TD.class);

    private static Work _work = null;

    /**
	 *
	 */
    private Npc_TD() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_TD();
    }

    @Override
    public int type() {
        return 16;
    }
    @Override
    public int workTime() {
        return 5;
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

        private final int[]  _targetPos = new int[]{33504, 32734};


        boolean _isStop = false;

        /**
         * 中断动作
         */
        private void stopMove() {
            _isStop = true;
        }

        private Work(final L1NpcInstance npc) {
            this._npc = npc;
            this._spr = SprTable.get().getMoveSpeed(npc.getTempCharGfx(), 0);
            this._npcMove = new NpcWorkMove(npc);
        }

        /**
         * 启动线程
         */
        public void getStart() {
            _work = this;
            GeneralThreadPool.get().schedule(this, 10);
        }

        @Override
        public void run() {
            try {
                final Point tgloc = new Point(_targetPos[0], _targetPos[1]);
                boolean isMove = true;
                while (isMove) {
                    Thread.sleep(this._spr);
                    if (_isStop) {
                        break;
                    }
                    if (tgloc != null) {
                        isMove = this._npcMove.actionStart(tgloc);
                    }
                    if (this._npc.getLocation().isSamePoint(tgloc)) {
                        isMove = false;
                    }
                }

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);

            } finally {
                _work = null;
            }
        }
    }
}
