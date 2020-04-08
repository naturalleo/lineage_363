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
import com.lineage.server.templates.L1Trap;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 陷阱资料
 * 
 * @author dexc
 * 
 */
public class TrapTable {

    private static final Log _log = LogFactory.getLog(TrapTable.class);

    private static TrapTable _instance;

    private static final Map<Integer, L1Trap> _traps = new HashMap<Integer, L1Trap>();

    public static TrapTable get() {
        if (_instance == null) {
            _instance = new TrapTable();
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

            pstm = con.prepareStatement("SELECT * FROM `trap`");

            rs = pstm.executeQuery();

            while (rs.next()) {
                final L1Trap trap = new L1Trap(rs);

                _traps.put(trap.getId(), trap);
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入陷阱资料数量: " + _traps.size() + "(" + timer.get() + "ms)");
    }

    /**
     * 重新加载陷阱
     */
    public static void reload() {
        _instance = new TrapTable();
        TrapTable._traps.clear();

        _instance.load();
    }

    /**
     * 指定陷阱资料
     * 
     * @param id
     * @return
     */
    public L1Trap getTemplate(final int id) {
        return _traps.get(id);
    }
}
