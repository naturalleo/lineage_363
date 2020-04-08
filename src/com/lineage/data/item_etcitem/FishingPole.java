package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.ActionCodes;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Fishing;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.timecontroller.pc.PcFishingTimer;

/**
 * 
 * 长钓鱼竿41293<BR>
 * 短钓鱼竿41294<BR>
 * 
 * @author dexc
 */
public class FishingPole extends ItemExecutor {

    /**
	 *
	 */
    private FishingPole() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new FishingPole();
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
        final int fishX = data[0];
        final int fishY = data[1];
        final int itemId = item.getItemId();
        this.startFishing(pc, itemId, fishX, fishY);
    }

    private void startFishing(final L1PcInstance pc, final int itemId,
            final int fishX, final int fishY) {
        if (pc.getMapId() != 5300) {
            // 无法在这个地区使用钓竿。
            pc.sendPackets(new S_ServerMessage(1138));
            return;
        }

        int rodLength = 0;
        if (itemId == 41293) {
            rodLength = 5;

        } else if (itemId == 41294) {
            rodLength = 3;
        }
        if (pc.getMap().isFishingZone(fishX, fishY)) {
            if (pc.getMap().isFishingZone(fishX + 1, fishY)
                    && pc.getMap().isFishingZone(fishX - 1, fishY)
                    && pc.getMap().isFishingZone(fishX, fishY + 1)
                    && pc.getMap().isFishingZone(fishX, fishY - 1)) {

                if ((fishX > pc.getX() + rodLength)
                        || (fishX < pc.getX() - rodLength)) {
                    // 无法在这个地区使用钓竿。
                    pc.sendPackets(new S_ServerMessage(1138));

                } else if ((fishY > pc.getY() + rodLength)
                        || (fishY < pc.getY() - rodLength)) {
                    // 无法在这个地区使用钓竿。
                    pc.sendPackets(new S_ServerMessage(1138));

                } else if (pc.getInventory().checkItem(41295, 2)) {
                    pc.sendPacketsAll(new S_Fishing(pc.getId(),
                            ActionCodes.ACTION_Fishing, fishX, fishY));

                    pc.setFishing(true, fishX, fishY);

                    PcFishingTimer.addMember(pc);

                } else {
                    // 钓鱼就必须要有饵。
                    pc.sendPackets(new S_ServerMessage(1137));
                }

            } else {
                // 无法在这个地区使用钓竿。
                pc.sendPackets(new S_ServerMessage(1138));
            }

        } else {
            // 无法在这个地区使用钓竿。
            pc.sendPackets(new S_ServerMessage(1138));
        }
    }
}
