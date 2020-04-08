package com.lineage.data.item_etcitem.quest;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 结盟瞬间移动卷轴 49226
 */
public class DK45_Teleport extends ItemExecutor {

    /**
	 *
	 */
    private DK45_Teleport() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new DK45_Teleport();
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
        if (pc.isDragonKnight()) {// 龙骑
            pc.getInventory().removeItem(item, 1);
            // 传送任务执行者(幻术师村庄)
            L1Teleport.teleport(pc, 32839, 32860, (short) 1000, 2, true);

        } else {
            // 没有任何事情发生
            pc.sendPackets(new S_ServerMessage(79));
        }
    }
}
