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
import com.lineage.server.model.L1NpcTalkData;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * NPC对话资料
 * 
 * @author dexc
 * 
 */
public class NPCTalkDataTable {

    private static final Log _log = LogFactory.getLog(NPCTalkDataTable.class);

    private static NPCTalkDataTable _instance;

    private static final Map<Integer, L1NpcTalkData> _datatable = new HashMap<Integer, L1NpcTalkData>();

    public static NPCTalkDataTable get() {
        if (_instance == null) {
            _instance = new NPCTalkDataTable();
        }
        return _instance;
    }

    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection co = null;
        PreparedStatement pm = null;
        ResultSet rs = null;
        try {

            co = DatabaseFactory.get().getConnection();
            pm = co.prepareStatement("SELECT * FROM npcaction");

            rs = pm.executeQuery();
            while (rs.next()) {
                final L1NpcTalkData action = new L1NpcTalkData();
                final int npcTemplateId = rs.getInt("npcid");
                action.setNpcID(rs.getInt("npcid"));
                final L1Npc temp1 = NpcTable.get().getTemplate(npcTemplateId);

                if (temp1 == null) {
                    _log.error("NPC对话资料编号: " + npcTemplateId + " 不存在资料库中!");
                    delete(npcTemplateId);

                } else {

                    action.setNormalAction(rs.getString("normal_action"));
                    action.setCaoticAction(rs.getString("caotic_action"));
                    action.setTeleportURL(rs.getString("teleport_url"));
                    action.setTeleportURLA(rs.getString("teleport_urla"));

                    _datatable.put(new Integer(action.getNpcID()), action);

                }
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pm);
            SQLUtil.close(co);
        }
        _log.info("载入NPC对话资料数量: " + _datatable.size() + "(" + timer.get()
                + "ms)");
    }

    public L1NpcTalkData getTemplate(final int i) {
        return _datatable.get(new Integer(i));
    }

    private static void delete(final int npc_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `npcaction` WHERE `npcid`=?");
            ps.setInt(1, npc_id);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

}
