package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 一般频道
 * 
 * @author dexc
 * 
 */
public class S_Chat extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 一般频道
     * 
     * @param pc
     * @param chat
     */
    public S_Chat(final L1PcInstance pc, final String chat) {
        this.buildPacket(pc, chat);
    }

    private void buildPacket(final L1PcInstance pc, final String chat) {
        this.writeC(S_OPCODE_NORMALCHAT);
        this.writeC(0x00);
        this.writeD(pc.isInvisble() ? 0 : pc.getId());
        this.writeS(pc.getName() + ": " + chat);
    }

    /**
     * NPC对话输出
     * 
     * @param npc
     * @param chat
     */
    public S_Chat(final L1NpcInstance npc, final String chat) {
        this.writeC(S_OPCODE_NORMALCHAT);
        this.writeC(0x00);
        this.writeD(npc.isInvisble() ? 0 : npc.getId());
        this.writeS(npc.getNameId() + ": " + chat);
    }

    public S_Chat(L1Object object, String chat, int x) {
        this.writeC(S_OPCODE_NORMALCHAT);
        this.writeC(0x00);
        this.writeD(object.getId());
        this.writeS(chat);
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
