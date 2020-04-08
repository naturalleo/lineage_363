package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 物品名单(精灵仓库)
 * 
 * @author dexc
 * 
 */
public class S_RetrieveElfList extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物品名单(精灵仓库)
     * 
     * @param objid
     * @param pc
     */
    public S_RetrieveElfList(final int objid, final L1PcInstance pc) {
        if (pc.getInventory().getSize() < 180) {
            final int size = pc.getDwarfForElfInventory().getSize();
            if (size > 0) {
                this.writeC(S_OPCODE_SHOWRETRIEVELIST);
                this.writeD(objid);
                this.writeH(size);
                this.writeC(0x09); // 精灵仓库
                for (final Object itemObject : pc.getDwarfForElfInventory()
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
