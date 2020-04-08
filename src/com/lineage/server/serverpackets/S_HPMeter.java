package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Character;

/**
 * 物件血条
 * 
 * @author dexc
 * 
 */
public class S_HPMeter extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物件血条
     * 
     * @param objId
     * @param hpRatio
     */
    public S_HPMeter(final int objId, final int hpRatio) {
        this.buildPacket(objId, hpRatio);
    }

    /**
     * 物件血条
     * 
     * @param cha
     */
    public S_HPMeter(final L1Character cha) {
        final int objId = cha.getId();
        int hpRatio = 100;
        if (0 < cha.getMaxHp()) {
            hpRatio = 100 * cha.getCurrentHp() / cha.getMaxHp();
        }

        this.buildPacket(objId, hpRatio);
    }

    private void buildPacket(final int objId, final int hpRatio) {
        this.writeC(S_OPCODE_HPMETER);
        this.writeD(objId);
        this.writeC(hpRatio);
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
