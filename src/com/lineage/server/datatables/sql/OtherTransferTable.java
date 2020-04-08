package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.OtherTransferStorage;
import com.lineage.server.utils.SQLUtil;

/**
 * 转移物品纪录
 * 
 * @author hjx1000
 * 
 */
public class OtherTransferTable implements OtherTransferStorage {

    private static final Log _log = LogFactory.getLog(OtherTransferTable.class);

    /**
     * 增加纪录
     * 
     * @param itemname
     *            转移物品名称
     * @param itemobjid
     *            转移物品OBJID
     * @param itemId
     *            物品ID
     * @param pcobjid
     *            转移者OBJID
     * @param pcname
     *            转移者名称
     * @param srcpcobjid
     *            来源者OBJID
     * @param srcpcname
     *            来源者名称
     */
    @Override
    public void add(final String itemname, final int itemobjid,
            final long itemId, final int pcobjid,
            final String pcname, final int srcpcobjid, final String srcpcname, final String src) {
        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("INSERT INTO `other_transfer` SET "
                    + "`itemname`=?,`itemobjid`=?,`itemcount`=?,"
                    + "`pcobjid`=?,`pcname`=?,`src`=?"
                    + "`srcpcobjid`=?,`srcpcname`=?," + "`datetime`=SYSDATE()");
            int i = 0;
            ps.setString(++i, itemname);
            ps.setInt(++i, itemobjid);
            ps.setLong(++i, itemId);
            ps.setInt(++i, pcobjid);
            ps.setString(++i, pcname + "(转移者)");
            ps.setInt(++i, srcpcobjid);
            ps.setString(++i, srcpcname + "(来源者)");
            ps.setString(++i, src + "(来源)");
            ps.execute();

        } catch (final Exception e) {
            SqlError.isError(_log, e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }
}
