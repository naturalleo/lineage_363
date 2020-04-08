package com.lineage.server.serverpackets;

import com.lineage.server.datatables.SceneryTable;
import com.lineage.server.model.Instance.L1FieldObjectInstance;

/**
 * 物件封包
 * 
 * @author dexc
 * 
 */
public class S_NPCPack_F extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物件封包
     * 
     * @param npc
     */
    public S_NPCPack_F(final L1FieldObjectInstance npc) {
        this.writeC(S_OPCODE_CHARPACK);
        this.writeH(npc.getX());
        this.writeH(npc.getY());
        this.writeD(npc.getId());

        this.writeH(npc.getGfxId());

        this.writeC(npc.getStatus());

        this.writeC(npc.getHeading());
        this.writeC(npc.getChaLightSize());
        this.writeC(npc.getMoveSpeed());
        this.writeD((int) npc.getExp());
        this.writeH(npc.getTempLawful());
        this.writeS(npc.getNameId());

        final String sceneryHtml = SceneryTable.get().get_sceneryHtml(
                npc.getId());
        if (sceneryHtml != null) {
            this.writeS(sceneryHtml);

        } else {
            this.writeS(null);
        }

        this.writeC(0x00); // 状态

        this.writeD(0x00000000); // 0以外にするとC_27が飞ぶ
        this.writeS(null);
        this.writeS(null); // マスター名？

        this.writeC(0x00); // 物件分类

        this.writeC(0xff); // HP
        this.writeC(0x00);
        this.writeC(0x00);// LV
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
