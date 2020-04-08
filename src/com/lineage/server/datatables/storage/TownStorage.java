package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Town;

/**
 * 村庄资料
 * 
 * @author dexc
 * 
 */
public interface TownStorage {

    /**
     * 初始化载入
     */
    public void load();

    /**
     * 传回村庄阵列资料
     * 
     * @return
     */
    public L1Town[] getTownTableList();

    /**
     * 传回指定村庄资料
     * 
     * @param id
     * @return
     */
    public L1Town getTownTable(int id);

    /**
     * 检查是否为村长
     * 
     * @param pc
     * @param town_id
     * @return
     */
    public boolean isLeader(L1PcInstance pc, int town_id);

    /**
     * 更新村庄收入
     * 
     * @param town_id
     * @param salesMoney
     */
    public void addSalesMoney(int town_id, int salesMoney);

    /**
     * 更新村庄税率
     */
    public void updateTaxRate();

    /**
     * 更新收入资讯
     */
    public void updateSalesMoneyYesterday();

    /**
     * 
     * @param townId
     * @return
     */
    public String totalContribution(int townId);

    /**
	 *
	 */
    public void clearHomeTownID();

    /**
     * 领取报酬
     * 
     * @return 报酬
     */
    public int getPay(int objid);
}
