package com.lineage.server.datatables.lock;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.DwarfForClanTable;
import com.lineage.server.datatables.storage.DwarfForClanStorage;
import com.lineage.server.model.Instance.L1ItemInstance;

/**
 * 血盟仓库数据
 * 
 * @author dexc
 * 
 */
public class DwarfForClanReading {

    private final Lock _lock;

    private final DwarfForClanStorage _storage;

    private static DwarfForClanReading _instance;

    private DwarfForClanReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new DwarfForClanTable();
    }

    public static DwarfForClanReading get() {
        if (_instance == null) {
            _instance = new DwarfForClanReading();
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

    /**
     * 传回血盟仓库数据
     * 
     * @return
     */
    public CopyOnWriteArrayList<L1ItemInstance> loadItems(final String clan_name) {
        this._lock.lock();
        CopyOnWriteArrayList<L1ItemInstance> tmp = null;
        try {
            tmp = this._storage.loadItems(clan_name);

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 删除血盟仓库资料(完整)
     * 
     * @param account_name
     */
    public void delUserItems(final String clan_name) {
        this._lock.lock();
        try {
            this._storage.delUserItems(clan_name);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 加入血盟仓库数据
     */
    public void insertItem(final String clan_name, final L1ItemInstance item) {
        this._lock.lock();
        try {
            this._storage.insertItem(clan_name, item);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 血盟仓库资料更新(物品数量)
     * 
     * @param item
     */
    public void updateItem(final L1ItemInstance item) {
        this._lock.lock();
        try {
            this._storage.updateItem(item);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 血盟仓库物品资料删除
     * 
     * @param account_name
     * @param item
     */
    public void deleteItem(final String clan_name, final L1ItemInstance item) {
        this._lock.lock();
        try {
            this._storage.deleteItem(clan_name, item);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 该血盟仓库是否有指定数据
     * 
     * @param clan_name
     * @param objid
     * @param count
     * @return
     */
    public boolean getUserItems(final String clan_name, final int objid,
            int count) {
        this._lock.lock();
        boolean tmp = false;
        try {
            tmp = this._storage.getUserItems(clan_name, objid, count);

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }
}
