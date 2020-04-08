package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.OtherTransferTable;

/**
 * 物品转移记录
 * 
 * @author hjx1000
 * 
 */
public class OtherTransferReading {

    private final Lock _lock;

    private final OtherTransferTable _storage;

    private static OtherTransferReading _instance;

    private OtherTransferReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new OtherTransferTable();
    }

    public static OtherTransferReading get() {
        if (_instance == null) {
            _instance = new OtherTransferReading();
        }
        return _instance;
    }

    /**
     * 增加纪录
     * 
     * @param itemname
     *            转移物品名称
     * @param itemobjid
     *            转移物品OBJID
     * @param itemId
     *            物品ID
     * @param pcobjid
     *            转移者OBJID
     * @param pcname
     *            转移者名称
     * @param srcpcobjid
     *            来源者OBJID
     * @param srcpcname
     *            来源者名称
     * @param 来源 string           
     */
    public void add(final String itemname, final int itemobjid,
            final long itemId, final int pcobjid,
            final String pcname, final int srcpcobjid, final String srcpcname
            , final String src) {
        this._lock.lock();
        try {
            this._storage.add(itemname, itemobjid, itemId,
                    pcobjid, pcname, srcpcobjid, srcpcname, src);

        } finally {
            this._lock.unlock();
        }
    }
}
