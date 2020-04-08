package com.lineage.server.datatables.lock;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.ClanTable;
import com.lineage.server.datatables.storage.ClanStorage;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 血盟资料
 * 
 * @author dexc
 * 
 */
public class ClanReading {

    private final Lock _lock;

    private final ClanStorage _storage;

    private static ClanReading _instance;

    private ClanReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new ClanTable();
    }

    public static ClanReading get() {
        if (_instance == null) {
            _instance = new ClanReading();
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
     * 加入虚拟血盟
     * 
     * @param integer
     * @param l1Clan
     */
    public void addDeClan(Integer integer, L1Clan clan) {
        this._lock.lock();
        try {
            this._storage.addDeClan(integer, clan);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 建立血盟资料
     * 
     * @param player
     * @param clan_name
     * @return
     */
    public L1Clan createClan(final L1PcInstance player, final String clan_name) {
        this._lock.lock();
        L1Clan tmp = null;
        try {
            tmp = this._storage.createClan(player, clan_name);

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 更新血盟资料
     * 
     * @param clan
     */
    public void updateClan(final L1Clan clan) {
        this._lock.lock();
        try {
            this._storage.updateClan(clan);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 删除血盟资料
     * 
     * @param clan_name
     */
    public void deleteClan(final String clan_name) {
        this._lock.lock();
        try {
            this._storage.deleteClan(clan_name);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 指定血盟资料
     * 
     * @param clan_id
     * @return
     */
    public L1Clan getTemplate(final int clan_id) {
        this._lock.lock();
        L1Clan tmp = null;
        try {
            tmp = this._storage.getTemplate(clan_id);

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 全部血盟资料
     * 
     * @return
     */
    public Map<Integer, L1Clan> get_clans() {
        this._lock.lock();
        Map<Integer, L1Clan> tmp = null;
        try {
            tmp = this._storage.get_clans();

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }
    
    /** 血盟UI hjx1000
    * @param clan
    */
   public void updateClanOnlineMaxUser(L1Clan clan) {
       this._lock.lock();
       try {
         this._storage.updateClanOnlineMaxUser(clan);
       }
       finally {
         this._lock.unlock();
       }
     }
}
