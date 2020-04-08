package com.lineage.server.model.Instance;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.world.World;

/**
 * 家具
 * 
 * @author dexc
 * 
 */
public class L1FurnitureInstance extends L1NpcInstance {

    private static final long serialVersionUID = 1L;

    private static final Log _log = LogFactory
            .getLog(L1FurnitureInstance.class);

    private int _itemObjId;

    public L1FurnitureInstance(final L1Npc template) {
        super(template);
    }

    @Override
    public void onAction(final L1PcInstance player) {
    }

    @Override
    public void deleteMe() {
        try {
            _destroyed = true;
            if (getInventory() != null) {
                getInventory().clearItems();
            }
            World.get().removeVisibleObject(this);
            World.get().removeObject(this);
            for (final L1PcInstance pc : World.get().getRecognizePlayer(this)) {
                pc.removeKnownObject(this);
                pc.sendPackets(new S_RemoveObject(this));
            }
            removeAllKnownObjects();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public int getItemObjId() {
        return _itemObjId;
    }

    public void setItemObjId(final int i) {
        _itemObjId = i;
    }
}
