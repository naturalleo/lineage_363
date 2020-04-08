package com.lineage.server.model.poison;

import static com.lineage.server.model.skill.L1SkillId.STATUS_POISON_PARALYZED;
import static com.lineage.server.model.skill.L1SkillId.STATUS_POISON_PARALYZING;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.ModelError;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.thread.GeneralThreadPool;

/**
 * 麻痹型中毒
 * 
 * @author dexc
 * 
 */
public class L1ParalysisPoison extends L1Poison {

    private static final Log _log = LogFactory.getLog(L1ParalysisPoison.class);

    // 麻痹毒の性能一览 犹予 持续 (参考值、未适用)
    // グール 20 45
    // アステ 10 60
    // 蚁穴ムカデ 14 30
    // D-グール 39 45

    private final L1Character _target;

    private Thread _timer;

    private final int _delay;

    private final int _time;

    private int _effectId = 1;

    private class ParalysisPoisonTimer extends Thread {
        @Override
        public void run() {
            L1ParalysisPoison.this._target.setSkillEffect(
                    STATUS_POISON_PARALYZING, 0);

            try {
                Thread.sleep(L1ParalysisPoison.this._delay); // 麻痹するまでの犹予时间を待つ。

            } catch (final InterruptedException e) {
                ModelError.isError(_log, e.getLocalizedMessage(), e);
                return;
            }

            // 绿色改灰色
            L1ParalysisPoison.this._effectId = 2;
            L1ParalysisPoison.this._target.setPoisonEffect(2);

            if (L1ParalysisPoison.this._target instanceof L1PcInstance) {
                final L1PcInstance player = (L1PcInstance) L1ParalysisPoison.this._target;
                if (player.isDead() == false) {
                    player.sendPackets(new S_Paralysis(1, true)); // 麻痹状态にする
                    L1ParalysisPoison.this._timer = new ParalysisTimer();
                    GeneralThreadPool.get().execute(
                            L1ParalysisPoison.this._timer); // 麻痹计时开始
                    if (this.isInterrupted()) {// XXX
                        L1ParalysisPoison.this._timer.interrupt();
                    }
                }
            }
        }
    }

    private class ParalysisTimer extends Thread {
        @Override
        public void run() {
            L1ParalysisPoison.this._target
                    .killSkillEffectTimer(STATUS_POISON_PARALYZING);
            L1ParalysisPoison.this._target.setSkillEffect(
                    STATUS_POISON_PARALYZED, 0);
            try {
                Thread.sleep(L1ParalysisPoison.this._time);

            } catch (final InterruptedException e) {
                ModelError.isError(_log, e.getLocalizedMessage(), e);
            }

            L1ParalysisPoison.this._target
                    .killSkillEffectTimer(STATUS_POISON_PARALYZED);
            if (L1ParalysisPoison.this._target instanceof L1PcInstance) {
                final L1PcInstance player = (L1PcInstance) L1ParalysisPoison.this._target;
                if (!player.isDead()) {
                    player.sendPackets(new S_Paralysis(1, false)); // 麻痹状态を解除する
                    L1ParalysisPoison.this.cure(); // 解毒处理
                }
            }
        }
    }

    private L1ParalysisPoison(final L1Character cha, final int delay,
            final int time) {
        this._target = cha;
        this._delay = delay;
        this._time = time;

        this.doInfection();
    }

    public static boolean doInfection(final L1Character cha, final int delay,
            final int time) {
        if (!L1Poison.isValidTarget(cha)) {
            return false;
        }

        cha.setPoison(new L1ParalysisPoison(cha, delay, time));
        return true;
    }

    private void doInfection() {
        sendMessageIfPlayer(this._target, 212);
        this._target.setPoisonEffect(1);

        if (this._target instanceof L1PcInstance) {
            this._timer = new ParalysisPoisonTimer();
            GeneralThreadPool.get().execute(this._timer);
        }
    }

    @Override
    public int getEffectId() {
        return this._effectId;
    }

    @Override
    public void cure() {
        this._target.setPoisonEffect(0);
        this._target.setPoison(null);

        if (this._timer != null) {// XXX
            this._timer.interrupt(); // 麻痹毒タイマー解除
        }
    }
}
