package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.Instance.L1TrapInstance;
import com.lineage.server.templates.L1Trap;
import com.lineage.server.types.Point;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldTrap;

/**
 * 陷阱召唤
 * 
 * @author dexc
 * 
 */
public class TrapsSpawn {

    private static final Log _log = LogFactory.getLog(TrapsSpawn.class);

    private static TrapsSpawn _instance;

    public static TrapsSpawn get() {
        if (_instance == null) {
            _instance = new TrapsSpawn();
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

            pstm = con.prepareStatement("SELECT * FROM `spawnlist_trap`");

            rs = pstm.executeQuery();

            while (rs.next()) {
                final int trapId = rs.getInt("trapId");
                final L1Trap trapTemp = TrapTable.get().getTemplate(trapId);
                // 座标资料
                final L1Location loc = new L1Location();
                loc.setMap(rs.getInt("mapId"));
                loc.setX(rs.getInt("locX"));
                loc.setY(rs.getInt("locY"));

                // 召唤范围点资料
                final Point rndPt = new Point();
                rndPt.setX(rs.getInt("locRndX"));
                rndPt.setY(rs.getInt("locRndY"));

                final int count = rs.getInt("count");// 召唤数量
                final int span = rs.getInt("span");// 陷阱重新动作时间

                // 召唤陷阱
                for (int i = 0; i < count; i++) {
                    final L1TrapInstance trap = new L1TrapInstance(IdFactoryNpc
                            .get().nextId(), trapTemp, loc, rndPt, span);

                    World.get().storeObject(trap);
                    World.get().addVisibleObject(trap);
                }
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入陷阱召唤资料数量: " + WorldTrap.get().map().size() + "("
                + timer.get() + "ms)");
    }

    /**
     * 重新载入所有陷阱
     */
    public void reloadTraps() {
        // 清除原陷阱资料
        for (final Object iter : WorldTrap.get().map().values().toArray()) {
            final L1TrapInstance trap = (L1TrapInstance) iter;
            World.get().removeObject(trap);
            World.get().removeVisibleObject(trap);
        }

        // 重新加载
        load();
    }
}
