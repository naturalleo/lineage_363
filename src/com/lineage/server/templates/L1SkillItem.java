package com.lineage.server.templates;

/**
 * 购买技能 金币/材料 设置资料
 * 
 * @author DaiEn
 * 
 */
public class L1SkillItem {

    private int _skill_id;// 技能编号

    /**
     * 技能编号
     * 
     * @return
     */
    public int get_skill_id() {
        return this._skill_id;
    }

    /**
     * 技能编号
     * 
     * @param i
     */
    public void set_skill_id(final int i) {
        this._skill_id = i;
    }

    private String _name;// 技能名称

    /**
     * 技能名称
     * 
     * @return
     */
    public String get_name() {
        return this._name;
    }

    /**
     * 技能名称
     * 
     * @param s
     */
    public void set_name(final String s) {
        this._name = s;
    }

    private int[] _items;// 物件组

    /**
     * 耗用物件组
     * 
     * @return
     */
    public int[] get_items() {
        return this._items;
    }

    /**
     * 物件组
     * 
     * @param is
     */
    public void set_items(final int[] is) {
        this._items = is;
    }

    private int[] _counts;// 数量组

    /**
     * 耗用数量组
     * 
     * @return
     */
    public int[] get_counts() {
        return this._counts;
    }

    /**
     * 数量组
     * 
     * @param is
     */
    public void set_counts(final int[] is) {
        this._counts = is;
    }

    // private int _adena;// 金币

    /**
     * 耗用金币
     * 
     * @return
     */
    /*
     * public int get_adena() { return this._adena; }
     */

    /**
     * 金币
     * 
     * @param i
     */
    /*
     * public void set_adena(final int i) { this._adena = i; }
     */
}
