package com.lineage.data.item_etcitem.furniture;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.lock.FurnitureSpawnReading;
import com.lineage.server.model.L1HouseLocation;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1FurnitureInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;

/**
 * 茶几椅子
 */
public class Alice extends ItemExecutor {

    /**
	 *
	 */
    private Alice() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Alice();
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
        final int itemObjectId = item.getId();

        if (!L1HouseLocation.isInHouse(pc.getX(), pc.getY(), pc.getMapId())) {
            pc.sendPackets(new S_ServerMessage(563)); // \f1你无法在这个地方使用。
            return;
        }

        boolean isAppear = true;
        L1FurnitureInstance furniture = null;
        for (final L1Object l1object : World.get().getObject()) {
            if (l1object instanceof L1FurnitureInstance) {
                furniture = (L1FurnitureInstance) l1object;
                if (furniture.getItemObjId() == itemObjectId) {
                    isAppear = false;
                    break;
                }
            }
        }

        if ((pc.getHeading() != 0) && (pc.getHeading() != 2)) {
            pc.sendPackets(new S_ServerMessage(79));// 没有任何事发生
            return;
        }

        final int npcId = 80163;

        if (isAppear) {
            L1SpawnUtil.spawn(pc, npcId, itemObjectId);

        } else {
            furniture.deleteMe();
            FurnitureSpawnReading.get().deleteFurniture(furniture);
        }
    }
}
