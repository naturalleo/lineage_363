package com.lineage;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigSQL;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 登入资料库连接设置管理
 */
public class DatabaseFactoryLogin {

    private static final Log _log = LogFactory
            .getLog(DatabaseFactoryLogin.class);

    private static DatabaseFactoryLogin _instance;

    // 连接池
    private ComboPooledDataSource _source;

    // 驱动程式
    private static String _driver;

    // 资料库位置
    private static String _url;

    // 使用者名称
    private static String _user;

    // 使用者密码
    private static String _password;

    /**
     * 初始化设置
     * 
     * @param driver
     *            驱动程式
     * @param url
     *            资料库位置
     * @param user
     *            使用者名称
     * @param password
     *            使用者密码
     */
    public static void setDatabaseSettings() {
        _driver = ConfigSQL.DB_DRIVER;
        _url = ConfigSQL.DB_URL1_LOGIN + ConfigSQL.DB_URL2_LOGIN
                + ConfigSQL.DB_URL3_LOGIN;
        _user = ConfigSQL.DB_LOGIN_LOGIN;
        _password = ConfigSQL.DB_PASSWORD_LOGIN;
    }

    /**
     * 设置资料载入
     * 
     * @throws SQLException
     */
    public DatabaseFactoryLogin() throws SQLException {
        try {
            this._source = new ComboPooledDataSource();
            this._source.setDriverClass(_driver);
            this._source.setJdbcUrl(_url);
            this._source.setUser(_user);
            this._source.setPassword(_password);

            this._source.getConnection().close();

        } catch (final SQLException e) {
            _log.fatal("资料库读取错误!", e);

        } catch (final Exception e) {
            _log.fatal("资料库读取错误!", e);

        }
    }

    /**
     * 资料库连线关闭
     */
    public void shutdown() {
        try {
            this._source.close();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        try {
            this._source = null;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 
     * @return
     * @throws SQLException
     */
    public static DatabaseFactoryLogin get() throws SQLException {
        if (_instance == null) {
            _instance = new DatabaseFactoryLogin();
        }
        return _instance;
    }

    /**
     * 传回资料库连接
     * 
     * @return Connection
     * @throws SQLException
     */
    public Connection getConnection() {
        Connection con = null;

        while (con == null) {
            try {
                con = this._source.getConnection();

            } catch (final SQLException e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
        return con;
    }
}
