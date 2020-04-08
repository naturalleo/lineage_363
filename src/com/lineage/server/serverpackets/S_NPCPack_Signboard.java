package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1SignboardInstance;

/**
 * 物件封包 - 告示
 * 
 * @author dexc
 * 
 */
public class S_NPCPack_Signboard extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物件封包 - 告示
     * 
     * @param signboard
     */
    public S_NPCPack_Signboard(final L1SignboardInstance signboard) {
        this.writeC(S_OPCODE_CHARPACK);
        this.writeH(signboard.getX()); // X
        this.writeH(signboard.getY()); // Y
        this.writeD(signboard.getId()); // OBJID
        this.writeH(signboard.getGfxId()); // GFXID
        this.writeC(0x00); // 物件外观属性
        this.writeC(this.getDirection(signboard.getHeading())); // 方向
        this.writeC(0x00); // 亮度 0:normal, 1:fast, 2:slow
        this.writeC(0x00); // 速度
        this.writeD(0x00000000); // 数量, 经验值
        this.writeH(0x0000); // 正义质
        this.writeS(null); // 名称
        this.writeS(signboard.getName()); // 封号
        this.writeC(0x00); // 状态
        this.writeD(0x00000000); // 血盟OBJID
        this.writeS(null); // 血盟名称
        this.writeS(null); // 主人名称

        // 0:NPC,道具
        // 1:中毒 ,
        // 2:隐身
        // 4:人物
        // 8:诅咒
        // 16:勇水
        // 32:??
        // 64:??(??)
        // 128:invisible but name
        this.writeC(0x00); // 物件分类

        this.writeC(0xFF); // HP显示
        this.writeC(0x00); // タルクック距离(通り)
        this.writeC(0x00); // LV
        this.writeC(0x00);
        this.writeC(0xFF);
        this.writeC(0xFF);
    }

    private int getDirection(final int heading) {
        int dir = 0;
        switch (heading) {
            case 2:
                dir = 1;
                break;
            case 3:
                dir = 2;
                break;
            case 4:
                dir = 3;
                break;
            case 6:
                dir = 4;
                break;
            case 7:
                dir = 5;
                break;
        }
        return dir;
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
