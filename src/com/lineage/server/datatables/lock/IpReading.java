package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.IpTable;
import com.lineage.server.datatables.storage.IpStorage;

/**
 * 禁用ip资料
 * 
 * @author dexc
 * 
 */
public class IpReading {

    private final Lock _lock;

    private final IpStorage _storage;

    private static IpReading _instance;

    private IpReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new IpTable();
    }

    public static IpReading get() {
        if (_instance == null) {
            _instance = new IpReading();
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
     * 加入禁止位置
     * 
     * @param ip
     * @param info
     *            原因
     */
    public void add(final String ip, final String info) {
        this._lock.lock();
        try {
            this._storage.add(ip, info);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 移出禁止位置
     * 
     * @param ip
     * @return
     */
    public void remove(final String ip) {
        this._lock.lock();
        try {
            this._storage.remove(ip);
        } finally {
            this._lock.unlock();
        }
    }
}
