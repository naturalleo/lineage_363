package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.CharacterConfigTable;
import com.lineage.server.datatables.storage.CharacterConfigStorage;
import com.lineage.server.templates.L1Config;

/**
 * 快速键纪录
 * 
 * @author dexc
 * 
 */
public class CharacterConfigReading {

    private final Lock _lock;

    private final CharacterConfigStorage _storage;

    private static CharacterConfigReading _instance;

    private CharacterConfigReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new CharacterConfigTable();
    }

    public static CharacterConfigReading get() {
        if (_instance == null) {
            _instance = new CharacterConfigReading();
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
     * 传回 L1Config
     */
    public L1Config get(final int objectId) {
        this._lock.lock();
        L1Config tmp;
        try {
            tmp = this._storage.get(objectId);
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 新建 L1Config
     */
    public void storeCharacterConfig(final int objectId, final int length,
            final byte[] data) {
        this._lock.lock();
        try {
            this._storage.storeCharacterConfig(objectId, length, data);
        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 更新 L1Config
     */
    public void updateCharacterConfig(final int objectId, final int length,
            final byte[] data) {
        this._lock.lock();
        try {
            this._storage.updateCharacterConfig(objectId, length, data);
        } finally {
            this._lock.unlock();
        }
    }
}
