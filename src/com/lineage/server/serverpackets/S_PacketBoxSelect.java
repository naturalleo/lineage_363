package com.lineage.server.serverpackets;

/**
 * 角色选择视窗
 */
public class S_PacketBoxSelect extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * <font color=#00800>角色选择视窗</font> > 0000 : 39 2a e1 88 08 12 48 fa
     * 9*....H.
     */
    public static final int LOGOUT = 0x2a;// 42

    /**
     * 角色选择视窗
     * 
     * @param subCode
     */
    public S_PacketBoxSelect() {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(LOGOUT);// 42
        this.writeC(0x00);
        this.writeC(0x00);
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
