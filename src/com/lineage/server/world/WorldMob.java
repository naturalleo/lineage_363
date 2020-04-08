package com.lineage.server.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.types.Point;

/**
 * 世界MOB暂存区<BR>
 * 
 * @author dexc
 * 
 */
public class WorldMob {

    private static final Log _log = LogFactory.getLog(WorldMob.class);

    private static WorldMob _instance;

    private final ConcurrentHashMap<Integer, L1MonsterInstance> _isMob;

    private Collection<L1MonsterInstance> _allMobValues;

    public static WorldMob get() {
        if (_instance == null) {
            _instance = new WorldMob();
        }
        return _instance;
    }

    private WorldMob() {
        _isMob = new ConcurrentHashMap<Integer, L1MonsterInstance>();
    }

    /**
     * 全部怪物
     * 
     * @return
     */
    public Collection<L1MonsterInstance> all() {
        try {
            final Collection<L1MonsterInstance> vs = _allMobValues;
            return (vs != null) ? vs : (_allMobValues = Collections
                    .unmodifiableCollection(_isMob.values()));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 怪物清单
     * 
     * @return
     */
    public ConcurrentHashMap<Integer, L1MonsterInstance> map() {
        return _isMob;
    }

    /**
     * 加入怪物清单
     * 
     * @param key
     * @param value
     */
    public void put(final Integer key, final L1MonsterInstance value) {
        try {
            _isMob.put(key, value);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 移出怪物清单
     * 
     * @param key
     */
    public void remove(final Integer key) {
        try {
            _isMob.remove(key);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 19格范围NPC物件
     * 
     * @param src
     *            原始物件
     * @return
     */
    public ArrayList<L1MonsterInstance> getVisibleMob(
            final L1MonsterInstance src) {
        final L1Map map = src.getMap();
        final Point pt = src.getLocation();
        final ArrayList<L1MonsterInstance> result = new ArrayList<L1MonsterInstance>();

        for (final Iterator<L1MonsterInstance> iter = all().iterator(); iter
                .hasNext();) {
            final L1MonsterInstance element = iter.next();
            // for (final L1MonsterInstance element : this._isMob.values()) {
            if (element.equals(src)) {
                continue;
            }
            if (map != element.getMap()) {
                continue;
            }
            // 19格内NPC
            if (pt.isInScreen(element.getLocation())) {
                result.add(element);
            }
        }

        return result;
    }

    /**
     * 19格范围内相同NPC物件数量
     * 
     * @param src
     *            原始物件
     * @return
     */
    public int getVisibleCount(final L1MonsterInstance src) {
        final L1Map map = src.getMap();
        final Point pt = src.getLocation();
        int count = 0;

        for (final Iterator<L1MonsterInstance> iter = all().iterator(); iter
                .hasNext();) {
            final L1MonsterInstance element = iter.next();
            // for (final L1MonsterInstance element : this._isMob.values()) {
            if (element.equals(src)) {
                continue;
            }
            if (map != element.getMap()) {
                continue;
            }
            // 19格内NPC
            if (pt.isInScreen(element.getLocation())) {
                if (src.getNpcId() == element.getNpcId()) {
                    count++;
                }
            }
        }

        return count;
    }
}
