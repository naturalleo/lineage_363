package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * Quest(副本)地图设置资料
 * 
 * @author dexc
 * 
 */
public class QuestMapTable {

    private static final Log _log = LogFactory.getLog(QuestMapTable.class);

    private static QuestMapTable _instance;

    // 地图资讯
    private static final Map<Integer, Integer> _mapList = new HashMap<Integer, Integer>();

    // 地图时间限制(单位:秒)
    private static final Map<Integer, Integer> _timeList = new HashMap<Integer, Integer>();

    public static QuestMapTable get() {
        if (_instance == null) {
            _instance = new QuestMapTable();
        }
        return _instance;
    }

    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `server_quest_maps`");
            rs = pstm.executeQuery();

            while (rs.next()) {
                final int mapid = rs.getInt("mapid");// 地图编号
                final int time = rs.getInt("time");// 该地图可进入时间限制 单位:秒(-1无限制)
                final int users = rs.getInt("users");// 允许进入人数(<= 0 不限制)

                /*
                 * 假设该副本对应多张地图 则对应任务编号仅设定在任务开始进入的地一张地图 其余地图均设定-1
                 */

                _mapList.put(new Integer(mapid), new Integer(users));

                if (time > 0) {
                    _timeList.put(new Integer(mapid), new Integer(time));
                }
            }

            _log.info("载入Quest(副本)地图设置资料数量: " + _mapList.size() + "("
                    + timer.get() + "ms)");

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 是Quest(副本)地图设置
     * 
     * @param mapid
     * @return true:是 false:不是
     */
    public boolean isQuestMap(final int mapid) {
        return _mapList.get(new Integer(mapid)) != null;
    }

    /**
     * 传回Quest(副本)地图设置内容
     * 
     * @param mapid
     *            MAP编号
     * @return 可进入人数限制
     */
    public int getTemplate(final int mapid) {
        if (_mapList.get(new Integer(mapid)) != null) {
            return _mapList.get(new Integer(mapid));
        }
        return -1;
    }

    /**
     * 传回Quest(副本)地图时间内容
     * 
     * @param mapid
     *            MAP编号
     * @return 可进入时间限制(单位:秒)
     */
    public Integer getTime(final int mapid) {
        return _timeList.get(new Integer(mapid));
    }

    /**
     * 传回Quest(副本)地图设置清单
     * 
     * @return
     */
    public Map<Integer, Integer> getList() {
        return _mapList;
    }
}
