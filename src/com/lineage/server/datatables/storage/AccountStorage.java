package com.lineage.server.datatables.storage;

import com.lineage.server.templates.L1Account;

/**
 * 人物帐户资料
 */
public interface AccountStorage {

    /**
     * 预先加载帐户名称
     */
    public void load();

    /**
     * 传回指定帐户名称资料是否存在
     * 
     * @param loginName
     *            帐号名称
     * @return true:有该帐号 false:没有该帐号
     */
    public boolean isAccountUT(final String loginName);

    /**
     * 建立帐号资料
     * 
     * @param loginName
     *            帐号
     * @param pwd
     *            密码
     * @param ip
     *            IP位置
     * @param host
     *            MAC位置
     * @param spwd
     *            超级密码
     * @return L1Account
     */
    public L1Account create(final String loginName, final String pwd,
            final String ip, final String host, final String spwd);

    /**
     * 传回指定帐户资料是否存在
     * 
     * @param loginName
     *            帐号名称
     */
    public boolean isAccount(final String loginName);

    /**
     * 传回指定帐户资料
     * 
     * @param loginName
     *            帐号
     */
    public L1Account getAccount(final String loginName);

    /**
     * 更新仓库密码
     * 
     * @param loginName
     *            帐号
     * @param pwd
     *            密码
     */
    public void updateWarehouse(final String loginName, final int pwd);

    /**
     * 更新上线时间/IP/MAC
     * 
     * @param account
     *            帐户
     */
    public void updateLastActive(final L1Account account);

    /**
     * 更新帐号可用人物数量
     * 
     * @param loginName
     *            帐号
     * @param count
     *            扩充数量
     */
    public void updateCharacterSlot(final String loginName, final int count);

    /**
     * 更新帐号密码
     * 
     * @param loginName
     *            帐号
     * 
     * @param newpwd
     *            新密码
     */
    public void updatePwd(final String loginName, final String newpwd);

    /**
     * 更新帐号连线状态
     * 
     * @param loginName
     *            帐号
     * @param islan
     *            是否连线
     */
    public void updateLan(final String loginName, final boolean islan);

    /**
     * 更新帐号连线状态(全部离线)
     */
    public void updateLan();

    /**
     * 更新消费计时卡数 hjx1000
     * @param loginName
     * 			账号
     * @param card_cou
     * 			/时间
     */
	public void updatecard_fee(String loginName, int card_cou);//hjx1000

}
