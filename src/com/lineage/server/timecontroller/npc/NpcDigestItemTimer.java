package com.lineage.server.timecontroller.npc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance.DelItemTime;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldMob;

/**
 * NPC消化道具时间轴
 * 
 * @author dexc
 * 
 */
public class NpcDigestItemTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(NpcDigestItemTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 5000;// 5秒
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            final Collection<L1MonsterInstance> allMob = WorldMob.get().all();
            // 不包含元素
            if (allMob.isEmpty()) {
                return;
            }

            for (final Iterator<L1MonsterInstance> iter = allMob.iterator(); iter
                    .hasNext();) {
                final L1MonsterInstance mob = iter.next();
                if (mob == null) {
                    continue;
                }
                if (mob.getMaxHp() <= 0) {
                    continue;
                }
                if (mob.isDead()) {
                    continue;
                }
                // 不具备消化功能
                if (mob.getNpcTemplate().get_digestitem() <= 0) {
                    continue;
                }
                // 没有可消化物件
                if (mob.getDigestItemEmpty()) {
                    continue;
                }

                checkDelItem(mob);
                Thread.sleep(50);
            }

        } catch (final Exception e) {
            _log.error("NPC消化道具时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final NpcDigestItemTimer digestItemTimer = new NpcDigestItemTimer();
            digestItemTimer.start();
        }
    }

    private void checkDelItem(L1MonsterInstance mob) {
        try {
            if (mob == null) {
                return;
            }
            if (mob.isDead()) {
                return;
            }
            final HashMap<L1ItemInstance, DelItemTime> allItem = mob
                    .getDigestItem();
            // 不包含元素
            if (allItem.isEmpty()) {
                return;
            }

            int count = allItem.size();
            for (final Iterator<L1ItemInstance> iter = allItem.keySet()
                    .iterator(); iter.hasNext();) {
                final L1ItemInstance key = iter.next();
                DelItemTime value = allItem.get(key);
                if (value._del_item_time <= 0) {
                    count -= 1;
                    continue;
                }
                value._del_item_time = value._del_item_time - 5;
                if (value._del_item_time <= 0) {
                    mob.getInventory().removeItem(key, key.getCount());
                }
            }

            if (count <= 0) {// 清空NPC消化道具时间清单
                mob.getDigestItemClear();
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
