package com.lineage.server;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * NPC OBJID分配器
 * 
 * @author dexc
 * 
 */
public class IdFactoryNpc {

    private static final Log _log = LogFactory.getLog(IdFactoryNpc.class);

    private static IdFactoryNpc _instance;

    // 同步层次
    private Object _monitor;

    private AtomicInteger _nextId;

    public static IdFactoryNpc get() {
        if (_instance == null) {
            _instance = new IdFactoryNpc();
        }
        return _instance;
    }

    public IdFactoryNpc() {
        try {
            _monitor = new Object();
            _nextId = new AtomicInteger(0x77359400);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 以原子方式将当前值加 1
     * 
     * @return
     */
    public int nextId() {
        synchronized (_monitor) {
            return _nextId.getAndIncrement();
        }
    }

    /**
     * 获取当前值
     * 
     * @return
     */
    public int maxId() {
        synchronized (_monitor) {
            return _nextId.get();
        }
    }
}
