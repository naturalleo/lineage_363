package com.lineage.data.npc.teleport;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.NpcTeleportTable;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_PacketBoxGame;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1TeleportLoc;
import com.lineage.server.timecontroller.server.ServerUseMapTimer;

/**
 * 传送师 91053
 * 
 * @author dexc
 * 
 */
public class Npc_Teleport extends NpcExecutor {

    /**
     * 传送师
     */
    private Npc_Teleport() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Teleport();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_t_0"));
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        if (cmd.equals("up")) {
            final int page = pc.get_other().get_page() - 1;
            showPage(pc, npc, page);

        } else if (cmd.equals("dn")) {
            final int page = pc.get_other().get_page() + 1;
            showPage(pc, npc, page);

            // 消除其他地图使用权
        } else if (cmd.equals("del")) {
            final Integer inMap = ServerUseMapTimer.MAP.get(pc);
            if (inMap != null) {
                pc.get_other().set_usemap(-1);
                pc.get_other().set_usemapTime(0);
                pc.sendPackets(new S_PacketBoxGame(
                        S_PacketBoxGame.STARTTIMECLEAR));
                ServerUseMapTimer.MAP.remove(pc);
            }

            // 数字选项
        } else if (cmd.matches("[0-9]+")) {
            final String pagecmd = pc.get_other().get_page() + cmd;
            teleport(pc, npc, Integer.valueOf(pagecmd));

        } else {
            pc.get_other().set_page(0);
            final HashMap<Integer, L1TeleportLoc> teleportMap = NpcTeleportTable
                    .get().get_teles(cmd);
            if (teleportMap != null) {
                if (teleportMap.size() <= 0) {
                    // 1,447：目前暂不开放。
                    pc.sendPackets(new S_ServerMessage(1447));
                    return;
                }
                pc.get_otherList().teleport(teleportMap);
                showPage(pc, npc, 0);
            } else {
                // 1,447：目前暂不开放。
                pc.sendPackets(new S_ServerMessage(1447));
            }
        }
    }

    /**
     * 执行传送
     * 
     * @param pc
     * @param npc
     * @param key
     */
    private void teleport(final L1PcInstance pc, final L1NpcInstance npc,
            final Integer key) {
        // System.out.println("key:" + key);
        final Map<Integer, L1TeleportLoc> list = pc.get_otherList()
                .teleportMap();
        final L1TeleportLoc info = list.get(key);

        // 已经具有时间地图使用权
        if (info.get_time() != 0) {
            final Integer inMap = ServerUseMapTimer.MAP.get(pc);
            if (inMap != null) {
                if (pc.get_other().get_usemap() == info.get_mapid()) {
                    L1Teleport.teleport(pc, info.get_locx(), info.get_locy(),
                            (short) info.get_mapid(), 5, true);

                } else {
                    // 你必须先消除其它地图使用权才能进入这里！！
                    pc.sendPackets(new S_ServerMessage("必须先消除其它地图使用权才能进入这里"));
                }
                return;
            }
        }

        boolean party = false;

        if (info.get_user() > 0) {
            if (!pc.isInParty()) {
                // 425：您并没有参加任何队伍。
                pc.sendPackets(new S_ServerMessage(425));
                return;
            }
            // 不是队长
            if (!pc.getParty().isLeader(pc)) {
                // 你必须是队伍的领导者。
                pc.sendPackets(new S_ServerMessage("你必须是队伍的领导者"));
                return;
            }
            // 人数不足
            if (pc.getParty().getNumOfMembers() < info.get_user()) {
                // 本团队地图要求队伍成员必须达到 %0人。
                pc.sendPackets(new S_ServerMessage("队伍成员必须达到" + info.get_user()
                        + "人"));
                return;
            }
            party = true;
        }

        if (info.get_min() > pc.getLevel()) {
            pc.sendPackets(new S_ServerMessage("等级(" + pc.getLevel() + ")低于限制"));
            return;
        }

        if (info.get_max() < pc.getLevel()) {
            pc.sendPackets(new S_ServerMessage("等级(" + pc.getLevel() + ")超过限制"));
            return;
        }

        final int itemid = info.get_itemid();
        final L1ItemInstance item = pc.getInventory().checkItemX(itemid,
                info.get_price());

        if (item != null) {
            if (info.get_time() != 0) {
                pc.get_other().set_usemap(info.get_mapid());
                ServerUseMapTimer.put(pc, info.get_time());
            }

            pc.getInventory().removeItem(item, info.get_price());// 删除道具

            if (party) {
                final ConcurrentHashMap<Integer, L1PcInstance> pcs = pc
                        .getParty().partyUsers();

                if (pcs.isEmpty()) {
                    return;
                }
                if (pcs.size() <= 0) {
                    return;
                }

                for (final Iterator<L1PcInstance> iter = pcs.values()
                        .iterator(); iter.hasNext();) {
                    final L1PcInstance tgpc = iter.next();
                    if (info.get_time() != 0) {
                        final Integer inMap = ServerUseMapTimer.MAP.get(tgpc);
                        if (inMap != null) {
                            tgpc.get_other().set_usemap(-1);
                            tgpc.get_other().set_usemapTime(0);
                            tgpc.sendPackets(new S_PacketBoxGame(
                                    S_PacketBoxGame.STARTTIMECLEAR));
                            ServerUseMapTimer.MAP.remove(tgpc);
                        }
                        tgpc.get_other().set_usemap(info.get_mapid());
                        ServerUseMapTimer.put(tgpc, info.get_time());
                    }
                    L1Teleport.teleport(tgpc, info.get_locx(), info.get_locy(),
                            (short) info.get_mapid(), 5, true);
                }

            } else {
                L1Teleport.teleport(pc, info.get_locx(), info.get_locy(),
                        (short) info.get_mapid(), 5, true);
            }

        } else {
            // 找回物品
            final L1Item itemtmp = ItemTable.get().getTemplate(itemid);
            pc.sendPackets(new S_ServerMessage(337, itemtmp.getNameId()));
        }
    }

    /**
     * 展示指定页面
     * 
     * @param pc
     * @param npc
     * @param page
     */
    private void showPage(final L1PcInstance pc, final L1NpcInstance npc,
            int page) {
        final Map<Integer, L1TeleportLoc> list = pc.get_otherList()
                .teleportMap();

        // 全部页面数量
        int allpage = list.size() / 10;
        if ((page > allpage) || (page < 0)) {
            page = 0;
        }

        if (list.size() % 10 != 0) {
            allpage += 1;
        }

        pc.get_other().set_page(page);// 设置页面

        final int showId = page * 10;// 要显示的项目ID

        final StringBuilder stringBuilder = new StringBuilder();
        // 每页显示10笔(showId + 10)资料
        for (int key = showId; key < showId + 10; key++) {
            final L1TeleportLoc info = list.get(key);
            if (info != null) {
                // 找回物品
                final L1Item itemtmp = ItemTable.get().getTemplate(
                        info.get_itemid());
                if (itemtmp != null) {
                    stringBuilder.append(info.get_name());
                    if (info.get_user() > 0) {
                        // 6793 (队伍: 6794 人以上)
                        stringBuilder.append("队伍:" + info.get_user());
                    }
                    stringBuilder.append(" (" + itemtmp.getName() + "-"
                            + Integer.toString(info.get_price()) + "),");
                }
            }
        }

        final String[] clientStrAry = stringBuilder.toString().split(",");
        if (allpage == 1) {
            // 核心要求显示仅有一页
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_t_1",
                    clientStrAry));

        } else {
            if (page < 1) {// 无上一页
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_t_3",
                        clientStrAry));

            } else if (page >= (allpage - 1)) {// 无下一页(吻合第一页为0 所以 -1)
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_t_4",
                        clientStrAry));

            } else {// 正常
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_t_2",
                        clientStrAry));
            }
        }
    }
}
