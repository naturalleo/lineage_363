package com.lineage.server.serverpackets;

public class S_PacketBoxIcon2 extends ServerBasePacket {

    /** 技能图示 */
    private static final int ICONS2 = 0x15;// 21;//0x15

    private byte[] _byte = null;

    public S_PacketBoxIcon2(final int type, final int time) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(ICONS2);
        this.writeH(time);
        this.writeC(type);
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
