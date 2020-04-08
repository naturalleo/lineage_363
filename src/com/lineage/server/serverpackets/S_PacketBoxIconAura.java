package com.lineage.server.serverpackets;

/**
 * 技能效果图示(S_OPCODE_PACKETBOX)
 * 
 * @author dexc
 * 
 */
public class S_PacketBoxIconAura extends ServerBasePacket {

    private byte[] _byte = null;

    /** 技能图示 */
    public static final int ICON_AURA = 0x16;// 22;//0x16

    /** 资安 */
    public static final int ICON_OS = 0x7D;// 22;//0x16

    /** 火焰之影将会导致敌人发现我们 */
    public static final int ICON_E3 = 0xE3;// 22;//0x16

    /**
     * 技能效果图示(S_OPCODE_PACKETBOX)
     * 
     * @param iconid
     * @param time
     */
    public S_PacketBoxIconAura(final int iconid, final int time) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(ICON_AURA);
        this.writeC(iconid);
        this.writeH(time);
    }

    /**
     * 火焰之影 炎魔攻击图示
     * 
     * @param iconid
     * @param time
     * @param type
     */
    public S_PacketBoxIconAura(final int iconid, final int time, final int type) {
        writeC(S_OPCODE_PACKETBOX);
        writeC(ICON_AURA);
        writeC(iconid); // 221
        writeH(time); // time
        writeC(type); // 1:炎魔 2:火焰之影
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
