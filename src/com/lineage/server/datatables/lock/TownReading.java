package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.TownTable;
import com.lineage.server.datatables.storage.TownStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Town;

/**
 * 村庄资料
 * 
 * @author dexc
 * 
 */
public class TownReading {

    private final Lock _lock;

    private final TownStorage _storage;

    private static TownReading _instance;

    private TownReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new TownTable();
    }

    public static TownReading get() {
        if (_instance == null) {
            _instance = new TownReading();
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
     * 传回村庄阵列资料
     * 
     * @return
     */
    public L1Town[] getTownTableList() {
        this._lock.lock();
        L1Town[] tmp;
        try {
            tmp = this._storage.getTownTableList();
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 传回指定村庄资料
     * 
     * @param id
     * @return
     */
    public L1Town getTownTable(final int id) {
        this._lock.lock();
        L1Town tmp;
        try {
            tmp = this._storage.getTownTable(id);
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 检查是否为村长
     * 
     * @param pc
     * @param town_id
     * @return
     */
    public boolean isLeader(final L1PcInstance pc, final int town_id) {
        this._lock.lock();
        boolean tmp;
        try {
            tmp = this._storage.isLeader(pc, town_id);
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 更新村庄收入
     * 
     * @param town_id
     * @param salesMoney
     */
    public void addSalesMoney(final int town_id, final int salesMoney) {
        this._lock.lock();
        try {
            this._storage.addSalesMoney(town_id, salesMoney);
        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 更新村庄税率
     */
    public void updateTaxRate() {
        this._lock.lock();
        try {
            this._storage.updateTaxRate();
        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 更新收入资讯
     */
    public void updateSalesMoneyYesterday() {
        this._lock.lock();
        try {
            this._storage.updateSalesMoneyYesterday();
        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 
     * @param townId
     * @return
     */
    public String totalContribution(final int townId) {
        this._lock.lock();
        String tmp;
        try {
            tmp = this._storage.totalContribution(townId);
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
	 *
	 */
    public void clearHomeTownID() {
        this._lock.lock();
        try {
            this._storage.clearHomeTownID();
        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 领取报酬
     * 
     * @return 报酬
     */
    public int getPay(final int objid) {
        this._lock.lock();
        final int tmp = 0;
        try {
            this._storage.getPay(objid);
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }
}
