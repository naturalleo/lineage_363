package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Clan;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.world.WorldClan;

/**
 * 物品名单(血盟仓库)
 * 
 * @author dexc
 * 
 */
public class S_RetrievePledgeList extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物品名单(血盟仓库)
     * 
     * @param objid
     * @param pc
     */
    public S_RetrievePledgeList(final int objid, final L1PcInstance pc) {
        final L1Clan clan = WorldClan.get().getClan(pc.getClanname());
        // final L1Clan clan = World.get().getClan(pc.getClanname());
        if (clan == null) {
            return;
        }

        if ((clan.getWarehouseUsingChar() != 0)
                && (clan.getWarehouseUsingChar() != pc.getId())) {// 目前无人使用 或是
                                                                  // 使用者是自己
            pc.sendPackets(new S_ServerMessage(209)); // \f1血盟成员在使用仓库。请稍后再使用。
            return;
        }

        if (pc.getInventory().getSize() < 180) {
            final int size = clan.getDwarfForClanInventory().getSize();
            if (size > 0) {
                clan.setWarehouseUsingChar(pc.getId()); // 设置血盟仓库目前使用者
                this.writeC(S_OPCODE_SHOWRETRIEVELIST);
                this.writeD(objid);
                this.writeH(size);
                this.writeC(0x05); // 血盟仓库
                for (final Object itemObject : clan.getDwarfForClanInventory()
                        .getItems()) {
                    final L1ItemInstance item = (L1ItemInstance) itemObject;
                    this.writeD(item.getId());
                    int i = item.getItem().getUseType();
                    if (i < 0) {
                        i = 0;
                    }
                    this.writeC(i);// this.writeC(0x00);
                    this.writeH(item.get_gfxid());
                    this.writeC(item.getBless());
                    this.writeD((int) Math.min(item.getCount(), 2000000000));
                    this.writeC(item.isIdentified() ? 0x01 : 0x00);
                    this.writeS(item.getViewName());
                }
				writeD(30);
				writeD(0x00000000);
				writeH(0x00);
            }
        } else {
            pc.sendPackets(new S_ServerMessage(263)); // 263 \f1一个角色最多可携带180个道具。
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
