package com.lineage.server.datatables.storage;

import java.util.concurrent.CopyOnWriteArrayList;

import com.lineage.server.model.Instance.L1ItemInstance;

/**
 * 精灵仓库物件
 * 
 * @author dexc
 * 
 */
public interface DwarfForElfStorage {

    /**
     * 预先加载
     */
    public void load();

    /**
     * 传回精灵仓库数据
     * 
     * @return
     */
    public CopyOnWriteArrayList<L1ItemInstance> loadItems(
            final String account_name);

    /**
     * 删除精灵仓库资料(完整)
     * 
     * @param account_name
     */
    public void delUserItems(final String account_name);

    /**
     * 该精灵仓库是否有指定数据
     * 
     * @param account_name
     * @param objid
     * @param count
     * @return
     */
    public boolean getUserItems(final String account_name, final int objid,
            int count);

    /**
     * 加入精灵仓库数据
     */
    public void insertItem(final String account_name, final L1ItemInstance item);

    /**
     * 精灵仓库资料更新(物品数量)
     * 
     * @param item
     */
    public void updateItem(final L1ItemInstance item);

    /**
     * 精灵仓库物品资料删除
     * 
     * @param account_name
     * @param item
     */
    public void deleteItem(final String account_name, final L1ItemInstance item);

}
