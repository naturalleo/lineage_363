package com.lineage.server.timecontroller.event;

import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigDescs;
import com.lineage.server.serverpackets.S_HelpMessage;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;

/**
 * 服务器介绍与教学<BR>
 * 
 * @author dexc
 * 
 */
public class NewServerTime extends TimerTask {

    private static final Log _log = LogFactory.getLog(NewServerTime.class);

    private int _count = 1;

    private int _time = 0;

    private ScheduledFuture<?> _timer;

    public void start(final int time) {
        _time = time;
        final int timeMillis = time * 1000;// 间隔时间
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            // 产生讯息封包 (服务器介绍与教学)
            World.get().broadcastPacketToAll(
                    new S_HelpMessage(ConfigDescs.getShow(_count)));

            _count++;
            if (_count >= ConfigDescs.get_show_size()) {
                _count = 1;
            }

        } catch (final Exception e) {
            _log.error("服务器介绍与教学时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final NewServerTime timerTask = new NewServerTime();
            timerTask.start(_time);
        }
    }
}
