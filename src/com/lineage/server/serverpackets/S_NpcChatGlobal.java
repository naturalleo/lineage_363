package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;

/**
 * NPC 广播频道
 * 
 * @author dexc
 * 
 */
public class S_NpcChatGlobal extends ServerBasePacket {

    private byte[] _byte = null;

    public S_NpcChatGlobal(final L1NpcInstance npc, final String chat) {
        this.buildPacket(npc, chat);
    }

    private void buildPacket(final L1NpcInstance npc, final String chat) {
        this.writeC(S_OPCODE_NPCSHOUT);
        this.writeC(0x03); // XXX 白色になる
        this.writeD(npc.getId());
        this.writeS("[" + npc.getNameId() + "] " + chat);
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
