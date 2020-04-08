package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.CharBuffTable;
import com.lineage.server.datatables.storage.CharBuffStorage;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 保留技能纪录
 * 
 * @author dexc
 * 
 */
public class CharBuffReading {

    private final Lock _lock;

    private final CharBuffStorage _storage;

    private static CharBuffReading _instance;

    private CharBuffReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new CharBuffTable();
    }

    public static CharBuffReading get() {
        if (_instance == null) {
            _instance = new CharBuffReading();
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
     * 增加保留技能纪录
     * 
     * @param pc
     */
    public void saveBuff(final L1PcInstance pc) {
        this._lock.lock();
        try {
            this._storage.saveBuff(pc);
        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 取回保留技能纪录
     * 
     * @param pc
     */
    public void buff(final L1PcInstance pc) {
        this._lock.lock();
        try {
            this._storage.buff(pc);
        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 删除全部保留技能纪录
     * 
     * @param pc
     */
    public void deleteBuff(final L1PcInstance pc) {
        this._lock.lock();
        try {
            this._storage.deleteBuff(pc);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 删除全部保留技能纪录
     * 
     * @param objid
     */
    public void deleteBuff(final int objid) {
        this._lock.lock();
        try {
            this._storage.deleteBuff(objid);
        } finally {
            this._lock.unlock();
        }
    }
}
