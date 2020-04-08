package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 魔法效果:力量提升
 * 
 * @author dexc
 * 
 */
public class S_Strup extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 魔法效果:力量提升
     * 
     * @param pc
     *            执行者
     * @param type
     *            增加值
     * @param time
     *            时间
     */
    public S_Strup(final L1PcInstance pc, final int type, final int time) {
        // 0000: 3a b0 04 10 5a 05 9e 01 :...Z...
        this.writeC(S_OPCODE_STRUP);
        this.writeH(time);
        this.writeC(pc.getStr());
        this.writeC(pc.getInventory().getWeight240());
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
