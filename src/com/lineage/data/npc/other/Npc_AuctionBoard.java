package com.lineage.data.npc.other;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ApplyAuction;
import com.lineage.server.serverpackets.S_AuctionBoard;
import com.lineage.server.serverpackets.S_AuctionBoardRead;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_HouseMap;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.WorldClan;

/**
 * 81161 盟屋拍卖公告栏
 * 
 * @author loli
 * 
 */
public class Npc_AuctionBoard extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_AuctionBoard.class);

    /**
	 *
	 */
    private Npc_AuctionBoard() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_AuctionBoard();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            pc.sendPackets(new S_AuctionBoard(npc));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        try {
            boolean isCloseList = false;
            final String[] temp = cmd.split(",");
            final int objid = npc.getId();

            if (temp[0].equalsIgnoreCase("select")) { // 查看资讯
                pc.sendPackets(new S_AuctionBoardRead(objid, temp[1]));

            } else if (temp[0].equalsIgnoreCase("map")) { // 观看位置
                pc.sendPackets(new S_HouseMap(objid, temp[1]));

            } else if (temp[0].equalsIgnoreCase("apply")) { // 参加拍卖
                final L1Clan clan = WorldClan.get().getClan(pc.getClanname());
                if (clan != null) {
                    if (pc.isCrown() && (pc.getId() == clan.getLeaderId())) { // 王族
                                                                              // 血盟盟主
                        if (pc.getLevel() >= 15) {
                            if (clan.getHouseId() == 0) {
                                pc.sendPackets(new S_ApplyAuction(objid,
                                        temp[1]));
                            } else {
                                pc.sendPackets(new S_ServerMessage(521)); // 已经拥有血盟小屋。
                                isCloseList = true;
                            }
                        } else {
                            pc.sendPackets(new S_ServerMessage(519)); // 等级15以下的王族无法参与拍卖。
                            isCloseList = true;
                        }
                    } else {
                        pc.sendPackets(new S_ServerMessage(518)); // 血盟君主才可使用此命令。
                        isCloseList = true;
                    }
                } else {
                    pc.sendPackets(new S_ServerMessage(518)); // 血盟君主才可使用此命令。
                    isCloseList = true;
                }
            }

            if (isCloseList) {
                // 关闭对话窗
                pc.sendPackets(new S_CloseList(pc.getId()));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
