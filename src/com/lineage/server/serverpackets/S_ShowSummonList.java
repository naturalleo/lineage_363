package com.lineage.server.serverpackets;

/**
 * 
 * @author dexc
 * 
 */
public class S_ShowSummonList extends ServerBasePacket {

    private byte[] _byte = null;

    public S_ShowSummonList(final int objid) {
        this.writeC(S_OPCODE_SHOWHTML);
        this.writeD(objid);
        this.writeS("summonlist");
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
