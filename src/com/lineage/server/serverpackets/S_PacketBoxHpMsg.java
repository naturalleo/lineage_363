package com.lineage.server.serverpackets;

/**
 * 你觉得舒服多了讯息
 */
public class S_PacketBoxHpMsg extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * <font color=#00800>(77) \f1你觉得舒服多了。</font>
     */
    private static final int MSG_FEEL_GOOD = 31;

    public S_PacketBoxHpMsg() {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(MSG_FEEL_GOOD);
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
