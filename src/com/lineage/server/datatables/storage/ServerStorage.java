package com.lineage.server.datatables.storage;

/**
 * 服务器资料
 */
public interface ServerStorage {

    /**
     * 预先加载服务器存档资料
     */
    public void load();

    /**
     * 传回服务器最小编号设置
     */
    public int minId();

    /**
     * 传回服务器最大编号设置
     */
    public int maxId();

    /**
     * 设定服务器关机<BR>
     * 同时记录已用最大编号
     */
    public void isStop();

}
