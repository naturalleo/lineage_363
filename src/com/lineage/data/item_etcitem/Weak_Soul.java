package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 微弱的灵魂41208
 */
public class Weak_Soul extends ItemExecutor {

    /**
	 *
	 */
    private Weak_Soul() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Weak_Soul();
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
        if (((pc.getX() >= 32844) && (pc.getX() <= 32845))
                && ((pc.getY() >= 32693) && (pc.getY() <= 32694))
                && (pc.getMapId() == 550)) { // 船的墓场:地上层
            L1Teleport.teleport(pc, 32833, 33089, (short) 550, 5, true);

        } else {
            // 没有任何事情发生。
            pc.sendPackets(new S_ServerMessage(79));
        }
    }
}
