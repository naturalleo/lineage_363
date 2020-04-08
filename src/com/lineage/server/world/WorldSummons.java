package com.lineage.server.world;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1SummonInstance;

/**
 * 世界召唤兽暂存区<BR>
 * 
 * @author dexc
 * 
 */
public class WorldSummons {

    private static final Log _log = LogFactory.getLog(WorldSummons.class);

    private static WorldSummons _instance;

    private final ConcurrentHashMap<Integer, L1SummonInstance> _isSummons;

    private Collection<L1SummonInstance> _allSummonValues;

    public static WorldSummons get() {
        if (_instance == null) {
            _instance = new WorldSummons();
        }
        return _instance;
    }

    private WorldSummons() {
        _isSummons = new ConcurrentHashMap<Integer, L1SummonInstance>();
    }

    /**
     * 全部召唤兽
     * 
     * @return
     */
    public Collection<L1SummonInstance> all() {
        try {
            final Collection<L1SummonInstance> vs = _allSummonValues;
            return (vs != null) ? vs : (_allSummonValues = Collections
                    .unmodifiableCollection(_isSummons.values()));
            // return Collections.unmodifiableCollection(_isCrown.values());

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 召唤兽清单
     * 
     * @return
     */
    public ConcurrentHashMap<Integer, L1SummonInstance> map() {
        return _isSummons;
    }

    /**
     * 加入召唤兽清单
     * 
     * @param key
     * @param value
     */
    public void put(final Integer key, final L1SummonInstance value) {
        try {
            _isSummons.put(key, value);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 移出召唤兽清单
     * 
     * @param key
     */
    public void remove(final Integer key) {
        try {
            _isSummons.remove(key);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
