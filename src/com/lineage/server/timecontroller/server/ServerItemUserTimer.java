package com.lineage.server.timecontroller.server;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.lock.CharItemsReading;
import com.lineage.server.datatables.lock.PetReading;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.templates.L1Pet;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldItem;
import com.lineage.server.world.WorldPet;

/**
 * 物品使用期限计时轴
 * 
 * @author dexc
 * 
 */
public class ServerItemUserTimer extends TimerTask {

    private static final Log _log = LogFactory
            .getLog(ServerItemUserTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 60 * 1000;// 1分钟
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            final Collection<L1ItemInstance> items = WorldItem.get().all();

            // 不包含元素
            if (items.isEmpty()) {
                return;
            }
            // 目前时间
            final Timestamp ts = new Timestamp(System.currentTimeMillis());

            for (final Iterator<L1ItemInstance> iter = items.iterator(); iter
                    .hasNext();) {
                final L1ItemInstance item = iter.next();
                // 不具备使用期限 忽略
                if (item.get_time() == null) {
                    continue;
                }
                checkItem(item, ts);
                Thread.sleep(5);
            }

        } catch (final Exception e) {
            _log.error("物品使用期限计时时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final ServerItemUserTimer userTimer = new ServerItemUserTimer();
            userTimer.start();
        }
    }

    private static void checkItem(final L1ItemInstance item, final Timestamp ts)
            throws Exception {
        // 指示此 time 对象是否早于给定的 ts 对象。
        if (item.get_time().before(ts)) {
            final L1Object object = World.get().findObject(
                    item.get_char_objid());
            if (object != null) {
                if (object instanceof L1PcInstance) {
                    final L1PcInstance tgpc = (L1PcInstance) object;
                    // 删除物品
                    tgpc.getInventory().removeItem(item);

                    final L1Pet pet = PetReading.get()
                            .getTemplate(item.getId());
                    if (pet != null) {
                        L1PetInstance tgpet = WorldPet.get().get(
                                pet.get_objid());
                        if (tgpet != null) {
                            tgpet.dropItem();
                            tgpet.deleteMe();
                        }
                    }
                }

            } else {
                // 人物不在线上 删除物品
                CharItemsReading.get().deleteItem(item.get_char_objid(), item);
            }
        }
    }
}
