package com.lineage.server.world;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 世界人物暂存区(区分职业)<BR>
 * 法师
 * 
 * @author dexc
 * 
 */
public class WorldWizard {

    private static final Log _log = LogFactory.getLog(WorldWizard.class);

    private static WorldWizard _instance;

    private final ConcurrentHashMap<Integer, L1PcInstance> _isWizard;

    private Collection<L1PcInstance> _allPlayer;

    public static WorldWizard get() {
        if (_instance == null) {
            _instance = new WorldWizard();
        }
        return _instance;
    }

    private WorldWizard() {
        _isWizard = new ConcurrentHashMap<Integer, L1PcInstance>();
    }

    /**
     * 全部法师玩家
     * 
     * @return
     */
    public Collection<L1PcInstance> all() {
        try {
            final Collection<L1PcInstance> vs = _allPlayer;
            return (vs != null) ? vs : (_allPlayer = Collections
                    .unmodifiableCollection(_isWizard.values()));
            // return Collections.unmodifiableCollection(_isCrown.values());

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 法师玩家清单
     * 
     * @return
     */
    public ConcurrentHashMap<Integer, L1PcInstance> map() {
        return _isWizard;
    }

    /**
     * 加入法师玩家清单
     * 
     * @param key
     * @param value
     */
    public void put(final Integer key, final L1PcInstance value) {
        try {
            _isWizard.put(key, value);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 移出法师玩家清单
     * 
     * @param key
     */
    public void remove(final Integer key) {
        try {
            _isWizard.remove(key);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
