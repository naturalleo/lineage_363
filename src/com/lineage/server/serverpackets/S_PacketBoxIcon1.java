package com.lineage.server.serverpackets;

public class S_PacketBoxIcon1 extends ServerBasePacket {

    /** 技能图示 */
    private static final int _icons_1 = 0x14;// 20;//0x14

    private static final int _dodge_up = 0x58;// 88 增加闪避率

    private static final int _dodge_down = 0x65;// 101 减少闪避率

    private byte[] _byte = null;

    public S_PacketBoxIcon1(final int type, final int time) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(_icons_1);
        this.writeH(time);
        this.writeC(type);
    }

    /**
     * 技能 - 闪避率
     * 
     * @param type
     *            true:增加闪避率 false:减少闪避率
     * @param i
     *            增减质
     */
    public S_PacketBoxIcon1(final boolean type, final int i) {
        this.writeC(S_OPCODE_PACKETBOX);
        if (type) {// 增加闪避率
            this.writeC(_dodge_up);
            this.writeC(i);

        } else {// 减少闪避率
            this.writeC(_dodge_down);
            this.writeC(i);
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
