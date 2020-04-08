package com.lineage.server.datatables.storage;

/**
 * 个人交易物品纪录
 * 
 * @author dexc
 * 
 */
public interface OtherUserTradeStorage {

    /**
     * 增加纪录
     * 
     * @param itemname
     *            物品名称
     * @param itemobjid
     *            物品OBJID
     * @param itemadena
     *            0 暂无用途
     * @param itemcount
     *            数量
     * @param pcobjid
     *            移入人物OBJID
     * @param pcname
     *            移入人物名称
     * @param srcpcobjid
     *            移出人物者OBJID
     * @param srcpcname
     *            移出人物名称
     */
    public void add(final String itemname, final int itemobjid,
            final int itemadena, final long itemcount, final int pcobjid,
            final String pcname, final int srcpcobjid, final String srcpcname);
}
