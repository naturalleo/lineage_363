package com.lineage.data.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.datatables.ShopXTable;
import com.lineage.server.datatables.lock.DwarfShopReading;
import com.lineage.server.templates.L1Event;
import com.lineage.server.timecontroller.event.ShopXTime;

/**
 * 托售管理员
 * 
 * @author dexc
 * 
 */
public class ShopXSet extends EventExecutor {

    private static final Log _log = LogFactory.getLog(ShopXSet.class);

    public static int ADENA;// 手续费

    public static int DATE;// 寄售时间(天)

    public static int MIN;// 最低出售价

    public static int MAX;// 最高出售价

    /**
	 *
	 */
    private ShopXSet() {
        // TODO Auto-generated constructor stub
    }

    public static EventExecutor get() {
        return new ShopXSet();
    }

    @Override
    public void execute(final L1Event event) {
        try {
            final String[] set = event.get_eventother().split(",");

            ADENA = Integer.parseInt(set[0]);

            DATE = Integer.parseInt(set[1]);

            MIN = Integer.parseInt(set[2]);

            MAX = Integer.parseInt(set[3]);

            DwarfShopReading.get().load();

            // 载入禁止拍卖物品资料
            ShopXTable.get().load();

            ShopXTime timer = new ShopXTime();
            timer.start();

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
