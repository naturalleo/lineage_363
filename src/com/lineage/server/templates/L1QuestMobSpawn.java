package com.lineage.server.templates;

/**
 * QUEST NPC召唤资料 暂存
 * 
 * @author daien
 * 
 */
public class L1QuestMobSpawn {

    private int _questid;// QUEST编号

    private int _count;// 数量

    private int _npc_templateid;// NPCID

    private int _group_id;// NPC队伍ID

    private int _locx1;// X1

    private int _locy1;// Y1

    private int _locx2;// X2

    private int _locy2;// Y2

    private int _heading;// 面向

    private int _mapid;// mapid

    /**
     * QUEST编号
     * 
     * @return
     */
    public int get_questid() {
        return _questid;
    }

    /**
     * QUEST编号
     * 
     * @param _questid
     */
    public void set_questid(int _questid) {
        this._questid = _questid;
    }

    /**
     * 数量
     * 
     * @return
     */
    public int get_count() {
        return _count;
    }

    /**
     * 数量
     * 
     * @param _count
     */
    public void set_count(int _count) {
        this._count = _count;
    }

    /**
     * NPCID
     * 
     * @return
     */
    public int get_npc_templateid() {
        return _npc_templateid;
    }

    /**
     * NPCID
     * 
     * @param _npc_templateid
     */
    public void set_npc_templateid(int _npc_templateid) {
        this._npc_templateid = _npc_templateid;
    }

    /**
     * NPC队伍ID
     * 
     * @return
     */
    public int get_group_id() {
        return _group_id;
    }

    /**
     * NPC队伍ID
     * 
     * @param _group_id
     */
    public void set_group_id(int _group_id) {
        this._group_id = _group_id;
    }

    /**
     * X1
     * 
     * @return
     */
    public int get_locx1() {
        return _locx1;
    }

    /**
     * X1
     * 
     * @param _locx1
     */
    public void set_locx1(int _locx1) {
        this._locx1 = _locx1;
    }

    /**
     * Y1
     * 
     * @return
     */
    public int get_locy1() {
        return _locy1;
    }

    /**
     * Y1
     * 
     * @param _locy1
     */
    public void set_locy1(int _locy1) {
        this._locy1 = _locy1;
    }

    /**
     * X2
     * 
     * @return
     */
    public int get_locx2() {
        return _locx2;
    }

    /**
     * X2
     * 
     * @param _locx2
     */
    public void set_locx2(int _locx2) {
        this._locx2 = _locx2;
    }

    /**
     * Y2
     * 
     * @return
     */
    public int get_locy2() {
        return _locy2;
    }

    /**
     * Y2
     * 
     * @param _locy2
     */
    public void set_locy2(int _locy2) {
        this._locy2 = _locy2;
    }

    /**
     * 面向
     * 
     * @return
     */
    public int get_heading() {
        return _heading;
    }

    /**
     * 面向
     * 
     * @param _heading
     */
    public void set_heading(int _heading) {
        this._heading = _heading;
    }

    /**
     * mapid
     * 
     * @return
     */
    public int get_mapid() {
        return _mapid;
    }

    /**
     * mapid
     * 
     * @param _mapid
     */
    public void set_mapid(int _mapid) {
        this._mapid = _mapid;
    }
}
