package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 强化纪录
 * 
 * @author dexc
 * 
 */
public interface LogEnchantStorage {

    /**
     * 强化纪录(失败)
     * 
     * @param pc
     * @param item
     */
    public void failureEnchant(final L1PcInstance pc, final L1ItemInstance item);

}
