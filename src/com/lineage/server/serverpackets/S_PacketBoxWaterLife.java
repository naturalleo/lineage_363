package com.lineage.server.serverpackets;

/**
 * 水之元气 OVER
 * 
 * @author daien
 * 
 */
public class S_PacketBoxWaterLife extends ServerBasePacket {

    private byte[] _byte = null;

    public S_PacketBoxWaterLife() {
        writeC(S_OPCODE_PACKETBOX);
        writeC(0x3b);
        writeH(0x0000);
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
