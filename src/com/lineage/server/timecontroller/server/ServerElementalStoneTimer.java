package com.lineage.server.timecontroller.server;

import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.L1GroundInventory;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.types.Point;
import com.lineage.server.world.World;

/**
 * 元素石生成 时间轴
 * 
 * @author dexc
 * 
 */
public class ServerElementalStoneTimer extends TimerTask {

    private static final Log _log = LogFactory
            .getLog(ServerElementalStoneTimer.class);

    private ScheduledFuture<?> _timer;

    private static final short ELVEN_FOREST_MAPID = 4;
    private static final int MAX_COUNT = ConfigAlt.ELEMENTAL_STONE_AMOUNT; // 设置个数
    private static final int INTERVAL = 3; // 设置间隔 秒
    private static final int SLEEP_TIME = 30; // 设置终了后、再设置までのスリープ时间 秒
    private static final int FIRST_X = 32911;
    private static final int FIRST_Y = 32210;
    private static final int LAST_X = 33141;
    private static final int LAST_Y = 32500;
    private static final int ELEMENTAL_STONE_ID = 40515; // 元素石

    private ArrayList<L1GroundInventory> _itemList = new ArrayList<L1GroundInventory>(
            MAX_COUNT);

    private final L1Map _map = L1WorldMap.get().getMap(ELVEN_FOREST_MAPID);

    private Random _random = new Random();

    private final L1Object _dummy = new L1Object();

    public void start() {
        final int timeMillis = SLEEP_TIME * 1000;// 指定秒数
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    /**
     * 指定された位置に石を置けるかを返す。
     */
    private boolean canPut(final L1Location loc) {
        _dummy.setMap(loc.getMap());
        _dummy.setX(loc.getX());
        _dummy.setY(loc.getY());

        // 可视范围のプレイヤーチェック
        if (World.get().getVisiblePlayer(_dummy).size() > 0) {
            return false;
        }
        return true;
    }

    /**
     * 次の设置ポイントを决める。
     */
    private Point nextPoint() {
        final int newX = this._random.nextInt(LAST_X - FIRST_X) + FIRST_X;
        final int newY = this._random.nextInt(LAST_Y - FIRST_Y) + FIRST_Y;

        return new Point(newX, newY);
    }

    /**
     * 拾われた石をリストから削除する。
     */
    private void removeItemsPickedUp() {
        for (int i = 0; i < this._itemList.size(); i++) {
            final L1GroundInventory gInventory = this._itemList.get(i);
            if (!gInventory.checkItem(ELEMENTAL_STONE_ID)) {
                this._itemList.remove(i);
                i--;
            }
        }
    }

    /**
     * 指定された位置へ石を置く。
     */
    private void putElementalStone(final L1Location loc) {
        final L1GroundInventory gInventory = World.get().getInventory(loc);

        final L1ItemInstance item = ItemTable.get().createItem(
                ELEMENTAL_STONE_ID);
        item.setEnchantLevel(0);
        item.setCount(1);
        gInventory.storeItem(item);
        this._itemList.add(gInventory);
    }

    @Override
    public void run() {
        try {
            this.removeItemsPickedUp();

            while (this._itemList.size() < MAX_COUNT) { // 减っている场合セット
                final L1Location loc = new L1Location(this.nextPoint(),
                        this._map);

                if (!this.canPut(loc)) {
                    // XXX 设置范围内全てにPCが居た场合无限ループになるが…
                    continue;
                }

                this.putElementalStone(loc);

                Thread.sleep(INTERVAL * 1000); // 一定时间每に设置
            }

        } catch (final Throwable e) {
            _log.error("元素石生成时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final ServerElementalStoneTimer elementalStoneTimer = new ServerElementalStoneTimer();
            elementalStoneTimer.start();
        }
    }
}
