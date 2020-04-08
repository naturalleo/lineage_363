package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 更新正义值
 * 
 * @author dexc
 * 
 */
public class S_Lawful extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 更新正义值
     * 
     * @param pc
     */
    public S_Lawful(L1PcInstance pc) {
        this.buildPacket(pc);
    }

    private void buildPacket(L1PcInstance pc) {
        // 0000: 75 5d 71 cd 00 00 00 45 u]q....E
        this.writeC(S_OPCODE_LAWFUL);
        this.writeD(pc.getId());
        this.writeH(pc.getLawful());
    }

    /**
     * 更新正义值
     * 
     * @param objid
     * @param lawful
     */
    public S_Lawful(final int objid) {
        this.writeC(S_OPCODE_LAWFUL);
        this.writeD(objid);
        this.writeH(-32768);
        this.writeH(-32768);
        this.writeH(-32768);
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
