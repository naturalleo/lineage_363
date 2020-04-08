package com.lineage.server.datatables.storage;

import java.util.Map;

import com.lineage.server.templates.L1Bank;

/**
 * 银行帐户资料
 */
public interface AccountBankStorage {

    /**
     * 预先加载
     */
    public void load();

    /**
     * 该帐户资料
     * 
     * @param account_name
     * @return
     */
    public L1Bank get(String account_name);

    public Map<String, L1Bank> map();

    /**
     * 建立帐号资料
     * 
     * @param loginName
     * @param bank
     */
    public void create(final String loginName, final L1Bank bank);

    /**
     * 更新密码
     * 
     * @param loginName
     *            帐号
     * @param pwd
     *            密码
     */
    public void updatePass(final String loginName, final String pwd);

    /**
     * 更新存款
     * 
     * @param loginName
     *            帐号
     * @param adena
     *            金额
     */
    public void updateAdena(final String loginName, final long adena);

}
