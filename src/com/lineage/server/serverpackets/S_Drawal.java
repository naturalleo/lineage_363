package com.lineage.server.serverpackets;

/**
 * 城堡宝库(要求领出资金)
 * 
 * @author dexc
 * 
 */
public class S_Drawal extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 城堡宝库(要求领出资金)
     * 
     * @param objectId
     * @param count
     */
    public S_Drawal(final int objectId, final long count) {
        this.writeC(S_OPCODE_DRAWAL);
        this.writeD(objectId);
        this.writeD((int) Math.min(count, 2000000000));
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
