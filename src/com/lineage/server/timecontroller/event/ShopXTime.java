package com.lineage.server.timecontroller.event;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.lock.DwarfShopReading;
import com.lineage.server.templates.L1ShopS;
import com.lineage.server.thread.GeneralThreadPool;

/**
 * 托售物件计时轴
 * 
 * @author dexc
 * 
 */
public class ShopXTime extends TimerTask {

    private static final Log _log = LogFactory.getLog(ShopXTime.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 60 * 60 * 1000;// 1小时
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            final Timestamp overTime = new Timestamp(System.currentTimeMillis());
            final HashMap<Integer, L1ShopS> allShopS = DwarfShopReading.get()
                    .allShopS();
            for (final L1ShopS shopS : allShopS.values()) {
                if (shopS.get_end() == 0) {
                    if (overTime.after(shopS.get_overtime())) {
                        shopS.set_end(3);
                        shopS.set_item(null);
                        DwarfShopReading.get().updateShopS(shopS);
                    }
                }
                Thread.sleep(1);
            }
            allShopS.clear();

        } catch (final Exception e) {
            _log.error("托售物件时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final ShopXTime timerTask = new ShopXTime();
            timerTask.start();
        }
    }
}
