package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;

/**
 * <font color=#00800>柴火41260</font><BR>
 * 
 * @author dexc
 */
public class Fuel extends ItemExecutor {

    /**
	 *
	 */
    private Fuel() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Fuel();
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
        for (final L1Object object : World.get().getVisibleObjects(pc, 3)) {
            if (object instanceof L1EffectInstance) {
                if (((L1NpcInstance) object).getNpcTemplate().get_npcId() == 81170) {
                    // 附近已经有柴火了。
                    pc.sendPackets(new S_ServerMessage(1162));
                    return;
                }
            }
        }
        int[] loc = new int[2];
        loc = pc.getFrontLoc();
        L1SpawnUtil.spawnEffect(81170, 600, loc[0], loc[1], pc.getMapId(),
                null, 0);
        pc.getInventory().removeItem(item, 1);
    }

}
