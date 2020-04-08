package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;

/**
 * NPC 大喊频道
 * 
 * @author dexc
 * 
 */
public class S_NpcChatShouting extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * NPC 大喊频道
     * 
     * @param npc
     * @param chat
     */
    public S_NpcChatShouting(final L1NpcInstance npc, final String chat) {
        this.buildPacket(npc, chat);
    }

    private void buildPacket(final L1NpcInstance npc, final String chat) {
        this.writeC(S_OPCODE_NPCSHOUT); // Key is 16 , can use
        // desc-?.tbl
        this.writeC(0x02); // Color
        this.writeD(npc.getId());
        this.writeS("<" + npc.getNameId() + "> " + chat);
    }

    @Override
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = this.getBytes();
        }
        return this._byte;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
