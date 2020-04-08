package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Item;

/**
 * 天宝购买纪录
 */
public interface ServerCnInfoStorage {

    /**
     * 天宝购买纪录
     * 
     * @param pc
     * @param itemtmp
     * @param count
     */
    public void create(final L1PcInstance pc, final L1Item itemtmp,
            final int count);
}
