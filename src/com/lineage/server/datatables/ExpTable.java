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
import com.lineage.server.templates.L1Exp;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 经验质资料库
 */
public final class ExpTable {

    private static final Log _log = LogFactory.getLog(ExpTable.class);

    public static int MAX_LEVEL = 0;

    public static long MAX_EXP = 0;

    private static ExpTable _instance;

    private static final Map<Integer, L1Exp> _expList = new HashMap<Integer, L1Exp>();

    public static ExpTable get() {
        if (_instance == null) {
            _instance = new ExpTable();
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
            pstm = con.prepareStatement("SELECT * FROM `exp`");
            rs = pstm.executeQuery();

            L1Exp l1exp;
            while (rs.next()) {
                final int level = rs.getInt("level");
                final long exp = rs.getLong("exp");
                final double expPenalty = rs.getDouble("expPenalty");

                if (level > MAX_LEVEL) {
                    MAX_LEVEL = level;
                }
                if (exp > MAX_EXP) {
                    MAX_EXP = exp;
                }

                l1exp = new L1Exp();
                l1exp.set_level(level);
                l1exp.set_exp(exp);
                l1exp.set_expPenalty(expPenalty);

                _expList.put(new Integer(level), l1exp);
            }

            _log.info("载入经验质设置资料数量: " + _expList.size() + "(" + timer.get()
                    + "ms)");

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        MAX_LEVEL -= 1;
    }

    /**
     * 指定等级所需要的经验直
     * 
     * @param level
     * @return 所需要的经验直
     */
    public static long getExpByLevel(final int level) {
        final L1Exp l1exp = _expList.get(new Integer(level - 1));
        long exp = 0;
        if (l1exp != null) {
            exp = l1exp.get_exp();
        }
        return exp;
    }

    /**
     * 下一个等级需要的经验直
     * 
     * @param level
     * @return 所需要的经验直
     */
    public static long getNeedExpNextLevel(final int level) {
        return getExpByLevel(level + 1) - getExpByLevel(level);
    }

    /**
     * 累积经验直对应的等级
     * 
     * @param exp
     *            累积经验值
     * @return
     */
    public static int getLevelByExp(final long exp) {
        int level = 1;
        for (Integer integer : _expList.keySet()) {
            if (integer == 0) {
                continue;
            }
            L1Exp up = _expList.get(integer);
            L1Exp dn = _expList.get(integer - 1);
            final long upe = up.get_exp();
            final long dne = dn.get_exp();
            if ((exp >= dne) && (exp < upe)) {
                level = integer;
            }
        }
        return Math.min(level, MAX_LEVEL);
    }

    /**
     * 经验直百分比(宠物等级显示使用)
     * 
     * @param level
     * @param exp
     * @return
     */
    public static int getExpPercentage(final int level, final long exp) {
        return (int) (100.0 * ((double) (exp - getExpByLevel(level)) / (double) getNeedExpNextLevel(level)));
    }

    /**
     * 目前等即可取回的经验直
     * 
     * @param level
     * @return
     */
    public static double getPenaltyRate(final int level) {
        final L1Exp l1exp = _expList.get(new Integer(level));
        double expPenalty = 1.0;
        if (l1exp != null) {
            expPenalty = 1.0 / l1exp.get_expPenalty();
        }
        return expPenalty;
    }
}
