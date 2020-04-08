package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 各种地图<br>
 * 40373~40382<br>
 * 40385~40390<br>
 * 40383，40384新增
 */
public class MapR extends ItemExecutor {

    /**
	 *
	 */
    private MapR() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new MapR();
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
        pc.sendPackets(new S_ServerMessage(74, item.getLogName()));
        // pc.sendPackets(new S_UseMap(pc, item.getId(),
        // item.getItem().getItemId()));
    }
}
