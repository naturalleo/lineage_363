package com.lineage.server.datatables.lock;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.SpawnBossTable;
import com.lineage.server.datatables.storage.SpawnBossStorage;
import com.lineage.server.model.L1Spawn;

/**
 * BOSS召唤资料
 * 
 * @author dexc
 * 
 */
public class SpawnBossReading {

    private final Lock _lock;

    private final SpawnBossStorage _storage;

    private static SpawnBossReading _instance;

    private SpawnBossReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new SpawnBossTable();
    }

    public static SpawnBossReading get() {
        if (_instance == null) {
            _instance = new SpawnBossReading();
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
     * 更新资料库 下次召唤时间纪录
     * 
     * @param id
     * @param spawnTime
     */
    public void upDateNextSpawnTime(final int id, final Calendar spawnTime) {
        this._lock.lock();
        try {
            this._storage.upDateNextSpawnTime(id, spawnTime);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * BOSS召唤列表中物件
     * 
     * @param key
     * @return
     */
    public L1Spawn getTemplate(final int key) {
        this._lock.lock();
        L1Spawn tmp = null;
        try {
            tmp = this._storage.getTemplate(key);

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * BOSS召唤列表中物件(NPCID)
     * 
     * @return _bossId
     */
    public List<Integer> bossIds() {
        this._lock.lock();
        List<Integer> tmp = null;
        try {
            tmp = this._storage.bossIds();

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }
}
