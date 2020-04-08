package com.lineage.server.model;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.types.Point;

/**
 * 物件座标资讯
 * 
 * @author daien
 * 
 */
public class L1Location extends Point {

    private static final Log _log = LogFactory.getLog(L1Location.class);

    private static Random _random = new Random();

    protected L1Map _map = L1Map.newNull();

    public L1Location() {
        super();
    }

    public L1Location(final L1Location loc) {
        this(loc._x, loc._y, loc._map);
    }

    public L1Location(final int x, final int y, final int mapId) {
        super(x, y);
        this.setMap(mapId);
    }

    public L1Location(final int x, final int y, final L1Map map) {
        super(x, y);
        _map = map;
    }

    public L1Location(final Point pt, final int mapId) {
        super(pt);
        setMap(mapId);
    }

    public L1Location(final Point pt, final L1Map map) {
        super(pt);
        _map = map;
    }

    public void set(final L1Location loc) {
        _map = loc._map;
        _x = loc._x;
        _y = loc._y;
    }

    public void set(final int x, final int y, final int mapId) {
        set(x, y);
        setMap(mapId);
    }

    public void set(final int x, final int y, final L1Map map) {
        set(x, y);
        _map = map;
    }

    public void set(final Point pt, final int mapId) {
        set(pt);
        setMap(mapId);
    }

    public void set(final Point pt, final L1Map map) {
        set(pt);
        _map = map;
    }

    public L1Map getMap() {
        return _map;
    }

    public int getMapId() {
        return _map.getId();
    }

    public void setMap(final L1Map map) {
        _map = map;
    }

    public void setMap(final int mapId) {
        _map = L1WorldMap.get().getMap((short) mapId);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof L1Location)) {
            return false;
        }
        final L1Location loc = (L1Location) obj;
        return (getMap() == loc.getMap()) && (getX() == loc.getX())
                && (getY() == loc.getY());
    }

    @Override
    public int hashCode() {
        return 7 * _map.getId() + hashCode();
    }

    @Override
    public String toString() {
        return String.format("(%d, %d) on %d", _x, _y, _map.getId());
    }

    /**
     * 返回此座标可移动随机范围的位置。
     * 
     * @param max
     *            最大范围质
     * @param isRandomTeleport
     *            是否闪避城区 小屋区
     * @return 新座标
     */
    public L1Location randomLocation(final int max,
            final boolean isRandomTeleport) {
        return randomLocation(0, max, isRandomTeleport);
    }

    /**
     * 取回随机传送座标
     * 
     * @param min
     *            范围最小质
     * @param max
     *            范围最大质
     * @param isRandomTeleport
     *            是否闪避小屋 与战争区域座标
     * @return 新的Location
     */
    public L1Location randomLocation(final int min, final int max,
            final boolean isRandomTeleport) {
        return L1Location.randomLocation(this, min, max, isRandomTeleport);
    }

    /**
     * 取回随机传送座标
     * 
     * @param baseLocation
     *            自己的L1Location
     * @param min
     *            范围最小质
     * @param max
     *            范围最大质
     * @param isRandomTeleport
     *            是否闪避小屋 与战争区域座标
     * @return 新的Location
     */
    public static L1Location randomLocation(final L1Location baseLocation,
            int min, final int max, final boolean isRandomTeleport) {
        L1Location newLocation = null;
        try {
            if (min > max) {
                throw new IllegalArgumentException("min > max 设定异常");
            }
            if (max <= 0) {
                return new L1Location(baseLocation);
            }
            if (min < 0) {
                min = 0;
            }

            newLocation = new L1Location();
            int newX = 0;
            int newY = 0;
            final int locX = baseLocation.getX();
            final int locY = baseLocation.getY();
            final short mapId = (short) baseLocation.getMapId();
            final L1Map map = baseLocation.getMap();

            newLocation.setMap(map);

            int locX1 = locX - max;
            int locX2 = locX + max;
            int locY1 = locY - max;
            int locY2 = locY + max;

            // map范围
            final int mapX1 = map.getX();
            final int mapX2 = mapX1 + map.getWidth();
            final int mapY1 = map.getY();
            final int mapY2 = mapY1 + map.getHeight();

            // 最大でもマップの范围内までに补正
            if (locX1 < mapX1) {
                locX1 = mapX1;
            }
            if (locX2 > mapX2) {
                locX2 = mapX2;
            }
            if (locY1 < mapY1) {
                locY1 = mapY1;
            }
            if (locY2 > mapY2) {
                locY2 = mapY2;
            }

            final int diffX = locX2 - locX1; // x方向
            final int diffY = locY2 - locY1; // y方向

            int trial = 0;
            // 试行回数を范围最小值によってあげる为の计算
            final int amax = (int) Math.pow(1 + (max * 2), 2);
            final int amin = (min == 0) ? 0 : (int) Math.pow(
                    1 + ((min - 1) * 2), 2);
            final int trialLimit = 40 * amax / (amax - amin);

            boolean run = true;
            while (run) {
                // Thread.sleep(2);
                if (trial >= trialLimit) {
                    newLocation.set(locX, locY);
                    run = false;
                    break;
                }

                trial++;
                try {
                    newX = locX1 + L1Location._random.nextInt(diffX + 1);
                    newY = locY1 + L1Location._random.nextInt(diffY + 1);
                } catch (final Exception e) {
                    newLocation.set(locX, locY);
                    run = false;
                    break;
                }

                newLocation.set(newX, newY);

                if (baseLocation.getTileLineDistance(newLocation) < min) {
                    continue;

                }
                if (isRandomTeleport) { // 闪避小屋 与战争区域座标
                    // 战争区域
                    if (L1CastleLocation.checkInAllWarArea(newX, newY, mapId)) {
                        continue;
                    }

                    // 小屋内座标
                    if (L1HouseLocation.isInHouse(newX, newY, mapId)) {
                        continue;
                    }
                }
                // 位置可通行中断线程
                if (map.isInMap(newX, newY) && map.isPassable(newX, newY, null)) {
                    run = false;
                    break;
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return newLocation;
    }
}
