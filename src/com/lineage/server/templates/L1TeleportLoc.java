package com.lineage.server.templates;

/**
 * 传送点资料
 * 
 * @author daien
 * 
 */
public class L1TeleportLoc {

    private int _id;

    private String _name;

    private String _orderid;

    private int _locx;

    private int _locy;

    private int _mapid;

    private int _itemid;

    private int _price;

    private int _time;

    private int _user;

    private int _min;

    private int _max;

    /**
     * @return 传出 _id
     */
    public int get_id() {
        return this._id;
    }

    /**
     * @param _id
     *            对 _id 进行设置
     */
    public void set_id(final int _id) {
        this._id = _id;
    }

    /**
     * @return 传出 _name
     */
    public String get_name() {
        return this._name;
    }

    /**
     * @param _name
     *            对 _name 进行设置
     */
    public void set_name(final String _name) {
        this._name = _name;
    }

    /**
     * @return 传出 _orderid
     */
    public String get_orderid() {
        return this._orderid;
    }

    /**
     * @param _orderid
     *            对 _orderid 进行设置
     */
    public void set_orderid(final String _orderid) {
        this._orderid = _orderid;
    }

    /**
     * @return 传出 _locx
     */
    public int get_locx() {
        return this._locx;
    }

    /**
     * @param _locx
     *            对 _locx 进行设置
     */
    public void set_locx(final int _locx) {
        this._locx = _locx;
    }

    /**
     * @return 传出 _locy
     */
    public int get_locy() {
        return this._locy;
    }

    /**
     * @param _locy
     *            对 _locy 进行设置
     */
    public void set_locy(final int _locy) {
        this._locy = _locy;
    }

    /**
     * @return 传出 _mapid
     */
    public int get_mapid() {
        return this._mapid;
    }

    /**
     * @param _mapid
     *            对 _mapid 进行设置
     */
    public void set_mapid(final int _mapid) {
        this._mapid = _mapid;
    }

    /**
     * @return 传出 _itemid
     */
    public int get_itemid() {
        return this._itemid;
    }

    /**
     * @param _itemid
     *            对 _itemid 进行设置
     */
    public void set_itemid(final int _itemid) {
        this._itemid = _itemid;
    }

    /**
     * @return 传出 _price
     */
    public int get_price() {
        return this._price;
    }

    /**
     * @param _price
     *            对 _price 进行设置
     */
    public void set_price(final int _price) {
        this._price = _price;
    }

    /**
     * @return 传出 _time
     */
    public int get_time() {
        return this._time;
    }

    /**
     * @param _time
     *            对 _time 进行设置
     */
    public void set_time(final int _time) {
        this._time = _time;
    }

    /**
     * @return 传出 _user
     */
    public int get_user() {
        return this._user;
    }

    /**
     * @param user
     *            对 _user 进行设置
     */
    public void set_user(int user) {
        this._user = user;
    }

    public int get_min() {
        return _min;
    }

    public void set_min(int _min) {
        this._min = _min;
    }

    public int get_max() {
        return _max;
    }

    public void set_max(int _max) {
        this._max = _max;
    }
}
