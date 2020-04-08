package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1TrapInstance;

/**
 * 物件封包 - 陷阱(GM探查用)
 * 
 * @author DaiEn
 * 
 */
public class S_Trap extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物件封包 - 陷阱(GM探查用)
     * 
     * @param trap
     * @param name
     */
    public S_Trap(final L1TrapInstance trap, final String name) {
        this.writeC(S_OPCODE_CHARPACK);
        this.writeH(trap.getX()); // X
        this.writeH(trap.getY()); // Y
        this.writeD(trap.getId()); // OBJID
        this.writeH(0x0007); // GFXID
        this.writeC(0x00); // 物件外观属性
        this.writeC(0x00); // 方向
        this.writeC(0x00); // 亮度 0:normal, 1:fast, 2:slow
        this.writeC(0x00); // 速度
        this.writeD(0x00000000); // 数量, 经验值
        this.writeH(0x0000); // 正义质
        this.writeS(name); // 名称
        this.writeS(null); // 封号
        this.writeC(0x00); // 状态
        this.writeD(0x00000000); // 血盟OBJID
        this.writeS(null); // 血盟名称
        this.writeS(null); // 主人名称
        this.writeC(0x00); // 物件分类
        this.writeC(0xFF); // HP显示
        this.writeC(0x00); // タルクック距离(通り)
        this.writeC(0x00); // LV
        this.writeC(0x00);
        this.writeC(0xFF);
        this.writeC(0xFF);

        /*
         * this.writeC(S_OPCODE_CHARPACK); this.writeH(trap.getX());
         * this.writeH(trap.getY()); this.writeD(trap.getId());
         * this.writeH(0x07); // adena this.writeC(0x00); this.writeC(0x00);
         * this.writeC(0x00); this.writeC(0x00); this.writeD(0x00);
         * this.writeC(0x00); this.writeC(0x00); this.writeS(name);
         * this.writeC(0x00); this.writeD(0x00); this.writeD(0x00);
         * this.writeC(255); this.writeC(0x00); this.writeC(0x00);
         * this.writeC(0x00); this.writeH(65535); // writeD(0x401799a);
         * this.writeD(0); this.writeC(8); this.writeC(0);
         */
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
