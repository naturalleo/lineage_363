package com.lineage.server.templates;

import com.lineage.server.datatables.ItemTable;

/**
 * 天宝商品
 * 
 * @author daien
 * 
 */
public class L1ShopItem {

    private final int _id;// id

    private final int _itemId;// item id

    private final L1Item _item;// item

    private final int _price;// 售价

    private final int _packCount;// 数量

    public L1ShopItem(final int _id, final int itemId, final int price,
            final int packCount) {
        this._id = _id;
        this._itemId = itemId;
        this._item = ItemTable.get().getTemplate(itemId);
        this._price = price;
        this._packCount = packCount;
    }

    public L1ShopItem(final int itemId, final int price, final int packCount) {
        this._id = -1;
        this._itemId = itemId;
        this._item = ItemTable.get().getTemplate(itemId);
        this._price = price;
        this._packCount = packCount;
    }

    public int getId() {
        return this._id;
    }

    public int getItemId() {
        return this._itemId;
    }

    public L1Item getItem() {
        return this._item;
    }

    public int getPrice() {
        return this._price;
    }

    public int getPackCount() {
        return this._packCount;
    }
}
