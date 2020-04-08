package com.lineage.server.serverpackets;

import java.util.List;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 托售 物品名单
 * 
 * @author dexc
 * 
 */
public class S_CnSRetrieve extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 托售 物品名单
     * 
     * @param pc
     * @param items
     */
    public S_CnSRetrieve(final L1PcInstance pc, final int objid,
            final List<L1ItemInstance> items) {
        this.writeC(S_OPCODE_SHOWRETRIEVELIST);
        this.writeD(objid);
        this.writeH(items.size());
        this.writeC(0x0c); // 提炼武器/托售
        for (final L1ItemInstance item : items) {
            final int itemobjid = item.getId();
            this.writeD(itemobjid);
            // System.out.println("itemobjid:" + itemobjid);
            this.writeC(0x00);
            this.writeH(item.get_gfxid());
            this.writeC(item.getBless());
            this.writeD(1);
            this.writeC(item.isIdentified() ? 0x01 : 0x00);
            this.writeS(item.getViewName());
        }
		writeD(5);
		writeD(0x00000000);
		writeH(0x00);
        items.clear();
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
