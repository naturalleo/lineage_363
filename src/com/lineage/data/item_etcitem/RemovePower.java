package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.lock.CharItemPowerReading;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1ItemPower_name;

public class RemovePower extends ItemExecutor {

    /**
     * 移除凹槽的道具 add hjx1000
     */
    private RemovePower() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new RemovePower();
    }

    @Override
    public void execute(final int[] data, final L1PcInstance pc,
            final L1ItemInstance item) {
		// 對象OBJID
		final int targObjId = data[0];

		final L1ItemInstance tgItem = pc.getInventory().getItem(targObjId);

		if (tgItem == null) {
			return;
		}
		if (tgItem.getItem().getType2() == 0) {
			return;
		}
		final L1ItemPower_name power_name = tgItem.get_power_name();
		if (power_name == null) {
			pc.sendPackets(new S_SystemMessage("您选择的物品并没有附魔"));
			return;
		}
		if (tgItem.isEquipped()) {//如果装备中不能使用
			pc.sendPackets(new S_SystemMessage("你必须先解除装备"));
			return;
		}
		final int item_obj_id = tgItem.getId();
		power_name.set_hole_1(0);
		power_name.set_hole_2(0);
		power_name.set_hole_3(0);
		power_name.set_hole_4(0);
		power_name.set_hole_5(0);
		power_name.set_hole_count(0);
		power_name.set_xing_count(0);
		tgItem.set_power_name(null);
		CharItemPowerReading.get().removeItemPower(item_obj_id);
		pc.getInventory().updateItem(tgItem, L1PcInventory.COL_ENCHANTLVL);
		pc.getInventory().saveItem(tgItem, L1PcInventory.COL_ENCHANTLVL);
		pc.getInventory().removeItem(item, 1);
		pc.sendPackets(new S_SystemMessage("您选择的物品已经成功解除附魔"));
    }
}
