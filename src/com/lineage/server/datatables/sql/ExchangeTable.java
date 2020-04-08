package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.ExchangeStorage;
import com.lineage.server.templates.L1Exchange;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 金币元宝兑换表
 */
public class ExchangeTable implements ExchangeStorage {

    private static final Log _log = LogFactory.getLog(ExchangeTable.class);

    // 金币元宝兑换资料
    private final Map<Integer, L1Exchange> _ExchangeList = new HashMap<Integer, L1Exchange>();

    /**
     * 预先加载金币元宝兑换
     */
    @Override
    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactory.get().getConnection();
            final String sqlstr = "SELECT * FROM `exchange`";
            ps = co.prepareStatement(sqlstr);
            rs = ps.executeQuery();
            L1Exchange l1exchange;
            while (rs.next()) {
                final int npcid = rs.getInt("npcid");
                final int price = rs.getInt("price");
                final int upnumber = rs.getInt("upnumber");

                l1exchange = new L1Exchange();
                l1exchange.set_npcid(npcid);
                l1exchange.set_price_count(price);
                l1exchange.set_upnumber(upnumber);
                _ExchangeList.put(npcid, l1exchange);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
            SQLUtil.close(rs);
        }
        _log.info("载入已有金币元宝兑换资料数量: " + _ExchangeList.size() + "(" + timer.get()
                + "ms)");
    }

    /**
     * 更新比例
     * 
     * @param NPCID
     *            帐号
     * @param adena
     *            金额
     */
    @Override
    public void updateAdena(final int npcid, final int adena) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            final String sqlstr = "UPDATE `exchange` SET `price`=? WHERE `npcid`=?";
            pstm = con.prepareStatement(sqlstr);
            pstm.setInt(1, adena);

            pstm.setInt(2, npcid);
            pstm.execute();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

	@Override
	public L1Exchange getExchangeTable(int id) {
		// TODO Auto-generated method stub
		return _ExchangeList.get(id);
	}
}
