package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1PetType;
import com.lineage.server.utils.RangeInt;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 宠物种族资料
 * 
 * @author dexc
 * 
 */
public class PetTypeTable {

    private static final Log _log = LogFactory.getLog(PetTypeTable.class);

    private static PetTypeTable _instance;

    private static final Map<Integer, L1PetType> _types = new HashMap<Integer, L1PetType>();

    private static final Set<String> _defaultNames = new HashSet<String>();

    public static void load() {
        _instance = new PetTypeTable();
    }

    public static PetTypeTable getInstance() {
        return _instance;
    }

    private PetTypeTable() {
        final PerformanceTimer timer = new PerformanceTimer();
        this.loadTypes();
        _log.info("载入宠物种族资料数量: " + _types.size() + "(" + timer.get() + "ms)");
    }

    private void loadTypes() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM pettypes");

            rs = pstm.executeQuery();

            while (rs.next()) {
                final int baseNpcId = rs.getInt("BaseNpcId");
                final String name = rs.getString("Name");
                final int itemIdForTaming = rs.getInt("ItemIdForTaming");
                final int hpUpMin = rs.getInt("HpUpMin");
                final int hpUpMax = rs.getInt("HpUpMax");
                final int mpUpMin = rs.getInt("MpUpMin");
                final int mpUpMax = rs.getInt("MpUpMax");
                final int npcIdForEvolving = rs.getInt("NpcIdForEvolving");
                final int msgIds[] = new int[5];
                for (int i = 0; i < 5; i++) {
                    msgIds[i] = rs.getInt("MessageId" + (i + 1));
                }
                final int defyMsgId = rs.getInt("DefyMessageId");
                final RangeInt hpUpRange = new RangeInt(hpUpMin, hpUpMax);
                final RangeInt mpUpRange = new RangeInt(mpUpMin, mpUpMax);
                _types.put(baseNpcId, new L1PetType(baseNpcId, name,
                        itemIdForTaming, hpUpRange, mpUpRange,
                        npcIdForEvolving, msgIds, defyMsgId));
                _defaultNames.add(name.toLowerCase());
            }
        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public L1PetType get(final int baseNpcId) {
        return _types.get(baseNpcId);
    }

    public boolean isNameDefault(final String name) {
        return _defaultNames.contains(name.toLowerCase());
    }
}
