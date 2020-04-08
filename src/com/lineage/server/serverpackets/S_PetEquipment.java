package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PetInstance;

/**
 * 宠物装备 穿着更新资讯 hjx1000
 * @author Administrator
 *
 */
public class S_PetEquipment extends ServerBasePacket {
	private byte[] _byte = null;
	
    public S_PetEquipment(final int i, final L1PetInstance pet) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(0x25);
        this.writeC(i);
        this.writeD(pet.getId());
        this.writeH(pet.getAc()); // 宠物防御
    }

    @Override
    public byte[] getContent() {
        if (_byte == null) {
            _byte = this.getBytes();
        }
        return _byte;
    }

    @Override
    public String getType() {
        return getClass().getSimpleName();
    }
}
