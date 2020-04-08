package com.lineage.server.datatables.lock;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.CharItemsTable;
import com.lineage.server.datatables.storage.CharItemsStorage;
import com.lineage.server.model.Instance.L1ItemInstance;

/**
 * 人物背包资料
 * 
 * @author dexc
 * 
 */
public class CharItemsReading {

    private final Lock _lock;

    private final CharItemsStorage _storage;

    private static CharItemsReading _instance;

    private CharItemsReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new CharItemsTable();
    }

    public static CharItemsReading get() {
        if (_instance == null) {
            _instance = new CharItemsReading();
        }
        return _instance;
    }

    /**
     * 资料预先载入
     */
    public void load() {
        _lock.lock();
        try {
            _storage.load();

        } finally {
            _lock.unlock();
        }
    }

    /**
     * 传回该人物背包资料
     * 
     * @param objid
     * @return
     */
    public CopyOnWriteArrayList<L1ItemInstance> loadItems(final Integer objid) {
        _lock.lock();
        CopyOnWriteArrayList<L1ItemInstance> tmp = null;
        try {
            tmp = _storage.loadItems(objid);

        } finally {
            _lock.unlock();
        }
        return tmp;
    }

    /**
     * 删除人物背包资料(完整)
     * 
     * @param objid
     */
    public void delUserItems(final Integer objid) {
        _lock.lock();
        try {
            _storage.delUserItems(objid);

        } finally {
            _lock.unlock();
        }
    }

    /**
     * 删除指定编号全部数据
     * 
     * @param itemid
     */
    public void del_item(final int itemid) {
        _lock.lock();
        try {
            _storage.del_item(itemid);

        } finally {
            _lock.unlock();
        }
    }

    /**
     * 增加背包物品
     * 
     * @param objId
     * @param item
     * @throws Exception
     */
    public void storeItem(final int objId, final L1ItemInstance item)
            throws Exception {
        _lock.lock();
        try {
            _storage.storeItem(objId, item);

        } finally {
            _lock.unlock();
        }
    }

    /**
     * 删除背包物品
     * 
     * @param objid
     *            人物OBJID
     * @param item
     *            物品
     * 
     * @throws Exception
     */
    public void deleteItem(final int objid, final L1ItemInstance item)
            throws Exception {
        _lock.lock();
        try {
            _storage.deleteItem(objid, item);

        } finally {
            _lock.unlock();
        }
    }

    /**
     * 更新物品ITEMID 与中文名称
     * 
     * @param item
     */
    public void updateItemId_Name(final L1ItemInstance item) throws Exception {
        _lock.lock();
        try {
            _storage.updateItemId_Name(item);

        } finally {
            _lock.unlock();
        }
    }

    /**
     * 更新ITEMID
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemId(final L1ItemInstance item) throws Exception {
        _lock.lock();
        try {
            _storage.updateItemId(item);

        } finally {
            _lock.unlock();
        }
    }

    /**
     * 更新数量
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemCount(final L1ItemInstance item) throws Exception {
        _lock.lock();
        try {
            _storage.updateItemCount(item);

        } finally {
            _lock.unlock();
        }
    }

    /**
     * 更新损坏度
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemDurability(final L1ItemInstance item)
            throws Exception {
        _lock.lock();
        try {
            _storage.updateItemDurability(item);

        } finally {
            _lock.unlock();
        }
    }

    /**
     * 更新可用次数
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemChargeCount(final L1ItemInstance item)
            throws Exception {
        _lock.lock();
        try {
            _storage.updateItemChargeCount(item);

        } finally {
            _lock.unlock();
        }
    }

    /**
     * 更新可用时间
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemRemainingTime(final L1ItemInstance item)
            throws Exception {
        _lock.lock();
        try {
            _storage.updateItemRemainingTime(item);

        } finally {
            _lock.unlock();
        }
    }

    /**
     * 更新强化度
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemEnchantLevel(final L1ItemInstance item)
            throws Exception {
        _lock.lock();
        try {
            _storage.updateItemEnchantLevel(item);

        } finally {
            _lock.unlock();
        }
    }

    /**
     * 更新使用状态
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemEquipped(final L1ItemInstance item) throws Exception {
        _lock.lock();
        try {
            _storage.updateItemEquipped(item);

        } finally {
            _lock.unlock();
        }
    }

    /**
     * 更新鉴定状态
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemIdentified(final L1ItemInstance item)
            throws Exception {
        _lock.lock();
        try {
            _storage.updateItemIdentified(item);

        } finally {
            _lock.unlock();
        }
    }

    /**
     * 更新祝福状态
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemBless(final L1ItemInstance item) throws Exception {
        _lock.lock();
        try {
            _storage.updateItemBless(item);

        } finally {
            _lock.unlock();
        }
    }

    /**
     * 更新强化属性
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemAttrEnchantKind(final L1ItemInstance item)
            throws Exception {
        _lock.lock();
        try {
            _storage.updateItemAttrEnchantKind(item);

        } finally {
            _lock.unlock();
        }
    }

    /**
     * 更新强化属性强化度
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemAttrEnchantLevel(final L1ItemInstance item)
            throws Exception {
        _lock.lock();
        try {
            _storage.updateItemAttrEnchantLevel(item);

        } finally {
            _lock.unlock();
        }
    }

    /**
     * 更新最后使用时间
     * 
     * @param item
     * @throws Exception
     */
    public void updateItemDelayEffect(final L1ItemInstance item)
            throws Exception {
        _lock.lock();
        try {
            _storage.updateItemDelayEffect(item);

        } finally {
            _lock.unlock();
        }
    }

    /**
     * 传回对应所有物品数量
     * 
     * @param objId
     * @return
     * @throws Exception
     */
    public int getItemCount(final int objId) throws Exception {
        _lock.lock();
        int tmp = 0;
        try {
            tmp = _storage.getItemCount(objId);

        } finally {
            _lock.unlock();
        }
        return tmp;
    }

    /**
     * 给予金币(对离线人物)
     * 
     * @param objId
     * @param count
     * @throws Exception
     */
    public void getAdenaCount(final int objId, final long count)
            throws Exception {
        _lock.lock();
        try {
            _storage.getAdenaCount(objId, count);

        } finally {
            _lock.unlock();
        }
    }

    /**
     * 该人物背包是否有指定数据
     * 
     * @param pcObjId
     * @param objid
     * @param count
     * @return
     */
    public boolean getUserItems(final int pcObjId, final int objid, long count) {
        _lock.lock();
        boolean tmp = false;
        try {
            tmp = _storage.getUserItems(pcObjId, objid, count);

        } finally {
            _lock.unlock();
        }
        return tmp;
    }

    /**
     * 是否有指定数据
     * 
     * @param pcObjid
     * @param objid
     * @param count
     * @return
     */
    public boolean getUserItem(final int objid) {
        _lock.lock();
        boolean tmp = false;
        try {
            tmp = _storage.getUserItem(objid);

        } finally {
            _lock.unlock();
        }
        return tmp;
    }

    /**
     * 传回佣有该物品ID的人物清单<BR>
     * (适用该物品每人只能佣有一个的状态)
     * 
     * @param itemid
     * @return
     */
    public Map<Integer, L1ItemInstance> getUserItems(int itemid) {
        _lock.lock();
        Map<Integer, L1ItemInstance> tmp = null;
        try {
            tmp = _storage.getUserItems(itemid);

        } finally {
            _lock.unlock();
        }
        return tmp;
    }
}
