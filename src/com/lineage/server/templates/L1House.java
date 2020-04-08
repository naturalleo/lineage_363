package com.lineage.server.templates;

import java.util.Calendar;

/**
 * 小屋资料缓存
 * 
 * @author dexc
 * 
 */
public class L1House {

    private int _houseId;

    /**
     * 传回小屋编号
     * 
     * @return
     */
    public int getHouseId() {
        return this._houseId;
    }

    /**
     * 设置小屋编号
     * 
     * @param i
     */
    public void setHouseId(final int i) {
        this._houseId = i;
    }

    private String _houseName;

    /**
     * 传回小屋名称
     * 
     * @return
     */
    public String getHouseName() {
        return this._houseName;
    }

    /**
     * 设置小屋名称
     * 
     * @param s
     */
    public void setHouseName(final String s) {
        this._houseName = s;
    }

    private int _houseArea;

    /**
     * 传回大小(坪数)
     * 
     * @return
     */
    public int getHouseArea() {
        return this._houseArea;
    }

    /**
     * 设置大小(坪数)
     * 
     * @param i
     */
    public void setHouseArea(final int i) {
        this._houseArea = i;
    }

    private String _location;

    /**
     * 传回小屋顺序排列
     * 
     * @return
     */
    public String getLocation() {
        return this._location;
    }

    /**
     * 设置小屋顺序排列
     * 
     * @param s
     */
    public void setLocation(final String s) {
        this._location = s;
    }

    private int _keeperId;

    /**
     * 传回女佣编号
     * 
     * @return
     */
    public int getKeeperId() {
        return this._keeperId;
    }

    /**
     * 设置女佣编号
     * 
     * @param i
     */
    public void setKeeperId(final int i) {
        this._keeperId = i;
    }

    private boolean _isOnSale;

    /**
     * 是否在出售状态
     * 
     * @return true:是 false:不是
     */
    public boolean isOnSale() {
        return this._isOnSale;
    }

    /**
     * 设置是否在出售状态
     * 
     * @param flag
     */
    public void setOnSale(final boolean flag) {
        this._isOnSale = flag;
    }

    private boolean _isPurchaseBasement;

    /**
     * 是否有地下盟屋
     * 
     * @return
     */
    public boolean isPurchaseBasement() {
        return this._isPurchaseBasement;
    }

    /**
     * 设置是否有地下盟屋
     * 
     * @param flag
     */
    public void setPurchaseBasement(final boolean flag) {
        this._isPurchaseBasement = flag;
    }

    private Calendar _taxDeadline;

    /**
     * 传回交税时间
     * 
     * @return
     */
    public Calendar getTaxDeadline() {
        return this._taxDeadline;
    }

    /**
     * 设置交税时间
     * 
     * @param cal
     */
    public void setTaxDeadline(final Calendar cal) {
        this._taxDeadline = cal;
    }

}
