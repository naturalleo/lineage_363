package com.lineage.server.templates;

import java.sql.Timestamp;

import com.lineage.server.model.Instance.L1ItemInstance;

/**
 * 托售物品暂存
 * 
 * @author daien
 * 
 */
public class L1ShopS {

    private int _id;// 编号

    private int _item_obj_id;// 物品OBJID

    private int _user_obj_id;// 出售人OBJID

    private int _adena;// 售价

    private Timestamp _overtime;// 拍卖终止时间

    // 结束模式 0:出售中 1:已售出 2:已售出领回天宝 3:未售出 4:未售出道具以领回
    private int _end;

    private String _none;// 备注

    private L1ItemInstance _item = null;// 物品资料

    /**
     * 编号
     * 
     * @return the _id
     */
    public int get_id() {
        return _id;
    }

    /**
     * 编号
     * 
     * @param id
     *            the _id to set
     */
    public void set_id(int id) {
        _id = id;
    }

    /**
     * 物品OBJID
     * 
     * @return the _item_obj_id
     */
    public int get_item_obj_id() {
        return _item_obj_id;
    }

    /**
     * 物品OBJID
     * 
     * @param itemObjId
     *            the _item_obj_id to set
     */
    public void set_item_obj_id(int itemObjId) {
        _item_obj_id = itemObjId;
    }

    /**
     * 出售人OBJID
     * 
     * @return the _user_obj_id
     */
    public int get_user_obj_id() {
        return _user_obj_id;
    }

    /**
     * 出售人OBJID
     * 
     * @param userObjId
     *            the _user_obj_id to set
     */
    public void set_user_obj_id(int userObjId) {
        _user_obj_id = userObjId;
    }

    /**
     * 售价
     * 
     * @return the _adena
     */
    public int get_adena() {
        return _adena;
    }

    /**
     * 售价
     * 
     * @param adena
     *            the _adena to set
     */
    public void set_adena(int adena) {
        _adena = adena;
    }

    /**
     * 拍卖终止时间
     * 
     * @return the _overtime
     */
    public Timestamp get_overtime() {
        return _overtime;
    }

    /**
     * 拍卖终止时间
     * 
     * @param overtime
     *            the _overtime to set
     */
    public void set_overtime(Timestamp overtime) {
        _overtime = overtime;
    }

    /**
     * 结束模式 0:出售中 1:已售出 2:已售出领回天宝 3:未售出 4:未售出道具以领回
     * 
     * @return the _end
     */
    public int get_end() {
        return _end;
    }

    /**
     * 结束模式 0:出售中 1:已售出 2:已售出领回天宝 3:未售出 4:未售出道具以领回
     * 
     * @param end
     *            the _end to set
     */
    public void set_end(int end) {
        _end = end;
    }

    /**
     * 备注
     * 
     * @return the _none
     */
    public String get_none() {
        return _none;
    }

    /**
     * 备注
     * 
     * @param none
     *            the _none to set
     */
    public void set_none(String none) {
        _none = none;
    }

    /**
     * 物品资料
     * 
     * @return
     */
    public L1ItemInstance get_item() {
        return _item;
    }

    /**
     * 物品资料
     * 
     * @param item
     */
    public void set_item(L1ItemInstance item) {
        _item = item;
    }
}
