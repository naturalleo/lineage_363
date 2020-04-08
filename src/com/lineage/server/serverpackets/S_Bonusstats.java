package com.lineage.server.serverpackets;

/**
 * 能力质选取资料
 * 
 * @author dexc
 * 
 */
public class S_Bonusstats extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 能力质选取资料
     * 
     * @param objid
     */
    public S_Bonusstats(final int objid) {
        this.buildPacket(objid);
    }

    private void buildPacket(final int objid) {
        this.writeC(S_OPCODE_SHOWHTML);
        this.writeD(objid);
        this.writeS("RaiseAttr");
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
