package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.ServerGmCommandTable;
import com.lineage.server.datatables.storage.ServerGmCommandStorage;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * GM指令使用纪录
 */
public class ServerGmCommandReading {

    private final Lock _lock;

    private final ServerGmCommandStorage _storage;

    private static ServerGmCommandReading _instance;

    private ServerGmCommandReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new ServerGmCommandTable();
    }

    public static ServerGmCommandReading get() {
        if (_instance == null) {
            _instance = new ServerGmCommandReading();
        }
        return _instance;
    }

    /**
     * GM指令使用纪录
     * 
     * @param pc
     * @param itemtmp
     * @param count
     */
    public void create(final L1PcInstance pc, final String cmd) {
        this._lock.lock();
        try {
            this._storage.create(pc, cmd);

        } finally {
            this._lock.unlock();
        }
    }
}
