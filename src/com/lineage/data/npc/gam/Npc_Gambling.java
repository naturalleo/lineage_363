package com.lineage.data.npc.gam;

import java.util.HashMap;
import java.util.Map;
import com.lineage.data.event.gambling.Gambling;
import com.lineage.data.event.gambling.GamblingNpc;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.lock.GamblingReading;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_NoSell;
import com.lineage.server.serverpackets.S_ShopBuyListGam;
import com.lineage.server.serverpackets.S_ShopSellListGam;
import com.lineage.server.templates.L1Gambling;
import com.lineage.server.timecontroller.event.GamblingTime;
import com.lineage.server.utils.RangeLong;

/**
 * 奇岩赌场管理员<BR>
 * 70035赛西<BR>
 * 70041波金<BR>
 * 70042波丽
 * 
 * @author dexc
 * 
 */
public class Npc_Gambling extends NpcExecutor {

    /**
	 *
	 */
    private Npc_Gambling() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Gambling();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        final Gambling gambling = GamblingTime.get_gambling();
        String[] info = null;
        if (gambling == null) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_g_03", info));
            return;

        } else {
            if (npc.getNpcId() == 91172) {
                // (赛狗场)传师
                L1Teleport.teleport(pc, 33529, 32856, (short) 4, 5, true);
                return;
            }

            String no = String.valueOf(GamblingTime.get_gamblingNo()) + "~"
                    + RangeLong.scount(gambling.get_allRate()) + "~";
            for (Integer key : GamblingTime.get_gambling().get_allNpc()
                    .keySet()) {
                GamblingNpc o = GamblingTime.get_gambling().get_allNpc()
                        .get(key);
                String name = o.get_npc().getNameId();
                long adena = o.get_adena();
                no += name + " [" + RangeLong.scount(adena) + "]~";
            }

            if (GamblingTime.isStart()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_g_E", no
                        .split("~")));
                return;

            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_g_01", no
                        .split("~")));
                return;
            }
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        if (GamblingTime.isStart()) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
            return;
        }

        if (cmd.equals("a")) {// 卖中奖的票
            sell(pc, npc);

        } else if (cmd.equals("b")) {// 下注本场
            pc.sendPackets(new S_ShopSellListGam(pc, npc));

        } else if (cmd.equals("c")) {// 查看纪录
            reading(pc, npc);
        }

    }

    /**
     * 查看纪录
     * 
     * @param pc
     * @param npc
     */
    private void reading(final L1PcInstance pc, final L1NpcInstance npc) {
        final StringBuilder stringBuilder = new StringBuilder();
        final Gambling gambling = GamblingTime.get_gambling();
        final Map<Integer, GamblingNpc> npclist = gambling.get_allNpc();
        for (final GamblingNpc gamblingNpc : npclist.values()) {
            stringBuilder.append(gamblingNpc.get_npc().getNameId() + "~");
            final int npcid = gamblingNpc.get_npc().getNpcId();
            final int[] x = GamblingReading.get().winCount(npcid);
            int all = x[0];
            if (all == 0) {
                all = 1;
            }
            int win = x[1];
            if (win == 0) {
                win = all;
            }
            final double rate = all / win;
            stringBuilder.append(rate + "~");

            final StringBuilder adena = RangeLong.scount(gamblingNpc
                    .get_adena());
            stringBuilder.append(adena + "~");
        }

        final String[] clientStrAry = stringBuilder.toString().split("~");
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_g_02", clientStrAry));
    }

    /**
     * 卖中奖的票
     * 
     * @param pc
     * @param npc
     */
    private void sell(final L1PcInstance pc, final L1NpcInstance npc) {
        final Map<Integer, L1Gambling> sellList = new HashMap<Integer, L1Gambling>();
        // System.out.println("卖中奖的票");
        final L1ItemInstance[] items = pc.getInventory().findItemsId(40309);// 食人妖精竞赛票

        if (items.length <= 0) {
            pc.sendPackets(new S_NoSell(npc));
            sellList.clear();
            return;
        }

        for (final L1ItemInstance item : items) {
            final L1Gambling gam = GamblingReading.get().getGambling(
                    item.getGamNo());
            if (gam != null) {
                final int objid = item.getId();
                sellList.put(new Integer(objid), gam);
                ;
            }
        }

        if (sellList.size() > 0) {
            pc.sendPackets(new S_ShopBuyListGam(pc, npc, sellList));
            pc.get_otherList().set_gamSellList(sellList);

        } else {
            pc.sendPackets(new S_NoSell(npc));
        }

        sellList.clear();
    }
}
