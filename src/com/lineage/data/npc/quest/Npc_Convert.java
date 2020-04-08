package com.lineage.data.npc.quest;

import java.sql.Timestamp;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.AccountReading;
import com.lineage.server.datatables.lock.CharItemsTimeReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 领双NPC<BR>
 * 98009<BR>
 * 说明:转换月卡NPC
 * 
 * @author hjx1000
 * 
 */
public class Npc_Convert extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Convert.class);

    private Npc_Convert() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Convert();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {

        	//显示转换点卡对话窗
    		pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dianka01"));
        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
    	if (pc.isActived()) {
    		return;
    	}
        if (pc.getNetConnection().getAccount().get_card_fee() >= 19200) {
        	if (pc.getInventory().checkItem(58888)) {
        		System.out.println("月");
            	final int card_cou = pc.getNetConnection().getAccount().get_card_fee() - 19200;
                pc.getNetConnection().getAccount().set_card_fee(card_cou);
                AccountReading.get().updatecard_fee(pc.getAccountName(), card_cou);
        		final L1ItemInstance item = pc.getInventory().findItemId(58888);
        		long time = item.get_time().getTime();
        		pc.getInventory().removeItem(item);
        		final L1ItemInstance item1 = ItemTable.get().createItem(58888);
        		long x1 = 720 * 60 * 60;// 指定小时耗用秒数
                long x2 = x1 * 1000;// 转为豪秒
        		long upTime = x2 + time;// 目前时间 加上指定天数耗用秒数
                // 时间数据
                final Timestamp ts = new Timestamp(upTime);
                item1.set_time(ts);
                // 人物背包物品使用期限资料
                CharItemsTimeReading.get().addTime(item1.getId(), ts);
                pc.getInventory().storeItem(item1);
                pc.sendPackets(new S_ServerMessage("\\aD已增加点卡时间30天。"));
        	} else {
        		final L1ItemInstance item = ItemTable.get().createItem(58888);
        		long time = System.currentTimeMillis();// 目前时间豪秒
        		long x1 = 720 * 60 * 60;// 指定小时耗用秒数
                long x2 = x1 * 1000;// 转为豪秒
        		long upTime = x2 + time;// 目前时间 加上指定天数耗用秒数
                // 时间数据
                final Timestamp ts = new Timestamp(upTime);
                item.set_time(ts);
                // 人物背包物品使用期限资料
                CharItemsTimeReading.get().addTime(item.getId(), ts);
        		if (pc.getInventory().checkAddItem(item, 1) == 0) {
                	final int card_cou = pc.getNetConnection().getAccount().get_card_fee() - 19200;
                    pc.getNetConnection().getAccount().set_card_fee(card_cou);
                    AccountReading.get().updatecard_fee(pc.getAccountName(), card_cou);
        			pc.getInventory().storeItem(item);
        			pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
        		}
        	}
        } else {
        	pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dianka02"));
        }
        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
