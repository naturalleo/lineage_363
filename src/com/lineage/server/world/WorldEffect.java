package com.lineage.server.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.types.Point;

/**
 * 世界Effect(技能物件)暂存区<BR>
 * 
 * @author dexc
 * 
 */
public class WorldEffect {

    private static final Log _log = LogFactory.getLog(WorldEffect.class);

    private static WorldEffect _instance;

    private final ConcurrentHashMap<Integer, L1EffectInstance> _isEff;

    private Collection<L1EffectInstance> _allEffValues;

    public static WorldEffect get() {
        if (_instance == null) {
            _instance = new WorldEffect();
        }
        return _instance;
    }

    private WorldEffect() {
        _isEff = new ConcurrentHashMap<Integer, L1EffectInstance>();
    }

    /**
     * 全部Effect
     * 
     * @return
     */
    public Collection<L1EffectInstance> all() {
        try {
            final Collection<L1EffectInstance> vs = _allEffValues;
            return (vs != null) ? vs : (_allEffValues = Collections
                    .unmodifiableCollection(_isEff.values()));
            // return Collections.unmodifiableCollection(_isCrown.values());

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * Effect清单
     * 
     * @return
     */
    public ConcurrentHashMap<Integer, L1EffectInstance> map() {
        return _isEff;
    }

    /**
     * 加入Effect清单
     * 
     * @param key
     * @param value
     */
    public void put(final Integer key, final L1EffectInstance value) {
        try {
            _isEff.put(key, value);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 移出Effect清单
     * 
     * @param key
     */
    public void remove(final Integer key) {
        try {
            _isEff.remove(key);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 座标是否有 重叠Effect物件
     * 
     * @param loc
     *            原始座标
     * @param npcid
     * @return true:有 false:没有
     */
    public boolean isEffect(final L1Location loc, int npcid) {
        for (final Iterator<L1EffectInstance> iter = all().iterator(); iter
                .hasNext();) {
            final L1EffectInstance element = iter.next();
            // for (final L1EffectInstance element : this._isEff.values()) {
            // 地图编号不相等
            if (loc.getMapId() != element.getMap().getId()) {
                continue;
            }
            // NPC编号不相等
            if (npcid != element.getNpcId()) {
                continue;
            }
            // 是否与指定座标位置重叠
            if (loc.isSamePoint(element.getLocation())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 座标重叠Effect物件
     * 
     * @param loc
     *            原始座标
     * @return
     */
    public ArrayList<L1EffectInstance> getVisibleEffect(final L1Location loc) {
        final ArrayList<L1EffectInstance> result = new ArrayList<L1EffectInstance>();

        for (final Iterator<L1EffectInstance> iter = all().iterator(); iter
                .hasNext();) {
            final L1EffectInstance element = iter.next();
            // for (final L1EffectInstance element : this._isEff.values()) {
            // 地图编号不相等
            if (loc.getMapId() != element.getMap().getId()) {
                continue;
            }
            // 是否与指定座标位置重叠
            if (loc.isSamePoint(element.getLocation())) {
                result.add(element);
            }
        }

        return result;
    }

    /**
     * 座标重叠Effect物件
     * 
     * @param src
     *            原始物件
     * @return
     */
    public ArrayList<L1EffectInstance> getVisibleEffect(
            final L1EffectInstance src) {
        final L1Map map = src.getMap();
        final Point pt = src.getLocation();
        final ArrayList<L1EffectInstance> result = new ArrayList<L1EffectInstance>();

        for (final Iterator<L1EffectInstance> iter = all().iterator(); iter
                .hasNext();) {
            final L1EffectInstance element = iter.next();
            // for (final L1EffectInstance element : this._isEff.values()) {
            if (element.equals(src)) {
                continue;
            }
            // 地图编号不相等
            if (map.getId() != element.getMap().getId()) {
                continue;
            }
            if (src.getNpcId() != element.getNpcId()) {
                continue;
            }
            // 是否与指定座标位置重叠
            if (pt.isSamePoint(element.getLocation())) {
                result.add(element);
            }
        }

        return result;
    }

    /**
     * 13格范围内相同Effect物件数量
     * 
     * @param src
     *            原始物件
     * @return
     */
    public int getVisibleCount(final L1EffectInstance src) {
        final L1Map map = src.getMap();
        final Point pt = src.getLocation();
        int count = 0;

        for (final Iterator<L1EffectInstance> iter = all().iterator(); iter
                .hasNext();) {
            final L1EffectInstance element = iter.next();
            // for (final L1EffectInstance element : this._isEff.values()) {
            if (element.equals(src)) {
                continue;
            }
            if (map != element.getMap()) {
                continue;
            }
            // 13格内NPC
            if (pt.isInScreen(element.getLocation())) {
                if (src.getNpcId() == element.getNpcId()) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * 火牢范围物件
     * 
     * @param firewall
     *            计算物件(火牢)
     * @return
     */
    public ArrayList<L1Character> getFirewall(final L1EffectInstance firewall) {
        final L1Map map = firewall.getMap();
        final Point pt = firewall.getLocation();
        final ArrayList<L1Character> result = new ArrayList<L1Character>();

        // 取回原地图资料
        final ArrayList<L1Object> mapSrc = World.get().getVisibleObjects(
                firewall, 2);
        if (mapSrc == null) {
            _log.error("游戏世界储存中心并未建立该地图编号资料档案11: " + map.getId());
            return result;
        }
        if (mapSrc.isEmpty()) {
            return result;
        }
        // 施展者遗失(世界物件中)
        if (World.get().findObject(firewall.getId()) == null) {
            return result;
        }
        // 施展者主人遗失(世界物件中)
        if (World.get().findObject(firewall.getMaster().getId()) == null) {
            return result;
        }
        // 施展者进行删除
        if (firewall.destroyed()) {
            return result;
        }

        // 主人为空
        if (firewall.getMaster() == null) {
            return result;
        }

        for (final L1Object element : mapSrc) {
            // 判断对象不是L1Character
            if (!(element instanceof L1Character)) {
                continue;
            }

            // 判断对象是主人
            if (firewall.getMaster().equals(element)) {
                continue;
            }

            final L1Character cha = (L1Character) element;

            // 判断对象死亡
            if (cha.isDead()) {
                continue;
            }

            final int r = pt.getTileLineDistance(element.getLocation());
            if (r <= 1) {
                result.add((L1Character) element);
            }
        }

        return result;
    }
}
