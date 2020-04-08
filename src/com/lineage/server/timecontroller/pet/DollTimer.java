package com.lineage.server.timecontroller.pet;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.ActionCodes;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldDoll;

/**
 * 魔法娃娃处理时间轴
 * 
 * @author dexc
 * 
 */
public class DollTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(DollTimer.class);

    private static Random _random = new Random();

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 60 * 1000;// 60秒
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            final Collection<L1DollInstance> allDoll = WorldDoll.get().all();
            // 不包含元素
            if (allDoll.isEmpty()) {
                return;
            }

            for (final Iterator<L1DollInstance> iter = allDoll.iterator(); iter
                    .hasNext();) {
                final L1DollInstance doll = iter.next();
                final int time = doll.get_time() - 60;
                // time -= 60;
                if (time <= 0) {
                    outDoll(doll);

                } else {
                    doll.set_time(time);
                    if (doll.isDead()) {
                        continue;
                    }
                    checkAction(doll);
                }
                Thread.sleep(50);
            }

        } catch (final Exception e) {
            _log.error("魔法娃娃处理时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final DollTimer dollTimer = new DollTimer();
            dollTimer.start();
        }
    }

    /**
     * 移除魔法娃娃
     * 
     * @param tgpc
     */
    private static void outDoll(final L1DollInstance doll) {
        try {
            if (doll != null) {
                if (doll.destroyed()) {
                    return;
                }
                doll.deleteDoll();
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 娃娃动作
     * 
     * @param doll
     */
    private void checkAction(final L1DollInstance doll) {
        try {
            if (doll.getX() == doll.get_olX() && doll.getY() == doll.get_olY()) {
                int run = _random.nextInt(100) + 1;
                if (run <= 76) {
                    int actionCode = ActionCodes.ACTION_Think; // 66
                    if (_random.nextInt(10) <= 3) {
                        actionCode = ActionCodes.ACTION_Aggress; // 67
                    }

                    doll.broadcastPacketAll(new S_DoActionGFX(doll.getId(),
                            actionCode));
                }
            }
            doll.set_olX(doll.getX());
            doll.set_olY(doll.getY());

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
