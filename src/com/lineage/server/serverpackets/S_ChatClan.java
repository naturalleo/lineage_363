package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 血盟频道
 * 
 * @author dexc
 * 
 */
public class S_ChatClan extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 血盟频道
     * 
     * @param pc
     * @param chat
     */
    public S_ChatClan(final L1PcInstance pc, final String chat) {
        this.buildPacket(pc, chat);
    }

    private void buildPacket(final L1PcInstance pc, final String chat) {
        this.writeC(S_OPCODE_GLOBALCHAT);
        this.writeC(0x04);
        this.writeS("{" + pc.getName() + "} " + chat);
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
