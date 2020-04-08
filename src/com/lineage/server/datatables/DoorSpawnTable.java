package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;

/**
 * 门资料
 * 
 * @author dexc
 * 
 */
public class DoorSpawnTable {

    private static final Log _log = LogFactory.getLog(DoorSpawnTable.class);

    private static DoorSpawnTable _instance;

    private static final ArrayList<L1DoorInstance> _doorList = new ArrayList<L1DoorInstance>();

    public static DoorSpawnTable get() {
        if (_instance == null) {
            _instance = new DoorSpawnTable();
        }
        return _instance;
    }

    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        int i = 0;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `spawnlist_door`");
            rs = pstm.executeQuery();
            do {
                if (!rs.next()) {
                    break;
                }
                i++;

                final L1Npc l1npc = NpcTable.get().getTemplate(81158);
                if (l1npc != null) {

                    int id = rs.getInt("id");

                    // 忽略原有的赌场门设置
                    if (id >= 808 && id <= 812) {
                        continue;
                    }

                    final L1DoorInstance door = (L1DoorInstance) NpcTable.get()
                            .newNpcInstance(l1npc);

                    door.setId(IdFactoryNpc.get().nextId());

                    door.setDoorId(id);
                    door.setGfxId(rs.getInt("gfxid"));
                    int x = rs.getInt("locx");
                    int y = rs.getInt("locy");
                    door.setX(x);
                    door.setY(y);
                    door.setMap(rs.getShort("mapid"));
                    door.setHomeX(x);
                    door.setHomeY(y);
                    door.setDirection(rs.getInt("direction"));
                    door.setLeftEdgeLocation(rs.getInt("left_edge_location"));
                    door.setRightEdgeLocation(rs.getInt("right_edge_location"));
                    int hp = rs.getInt("hp");
                    door.setMaxHp(hp);
                    door.setCurrentHp(hp);
                    door.setKeeperId(rs.getInt("keeper"));

                    World.get().storeObject(door);
                    World.get().addVisibleObject(door);

                    _doorList.add(door);
                }
            } while (true);

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } catch (final SecurityException e) {
            _log.error(e.getLocalizedMessage(), e);

        } catch (final IllegalArgumentException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入门资料数量: " + i + "(" + timer.get() + "ms)");
    }

    public L1DoorInstance[] getDoorList() {
        return _doorList.toArray(new L1DoorInstance[_doorList.size()]);
    }
}
