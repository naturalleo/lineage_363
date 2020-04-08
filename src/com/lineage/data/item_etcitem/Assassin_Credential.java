package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 刺客之证40572
 */
public class Assassin_Credential extends ItemExecutor {

    /**
	 *
	 */
    private Assassin_Credential() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Assassin_Credential();
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
        if ((pc.getX() == 32778) && (pc.getY() == 32738)
                && (pc.getMapId() == 21)) {
            L1Teleport.teleport(pc, 32781, 32728, (short) 21, 5, true);

        } else if ((pc.getX() == 32781) && (pc.getY() == 32728)
                && (pc.getMapId() == 21)) {
            L1Teleport.teleport(pc, 32778, 32738, (short) 21, 5, true);

        } else {// 没有任何事情发生。
            pc.sendPackets(new S_ServerMessage(79));
        }
    }
}
