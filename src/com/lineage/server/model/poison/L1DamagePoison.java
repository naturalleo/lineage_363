package com.lineage.server.model.poison;

import static com.lineage.server.model.skill.L1SkillId.STATUS_POISON;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.ModelError;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.thread.GeneralThreadPool;

/**
 * 一般型中毒
 * 
 * @author dexc
 * 
 */
public class L1DamagePoison extends L1Poison {

    private static final Log _log = LogFactory.getLog(L1DamagePoison.class);

    private Thread _timer;

    private final L1Character _attacker;

    private final L1Character _target;

    private final int _damageSpan;

    private final int _damage;

    private L1DamagePoison(final L1Character attacker, final L1Character cha,
            final int damageSpan, final int damage) {
        _attacker = attacker;
        _target = cha;
        _damageSpan = damageSpan;
        _damage = damage;

        doInfection();
    }

    private class NormalPoisonTimer extends Thread {
        @Override
        public void run() {
            try {
                while (_target.hasSkillEffect(STATUS_POISON)) {
                    Thread.sleep(_damageSpan);

                    if (!_target.hasSkillEffect(STATUS_POISON)) {
                        break;
                    }

                    if (_target instanceof L1PcInstance) {
                        final L1PcInstance player = (L1PcInstance) _target;
                        player.receiveDamage(_attacker, _damage, false, true);
                        if (player.isDead()) { // 死亡したら解毒处理
                            break;
                        }

                    } else if (_target instanceof L1MonsterInstance) {
                        final L1MonsterInstance mob = (L1MonsterInstance) _target;
                        mob.receiveDamage(_attacker, _damage);
                        if (mob.isDead()) { // 死亡しても解毒しない
                            break;
                        }
                    }
                }

            } catch (final InterruptedException e) {
                ModelError.isError(_log, e.getLocalizedMessage(), e);
            }
            cure(); // 解毒处理
        }
    }

    boolean isDamageTarget(final L1Character cha) {
        return (cha instanceof L1PcInstance)
                || (cha instanceof L1MonsterInstance);
    }

    private void doInfection() {
        _target.setSkillEffect(STATUS_POISON, 30000);
        _target.setPoisonEffect(1);

        if (isDamageTarget(_target)) {
            _timer = new NormalPoisonTimer();
            GeneralThreadPool.get().execute(_timer); // 通常毒计时开始
        }
    }

    public static boolean doInfection(final L1Character attacker,
            final L1Character cha, final int damageSpan, final int damage) {
        if (!isValidTarget(cha)) {
            return false;
        }

        cha.setPoison(new L1DamagePoison(attacker, cha, damageSpan, damage));
        return true;
    }

    @Override
    public int getEffectId() {
        return 1;
    }

    @Override
    public void cure() {
        _target.setPoisonEffect(0);
        _target.killSkillEffectTimer(STATUS_POISON);
        _target.setPoison(null);

        if (_timer != null) {// XXX
            _timer.interrupt(); // 毒タイマー解除
        }
    }
}
