package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ShopSellListCnX;

public class XXX extends ItemExecutor {

    /**
     * NC助理
     */
    private XXX() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new XXX();
    }

    @Override
    public void execute(final int[] data, final L1PcInstance pc,
            final L1ItemInstance item) {
        pc.sendPackets(new S_ShopSellListCnX(pc, item.getId()));
    }
}
