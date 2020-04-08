package com.lineage.server.templates;

/**
 * 布告栏资料
 * 
 * @author dexc
 * 
 */
public class L1Board {

    private int _id;

    private String _name;

    private String _date;

    private String _title;

    private String _content;

    /**
     * 传回布告栏ID
     * 
     * @return
     */
    public int get_id() {
        return this._id;
    }

    /**
     * 设置布告栏ID
     * 
     * @param id
     */
    public void set_id(final int id) {
        this._id = id;
    }

    /**
     * 传回布告栏公布者
     * 
     * @return
     */
    public String get_name() {
        return this._name;
    }

    /**
     * 设置布告栏公布者
     * 
     * @param name
     */
    public void set_name(final String name) {
        this._name = name;
    }

    /**
     * 传回布告栏公布日期
     * 
     * @return
     */
    public String get_date() {
        return this._date;
    }

    /**
     * 设置布告栏公布日期
     * 
     * @param date
     */
    public void set_date(final String date) {
        this._date = date;
    }

    /**
     * 传回布告栏标题
     * 
     * @return
     */
    public String get_title() {
        return this._title;
    }

    /**
     * 设置布告栏标题
     * 
     * @param title
     */
    public void set_title(final String title) {
        this._title = title;
    }

    /**
     * 传回布告栏内容
     * 
     * @return
     */
    public String get_content() {
        return this._content;
    }

    /**
     * 设置布告栏内容
     * 
     * @param content
     */
    public void set_content(final String content) {
        this._content = content;
    }
}
