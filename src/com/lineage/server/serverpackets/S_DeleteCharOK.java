package com.lineage.server.serverpackets;

/**
 * 角色移除(立即/非立即)
 * 
 * @author dexc
 * 
 */
public class S_DeleteCharOK extends ServerBasePacket {

    private byte[] _byte = null;

    public static final int DELETE_CHAR_NOW = 0x05;// 立即删除

    public static final int DELETE_CHAR_AFTER_7DAYS = 0x51;// 7日删除

    /**
     * 角色移除(立即/非立即)
     * 
     * @param type
     */
    public S_DeleteCharOK(final int type) {
        // 0000: 04 05 34 12 59 00 00 32 ..4.Y..2
        this.writeC(S_OPCODE_DETELECHAROK);
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
