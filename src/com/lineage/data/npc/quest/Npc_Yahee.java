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
 * 火焰之影<BR>
 * 91343<BR>
 * 说明:魔法师．哈汀(故事)
 * 
 * @author dexc
 * 
 */
public class Npc_Yahee extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Yahee.class);

    private Npc_Yahee() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Yahee();
    }

    @Override
    public int type() {
        return 33;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.get_hardinR() != null) {
                // 竟敢，找我说话。 勇气可嘉啊。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_ep009"));

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
        YaheeR yaheeR = new YaheeR(npc);
        GeneralThreadPool.get().execute(yaheeR);
    }

    class YaheeR implements Runnable {

        private final L1NpcInstance _npc;

        public YaheeR(final L1NpcInstance npc) {
            _npc = npc;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(7000);
                _npc.broadcastPacketAll(new S_NpcChat(_npc, "$7657"));// 7657
                                                                      // 呼呼…是【哈汀】你阿！哈哈哈哈哈！

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
