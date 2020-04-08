package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 对话纪录
 * 
 * @author dexc
 * 
 */
public interface LogChatStorage {

    /**
     * 具有传送对象
     * 
     * @param pc
     * @param target
     * @param text
     * @param type
     */
    public void isTarget(L1PcInstance pc, L1PcInstance target, String text,
            int type);

    /**
     * 无传送对象
     * 
     * @param pc
     * @param text
     * @param type
     */
    public void noTarget(L1PcInstance pc, String text, int type);

}
