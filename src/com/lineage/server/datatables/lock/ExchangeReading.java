package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.ExchangeTable;
import com.lineage.server.datatables.storage.ExchangeStorage;
import com.lineage.server.templates.L1Exchange;

/**
 * 金币元宝兑换
 * 
 * @author hjx1000
 */
public class ExchangeReading {

    private final Lock _lock;

    private final ExchangeStorage _storage;

    private static ExchangeReading _instance;

    private ExchangeReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new ExchangeTable();
    }

    public static ExchangeReading get() {
        if (_instance == null) {
            _instance = new ExchangeReading();
        }
        return _instance;
    }

    /**
     * 预先加载
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
     * 指定NPC兑换资料
     * 
     * @param id
     * @return
     */
    public L1Exchange getExchangeTable(final int id) {
        this._lock.lock();
        L1Exchange tmp;
        try {
            tmp = this._storage.getExchangeTable(id);
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 更新比例
     * 
     * @param npcid
     *            npc
     * @param adena
     *            金额
     */
    public void updateAdena(final int npcid, final int adena) {
        this._lock.lock();
        try {
            this._storage.updateAdena(npcid, adena);

        } finally {
            this._lock.unlock();
        }
    }
}
