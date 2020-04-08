package com.lineage.server.model.monitor;

import com.lineage.server.model.Instance.L1PcInstance;

public class L1PcInvisDelay extends L1PcMonitor {

    public L1PcInvisDelay(final int oId) {
        super(oId);
    }

    @Override
    public void execTask(final L1PcInstance pc) {
        pc.addInvisDelayCounter(-1);
    }
}
