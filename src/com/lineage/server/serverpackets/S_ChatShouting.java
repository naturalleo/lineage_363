package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 大喊频道
 * 
 * @author dexc
 * 
 */
public class S_ChatShouting extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 大喊频道
     * 
     * @param pc
     * @param chat
     */
    public S_ChatShouting(final L1PcInstance pc, final String chat) {
        this.buildPacket(pc, chat);
    }

    private void buildPacket(final L1PcInstance pc, final String chat) {
        this.writeC(S_OPCODE_NORMALCHAT);
        this.writeC(0x02);
        this.writeD(pc.isInvisble() ? 0 : pc.getId());
        this.writeS("<" + pc.getName() + "> " + chat);

        this.writeH(pc.getX());
        this.writeH(pc.getY());
    }

    /**
     * NPC对话输出
     * 
     * @param pc
     * @param chat
     */
    public S_ChatShouting(final L1NpcInstance npc, final String chat) {
        this.writeC(S_OPCODE_NORMALCHAT);
        this.writeC(0x02);
        this.writeD(npc.isInvisble() ? 0 : npc.getId());
        this.writeS("<" + npc.getNameId() + "> " + chat);

        this.writeH(npc.getX());
        this.writeH(npc.getY());
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
