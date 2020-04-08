package com.lineage.data.item_etcitem.teleport;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 挂机开关符 58030
 */
public class Hang_fu extends ItemExecutor {

    /**
	 *
	 */
    private Hang_fu() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Hang_fu();
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
    	final String[] R = new String[] {String.valueOf(pc.gethookrange())};
        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "hookrange",
        		R));
//        if (item.get_time() == null) {
//            long time = System.currentTimeMillis();// 目前时间豪秒
//            long x1 = 24 * 60 * 60;// 指定小时耗用秒数 //24小时
//            long x2 = x1 * 1000;// 转为豪秒
//            long upTime = x2 + time;// 目前时间 加上指定天数耗用秒数
//
//            // 时间数据
//            final Timestamp ts = new Timestamp(upTime);
//            item.set_time(ts);
//
//            // 人物背包物品使用期限资料
//            CharItemsTimeReading.get().addTime(item.getId(), ts);
//            pc.sendPackets(new S_ItemName(item));
//        }

    }
}
