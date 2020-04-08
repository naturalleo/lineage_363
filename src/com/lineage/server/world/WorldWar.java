package com.lineage.server.world;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1War;

/**
 * 世界战争暂存区<BR>
 * 
 * @author dexc
 * 
 */
public class WorldWar {

    private static final Log _log = LogFactory.getLog(WorldWar.class);

    private static WorldWar _instance;

    private final CopyOnWriteArrayList<L1War> _allWars;

    private List<L1War> _allWarList;

    public static WorldWar get() {
        if (_instance == null) {
            _instance = new WorldWar();
        }
        return _instance;
    }

    private WorldWar() {
        this._allWars = new CopyOnWriteArrayList<L1War>(); // 全部战争
    }

    /**
     * 加入战争清单
     * 
     * @param war
     */
    public void addWar(final L1War war) {
        try {
            if (!this._allWars.contains(war)) {
                this._allWars.add(war);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 移出战争清单
     * 
     * @param war
     */
    public void removeWar(final L1War war) {
        try {
            if (this._allWars.contains(war)) {
                this._allWars.remove(war);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 全部战争
     * 
     * @return
     */
    public List<L1War> getWarList() {
        try {
            final List<L1War> vs = this._allWarList;
            return (vs != null) ? vs : (this._allWarList = Collections
                    .unmodifiableList(this._allWars));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 是否为对战中血盟
     * 
     * @param clanname
     * @param tgclanname
     * @return
     */
    public boolean isWar(String clanname, String tgclanname) {
        try {
            for (final L1War war : _allWars) {
                final boolean isInWar = war.checkClanInWar(clanname);
                if (isInWar) {
                    final boolean isInWarTg = war.checkClanInWar(tgclanname);
                    if (isInWarTg) {
                        return true;
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return false;
    }
}
