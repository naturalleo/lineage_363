package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.UpdateLocTable;
import com.lineage.server.datatables.storage.UpdateLocStorage;

/**
 * 座标更新
 * 
 * @author daien
 * 
 */
public class UpdateLocReading {

    private final Lock _lock;

    private final UpdateLocStorage _storage;

    private static UpdateLocReading _instance;

    private UpdateLocReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new UpdateLocTable();
    }

    public static UpdateLocReading get() {
        if (_instance == null) {
            _instance = new UpdateLocReading();
        }
        return _instance;
    }

    /**
     * 解卡点
     */
    public void setPcLoc(final String accName) {
        this._lock.lock();
        try {
            this._storage.setPcLoc(accName);
        } finally {
            this._lock.unlock();
        }
    }
}
