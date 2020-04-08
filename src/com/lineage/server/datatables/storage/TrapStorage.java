package com.lineage.server.datatables.storage;

/**
 * 陷阱数据接口
 * 
 * @author daien
 * 
 */
public interface TrapStorage {

    /**
     * 文字类型栏位
     * 
     * @param name
     *            栏位名称
     * @return
     */
    public String getString(String name);

    /**
     * 数字类型栏位
     * 
     * @param name
     *            栏位名称
     * @return
     */
    public int getInt(String name);

    /**
     * boolean 类型栏位
     * 
     * @param name
     *            栏位名称
     * @return
     */
    public boolean getBoolean(String name);
}
