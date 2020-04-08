package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.OtherUserBuyStorage;
import com.lineage.server.utils.SQLUtil;

/**
 * 买入个人商店物品纪录
 * 
 * @author dexc
 * 
 */
public class OtherUserBuyTable implements OtherUserBuyStorage {

    private static final Log _log = LogFactory.getLog(OtherUserBuyTable.class);

    /**
     * 增加纪录
     * 
     * @param itemname
     *            买入物品名称
     * @param itemobjid
     *            买入物品OBJID
     * @param itemadena
     *            单件物品买入金额
     * @param itemcount
     *            买入数量
     * @param pcobjid
     *            买入者OBJID
     * @param pcname
     *            买入者名称
     * @param srcpcobjid
     *            卖出者OBJID(个人商店)
     * @param srcpcname
     *            卖出者名称(个人商店)
     */
    @Override
    public void add(final String itemname, final int itemobjid,
            final int itemadena, final long itemcount, final int pcobjid,
            final String pcname, final int srcpcobjid, final String srcpcname) {
        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("INSERT INTO `other_pcbuy` SET "
                    + "`itemname`=?,`itemobjid`=?,`itemadena`=?,`itemcount`=?,"
                    + "`pcobjid`=?,`pcname`=?,"
                    + "`srcpcobjid`=?,`srcpcname`=?," + "`datetime`=SYSDATE()");
            int i = 0;
            ps.setString(++i, itemname);
            ps.setInt(++i, itemobjid);
            ps.setInt(++i, itemadena);
            ps.setLong(++i, itemcount);
            ps.setInt(++i, pcobjid);
            ps.setString(++i, pcname + "(买家)");
            ps.setInt(++i, srcpcobjid);
            ps.setString(++i, srcpcname + "(卖家-商店)");
            ps.execute();

        } catch (final Exception e) {
            SqlError.isError(_log, e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }
}
