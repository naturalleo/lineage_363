package com.lineage.server.datatables.storage;

import java.util.concurrent.CopyOnWriteArrayList;

import com.lineage.server.model.Instance.L1ItemInstance;

/**
 * 血盟仓库物件清单
 * 
 * @author dexc
 * 
 */
public interface DwarfForClanStorage {

    /**
     * 预先加载
     */
    public void load();

    /**
     * 传回血盟仓库数据
     * 
     * @return
     */
    public CopyOnWriteArrayList<L1ItemInstance> loadItems(final String clan_name);

    /**
     * 删除血盟仓库资料(完整)
     * 
     * @param account_name
     */
    public void delUserItems(final String clan_name);

    /**
     * 该血盟仓库是否有指定数据
     * 
     * @param clan_name
     * @param objid
     * @param count
     * @return
     */
    public boolean getUserItems(final String clan_name, final int objid,
            int count);

    /**
     * 加入血盟仓库数据
     */
    public void insertItem(final String clan_name, final L1ItemInstance item);

    /**
     * 血盟仓库资料更新(物品数量)
     * 
     * @param item
     */
    public void updateItem(final L1ItemInstance item);

    /**
     * 血盟仓库物品资料删除
     * 
     * @param account_name
     * @param item
     */
    public void deleteItem(final String clan_name, final L1ItemInstance item);
}
