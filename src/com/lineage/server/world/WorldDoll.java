package com.lineage.server.world;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1DollInstance;

/**
 * 世界魔法娃娃暂存区<BR>
 * 
 * @author dexc
 * 
 */
public class WorldDoll {

    private static final Log _log = LogFactory.getLog(WorldDoll.class);

    private static WorldDoll _instance;

    private final ConcurrentHashMap<Integer, L1DollInstance> _isDoll;

    private Collection<L1DollInstance> _allDollValues;

    public static WorldDoll get() {
        if (_instance == null) {
            _instance = new WorldDoll();
        }
        return _instance;
    }

    private WorldDoll() {
        _isDoll = new ConcurrentHashMap<Integer, L1DollInstance>();
    }

    /**
     * 全部魔法娃娃
     * 
     * @return
     */
    public Collection<L1DollInstance> all() {
        try {
            final Collection<L1DollInstance> vs = _allDollValues;
            return (vs != null) ? vs : (_allDollValues = Collections
                    .unmodifiableCollection(_isDoll.values()));
            // return Collections.unmodifiableCollection(_isCrown.values());

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 魔法娃娃清单
     * 
     * @return
     */
    public ConcurrentHashMap<Integer, L1DollInstance> map() {
        return _isDoll;
    }

    /**
     * 指定魔法娃娃数据
     * 
     * @param key
     * @return
     */
    public L1DollInstance get(final Integer key) {
        try {
            return _isDoll.get(key);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 加入魔法娃娃清单
     * 
     * @param key
     * @param value
     */
    public void put(final Integer key, final L1DollInstance value) {
        try {
            _isDoll.put(key, value);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 移出魔法娃娃清单
     * 
     * @param key
     */
    public void remove(final Integer key) {
        try {
            _isDoll.remove(key);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
