package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.CharOtherTable;
import com.lineage.server.datatables.storage.CharOtherStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1PcOther;

/**
 * 额外纪录资料
 * 
 * @author dexc
 * 
 */
public class CharOtherReading {

    private final Lock _lock;

    private final CharOtherStorage _storage;

    private static CharOtherReading _instance;

    private CharOtherReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new CharOtherTable();
    }

    public static CharOtherReading get() {
        if (_instance == null) {
            _instance = new CharOtherReading();
        }
        return _instance;
    }

    public void load() {
        this._lock.lock();
        try {
            this._storage.load();

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 取回保留额外纪录
     * 
     * @param pc
     */
    public L1PcOther getOther(final L1PcInstance pc) {
        this._lock.lock();
        L1PcOther tmp;
        try {
            tmp = this._storage.getOther(pc);

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 增加 或更新保留额外纪录
     * 
     * @param objId
     * @param other
     */
    public void storeOther(final int objId, final L1PcOther other) {
        this._lock.lock();
        try {
            this._storage.storeOther(objId, other);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 归0杀人次数
     */
    public void tam() {
        this._lock.lock();
        try {
            this._storage.tam();

        } finally {
            this._lock.unlock();
        }
    }
}
