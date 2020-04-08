package com.lineage.data.item_etcitem.dragon;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_ItemName;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * <font color=#00800>42511 顽皮幼龙蛋</font><BR>
 * 
 * @author dexc
 * 
 */
public class HatchlingEgg_Yellow extends ItemExecutor {

    /**
	 *
	 */
    private HatchlingEgg_Yellow() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new HatchlingEgg_Yellow();
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
        // 例外状况:物件为空
        if (item == null) {
            return;
        }
        // 例外状况:人物为空
        if (pc == null) {
            return;
        }

        int petcost = 0;
        final Object[] petList = pc.getPetList().values().toArray();
        if (petList.length > 2) {
            // 489：你无法一次控制那么多宠物。
            pc.sendPackets(new S_ServerMessage(489));
            return;
        }
        for (final Object pet : petList) {
            int nowpetcost = ((L1NpcInstance) pet).getPetcost();
            petcost += nowpetcost;
        }

        int charisma = pc.getCha();

        if (pc.isCrown()) {// 王族
            charisma += 6;

        } else if (pc.isKnight()) {// 骑士

        } else if (pc.isElf()) {// 精灵
            charisma += 12;

        } else if (pc.isWizard()) {// 法师
            charisma += 6;

        } else if (pc.isDarkelf()) {// 黑暗精灵
            charisma += 6;

        } else if (pc.isDragonKnight()) {// 龙骑士
            charisma += 6;

        } else if (pc.isIllusionist()) {// 幻术师
            charisma += 6;
        }

        charisma -= petcost;

        if (charisma <= 0) {
            // 489：你无法一次控制那么多宠物。
            pc.sendPackets(new S_ServerMessage(489));
            return;
        }

        final L1PcInventory inv = pc.getInventory();
        if (inv.getSize() < 180) {
            final L1ItemInstance petamu = inv.storeItem(40314, 1); // 项圈
            if (petamu != null) {
                new L1PetInstance(71020, pc, petamu.getId());
                pc.sendPackets(new S_ItemName(petamu));

                // 删除道具
                pc.getInventory().removeItem(item, 1);
            }
        }
    }
}
