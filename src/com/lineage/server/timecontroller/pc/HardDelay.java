package com.lineage.server.timecontroller.pc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.thread.GeneralThreadPool;

/**
 * 动作延迟使用
 * 
 * @author hjx1000
 * 
 */
public class HardDelay {

    private static final Log _log = LogFactory.getLog(HardDelay.class);
//    final static int time = 100; //动作停滞100毫秒 

    /**
     * 动作延迟使用
     */
    private HardDelay() {
    }

    static class HardDelayTimer implements Runnable {

        private L1PcInstance _pc;

        public HardDelayTimer(final L1PcInstance pc) {
            _pc = pc;
        }

        @Override
        public void run() {
            stopDelayTimer();
        }

        public void stopDelayTimer() {
            _pc.setHardDelay(false);
//            System.out.println("HardDelay=" + _pc.isHardDelay());
        }
    }

    /**
     * 设置动作延迟使用
     * 
     * @param pc
     * @param time
     */
    public static void onHardUse(final L1PcInstance pc, final int time) {
        try {
        	pc.setHardDelay(true);
            GeneralThreadPool.get().schedule(new HardDelayTimer(pc), time);
//            System.out.println("HardDelay=" + pc.isHardDelay());

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
