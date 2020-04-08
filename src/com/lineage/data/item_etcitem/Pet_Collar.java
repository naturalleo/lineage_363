package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.lock.PetReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1Pet;

/**
 * 宠物项圈40314<br>
 * 高级宠物项圈40316
 */
public class Pet_Collar extends ItemExecutor {

    /**
	 *
	 */
    private Pet_Collar() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Pet_Collar();
    }

    /**
     * 道具物件执行
     * 
     * @param data
     *            参数
     * @param pc
     *            执行者
     * @param item
     *            物件
     */
    @Override
    public void execute(final int[] data, final L1PcInstance pc,
            final L1ItemInstance item) {
        if (pc.getInventory().checkItem(41160)) {
            if (this.withdrawPet(pc, item.getId())) {
                pc.getInventory().consumeItem(41160, 1);
            }

        } else {
            pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
        }
    }

    private boolean withdrawPet(final L1PcInstance pc, final int itemObjectId) {
        if (!pc.getMap().isTakePets()) {
            pc.sendPackets(new S_ServerMessage(563));
            return false;
        }

        int petCost = 0;
        int divisor = 6;
        final Object[] petList = pc.getPetList().values().toArray();
        if (petList.length > 2) {
            // 489：你无法一次控制那么多宠物。
            pc.sendPackets(new S_ServerMessage(489));
            return false;
        }
        for (final Object pet : petList) {
            if (pet instanceof L1PetInstance) {
                if (((L1PetInstance) pet).getItemObjId() == itemObjectId) {
                    return false;
                }
            }
            petCost += ((L1NpcInstance) pet).getPetcost();
        }
        int charisma = pc.getCha();
        if (pc.isCrown()) { // 君主
            charisma += 6;

        } else if (pc.isElf()) { // 精灵
            charisma += 12;

        } else if (pc.isWizard()) { // 法师
            charisma += 6;

        } else if (pc.isDarkelf()) { // 黑暗妖精
            charisma += 6;

        } else if (pc.isDragonKnight()) { // 龙骑士
            charisma += 6;

        } else if (pc.isIllusionist()) { // 幻术师
            charisma += 6;
        }


        final L1Pet l1pet = PetReading.get().getTemplate(itemObjectId);

        if (l1pet != null) {
            final int npcId = l1pet.get_npcid();
            charisma -= petCost;
            if ((npcId == 45313) || (npcId == 45710 // タイガー、バトルタイガー
                    ) || (npcId == 45711) || (npcId == 45712)) { // 纪州犬の子犬、纪州犬
                divisor = 12;
            } else {
                divisor = 6;
            }
            final int petCount = charisma / divisor;
            if (petCount <= 0) {
                // 你无法一次控制那么多宠物。
                pc.sendPackets(new S_ServerMessage(489));
                return false;
            }
            final L1Npc npcTemp = NpcTable.get().getTemplate(l1pet.get_npcid());
            final L1PetInstance pet = new L1PetInstance(npcTemp, pc, l1pet);
            pet.setPetcost(divisor);
        }
        return true;
    }
}
