package com.lineage.data.item_etcitem.teleport;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.GetbackTable;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1HouseLocation;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.L1TownLocation;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.WorldClan;

/**
 * 血盟传送卷轴40124
 */
public class Clan_Transmission_Reel extends ItemExecutor {

    /**
	 *
	 */
    private Clan_Transmission_Reel() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Clan_Transmission_Reel();
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
        if (pc.getMap().isEscapable() || pc.isGm()) {
            int castle_id = 0;
            int house_id = 0;
            if (pc.getClanid() != 0) { // 属于血盟成员
                final L1Clan clan = WorldClan.get().getClan(pc.getClanname());
                if (clan != null) {
                    castle_id = clan.getCastleId();
                    house_id = clan.getHouseId();
                }
            }
            if (castle_id != 0) { // 所在血盟拥有城堡
                if (pc.getMap().isEscapable() || pc.isGm()) {
                    int[] loc = new int[3];
                    loc = L1CastleLocation.getCastleLoc(castle_id);
                    final int locx = loc[0];
                    final int locy = loc[1];
                    final short mapid = (short) (loc[2]);
                    L1Teleport.teleport(pc, locx, locy, mapid, 5, true);
                    pc.getInventory().removeItem(item, 1);

                } else {
                    // 647 这附近的能量影响到瞬间移动。在此地无法使用瞬间移动。
                    pc.sendPackets(new S_ServerMessage(647));
                    pc.sendPackets(new S_Paralysis(
                            S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
                }

            } else if (house_id != 0) {
                if (pc.getMap().isEscapable() || pc.isGm()) {
                    int[] loc = new int[3];
                    loc = L1HouseLocation.getHouseLoc(house_id);
                    final int locx = loc[0];
                    final int locy = loc[1];
                    final short mapid = (short) (loc[2]);
                    L1Teleport.teleport(pc, locx, locy, mapid, 5, true);
                    pc.getInventory().removeItem(item, 1);

                } else {
                    // 647 这附近的能量影响到瞬间移动。在此地无法使用瞬间移动。
                    pc.sendPackets(new S_ServerMessage(647));
                    pc.sendPackets(new S_Paralysis(
                            S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
                }

            } else {
                if (pc.getHomeTownId() > 0) {
                    final int[] loc = L1TownLocation.getGetBackLoc(pc
                            .getHomeTownId());
                    final int locx = loc[0];
                    final int locy = loc[1];
                    final short mapid = (short) (loc[2]);
                    L1Teleport.teleport(pc, locx, locy, mapid, 5, true);
                    pc.getInventory().removeItem(item, 1);

                } else {
                    final int[] loc = GetbackTable.GetBack_Location(pc, true);
                    L1Teleport.teleport(pc, loc[0], loc[1], (short) loc[2], 5,
                            true);
                    pc.getInventory().removeItem(item, 1);
                }
            }
        } else {
            // 647 这附近的能量影响到瞬间移动。在此地无法使用瞬间移动。
            pc.sendPackets(new S_ServerMessage(647));
            pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK,
                    false));
        }
        // 解除魔法技能绝对屏障
        L1BuffUtil.cancelAbsoluteBarrier(pc);

    }
}
