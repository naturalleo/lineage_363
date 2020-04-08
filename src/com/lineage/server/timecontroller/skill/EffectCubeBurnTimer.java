package com.lineage.server.timecontroller.skill;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1EffectType;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldEffect;

/**
 * 技能NPC状态送出时间轴 幻术师技能(立方：燃烧)
 * 
 * @author dexc
 * 
 */
public class EffectCubeBurnTimer extends TimerTask {

    private static final Log _log = LogFactory
            .getLog(EffectCubeBurnTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = L1EffectInstance.CUBE_INTERVAL;
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            final Collection<L1EffectInstance> allNpc = WorldEffect.get().all();
            // 不包含元素
            if (allNpc.isEmpty()) {
                return;
            }

            for (final Iterator<L1EffectInstance> iter = allNpc.iterator(); iter
                    .hasNext();) {
                final L1EffectInstance effect = iter.next();
                // 不是幻术师技能(立方：燃烧)
                if (effect.effectType() != L1EffectType.isCubeBurn) {
                    continue;
                }
                // 计算结果
                EffectCubeExecutor.get().cubeBurn(effect);
                Thread.sleep(1);
            }

            /*
             * for (final L1EffectInstance effect : allNpc) { // 不是幻术师技能(立方：燃烧)
             * if (effect.effectType() != L1EffectType.isCubeBurn) { continue; }
             * // 计算结果 EffectCubeExecutor.get().cubeBurn(effect);
             * Thread.sleep(1); }
             */

        } catch (final Exception e) {
            _log.error("Npc L1Effect幻术师技能(立方：燃烧)状态送出时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final EffectCubeBurnTimer cubeBurnTimer = new EffectCubeBurnTimer();
            cubeBurnTimer.start();
        }
    }
}
