package com.lineage.server.datatables.lock;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.BuddyTable;
import com.lineage.server.datatables.storage.BuddyStorage;
import com.lineage.server.templates.L1BuddyTmp;

/**
 * 好友清单
 * 
 * @author dexc
 * 
 */
public class BuddyReading {

    private final Lock _lock;

    private final BuddyStorage _storage;

    private static BuddyReading _instance;

    private BuddyReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new BuddyTable();
    }

    public static BuddyReading get() {
        if (_instance == null) {
            _instance = new BuddyReading();
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
     * 取回该人物好友列表
     * 
     * @param pc
     * @return
     */
    public ArrayList<L1BuddyTmp> userBuddy(final int playerobjid) {
        this._lock.lock();
        ArrayList<L1BuddyTmp> tmp;
        try {
            tmp = this._storage.userBuddy(playerobjid);
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 加入好友清单
     * 
     * @param charId
     * @param objId
     * @param name
     */
    public void addBuddy(final int charId, final int objId, final String name) {
        this._lock.lock();
        try {
            this._storage.addBuddy(charId, objId, name);
        } finally {
            this._lock.unlock();
        }

    }

    /**
     * 移出好友清单
     * 
     * @param charId
     * @param buddyName
     */
    public void removeBuddy(final int charId, final String buddyName) {
        this._lock.lock();
        try {
            this._storage.removeBuddy(charId, buddyName);
        } finally {
            this._lock.unlock();
        }

    }
}
