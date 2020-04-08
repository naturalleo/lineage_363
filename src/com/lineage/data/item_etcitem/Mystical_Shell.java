package com.lineage.data.item_etcitem;

import java.util.Iterator;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.WorldMob;

/**
 * 神秘贝壳40566
 */
public class Mystical_Shell extends ItemExecutor {

    /**
	 *
	 */
    private Mystical_Shell() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Mystical_Shell();
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
        if (pc.isElf() && ((pc.getX() >= 33971) && (pc.getX() <= 33975))
                && ((pc.getY() >= 32324) && (pc.getY() <= 32328))
                && (pc.getMapId() == 4)) { // 确认位置
            boolean found = false;// 是否已经召唤古代亡灵

            for (final Iterator<L1MonsterInstance> iter = WorldMob.get().all()
                    .iterator(); iter.hasNext();) {
                final L1MonsterInstance mob = iter.next();
                if (mob != null) {
                    if (mob.getNpcTemplate().get_npcId() == 45300) {
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
                // 没有任何事情发生。
                pc.sendPackets(new S_ServerMessage(79));

            } else {
                // 召唤古代亡灵
                L1SpawnUtil.spawn(pc, 45300, 2, 300);
            }

        } else {
            pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
        }
    }
}
