package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 魔法效果:敏捷提升
 * 
 * @author dexc
 * 
 */
public class S_Dexup extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 魔法效果:敏捷提升
     * 
     * @param pc
     *            原始值
     * @param type
     *            增加值
     * @param time
     *            时间
     */
    public S_Dexup(final L1PcInstance pc, final int type, final int time) {
        // 0000: 65 b0 04 13 05 21 3e d8 e....!>.
        this.writeC(S_OPCODE_DEXUP);
        this.writeH(time);
        this.writeC(pc.getDex());
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
