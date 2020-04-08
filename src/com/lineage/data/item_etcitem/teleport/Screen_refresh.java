package com.lineage.data.item_etcitem.teleport;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Paralysis;

/**
 * 画面更新符 58002
 */
public class Screen_refresh extends ItemExecutor {

    /**
	 *
	 */
    private Screen_refresh() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Screen_refresh();
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
    	try {
            pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND,
                    true));
			Thread.sleep(1000);
	        final int x = pc.getX();
	        final int y = pc.getY();
	        final short map = pc.getMapId();
	        final int h = pc.getHeading();
	        L1Teleport.teleport(pc, x, y, map, h, false);
            pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND,
                    false));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
