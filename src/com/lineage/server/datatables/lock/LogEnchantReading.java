package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.LogEnchantTable;
import com.lineage.server.datatables.storage.LogEnchantStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 强化纪录
 * 
 * @author dexc
 * 
 */
public class LogEnchantReading {

    private final Lock _lock;

    private final LogEnchantStorage _storage;

    private static LogEnchantReading _instance;

    private LogEnchantReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new LogEnchantTable();
    }

    public static LogEnchantReading get() {
        if (_instance == null) {
            _instance = new LogEnchantReading();
        }
        return _instance;
    }

    /**
     * 强化纪录(失败)
     * 
     * @param pc
     * @param item
     */
    public void failureEnchant(final L1PcInstance pc, final L1ItemInstance item) {
        this._lock.lock();
        try {
            this._storage.failureEnchant(pc, item);
        } finally {
            this._lock.unlock();
        }
    }
}
