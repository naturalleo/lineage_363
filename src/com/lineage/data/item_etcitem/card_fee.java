package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.lock.AccountReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
/**
 * 10小时点卡58008<br>
 * 40小时点卡58009<br>
 * 80小时点卡59000<br>
 */
public class card_fee extends ItemExecutor {

    /**
	 *
	 */
    private card_fee() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new card_fee();
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
    	if (pc.isActived()) {
    		pc.sendPackets(new S_ServerMessage("请您停止挂机再冲值点卡."));
    		return;
    	}
    	int card = 0;
    	final int card_cou = pc.getNetConnection().getAccount().get_card_fee();
    	pc.getInventory().removeItem(item, 1);
    	if (item.getItemId() == 58008) { //10小时点卡 hjx1000
    		card = 601;
    	}
    	if (item.getItemId() == 58009) { // 40小时点卡
    		card = 2401;
//    		CreateNewItem.createNewItem(pc, 44060, 1);//附送天使香水一瓶 hjx1000
    	}
    	if (item.getItemId() == 58010) { // 80小时点卡
    		card = 4801;
    	}
//    	if (!pc.hasSkillEffect(Card_Fee)) {
//    		pc.getNetConnection().getAccount().set_card_fee(card_cou + card - 1);//扣一小时
//            AccountReading.get().updatecard_fee(pc.getAccountName(), card_cou + card - 1);
//            pc.setSkillEffect(Card_Fee, 3600 * 1000);//一小时 
//    	} else {
    		pc.getNetConnection().getAccount().set_card_fee(card_cou + card);
            AccountReading.get().updatecard_fee(pc.getAccountName(), card_cou + card);
//    	}

        pc.sendPackets(new S_ServerMessage("点卡冲值成功..您账号现在拥有的点卡时间是" + (card_cou + card)+ "分钟"));
    }
}
