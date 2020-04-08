package com.lineage.data.item_etcitem.quest;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 希莲恩的第三次信件 49174
 */
public class Silrein3lt extends ItemExecutor {

    /**
	 *
	 */
    private Silrein3lt() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Silrein3lt();
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
        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "silrein3lt"));
    }
}
