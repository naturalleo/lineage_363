package com.lineage.server.datatables.lock;

import java.sql.Timestamp;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.CharItemsTimeTable;
import com.lineage.server.datatables.storage.CharItemsTimeStorage;

/**
 * 人物背包物品使用期限资料
 * 
 * @author dexc
 * 
 */
public class CharItemsTimeReading {

    private final Lock _lock;

    private final CharItemsTimeStorage _storage;

    private static CharItemsTimeReading _instance;

    private CharItemsTimeReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new CharItemsTimeTable();
    }

    public static CharItemsTimeReading get() {
        if (_instance == null) {
            _instance = new CharItemsTimeReading();
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
     * 增加物品使用期限记录
     * 
     * @param objid
     * @return
     */
    public void addTime(final int itemr_obj_id, final Timestamp usertime) {
        this._lock.lock();
        try {
            this._storage.addTime(itemr_obj_id, usertime);

        } finally {
            this._lock.unlock();
        }
    }
}
