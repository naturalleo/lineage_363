package com.lineage.server.datatables.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.DwarfShopTable;
import com.lineage.server.datatables.storage.DwarfShopStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.templates.L1ShopS;

/**
 * 托售物件数据
 * 
 * @author dexc
 * 
 */
public class DwarfShopReading {

    private final Lock _lock;

    private final DwarfShopStorage _storage;

    private static DwarfShopReading _instance;

    private DwarfShopReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new DwarfShopTable();
    }

    public static DwarfShopReading get() {
        if (_instance == null) {
            _instance = new DwarfShopReading();
        }
        return _instance;
    }

    /**
     * 资料预先载入
     */
    public void load() {
        this._lock.lock();
        try {
            this._storage.load();

        } finally {
            this._lock.unlock();
        }
    }

    public int nextId() {
        this._lock.lock();
        int tmp = 1;
        try {
            tmp += this._storage.get_id();
            this._storage.set_id(tmp);

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 传回全部出售中物件资料数据
     * 
     * @return
     */
    public HashMap<Integer, L1ShopS> allShopS() {
        this._lock.lock();
        HashMap<Integer, L1ShopS> tmp = null;
        try {
            tmp = this._storage.allShopS();

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 传回全部托售物件数据
     * 
     * @return
     */
    public Map<Integer, L1ItemInstance> allItems() {
        this._lock.lock();
        Map<Integer, L1ItemInstance> tmp = null;
        try {
            tmp = this._storage.allItems();

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 传回指定托售物件数据
     * 
     * @param objid
     *            物品OBJID
     * @return
     */
    public L1ShopS getShopS(int objid) {
        this._lock.lock();
        L1ShopS tmp = null;
        try {
            tmp = this._storage.getShopS(objid);

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 指定人物托售纪录
     * 
     * @param pcobjid
     * @return
     */
    public HashMap<Integer, L1ShopS> getShopSMap(int pcobjid) {
        this._lock.lock();
        HashMap<Integer, L1ShopS> tmp = null;
        try {
            tmp = this._storage.getShopSMap(pcobjid);

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 加入托售物件数据
     * 
     * @param key
     * @param item
     * @param shopS
     */
    public void insertItem(final int key, final L1ItemInstance item,
            L1ShopS shopS) {
        this._lock.lock();
        try {
            this._storage.insertItem(key, item, shopS);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 资料更新(托售状态)
     * 
     * @param item
     */
    public void updateShopS(final L1ShopS shopS) {
        this._lock.lock();
        try {
            this._storage.updateShopS(shopS);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 托售物件资料删除
     * 
     * @param key
     */
    public void deleteItem(final int key) {
        this._lock.lock();
        try {
            this._storage.deleteItem(key);

        } finally {
            this._lock.unlock();
        }
    }
}
