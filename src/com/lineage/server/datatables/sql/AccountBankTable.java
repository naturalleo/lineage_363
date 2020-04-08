package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.AccountBankStorage;
import com.lineage.server.templates.L1Bank;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 银行帐户资料
 */
public class AccountBankTable implements AccountBankStorage {

    private static final Log _log = LogFactory.getLog(AccountBankTable.class);

    // 已有银行帐户资料
    private final Map<String, L1Bank> _bankNameList = new HashMap<String, L1Bank>();

    /**
     * 预先加载帐户名称
     */
    @Override
    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactory.get().getConnection();
            final String sqlstr = "SELECT * FROM `character_bank`";
            ps = co.prepareStatement(sqlstr);
            rs = ps.executeQuery();

            while (rs.next()) {
                final String account_name = rs.getString("account_name")
                        .toLowerCase();
                final long adena_count = rs.getLong("adena_count");
                final String pass = rs.getString("pass");

                final L1Bank bank = new L1Bank();
                bank.set_account_name(account_name);
                bank.set_adena_count(adena_count);
                bank.set_pass(pass);

                _bankNameList.put(account_name, bank);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
            SQLUtil.close(rs);
        }
        _log.info("载入已有银行帐户资料数量: " + _bankNameList.size() + "(" + timer.get()
                + "ms)");
    }

    /**
     * 该帐户资料
     * 
     * @param account_name
     * @return
     */
    @Override
    public L1Bank get(String account_name) {
        return _bankNameList.get(account_name);
    }

    @Override
    public Map<String, L1Bank> map() {
        return _bankNameList;
    }

    /**
     * 建立帐号资料
     * 
     * @param loginName
     * @param bank
     */
    @Override
    public void create(final String loginName, final L1Bank bank) {
        if (_bankNameList.get(loginName) == null) {
            _bankNameList.put(loginName, bank);
            Connection cn = null;
            PreparedStatement ps = null;
            try {
                final Timestamp lastactive = new Timestamp(
                        System.currentTimeMillis());

                cn = DatabaseFactory.get().getConnection();
                final String sqlstr = "INSERT INTO `character_bank` SET `account_name`=?,`adena_count`=?,`pass`=?,`settime`=?";

                ps = cn.prepareStatement(sqlstr);
                int i = 0;
                ps.setString(++i, bank.get_account_name());
                ps.setInt(++i, 0);
                ps.setString(++i, bank.get_pass());
                ps.setTimestamp(++i, lastactive);// 建立日期纪录
                ps.execute();

                _log.info("新银行帐号建立: " + bank.get_account_name());

            } catch (final SQLException e) {
                _log.error(e.getLocalizedMessage(), e);

            } finally {
                SQLUtil.close(ps);
                SQLUtil.close(cn);
            }
        }
    }

    /**
     * 更新密码
     * 
     * @param loginName
     *            帐号
     * @param pwd
     *            密码
     */
    @Override
    public void updatePass(final String loginName, final String pwd) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            final String sqlstr = "UPDATE `character_bank` SET `pass`=? WHERE `account_name`=?";
            pstm = con.prepareStatement(sqlstr);
            pstm.setString(1, pwd);

            pstm.setString(2, loginName);
            pstm.execute();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 更新存款
     * 
     * @param loginName
     *            帐号
     * @param adena
     *            金额
     */
    @Override
    public void updateAdena(final String loginName, final long adena) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            final String sqlstr = "UPDATE `character_bank` SET `adena_count`=? WHERE `account_name`=?";
            pstm = con.prepareStatement(sqlstr);
            pstm.setLong(1, adena);

            pstm.setString(2, loginName);
            pstm.execute();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
