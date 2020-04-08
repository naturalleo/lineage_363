package com.lineage.server.serverpackets;

import java.util.List;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PetInstance;

/**
 * 物品名单(宠物背包)
 * 
 * @author dexc
 * 
 */
public class S_PetInventory extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物品名单(宠物背包)
     * 
     * @param pet
     */
    /*
     * public S_PetInventory(final L1PetInstance pet) { final
     * List<L1ItemInstance> itemList = pet.getInventory().getItems();
     * 
     * this.writeC(S_OPCODE_SHOWRETRIEVELIST); this.writeD(pet.getId());
     * this.writeH(itemList.size()); this.writeC(0x0b); for (final
     * L1ItemInstance item : itemList) { if (item != null) {
     * this.writeD(item.getId()); this.writeC(0x13);
     * this.writeH(item.get_gfxid()); this.writeC(item.getBless());
     * this.writeD((int) Math.min(item.getCount(), 2000000000));
     * this.writeC(item.isIdentified() ? 1 : 0);
     * this.writeS(item.getViewName()); } } this.writeC(0x0a); }
     */

    /**
     * 物品名单(宠物背包)
     * 
     * @param pet
     * @param b
     *            宠物是否刚进入
     */
    public S_PetInventory(final L1PetInstance pet, boolean b) {
        isTrue(pet);
    }

    private void isTrue(final L1PetInstance pet) {
        final List<L1ItemInstance> itemList = pet.getInventory().getItems();

        this.writeC(S_OPCODE_SHOWRETRIEVELIST);
        this.writeD(pet.getId());
        this.writeH(itemList.size());
        this.writeC(0x0b);
        for (final L1ItemInstance item : itemList) {
            if (item != null) {
                this.writeD(item.getId());
                this.writeC(0x16);
                this.writeH(item.get_gfxid());
                this.writeC(item.getBless());
                this.writeD((int) Math.min(item.getCount(), 2000000000));
                this.writeC(item.isIdentified() ? 1 : 0);
                this.writeS(item.getViewName());
            }
        }
        this.writeC(0x0a);
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
