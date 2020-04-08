package com.lineage.data.item_etcitem.teleport;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 暗影神殿钥匙2楼40615
 */
public class Dark_Temple_Key2 extends ItemExecutor {

    /**
	 *
	 */
    private Dark_Temple_Key2() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Dark_Temple_Key2();
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
        if (((pc.getX() >= 32701) && (pc.getX() <= 32705))
                && ((pc.getY() >= 32894) && (pc.getY() <= 32898))
                && (pc.getMapId() == 522)) { // 影の神殿1F
            L1Teleport.teleport(pc, 32700, 32896, (short) 523, 5, true);

        } else {
            // 没有任何事情发生
            pc.sendPackets(new S_ServerMessage(79));
        }

    }

}
