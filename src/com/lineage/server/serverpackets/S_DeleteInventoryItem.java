package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;

/**
 * 物品删除
 * 
 * @author dexc
 * 
 */
public class S_DeleteInventoryItem extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物品删除
     * 
     * @param item
     */
    public S_DeleteInventoryItem(final L1ItemInstance item) {
        this.writeC(S_OPCODE_DELETEINVENTORYITEM);
        this.writeD(item.getId());
    }

    public S_DeleteInventoryItem(final int objid) {
        this.writeC(S_OPCODE_DELETEINVENTORYITEM);
        this.writeD(objid);
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
