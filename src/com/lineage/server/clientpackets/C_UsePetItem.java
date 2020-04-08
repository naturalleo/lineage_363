package com.lineage.server.clientpackets;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.PetItemTable;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_ItemName;
import com.lineage.server.serverpackets.S_PetEquipment;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1PetItem;
import com.lineage.server.world.WorldPet;

/**
 * 要求使用宠物道具
 * 
 * @author daien
 * 
 */
public class C_UsePetItem extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_UsePetItem.class);

    /*
     * public C_UsePetItem() { }
     * 
     * public C_UsePetItem(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final int data = this.readC();
            // System.out.println("data:"+data);
            final int petId = this.readD();
            final int listNo = this.readC();

            final L1PcInstance pc = client.getActiveChar();
            if (pc == null) {
                return;
            }

            final L1PetInstance pet = WorldPet.get().get(petId);
            if (pet == null) {
                return;
            }
            if (pc.getPetList().get(petId) == null) {
                return;
            }
            switch (pet.getNpcId()) {
                case 45034:// 牧羊犬
                case 45039:// 猫
                case 45040:// 熊
                case 45042:// 杜宾狗
                case 45043:// 狼
                case 45044:// 浣熊
                case 45046:// 小猎犬
                case 45047:// 圣伯纳犬
                case 45048:// 狐狸
                case 45049:// 暴走兔
                case 45053:// 哈士奇
                case 45054:// 柯利
                case 45313:// 斗虎
                case 46042:// 袋鼠
                case 45711:// 高丽幼犬
                case 46044:// 熊猫
                    return;
                    /*
                     * 45686 高等狼 45687 高等牧羊犬 45688 高等杜宾狗 45689 高等哈士奇 45690 高等熊
                     * 45691 高等柯利 45692 高等小猎犬 45693 高等圣伯纳犬 45694 高等狐狸 45695
                     * 高等暴走兔 45696 高等猫 45697 高等浣熊 45710 高等斗虎 45712 高丽犬 46043
                     * 高等袋鼠
                     */
            }

            final L1Inventory inventory = pet.getInventory();
            final List<L1ItemInstance> itemList = inventory.getItems();
            if (itemList.size() <= 0) {
                return;
            }

            final L1ItemInstance item = itemList.get(listNo);
            if (item == null) {
                return;
            }

            final int itemId = item.getItemId();
            // 宠物用道具
            final L1PetItem petItem = PetItemTable.get().getTemplate(itemId);

            if (petItem != null) {
                // 武器
                if (petItem.isWeapom()) {
                    usePetWeapon(pc, pet, item, petItem);
                    pc.sendPackets(new S_PetEquipment(data, pet)); // 装备时更新宠物资讯
                    // 防具
                } else {
                    usePetArmor(pc, pet, item, petItem);
                    pc.sendPackets(new S_PetEquipment(data, pet)); // 装备时更新宠物资讯
                }

            } else {
                pc.sendPackets(new S_ServerMessage(79));
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    /**
     * 装备武器
     * 
     * @param pc
     * @param pet
     * @param weapon
     * @param petItem
     */
    private static void usePetWeapon(final L1PcInstance pc,
            final L1PetInstance pet, final L1ItemInstance weapon,
            final L1PetItem petItem) {
        if (pet.getWeapon() == null) {
            // 设置装备
            setPetWeapon(pc, pet, weapon, petItem);

        } else {
            // 是目前的装备
            if (pet.getWeapon().equals(weapon)) {
                // 解除
                removePetWeapon(pc, pet, pet.getWeapon(), petItem);

            } else {
                // 解除目前的装备
                removePetWeapon(pc, pet, pet.getWeapon(), petItem);
                pc.sendPackets(new S_ItemName(pet.getWeapon()));
                // 设置新装备
                setPetWeapon(pc, pet, weapon, petItem);
            }
        }
        //pc.sendPackets(new S_ItemStatus(weapon));
        //pc.sendPackets(new S_PetInventory(pet, false));
    }

    /**
     * 装备防具
     * 
     * @param pc
     * @param pet
     * @param armor
     * @param petItem
     */
    private static void usePetArmor(final L1PcInstance pc,
            final L1PetInstance pet, final L1ItemInstance armor,
            final L1PetItem petItem) {
        if (pet.getArmor() == null) {
            setPetArmor(pc, pet, armor, petItem);

        } else {
            if (pet.getArmor().equals(armor)) {
                removePetArmor(pc, pet, pet.getArmor(), petItem);

            } else {
                removePetArmor(pc, pet, pet.getArmor(), petItem);
                pc.sendPackets(new S_ItemName(pet.getArmor()));
                setPetArmor(pc, pet, armor, petItem);
            }
        }
        // pc.sendPackets(new S_ItemStatus(armor));
        // pc.sendPackets(new S_PetInventory(pet, false));
    }

    private static void setPetWeapon(final L1PcInstance pc,
            final L1PetInstance pet, final L1ItemInstance weapon,
            final L1PetItem petItem) {
        pet.setHitByWeapon(petItem.getHitModifier());
        pet.setDamageByWeapon(petItem.getDamageModifier());
        pet.addStr(petItem.getAddStr());
        pet.addCon(petItem.getAddCon());
        pet.addDex(petItem.getAddDex());
        pet.addInt(petItem.getAddInt());
        pet.addWis(petItem.getAddWis());
        pet.addMaxHp(petItem.getAddHp());
        pet.addMaxMp(petItem.getAddMp());
        pet.addSp(petItem.getAddSp());
        pet.addMr(petItem.getAddMr());

        pet.setWeapon(weapon);
        weapon.setEquipped(true);
    }

    private static void removePetWeapon(final L1PcInstance pc,
            final L1PetInstance pet, final L1ItemInstance weapon,
            final L1PetItem petItem) {
        pet.setHitByWeapon(0);
        pet.setDamageByWeapon(0);
        pet.addStr(-petItem.getAddStr());
        pet.addCon(-petItem.getAddCon());
        pet.addDex(-petItem.getAddDex());
        pet.addInt(-petItem.getAddInt());
        pet.addWis(-petItem.getAddWis());
        pet.addMaxHp(-petItem.getAddHp());
        pet.addMaxMp(-petItem.getAddMp());
        pet.addSp(-petItem.getAddSp());
        pet.addMr(-petItem.getAddMr());

        pet.setWeapon(null);
        weapon.setEquipped(false);
    }

    private static void setPetArmor(final L1PcInstance pc,
            final L1PetInstance pet, final L1ItemInstance armor,
            final L1PetItem petItem) {
        pet.addAc(petItem.getAddAc());
        pet.addStr(petItem.getAddStr());
        pet.addCon(petItem.getAddCon());
        pet.addDex(petItem.getAddDex());
        pet.addInt(petItem.getAddInt());
        pet.addWis(petItem.getAddWis());
        pet.addMaxHp(petItem.getAddHp());
        pet.addMaxMp(petItem.getAddMp());
        pet.addSp(petItem.getAddSp());
        pet.addMr(petItem.getAddMr());

        pet.setArmor(armor);
        armor.setEquipped(true);
    }

    private static void removePetArmor(final L1PcInstance pc,
            final L1PetInstance pet, final L1ItemInstance armor,
            final L1PetItem petItem) {
        pet.addAc(-petItem.getAddAc());
        pet.addStr(-petItem.getAddStr());
        pet.addCon(-petItem.getAddCon());
        pet.addDex(-petItem.getAddDex());
        pet.addInt(-petItem.getAddInt());
        pet.addWis(-petItem.getAddWis());
        pet.addMaxHp(-petItem.getAddHp());
        pet.addMaxMp(-petItem.getAddMp());
        pet.addSp(-petItem.getAddSp());
        pet.addMr(-petItem.getAddMr());

        pet.setArmor(null);
        armor.setEquipped(false);
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
