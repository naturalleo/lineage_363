package com.lineage.server.templates;

/**
 * 个人商店出售物品的资料缓存
 * 
 * @author admin
 * 
 */
public class L1PrivateShopSellList {

    public L1PrivateShopSellList() {
    }

    private int _itemObjectId;// 物品objid

    /**
     * 设置物品objid
     * 
     * @param i
     */
    public void setItemObjectId(final int i) {
        this._itemObjectId = i;
    }

    /**
     * 传回物品objid
     * 
     * @return
     */
    public int getItemObjectId() {
        return this._itemObjectId;
    }

    private int _sellTotalCount; // 预计卖出的数量

    /**
     * 设置预计卖出的数量
     * 
     * @param i
     */
    public void setSellTotalCount(final int i) {
        this._sellTotalCount = i;
    }

    /**
     * 传回预计卖出的数量
     * 
     * @return
     */
    public int getSellTotalCount() {
        return this._sellTotalCount;
    }

    private int _sellPrice;// 售价

    /**
     * 设置售价
     * 
     * @param i
     */
    public void setSellPrice(final int i) {
        this._sellPrice = i;
    }

    /**
     * 传回售价
     * 
     * @return
     */
    public int getSellPrice() {
        return this._sellPrice;
    }

    private int _sellCount; // 已经卖出数量的累计

    /**
     * 设置已经卖出数量的累计
     * 
     * @param i
     */
    public void setSellCount(final int i) {
        this._sellCount = i;
    }

    /**
     * 传回已经卖出数量的累计
     * 
     * @return
     */
    public int getSellCount() {
        return this._sellCount;
    }
}
