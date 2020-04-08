package com.lineage.server.datatables.lock;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.CastleTable;
import com.lineage.server.datatables.storage.CastleStorage;
import com.lineage.server.templates.L1Castle;

/**
 * 城堡资料
 * 
 * @author dexc
 * 
 */
public class CastleReading {

    private final Lock _lock;

    private final CastleStorage _storage;

    private static CastleReading _instance;

    private CastleReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new CastleTable();
    }

    public static CastleReading get() {
        if (_instance == null) {
            _instance = new CastleReading();
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
     * 城堡MAP
     * 
     * @return
     */
    public Map<Integer, L1Castle> getCastleMap() {
        this._lock.lock();
        Map<Integer, L1Castle> tmp;
        try {
            tmp = this._storage.getCastleMap();
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 城堡阵列
     * 
     * @return
     */
    public L1Castle[] getCastleTableList() {
        this._lock.lock();
        L1Castle[] tmp;
        try {
            tmp = this._storage.getCastleTableList();
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 指定城堡资料
     * 
     * @param id
     * @return
     */
    public L1Castle getCastleTable(final int id) {
        this._lock.lock();
        L1Castle tmp;
        try {
            tmp = this._storage.getCastleTable(id);
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 更新城堡指定资料
     * 
     * @param castle
     */
    public void updateCastle(final L1Castle castle) {
        this._lock.lock();
        try {
            this._storage.updateCastle(castle);
        } finally {
            this._lock.unlock();
        }
    }
}
