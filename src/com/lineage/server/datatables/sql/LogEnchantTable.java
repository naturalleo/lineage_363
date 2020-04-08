package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.LogEnchantStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.SQLUtil;

/**
 * 强化纪录
 * 
 * @author dexc
 * 
 */
public class LogEnchantTable implements LogEnchantStorage {

    private static final Log _log = LogFactory.getLog(LogEnchantTable.class);

    /**
     * 强化纪录(失败)
     * 
     * @param char_id
     * @param item_id
     */
    @Override
    public void failureEnchant(final L1PcInstance pc, final L1ItemInstance item) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `other_enchant` SET "
                    + "`id`=?,`item_id`=?,`char_id`=?,"
                    + "`item_name`=?,`count`=?,`is_equipped`=?,"
                    + "`enchantlvl`=?,`is_id`=?,`durability`=?,"
                    + "`charge_count`=?,`remaining_time`=?,`last_used`=?,"
                    + "`bless`=?,`attr_enchant_kind`=?,`attr_enchant_level`=?,"
                    + "`datetime`=SYSDATE(),`ipmac`=?");

            int i = 0;
            ps.setInt(++i, item.getId());
            ps.setInt(++i, item.getItem().getItemId());
            ps.setInt(++i, pc.getId());
            ps.setString(++i, item.getNumberedName_to_String());
            ps.setLong(++i, item.getCount());
            ps.setInt(++i, 0);
            ps.setInt(++i, item.getEnchantLevel());
            ps.setInt(++i, item.isIdentified() ? 1 : 0);
            ps.setInt(++i, item.get_durability());
            ps.setInt(++i, item.getChargeCount());
            ps.setInt(++i, item.getRemainingTime());
            ps.setTimestamp(++i, item.getLastUsed());
            ps.setInt(++i, item.getBless());
            ps.setInt(++i, item.getAttrEnchantKind());
            ps.setInt(++i, item.getAttrEnchantLevel());

            StringBuilder ip = pc.getNetConnection().getIp();
            StringBuilder mac = pc.getNetConnection().getMac();
            ps.setString(++i, ip + "/" + mac);
            ps.execute();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);

        }
    }
}
