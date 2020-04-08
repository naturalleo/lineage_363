package com.lineage.data.item_etcitem.quest;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 41346 罗宾孙的便条纸(1)
 * 
 * @author dexc
 * 
 */
public class Robinscroll extends ItemExecutor {

    /**
	 *
	 */
    private Robinscroll() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Robinscroll();
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
        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "robinscroll"));
    }
}
