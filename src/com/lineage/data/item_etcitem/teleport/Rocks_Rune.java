package com.lineage.data.item_etcitem.teleport;

import java.util.Random;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 奇岩回城符 58038
 */
public class Rocks_Rune extends ItemExecutor {
	private final Random _random = new Random();

    /**
	 *
	 */
    private Rocks_Rune() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Rocks_Rune();
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
    	final int[][] locs = new int[][] { new int[] { 33442, 32797, 4 },
                new int[] { 33446, 32807, 4 },
                new int[] { 33430, 32809, 4 } };
    	final int[] loc = locs[_random.nextInt(locs.length)];
        L1Teleport
        .teleport(pc, loc[0], loc[1], (short) loc[2], 5, true);
    	if (pc.isActived()) {
    		pc.setActived(false);
    		pc.sendPackets(new S_ServerMessage("\\aD自动挂机已经结束。"));
    	}
    }
}
