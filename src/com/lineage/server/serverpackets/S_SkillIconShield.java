package com.lineage.server.serverpackets;

/**
 * 魔法效果:防御
 * 
 * @author dexc
 * 
 */
public class S_SkillIconShield extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 魔法效果:防御
     * 
     * @param type
     *            增加值
     * @param time
     *            时间
     */
    public S_SkillIconShield(final int type, final int time) {
        // 0000: 06 08 07 02 50 01 00 29 ....P..)
        this.writeC(S_OPCODE_SKILLICONSHIELD);
        this.writeH(time);
        this.writeC(type);

        /*
         * this.writeC(0x50); this.writeC(0x01); this.writeC(0x00);
         * this.writeC(0x29);
         */
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
