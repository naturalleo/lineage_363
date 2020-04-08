package com.lineage.server.serverpackets;

/**
 * 传送锁定(座标点)
 * 
 * @author dexc
 * 
 */
public class S_Teleport2 extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 传送锁定(座标点)
     * 
     * @param mapid
     * @param id
     */
    public S_Teleport2(final int mapid, final int id) {
        // 0000: 4a 04 00 3a 05 15 00 cc J..:....
        this.writeC(S_OPCODE_TELEPORTLOCK);
        this.writeH(mapid);
        this.writeD(id);// 传送点编号
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
