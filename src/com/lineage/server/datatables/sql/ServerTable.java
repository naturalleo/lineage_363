package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactoryLogin;
import com.lineage.config.Config;
import com.lineage.server.IdFactory;
import com.lineage.server.datatables.storage.ServerStorage;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 服务器资料
 */
public class ServerTable implements ServerStorage {

    private static final Log _log = LogFactory.getLog(ServerTable.class);

    // 已用最大id编号
    private int _maxId;

    // 可用最小id编号
    private int _minId;

    // 最小编号设置(基础值)
    private static int _srcminId = 0x2710;// 10000

    /**
     * 预先加载服务器存档资料
     */
    @Override
    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactoryLogin.get().getConnection();
            final String sqlstr = "SELECT * FROM `server_info`";
            ps = co.prepareStatement(sqlstr);
            rs = ps.executeQuery();

            boolean isInfo = false;
            while (rs.next()) {
                final int id = rs.getInt("id");
                if (Config.SERVERNO == id) {
                    isInfo = true;
                    final int minid = rs.getInt("minid");
                    final int maxid = rs.getInt("maxid");
                    // final boolean start = rs.getBoolean("start");
                    _minId = minid;
                    if (_minId < _srcminId) {
                        _minId = _srcminId;
                    }
                    _maxId = maxid;
                    set_start();
                }
            }
            if (!isInfo) {
                createServer();
                _minId = _srcminId;// 9000000
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
            SQLUtil.close(rs);
        }
        _log.info("载入服务器存档资料完成  (" + timer.get() + "ms)");
    }

    /**
     * 新服务器资料表建立
     */
    private static void createServer() {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactoryLogin.get().getConnection();
            final String sqlstr = "INSERT INTO `server_info` SET `id`=?,`minid`=?,"
                    + "`maxid`=?,`start`=?";
            ps = cn.prepareStatement(sqlstr);

            int i = 0;
            ps.setInt(++i, Config.SERVERNO);
            ps.setInt(++i, _srcminId);
            ps.setInt(++i, 0);
            ps.setBoolean(++i, true);

            ps.execute();

            _log.info("新服务器资料表建立 - 编号:" + Config.SERVERNO);

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    /**
     * 设定服务器开机
     */
    private void set_start() {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactoryLogin.get().getConnection();
            final String sqlstr = "UPDATE `server_info` SET `start`=? WHERE `id`=?";
            // final String sqlstr =
            // "UPDATE `server_info` SET `start`=?,`ip`=?,`port`=? WHERE `id`=?";
            pstm = con.prepareStatement(sqlstr);

            int i = 0;
            pstm.setBoolean(++i, true);
            // pstm.setString(++i, Config.GAME_SERVER_HOST_NAME);
            // pstm.setString(++i, Config.GAME_SERVER_PORT);

            pstm.setInt(++i, Config.SERVERNO);
            pstm.execute();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 设定服务器关机<BR>
     * 同时记录已用最大编号
     */
    @Override
    public void isStop() {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactoryLogin.get().getConnection();
            final String sqlstr = "UPDATE `server_info` SET `maxid`=?,`start`=? WHERE `id`=?";
            pstm = con.prepareStatement(sqlstr);

            int i = 0;
            pstm.setInt(++i, IdFactory.get().maxId());
            pstm.setBoolean(++i, false);

            pstm.setInt(++i, Config.SERVERNO);
            pstm.execute();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 传回服务器最小编号设置
     */
    @Override
    public int minId() {
        return _minId;
    }

    /**
     * 传回服务器最大编号设置
     */
    @Override
    public int maxId() {
        return _maxId;
    }
}
