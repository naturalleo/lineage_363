package com.lineage.server.timecontroller.server;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;

/**
 * 自动清除地面物件时间轴
 * 
 * @author dexc
 * 
 */
public class ServerDeleteItemTimer extends TimerTask {

    private static final Log _log = LogFactory
            .getLog(ServerDeleteItemTimer.class);

    private ScheduledFuture<?> _timer;

    private static final ArrayList<L1ItemInstance> _itemList = new ArrayList<L1ItemInstance>();

    public void start() {
        final int timeMillis = ConfigAlt.ALT_ITEM_DELETION_TIME * 60 * 1000;
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    public static void add(final L1ItemInstance item) {
        _itemList.add(item);
    }

    public static boolean contains(final L1ItemInstance item) {
        return _itemList.contains(item);
    }

    public static void remove(final L1ItemInstance item) {
        if (!_itemList.remove(item)) {
            _log.error("地面物件删除失败 OBJID:" + item.getId());
        }

        final int x = item.getX();
        final int y = item.getY();
        final short m = item.getMapId();
        // 取回物件点背包资料
        final L1Inventory inventory = World.get().getInventory(x, y, m);
        if (inventory.getItem(item.getId()) != null) {
            inventory.removeItem(item);
        }
    }

    @Override
    public void run() {
        try {
            if (_itemList.isEmpty()) {
                return;
            }
            for (final Object object : _itemList.toArray()) {
                final L1ItemInstance e = (L1ItemInstance) object;
                if (e == null) {
                    continue;
                }
                if (checkItem(e)) {
                    remove(e);
                }
            }
        } catch (final Exception e) {
            _log.error("自动清除地面物件时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final ServerDeleteItemTimer deleteItemTimer = new ServerDeleteItemTimer();
            deleteItemTimer.start();
        }
    }

    /**
     * 检查物品
     * 
     * @param item
     * @return
     */
    private static boolean checkItem(final L1ItemInstance item) {
        final List<L1PcInstance> players = World.get().getVisiblePlayer(item);
        // 指定范围内有PC
        if (!players.isEmpty()) {
            return false;
        }

        final int x = item.getX();
        final int y = item.getY();
        final short m = item.getMapId();

        // 取回物件点背包资料
        final L1Inventory inventory = World.get().getInventory(x, y, m);
        if (inventory.getItem(item.getId()) == null) {
            remove(item);
            return false;
        }
        return true;
    }
}
