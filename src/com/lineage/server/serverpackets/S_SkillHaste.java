package com.lineage.server.serverpackets;

/**
 * 魔法效果:加速颣
 * 
 * @author dexc
 * 
 */
public class S_SkillHaste extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 魔法效果:加速颣
     * 
     * @param objid
     *            对象objid
     * @param mode
     *            效果 <br>
     *            0:正常<br>
     *            1:加速<br>
     *            2:减速<br>
     * @param time
     *            时间
     */
    public S_SkillHaste(final int objid, final int mode, final int time) {
        // 0000: 0b 9d dc ad 01 01 b0 04 ........
        this.writeC(S_OPCODE_SKILLHASTE);
        this.writeD(objid);
        this.writeC(mode);
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
