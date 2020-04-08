package com.lineage.server.serverpackets;

/**
 * 交易封包
 * 
 * @author dexc
 * 
 */
public class S_Trade extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 交易封包
     * 
     * @param name
     */
    public S_Trade(final String name) {
        this.writeC(S_OPCODE_TRADE);
        this.writeS(name);
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
