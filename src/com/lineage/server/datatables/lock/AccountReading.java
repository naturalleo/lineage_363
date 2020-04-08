package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.AccountTable;
import com.lineage.server.datatables.storage.AccountStorage;
import com.lineage.server.templates.L1Account;

/**
 * 人物帐户资料
 * 
 * @author dexc
 * 
 */
public class AccountReading {

    private final Lock _lock;

    private final AccountStorage _storage;

    private static AccountReading _instance;

    private AccountReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new AccountTable();
    }

    public static AccountReading get() {
        if (_instance == null) {
            _instance = new AccountReading();
        }
        return _instance;
    }

    /**
     * 预先加载帐户名称
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
     * 传回指定帐户名称资料是否存在
     * 
     * @param loginName
     *            帐号名称
     * 
     * @return true:有该帐号 false:没有该帐号
     */
    public boolean isAccountUT(final String loginName) {
        this._lock.lock();
        boolean tmp = false;
        try {
            tmp = this._storage.isAccountUT(loginName);

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 建立帐号资料
     * 
     * @param loginName
     *            帐号
     * @param pwd
     *            密码
     * @param ip
     *            IP位置
     * @param host
     *            MAC位置
     * @param spwd
     *            超级密码
     * @return L1Account
     */
    public L1Account create(final String loginName, final String pwd,
            final String ip, final String host, final String spwd) {
        this._lock.lock();
        L1Account tmp = null;
        try {
            tmp = this._storage.create(loginName, pwd, ip, host, spwd);

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 传回指定帐户资料是否存在
     * 
     * @param loginName
     *            帐号名称
     * 
     * @return true:有该帐号 false:没有该帐号
     */
    public boolean isAccount(final String loginName) {
        this._lock.lock();
        boolean tmp = false;
        try {
            tmp = this._storage.isAccount(loginName);

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 传回指定帐户资料
     * 
     * @param loginName
     *            帐号
     * 
     * @return L1Account
     */
    public L1Account getAccount(final String loginName) {
        this._lock.lock();
        L1Account tmp = null;
        try {
            tmp = this._storage.getAccount(loginName);

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 更新仓库密码
     * 
     * @param loginName
     *            帐号
     * @param pwd
     *            密码
     */
    public void updateWarehouse(final String loginName, final int pwd) {
        this._lock.lock();
        try {
            this._storage.updateWarehouse(loginName, pwd);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 更新上线时间/IP/MAC
     * 
     * @param account
     *            帐户
     */
    public void updateLastActive(final L1Account account) {
        this._lock.lock();
        try {
            this._storage.updateLastActive(account);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 更新帐号可用人物数量
     * 
     * @param loginName
     *            帐号
     * @param count
     *            扩充数量
     */
    public void updateCharacterSlot(final String loginName, final int count) {
        this._lock.lock();
        try {
            this._storage.updateCharacterSlot(loginName, count);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 更新帐号密码
     * 
     * @param loginName
     *            帐号
     * 
     * @param newpwd
     *            新密码
     */
    public void updatePwd(final String loginName, final String newpwd) {
        this._lock.lock();
        try {
            this._storage.updatePwd(loginName, newpwd);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 更新帐号连线状态
     * 
     * @param loginName
     *            帐号
     * @param islan
     *            是否连线
     */
    public void updateLan(final String loginName, final boolean islan) {
        this._lock.lock();
        try {
            this._storage.updateLan(loginName, islan);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 更新帐号连线状态(全部离线)
     */
    public void updateLan() {
        this._lock.lock();
        try {
            this._storage.updateLan();

        } finally {
            this._lock.unlock();
        }
    }
    
    /**
     * 更新消费计时卡数 hjx1000
     * @param loginName
     * 			账号
     * @param card_cou
     * 			/时间
     */
	public void updatecard_fee(String loginName, int card_cou) {//hjx1000
        this._lock.lock();
        try {
            this._storage.updatecard_fee(loginName, card_cou);

        } finally {
            this._lock.unlock();
        }
	}
}
