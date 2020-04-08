package com.lineage.server.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.server.datatables.MapsTable;
import com.lineage.server.model.L1GroundInventory;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1BowInstance;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1DwarfInstance;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MerchantInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.Instance.L1TrapInstance;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.types.Point;

/**
 * 游戏世界储存中心
 * 
 * @author dexc
 * 
 */
public class World {

    private static final Log _log = LogFactory.getLog(World.class);

    // 世界人物资料<pcName, Object>
    private final ConcurrentHashMap<String, L1PcInstance> _allPlayers;

    // 世界物件资料<Objid, Object>
    private final ConcurrentHashMap<Integer, L1Object> _allObjects;

    // 世界物件资料(区分地图编号)<MapId, <Objid, Object>>
    private final HashMap<Integer, ConcurrentHashMap<Integer, L1Object>> _visibleObjects;

    private static World _instance;

    public World() {
        _allPlayers = new ConcurrentHashMap<String, L1PcInstance>(); // 世界人物资料
        _allObjects = new ConcurrentHashMap<Integer, L1Object>(); // 世界物件资料
        _visibleObjects = new HashMap<Integer, ConcurrentHashMap<Integer, L1Object>>(); // 世界物件资料(区分地图编号)

        for (Integer mapid : MapsTable.get().getMaps().keySet()) {
            final ConcurrentHashMap<Integer, L1Object> map = new ConcurrentHashMap<Integer, L1Object>();
            _visibleObjects.put(mapid, map);
        }
//        for (int i=10001; i<12000; i++) {// 增加虚拟地图 map object 防止报错 hjx1000
//        	final ConcurrentHashMap<Integer, L1Object> map = new ConcurrentHashMap<Integer, L1Object>();
//        	_visibleObjects.put(i, map);
//        }
        _log.info("游戏世界储存中心建立完成!!!");
    }

    public static World get() {
        if (_instance == null) {
            _instance = new World();
        }
        return _instance;
    }

    public Object getRegion(final Object object) {
        return null;
    }

    /**
     * 世界资料状态全部重置
     */
    public void clear() {
        _instance = new World();
    }

