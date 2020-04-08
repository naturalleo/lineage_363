package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.ServerCnInfoTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Item;

/**
 * 天宝购买纪录
 */
public class ServerCnInfoReading {

    private final Lock _lock;

    private final ServerCnInfoTable _storage;

    private static ServerCnInfoReading _instance;

    private ServerCnInfoReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new ServerCnInfoTable();
    }

    public static ServerCnInfoReading get() {
        if (_instance == null) {
            _instance = new ServerCnInfoReading();
        }
        return _instance;
    }

    /**
     * 资料预先载入
     */
    public void create(final L1PcInstance pc, final L1Item itemtmp,
            final int count) {
        this._lock.lock();
        try {
            this._storage.create(pc, itemtmp, count);

        } finally {
            this._lock.unlock();
        }
    }
}
