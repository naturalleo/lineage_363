package com.lineage.server.datatables.storage;

/**
 * GM物品纪录
 * 
 * @author dexc
 * 
 */
public interface OtherUserToPcStorage {

    /**
     * 增加纪录
     * 
     * @param itemname
     *            物品名称
     * @param itemcount
     *            数量
     * @param pcobjid
     *            GM OBJID
     * @param pcname
     *            GM名称
     * @param srcpcobjid
     *            对象OBJID
     * @param srcpcname
     *            对象名称
     */
    public void add(final String itemname, final long itemcount,
            final int pcobjid, final String pcname, final int srcpcobjid,
            final String srcpcname);
}
