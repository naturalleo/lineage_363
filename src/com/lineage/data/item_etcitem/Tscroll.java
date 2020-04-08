package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1PcQuest;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

public class Tscroll extends ItemExecutor {

    private static boolean ALT_TALKINGSCROLLQUEST = true;

    /**
	 *
	 */
    private Tscroll() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Tscroll();
    }

    @Override
    public void execute(final int[] data, final L1PcInstance pc,
            final L1ItemInstance item) {
        if (ALT_TALKINGSCROLLQUEST == true) {
            if (pc.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 0) {
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrolla"));

            } else if (pc.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 1) {
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollb"));

            } else if (pc.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 2) {
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollc"));

            } else if (pc.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 3) {
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrolld"));

            } else if (pc.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 4) {
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrolle"));

            } else if (pc.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 5) {
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollf"));

            } else if (pc.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 6) {
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollg"));

            } else if (pc.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 7) {
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollh"));

            } else if (pc.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 8) {
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrolli"));

            } else if (pc.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 9) {
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollj"));

            } else if (pc.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 10) {
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollk"));

            } else if (pc.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 11) {
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrolll"));

            } else if (pc.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 12) {
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollm"));

            } else if (pc.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 13) {
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrolln"));

            } else if (pc.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 255) {
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollo"));
            }

        } else {
            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollp"));
        }
    }
}
