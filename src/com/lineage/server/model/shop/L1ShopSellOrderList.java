package com.lineage.server.model.shop;

import java.util.ArrayList;
import java.util.List;

import com.lineage.server.model.Instance.L1PcInstance;

class L1ShopSellOrder {

    private final L1AssessedItem _item;

    private final int _count;

    public L1ShopSellOrder(final L1AssessedItem item, final int count) {
        this._item = item;
        this._count = Math.max(0, count);
    }

    public L1AssessedItem getItem() {
        return this._item;
    }

    public int getCount() {
        return this._count;
    }

}

public class L1ShopSellOrderList {
    private final L1Shop _shop;
    private final L1PcInstance _pc;
    private final List<L1ShopSellOrder> _list = new ArrayList<L1ShopSellOrder>();

    L1ShopSellOrderList(final L1Shop shop, final L1PcInstance pc) {
        this._shop = shop;
        this._pc = pc;
    }

    public void add(final int itemObjectId, final int count) {
        final L1AssessedItem assessedItem = this._shop.assessItem(this._pc
                .getInventory().getItem(itemObjectId));
        if (assessedItem == null) {
            /*
             * 买取リストに无いアイテムが指定された。 不正パケの可能性。
             */
            throw new IllegalArgumentException();
        }

        this._list.add(new L1ShopSellOrder(assessedItem, count));
    }

    L1PcInstance getPc() {
        return this._pc;
    }

    List<L1ShopSellOrder> getList() {
        return this._list;
    }
}
