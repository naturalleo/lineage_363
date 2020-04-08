package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 精灵结晶40417
 */
public class Elf_Crystallization extends ItemExecutor {

    /**
	 *
	 */
    private Elf_Crystallization() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Elf_Crystallization();
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
        if (((pc.getX() >= 32665) && (pc.getX() <= 32674))
                && ((pc.getY() >= 32976) && (pc.getY() <= 32985))
                && (pc.getMapId() == 440)) {// 海贼岛
            final short mapid = 430;
            L1Teleport.teleport(pc, 32922, 32812, mapid, 5, true);
        } else {
            pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
        }
    }
}
