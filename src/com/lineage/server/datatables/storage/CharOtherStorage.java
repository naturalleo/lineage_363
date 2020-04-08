package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1PcOther;

/**
 * 额外纪录资料
 * 
 * @author dexc
 * 
 */
public interface CharOtherStorage {

    /**
     * 初始化载入
     */
    public void load();

    /**
     * 取回保留额外纪录
     * 
     * @param pc
     */
    public L1PcOther getOther(final L1PcInstance pc);

    /**
     * 增加保留额外纪录
     * 
     * @param objId
     * @param other
     */
    public void storeOther(final int objId, final L1PcOther other);

    /**
     * 归0杀人次数
     */
    public void tam();

}
