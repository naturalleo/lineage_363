package com.lineage.server.datatables.lock;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.AccountBankTable;
import com.lineage.server.datatables.storage.AccountBankStorage;
import com.lineage.server.templates.L1Bank;

/**
 * 银行帐户资料
 * 
 * @author loli
 */
public class AccountBankReading {

    private final Lock _lock;

    private final AccountBankStorage _storage;

    private static AccountBankReading _instance;

    private AccountBankReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new AccountBankTable();
    }

    public static AccountBankReading get() {
        if (_instance == null) {
            _instance = new AccountBankReading();
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
     * 该帐户资料
     * 
     * @param account_name
     * @return
     */
    public L1Bank get(String account_name) {
        this._lock.lock();
        L1Bank tmp = null;
        try {
            tmp = this._storage.get(account_name);

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    public Map<String, L1Bank> map() {
        this._lock.lock();
        Map<String, L1Bank> tmp = null;
        try {
            tmp = this._storage.map();

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 建立帐号资料
     * 
     * @param loginName
     * @param bank
     */
    public void create(final String loginName, final L1Bank bank) {
        this._lock.lock();
        try {
            this._storage.create(loginName, bank);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 更新密码
     * 
     * @param loginName
     *            帐号
     * @param pwd
     *            密码
     */
    public void updatePass(final String loginName, final String pwd) {
        this._lock.lock();
        try {
            this._storage.updatePass(loginName, pwd);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 更新存款
     * 
     * @param loginName
     *            帐号
     * @param adena
     *            金额
     */
    public void updateAdena(final String loginName, final long adena) {
        this._lock.lock();
        try {
            this._storage.updateAdena(loginName, adena);

        } finally {
            this._lock.unlock();
        }
    }
}
