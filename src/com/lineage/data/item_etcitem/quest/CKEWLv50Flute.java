package com.lineage.data.item_etcitem.quest;

import java.util.HashMap;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.data.quest.CKEWLv50_1;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldQuest;

/**
 * 魔之角笛 49167
 */
public class CKEWLv50Flute extends ItemExecutor {

    /**
	 *
	 */
    private CKEWLv50Flute() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new CKEWLv50Flute();
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
        if (pc.getMapId() != (short) CKEWLv50_1.MAPID) {
            // 没有任何事情发生
            pc.sendPackets(new S_ServerMessage(79));
            return;
        }
        final HashMap<Integer, L1Object> mapList = new HashMap<Integer, L1Object>();
        mapList.putAll(World.get().getVisibleObjects(CKEWLv50_1.MAPID));

        int itemid = 49167;
        int i = 0;
        for (L1Object tgobj : mapList.values()) {
            if (tgobj instanceof L1MonsterInstance) {
                final L1MonsterInstance tgnpc = (L1MonsterInstance) tgobj;
                // 不同副本忽略
                if (pc.get_showId() != tgnpc.get_showId()) {
                    continue;
                }
                switch (tgnpc.getNpcId()) {
                    case 87015:// 神官奇诺 (妖精)
                    case 87016:// 神官奇诺 (法师)
                    case 87017:// 神官奇诺 (王族)
                    case 87018:// 神官奇诺 (骑士)
                        i += 16;
                        break;

                    default:
                        break;
                }
            }
            if (tgobj instanceof L1PcInstance) {
                final L1PcInstance tgpc = (L1PcInstance) tgobj;
                // 相同副本
                if (tgpc.get_showId() == pc.get_showId()) {
                    if (tgpc.isCrown()) {// 王族
                        i += 1;
                    } else if (tgpc.isKnight()) {// 骑士
                        i += 2;
                    } else if (tgpc.isElf()) {// 精灵
                        i += 4;
                    } else if (tgpc.isWizard()) {// 法师
                        i += 8;
                    }
                }
            }
        }

        if (i == CKEWLv50_1.USER) {// 人数足
            for (L1Object tgobj : mapList.values()) {
                if (tgobj instanceof L1PcInstance) {
                    final L1PcInstance tgpc = (L1PcInstance) tgobj;
                    // 相同副本
                    if (tgpc.get_showId() == pc.get_showId()) {
                        final L1ItemInstance reitem = tgpc.getInventory()
                                .findItemId(itemid);
                        if (reitem != null) {
                            // 165：\f1%0%s 强烈的颤抖后消失了。
                            tgpc.sendPackets(new S_ServerMessage(165, reitem
                                    .getName()));
                            tgpc.getInventory().removeItem(reitem);// 移除道具
                        }

                        // 随机周边座标
                        final L1Location loc = tgpc.getLocation()
                                .randomLocation(2, false);
                        pc.sendPacketsXR(new S_EffectLocation(loc, 3992), 8);

                        if (tgpc.isCrown()) {// 神官奇诺 (王族)
                            L1SpawnUtil.spawnX(87017, loc, tgpc.get_showId());

                        } else if (tgpc.isKnight()) {// 神官奇诺 (骑士)
                            L1SpawnUtil.spawnX(87018, loc, tgpc.get_showId());

                        } else if (tgpc.isElf()) {// 神官奇诺 (妖精)
                            L1SpawnUtil.spawnX(87015, loc, tgpc.get_showId());

                        } else if (tgpc.isWizard()) {// 神官奇诺 (法师)
                            L1SpawnUtil.spawnX(87016, loc, tgpc.get_showId());
                        }
                    }
                }
            }

        } else {// 人数不足
            // 没有任何事情发生
            pc.sendPackets(new S_ServerMessage(79));
            L1QuestUser quest = WorldQuest.get().get(pc.get_showId());
            quest.endQuest();
        }
        mapList.clear();
    }
}
