package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1IllusoryInstance;

/**
 * 物件封包 - 分身
 * 
 * @author dexc
 * 
 */
public class S_NPCPack_Ill extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物件封包 - 分身
     * 
     * @param de
     */
    public S_NPCPack_Ill(final L1IllusoryInstance de) {
        this.writeC(S_OPCODE_CHARPACK);
        this.writeH(de.getX());
        this.writeH(de.getY());
        this.writeD(de.getId());

        this.writeH(de.getTempCharGfx());

        this.writeC(de.getStatus());

        this.writeC(de.getHeading());
        this.writeC(de.getChaLightSize());
        this.writeC(de.getMoveSpeed());
        this.writeD(0x00000000); // exp
        this.writeH(de.getLawful());

        this.writeS(de.getNameId());
        this.writeS(de.getTitle());

        this.writeC(0x00); // 状态
        this.writeD(0x00000000);
        this.writeS(null); // クラン名
        this.writeS(null); // ペッホチング？

        this.writeC(0x00); // 物件分类

        this.writeC(0xff); // HP
        this.writeC(0x00); // タルクック距离(通り)
        this.writeC(0x00); // PC = 0, Mon = Lv
        this.writeC(0x00); // ？
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
