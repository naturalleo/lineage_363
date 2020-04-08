package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * GM指令使用纪录
 */
public interface ServerGmCommandStorage {

    /**
     * GM指令使用纪录
     * 
     * @param pc
     * @param itemtmp
     * @param count
     */
    public void create(final L1PcInstance pc, final String cmd);
}
