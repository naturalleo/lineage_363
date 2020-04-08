package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.CastleStorage;
import com.lineage.server.templates.L1Castle;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 城堡资料
 * 
 * @author dexc
 * 
 */
public class CastleTable implements CastleStorage {

    private static final Log _log = LogFactory.getLog(CastleTable.class);

    private static final Map<Integer, L1Castle> _castles = new HashMap<Integer, L1Castle>();

    private Calendar timestampToCalendar(final Timestamp ts) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ts.getTime());
        return cal;
    }

    /**
     * 初始化载入
     */
    @Override
    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `server_castle`");

            rs = pstm.executeQuery();
            while (rs.next()) {
                final int castle_id = rs.getInt("castle_id");
                final String name = rs.getString("name");
                final Calendar war_time = this.timestampToCalendar(rs
                        .getTimestamp("war_time"));
                final int tax_rate = rs.getInt("tax_rate");
                final long public_money = rs.getLong("public_money");

                final L1Castle castle = new L1Castle(castle_id, name);
                castle.setWarTime(war_time);
                castle.setTaxRate(tax_rate);
                castle.setPublicMoney(public_money);

                _castles.put(castle_id, castle);
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入城堡资料数量: " + _castles.size() + "(" + timer.get() + "ms)");
    }

    @Override
    public Map<Integer, L1Castle> getCastleMap() {
        return _castles;
    }

    @Override
    public L1Castle[] getCastleTableList() {
        return _castles.values().toArray(new L1Castle[_castles.size()]);
    }

    @Override
    public L1Castle getCastleTable(final int id) {
        return _castles.get(id);
    }

    @Override
    public void updateCastle(final L1Castle castle) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("UPDATE `server_castle` SET `name`=?,`war_time`=?,`tax_rate`=?,`public_money`=? WHERE `castle_id`=?");

            int i = 0;
            pstm.setString(++i, castle.getName());

            // String fm =
            // DateFormat.getDateTimeInstance().format(castle.getWarTime().getTime());

            final SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            final String fm = sdf.format(castle.getWarTime().getTime());

            pstm.setString(++i, fm);
            pstm.setInt(++i, castle.getTaxRate());
            pstm.setLong(++i, castle.getPublicMoney());
            pstm.setInt(++i, castle.getId());
            pstm.execute();

            _castles.put(castle.getId(), castle);
        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
