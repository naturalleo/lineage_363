package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.ServerCnInfoStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.SQLUtil;

/**
 * 天宝购买纪录
 */
public class ServerCnInfoTable implements ServerCnInfoStorage {

    private static final Log _log = LogFactory.getLog(ServerCnInfoTable.class);

    /**
     * 天宝购买纪录
     * 
     * @param pc
     * @param itemtmp
     * @param count
     */
    @Override
    public void create(final L1PcInstance pc, final L1Item itemtmp,
            final int count) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            final Timestamp lastactive = new Timestamp(
                    System.currentTimeMillis());

            con = DatabaseFactory.get().getConnection();
            final String sqlstr = "INSERT INTO `other_cn_shop` SET `itemname`=?,`itemid`=?,`selling_price`=?,`time`=?,`pcobjid`=?";

            pstm = con.prepareStatement(sqlstr);
            int i = 0;
            String pcinfo = "(玩家)";
            if (pc.isGm()) {
                pcinfo = "(管理者)";
            }
            pstm.setString(++i, itemtmp.getName() + pcinfo);
            pstm.setInt(++i, itemtmp.getItemId());
            pstm.setInt(++i, count);
            pstm.setTimestamp(++i, lastactive);
            pstm.setInt(++i, pc.getId());

            pstm.execute();

            _log.info("建立天宝购买纪录: " + pc.getName() + " " + itemtmp.getName()
                    + " " + count);

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
