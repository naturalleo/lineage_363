package com.lineage.server.templates;

/**
 * 物品升级资料暂存
 * 
 * @author DaiEn
 * 
 */
public class L1ItemPowerUpdate {

    private int _itemid;// 要升级的物品ITEMID

    private int _nedid;// 需要物品

    private int _type_id;// 群组ID

    private int _order_id;// 群组排序

    private int _mode;// 0:无 1:强化失败会倒退 2:强化失败会消失 4:无法强化

    private int _random;// 强化机率(1/1000)

    public int get_itemid() {
        return _itemid;
    }

    public void set_itemid(int itemid) {
        this._itemid = itemid;
    }

    public int get_type_id() {
        return _type_id;
    }

    public void set_type_id(int type_id) {
        this._type_id = type_id;
    }

    public int get_order_id() {
        return _order_id;
    }

    public void set_order_id(int order_id) {
        this._order_id = order_id;
    }

    public int get_mode() {
        return _mode;
    }

    public void set_mode(int mode) {
        this._mode = mode;
    }

    public int get_random() {
        return _random;
    }

    public void set_random(int random) {
        this._random = random;
    }

    public int get_nedid() {
        return _nedid;
    }

    public void set_nedid(int _nedid) {
        this._nedid = _nedid;
    }
}
