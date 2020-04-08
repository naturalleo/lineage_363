package com.lineage.data.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.datatables.lock.CharItemPowerReading;
import com.lineage.server.templates.L1Event;

/**
 * 凹槽系统 *
 * 
 * DELETE FROM `server_event` WHERE `id`='39'; INSERT INTO `server_event` VALUES
 * ('39', '凹槽系统', 'PowerItemSet', '1', '0', '说明:启动凹槽系统');
 * 
 * @author dexc
 * 
 */
public class PowerItemSet extends EventExecutor {

    private static final Log _log = LogFactory.getLog(PowerItemSet.class);

    // 凹槽系统
    public static boolean START = false;

    public static int HOLER = 0;// 强化成功机率(1/1000)

    public static int ARMORHOLE = 0;// 防具最大凹槽数量

    public static int WEAPONHOLE = 0;// 武器最大凹槽数量

    /**
	 *
	 */
    private PowerItemSet() {
        // TODO Auto-generated constructor stub
    }

    public static EventExecutor get() {
        return new PowerItemSet();
    }

    @Override
    public void execute(final L1Event event) {
        try {
            START = true;

            final String[] set = event.get_eventother().split(",");

            HOLER = Integer.parseInt(set[0]);

            ARMORHOLE = Integer.parseInt(set[1]);
            if (ARMORHOLE > 5) {
                ARMORHOLE = 5;
            }

            WEAPONHOLE = Integer.parseInt(set[2]);
            if (WEAPONHOLE > 5) {
                WEAPONHOLE = 5;
            }

            // 人物物品凹槽资料
            CharItemPowerReading.get().load();

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
