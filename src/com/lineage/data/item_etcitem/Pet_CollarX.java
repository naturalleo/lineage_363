package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.lock.PetReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_NewMaster;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1Pet;

/**
 * 魔幻 项圈 42532
 */
public class Pet_CollarX extends ItemExecutor {

    /**
	 *
	 */
    private Pet_CollarX() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Pet_CollarX();
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
        if (pc.getInventory().checkItem(49352)) {// 勇敢的玉石
            if (this.withdrawPet(pc, item.getId())) {
                pc.getInventory().consumeItem(49352, 1);
            }

        } else {
            // 337 \f1%0不足%s。
            pc.sendPackets(new S_ServerMessage(337, "勇敢的玉石(1)"));
            // pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
        }
    }

    private boolean withdrawPet(final L1PcInstance pc, final int itemObjectId) {
        if (!pc.getMap().isTakePets()) {
            // \f1你无法在这个地方使用。
            pc.sendPackets(new S_ServerMessage(563));
            return false;
        }

        final Object[] petList = pc.getPetList().values().toArray();
        if (petList.length >= 1) {
            // 489：你无法一次控制那么多宠物。
            pc.sendPackets(new S_ServerMessage(489));
            return false;
        }

        final L1Pet l1pet = PetReading.get().getTemplate(itemObjectId);

        if (l1pet != null) {
            final L1Npc npcTemp = NpcTable.get().getTemplate(l1pet.get_npcid());
            final L1PetInstance pet = new L1PetInstance(npcTemp, pc, l1pet);
            pet.setPetcost(128);
            // 增加物件组人
            pc.sendPackets(new S_NewMaster(pc.getName(), pet));
        }
        return true;
    }
}
