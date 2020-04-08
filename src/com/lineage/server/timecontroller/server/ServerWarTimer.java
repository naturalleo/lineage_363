package com.lineage.server.timecontroller.server;

import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.thread.GeneralThreadPool;

/**
 * 城战计时轴
 * 
 * @author dexc
 * 
 */
public class ServerWarTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(ServerWarTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 60 * 1000;// 1分钟
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            ServerWarExecutor.get().checkWarTime();

        } catch (final Exception e) {
            _log.error("城战计时时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final ServerWarTimer warTimer = new ServerWarTimer();
            warTimer.start();
        }
    }
}
