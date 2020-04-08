package com.lineage.server.templates;

import java.sql.Timestamp;

public class L1Account {

    private String _login;// 帐号

    private String _password;// 密码

    private Timestamp _lastactive;// 最后登入时间

    private int _access_level;// 帐户等级

    private String _ip;// 登入IP

    private String _mac;// 登入MAC

    private int _character_slot;// 人物扩充

    private String _spw;// 超级密码

    private int _warehouse = -256;// 仓库密码

    private int _countCharacters;// 已创人物数量

    private boolean _isLoad;// 帐户已登入

    private int _server_no;// 帐户登入服务器编号
    
    private int _card_fee = 40;//账号点卡计时 hjx1000 /小时

    /**
     * 帐号
     * 
     * @return 传出 _login
     */
    public String get_login() {
        return _login;
    }

    /**
     * 帐号
     * 
     * @param login
     *            对 _login 进行设置
     */
    public void set_login(String login) {
        this._login = login;
    }

    /**
     * 密码
     * 
     * @return 传出 _password
     */
    public String get_password() {
        return _password;
    }

    /**
     * 密码
     * 
     * @param password
     *            对 _password 进行设置
     */
    public void set_password(String password) {
        this._password = password;
    }

    /**
     * 最后登入时间
     * 
     * @return 传出 _lastactive
     */
    public Timestamp get_lastactive() {
        return _lastactive;
    }

    /**
     * 最后登入时间
     * 
     * @param lastactive
     *            对 _lastactive 进行设置
     */
    public void set_lastactive(Timestamp lastactive) {
        this._lastactive = lastactive;
    }

    /**
     * 帐户等级
     * 
     * @return 传出 _access_level
     */
    public int get_access_level() {
        return _access_level;
    }

    /**
     * 帐户等级
     * 
     * @param access_level
     *            对 _access_level 进行设置
     */
    public void set_access_level(int access_level) {
        this._access_level = access_level;
    }

    /**
     * 登入IP
     * 
     * @return 传出 _ip
     */
    public String get_ip() {
        return _ip;
    }

    /**
     * 登入IP
     * 
     * @param ip
     *            对 _ip 进行设置
     */
    public void set_ip(String ip) {
        this._ip = ip;
    }

    /**
     * 登入MAC
     * 
     * @return 传出 _mac
     */
    public String get_mac() {
        return _mac;
    }

    /**
     * 登入MAC
     * 
     * @param mac
     *            对 _mac 进行设置
     */
    public void set_mac(String mac) {
        this._mac = mac;
    }

    /**
     * 人物扩充
     * 
     * @return 传出 _character_slot
     */
    public int get_character_slot() {
        return _character_slot;
    }

    /**
     * 人物扩充
     * 
     * @param character_slot
     *            对 _character_slot 进行设置
     */
    public void set_character_slot(int character_slot) {
        this._character_slot = character_slot;
    }

    /**
     * 超级密码
     * 
     * @return 传出 _spw
     */
    public String get_spw() {
        return _spw;
    }

    /**
     * 超级密码
     * 
     * @param spw
     *            对 _spw 进行设置
     */
    public void set_spw(String spw) {
        this._spw = spw;
    }

    /**
     * 仓库密码
     * 
     * @return 传出 _warehouse
     */
    public int get_warehouse() {
        return _warehouse;
    }

    /**
     * 仓库密码
     * 
     * @param warehouse
     *            对 _warehouse 进行设置
     */
    public void set_warehouse(int warehouse) {
        this._warehouse = warehouse;
    }

    /**
     * 已创人物数量
     * 
     * @return 传出 _countCharacters
     */
    public int get_countCharacters() {
        return _countCharacters;
    }

    /**
     * 已创人物数量
     * 
     * @param characters
     *            对 _countCharacters 进行设置
     */
    public void set_countCharacters(int characters) {
        _countCharacters = characters;
    }

    /**
     * 帐户已登入
     * 
     * @return 传出 _isLoad true:登入 false:未登入
     */
    public boolean is_isLoad() {
        return _isLoad;
    }

    /**
     * @param load
     *            对 _isLoad 进行设置
     */
    public void set_isLoad(boolean load) {
        _isLoad = load;
    }

    /**
     * 帐户登入服务器编号
     * 
     * @param server_no
     */
    public void set_server_no(int server_no) {
        _server_no = server_no;
    }

    /**
     * 帐户登入服务器编号
     * 
     * @return
     */
    public int get_server_no() {
        return _server_no;
    }

	public int get_card_fee() { //hjx1000
		return _card_fee;
	}

	public void set_card_fee(int _card_fee) { //hjx1000
		this._card_fee = _card_fee;
	}
}
