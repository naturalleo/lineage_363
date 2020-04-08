package com.lineage.server.datatables.storage;

/**
 * 卖出物品给个人商店纪录
 * 
 * @author dexc
 * 
 */
public interface OtherUserSellStorage {

    /**
     * 增加纪录
     * 
     * @param itemname
     *            回收物品名称
     * @param itemobjid
     *            回收物品OBJID
     * @param itemadena
     *            单件物品回收金额
     * @param itemcount
     *            回收数量
     * @param pcobjid
     *            卖出者OBJID
     * @param pcname
     *            卖出者名称
     * @param srcpcobjid
     *            买入者OBJID(个人商店)
     * @param srcpcname
     *            买入者名称(个人商店)
     */
    public void add(final String itemname, final int itemobjid,
            final int itemadena, final long itemcount, final int pcobjid,
            final String pcname, final int srcpcobjid, final String srcpcname);
}
