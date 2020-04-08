package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

/**
 * 结婚戒指(银)40901<br>
 * 结婚戒指(金)40902<br>
 * 结婚戒指(蓝宝石)40903<br>
 * 结婚戒指(绿宝石)40904<br>
 * 结婚戒指(红宝石)40905<br>
 * 结婚戒指(钻石)40906<br>
 * 西玛戒指40907<br>
 * 欧林戒指40908<br>
 */
public class Ring extends ItemExecutor {

    /**
	 *
	 */
    private Ring() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Ring();
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
        L1PcInstance partner = null;
        boolean partner_stat = false;
        // 不列入判断的地形
        switch (pc.getMapId()) {
            default:
                // 是副本专用地图
                if (QuestMapTable.get().isQuestMap(pc.getMapId())) {
                    return;
                }
                if (pc.getPartnerId() != 0) { // 结婚中
                    partner = (L1PcInstance) World.get().findObject(
                            pc.getPartnerId());
                    if ((partner != null) && (partner.getPartnerId() != 0)
                            && (pc.getPartnerId() == partner.getId())
                            && (partner.getPartnerId() == pc.getId())) {
                        partner_stat = true;
                    }

                } else {
                    pc.sendPackets(new S_ServerMessage(662)); // \f1你(你)目前未婚。
                    return;
                }

                if (partner_stat) {
                    final boolean castle_area = L1CastleLocation
                            .checkInAllWarArea(partner.getX(), partner.getY(),
                                    partner.getMapId());
                    if (((partner.getMapId() == 0) || (partner.getMapId() == 4) || (partner
                            .getMapId() == 304)) && (castle_area == false)) {
                        L1Teleport.teleport(pc, partner.getX(), partner.getY(),
                                partner.getMapId(), 5, true);
                    } else {
                        pc.sendPackets(new S_ServerMessage(547)); // \f1你的情人在你无法传送前往的地区。
                    }

                } else {
                    pc.sendPackets(new S_ServerMessage(546)); // \f1你的情人目前不在线上。
                }
                break;
        }
    }
}
