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
import com.lineage.server.templates.L1PetItem;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 宠物道具资料
 * 
 * @author dexc
 * 
 */
public class PetItemTable {

    private static final Log _log = LogFactory.getLog(PetItemTable.class);

    private static PetItemTable _instance;

    private static final Map<Integer, L1PetItem> _petItemIdIndex = new HashMap<Integer, L1PetItem>();

    public static PetItemTable get() {
        if (_instance == null) {
            _instance = new PetItemTable();
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
            pstm = con.prepareStatement("SELECT * FROM petitem");
            rs = pstm.executeQuery();
            this.fillPetItemTable(rs);

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入宠物道具资料数量: " + _petItemIdIndex.size() + "(" + timer.get()
                + "ms)");
    }

    private void fillPetItemTable(final ResultSet rs) throws SQLException {
        while (rs.next()) {
            final L1PetItem petItem = new L1PetItem();
            petItem.setItemId(rs.getInt("item_id"));
            petItem.setHitModifier(rs.getInt("hitmodifier"));
            petItem.setDamageModifier(rs.getInt("dmgmodifier"));
            petItem.setAddAc(rs.getInt("ac"));
            petItem.setAddStr(rs.getInt("add_str"));
            petItem.setAddCon(rs.getInt("add_con"));
            petItem.setAddDex(rs.getInt("add_dex"));
            petItem.setAddInt(rs.getInt("add_int"));
            petItem.setAddWis(rs.getInt("add_wis"));
            petItem.setAddHp(rs.getInt("add_hp"));
            petItem.setAddMp(rs.getInt("add_mp"));
            petItem.setAddSp(rs.getInt("add_sp"));
            petItem.setAddMr(rs.getInt("m_def"));
            petItem.setAddMr(rs.getInt("m_def"));
            petItem.setWeapom(rs.getBoolean("isweapon"));
            petItem.setHigher(rs.getBoolean("ishigher"));
            _petItemIdIndex.put(petItem.getItemId(), petItem);
        }
    }

    public L1PetItem getTemplate(final int itemId) {
        return _petItemIdIndex.get(itemId);
    }

}
