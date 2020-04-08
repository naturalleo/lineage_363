package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 队伍频道(聊天)
 * 
 * @author dexc
 * 
 */
public class S_ChatParty2 extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 队伍频道(聊天)
     * 
     * @param pc
     * @param chat
     */
    public S_ChatParty2(final L1PcInstance pc, final String chat) {
        this.buildPacket(pc, chat);
    }

    private void buildPacket(final L1PcInstance pc, final String chat) {
        this.writeC(S_OPCODE_NORMALCHAT);
        this.writeC(0x0e);
        this.writeD(pc.isInvisble() ? 0 : pc.getId());
        this.writeS("(" + pc.getName() + ") " + chat);
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
