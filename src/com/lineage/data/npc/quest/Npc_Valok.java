package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.thread.GeneralThreadPool;

/**
 * 炎魔<BR>
 * 91344<BR>
 * 说明:魔法师．哈汀(故事)
 * 
 * @author dexc
 * 
 */
public class Npc_Valok extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Valok.class);

    private Npc_Valok() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Valok();
    }

    @Override
    public int type() {
        return 33;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.get_hardinR() != null) {
                // 就像扑向地狱之火的飞蛾一样的存在？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_ep017"));

            } else {
                // 该讯息只有发生错误时才会显示。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_html05"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * NPC召唤(32)
     * 
     * @param mode
     */
    @Override
    public void spawn(final L1NpcInstance npc) {
        ValokR valokR = new ValokR(npc);
        GeneralThreadPool.get().execute(valokR);
    }

    class ValokR implements Runnable {

        private final L1NpcInstance _npc;

        public ValokR(final L1NpcInstance npc) {
            _npc = npc;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(7000);
                _npc.broadcastPacketAll(new S_NpcChat(_npc, "$7675"));// 7675
                                                                      // 是阿，这也是他的意思…

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
