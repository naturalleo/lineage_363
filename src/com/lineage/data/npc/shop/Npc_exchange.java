package com.lineage.data.npc.shop;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.lock.ExchangeReading;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1Exchange;

/**
 * 金币元宝互兑换NPC<BR>
 * XXXX
 * 
 * @author hjx1000
 * 
 */
public class Npc_exchange extends NpcExecutor {

    /**
	 *
	 */
    private Npc_exchange() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_exchange();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
		final int npcid = npc.getNpcId();
		final L1Exchange l1exchange = ExchangeReading.get().getExchangeTable(
				npcid);
		final int count = l1exchange.get_adena_count();// DB上面的价格
		final int up = l1exchange.get_upnumber();
		final int osd = (int) ((count + up) * 1.05);
		final String[] info = new String[] {
				String.valueOf((up)), 
				String.valueOf((osd)), 
				String.valueOf((count)),
				String.valueOf((1))};//对话档上显示的1只怀旧币
		pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "pc_qz", info));
    }

    @Override
    public synchronized void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
    	final int npcid = npc.getNpcId();
    	final L1Exchange l1exchange = ExchangeReading.get().getExchangeTable(npcid);
    	final int count = l1exchange.get_adena_count();//DB上面的价格
    	final int up = l1exchange.get_upnumber();
        if (cmd.equalsIgnoreCase("a")) {       //金币兑换元宝 	
            int[] items = new int[] { 40308 }; //金币
            int[] counts = new int[] { (int) ((count + up) * 1.05d) }; //金币数量
            int[] gitems = new int[] { 44070 };// 元宝
            int[] gcounts = new int[] { 1 }; //元宝数量

            // 需要物件不足
            if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                    counts) >= 1) {// 传回可交换道具数小于1(需要物件不足)
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, 1, gcounts);// 给予
                if (count < 1000000000) {
                    l1exchange.set_price_count(count + up);
                    ExchangeReading.get().updateAdena(npcid, count + up);
                }

            }
        	//pc.sendPackets(new S_SystemMessage("不开放金币兑换元宝.")); 
        } else if (cmd.equalsIgnoreCase("b")) {  //元宝兑换金币
            int[] items = new int[] { 44070 }; //元宝
            int[] counts = new int[] { 1 }; //元宝数量
            int[] gitems = new int[] { 40308 };// 金币
            int[] gcounts = new int[] { count }; //金币数量

            // 需要物件不足
            if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                    counts) >= 1) {// 传回可交换道具数小于1(需要物件不足)
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, 1, gcounts);// 给予
                if (count > 500000) {
                    l1exchange.set_price_count(count - up);
                    ExchangeReading.get().updateAdena(npcid, count - up);
                }

            }
        }
    }
}
