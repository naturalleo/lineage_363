package com.lineage.server.timecontroller.pc;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.thread.PcOtherThreadPool;
import com.lineage.server.world.WorldCrown;

/**
 * PC 可见物更新处理 时间轴(王族)
 * 
 * @author dexc
 * 
 */
public class UpdateObjectCTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(UpdateObjectCTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 350;// 0.35秒
        _timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            final Collection<L1PcInstance> allPc = WorldCrown.get().all();
            // 不包含元素
            if (allPc.isEmpty()) {
                return;
            }

            for (final Iterator<L1PcInstance> iter = allPc.iterator(); iter
                    .hasNext();) {
                final L1PcInstance tgpc = iter.next();
                if (UpdateObjectCheck.check(tgpc)) {
                    tgpc.updateObject();
                }
            }

            /*
             * for (final L1PcInstance iter : allPc) { if
             * (UpdateObjectCheck.check(iter)) { iter.updateObject(); } }
             */

        } catch (final Exception e) {
            _log.error("Pc 可见物更新处理时间轴(王族)异常重启", e);
            PcOtherThreadPool.get().cancel(_timer, false);
            final UpdateObjectCTimer objectCTimer = new UpdateObjectCTimer();
            objectCTimer.start();
        }
    }
}
