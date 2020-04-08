package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.CharItemsPowerTable;
import com.lineage.server.datatables.storage.CharItemsPowerStorage;
import com.lineage.server.templates.L1ItemPower_name;

/**
 * 人物物品凹槽资料
 * 
 * @author dexc
 */
public class CharItemPowerReading {

    private final Lock _lock;

    private final CharItemsPowerStorage _storage;

    private static CharItemPowerReading _instance;

    private CharItemPowerReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new CharItemsPowerTable();
    }

    public static CharItemPowerReading get() {
        if (_instance == null) {
            _instance = new CharItemPowerReading();
        }
        return _instance;
    }

    /**
     * 初始化载入
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
     * 增加物品凹槽资料
     * 
     * @param objId
     * @param power
     */
    public void storeItem(final int objId, final L1ItemPower_name power) {
        this._lock.lock();
        try {
            this._storage.storeItem(objId, power);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 更新凹槽资料
     * 
     * @param item_obj_id
     * @param power
     */
    public void updateItem(final int item_obj_id, final L1ItemPower_name power) {
        this._lock.lock();
        try {
            this._storage.updateItem(item_obj_id, power);

        } finally {
            this._lock.unlock();
        }
    }
    
    /**
     * 移除凹槽资料
     * 
     * @param item_obj_id
     */
    public void removeItemPower(final int item_obj_id) {
        this._lock.lock();
        try {
            this._storage.removeItemPower(item_obj_id);

        } finally {
            this._lock.unlock();
        }
    }
}