    /**
     * 加入世界
     * 
     * @param object
     */
    public void storeObject(final L1Object object) {
        try {
            if (object == null) {
                throw new NullPointerException();
            }

            _allObjects.put(object.getId(), object);

            if (object instanceof L1ItemInstance) {
                WorldItem.get().put(new Integer(object.getId()),
                        (L1ItemInstance) object);
            }

            if (object instanceof L1PcInstance) {
                final L1PcInstance pc = (L1PcInstance) object;

                if (pc.isCrown()) {
                    WorldCrown.get().put(new Integer(pc.getId()), pc);

                } else if (pc.isKnight()) {
                    WorldKnight.get().put(new Integer(pc.getId()), pc);

                } else if (pc.isElf()) {
                    WorldElf.get().put(new Integer(pc.getId()), pc);

                } else if (pc.isWizard()) {
                    WorldWizard.get().put(new Integer(pc.getId()), pc);

                } else if (pc.isDarkelf()) {
                    WorldDarkelf.get().put(new Integer(pc.getId()), pc);

                } else if (pc.isDragonKnight()) {
                    WorldDragonKnight.get().put(new Integer(pc.getId()), pc);

                } else if (pc.isIllusionist()) {
                    WorldIllusionist.get().put(new Integer(pc.getId()), pc);
                }
                _allPlayers.put(pc.getName(), pc);
            }
            // 陷阱
            if (object instanceof L1TrapInstance) {
                WorldTrap.get().put(new Integer(object.getId()),
                        (L1TrapInstance) object);
            }
            // 宠物
            if (object instanceof L1PetInstance) {
                WorldPet.get().put(new Integer(object.getId()),
                        (L1PetInstance) object);
            }
            // 召唤兽
            if (object instanceof L1SummonInstance) {
                WorldSummons.get().put(new Integer(object.getId()),
                        (L1SummonInstance) object);
            }
            // 魔法娃娃
            if (object instanceof L1DollInstance) {
                WorldDoll.get().put(new Integer(object.getId()),
                        (L1DollInstance) object);
            }
            // Effect(技能物件)
            if (object instanceof L1EffectInstance) {
                WorldEffect.get().put(new Integer(object.getId()),
                        (L1EffectInstance) object);
            }
            // MOB
            if (object instanceof L1MonsterInstance) {
                WorldMob.get().put(new Integer(object.getId()),
                        (L1MonsterInstance) object);
            }
            // BOW
            if (object instanceof L1BowInstance) {
                WorldBow.get().put(new Integer(object.getId()),
                        (L1BowInstance) object);
            }
            // NPC
            if (object instanceof L1NpcInstance) {
                WorldNpc.get().put(new Integer(object.getId()),
                        (L1NpcInstance) object);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 移出世界
     * 
     * @param object
     */
    public void removeObject(final L1Object object) {
        // System.out.println("移出世界");
        try {
            if (object == null) {
                throw new NullPointerException();
            }

            _allObjects.remove(object.getId());

            if (object instanceof L1ItemInstance) {
                WorldItem.get().remove(new Integer(object.getId()));
            }

            if (object instanceof L1PcInstance) {
                final L1PcInstance pc = (L1PcInstance) object;

                if (pc.isCrown()) {
                    WorldCrown.get().remove(new Integer(pc.getId()));

                } else if (pc.isKnight()) {
                    WorldKnight.get().remove(new Integer(pc.getId()));

                } else if (pc.isElf()) {
                    WorldElf.get().remove(new Integer(pc.getId()));

                } else if (pc.isWizard()) {
                    WorldWizard.get().remove(new Integer(pc.getId()));

                } else if (pc.isDarkelf()) {
                    WorldDarkelf.get().remove(new Integer(pc.getId()));

                } else if (pc.isDragonKnight()) {
                    WorldDragonKnight.get().remove(new Integer(pc.getId()));

                } else if (pc.isIllusionist()) {
                    WorldIllusionist.get().remove(new Integer(pc.getId()));
                }
                _allPlayers.remove(pc.getName());
            }
            // 陷阱
            if (object instanceof L1TrapInstance) {
                WorldTrap.get().remove(new Integer(object.getId()));
            }
            // 宠物
            if (object instanceof L1PetInstance) {
                WorldPet.get().remove(new Integer(object.getId()));
            }
            // 召唤兽
            if (object instanceof L1SummonInstance) {
                WorldSummons.get().remove(new Integer(object.getId()));
            }
            // 魔法娃娃
            if (object instanceof L1DollInstance) {
                WorldDoll.get().remove(new Integer(object.getId()));
            }
            // Effect(技能物件)
            if (object instanceof L1EffectInstance) {
                WorldEffect.get().remove(new Integer(object.getId()));
            }
            // MOB
            if (object instanceof L1MonsterInstance) {
                WorldMob.get().remove(new Integer(object.getId()));
            }
            // BOW
            if (object instanceof L1BowInstance) {
                WorldBow.get().remove(new Integer(object.getId()));
            }
            // NPC
            if (object instanceof L1NpcInstance) {
                WorldNpc.get().remove(new Integer(object.getId()));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 取回物件
     * 
     * @param oID
     * @return
     */
    public L1Object findObject(final int oID) {
        if (oID == 0) {
            return null;
        }
        return _allObjects.get(oID);
    }

    // _allObjects 的 Collection
    private Collection<L1Object> _allValues;

    /**
     * 全部L1Object
     * 
     * @return
     */
    public Collection<L1Object> getObject() {
        try {
            final Collection<L1Object> vs = _allValues;
            return (vs != null) ? vs : (_allValues = Collections
                    .unmodifiableCollection(_allObjects.values()));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 座标点-游戏世界地面背包
     * 
     * @param x
     * @param y
     * @param map
     * @return
     */
    public L1GroundInventory getInventory(final int x, final int y,
            final short map) {
        try {
            // 以XY座标值做为地面背包OBJID(避免冲突 转为负数)
            final int inventoryKey = ((x - 30000) * 10000 + (y - 30000)) * -1;
            // Integer
            final ConcurrentHashMap<Integer, L1Object> idmap = _visibleObjects
                    .get(new Integer(map));
            if (idmap != null) {
                final Object object = idmap.get(inventoryKey);

                if (object == null) {
                    return new L1GroundInventory(inventoryKey, x, y, map);

                } else {
                    return (L1GroundInventory) object;
                }

            } else {
                _log.error("游戏世界储存中心并未建立该地图编号资料档案1: " + map);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 座标点-游戏世界地面背包
     * 
     * @param loc
     * @return
     */
    public L1GroundInventory getInventory(final L1Location loc) {
        return getInventory(loc.getX(), loc.getY(), (short) loc.getMap()
                .getId());
    }

    /**
     * 加入MAP内物件
     * 
     * @param object
     */
    public void addVisibleObject(final L1Object object) {
        try {
            final ConcurrentHashMap<Integer, L1Object> map = _visibleObjects
                    .get(new Integer(object.getMapId()));
            if (map != null) {
                map.put(object.getId(), object);

            } else {
                _log.error("游戏世界储存中心并未建立该地图编号资料档案2: " + object.getMapId());
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 移除MAP内物件
     * 
     * @param object
     */
    public void removeVisibleObject(final L1Object object) {
        try {
            final ConcurrentHashMap<Integer, L1Object> map = _visibleObjects
                    .get(new Integer(object.getMapId()));
            if (map != null) {
                map.remove(object.getId());

            } else {
                _log.error("游戏世界储存中心并未建立该地图编号资料档案3: " + object.getMapId());
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 移动MAP内物件
     * 
     * @param object
     * @param newMapId
     */
    public void moveVisibleObject(final L1Object object, final int newMapId) {// set_Mapで新しいMapにするまえに呼ぶこと
        try {
            int srcMapId = object.getMapId();
            if (srcMapId != newMapId) {
                // 取回原地图资料
                final ConcurrentHashMap<Integer, L1Object> mapSrc = _visibleObjects
                        .get(new Integer(srcMapId));
                if (mapSrc != null) {
                    mapSrc.remove(object.getId());

                } else {
                    _log.error("游戏世界储存中心并未建立该地图编号资料档案4: " + srcMapId);
                }

                // 取回新地图资料
                final ConcurrentHashMap<Integer, L1Object> map = _visibleObjects
                        .get(new Integer(newMapId));
                if (map != null) {
                    map.put(object.getId(), object);

                } else {
                    _log.error("游戏世界储存中心并未建立该地图编号资料档案5: " + newMapId);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 直线上目标列举
     * 
     * @param src
     * @param target
     * @return
     */
    private HashMap<Integer, Integer> createLineMap(final Point src,
            final Point target) {
        final HashMap<Integer, Integer> lineMap = new HashMap<Integer, Integer>();

        /*
         * http://www2.starcat.ne.jp/~fussy/algo/algo1-1.htmより
         */
        int E;
        int x;
        int y;
        int key;
        int i;
        final int x0 = src.getX();
        final int y0 = src.getY();
        final int x1 = target.getX();
        final int y1 = target.getY();
        final int sx = (x1 > x0) ? 1 : -1;
        final int dx = (x1 > x0) ? x1 - x0 : x0 - x1;
        final int sy = (y1 > y0) ? 1 : -1;
        final int dy = (y1 > y0) ? y1 - y0 : y0 - y1;

        x = x0;
        y = y0;
        /* 倾きが1以下の场合 */
        if (dx >= dy) {
            E = -dx;
            for (i = 0; i <= dx; i++) {
                key = (x << 16) + y;
                lineMap.put(key, key);
                x += sx;
                // E += 2 * dy;
                E += (dy << 1);
                if (E >= 0) {
                    y += sy;
                    // E -= 2 * dx;
                    E -= (dx << 1);
                }
            }
            /* 倾きが1より大きい场合 */
        } else {
            E = -dy;
            for (i = 0; i <= dy; i++) {
                key = (x << 16) + y;
                lineMap.put(key, key);
                y += sy;
                // E += 2 * dx;
                E += (dx << 1);
                if (E >= 0) {
                    x += sx;
                    // E -= 2 * dy;
                    E -= (dy << 1);
                }
            }
        }

        return lineMap;
    }

    /**
     * 直线距离目标列举
     * 
     * @param src
     * @param target
     * @return
     */
    public ArrayList<L1Object> getVisibleLineObjects(final L1Object src,
            final L1Object target) {
        try {
            final HashMap<Integer, Integer> lineMap = this.createLineMap(
                    src.getLocation(), target.getLocation());

            final int map = target.getMapId();
            final ArrayList<L1Object> result = new ArrayList<L1Object>();

            // 取回原地图资料
            final ConcurrentHashMap<Integer, L1Object> mapSrc = _visibleObjects
                    .get(new Integer(map));
            if (mapSrc == null) {
                _log.error("游戏世界储存中心并未建立该地图编号资料档案6: " + map);
                return result;
            }

            for (final L1Object element : mapSrc.values()) {
                if (element.equals(src)) {
                    continue;
                }

                // 副本ID不相等
                if (src.get_showId() != element.get_showId()) {
                    continue;
                }

                final int key = (element.getX() << 16) + element.getY();
                if (lineMap.containsKey(key)) {
                    result.add(element);
                }
            }
            lineMap.clear();
            return result;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 指定范围目标列举
     * 
     * @param object
     * @param heading
     * @param width
     * @param height
     * @return
     */
    public ArrayList<L1Object> getVisibleBoxObjects(final L1Object object,
            final int heading, final int width, final int height) {
        final int x = object.getX();
        final int y = object.getY();
        final int map = object.getMapId();
        final L1Location location = object.getLocation();
        final ArrayList<L1Object> result = new ArrayList<L1Object>();
        final int headingRotate[] = { 6, 7, 0, 1, 2, 3, 4, 5 };
        final double cosSita = Math.cos((headingRotate[heading] * Math.PI) / 4);
        final double sinSita = Math.sin((headingRotate[heading] * Math.PI) / 4);

        // 取回原地图资料
        final ConcurrentHashMap<Integer, L1Object> mapSrc = _visibleObjects
                .get(new Integer(map));
        if (mapSrc == null) {
            _log.error("游戏世界储存中心并未建立该地图编号资料档案7: " + map);
            return result;
        }

        for (final L1Object element : mapSrc.values()) {
            if (element.equals(object)) {
                continue;
            }

            // 副本ID不相等
            if (object.get_showId() != element.get_showId()) {
                continue;
            }

            if (map != element.getMapId()) {
                continue;
            }

            // 同じ座标に重なっている场合は范围内とする
            if (location.isSamePoint(element.getLocation())) {
                result.add(element);
                continue;
            }

            final int distance = location.getTileLineDistance(element
                    .getLocation());
            // 直线距离が高さ、幅どちらよりも大きい场合、计算するまでもなく范围外
            if ((distance > height) && (distance > width)) {
                continue;
            }

            // objectの位置を原点とするための座标补正
            final int x1 = element.getX() - x;
            final int y1 = element.getY() - y;

            // Z轴回转させ角度を0度にする。
            final int rotX = (int) Math.round(x1 * cosSita + y1 * sinSita);
            final int rotY = (int) Math.round(-x1 * sinSita + y1 * cosSita);

            final int xmin = 0;
            final int xmax = height;
            final int ymin = -width;
            final int ymax = width;

            // 奥行きが射程とかみ合わないので直线距离で判定するように变更。
            // if (rotX > xmin && rotX <= xmax && rotY >= ymin && rotY <=
            // ymax) {
            if ((rotX > xmin) && (distance <= xmax) && (rotY >= ymin)
                    && (rotY <= ymax)) {
                result.add(element);
            }
        }

        return result;
    }

    /**
     * 画面可见范围Objects
     * 
     * @param object
     * @return
     */
    public ArrayList<L1Object> getVisibleObjects(final L1Object object) {
        return getVisibleObjects(object, -1);
    }

    /**
     * 相对物件在画面范围内
     * 
     * @param src
     * @param tg
     * @return
     */
    public boolean getVisibleObjects(final L1Object src, final L1Object tg) {
        // 副本ID不相等
        if (src.get_showId() != tg.get_showId()) {
            return false;
        }
        // 判断对象MAP不相等
        if (src.getMapId() != tg.getMapId()) {
            return false;
        }
        // 画面范围内
        if (src.getLocation().isInScreen(tg.getLocation())) {
            return true;
        }
        return false;
    }

    /**
     * 范围物件
     * 
     * @param object
     *            原始物件
     * @param radius
     *            范围 -1:全视窗 0:重叠 其他:范围
     * @return
     */
    public ArrayList<L1Object> getVisibleObjects(final L1Object object,
            final int radius) {
        final L1Map map = object.getMap();
        final Point pt = object.getLocation();
        final ArrayList<L1Object> result = new ArrayList<L1Object>();

        // 取回原地图资料
        final ConcurrentHashMap<Integer, L1Object> mapSrc = _visibleObjects
                .get(new Integer(map.getId()));
        if (mapSrc == null) {
            _log.error("游戏世界储存中心并未建立该地图编号资料档案8: " + map.getId());
            return result;
        }

        for (final L1Object element : mapSrc.values()) {
            // 判断对象是自己
            if (element.equals(object)) {
                continue;
            }

            // 副本ID不相等
            if (object.get_showId() != element.get_showId()) {
                boolean is = false;
                if (element instanceof L1MerchantInstance) {// 对话NPC
                    is = true;
                    ;
                }
                if (element instanceof L1DwarfInstance) {// 仓库NPC
                    is = true;
                    ;
                }
                if (!is) {
                    continue;
                }
            }

            // 判断对象MAP不相等
            if (map != element.getMap()) {
                continue;
            }
            switch (radius) {
                case -1:
                    if (pt.isInScreen(element.getLocation())) {
                        result.add(element);
                    }
                    break;

                case 0:
                    if (pt.isSamePoint(element.getLocation())) {
                        result.add(element);
                    }
                    break;

                default:
                    int r = pt.getTileLineDistance(element.getLocation());
                    if (r <= radius) {
                        result.add(element);
                    }
                    break;
            }
        }

        return result;
    }

    /**
     * 范围物件(PC集体传送使用)
     * 
     * @param loc
     *            原始座标
     * @param radius
     *            范围
     * @param showid
     *            副本ID
     * @return
     */
    public ArrayList<L1Object> getVisiblePoint(final L1Location loc,
            final int radius, final int showid) {
        final ArrayList<L1Object> result = new ArrayList<L1Object>();
        final int mapId = loc.getMapId(); // ループ内で呼ぶと重いため

        // 取回原地图资料
        final ConcurrentHashMap<Integer, L1Object> mapSrc = _visibleObjects
                .get(new Integer(mapId));
        if (mapSrc == null) {
            _log.error("游戏世界储存中心并未建立该地图编号资料档案9: " + mapId);
            return result;
        }

        for (final L1Object element : mapSrc.values()) {
            if (mapId != element.getMapId()) {
                continue;
            }

            // 副本ID不相等
            if (showid != element.get_showId()) {
                continue;
            }

            if (loc.getTileLineDistance(element.getLocation()) <= radius) {
                result.add(element);
            }
        }

        return result;
    }

    /**
     * 可见范围PC物件(一般召唤使用 不考虑副本ID)
     * 
     * @param loc
     *            原始座标
     * @param showId
     * @return
     */
    public ArrayList<L1PcInstance> getVisiblePc(final L1Location loc) {
        final ArrayList<L1PcInstance> result = new ArrayList<L1PcInstance>();
        final int mapId = loc.getMapId(); // ループ内で呼ぶと重いため

        // 取回原地图资料
        final ConcurrentHashMap<Integer, L1Object> mapSrc = _visibleObjects
                .get(new Integer(mapId));
        if (mapSrc == null) {
            _log.error("游戏世界储存中心并未建立该地图编号资料档案10: " + mapId);
            return result;
        }

        for (final L1PcInstance element : this._allPlayers.values()) {
            if (mapId != element.getMapId()) {
                continue;
            }
            if (element.isDead()) { //死亡的玩家不影响刷怪 hjx1000
            	continue;
            }

            if (loc.isInScreen(element.getLocation())) {
                result.add(element);
            }
        }

        return result;
    }

    /**
     * 画面可见人物
     * 
     * @param object
     *            原始执行物件
     * @return
     */
    public ArrayList<L1PcInstance> getVisiblePlayer(final L1Object object) {
        return this.getVisiblePlayer(object, -1);
    }

    /**
     * 画面可见人物
     * 
     * @param object
     *            原始执行物件
     * @param radius
     *            -1:13格范围 0:座标重叠 其它:指定范围
     * @return
     */
    public ArrayList<L1PcInstance> getVisiblePlayer(final L1Object object,
            final int radius) {
        final int map = object.getMapId();
        final Point pt = object.getLocation();
        final ArrayList<L1PcInstance> result = new ArrayList<L1PcInstance>();

        for (final L1PcInstance element : _allPlayers.values()) {
            if (element.equals(object)) {
                continue;
            }

            if (map != element.getMapId()) {
                continue;
            }

            // 副本ID不相等
            if (object.get_showId() != element.get_showId()) {
                continue;
            }

            switch (radius) {
                case -1:
                    if (pt.isInScreen(element.getLocation())) {
                        result.add(element);
                    }
                    break;

                case 0:
                    if (pt.isSamePoint(element.getLocation())) {
                        result.add(element);
                    }
                    break;

                default:
                    if (pt.getTileLineDistance(element.getLocation()) <= radius) {
                        result.add(element);
                    }
                    break;
            }
        }
        return result;
    }

    /**
     * 指定目标物件 获取可见范围PC物件
     * 
     * @param object
     *            执行者
     * @param target
     *            指定物件
     * @return
     */
    public ArrayList<L1PcInstance> getVisiblePlayerExceptTargetSight(
            final L1Object object, final L1Object target) {
        final int map = object.getMapId();
        final Point objectPt = object.getLocation();
        final Point targetPt = target.getLocation();
        final ArrayList<L1PcInstance> result = new ArrayList<L1PcInstance>();

        for (final L1PcInstance element : _allPlayers.values()) {
            if (element.equals(object)) {
                continue;
            }

            if (map != element.getMapId()) {
                continue;
            }

            // 副本ID不相等
            if (target.get_showId() != element.get_showId()) {
                continue;
            }

            if (Config.PC_RECOGNIZE_RANGE == -1) {
                if (objectPt.isInScreen(element.getLocation())) {
                    if (!targetPt.isInScreen(element.getLocation())) {
                        result.add(element);
                    }
                }
            } else {
                if (objectPt.getTileLineDistance(element.getLocation()) <= Config.PC_RECOGNIZE_RANGE) {
                    if (targetPt.getTileLineDistance(element.getLocation()) > Config.PC_RECOGNIZE_RANGE) {
                        result.add(element);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 传回目标物件 指定范围内物件 排除原始物件
     * 
     * @param object
     *            原始物件
     * @param target
     *            目标物件
     * @param radius
     *            范围
     * @return
     */
    public ArrayList<L1PcInstance> getVisiblePlayerExceptTargetSight(
            final L1Object object, final L1Object target, final int radius) {
        final int map = object.getMapId();
        final Point objectPt = object.getLocation();
        final Point targetPt = target.getLocation();
        final ArrayList<L1PcInstance> result = new ArrayList<L1PcInstance>();

        for (final L1PcInstance element : _allPlayers.values()) {
            if (element.equals(object)) {
                continue;
            }

            if (map != element.getMapId()) {
                continue;
            }

            // 副本ID不相等
            if (target.get_showId() != element.get_showId()) {
                continue;
            }

            if (objectPt.getTileLineDistance(element.getLocation()) <= radius) {
                if (targetPt.getTileLineDistance(element.getLocation()) <= radius) {
                    result.add(element);
                }
            }
        }
        return result;
    }

    /**
     * object 画面内可见范围物件取回
     * 
     * @param object
     * @return
     */
    public ArrayList<L1PcInstance> getRecognizePlayer(final L1Object object) {
        return this.getVisiblePlayer(object, Config.PC_RECOGNIZE_RANGE);
    }

    // 全部玩家
    private Collection<L1PcInstance> _allPlayerValues;

    /**
     * 全部玩家
     * 
     * @return
     */
    public Collection<L1PcInstance> getAllPlayers() {
        try {
            final Collection<L1PcInstance> vs = _allPlayerValues;
            return (vs != null) ? vs : (_allPlayerValues = Collections
                    .unmodifiableCollection(_allPlayers.values()));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 以人物名称传回人物数据
     * 
     * @param name
     *            人物名称(不区分大小写)
     * @return
     */
    public L1PcInstance getPlayer(final String name) {
        if (_allPlayers.contains(name)) {
            return _allPlayers.get(name);
        }
        for (final L1PcInstance each : getAllPlayers()) {
            if (each.getName().equalsIgnoreCase(name)) {
                return each;
            }
        }
        return null;
    }

    /**
     * 该人物在可见范围内
     * 
     * @param name
     *            人物名称(不区分大小写)
     * @return
     */
    public boolean get_pc(final L1PcInstance pc, final String name) {
        L1PcInstance tg = _allPlayers.get(name);
        if (tg != null) {
            if (pc.getLocation().isInScreen(tg.getLocation())) {
                return true;
            }
        }
        return false;
    }

    /*
     * public final Map<String, L1PcInstance> getAllPc() { return
     * this._allPlayers; }
     */

    public final Map<Integer, L1Object> getAllVisibleObjects() {
        return _allObjects;
    }

    /**
     * 全部地图(MAPID为KEY)世界资料
     * 
     * @return
     */
    public final HashMap<Integer, ConcurrentHashMap<Integer, L1Object>> getVisibleObjects() {
        return _visibleObjects;
    }

    /**
     * 指定地图世界资料
     * 
     * @param mapId
     * @return
     */
    public final ConcurrentHashMap<Integer, L1Object> getVisibleObjects(
            final int mapId) {
        return _visibleObjects.get(new Integer(mapId));
    }

    private int _weather = 4;// 世界天气状况

    /**
     * 世界天气状况
     * 
     * @param weather
     */
    public void setWeather(final int weather) {
        _weather = weather;
    }

    /**
     * 世界天气状况
     * 
     * @return
     */
    public int getWeather() {
        return _weather;
    }

    private boolean _worldChatEnabled = true;// 允许广播

    /**
     * 允许广播
     * 
     * @param flag
     */
    public void set_worldChatElabled(final boolean flag) {
        _worldChatEnabled = flag;
    }

    /**
     * 允许广播
     * 
     * @return true允许 false不允许
     */
    public boolean isWorldChatElabled() {
        return _worldChatEnabled;
    }

    private boolean _processingContributionTotal = false;// 计算贡献度

    /**
     * 计算贡献度
     * 
     * @param flag
     */
    public void setProcessingContributionTotal(final boolean flag) {
        _processingContributionTotal = flag;
    }

    /**
     * 计算贡献度
     * 
     * @return
     */
    public boolean isProcessingContributionTotal() {
        return _processingContributionTotal;
    }

    /**
     * 送出全体封包
     * 
     * @param packet
     */
    public void broadcastPacketToAll(final ServerBasePacket packet) {
        for (final L1PcInstance pc : getAllPlayers()) {
            pc.sendPackets(packet);
        }
    }

    /**
     * 送出全体讯息封包
     * 
     * @param message
     */
    public void broadcastServerMessage(final String message) {
        broadcastPacketToAll(new S_SystemMessage(message));
    }
}
