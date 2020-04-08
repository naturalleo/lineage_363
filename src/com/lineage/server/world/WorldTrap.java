package com.lineage.server.world;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Location;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1TrapInstance;

/**
 * 世界陷阱暂存区<BR>
 * 
 * @author dexc
 * 
 */
public class WorldTrap {

    private static final Log _log = LogFactory.getLog(WorldTrap.class);

    private static WorldTrap _instance;

    private final HashMap<Integer, L1TrapInstance> _isTrap;

    public static WorldTrap get() {
        if (_instance == null) {
            _instance = new WorldTrap();
        }
        return _instance;
    }

    private WorldTrap() {
        _isTrap = new HashMap<Integer, L1TrapInstance>();
    }

    /**
     * NPC清单
     * 
     * @return
     */
    public HashMap<Integer, L1TrapInstance> map() {
        return _isTrap;
    }

    /**
     * 加入NPC清单
     * 
     * @param key
     * @param value
     */
    public void put(final Integer key, final L1TrapInstance value) {
        try {
            _isTrap.put(key, value);

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
            _isTrap.remove(key);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 重置所有陷阱
     */
    public void resetAllTraps() {
        for (final Object iter : _isTrap.values().toArray()) {
            final L1TrapInstance trap = (L1TrapInstance) iter;
            trap.resetLocation();
            trap.enableTrap();
        }
    }

    /**
     * 踩到陷阱的处理
     * 
     * @param pc
     */
    public void onPlayerMoved(final L1PcInstance pc) {
        final L1Location loc = pc.getLocation();

        for (final Object iter : _isTrap.values().toArray()) {
            final L1TrapInstance trap = (L1TrapInstance) iter;
            if (trap.isEnable() && loc.equals(trap.getLocation())) {
                trap.onTrod(pc);
                trap.disableTrap();
            }
        }
    }

    /**
     * 侦测陷阱的处理
     * 
     * @param pc
     */
    public void onDetection(final L1PcInstance pc) {
        final L1Location loc = pc.getLocation();

        for (final Object iter : _isTrap.values().toArray()) {
            final L1TrapInstance trap = (L1TrapInstance) iter;
            if (trap.isEnable() && loc.isInScreen(trap.getLocation())) {
                trap.onDetection(pc);
                trap.disableTrap();
            }
        }
    }
}
