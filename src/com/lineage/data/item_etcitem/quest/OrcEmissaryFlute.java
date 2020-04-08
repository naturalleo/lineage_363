package com.lineage.data.item_etcitem.quest;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.L1SpawnUtil;

/**
 * 妖魔密使之笛子 49222
 */
public class OrcEmissaryFlute extends ItemExecutor {

    /**
	 *
	 */
    private OrcEmissaryFlute() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new OrcEmissaryFlute();
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
        if (pc.getMapId() != 61) {// 海音地监 3楼
            // 没有任何事情发生
            pc.sendPackets(new S_ServerMessage(79));
            return;
        }

        // 随机周边座标
        final L1Location loc = pc.getLocation().randomLocation(2, false);
        pc.sendPacketsXR(new S_EffectLocation(loc, 3992), 8);
        final L1MonsterInstance mob = L1SpawnUtil.spawnX(84005, loc,
                pc.get_showId());
        mob.setLink(pc);
        pc.getInventory().removeItem(item);// 移除道具
    }
}
