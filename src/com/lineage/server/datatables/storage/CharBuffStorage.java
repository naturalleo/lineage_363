package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 保留技能纪录
 * 
 * @author dexc
 * 
 */
public interface CharBuffStorage {

    /**
     * 初始化载入
     */
    public void load();

    /**
     * 增加保留技能纪录
     * 
     * @param pc
     */
    public void saveBuff(L1PcInstance pc);

    /**
     * 取回保留技能纪录
     * 
     * @param pc
     */
    public void buff(L1PcInstance pc);

    /**
     * 删除全部保留技能纪录
     * 
     * @param pc
     */
    public void deleteBuff(L1PcInstance pc);

    /**
     * 删除全部保留技能纪录
     * 
     * @param objid
     */
    public void deleteBuff(int objid);
}
