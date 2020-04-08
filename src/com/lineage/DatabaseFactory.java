package com.lineage;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigSQL;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 资料库连接设置管理
 */
public class DatabaseFactory {

    private static final Log _log = LogFactory.getLog(DatabaseFactory.class);

    private static DatabaseFactory _instance;

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
     */
    public static void setDatabaseSettings() {
        _driver = ConfigSQL.DB_DRIVER;
        _url = ConfigSQL.DB_URL1 + ConfigSQL.DB_URL2 + ConfigSQL.DB_URL3;
        _user = ConfigSQL.DB_LOGIN;
        _password = ConfigSQL.DB_PASSWORD;
    }

    /**
     * 设置资料载入
     * 
     * @throws SQLException
     */
    public DatabaseFactory() throws SQLException {
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
    public static DatabaseFactory get() throws SQLException {
        if (_instance == null) {
            _instance = new DatabaseFactory();
        }
        return _instance;
    }

    /**
     * 传回资料库连接.
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
