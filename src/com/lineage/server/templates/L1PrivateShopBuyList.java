package com.lineage.server.templates;

/**
 * 回收物品资料
 * 
 * @author admin
 * 
 */
public class L1PrivateShopBuyList {

    public L1PrivateShopBuyList() {
    }

    private int _itemObjectId;

    /**
     * 设置要购买的物品OBJID
     * 
     * @param i
     */
    public void setItemObjectId(final int i) {
        this._itemObjectId = i;
    }

    /**
     * 传回要购买的物品OBJID
     * 
     * @return
     */
    public int getItemObjectId() {
        return this._itemObjectId;
    }

    private int _buyTotalCount; // 预计买入数量

    /**
     * 设置预计买入数量
     * 
     * @param i
     */
    public void setBuyTotalCount(final int i) {
        this._buyTotalCount = i;
    }

    /**
     * 传回预计买入数量
     * 
     * @return
     */
    public int getBuyTotalCount() {
        return this._buyTotalCount;
    }

    private int _buyPrice; // 预计价格

    /**
     * 设置回收价格
     * 
     * @param i
     */
    public void setBuyPrice(final int i) {
        this._buyPrice = i;
    }

    /**
     * 回收价格
     * 
     * @return
     */
    public int getBuyPrice() {
        return this._buyPrice;
    }

    private int _buyCount; // 买入数量累计

    /**
     * 设置买入数量累计
     * 
     * @param i
     */
    public void setBuyCount(final int i) {
        this._buyCount = i;
    }

    /**
     * 传回买入数量累计
     * 
     * @return
     */
    public int getBuyCount() {
        return this._buyCount;
    }
}
