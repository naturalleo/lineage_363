package com.lineage.data.item_etcitem.wand;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.ActionCodes;
import com.lineage.server.datatables.lock.FurnitureSpawnReading;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1FurnitureInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

/**
 * 移除家具魔杖41401
 */
public class Furniture_Removal_Wand extends ItemExecutor {

    /**
	 *
	 */
    private Furniture_Removal_Wand() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Furniture_Removal_Wand();
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
        final int spellsc_objid = data[0];
        this.useFurnitureRemovalWand(pc, spellsc_objid, item);
    }

    private void useFurnitureRemovalWand(final L1PcInstance pc,
            final int targetId, final L1ItemInstance item) {

        final L1Object target = World.get().findObject(targetId);

        if (target == null) {
            return;
        }

        // 送出封包(动作)
        pc.sendPacketsX8(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Wand));

        final int chargeCount = item.getChargeCount();
        if (chargeCount <= 0) {
            // 没有任何事情发生。
            pc.sendPackets(new S_ServerMessage(79));
            return;
        }

        if ((target != null) && (target instanceof L1FurnitureInstance)) {
            final L1FurnitureInstance furniture = (L1FurnitureInstance) target;
            furniture.deleteMe();
            FurnitureSpawnReading.get().deleteFurniture(furniture);
            item.setChargeCount(item.getChargeCount() - 1);
            pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
        }
    }
}
