package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.OtherUserToPcStorage;
import com.lineage.server.utils.SQLUtil;

/**
 * GM物品纪录
 * 
 * @author dexc
 * 
 */
public class OtherUserToPcTable implements OtherUserToPcStorage {

    private static final Log _log = LogFactory.getLog(OtherUserToPcTable.class);

    /**
     * 增加纪录
     * 
     * @param itemname
     *            物品名称
     * @param itemcount
     *            数量
     * @param pcobjid
     *            GM OBJID
     * @param pcname
     *            GM名称
     * @param srcpcobjid
     *            对象OBJID
     * @param srcpcname
     *            对象名称
     */
    @Override
    public void add(final String itemname, final long itemcount,
            final int pcobjid, final String pcname, final int srcpcobjid,
            final String srcpcname) {
        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("INSERT INTO `other_topc` SET "
                    + "`itemname`=?,`itemcount`=?," + "`pcobjid`=?,`pcname`=?,"
                    + "`srcpcobjid`=?,`srcpcname`=?," + "`datetime`=SYSDATE()");
            int i = 0;
            ps.setString(++i, itemname);
            ps.setLong(++i, itemcount);
            ps.setInt(++i, pcobjid);
            ps.setString(++i, pcname);
            ps.setInt(++i, srcpcobjid);
            ps.setString(++i, srcpcname);
            ps.execute();

        } catch (final Exception e) {
            SqlError.isError(_log, e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }
}
