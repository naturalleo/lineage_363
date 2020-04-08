package com.lineage.data.item_etcitem.quest;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.data.quest.IllusionistLv30_1;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.utils.L1SpawnUtil;

/**
 * 49189 索夏依卡灵魂之笛
 */
public class I30_Flute extends ItemExecutor {

    /**
	 *
	 */
    private I30_Flute() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new I30_Flute();
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

        // 任务已经开始
        if (pc.getQuest().isStart(IllusionistLv30_1.QUEST.get_id())) {
            // System.out.println("反王肯恩");
            // 反王肯恩
            L1PolyMorph.doPoly(pc, 6214, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC);

            // 随机周边座标
            final L1Location loc = pc.getLocation().randomLocation(5, false);
            pc.sendPacketsXR(new S_EffectLocation(loc, 7004), 8);
            final L1MonsterInstance mob = L1SpawnUtil.spawnX(45020, loc,
                    pc.get_showId());
            mob.setLink(pc);
        }
        pc.getInventory().removeItem(item, 1);// 移除道具
    }
}
