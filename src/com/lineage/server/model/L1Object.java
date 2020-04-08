package com.lineage.server.model;

import java.io.Serializable;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.map.L1WorldMap;

/**
 * 世界物件共用容器
 */
public class L1Object implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 传回地图编号
     * 
     * @return 地图编号
     */
    public short getMapId() {
        return (short) this._loc.getMap().getId();
    }

    /**
     * 设置地图编号
     * 
     * @param mapId
     *            地图编号
     */
    public void setMap(final short mapId) {
        this._loc.setMap(L1WorldMap.get().getMap(mapId));
    }

    /**
     * 传回 L1Map
     * 
     */
    public L1Map getMap() {
        return this._loc.getMap();
    }

    /**
     * 设置 L1Map
     * 
     * @param map
     *            オブジェクトが存在するマップを保持するL1Mapオブジェクト
     */
    public void setMap(final L1Map map) {
        if (map == null) {
            throw new NullPointerException();
        }
        this._loc.setMap(map);
    }

    private int _id = 0;

    /**
     * 世界识别用编号(OBJID)
     * 
     * @return 识别用编号
     */
    public int getId() {
        return this._id;
    }

    /**
     * 设置世界识别用编号(OBJID)
     * 
     * @param id
     *            识别用编号
     */
    public void setId(final int id) {
        this._id = id;
    }

    /**
     * 物件目前X座标值
     * 
     * @return 座标X值
     */
    public int getX() {
        return this._loc.getX();
    }

    /**
     * 设置物件目前X座标值
     * 
     * @param x
     *            座标X值
     */
    public void setX(final int x) {
        this._loc.setX(x);
    }

    /**
     * 物件目前Y座标值
     * 
     * @return 座标Y值
     */
    public int getY() {
        return this._loc.getY();
    }

    /**
     * 设置物件目前Y座标值
     * 
     * @param y
     *            座标Y值
     */
    public void setY(final int y) {
        this._loc.setY(y);
    }

    private final L1Location _loc = new L1Location();// 物件座标资讯

    /**
     * 返回物件的 L1Location
     * 
     * @return L1Location
     */
    public L1Location getLocation() {
        return this._loc;
    }

    /**
     * 设置物件座标资料
     * 
     * @param loc
     */
    public void setLocation(final L1Location loc) {
        this._loc.setX(loc.getX());
        this._loc.setY(loc.getY());
        this._loc.setMap(loc.getMapId());
    }

    /**
     * 设置物件座标资料
     * 
     * @param x
     * @param y
     * @param mapid
     */
    public void setLocation(final int x, final int y, final int mapid) {
        this._loc.setX(x);
        this._loc.setY(y);
        this._loc.setMap(mapid);
    }

    /**
     * 指定されたオブジェクトまでの直线距离を返す。
     */
    public double getLineDistance(final L1Object obj) {
        return this.getLocation().getLineDistance(obj.getLocation());
    }

    /**
     * 指定座标直线距离(相对位置最大距离)
     */
    public int getTileLineDistance(final L1Object obj) {
        return this.getLocation().getTileLineDistance(obj.getLocation());
    }

    /**
     * 指定座标距离(XY距离总合)
     */
    public int getTileDistance(final L1Object obj) {
        return this.getLocation().getTileDistance(obj.getLocation());
    }

    /**
     * 周边遭遇人物的调用
     * 
     * @param perceivedFrom
     *            遭遇的人物
     */
    public void onPerceive(final L1PcInstance perceivedFrom) {
    }

    /**
     * 对该物件攻击的调用
     * 
     * @param actionFrom
     *            攻击者
     */
    public void onAction(final L1PcInstance actionFrom) {
    }

    /**
     * 具备对话能力NPC的对话显示的调用
     * 
     * @param talkFrom
     *            对话的人物
     */
    public void onTalkAction(final L1PcInstance talkFrom) {
    }

    private int _showId = -1; // 可见(副本)编号

    /**
     * 可见(副本)编号
     * 
     * @return
     */
    public int get_showId() {
        return _showId;
    }

    /**
     * 可见(副本)编号<BR>
     * 副本编号设置必须在加入世界物件之前<BR>
     * 避免部分封包发送失败<BR>
     * 
     * @param showId
     */
    public void set_showId(int showId) {
        _showId = showId;
    }
}
