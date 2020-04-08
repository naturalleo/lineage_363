package com.lineage.server.world;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 世界人物暂存区(区分职业)<BR>
 * 幻术
 * 
 * @author dexc
 * 
 */
public class WorldIllusionist {

    private static final Log _log = LogFactory.getLog(WorldIllusionist.class);

    private static WorldIllusionist _instance;

    private final ConcurrentHashMap<Integer, L1PcInstance> _isIllusionist;

    private Collection<L1PcInstance> _allPlayer;

    public static WorldIllusionist get() {
        if (_instance == null) {
            _instance = new WorldIllusionist();
        }
        return _instance;
    }

    private WorldIllusionist() {
        _isIllusionist = new ConcurrentHashMap<Integer, L1PcInstance>();
    }

    /**
     * 全部幻术玩家
     * 
     * @return
     */
    public Collection<L1PcInstance> all() {
        try {
            final Collection<L1PcInstance> vs = _allPlayer;
            return (vs != null) ? vs : (_allPlayer = Collections
                    .unmodifiableCollection(_isIllusionist.values()));
            // return Collections.unmodifiableCollection(_isCrown.values());

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 幻术玩家清单
     * 
     * @return
     */
    public ConcurrentHashMap<Integer, L1PcInstance> map() {
        return _isIllusionist;
    }

    /**
     * 加入幻术玩家清单
     * 
     * @param key
     * @param value
     */
    public void put(final Integer key, final L1PcInstance value) {
        try {
            _isIllusionist.put(key, value);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 移出幻术玩家清单
     * 
     * @param key
     */
    public void remove(final Integer key) {
        try {
            _isIllusionist.remove(key);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
