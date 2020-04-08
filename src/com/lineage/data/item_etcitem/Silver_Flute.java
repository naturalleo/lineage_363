package com.lineage.data.item_etcitem;

import java.util.Iterator;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Sound;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.WorldMob;

/**
 * 银笛40700
 */
public class Silver_Flute extends ItemExecutor {

    /**
	 *
	 */
    private Silver_Flute() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Silver_Flute();
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
        pc.sendPacketsX8(new S_Sound(10));

        if (((pc.getX() >= 32619) && (pc.getX() <= 32623))
                && ((pc.getY() >= 33120) && (pc.getY() <= 33124))
                && (pc.getMapId() == 440)) { // 海贼岛前半魔方阵座标
            boolean found = false;

            for (final Iterator<L1MonsterInstance> iter = WorldMob.get().all()
                    .iterator(); iter.hasNext();) {
                final L1MonsterInstance mob = iter.next();
                if (mob != null) {
                    if (mob.getNpcTemplate().get_npcId() == 45875) {
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
            } else {
                L1SpawnUtil.spawn(pc, 45875, 0, 0);
            }
        }
    }
}
