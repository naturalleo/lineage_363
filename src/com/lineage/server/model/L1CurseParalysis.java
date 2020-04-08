package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.STATUS_CURSE_PARALYZED;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CURSE_PARALYZING;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.thread.GeneralThreadPool;

/**
 * 诅咒型麻痹
 * 
 * @author dexc
 * 
 */
public class L1CurseParalysis extends L1Paralysis {

    private static final Log _log = LogFactory.getLog(L1CurseParalysis.class);

    private final L1Character _target;

    private final int _delay;

    private final int _time;

    private Thread _timer;

    private class ParalysisDelayTimer extends Thread {
        @Override
        public void run() {
            L1CurseParalysis.this._target.setSkillEffect(
                    STATUS_CURSE_PARALYZING, 0);

            try {
                Thread.sleep(L1CurseParalysis.this._delay); // 麻痹するまでの犹予时间を待つ。
            } catch (final InterruptedException e) {
                L1CurseParalysis.this._target
                        .killSkillEffectTimer(STATUS_CURSE_PARALYZING);

                ModelError.isError(_log, e.getLocalizedMessage(), e);
                return;
            }

            if (L1CurseParalysis.this._target instanceof L1PcInstance) {
                final L1PcInstance player = (L1PcInstance) L1CurseParalysis.this._target;
                if (!player.isDead()) {
                    player.sendPackets(new S_Paralysis(1, true)); // 麻痹状态にする
                }
            }
            L1CurseParalysis.this._target.setParalyzed(true);
            L1CurseParalysis.this._timer = new ParalysisTimer();
            GeneralThreadPool.get().execute(L1CurseParalysis.this._timer); // 麻痹计时开始
            if (this.isInterrupted()) {// XXX
                L1CurseParalysis.this._timer.interrupt();
            }
        }
    }

    private class ParalysisTimer extends Thread {
        @Override
        public void run() {
            L1CurseParalysis.this._target
                    .killSkillEffectTimer(STATUS_CURSE_PARALYZING);
            L1CurseParalysis.this._target.setSkillEffect(
                    STATUS_CURSE_PARALYZED, 0);

            try {
                Thread.sleep(L1CurseParalysis.this._time);

            } catch (final InterruptedException e) {
                ModelError.isError(_log, e.getLocalizedMessage(), e);
            }

            L1CurseParalysis.this._target
                    .killSkillEffectTimer(STATUS_CURSE_PARALYZED);
            if (L1CurseParalysis.this._target instanceof L1PcInstance) {
                final L1PcInstance player = (L1PcInstance) L1CurseParalysis.this._target;
                if (!player.isDead()) {
                    player.sendPackets(new S_Paralysis(1, false)); // 麻痹状态を解除する
                }
            }
            L1CurseParalysis.this._target.setParalyzed(false);
            L1CurseParalysis.this.cure(); // 解咒处理
        }
    }

    /**
     * 魔法效果:麻痹
     * 
     * @param cha
     *            对象
     * @param delay
     *            延迟时间(毫秒)
     * @param time
     *            麻痹时间(毫秒)
     */
    private L1CurseParalysis(final L1Character cha, final int delay,
            final int time, final int mode) {
        this._target = cha;
        this._delay = delay;
        this._time = time;

        this.curse(mode);
    }

    private void curse(final int mode) {
        if (this._target instanceof L1PcInstance) {
            final L1PcInstance player = (L1PcInstance) this._target;
            switch (mode) {
                case 1:
                    // 212 \f1你的身体渐渐麻痹。
                    player.sendPackets(new S_ServerMessage(212));
                    break;

                case 2:
                    // 291 \f1你的身体正在迅速痲痹。
                    player.sendPackets(new S_ServerMessage(291));
                    break;
            }
        }

        this._target.setPoisonEffect(2);

        this._timer = new ParalysisDelayTimer();
        GeneralThreadPool.get().execute(this._timer);
    }

    /**
     * 魔法效果:麻痹
     * 
     * @param cha
     *            对象
     * @param delay
     *            延迟时间(毫秒)
     * @param time
     *            麻痹时间(毫秒)
     * @param mode
     *            1:你的身体渐渐麻痹。 2:你的身体正在迅速痲痹。
     * @return
     */
    public static boolean curse(final L1Character cha, final int delay,
            final int time, final int mode) {
        if (!((cha instanceof L1PcInstance) || (cha instanceof L1MonsterInstance))) {
            return false;
        }
        if (cha.hasSkillEffect(STATUS_CURSE_PARALYZING)
                || cha.hasSkillEffect(STATUS_CURSE_PARALYZED)) {
            return false; // 既に麻痹している
        }

        cha.setParalaysis(new L1CurseParalysis(cha, delay, time, mode));
        return true;
    }

    @Override
    public int getEffectId() {
        return 2;
    }

    @Override
    public void cure() {
        this._target.setPoisonEffect(0);
        this._target.setParalaysis(null);

        if (this._timer != null) {// XXX
            this._timer.interrupt();
        }
    }
}
