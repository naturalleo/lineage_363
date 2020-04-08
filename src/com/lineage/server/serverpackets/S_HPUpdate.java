package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.RangeInt;

/**
 * HP更新显示
 * 
 * @author dexc
 * 
 */
public class S_HPUpdate extends ServerBasePacket {

    private byte[] _byte = null;

    private static final RangeInt _hpRange = new RangeInt(1, 32767);

    /**
     * HP更新显示
     * 
     * @param currentHp
     * @param maxHp
     */
    public S_HPUpdate(final int currentHp, final int maxHp) {
        this.buildPacket(currentHp, maxHp);
    }

    /**
     * HP更新显示
     * 
     * @param pc
     */
    public S_HPUpdate(final L1PcInstance pc) {
        this.buildPacket(pc.getCurrentHp(), pc.getMaxHp());
    }

    public void buildPacket(final int currentHp, final int maxHp) {
        // 0000: 26 d8 01 fd 01 bd 53 a9 &.....S.
        this.writeC(S_OPCODE_HPUPDATE);
        this.writeH(_hpRange.ensure(currentHp));
        this.writeH(_hpRange.ensure(maxHp));

        /*
         * this.writeC(0x00); this.writeC(0x00); this.writeC(0x00);
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
