package com.lineage.server.serverpackets;

/**
 * 产生动画(物件)
 * 
 * @author dexc
 * 
 */
public class S_SkillSound extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 产生动画(物件)
     * 
     * @param objid
     * @param gfxid
     */
    public S_SkillSound(final int objid, final int gfxid) {
        this.buildPacket(objid, gfxid);
    }

    private void buildPacket(final int objid, final int gfxid) {
        // 0000: 56 2c 80 a1 01 82 08 87 V,......
        this.writeC(S_OPCODE_SKILLSOUNDGFX);
        this.writeD(objid);
        this.writeH(gfxid);
    }

    /**
     * 产生动画(物件)
     * 
     * @param objid
     * @param gfxid
     * @param time
     */
    public S_SkillSound(final int objid, final int gfxid, final int time) {
        // 0000: 56 2c 80 a1 01 82 08 87 V,......
        this.writeC(S_OPCODE_SKILLSOUNDGFX);
        this.writeD(objid);
        this.writeH(gfxid);
        this.writeH(time);
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
