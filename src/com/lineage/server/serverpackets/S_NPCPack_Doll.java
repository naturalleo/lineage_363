package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 物件封包 - 魔法娃娃
 * 
 * @author dexc
 * 
 */
public class S_NPCPack_Doll extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物件封包 - 魔法娃娃
     * 
     * @param pet
     * @param pc
     */
    public S_NPCPack_Doll(final L1DollInstance pet, final L1PcInstance pc) {
        /*
         * int addbyte = 0; int addbyte1 = 1; int addbyte2 = 13; int setting =
         * 4;
         */
        this.writeC(S_OPCODE_CHARPACK);
        this.writeH(pet.getX());
        this.writeH(pet.getY());
        this.writeD(pet.getId());
        this.writeH(pet.getGfxId()); // SpriteID in List.spr
        this.writeC(pet.getStatus()); // Modes in List.spr
        this.writeC(pet.getHeading());
        this.writeC(0x00); // (Bright) - 0~15
        this.writeC(pet.getMoveSpeed()); // ???????? - 0:normal,1:fast,2:slow
        this.writeD(0x00000000); // exp
        this.writeH(0x0000); // Lawful

        this.writeS(pet.getNameId());
        this.writeS(pet.getTitle());

        this.writeC(0x00); // 状态
        this.writeD(0x00000000); // ??

        this.writeS(null); // ??

        if (pet.getMaster() != null) {
            if (pet.getMaster() instanceof L1PcInstance) {
                writeS(pet.getMaster().getName());
            } else if (pet.getMaster() instanceof L1NpcInstance) {
                L1NpcInstance npc = (L1NpcInstance) pet.getMaster();
                writeS(npc.getNameId());
            }

        } else {
            this.writeS("");
        }

        this.writeC(0x00); // 物件分类

        this.writeC(0xff); // HP
        this.writeC(0x00);
        this.writeC(0x00);
        // this.writeC(pet.getLevel()); // LV
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
