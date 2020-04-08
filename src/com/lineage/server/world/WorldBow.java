package com.lineage.server.world;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1BowInstance;

/**
 * 世界NPC(BOW)暂存区<BR>
 * 
 * @author dexc
 * 
 */
public class WorldBow {

    private static final Log _log = LogFactory.getLog(WorldBow.class);

    private static WorldBow _instance;

    private final HashMap<Integer, L1BowInstance> _isBow;

    public static WorldBow get() {
        if (_instance == null) {
            _instance = new WorldBow();
        }
        return _instance;
    }

    private WorldBow() {
        _isBow = new HashMap<Integer, L1BowInstance>();
    }

    /**
     * NPC清单
     * 
     * @return
     */
    public HashMap<Integer, L1BowInstance> map() {
        return _isBow;
    }

    /**
     * 加入NPC清单
     * 
     * @param key
     * @param value
     */
    public void put(final Integer key, final L1BowInstance value) {
        try {
            _isBow.put(key, value);

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
            _isBow.remove(key);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
