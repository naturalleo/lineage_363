package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_PacketBoxCooking extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * <font color=#00800>料理选单</font>
     */
    public static final int COOK_WINDOW = 52;

    /** writeByte(type) writeShort(time): 料理アイコンが表示される */
    public static final int ICON_COOKING = 53;

    public S_PacketBoxCooking(final int value) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(COOK_WINDOW);
        this.writeC(0xdb); // ?
        this.writeC(0x31);
        this.writeC(0xdf);
        this.writeC(0x02);
        this.writeC(0x01);
        this.writeC(value); // level
    }

    public S_PacketBoxCooking(final L1PcInstance pc, final int type,
            final int time) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(ICON_COOKING);
        // 0000: 79 35 15 19 12 0c 0e 0c d0 07 31 24 84 03 85 16
        int food = (pc.get_food() * 10) - 250;
        if (food < 0) {
            food = 0;
        }
        switch (type) {
            case 0x07:
                this.writeC(pc.getStr());// str
                this.writeC(pc.getInt());// int
                this.writeC(pc.getWis());// wis
                this.writeC(pc.getDex());// dex
                this.writeC(pc.getCon());// con
                this.writeC(pc.getCha());// cha
                this.writeH(food);
                this.writeC(type);// 类型
                this.writeC(0x24);
                this.writeH(time);// 时间
                this.writeC(0x0);// 负重
                break;

            case 54:
                this.writeC(0x00);// str
                this.writeC(0x00);// int
                this.writeC(0x00);// wis
                this.writeC(0x00);// dex
                this.writeC(0x00);// con
                this.writeC(0x00);// cha
                this.writeH(0x00);// 饱食
                this.writeC(type);// 类型
                this.writeC(0x2a);
                this.writeH(time);// 时间
                this.writeC(0x0);// 负重
                break;

            default:
                this.writeC(pc.getStr());// str
                this.writeC(pc.getInt());// int
                this.writeC(pc.getWis());// wis
                this.writeC(pc.getDex());// dex
                this.writeC(pc.getCon());// con
                this.writeC(pc.getCha());// cha
                this.writeH(food);
                this.writeC(type);// 类型
                this.writeC(0x26);
                this.writeH(time);// 时间
                this.writeC(0x0);// 负重
                break;
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
