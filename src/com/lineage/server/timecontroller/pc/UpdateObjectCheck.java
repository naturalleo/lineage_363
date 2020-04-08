package com.lineage.server.timecontroller.pc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * PC 可见物更新处理 判断
 * 
 * @author dexc
 * 
 */
public class UpdateObjectCheck {

    private static final Log _log = LogFactory.getLog(UpdateObjectCheck.class);

    /**
     * 判断
     * 
     * @param tgpc
     * @return true:执行 false:不执行
     */
    public static boolean check(final L1PcInstance tgpc) {
        try {
            if (tgpc == null) {
                return false;
            }

            if (tgpc.getOnlineStatus() == 0) {
                return false;
            }

            if (tgpc.getNetConnection() == null) {
                return false;
            }

            if (tgpc.isTeleport()) {
                return false;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        }
        return true;
    }
}
