package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.OtherUserTradeTable;
import com.lineage.server.datatables.storage.OtherUserTradeStorage;

/**
 * 个人交易物品纪录
 * 
 * @author dexc
 * 
 */
public class OtherUserTitleReading {

    private final Lock _lock;

    private final OtherUserTradeStorage _storage;

    private static OtherUserTitleReading _instance;

    private OtherUserTitleReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new OtherUserTradeTable();
    }

    public static OtherUserTitleReading get() {
        if (_instance == null) {
            _instance = new OtherUserTitleReading();
        }
        return _instance;
    }

    /**
     * 增加纪录
     * 
     * @param itemname
     *            物品名称
     * @param itemobjid
     *            物品OBJID
     * @param itemadena
     *            0 暂无用途
     * @param itemcount
     *            数量
     * @param pcobjid
     *            移入人物OBJID
     * @param pcname
     *            移入人物名称
     * @param srcpcobjid
     *            移出人物者OBJID
     * @param srcpcname
     *            移出人物名称
     */
    public void add(final String itemname, final int itemobjid,
            final int itemadena, final long itemcount, final int pcobjid,
            final String pcname, final int srcpcobjid, final String srcpcname) {
        this._lock.lock();
        try {
            this._storage.add(itemname, itemobjid, itemadena, itemcount,
                    pcobjid, pcname, srcpcobjid, srcpcname);

        } finally {
            this._lock.unlock();
        }
    }
}
