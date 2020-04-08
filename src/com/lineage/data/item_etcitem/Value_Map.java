package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 完成的藏宝图40692
 */
public class Value_Map extends ItemExecutor {

    /**
	 *
	 */
    private Value_Map() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Value_Map();
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
        if (pc.getInventory().checkItem(40621)) {
            // 没有任何事情发生。
            pc.sendPackets(new S_ServerMessage(79));
        } else if (((pc.getX() >= 32856) && (pc.getX() <= 32858))
                && ((pc.getY() >= 32857) && (pc.getY() <= 32858))
                && (pc.getMapId() == 443)) { // 海贼岛第三层
            L1Teleport.teleport(pc, 32794, 32839, (short) 443, 5, true);

        } else {
            // 没有任何事情发生。
            pc.sendPackets(new S_ServerMessage(79));
        }
    }

}
