package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 密语交谈(发送)频道
 * 
 * @author dexc
 * 
 */
public class S_ChatWhisperTo extends ServerBasePacket {

    private byte[] _byte = null;

    public S_ChatWhisperTo(final L1PcInstance pc, final String chat) {
        this.buildPacket(pc, chat);
    }

    private void buildPacket(final L1PcInstance pc, final String chat) {
        this.writeC(S_OPCODE_GLOBALCHAT);
        this.writeC(0x09);
        this.writeS("-> (" + pc.getName() + ") " + chat);
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
