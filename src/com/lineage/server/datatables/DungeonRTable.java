package com.lineage.server.datatables;

import static com.lineage.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Teleport;
//import com.lineage.server.serverpackets.S_Teleport2;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 地图切换点设置(多点)
 * 
 * @author dexc
 * 
 */
public class DungeonRTable {

    private static final Log _log = LogFactory.getLog(DungeonRTable.class);

    private static DungeonRTable _instance = null;

    private static Map<String, ArrayList<int[]>> _dungeonMap = new HashMap<String, ArrayList<int[]>>();

    private static Map<String, Integer> _dungeonMapID = new HashMap<String, Integer>();

    private static Random _random = new Random();

    public static DungeonRTable get() {
        if (_instance == null) {
            _instance = new DungeonRTable();
        }
        return _instance;
    }

    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        int teltportId = 1000;// 传送点编号

        try {
            cn = DatabaseFactory.get().getConnection();

            ps = cn.prepareStatement("SELECT * FROM `dungeon_random`");
            rs = ps.executeQuery();
            while (rs.next()) {
                final int srcMapId = rs.getInt("src_mapid");
                final int srcX = rs.getInt("src_x");
                final int srcY = rs.getInt("src_y");
                final String key = new StringBuilder().append(srcMapId)
                        .append(srcX).append(srcY).toString();
                if (_dungeonMap.containsKey(key)) {
                    _log.error("相同SRC(多点)传送座标(" + key + ")");

                } else {

                    final int heading = rs.getInt("new_heading");

                    ArrayList<int[]> value = new ArrayList<int[]>();

                    if (rs.getInt("new_x1") != 0) {
                        int newLoc[] = new int[4];
                        newLoc[0] = rs.getInt("new_x1");
                        newLoc[1] = rs.getInt("new_y1");
                        newLoc[2] = rs.getShort("new_mapid1");
                        newLoc[3] = heading;
                        value.add(newLoc);
                    }
                    if (rs.getInt("new_x2") != 0) {
                        int newLoc[] = new int[4];
                        newLoc[0] = rs.getInt("new_x2");
                        newLoc[1] = rs.getInt("new_y2");
                        newLoc[2] = rs.getShort("new_mapid2");
                        newLoc[3] = heading;
                        value.add(newLoc);
                    }
                    if (rs.getInt("new_x3") != 0) {
                        int newLoc[] = new int[4];
                        newLoc[0] = rs.getInt("new_x3");
                        newLoc[1] = rs.getInt("new_y3");
                        newLoc[2] = rs.getShort("new_mapid3");
                        newLoc[3] = heading;
                        value.add(newLoc);
                    }
                    if (rs.getInt("new_x4") != 0) {
                        int newLoc[] = new int[4];
                        newLoc[0] = rs.getInt("new_x4");
                        newLoc[1] = rs.getInt("new_y4");
                        newLoc[2] = rs.getShort("new_mapid4");
                        newLoc[3] = heading;
                        value.add(newLoc);
                    }
                    if (rs.getInt("new_x5") != 0) {
                        int newLoc[] = new int[4];
                        newLoc[0] = rs.getInt("new_x5");
                        newLoc[1] = rs.getInt("new_y5");
                        newLoc[2] = rs.getShort("new_mapid5");
                        newLoc[3] = heading;
                        value.add(newLoc);
                    }

                    _dungeonMap.put(key, value);
                    _dungeonMapID.put(key, teltportId++);
                }
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("载入地图切换点设置(多点)数量: " + _dungeonMapID.size() + "("
                + timer.get() + "ms)");
    }

    public boolean dg(final int locX, final int locY, final int mapId,
            final L1PcInstance pc) {
        final String key = new StringBuilder().append(mapId).append(locX)
                .append(locY).toString();
        if (_dungeonMap.containsKey(key)) {
            final ArrayList<int[]> newLocs = _dungeonMap.get(key);
            final int rnd = _random.nextInt(newLocs.size());
            int[] loc = newLocs.get(rnd);
            final int newX = loc[0];
            final int newY = loc[1];
            final short newMap = (short) loc[2];
            final int heading = loc[3];

            // 2秒内无敌
            pc.setSkillEffect(ABSOLUTE_BARRIER, 2000);
            pc.stopHpRegeneration();
            pc.stopMpRegeneration();

            final int teleportId = _dungeonMapID.get(key);
            this.teleport(pc, teleportId, newX, newY, newMap, heading, false);
            return true;
        }

        return false;
    }

    /**
     * 执行传送
     * 
     * @param pc
     * @param newX
     * @param newY
     * @param newMap
     * @param heading
     * @param b
     */
    private void teleport(final L1PcInstance pc, final int id, final int newX,
            final int newY, final short newMap, final int heading,
            final boolean b) {
        pc.setTeleportX(newX);
        pc.setTeleportY(newY);
        pc.setTeleportMapId(newMap);
        pc.setTeleportHeading(heading);
        pc.sendPackets(new S_Teleport());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //pc.sendPackets(new S_Teleport2(newMap, id));
    }
}
