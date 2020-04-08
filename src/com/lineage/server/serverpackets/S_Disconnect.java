package com.lineage.server.serverpackets;

/**
 * 立即中断连线
 * 
 * @author dexc
 * 
 */
public class S_Disconnect extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 立即中断连线
     */
    public S_Disconnect() {
        // final int content = 500;

        this.writeC(S_OPCODE_DISCONNECT);
        this.writeH(0x01f4);// 500
        this.writeD(0x00000000);
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
