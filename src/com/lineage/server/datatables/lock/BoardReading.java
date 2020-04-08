package com.lineage.server.datatables.lock;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.BoardTable;
import com.lineage.server.datatables.storage.BoardStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Board;

/**
 * 布告栏资料
 * 
 * @author dexc
 * 
 */
public class BoardReading {

    private final Lock _lock;

    private final BoardStorage _storage;

    private static BoardReading _instance;

    private BoardReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new BoardTable();
    }

    public static BoardReading get() {
        if (_instance == null) {
            _instance = new BoardReading();
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
     * 传回公告MAP
     */
    public Map<Integer, L1Board> getBoardMap() {
        this._lock.lock();
        Map<Integer, L1Board> tmp;
        try {
            tmp = this._storage.getBoardMap();
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 传回公告阵列
     */
    public L1Board[] getBoardTableList() {
        this._lock.lock();
        L1Board[] tmp;
        try {
            tmp = this._storage.getBoardTableList();
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 传回指定公告
     */
    public L1Board getBoardTable(final int houseId) {
        this._lock.lock();
        L1Board tmp;
        try {
            tmp = this._storage.getBoardTable(houseId);
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 传回已用最大公告编号
     */
    public int getMaxId() {
        this._lock.lock();
        int tmp = 0;
        try {
            tmp = this._storage.getMaxId();
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 增加布告栏资料
     * 
     * @param pc
     * @param date
     * @param title
     * @param content
     */
    public void writeTopic(final L1PcInstance pc, final String date,
            final String title, final String content) {
        this._lock.lock();
        try {
            this._storage.writeTopic(pc, date, title, content);
        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 删除布告栏资料
     * 
     * @param number
     */
    public void deleteTopic(final int number) {
        this._lock.lock();
        try {
            this._storage.deleteTopic(number);
        } finally {
            this._lock.unlock();
        }
    }

}
