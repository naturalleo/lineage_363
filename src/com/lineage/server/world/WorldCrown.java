package com.lineage.server.world;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 世界人物暂存区(区分职业)<BR>
 * 王族
 * 
 * @author dexc
 * 
 */
public class WorldCrown {

    private static final Log _log = LogFactory.getLog(WorldCrown.class);

    private static WorldCrown _instance;

    private final ConcurrentHashMap<Integer, L1PcInstance> _isCrown;

    private Collection<L1PcInstance> _allPlayer;

    public static WorldCrown get() {
        if (_instance == null) {
            _instance = new WorldCrown();
        }
        return _instance;
    }

    private WorldCrown() {
        _isCrown = new ConcurrentHashMap<Integer, L1PcInstance>();
    }

    /**
     * 全部王族玩家
     * 
     * @return
     */
    public Collection<L1PcInstance> all() {
        try {
            final Collection<L1PcInstance> vs = _allPlayer;
            return (vs != null) ? vs : (_allPlayer = Collections
                    .unmodifiableCollection(_isCrown.values()));
            // return Collections.unmodifiableCollection(_isCrown.values());

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 王族玩家清单
     * 
     * @return
     */
    public ConcurrentHashMap<Integer, L1PcInstance> map() {
        return _isCrown;
    }

    /**
     * 加入王族玩家清单
     * 
     * @param key
     * @param value
     */
    public void put(final Integer key, final L1PcInstance value) {
        try {
            _isCrown.put(key, value);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 移出王族玩家清单
     * 
     * @param key
     */
    public void remove(final Integer key) {
        try {
            _isCrown.remove(key);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
