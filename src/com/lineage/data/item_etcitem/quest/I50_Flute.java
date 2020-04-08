package com.lineage.data.item_etcitem.quest;

import java.util.HashMap;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.data.quest.IllusionistLv50_1;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;

/**
 * 49208 蓝色之火碎片
 */
public class I50_Flute extends ItemExecutor {

    /**
	 *
	 */
    private I50_Flute() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new I50_Flute();
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
        if (pc.getMapId() != IllusionistLv50_1.MAPID) {// 奎斯特
            // 没有任何事情发生
            pc.sendPackets(new S_ServerMessage(79));
            return;
        }
        final HashMap<Integer, L1Object> mapList = new HashMap<Integer, L1Object>();
        mapList.putAll(World.get().getVisibleObjects(pc.getMapId()));

        int i = 0;
        for (L1Object tgobj : mapList.values()) {
            if (tgobj instanceof L1MonsterInstance) {
                final L1MonsterInstance tgnpc = (L1MonsterInstance) tgobj;
                // 不同副本忽略
                if (pc.get_showId() != tgnpc.get_showId()) {
                    continue;
                }
                if (tgnpc.getNpcId() == 45026) {// 塞维斯
                    i += 1;
                }
            }
        }

        if (i > 0) {// 已有塞维斯
            // 没有任何事情发生
            pc.sendPackets(new S_ServerMessage(79));

        } else {// 召唤塞维斯
            // 随机周边座标
            final L1Location loc = pc.getLocation().randomLocation(5, false);
            pc.sendPacketsXR(new S_EffectLocation(loc, 7004), 8);
            final L1MonsterInstance mob = L1SpawnUtil.spawnX(45026, loc,
                    pc.get_showId());
            mob.setLink(pc);
            pc.getInventory().removeItem(item, 1);// 移除道具
        }
        mapList.clear();
    }
}
