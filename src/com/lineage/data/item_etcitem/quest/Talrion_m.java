package com.lineage.data.item_etcitem.quest;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 塔尔立昂的武器材料清单 49230
 */
public class Talrion_m extends ItemExecutor {

    /**
	 *
	 */
    private Talrion_m() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Talrion_m();
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
        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "talrion_m"));
    }
}
