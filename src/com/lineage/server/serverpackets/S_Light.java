package com.lineage.server.serverpackets;

/**
 * 更新物件亮度
 * 
 * @author dexc
 * 
 */
public class S_Light extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 更新物件亮度
     * 
     * @param objid
     * @param type
     */
    public S_Light(final int objid, final int type) {
        this.buildPacket(objid, type);
    }

    private void buildPacket(final int objid, final int type) {
        // 0000: 66 a5 ef 8a 01 00 e0 58 f......X
        this.writeC(S_OPCODE_LIGHT);
        this.writeD(objid);
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
