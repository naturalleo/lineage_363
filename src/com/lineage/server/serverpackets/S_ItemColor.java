package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;

/**
 * 物品色彩状态
 * 
 * @author dexc
 * 
 */
public class S_ItemColor extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物品色彩状态
     */
    public S_ItemColor(final L1ItemInstance item) {
        if (item == null) {
            return;
        }
        this.buildPacket(item);
    }

    private void buildPacket(final L1ItemInstance item) {
        this.writeC(S_OPCODE_ITEMCOLOR);
        this.writeD(item.getId());
        // 0:祝福 1:通常 2:咒い 3:未鉴定
        // 128:祝福&封印 129:&封印 130:咒い&封印 131:未鉴定&封印
        this.writeC(item.getBless());
    }

    /**
     * 物品色彩状态 - 测试用
     * 
     * @param item
     * @param id
     */
    /*
     * public S_ItemColor(L1PcInstance _pc, final L1ItemInstance item) {
     * this.writeC(S_OPCODE_ITEMCOLOR); this.writeD(item.getId()); // 0:祝福 1:通常
     * 2:咒い 3:未鉴定 // 128:祝福&封印 129:&封印 130:咒い&封印 131:未鉴定&封印 this.writeC(id); }
     */

    /**
     * 物品色彩状态 - 测试用
     * 
     * @param item
     * @param id
     */
    public S_ItemColor(final L1ItemInstance item, int id) {
        this.writeC(S_OPCODE_ITEMCOLOR);
        this.writeD(item.getId());
        // 0:祝福 1:通常 2:咒い 3:未鉴定
        // 128:祝福&封印 129:&封印 130:咒い&封印 131:未鉴定&封印
        this.writeC(id);
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
