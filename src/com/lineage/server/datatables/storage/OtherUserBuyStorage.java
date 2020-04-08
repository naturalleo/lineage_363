package com.lineage.server.datatables.storage;

/**
 * 买入个人商店物品纪录
 * 
 * @author dexc
 * 
 */
public interface OtherUserBuyStorage {

    /**
     * 增加纪录
     * 
     * @param itemname
     *            买入物品名称
     * @param itemobjid
     *            买入物品OBJID
     * @param itemadena
     *            单件物品买入金额
     * @param itemcount
     *            买入数量
     * @param pcobjid
     *            买入者OBJID
     * @param pcname
     *            买入者名称
     * @param srcpcobjid
     *            卖出者OBJID(个人商店)
     * @param srcpcname
     *            卖出者名称(个人商店)
     */
    public void add(final String itemname, final int itemobjid,
            final int itemadena, final long itemcount, final int pcobjid,
            final String pcname, final int srcpcobjid, final String srcpcname);
}
