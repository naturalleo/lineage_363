package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 更新角色防御属性
 * 
 * @author dexc
 * 
 */
public class S_OwnCharAttrDef extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 更新角色防御属性
     * 
     * @param pc
     */
    public S_OwnCharAttrDef(final L1PcInstance pc) {
        this.buildPacket(pc);
    }

    private void buildPacket(final L1PcInstance pc) {
        this.writeC(S_OPCODE_OWNCHARATTRDEF);
        int ac = pc.getAc();

        this.writeC(ac);
        this.writeC(pc.getFire());
        this.writeC(pc.getWater());
        this.writeC(pc.getWind());
        this.writeC(pc.getEarth());
    }

    /**
     * 更新角色防御属性-测试
     * 
     * @param pc
     */
    public S_OwnCharAttrDef() {
        this.writeC(S_OPCODE_OWNCHARATTRDEF);
        this.writeC(-99);
        this.writeC(90);
        this.writeC(85);
        this.writeC(80);
        this.writeC(75);
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
