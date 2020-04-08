package com.lineage.server.model;

import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.lock.DwarfReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.world.World;

/**
 * 仓库资料
 * 
 * @author dexc
 * 
 */
public class L1DwarfInventory extends L1Inventory {

    public static final Log _log = LogFactory.getLog(L1DwarfInventory.class);

    private static final long serialVersionUID = 1L;

    private final L1PcInstance _owner;

    public L1DwarfInventory(final L1PcInstance owner) {
        this._owner = owner;
    }

    /**
     * 传回该仓库资料
     */
    @Override
    public void loadItems() {
        try {
            final CopyOnWriteArrayList<L1ItemInstance> items = DwarfReading
                    .get().loadItems(this._owner.getAccountName());

            if (items != null) {
                _items = items;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 加入仓库数据
     */
    @Override
    public void insertItem(final L1ItemInstance item) {
        if (item.getCount() <= 0) {
            return;
        }
        try {
            DwarfReading.get().insertItem(this._owner.getAccountName(), item);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 仓库资料更新(物品数量)
     */
    @Override
    public void updateItem(final L1ItemInstance item) {
        // System.out.println("仓库资料更新(物品数量)");
        try {
            DwarfReading.get().updateItem(item);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 仓库物品资料删除
     */
    @Override
    public void deleteItem(final L1ItemInstance item) {
        // System.out.println("仓库物品资料删除");
        try {
            _items.remove(item);
            DwarfReading.get().deleteItem(this._owner.getAccountName(), item);
            World.get().removeObject(item);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
