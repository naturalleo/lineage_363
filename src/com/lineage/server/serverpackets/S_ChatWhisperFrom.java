package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 密语交谈(接收)频道
 * 
 * @author dexc
 * 
 */
public class S_ChatWhisperFrom extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 密语交谈(接收)频道
     * 
     * @param pc
     * @param chat
     */
    public S_ChatWhisperFrom(final L1PcInstance pc, final String chat) {
        this.buildPacket(pc, chat);
    }

    private void buildPacket(final L1PcInstance pc, final String chat) {
        this.writeC(S_OPCODE_WHISPERCHAT);
        this.writeS(pc.getName());
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
