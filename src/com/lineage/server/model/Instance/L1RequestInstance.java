package com.lineage.server.model.Instance;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.NPCTalkDataTable;
import com.lineage.server.model.L1NpcTalkData;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1Npc;

public class L1RequestInstance extends L1NpcInstance {

    private static final long serialVersionUID = 1L;

    private static final Log _log = LogFactory.getLog(L1RequestInstance.class);

    public L1RequestInstance(final L1Npc template) {
        super(template);
    }

    @Override
    public void onAction(final L1PcInstance player) {
        try {
            final int objid = this.getId();

            final L1NpcTalkData talking = NPCTalkDataTable.get().getTemplate(
                    this.getNpcTemplate().get_npcId());

            if (talking != null) {
                if (player.getLawful() < -1000) { // プレイヤーがカオティック
                    player.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
                } else {
                    player.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void onFinalAction(final L1PcInstance player, final String action) {

    }

    public void doFinalAction(final L1PcInstance player) {

    }
}
