package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.CharShiftingTable;
import com.lineage.server.datatables.storage.CharShiftingStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Item;

/**
 * 装备移转纪录资料
 * 
 * @author dexc
 * 
 */
public class CharShiftingReading {

    private final Lock _lock;

    private final CharShiftingStorage _storage;

    private static CharShiftingReading _instance;

    private CharShiftingReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new CharShiftingTable();
    }

    public static CharShiftingReading get() {
        if (_instance == null) {
            _instance = new CharShiftingReading();
        }
        return _instance;
    }

    /**
     * 增加装备移转纪录资料
     * 
     * @param pc
     *            执行人物
     * @param tgId
     *            目标objid
     * @param tgName
     *            目标名称
     * @param srcObjid
     *            原始objid
     * @param srcItem
     *            原始物件
     * @param newItem
     *            新物件
     * @param mode
     *            模式<BR>
     *            0: 交换装备<BR>
     *            1: 装备升级<BR>
     *            2: 转移装备<BR>
     */
    public void newShifting(final L1PcInstance pc, final int tgId,
            final String tgName, final int srcObjid, final L1Item srcItem,
            final L1ItemInstance newItem, final int mode) {
        this._lock.lock();
        try {
            this._storage.newShifting(pc, tgId, tgName, srcObjid, srcItem,
                    newItem, mode);

        } finally {
            this._lock.unlock();
        }
    }

}
