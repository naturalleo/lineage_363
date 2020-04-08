package com.lineage.server.datatables.storage;

import com.lineage.server.templates.L1Exchange;

/**
 * 兑换资料
 */
public interface ExchangeStorage {

    /**
     * 预先加载
     */
    public void load();



    /**
     * 更新比例
     * 
     * @param loginName
     *            帐号
     * @param adena
     *            金额
     */
    public void updateAdena(final int npcid, final int adena);



	public L1Exchange getExchangeTable(int id);

}
