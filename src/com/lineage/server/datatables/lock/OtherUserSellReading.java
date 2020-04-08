package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.OtherUserSellTable;
import com.lineage.server.datatables.storage.OtherUserSellStorage;

/**
 * 卖出物品给个人商店纪录
 * 
 * @author dexc
 * 
 */
public class OtherUserSellReading {

    private final Lock _lock;

    private final OtherUserSellStorage _storage;

    private static OtherUserSellReading _instance;

    private OtherUserSellReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new OtherUserSellTable();
    }

    public static OtherUserSellReading get() {
        if (_instance == null) {
            _instance = new OtherUserSellReading();
        }
        return _instance;
    }

    /**
     * 增加纪录
     * 
     * @param itemname
     *            回收物品名称
     * @param itemobjid
     *            回收物品OBJID
     * @param itemadena
     *            单件物品回收金额
     * @param itemcount
     *            回收数量
     * @param pcobjid
     *            卖出者OBJID
     * @param pcname
     *            卖出者名称
     * @param srcpcobjid
     *            买入者OBJID(个人商店)
     * @param srcpcname
     *            买入者名称(个人商店)
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
