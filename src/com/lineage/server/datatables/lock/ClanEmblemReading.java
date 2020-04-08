package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.ClanEmblemTable;
import com.lineage.server.datatables.storage.ClanEmblemStorage;
import com.lineage.server.templates.L1EmblemIcon;

/**
 * 盟辉图档纪录
 * 
 * @author dexc
 * 
 */
public class ClanEmblemReading {

    private final Lock _lock;

    private final ClanEmblemStorage _storage;

    private static ClanEmblemReading _instance;

    private ClanEmblemReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new ClanEmblemTable();
    }

    public static ClanEmblemReading get() {
        if (_instance == null) {
            _instance = new ClanEmblemReading();
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
     * 传回 Clan Icon
     */
    public L1EmblemIcon get(final int clan_id) {
        this._lock.lock();
        L1EmblemIcon tmp;
        try {
            tmp = this._storage.get(clan_id);

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 增加虚拟血盟盟辉
     * 
     * @param clan_id
     * @param icon
     */
    public void add(final int clan_id, final byte[] icon) {
        this._lock.lock();
        try {
            this._storage.add(clan_id, icon);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 删除盟辉资料
     * 
     * @param clan_id
     */
    public void deleteIcon(final int clan_id) {
        this._lock.lock();
        try {
            this._storage.deleteIcon(clan_id);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 新建 ICON
     */
    public L1EmblemIcon storeClanIcon(final int clan_id, final byte[] emblemicon) {
        this._lock.lock();
        L1EmblemIcon tmp;
        try {
            tmp = this._storage.storeClanIcon(clan_id, emblemicon);

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 更新 ICON
     */
    public void updateClanIcon(final L1EmblemIcon emblemIcon) {
        this._lock.lock();
        try {
            this._storage.updateClanIcon(emblemIcon);

        } finally {
            this._lock.unlock();
        }
    }
}
