package com.lineage.server.datatables;

import static com.lineage.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.gametime.L1GameTimeClock;
//import com.lineage.server.serverpackets.S_Teleport2;
import com.lineage.server.serverpackets.S_Teleport; //hjx1000
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 地图切换点设置
 * 
 * @author dexc
 * 
 */
public class DungeonTable {

    private static final Log _log = LogFactory.getLog(DungeonTable.class);

    private static DungeonTable _instance = null;

    private static Map<String, NewDungeon> _dungeonMap = new HashMap<String, NewDungeon>();

    private enum DungeonType {
        NONE, SHIP_FOR_FI, SHIP_FOR_HEINE, SHIP_FOR_PI, SHIP_FOR_HIDDENDOCK, SHIP_FOR_GLUDIN, SHIP_FOR_TI
    };

    public static DungeonTable get() {
        if (_instance == null) {
            _instance = new DungeonTable();
        }
        return _instance;
    }

    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            cn = DatabaseFactory.get().getConnection();

            ps = cn.prepareStatement("SELECT * FROM `dungeon`");
            rs = ps.executeQuery();
            while (rs.next()) {
                final int srcMapId = rs.getInt("src_mapid");
                final int srcX = rs.getInt("src_x");
                final int srcY = rs.getInt("src_y");
                final String key = new StringBuilder().append(srcMapId)
                        .append(srcX).append(srcY).toString();
                final int newX = rs.getInt("new_x");
                final int newY = rs.getInt("new_y");
                final int newMapId = rs.getInt("new_mapid");
                final int heading = rs.getInt("new_heading");
                DungeonType dungeonType = DungeonType.NONE;
                if ((((srcX == 33423) || (srcX == 33424) || (srcX == 33425) || (srcX == 33426))
                        && (srcY == 33502) && (srcMapId == 4 // ハイネ船着场->FI行きの船
                ))
                        || (((srcX == 32733) || (srcX == 32734)
                                || (srcX == 32735) || (srcX == 32736))
                                && (srcY == 32794) && (srcMapId == 83))) { // FI行きの船->ハイネ船着场
                    dungeonType = DungeonType.SHIP_FOR_FI;
                } else if ((((srcX == 32935) || (srcX == 32936) || (srcX == 32937))
                        && (srcY == 33058) && (srcMapId == 70 // FI船着场->ハイネ行きの船
                ))
                        || (((srcX == 32732) || (srcX == 32733)
                                || (srcX == 32734) || (srcX == 32735))
                                && (srcY == 32796) && (srcMapId == 84))) { // ハイネ行きの船->FI船着场
                    dungeonType = DungeonType.SHIP_FOR_HEINE;
                } else if ((((srcX == 32750) || (srcX == 32751) || (srcX == 32752))
                        && (srcY == 32874) && (srcMapId == 445 // 隐された船着场->海贼岛行きの船
                ))
                        || (((srcX == 32731) || (srcX == 32732) || (srcX == 32733))
                                && (srcY == 32796) && (srcMapId == 447))) { // 海贼岛行きの船->隐された船着场
                    dungeonType = DungeonType.SHIP_FOR_PI;
                } else if ((((srcX == 32296) || (srcX == 32297) || (srcX == 32298))
                        && (srcY == 33087) && (srcMapId == 440 // 海贼岛船着场->隐された船着场行きの船
                ))
                        || (((srcX == 32735) || (srcX == 32736) || (srcX == 32737))
                                && (srcY == 32794) && (srcMapId == 446))) { // 隐された船着场行きの船->海贼岛船着场
                    dungeonType = DungeonType.SHIP_FOR_HIDDENDOCK;
                } else if ((((srcX == 32630) || (srcX == 32631) || (srcX == 32632))
                        && (srcY == 32983) && (srcMapId == 0 // TalkingIsland->TalkingIslandShiptoAdenMainland
                ))
                        || (((srcX == 32733) || (srcX == 32734) || (srcX == 32735))
                                && (srcY == 32796) && (srcMapId == 5))) { // TalkingIslandShiptoAdenMainland->TalkingIsland
                    dungeonType = DungeonType.SHIP_FOR_GLUDIN;
                } else if ((((srcX == 32540) || (srcX == 32542)
                        || (srcX == 32543) || (srcX == 32544) || (srcX == 32545))
                        && (srcY == 32728) && (srcMapId == 4 // AdenMainland->AdenMainlandShiptoTalkingIsland
                ))
                        || (((srcX == 32734) || (srcX == 32735)
                                || (srcX == 32736) || (srcX == 32737))
                                && (srcY == 32794) && (srcMapId == 6))) { // AdenMainlandShiptoTalkingIsland->AdenMainland
                    dungeonType = DungeonType.SHIP_FOR_TI;
                }
                //增加随机洞口地图DB设置newX == 0  newY == 0 hjx1000
                //每次重启后随机变更入口
                /*if (newX == 0 && newY == 0) {
                	final L1Map mapData = L1WorldMap.get().getMap((short) srcMapId);
                    final int x = mapData.getX();
                    final int y = mapData.getY();
                    final int height = mapData.getHeight();
                    final int width = mapData.getWidth();
                    final int newx = x + (height / 2);
                    final int newy = y + (width / 2);
                    final L1Location loc = new L1Location(newx, newy, srcMapId);
                    final L1Location srcLocation = loc.randomLocation(300, true);
                    final L1Map mapData1 = L1WorldMap.get().getMap((short) newMapId);
                    final int x1 = mapData1.getX();
                    final int y1 = mapData1.getY();
                    final int height1 = mapData1.getHeight();
                    final int width1 = mapData1.getWidth();
                    final int newx1 = x1 + (height1 / 2);
                    final int newy1 = y1 + (width1 / 2);
                    final L1Location loc1 = new L1Location(newx1, newy1, newMapId);
                    final L1Location newLocation = loc1.randomLocation(300, true);
                    final int srcX1 = srcLocation.getX();
                    final int srcY1 = srcLocation.getY();
                    final int newX1 = newLocation.getX();
                    final int newY1 = newLocation.getY();
                    final String key1 = new StringBuilder().append(srcMapId)
                            .append(srcX1).append(srcY1).toString();
                    final NewDungeon newDungeon1 = new NewDungeon(newX1, newY1,
                            (short) newMapId, heading, dungeonType);
                    _dungeonMap.put(key1, newDungeon1);
                    System.out.println("入口X=="+srcX1+"入口Y=="+srcY1+"入口MAP=="+srcMapId+"出口X=="+newX1+"出口Y=="+newY1);
                    continue;
                }*/
                //增加随机洞口地图
                final NewDungeon newDungeon = new NewDungeon(newX, newY,
                        (short) newMapId, heading, dungeonType);
                if (_dungeonMap.containsKey(key)) {
                    _log.error("相同SRC传送座标(" + key + ")");
                }
                _dungeonMap.put(key, newDungeon);
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("载入地图切换点设置数量: " + _dungeonMap.size() + "(" + timer.get()
                + "ms)");
    }

