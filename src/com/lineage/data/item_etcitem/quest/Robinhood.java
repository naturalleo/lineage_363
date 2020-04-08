package com.lineage.data.item_etcitem.quest;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 41348 罗宾孙的推荐书
 * 
 * @author dexc
 * 
 */
public class Robinhood extends ItemExecutor {

    /**
	 *
	 */
    private Robinhood() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Robinhood();
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
        // 内容显示
        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "robinhood"));
    }
}
