package com.lineage.server.serverpackets;

/**
 * 城堡宝库(要求存入资金)
 * 
 * @author dexc
 * 
 */
public class S_Deposit extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 城堡宝库(要求存入资金)
     * 
     * @param objecId
     */
    public S_Deposit(final int objecId) {
        this.writeC(S_OPCODE_DEPOSIT);
        this.writeD(objecId);
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
