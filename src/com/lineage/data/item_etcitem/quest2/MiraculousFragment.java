package com.lineage.data.item_etcitem.quest2;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 42533 奇迹的碎片<BR>
 * 100片可以合成 49352 勇敢的玉石
 */
public class MiraculousFragment extends ItemExecutor {

    /**
	 *
	 */
    private MiraculousFragment() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new MiraculousFragment();
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
        long count = item.getCount();
        if (count >= 100) {
            pc.getInventory().removeItem(item, 100);
            // 取得道具
            CreateNewItem.createNewItem(pc, 49352, 1);

        } else {
            // 337 \f1%0不足%s。
            pc.sendPackets(new S_ServerMessage(337, "奇迹的碎片(" + (100 - count)
                    + ")"));
        }
    }
}
