package com.lineage.server.datatables.storage;

/**
 * 转移物品纪录
 * 
 * @author hjx1000
 * 
 */
public interface OtherTransferStorage {

    /**
     * 增加纪录
     * 
     * @param itemname
     *            转移物品名称
     * @param itemobjid
     *            转移物品OBJID
     * @param itemId
     *            物品ID
     * @param pcobjid
     *            转移者OBJID
     * @param pcname
     *            转移者名称
     * @param srcpcobjid
     *            来源者OBJID
     * @param srcpcname
     *            来源者名称
     */
    public void add(final String itemname, final int itemobjid,
            final long itemid, final int pcobjid,
            final String pcname, final int srcpcobjid, final String srcpcname, final String src);
}
