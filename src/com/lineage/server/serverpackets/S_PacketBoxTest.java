package com.lineage.server.serverpackets;

/**
 * 测试
 * 
 * @author dexc
 * 
 */
public class S_PacketBoxTest extends ServerBasePacket {

    private byte[] _byte = null;

    public S_PacketBoxTest() {
        // {0x52,0x52,(byte)0xc8,0x00,0x00,0x00,(byte)0xeb,0x1f},
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(0x52);
        this.writeC(150);// 经验值提高指数
        this.writeC(0x00);
        this.writeC(0x00);
        this.writeC(0x00);
    }

    public S_PacketBoxTest(byte ocid, final int time) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(ocid);
        this.writeC(time);// 经验值提高指数
        this.writeC(0x00);
        this.writeC(0x00);
        this.writeC(0x00);

    }

    public S_PacketBoxTest(final int type, final int time) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(type);
        this.writeH(time);
        this.writeH(0x00);
    }

    public S_PacketBoxTest(final int value, String[] clanName) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(value);
        for (int i = 0; i < value; i++) {
            this.writeS(clanName[i]);
        }
    }

    public S_PacketBoxTest(final int time) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(79);
        this.writeC(2);
        this.writeS("TEMP");
        this.writeS("AASS");

    }

    public S_PacketBoxTest(byte[] bs) {
        for (byte outbs : bs) {
            this.writeC(outbs);
        }
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
