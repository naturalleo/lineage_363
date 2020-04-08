package com.lineage.server.serverpackets;

import java.util.List;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 移转物品名单
 * 
 * @author dexc
 * 
 */
public class S_RetrieveShiftingList extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 移转物品名单
     * 
     * @param pc
     * @param items
     */
    public S_RetrieveShiftingList(final L1PcInstance pc,
            List<L1ItemInstance> items) {
        this.writeC(S_OPCODE_SHOWRETRIEVELIST);
        this.writeD(pc.getId());
        this.writeH(items.size());
        this.writeC(0x02); // 移转物品名单
        for (final L1ItemInstance item : items) {
            final int itemid = item.getId();
            this.writeD(itemid);
            // System.out.println("itemid:" + itemid);
            this.writeC(0x00);
            this.writeH(item.get_gfxid());
            this.writeC(item.getBless());
            this.writeD(1);
            this.writeC(item.isIdentified() ? 0x01 : 0x00);
            this.writeS(item.getViewName());
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
