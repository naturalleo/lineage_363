package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;

/**
 * 物件封包 - 地面物品
 * 
 * @author dexc
 * 
 */
public class S_NPCPack_Item extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物件封包 - 地面物品
     * 
     * @param item
     */
    public S_NPCPack_Item(final L1ItemInstance item) {
        this.buildPacket(item);
    }

    private void buildPacket(final L1ItemInstance item) {
        this.writeC(S_OPCODE_CHARPACK);
        this.writeH(item.getX());
        this.writeH(item.getY());
        this.writeD(item.getId());
        this.writeH(item.getItem().getGroundGfxId());
        this.writeC(0x00);
        this.writeC(0x00);
        this.writeC(item.isNowLighting() ? item.getItem().getLightRange()
                : 0x00);// 亮度
        this.writeC(0x00);
        this.writeD((int) Math.min(item.getCount(), 2000000000));// 数量
        this.writeH(0x0000);
        String name = "";
        if (item.getCount() > 1) {
            name = item.getItem().getNameId() + " (" + item.getCount() + ")";

        } else {
            switch (item.getItemId()) {
                case 20383: // 军马头盔
                case 41235: // %i．体力魔方
                case 41236: // %i．体力魔方(受祝福)
                    name = item.getItem().getNameId() + " ["
                            + item.getChargeCount() + "]";
                    break;

                case 40006: // 创造怪物魔杖
                case 140006: // 创造怪物魔杖
                case 40007: // 闪电魔杖
                case 40008: // 变形魔杖
                case 140008: // 变形魔杖
                case 40009: // 驱逐魔杖
                    if (item.isIdentified()) {
                        name = item.getItem().getNameId() + " ("
                                + item.getChargeCount() + ")";
                    }
                    break;

                default:// 其他道具
                    // 照明类
                    if ((item.getItem().getLightRange() != 0)
                            && item.isNowLighting()) {
                        name = item.getItem().getNameId() + " ($10)";

                    } else {
                        name = item.getItem().getNameId();
                    }
                    break;
            }
        }
        this.writeS(name);
        this.writeS(null);
        this.writeC(0x00); // 状态
        this.writeD(0x00000000);
        this.writeS(null);
        this.writeS(null);

        this.writeC(0x00); // 物件分类

        this.writeC(0xff); // HP
        this.writeC(0x00);
        this.writeC(0x00);
        this.writeC(0x00);
        this.writeC(0xff);
        this.writeC(0xff);
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
