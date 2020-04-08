package com.lineage.server.datatables.storage;

import java.util.HashMap;
import java.util.Map;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.templates.L1ShopS;

/**
 * 托售物件清单
 * 
 * @author dexc
 * 
 */
public interface DwarfShopStorage {

    /**
     * 预先加载
     */
    public void load();

    public int get_id();

    public void set_id(int id);

    /**
     * 传回全部出售中物件资料数据
     * 
     * @return
     */
    public HashMap<Integer, L1ShopS> allShopS();

    /**
     * 传回全部托售物件数据
     * 
     * @return
     */
    public Map<Integer, L1ItemInstance> allItems();

    /**
     * 传回指定托售物件数据
     * 
     * @return
     */
    public L1ShopS getShopS(int objid);

    /**
     * 指定人物托售纪录
     * 
     * @param pcobjid
     * @return
     */
    public HashMap<Integer, L1ShopS> getShopSMap(int pcobjid);

    /**
     * 加入托售物件数据
     * 
     * @param key
     * @param item
     * @param shopS
     */
    public void insertItem(final int key, final L1ItemInstance item,
            final L1ShopS shopS);

    /**
     * 资料更新(托售状态)
     * 
     * @param item
     */
    public void updateShopS(final L1ShopS shopS);

    /**
     * 托售物件资料删除
     * 
     * @param key
     */
    public void deleteItem(final int key);

}
