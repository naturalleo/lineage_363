package com.lineage.server.serverpackets;

/**
 * 给GM的讯息
 * 
 * @author dexc
 * 
 */
public class S_ToGmMessage extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 给GM的讯息
     * 
     * @param mode
     */
    public S_ToGmMessage(final String info) {
        this.writeC(S_OPCODE_NPCSHOUT);
        this.writeC(0x00);// 一般频道
        this.writeD(0x00000000);
        this.writeS("\\fY" + info);
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
