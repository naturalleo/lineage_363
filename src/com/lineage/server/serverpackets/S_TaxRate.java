package com.lineage.server.serverpackets;

public class S_TaxRate extends ServerBasePacket {

    private byte[] _byte = null;

    public S_TaxRate(final int objecId) {
        this.writeC(S_OPCODE_TAXRATE);
        this.writeD(objecId);
        this.writeC(0x0a); // 10 10%~50%
        this.writeC(0x32); // 50
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
