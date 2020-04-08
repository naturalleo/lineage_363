package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.OtherUserBuyTable;
import com.lineage.server.datatables.storage.OtherUserBuyStorage;

/**
 * 买入个人商店物品纪录
 * 
 * @author dexc
 * 
 */
public class OtherUserBuyReading {

    private final Lock _lock;

    private final OtherUserBuyStorage _storage;

    private static OtherUserBuyReading _instance;

    private OtherUserBuyReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new OtherUserBuyTable();
    }

    public static OtherUserBuyReading get() {
        if (_instance == null) {
            _instance = new OtherUserBuyReading();
        }
        return _instance;
    }

    /**
     * 增加纪录
     * 
     * @param itemname
     *            买入物品名称
     * @param itemobjid
     *            买入物品OBJID
     * @param itemadena
     *            单件物品买入金额
     * @param itemcount
     *            买入数量
     * @param pcobjid
     *            买入者OBJID
     * @param pcname
     *            买入者名称
     * @param srcpcobjid
     *            卖出者OBJID(个人商店)
     * @param srcpcname
     *            卖出者名称(个人商店)
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
