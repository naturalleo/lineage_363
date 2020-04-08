package com.lineage.server.templates;

import java.util.Calendar;

/**
 * 城堡数据暂存
 * 
 * @author DaiEn
 * 
 */
public class L1Castle {

    /**
     * 城堡数据暂存
     * 
     * @param id
     *            城堡编号
     * @param name
     *            城堡名称
     */
    public L1Castle(final int id, final String name) {
        this._id = id;
        this._name = name;
    }

    private int _id;

    public int getId() {
        return this._id;
    }

    private String _name;

    public String getName() {
        return this._name;
    }

    private Calendar _warTime;

    public Calendar getWarTime() {
        return this._warTime;
    }

    public void setWarTime(final Calendar i) {
        this._warTime = i;
    }

    private int _taxRate;

    public int getTaxRate() {
        return this._taxRate;
    }

    public void setTaxRate(final int i) {
        this._taxRate = i;
    }

    private long _publicMoney;

    public long getPublicMoney() {
        return this._publicMoney;
    }

    public void setPublicMoney(final long i) {
        this._publicMoney = i;
    }

}
