package com.lineage.server.templates;

/**
 * Event(活动)设置 暂存
 * 
 * @author daien
 * 
 */
public class L1Event {

    private int _eventid;// 活动编号

    private String _eventname;// 活动名称

    private String _eventclass;// 活动CLASS

    private boolean _eventstart;// 活动是否启用

    private String _eventother;// 活动其他设定

    /**
     * 活动编号
     * 
     * @return the _eventid
     */
    public int get_eventid() {
        return this._eventid;
    }

    /**
     * 活动编号
     * 
     * @param eventid
     *            the _eventid to set
     */
    public void set_eventid(final int eventid) {
        this._eventid = eventid;
    }

    /**
     * 活动名称
     * 
     * @return the _eventname
     */
    public String get_eventname() {
        return this._eventname;
    }

    /**
     * 活动名称
     * 
     * @param eventname
     *            the _eventname to set
     */
    public void set_eventname(final String eventname) {
        this._eventname = eventname;
    }

    /**
     * 活动CLASS
     * 
     * @return the _eventclass
     */
    public String get_eventclass() {
        return this._eventclass;
    }

    /**
     * 活动CLASS
     * 
     * @param eventclass
     *            the _eventclass to set
     */
    public void set_eventclass(final String eventclass) {
        this._eventclass = eventclass;
    }

    /**
     * 活动是否启用
     * 
     * @return the _eventstart
     */
    public boolean is_eventstart() {
        return this._eventstart;
    }

    /**
     * 活动是否启用
     * 
     * @param eventstart
     *            the _eventstart to set
     */
    public void set_eventstart(final boolean eventstart) {
        this._eventstart = eventstart;
    }

    /**
     * 活动其他设定
     * 
     * @return the _eventother
     */
    public String get_eventother() {
        return this._eventother;
    }

    /**
     * 活动其他设定
     * 
     * @param eventother
     *            the _eventother to set
     */
    public void set_eventother(final String eventother) {
        this._eventother = eventother;
    }
}
