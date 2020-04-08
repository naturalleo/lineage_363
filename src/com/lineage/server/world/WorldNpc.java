package com.lineage.server.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.types.Point;

/**
 * 世界NPC暂存区<BR>
 * 
 * @author dexc
 * 
 */
public class WorldNpc {

    private static final Log _log = LogFactory.getLog(WorldNpc.class);

    private static WorldNpc _instance;

    private final ConcurrentHashMap<Integer, L1NpcInstance> _isMob;

    private Collection<L1NpcInstance> _allMobValues;

    public static WorldNpc get() {
        if (_instance == null) {
            _instance = new WorldNpc();
        }
        return _instance;
    }

    private WorldNpc() {
        _isMob = new ConcurrentHashMap<Integer, L1NpcInstance>();
    }

    /**
     * 全部NPC
     * 
     * @return
     */
    public Collection<L1NpcInstance> all() {
        try {
            final Collection<L1NpcInstance> vs = _allMobValues;
            return (vs != null) ? vs : (_allMobValues = Collections
                    .unmodifiableCollection(_isMob.values()));
            // return Collections.unmodifiableCollection(_isCrown.values());

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * NPC清单
     * 
     * @return
     */
    public ConcurrentHashMap<Integer, L1NpcInstance> map() {
        return _isMob;
    }

    /**
     * 加入NPC清单
     * 
     * @param key
     * @param value
     */
    public void put(final Integer key, final L1NpcInstance value) {
        try {
            _isMob.put(key, value);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 移出NPC清单
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
     * 13格范围NPC物件
     * 
     * @param src
     *            原始物件
     * @return
     */
    public ArrayList<L1NpcInstance> getVisibleMob(final L1NpcInstance src) {
        final L1Map map = src.getMap();
        final Point pt = src.getLocation();
        final ArrayList<L1NpcInstance> result = new ArrayList<L1NpcInstance>();

        for (final Iterator<L1NpcInstance> iter = all().iterator(); iter
                .hasNext();) {
            final L1NpcInstance element = iter.next();
            // for (final L1NpcInstance element : this._isMob.values()) {
            if (element.equals(src)) {
                continue;
            }
            if (map != element.getMap()) {
                continue;
            }
            // 13格内NPC
            if (pt.isInScreen(element.getLocation())) {
                result.add(element);
            }
        }

        return result;
    }

    /**
     * 13格范围内相同NPC物件数量
     * 
     * @param src
     *            原始物件
     * @return
     */
    public int getVisibleCount(final L1NpcInstance src) {
        final L1Map map = src.getMap();
        final Point pt = src.getLocation();
        int count = 0;

        for (final Iterator<L1NpcInstance> iter = all().iterator(); iter
                .hasNext();) {
            final L1NpcInstance element = iter.next();
            // for (final L1NpcInstance element : this._isMob.values()) {
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
}
