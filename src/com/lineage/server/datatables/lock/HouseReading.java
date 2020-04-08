package com.lineage.server.datatables.lock;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.HouseTable;
import com.lineage.server.datatables.storage.HouseStorage;
import com.lineage.server.templates.L1House;

/**
 * 盟屋资料
 * 
 * @author dexc
 * 
 */
public class HouseReading {

    private final Lock _lock;

    private final HouseStorage _storage;

    private static HouseReading _instance;

    private HouseReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new HouseTable();
    }

    public static HouseReading get() {
        if (_instance == null) {
            _instance = new HouseReading();
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
     * 传回小屋列表
     * 
     * @return
     */
    public Map<Integer, L1House> getHouseTableList() {
        this._lock.lock();
        Map<Integer, L1House> tmp;
        try {
            tmp = this._storage.getHouseTableList();
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 传回指定小屋资料
     * 
     * @param houseId
     * @return
     */
    public L1House getHouseTable(final int houseId) {
        this._lock.lock();
        L1House tmp;
        try {
            tmp = this._storage.getHouseTable(houseId);
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 更新小屋资料
     * 
     * @param house
     */
    public void updateHouse(final L1House house) {
        this._lock.lock();
        try {
            this._storage.updateHouse(house);
        } finally {
            this._lock.unlock();
        }
    }
}