    private static AtomicInteger _nextId = new AtomicInteger(50000);

    private static class NewDungeon {
        int _id;
        int _newX;
        int _newY;
        short _newMapId;
        int _heading;
        DungeonType _dungeonType;

        private NewDungeon(final int newX, final int newY,
                final short newMapId, final int heading,
                final DungeonType dungeonType) {
            this._id = _nextId.incrementAndGet();
            this._newX = newX;
            this._newY = newY;
            this._newMapId = newMapId;
            this._heading = heading;
            this._dungeonType = dungeonType;

        }
    }

    /**
     * 执行座标移动
     * 
     * @param locX
     * @param locY
     * @param mapId
     * @param pc
     * @return
     */
    public boolean dg(final int locX, final int locY, final int mapId,
            final L1PcInstance pc) {
        final int servertime = L1GameTimeClock.getInstance().currentTime()
                .getSeconds();
        final int nowtime = servertime % 86400;
        final String key = new StringBuilder().append(mapId).append(locX)
                .append(locY).toString();
        
        if (_dungeonMap.containsKey(key)) {
            final NewDungeon newDungeon = _dungeonMap.get(key);
            final DungeonType dungeonType = newDungeon._dungeonType;
            boolean teleportable = false;

            if (dungeonType == DungeonType.NONE) {
                teleportable = true;

            } else {
                if (((nowtime >= 15 * 360) && (nowtime < 25 * 360 // 1.30~2.30
                ))
                        || ((nowtime >= 45 * 360) && (nowtime < 55 * 360 // 4.30~5.30
                        ))
                        || ((nowtime >= 75 * 360) && (nowtime < 85 * 360 // 7.30~8.30
                        ))
                        || ((nowtime >= 105 * 360) && (nowtime < 115 * 360 // 10.30~11.30
                        ))
                        || ((nowtime >= 135 * 360) && (nowtime < 145 * 360))
                        || ((nowtime >= 165 * 360) && (nowtime < 175 * 360))
                        || ((nowtime >= 195 * 360) && (nowtime < 205 * 360))
                        || ((nowtime >= 225 * 360) && (nowtime < 235 * 360))) {
                    if ((pc.getInventory().checkItem(40299, 1) && (dungeonType == DungeonType.SHIP_FOR_GLUDIN)) // TalkingIslandShiptoAdenMainland
                            || (pc.getInventory().checkItem(40301, 1) && (dungeonType == DungeonType.SHIP_FOR_HEINE)) // AdenMainlandShiptoForgottenIsland
                            || (pc.getInventory().checkItem(40302, 1) && (dungeonType == DungeonType.SHIP_FOR_PI))) { // ShipPirateislandtoHiddendock
                        teleportable = true;
                    }
                } else if (((nowtime >= 0) && (nowtime < 360))
                        || ((nowtime >= 30 * 360) && (nowtime < 40 * 360))
                        || ((nowtime >= 60 * 360) && (nowtime < 70 * 360))
                        || ((nowtime >= 90 * 360) && (nowtime < 100 * 360))
                        || ((nowtime >= 120 * 360) && (nowtime < 130 * 360))
                        || ((nowtime >= 150 * 360) && (nowtime < 160 * 360))
                        || ((nowtime >= 180 * 360) && (nowtime < 190 * 360))
                        || ((nowtime >= 210 * 360) && (nowtime < 220 * 360))) {

                    if ((pc.getInventory().checkItem(40298, 1) && (dungeonType == DungeonType.SHIP_FOR_TI)) // AdenMainlandShiptoTalkingIsland
                            || (pc.getInventory().checkItem(40300, 1) && (dungeonType == DungeonType.SHIP_FOR_FI)) // ForgottenIslandShiptoAdenMainland
                            || (pc.getInventory().checkItem(40303, 1) && (dungeonType == DungeonType.SHIP_FOR_HIDDENDOCK))) { // ShipHiddendocktoPirateisland
                        teleportable = true;
                    }
                }
            }

            if (teleportable) {
                final int id = newDungeon._id;
                final short newMap = newDungeon._newMapId;
                final int newX = newDungeon._newX;
                final int newY = newDungeon._newY;
                final int heading = newDungeon._heading;

                // 2秒间は无敌（アブソルートバリア状态）にする。
                pc.setSkillEffect(ABSOLUTE_BARRIER, 2000);
                pc.stopHpRegeneration();
                pc.stopMpRegeneration();

                this.teleport(pc, id, newX, newY, newMap, heading, false);
                return true;
            }
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
        pc.setShow(true); //hjx1000

        //pc.sendPackets(new S_Teleport2(newMap, id));
    }
}
