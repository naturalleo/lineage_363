package com.lineage.server.templates;

/**
 * 新手物品资料<BR>
 * 
 * @author dexc
 * 
 */
public class L1Beginner {

    private String _activate;
    private int _itemid;
    private int _count;
    private int _enchantlvl;
    private int _charge_count;
    private int _time;

    /**
     * 传出分组
     * 
     * @return 传出 _activate
     */
    public String get_activate() {
        return this._activate;
    }

    /**
     * 设置分组
     * 
     * @param activate
     *            对 _activate 进行设置
     */
    public void set_activate(final String activate) {
        this._activate = activate;
    }

    /**
     * 传出物品编号
     * 
     * @return 传出 _itemid
     */
    public int get_itemid() {
        return this._itemid;
    }

    /**
     * 设置物品编号
     * 
     * @param itemid
     *            对 _itemid 进行设置
     */
    public void set_itemid(final int itemid) {
        this._itemid = itemid;
    }

    /**
     * 传出给予数量
     * 
     * @return 传出 _count
     */
    public int get_count() {
        return this._count;
    }

    /**
     * 设置给予数量
     * 
     * @param count
     *            对 _count 进行设置
     */
    public void set_count(final int count) {
        this._count = count;
    }

    /**
     * 传出追加值
     * 
     * @return 传出 _enchantlvl
     */
    public int get_enchantlvl() {
        return this._enchantlvl;
    }

    /**
     * 设置追加值
     * 
     * @param enchantlvl
     *            对 _enchantlvl 进行设置
     */
    public void set_enchantlvl(final int enchantlvl) {
        this._enchantlvl = enchantlvl;
    }

    /**
     * 传出可用次数
     * 
     * @return 传出 _charge_count
     */
    public int get_charge_count() {
        return this._charge_count;
    }

    /**
     * 设置可用次数
     * 
     * @param charge_count
     *            对 _charge_count 进行设置
     */
    public void set_charge_count(final int charge_count) {
        this._charge_count = charge_count;
    }

    /**
     * 使用时间限制(小时)
     * 
     * @return
     */
    public int get_time() {
        return _time;
    }

    /**
     * 使用时间限制(小时)
     * 
     * @param time
     */
    public void set_time(int time) {
        _time = time;
    }
}
