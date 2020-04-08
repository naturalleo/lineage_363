package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.GamblingStorage;
import com.lineage.server.templates.L1Gambling;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 赌场纪录
 * 
 * @author dexc
 * 
 */
public class GamblingTable implements GamblingStorage {

    private static final Log _log = LogFactory.getLog(GamblingTable.class);

    private static final Map<String, L1Gambling> _gamblingList = new HashMap<String, L1Gambling>();

    /**
     * 初始化载入
     */
    @Override
    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `character_gambling` ORDER BY `id`");
            rs = ps.executeQuery();
            while (rs.next()) {
                final L1Gambling gambling = new L1Gambling();

                final int id = rs.getInt("id");
                final long adena = rs.getLong("adena");
                final double rate = rs.getDouble("rate");
                final String gamblingno = rs.getString("gamblingno");
                final int outcount = rs.getInt("outcount");
                gambling.set_id(id);
                gambling.set_adena(adena);
                gambling.set_rate(rate);
                gambling.set_gamblingno(gamblingno);
                gambling.set_outcount(outcount);

                _gamblingList.put(gamblingno, gambling);
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("载入奇岩赌场纪录资料数量: " + _gamblingList.size() + "(" + timer.get()
                + "ms)");
    }

    /**
     * 传回赌场纪录(获胜NPC票号)
     * 
     * @return
     */
    @Override
    public L1Gambling getGambling(final String key) {
        return _gamblingList.get(key);
    }

    /**
     * 传回赌场纪录(场次编号)
     * 
     * @param key
     * @return
     */
    @Override
    public L1Gambling getGambling(final int key) {
        for (final L1Gambling gambling : _gamblingList.values()) {
            if (gambling.get_id() == key) {
                return gambling;
            }
        }
        return null;
    }

    /**
     * 增加赌场纪录
     */
    @Override
    public void add(final L1Gambling gambling) {
        Connection co = null;
        PreparedStatement ps = null;

        final int id = gambling.get_id();
        final String gamblingno = gambling.get_gamblingno();
        _gamblingList.put(gamblingno, gambling);

        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("INSERT INTO `character_gambling` SET "
                    + "`id`=?,`adena`=?,`rate`=?,`gamblingno`=?,`outcount`=?,`r`=?");

            int i = 0;
            ps.setInt(++i, id);
            ps.setLong(++i, gambling.get_adena());// 总收入
            ps.setDouble(++i, gambling.get_rate());// 赔率
            ps.setString(++i, gamblingno);// 获胜彩票
            ps.setInt(++i, gambling.get_outcount());// 可领取卖出总数量
            ps.setInt(++i, gambling.get_outcount());// 可领取卖出总数量(记录)
            ps.execute();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }

    /**
     * 更新赌场纪录<BR>
     * 用于减少可领取卖出总数量
     */
    @Override
    public void updateGambling(final int id, final int outcount) {
        System.out.println("更新赌场纪录" + id + "-" + outcount);
        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("UPDATE `character_gambling` SET `outcount`=? WHERE `id`=?");
            int i = 0;
            ps.setInt(++i, outcount);
            ps.setInt(++i, id);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }

    /**
     * 传回场次数量 与获胜次数
     * 
     * @param npcid
     * @return
     */
    @Override
    public int[] winCount(final int npcid) {
        final int size = _gamblingList.size();
        int winCount = 0;
        for (final L1Gambling gambling : _gamblingList.values()) {
            final String no = gambling.get_gamblingno();
            final int index = no.indexOf("-");
            final int npcidx = Integer.parseInt(no.substring(index + 1));
            if (npcid == npcidx) {
                winCount++;
            }

        }
        return new int[] { size, winCount };
    }

    /**
     * 已用最大ID
     * 
     * @return
     */
    @Override
    public int maxId() {
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `character_gambling` ORDER BY `id`");
            rs = ps.executeQuery();

            int id = 0;
            while (rs.next()) {
                id = rs.getInt("id") + 1;
            }
            if (id < 100) {
                id = 100;
            }
            return id;

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        return 100;
    }
}
