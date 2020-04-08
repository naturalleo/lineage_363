package com.lineage.server.serverpackets;

/**
 * 宝物公告
 * 
 * @author loli
 * 
 */
public class S_BoxMessage extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 宝物公告
     * 
     * @param msg
     */
    public S_BoxMessage(final String msg) {
        this.writeC(S_OPCODE_NPCSHOUT);
        this.writeC(0x00);// 颜色
        this.writeD(0x00000000);
        this.writeS(msg);
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
