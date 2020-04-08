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
 * 多特<BR>
 * 70839<BR>
 * 
 * @author dexc
 * 
 */
public class Npc_Doett extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Doett.class);

    private static Work _work = null;

    /**
	 *
	 */
    private Npc_Doett() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Doett();
    }

    @Override
    public int type() {
        return 17;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            // 改变面向
            npc.setHeading(npc.targetDirection(pc.getX(), pc.getY()));
            npc.broadcastPacketAll(new S_ChangeHeading(npc));
            if (_work != null) {
                _work.stopMove();
            }

            if (pc.isCrown()) {// 王族
                // 将世界推向末日的主角怎么会来到这里...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "doettM1"));

            } else if (pc.isKnight()) {// 骑士
                // 将世界推向末日的主角怎么会来到这里...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "doettM1"));

            } else if (pc.isElf()) {// 精灵
                if (pc.getLawful() < 0) {// 邪恶
                    // 你...！你这被抛弃到黑暗之火中的黑暗妖精！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "doettec1"));

                } else {
                    // 虽然在战争中，但<a link="doettE2">米索莉</a>的光芒能永远在森林与湖泊上闪亮...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "doette1"));
                }

            } else if (pc.isWizard()) {// 法师
                // 将世界推向末日的主角怎么会来到这里...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "doettM1"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 身为黑暗种族的你为何来到圣地！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "doettM2"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 你是流着龙族血液的异地人？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "doettM3"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 你是古代英雄吗？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "doettM4"));

            } else {
                // 你是古代英雄吗？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "doettM4"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public int workTime() {
        return 30;
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

        private final Random _random = new Random();

        private final int[][] _loc = new int[][] { { 33066, 32311 },
                { 33065, 32319 }, { 33056, 32326 }, { 33044, 32323 },
                { 33051, 32314 }, };

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
                final int[] loc = _loc[_random.nextInt(_loc.length)];
                final Point tgloc = new Point(loc[0], loc[1]);
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
