package com.lineage.server.datatables.storage;

import com.lineage.server.templates.L1ItemPower_name;

/**
 * 物品凹槽资料
 * 
 * @author dexc
 */
public interface CharItemsPowerStorage {

    /**
     * 资料预先载入
     */
    public void load();

    /**
     * 增加物品凹槽资料
     * 
     * @param objid
     * @param power
     * @return
     */
    public void storeItem(final int objId, final L1ItemPower_name power)
            throws Exception;

    /**
     * 更新凹槽资料
     * 
     * @param item_obj_id
     * @param power
     */
    public void updateItem(final int item_obj_id, final L1ItemPower_name power);
    
    /**
     * 移除凹槽资料
     * 
     * @param item_obj_id
     */
    public void removeItemPower(final int item_obj_id);
}
