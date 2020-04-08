package com.lineage.data.npc.other;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.KnightLv15_1;
import com.lineage.server.datatables.SprTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_NPCPack;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.thread.GeneralThreadPool;

/**
 * 亚南<BR>
 * 70802<BR>
 * 瑞奇的抵抗 (骑士15级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Aanon extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Aanon.class);

    /**
	 *
	 */
    private Npc_Aanon() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Aanon();
    }

    @Override
    public int type() {
        return 19;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 欢迎光临，这次又是什么东西坏掉啦？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aanon8"));

            } else if (pc.isKnight()) {// 骑士
                // 任务已经完成
                if (pc.getQuest().isEnd(KnightLv15_1.QUEST.get_id())) {
                    // 唉呦！欢迎欢迎，这次又是什么装备坏了啊？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aanon8"));
                    // 如果你要修理红骑士武器的话，随时欢迎你。(包含印记任务)
                    // pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                    // "aanoncg"));

                } else {
                    // 等级达成要求
                    if (pc.getLevel() >= KnightLv15_1.QUEST.get_questlevel()) {

                        if (pc.getQuest().get_step(KnightLv15_1.QUEST.get_id()) == 2) {
                            // 有关古老的交易文件
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "aanon4"));

                        } else {
                            // 唉呦！欢迎欢迎，这次又是什么装备坏了啊？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "aanon8"));
                        }

                    } else {
                        // 唉呦！欢迎欢迎，这次又是什么装备坏了啊？
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "aanon8"));
                    }
                }

            } else if (pc.isElf()) {// 精灵
                // 欢迎光临，这次又是什么东西坏掉啦？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aanon8"));

            } else if (pc.isWizard()) {// 法师
                // 欢迎光临，这次又是什么东西坏掉啦？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aanon8"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 欢迎光临，这次又是什么东西坏掉啦？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aanon8"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 欢迎光临，这次又是什么东西坏掉啦？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aanon8"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 欢迎光临，这次又是什么东西坏掉啦？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aanon8"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;

        if (pc.isKnight()) {// 骑士
            if (cmd.equalsIgnoreCase("request hood of red knight")) {// 交换红骑士头巾
                // 任务已经完成
                if (pc.getQuest().isEnd(KnightLv15_1.QUEST.get_id())) {
                    return;
                }
                // 任务进度达到2
                if (pc.getQuest().get_step(KnightLv15_1.QUEST.get_id()) == 2) {
                    // 需要物件不足
                    if (CreateNewItem.checkNewItem(pc, new int[] { 40540,
                            40601, 20005 }, // 任务完成需要物件(古老的交易文件 x 1 龙龟甲 x 1 骑士头巾
                                            // x 1)
                            new int[] { 1, 1, 1 }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                        isCloseList = true;

                    } else {// 需要物件充足
                        // 收回任务需要物件 给予任务完成物件
                        CreateNewItem.createNewItem(pc, new int[] { 40540,
                                40601, 20005 }, new int[] { 1, 1, 1 }, // 需要古老的交易文件
                                                                       // x 1
                                                                       // 龙龟甲 x
                                                                       // 1 骑士头巾
                                                                       // x 1
                                new int[] { 20027 }, 1, new int[] { 1 });// 给予红骑士头巾
                                                                         // x 1
                        // 将任务设置结束
                        QuestClass.get().endQuest(pc,
                                KnightLv15_1.QUEST.get_id());
                        isCloseList = true;
                    }

                } else {
                    // 唉呦！欢迎欢迎，这次又是什么装备坏了啊？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aanon1"));
                }
            }
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    @Override
    public int workTime() {
        return 8;
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
