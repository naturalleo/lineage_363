package com.lineage.server.serverpackets;

/**
 * 学习魔法材料不足
 * 
 * @author dexc
 * 
 */
public class S_ItemError extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 学习魔法材料不足
     * 
     * @param skillid
     */
    public S_ItemError(final int skillid) {
        this.buildPacket(skillid);
    }

    private void buildPacket(final int skillid) {
        // 0000: 6f 00 00 00 00 10 4f e9 o.....O.
        this.writeC(S_OPCODE_ITEMERROR);
        this.writeD(skillid);
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
