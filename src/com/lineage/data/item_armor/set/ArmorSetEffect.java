package com.lineage.data.item_armor.set;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 套装效果接口
 * 
 * @author daien
 * 
 */
public interface ArmorSetEffect {

    /**
     * 套装效果启用
     * 
     * @param pc
     */
    public void giveEffect(L1PcInstance pc);

    /**
     * 套装效果结束
     * 
     * @param pc
     */
    public void cancelEffect(L1PcInstance pc);

    /**
     * 套装效果的值
     */
    public int get_mode();

}
