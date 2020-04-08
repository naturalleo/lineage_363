package com.lineage.server.serverpackets;

import java.util.ArrayList;

import com.lineage.server.datatables.lock.BuddyReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1BuddyTmp;
import com.lineage.server.world.World;

/**
 * 好友清单
 * 
 * @author dexc
 * 
 */
public class S_Buddy extends ServerBasePacket {

    private static final String _buddy = "buddy";

    private byte[] _byte = null;

    /**
     * 好友清单
     * 
     * @param objId
     */
    public S_Buddy(final int objId) {
        this.buildPacket(objId);
    }

    private void buildPacket(final int objId) {
        this.writeC(S_OPCODE_SHOWHTML);
        this.writeD(objId);
        this.writeS(_buddy);
        this.writeH(0x02);
        this.writeH(0x02);

        String result = new String("");
        String onlineBuddy = new String("");

        final ArrayList<L1BuddyTmp> list = BuddyReading.get().userBuddy(objId);
        if (list != null) {
            for (final L1BuddyTmp buddyTmp : BuddyReading.get()
                    .userBuddy(objId)) {
                final String buddy_name = buddyTmp.get_buddy_name();
                result += buddy_name + " ";

                final L1PcInstance find = World.get().getPlayer(buddy_name);
                if (find != null) {
                    onlineBuddy += find.getName() + " ";
                }
            }
        }
        this.writeS(result);
        this.writeS(onlineBuddy);
    }

    @Override
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = this.getBytes();
        }
        return this._byte;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
