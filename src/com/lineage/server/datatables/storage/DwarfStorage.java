package com.lineage.server.datatables.storage;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.lineage.server.model.Instance.L1ItemInstance;

/**
 * 仓库物件清单
 * 
 * @author dexc
 * 
 */
public interface DwarfStorage {

    /**
     * 预先加载
     */
    public void load();

    /**
     * 传回全部仓库数据
     * 
     * @return
     */
    public Map<String, CopyOnWriteArrayList<L1ItemInstance>> allItems();

    /**
     * 传回仓库数据
     * 
     * @return
     */
    public CopyOnWriteArrayList<L1ItemInstance> loadItems(
            final String account_name);

    /**
     * 删除人物背包资料(完整)
     * 
     * @param account_name
     */
    public void delUserItems(final String account_name);

    /**
     * 该仓库是否有指定数据
     * 
     * @param account_name
     * @param objid
     * @param count
     * @return
     */
    public boolean getUserItems(final String account_name, final int objid,
            int count);

    /**
     * 加入仓库数据
     */
    public void insertItem(final String account_name, final L1ItemInstance item);

    /**
     * 仓库资料更新(物品数量)
     * 
     * @param item
     */
    public void updateItem(final L1ItemInstance item);

    /**
     * 仓库物品资料删除
     * 
     * @param account_name
     * @param item
     */
    public void deleteItem(final String account_name, final L1ItemInstance item);

}
