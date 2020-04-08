package com.lineage.server.serverpackets;

public class S_WhoAmount extends ServerBasePacket {

    private byte[] _byte = null;

    public S_WhoAmount(final String amount) {
        this.writeC(S_OPCODE_SERVERMSG);
        this.writeH(0x0051);
        this.writeC(0x01);
        this.writeS(amount);
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
