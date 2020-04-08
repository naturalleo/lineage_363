package com.lineage.server.timecontroller.skill;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldDragonKnight;

/**
 * 龙骑士觉醒技能MP自然减少处理时间轴
 * 
 * @author dexc
 * 
 */
public class Skill_Awake_Timer extends TimerTask {

    private static final Log _log = LogFactory.getLog(Skill_Awake_Timer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 1000;
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            final Collection<L1PcInstance> allPc = WorldDragonKnight.get()
                    .all();
            // 不包含元素
            if (allPc.isEmpty()) {
                return;
            }

            for (final Iterator<L1PcInstance> iter = allPc.iterator(); iter
                    .hasNext();) {
                final L1PcInstance tgpc = iter.next();
                // 人物死亡
                if (tgpc.isDead()) {
                    continue;
                }

                // 非启动龙骑士觉醒技能 MP自然减少处理时间
                if (!tgpc.isMpReductionActiveByAwake()) {
                    continue;
                }

                // 取回龙骑士觉醒技能 MP自然减少处理时间
                int time = tgpc.get_awakeMprTime();
                time--;
                if (time <= 0) {
                    decreaseMp(tgpc);
                    // 重置时间
                    tgpc.set_awakeMprTime(L1PcInstance.INTERVAL_BY_AWAKE);

                } else {
                    tgpc.set_awakeMprTime(time);
                }
                Thread.sleep(1);
            }

            /*
             * for (final L1PcInstance tgpc : allPc) { // 人物死亡 if
             * (tgpc.isDead()) { continue; }
             * 
             * // 非启动龙骑士觉醒技能 MP自然减少处理时间 if (!tgpc.isMpReductionActiveByAwake())
             * { continue; }
             * 
             * // 取回龙骑士觉醒技能 MP自然减少处理时间 int time = tgpc.get_awakeMprTime();
             * time--; if (time <= 0) { decreaseMp(tgpc); // 重置时间
             * tgpc.set_awakeMprTime(L1PcInstance.INTERVAL_BY_AWAKE);
             * 
             * } else { tgpc.set_awakeMprTime(time); } Thread.sleep(1); }
             */

        } catch (final Exception e) {
            _log.error("龙骑士觉醒技能MP自然减少处理异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final Skill_Awake_Timer awake_Timer = new Skill_Awake_Timer();
            awake_Timer.start();
        }
    }

    /**
     * 龙骑士觉醒技能MP自然减少处理
     * 
     * @param tgpc
     */
    private static void decreaseMp(final L1PcInstance tgpc) {
        try {
            int newMp = Math.max(tgpc.getCurrentMp() - 8, 0);
            tgpc.setCurrentMp(newMp);

        } catch (final Throwable e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
