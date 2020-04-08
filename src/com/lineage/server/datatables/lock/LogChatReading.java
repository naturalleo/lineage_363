package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.LogChatTable;
import com.lineage.server.datatables.storage.LogChatStorage;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 对话纪录
 * 
 * @author dexc
 * 
 */
public class LogChatReading {

    private final Lock _lock;

    private final LogChatStorage _storage;

    private static LogChatReading _instance;

    private LogChatReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new LogChatTable();
    }

    public static LogChatReading get() {
        if (_instance == null) {
            _instance = new LogChatReading();
        }
        return _instance;
    }

    /**
     * 具有传送对象
     * 
     * @param pc
     * @param target
     * @param text
     * @param type
     */
    public void isTarget(final L1PcInstance pc, final L1PcInstance target,
            final String text, final int type) {
        this._lock.lock();
        try {
            this._storage.isTarget(pc, target, text, type);

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 无传送对象
     * 
     * @param pc
     * @param text
     * @param type
     */
    public void noTarget(final L1PcInstance pc, final String text,
            final int type) {
        this._lock.lock();
        try {
            this._storage.noTarget(pc, text, type);

        } finally {
            this._lock.unlock();
        }
    }

}
