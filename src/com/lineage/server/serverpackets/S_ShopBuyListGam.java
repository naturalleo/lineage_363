package com.lineage.server.serverpackets;

import java.util.Map;

import com.lineage.data.event.GamblingSet;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Gambling;

/**
 * 卖
 * 
 * @author dexc
 * 
 */
public class S_ShopBuyListGam extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 卖中奖的票
     * 
     * @param pc
     * @param npc
     * @param sellList
     */
    public S_ShopBuyListGam(final L1PcInstance pc, final L1NpcInstance npc,
            final Map<Integer, L1Gambling> sellList) {
        this.writeC(S_OPCODE_SHOWSHOPSELLLIST);
        this.writeD(npc.getId());

        if (sellList.isEmpty()) {
            this.writeH(0x0000);
            return;
        }

        if (sellList.size() <= 0) {
            this.writeH(0x0000);
            return;
        }

        this.writeH(sellList.size());

        for (final Integer itemobjid : sellList.keySet()) {
            this.writeD(itemobjid);
            final L1Gambling gam = sellList.get(itemobjid);
            final int adena = (int) (GamblingSet.GAMADENA * gam.get_rate());
            this.writeD(adena);
        }
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
