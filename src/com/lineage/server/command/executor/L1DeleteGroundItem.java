package com.lineage.server.command.executor;

import java.util.ArrayList;

import com.lineage.server.datatables.lock.FurnitureSpawnReading;
import com.lineage.server.datatables.sql.LetterTable;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1FurnitureInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.world.World;

/**
 * 删除地面物品
 * 
 * @author dexc
 * 
 */
public class L1DeleteGroundItem implements L1CommandExecutor {

    private L1DeleteGroundItem() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1DeleteGroundItem();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        for (final L1Object l1object : World.get().getObject()) {
            if (l1object instanceof L1ItemInstance) {
                final L1ItemInstance item = (L1ItemInstance) l1object;
                if ((item.getX() == 0) && (item.getY() == 0)) { // 地面上のアイテムではなく、谁かの所有物
                    continue;
                }

                final ArrayList<L1PcInstance> tpc = World.get()
                        .getVisiblePlayer(item, 0);
                if (0 == tpc.size()) {
                    final L1Inventory inv = World.get().getInventory(
                            item.getX(), item.getY(), item.getMapId());
                    final int itemId = item.getItem().getItemId();
                    if ((itemId >= 49016) && (itemId <= 49025)) { // 便笺
                        final LetterTable lettertable = new LetterTable();
                        lettertable.deleteLetter(item.getId());

                    } else if ((itemId >= 41383) && (itemId <= 41400)) { // 家具
                        if (l1object instanceof L1FurnitureInstance) {
                            final L1FurnitureInstance furniture = (L1FurnitureInstance) l1object;
                            if (furniture.getItemObjId() == item.getId()) { // 既に引き出している家具
                                FurnitureSpawnReading.get().deleteFurniture(
                                        furniture);
                            }
                        }
                    }
                    inv.deleteItem(item);
                    World.get().removeVisibleObject(item);
                    World.get().removeObject(item);
                }
            }
        }
        World.get().broadcastServerMessage("删除地面物品。");
    }
}
