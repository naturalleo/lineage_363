package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 更新经验值
 * 
 * @author dexc
 * 
 */
public class S_Exp extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 更新经验值
     * 
     * @param pc
     */
    public S_Exp(final L1PcInstance pc) {
        this.writeC(S_OPCODE_EXP);
        this.writeC(pc.getLevel());
        // this.writeD((int) pc.getExp());
        this.writeExp(pc.getExp());
        this.writeC(0x01);
    }

    /**
     * 更新经验值 - 测试
     * 
     * @param pc
     */
    public S_Exp() {
        this.writeC(S_OPCODE_EXP);
        this.writeC(59);// LV 59
        this.writeD(414931028);// 95.7553%
        this.writeC(0x01);
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
