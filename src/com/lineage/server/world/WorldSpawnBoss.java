package com.lineage.server.world;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.templates.L1SpawnBoss;

/**
 * BOSS SPAWN暂存区<BR>
 * 制作中 daien 2012-05-11
 * 
 * @author daien
 * 
 */
public class WorldSpawnBoss {

    private static final Log _log = LogFactory.getLog(WorldSpawnBoss.class);

    private static WorldSpawnBoss _instance;

    private final HashMap<Integer, L1SpawnBoss> _bossSpawn;// OBJID L1SpawnEx

    private Collection<L1SpawnBoss> _allBossValues;

    public static WorldSpawnBoss get() {
        if (_instance == null) {
            _instance = new WorldSpawnBoss();
        }
        return _instance;
    }

    private WorldSpawnBoss() {
        _bossSpawn = new HashMap<Integer, L1SpawnBoss>();
    }

    public Collection<L1SpawnBoss> all() {
        try {
            final Collection<L1SpawnBoss> vs = _allBossValues;
            return (vs != null) ? vs : (_allBossValues = Collections
                    .unmodifiableCollection(_bossSpawn.values()));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public HashMap<Integer, L1SpawnBoss> map() {
        return _bossSpawn;
    }

    public L1SpawnBoss get(final Integer key) {
        return _bossSpawn.get(key);
    }

    public void put(final Integer key, final L1SpawnBoss value) {
        try {
            _bossSpawn.put(key, value);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void remove(final Integer key) {
        try {
            _bossSpawn.remove(key);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
