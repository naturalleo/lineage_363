package com.lineage.data.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.datatables.lock.VIPReading;
import com.lineage.server.templates.L1Event;
import com.lineage.server.timecontroller.event.VIPTimer;

/**
 * VIP
 * 
 * @author dexc
 * 
 */
public class VIPSet extends EventExecutor {

    private static final Log _log = LogFactory.getLog(VIPSet.class);

    public static int ADENA;// 花费

    public static int DATETIME;// VIP时间(天)

    public static int ITEMID;// 需要物品编号

    /**
	 *
	 */
    private VIPSet() {
        // TODO Auto-generated constructor stub
    }

    public static EventExecutor get() {
        return new VIPSet();
    }

    @Override
    public void execute(final L1Event event) {
        try {
            final String[] set = event.get_eventother().split(",");

            ADENA = Integer.parseInt(set[0]);

            DATETIME = Integer.parseInt(set[1]);

            ITEMID = Integer.parseInt(set[2]);

            VIPReading.get().load();

            // VIP计时时间轴
            final VIPTimer exp11Timer = new VIPTimer();
            exp11Timer.start();

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
