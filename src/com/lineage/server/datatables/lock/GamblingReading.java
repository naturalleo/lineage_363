package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.GamblingTable;
import com.lineage.server.datatables.storage.GamblingStorage;
import com.lineage.server.templates.L1Gambling;

/**
 * 赌场纪录
 * 
 * @author dexc
 * 
 */
public class GamblingReading {

    private final Lock _lock;

    private final GamblingStorage _storage;

    private static GamblingReading _instance;

    private GamblingReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new GamblingTable();
    }

    public static GamblingReading get() {
        if (_instance == null) {
            _instance = new GamblingReading();
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
     * 传回赌场纪录(获胜NPC票号)
     * 
     * @return
     */
    public L1Gambling getGambling(final String key) {
        this._lock.lock();
        L1Gambling tmp;
        try {
            tmp = this._storage.getGambling(key);

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 传回赌场纪录(场次编号)
     * 
     * @return
     */
    public L1Gambling getGambling(final int key) {
        this._lock.lock();
        L1Gambling tmp;
        try {
            tmp = this._storage.getGambling(key);

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 增加赌场纪录
     */
    public void add(final L1Gambling gambling) {
        this._lock.lock();
        try {
            this._storage.add(gambling);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 更新赌场纪录
     */
    public void updateGambling(final int id, final int outcount) {
        this._lock.lock();
        try {
            this._storage.updateGambling(id, outcount);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 传回场次数量 与获胜次数
     * 
     * @param npcid
     * @return
     */
    public int[] winCount(final int npcid) {
        this._lock.lock();
        int[] tmp;
        try {
            tmp = this._storage.winCount(npcid);

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 已用最大ID
     * 
     * @return
     */
    public int maxId() {
        this._lock.lock();
        int tmp;
        try {
            tmp = this._storage.maxId();

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }
}
