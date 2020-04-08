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
import com.lineage.server.templates.L1GetBackRestart;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 回城座标资料
 * 
 * @author dexc
 * 
 */
public class GetBackRestartTable {

    private static final Log _log = LogFactory
            .getLog(GetBackRestartTable.class);

    private static GetBackRestartTable _instance;

    private static final Map<Integer, L1GetBackRestart> _getbackrestart = new HashMap<Integer, L1GetBackRestart>();

    public static GetBackRestartTable get() {
        if (_instance == null) {
            _instance = new GetBackRestartTable();
        }
        return _instance;
    }

    public void load() {
        // System.out.println(this.getClass().getSimpleName());// XXX
        final PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `getback_restart`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                final L1GetBackRestart gbr = new L1GetBackRestart();
                final int area = rs.getInt("area");
                gbr.setArea(area);
                gbr.setLocX(rs.getInt("locx"));
                gbr.setLocY(rs.getInt("locy"));
                gbr.setMapId(rs.getShort("mapid"));

                _getbackrestart.put(new Integer(area), gbr);
            }
        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入回城座标资料数量: " + _getbackrestart.size() + "(" + timer.get()
                + "ms)");
    }

    /**
     * 增加一笔回村资料
     * 
     * @param area
     *            应该回村的MAP
     * @param locx
     *            回村后的X
     * @param locy
     *            回村后的Y
     * @param map
     *            回村后的MAPID
     */
    public void add(int area, int locx, int locy, int map) {

        L1GetBackRestart tmp = _getbackrestart.get(new Integer(area));
        if (tmp == null) {
            final L1GetBackRestart gbr = new L1GetBackRestart();

            gbr.setArea(area);
            gbr.setLocX(locx);
            gbr.setLocY(locy);
            gbr.setMapId((short) map);

            _getbackrestart.put(new Integer(area), gbr);
        }
    }

    /**
     * 取回该MAP回村资料
     * 
     * @param mapid
     * @return
     */
    public L1GetBackRestart getGetBackRestart(int mapid) {
        L1GetBackRestart tmp = _getbackrestart.get(new Integer(mapid));
        if (tmp == null) {
            return null;
        }
        return tmp;
    }

    /**
     * 取回该MAP回村资料
     * 
     * @return
     */
    public L1GetBackRestart[] getGetBackRestartTableList() {
        return _getbackrestart.values().toArray(
                new L1GetBackRestart[_getbackrestart.size()]);
    }

}
