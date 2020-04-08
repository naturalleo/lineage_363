package com.lineage.server.templates;

import com.lineage.list.OnlineUser;

/**
 * 银行帐户资料
 * 
 * @author loli
 * 
 */
public class L1Bank {

    private String _account_name = null;

    private long _adena_count = 0;

    private String _pass = null;

    public String get_account_name() {
        return _account_name;
    }

    public void set_account_name(String _account_name) {
        this._account_name = _account_name;
    }

    public long get_adena_count() {
        return _adena_count;
    }

    public void set_adena_count(long _adena_count) {
        this._adena_count = _adena_count;
    }

    /**
     * 取款密码(0~9) 6位数
     * 
     * @return
     */
    public String get_pass() {
        return _pass;
    }

    public void set_pass(String _pass) {
        this._pass = _pass;
    }

    /**
     * 该帐号是否连线
     * 
     * @return
     */
    public boolean isLan() {
        return OnlineUser.get().isLan(_account_name);
    }

    /**
     * 该帐号无存款
     * 
     * @return
     */
    public boolean isEmpty() {
        return _adena_count <= 0;
    }
}
