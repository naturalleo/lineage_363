package com.lineage.server.templates;

/**
 * 物品升级资料暂存
 * 
 * @author daien
 * 
 */
public class L1ItemUpdate {

    public static final String _html_01 = "y_up_i0";

    public static final String _html_02 = "y_up_i1";

    public static final String _html_03 = "y_up_i2";

    private int _item_id;

    private int _toid;

    private int[] _needids = null;

    private int[] _needcounts = null;
    
    private int _probability;

    public int get_item_id() {
        return _item_id;
    }

    public void set_item_id(int _item_id) {
        this._item_id = _item_id;
    }

    public int get_toid() {
        return _toid;
    }

    public void set_toid(int _toid) {
        this._toid = _toid;
    }

    public int[] get_needids() {
        return _needids;
    }

    public void set_needids(int[] _needids) {
        this._needids = _needids;
    }

    public int[] get_needcounts() {
        return _needcounts;
    }

    public void set_needcounts(int[] _needcounts) {
        this._needcounts = _needcounts;
    }
    
    public int get_probability() {
    	return _probability;
    }
    
    public void set_probability(int _probability) {
    	this._probability = _probability;
    }
}
