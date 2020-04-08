package com.lineage.server.model;

import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.serverpackets.S_NpcChatGlobal;
import com.lineage.server.serverpackets.S_NpcChatShouting;
import com.lineage.server.templates.L1NpcChat;
import com.lineage.server.world.World;

public class L1NpcChatTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(L1NpcChatTimer.class);

    private final L1NpcInstance _npc;

    private final L1NpcChat _npcChat;

    public L1NpcChatTimer(final L1NpcInstance npc, final L1NpcChat npcChat) {
        this._npc = npc;
        this._npcChat = npcChat;
    }

    @Override
    public void run() {
        try {
            if ((this._npc == null) || (this._npcChat == null)) {
                return;
            }

            if ((this._npc.getHiddenStatus() != L1NpcInstance.HIDDEN_STATUS_NONE)
                    || this._npc._destroyed) {
                return;
            }

            final int chatTiming = this._npcChat.getChatTiming();
            final int chatInterval = this._npcChat.getChatInterval();
            final boolean isShout = this._npcChat.isShout();
            final boolean isWorldChat = this._npcChat.isWorldChat();
            final String chatId1 = this._npcChat.getChatId1();
            final String chatId2 = this._npcChat.getChatId2();
            final String chatId3 = this._npcChat.getChatId3();
            final String chatId4 = this._npcChat.getChatId4();
            final String chatId5 = this._npcChat.getChatId5();

            if (!chatId1.equals("")) {
                this.chat(this._npc, chatTiming, chatId1, isShout, isWorldChat);
            }

            if (!chatId2.equals("")) {
                Thread.sleep(chatInterval);
                this.chat(this._npc, chatTiming, chatId2, isShout, isWorldChat);
            }

            if (!chatId3.equals("")) {
                Thread.sleep(chatInterval);
                this.chat(this._npc, chatTiming, chatId3, isShout, isWorldChat);
            }

            if (!chatId4.equals("")) {
                Thread.sleep(chatInterval);
                this.chat(this._npc, chatTiming, chatId4, isShout, isWorldChat);
            }

            if (!chatId5.equals("")) {
                Thread.sleep(chatInterval);
                this.chat(this._npc, chatTiming, chatId5, isShout, isWorldChat);
            }
        } catch (final Throwable e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void chat(final L1NpcInstance npc, final int chatTiming,
            final String chatId, final boolean isShout,
            final boolean isWorldChat) {
        if ((chatTiming == L1NpcInstance.CHAT_TIMING_APPEARANCE)
                && npc.isDead()) {
            return;
        }
        if ((chatTiming == L1NpcInstance.CHAT_TIMING_DEAD) && !npc.isDead()) {
            return;
        }
        if ((chatTiming == L1NpcInstance.CHAT_TIMING_HIDE) && npc.isDead()) {
            return;
        }

        if (!isShout) {
            npc.broadcastPacketX8(new S_NpcChat(npc, chatId));

        } else {
            npc.wideBroadcastPacket(new S_NpcChatShouting(npc, chatId));
        }

        if (isWorldChat) {
            // XXX npcはsendPacketsできないので、ワールド内のPCからsendPacketsさせる
            for (final L1PcInstance pc : World.get().getAllPlayers()) {
                if (pc != null) {
                    pc.sendPackets(new S_NpcChatGlobal(npc, chatId));
                }
                break;
            }
        }
    }

}
