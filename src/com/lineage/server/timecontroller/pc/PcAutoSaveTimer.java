package com.lineage.server.timecontroller.pc;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.echo.ClientExecutor;
import com.lineage.list.OnlineUser;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.thread.PcOtherThreadPool;

/**
 * 人物资料自动保存
 * 
 * @author dexc
 * 
 */
public class PcAutoSaveTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(PcAutoSaveTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 60 * 1000;
        _timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            final Collection<ClientExecutor> allClien = OnlineUser.get().all();
            // 不包含元素
            if (allClien.isEmpty()) {
                return;
            }

            for (final Iterator<ClientExecutor> iter = allClien.iterator(); iter
                    .hasNext();) {
                final ClientExecutor client = iter.next();
                int time = client.get_savePc();
                if (time == -1) {
                    continue;
                }
                time--;
                save(client, time);
            }

            /*
             * for (final ClientExecutor client : allClien) { int time =
             * client.get_savePc(); if (time == -1) { continue; } time--;
             * save(client, time); }
             */

        } catch (final Exception e) {
            _log.error("人物资料自动保存时间轴异常重启", e);
            PcOtherThreadPool.get().cancel(_timer, false);
            final PcAutoSaveTimer restart = new PcAutoSaveTimer();
            restart.start();
        }
    }

    /**
     * 执行人物资料存档
     * 
     * @param pc
     */
    private void save(final ClientExecutor client, final Integer time) {
        try {
            if (client.get_socket() == null) {
                return;
            }
            if (time > 0) {
                // 更新
                client.set_savePc(time);

            } else {
                client.set_savePc(Config.AUTOSAVE_INTERVAL);

                L1PcInstance pc = client.getActiveChar();
                if (pc != null) {
                    pc.save();
                }
            }

        } catch (final Exception e) {
            _log.debug("执行人物资料存档处理异常 帐号:" + client.getAccountName());
        }
    }
}
