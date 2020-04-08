package com.lineage.server.datatables.lock;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.CharBookTable;
import com.lineage.server.datatables.storage.CharBookStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1BookMark;

/**
 * 记忆座标纪录资料
 * 
 * @author dexc
 * 
 */
public class CharBookReading {

    private final Lock _lock;

    private final CharBookStorage _storage;

    private static CharBookReading _instance;

    private CharBookReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new CharBookTable();
    }

    public static CharBookReading get() {
        if (_instance == null) {
            _instance = new CharBookReading();
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
     * 取回保留记忆座标纪录群
     * 
     * @param pc
     */
    public ArrayList<L1BookMark> getBookMarks(final L1PcInstance pc) {
        this._lock.lock();
        ArrayList<L1BookMark> tmp;
        try {
            tmp = this._storage.getBookMarks(pc);
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 取回保留记忆座标纪录
     * 
     * @param pc
     */
    public L1BookMark getBookMark(final L1PcInstance pc, final int i) {
        this._lock.lock();
        L1BookMark tmp;
        try {
            tmp = this._storage.getBookMark(pc, i);
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 删除记忆座标
     * 
     * @param pc
     * @param s
     */
    public void deleteBookmark(final L1PcInstance pc, final String s) {
        this._lock.lock();
        try {
            this._storage.deleteBookmark(pc, s);
        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 增加记忆座标
     * 
     * @param pc
     * @param s
     */
    public void addBookmark(final L1PcInstance pc, final String s) {
        this._lock.lock();
        try {
            this._storage.addBookmark(pc, s);
        } finally {
            this._lock.unlock();
        }
    }
}
