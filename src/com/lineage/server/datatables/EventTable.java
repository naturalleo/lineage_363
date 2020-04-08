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
import com.lineage.data.EventClass;
import com.lineage.server.templates.L1Event;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 活动设置资料
 * 
 * @author dexc
 * 
 */
public class EventTable {

    private static final Log _log = LogFactory.getLog(EventTable.class);

    private static EventTable _instance;

    private static final Map<Integer, L1Event> _eventList = new HashMap<Integer, L1Event>();

    public static EventTable get() {
        if (_instance == null) {
            _instance = new EventTable();
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
            pstm = con
                    .prepareStatement("SELECT * FROM `server_event` ORDER BY `id`");
            rs = pstm.executeQuery();

            L1Event event;
            while (rs.next()) {
                final int id = rs.getInt("id");
                final String eventname = rs.getString("eventname");
                final String eventclass = rs.getString("eventclass");
                final boolean eventstart = rs.getBoolean("eventstart");
                final String eventother = rs.getString("eventother")
                        .replaceAll(" ", "");// 取代空白

                if (eventstart) {
                    event = new L1Event();
                    event.set_eventid(id);
                    event.set_eventname(eventname);
                    event.set_eventclass(eventclass);
                    event.set_eventstart(eventstart);
                    event.set_eventother(eventother);

                    EventClass.get().addList(id, eventclass);
                    _eventList.put(new Integer(id), event);

                    EventClass.get().startEvent(event);
                }
            }

            _log.info("载入活动设置资料数量: " + _eventList.size() + "(" + timer.get()
                    + "ms)");

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 传回活动内容
     * 
     * @param id
     * @return
     */
    public L1Event getTemplate(final int id) {
        return _eventList.get(new Integer(id));
    }

    /**
     * 传回活动清单
     * 
     * @return
     */
    public Map<Integer, L1Event> getList() {
        return _eventList;
    }

    /**
     * 传回活动数量
     * 
     * @return
     */
    public int size() {
        return _eventList.size();
    }
}
