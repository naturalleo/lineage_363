package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;

/**
 * 更新物品显示名称(背包)
 * 
 * @author admin
 * 
 */
public class S_ItemName extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 更新物品显示名称(背包)
     */
    public S_ItemName(final L1ItemInstance item) {
        if (item == null) {
            return;
        }
        this.writeC(S_OPCODE_ITEMNAME);
        this.writeD(item.getId());
        this.writeS(item.getViewName());
    }

    /**
     * 更新物品显示名称(背包) - 测试用
     */
    public S_ItemName(final L1ItemInstance item, int id) {
        if (item == null) {
            return;
        }
        this.writeC(S_OPCODE_ITEMNAME);
        this.writeD(item.getId());
        this.writeS(item.getViewName() + " ($" + id + ")");
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
