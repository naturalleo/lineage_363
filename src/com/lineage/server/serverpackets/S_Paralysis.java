package com.lineage.server.serverpackets;

/**
 * 魔法效果:诅咒
 * 
 * @author dexc
 * 
 */
public class S_Paralysis extends ServerBasePacket {

    private byte[] _byte = null;

    /** 你的身体完全麻痹了 */
    public static final int TYPE_PARALYSIS = 0x01;// 你的身体完全麻痹了

    /** 你的身体完全麻痹了 */
    public static final int TYPE_PARALYSIS2 = 0x02;// 你的身体完全麻痹了

    /** 睡眠状态 */
    public static final int TYPE_SLEEP = 0x03;// 睡眠状态

    /** 冻结状态 */
    public static final int TYPE_FREEZE = 0x04;// 冻结状态

    /** 冲击之晕 */
    public static final int TYPE_STUN = 0x05;// 冲击之晕

    /** 双脚被困 */
    public static final int TYPE_BIND = 0x06;// 双脚被困

    /** 解除传送锁定 */
    public static final int TYPE_TELEPORT_UNLOCK = 0x07;// 解除传送锁定

    /**
     * 魔法效果:诅咒
     * 
     * @param type
     * @param flag
     */
    public S_Paralysis(final int type, final boolean flag) {
        this.writeC(S_OPCODE_PARALYSIS);
        switch (type) {
            case TYPE_PARALYSIS: // 你的身体完全麻痹了
                if (flag == true) {
                    this.writeC(0x02);
                } else {
                    this.writeC(0x03);
                }
                break;

            case TYPE_PARALYSIS2: // 你的身体完全麻痹了
                if (flag == true) {
                    this.writeC(0x04);
                } else {
                    this.writeC(0x05);
                }
                break;

            case TYPE_TELEPORT_UNLOCK: // 传送锁定解除
                if (flag == true) {
                    this.writeC(0x06);
                } else {
                    this.writeC(0x07);
                }
                break;

            case TYPE_SLEEP: // 睡眠状态
                if (flag == true) {
                    this.writeC(0x0a);// this.writeC(10);
                } else {
                    this.writeC(0x0b);// this.writeC(11);
                }
                break;

            case TYPE_FREEZE: // 冻结状态
                if (flag == true) {
                    this.writeC(0x0c);// this.writeC(12);
                } else {
                    this.writeC(0x0d);// this.writeC(13);
                }
                break;

            case TYPE_STUN: // 冲击之晕
                if (flag == true) {
                    this.writeC(0x16);// this.writeC(22);
                } else {
                    this.writeC(0x17);// this.writeC(23);
                }
                break;

            case TYPE_BIND: // 双脚被困
                if (flag == true) {
                    this.writeC(0x18);// this.writeC(24);
                } else {
                    this.writeC(0x19);// this.writeC(25);
                }
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
