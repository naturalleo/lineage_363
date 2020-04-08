package com.lineage.server.world;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 世界人物暂存区(区分职业)<BR>
 * 龙骑
 * 
 * @author dexc
 * 
 */
public class WorldDragonKnight {

    private static final Log _log = LogFactory.getLog(WorldDragonKnight.class);

    private static WorldDragonKnight _instance;

    private final ConcurrentHashMap<Integer, L1PcInstance> _isDragonKnight;

    private Collection<L1PcInstance> _allPlayer;

    public static WorldDragonKnight get() {
        if (_instance == null) {
            _instance = new WorldDragonKnight();
        }
        return _instance;
    }

    private WorldDragonKnight() {
        _isDragonKnight = new ConcurrentHashMap<Integer, L1PcInstance>();
    }

    /**
     * 全部龙骑玩家
     * 
     * @return
     */
    public Collection<L1PcInstance> all() {
        try {
            final Collection<L1PcInstance> vs = _allPlayer;
            return (vs != null) ? vs : (_allPlayer = Collections
                    .unmodifiableCollection(_isDragonKnight.values()));
            // return Collections.unmodifiableCollection(_isCrown.values());

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 龙骑玩家清单
     * 
     * @return
     */
    public ConcurrentHashMap<Integer, L1PcInstance> map() {
        return _isDragonKnight;
    }

    /**
     * 加入龙骑玩家清单
     * 
     * @param key
     * @param value
     */
    public void put(final Integer key, final L1PcInstance value) {
        try {
            _isDragonKnight.put(key, value);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 移出龙骑玩家清单
     * 
     * @param key
     */
    public void remove(final Integer key) {
        try {
            _isDragonKnight.remove(key);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
