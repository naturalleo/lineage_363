package com.lineage.server.templates;

/**
 * 置放家具暂时资料
 * 
 * @author daien
 * 
 */
public class L1Furniture {

    private int _npcid;// 对应的NPC编号

    private int _item_obj_id;// 对应的物品编号

    private int _locx;// 放置的X座标

    private int _locy;// 放置的Y座标

    private short _mapid;// 放置的地图编号

    /**
     * 对应的NPC编号
     * 
     * @return the _npcid
     */
    public int get_npcid() {
        return this._npcid;
    }

    /**
     * 对应的NPC编号
     * 
     * @param npcid
     *            the _npcid to set
     */
    public void set_npcid(final int npcid) {
        this._npcid = npcid;
    }

    /**
     * 对应的物品编号
     * 
     * @return the _item_obj_id
     */
    public int get_item_obj_id() {
        return this._item_obj_id;
    }

    /**
     * 对应的物品编号
     * 
     * @param itemObjId
     *            the _item_obj_id to set
     */
    public void set_item_obj_id(final int itemObjId) {
        this._item_obj_id = itemObjId;
    }

    /**
     * 放置的X座标
     * 
     * @return the _locx
     */
    public int get_locx() {
        return this._locx;
    }

    /**
     * 放置的X座标
     * 
     * @param locx
     *            the _locx to set
     */
    public void set_locx(final int locx) {
        this._locx = locx;
    }

    /**
     * 放置的Y座标
     * 
     * @return the _locy
     */
    public int get_locy() {
        return this._locy;
    }

    /**
     * 放置的Y座标
     * 
     * @param locy
     *            the _locy to set
     */
    public void set_locy(final int locy) {
        this._locy = locy;
    }

    /**
     * 放置的地图编号
     * 
     * @return the _mapid
     */
    public short get_mapid() {
        return this._mapid;
    }

    /**
     * 放置的地图编号
     * 
     * @param mapid
     *            the _mapid to set
     */
    public void set_mapid(final short mapid) {
        this._mapid = mapid;
    }
}
