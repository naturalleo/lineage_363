package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.RangeInt;

/**
 * MP更新显示
 * 
 * @author dexc
 * 
 */
public class S_MPUpdate extends ServerBasePacket {

    private byte[] _byte = null;

    private static final RangeInt _mpRangeA = new RangeInt(0, 32767);

    private static final RangeInt _mpRangeX = new RangeInt(1, 32767);

    /**
     * MP更新显示
     * 
     * @param currentmp
     * @param maxmp
     */
    public S_MPUpdate(final int currentmp, final int maxmp) {
        this.buildPacket(currentmp, maxmp);
    }

    /**
     * MP更新显示
     * 
     * @param pc
     */
    public S_MPUpdate(final L1PcInstance pc) {
        this.buildPacket(pc.getCurrentMp(), pc.getMaxMp());
    }

    /**
     * MP更新显示
     * 
     * @param currentmp
     * @param maxmp
     * @return
     */
    private void buildPacket(final int currentmp, final int maxmp) {
        // 0000: 0f bb 00 ce 00 52 9b 97 .....R..
        this.writeC(S_OPCODE_MPUPDATE);
        this.writeH(_mpRangeA.ensure(currentmp));
        this.writeH(_mpRangeX.ensure(maxmp));

        /*
         * this.writeC(0x00); this.writeC(0x00); this.writeC(0x00);
         */

        /*
         * if (currentmp < 0) { writeH(0);
         * 
         * } else if (currentmp > 32767) { writeH(32767);
         * 
         * } else { writeH(currentmp); }
         * 
         * if (maxmp < 1) { writeH(1);
         * 
         * } else if (maxmp > 32767) { writeH(32767);
         * 
         * } else { writeH(maxmp); }
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
