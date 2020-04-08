package com.lineage.server.serverpackets;

/**
 * 角色皇冠
 * 
 * @author dexc
 * 
 */
public class S_CastleMaster extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 角色皇冠
     * 
     * @param type
     *            城堡编号
     * @param objecId
     *            人物OBJID
     */
    public S_CastleMaster(final int type, final int objecId) {
        this.buildPacket(type, objecId);
    }

    private void buildPacket(final int type, final int objecId) {
        this.writeC(S_OPCODE_CASTLEMASTER);
        this.writeC(type);
        this.writeD(objecId);
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
