package com.lineage.server.datatables.storage;

/**
 * 禁用ip资料
 * 
 * @author dexc
 * 
 */
public interface IpStorage {

    /**
     * 初始化载入
     */
    public void load();

    /**
     * 加入禁止位置
     * 
     * @param ip
     * @param info
     *            原因
     */
    public void add(final String ip, final String info);

    /**
     * 移出禁止位置
     * 
     * @param ip
     * @return
     */
    public void remove(String ip);
}
