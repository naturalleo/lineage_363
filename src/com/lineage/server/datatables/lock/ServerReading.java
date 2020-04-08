package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.ServerTable;
import com.lineage.server.datatables.storage.ServerStorage;

/**
 * 服务器资料
 * 
 * @author dexc
 * 
 */
public class ServerReading {

    private final Lock _lock;

    private final ServerStorage _storage;

    private static ServerReading _instance;

    private ServerReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new ServerTable();
    }

    public static ServerReading get() {
        if (_instance == null) {
            _instance = new ServerReading();
        }
        return _instance;
    }

    /**
     * 预先加载服务器存档资料
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
     * 传回服务器最小编号设置
     */
    public int minId() {
        this._lock.lock();
        int temp = 0;
        try {
            temp = this._storage.minId();

        } finally {
            this._lock.unlock();
        }
        return temp;
    }

    /**
     * 传回服务器最大编号设置
     */
    public int maxId() {
        this._lock.lock();
        int temp = 0;
        try {
            temp = this._storage.maxId();

        } finally {
            this._lock.unlock();
        }
        return temp;
    }

    /**
     * 设定服务器关机<BR>
     * 同时记录已用最大编号
     */
    public void isStop() {
        this._lock.lock();
        try {
            this._storage.isStop();

        } finally {
            this._lock.unlock();
        }
    }
}
