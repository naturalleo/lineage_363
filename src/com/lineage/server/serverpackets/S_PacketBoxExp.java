package com.lineage.server.serverpackets;

/**
 * 受到殷海萨的祝福，增加了些许的狩猎经验值
 * 
 * @author DaiEn
 * 
 */
public class S_PacketBoxExp extends ServerBasePacket {

    private byte[] _byte = null;

    /** 1,550：受到殷海萨的祝福，增加了些许的狩猎经验值。 */
    public static final int LEAVES = 82;

    /**
     * 受到殷海萨的祝福，增加了些许的狩猎经验值
     * 
     * @param exp
     *            经验值增加率
     */
    public S_PacketBoxExp(final int exp) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(LEAVES);
        this.writeC(exp);
        this.writeC(0x00);
        this.writeC(0x00);
        this.writeC(0x00);
    }

    /**
     * 解除 受到殷海萨的祝福，增加了些许的狩猎经验值
     */
    public S_PacketBoxExp() {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(LEAVES);
        this.writeC(0x00);
        this.writeC(0x00);
        this.writeC(0x00);
        this.writeC(0x00);
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
