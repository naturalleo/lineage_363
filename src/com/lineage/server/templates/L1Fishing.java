package com.lineage.server.templates;

/**
 * 渔获资料暂存
 * 
 * @author daien
 * 
 */
public class L1Fishing {

    private int _itemid;// 物品编号

    private int _randomint;// 比对用机率

    private int _random;// 机率

    private int _count;// 给予数量

    private int _bait;// 需要鱼饵的数量

    public int get_itemid() {
        return _itemid;
    }

    public void set_itemid(int _itemid) {
        this._itemid = _itemid;
    }

    public int get_randomint() {
        return _randomint;
    }

    public void set_randomint(int _randomint) {
        this._randomint = _randomint;
    }

    public int get_random() {
        return _random;
    }

    public void set_random(int _random) {
        this._random = _random;
    }

    public int get_count() {
        return _count;
    }

    public void set_count(int _count) {
        this._count = _count;
    }

    public int get_bait() {
        return _bait;
    }

    public void set_bait(int bait) {
        this._bait = bait;
    }
}
