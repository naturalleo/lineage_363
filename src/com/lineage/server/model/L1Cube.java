package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static com.lineage.server.model.skill.L1SkillId.EARTH_BIND;
import static com.lineage.server.model.skill.L1SkillId.FREEZING_BLIZZARD;
import static com.lineage.server.model.skill.L1SkillId.FREEZING_BREATH;
import static com.lineage.server.model.skill.L1SkillId.ICE_LANCE;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CUBE_BALANCE;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CUBE_IGNITION_TO_ENEMY;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CUBE_QUAKE_TO_ENEMY;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CUBE_SHOCK_TO_ENEMY;
import static com.lineage.server.model.skill.L1SkillId.STATUS_FREEZE;
import static com.lineage.server.model.skill.L1SkillId.STATUS_MR_REDUCTION_BY_CUBE_SHOCK;

import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.ActionCodes;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.timecontroller.pc.HardDelay;

public class L1Cube extends TimerTask {

    private static final Log _log = LogFactory.getLog(L1Cube.class);

    private ScheduledFuture<?> _future = null;
    private int _timeCounter = 0;
    private final L1Character _effect;
    private final L1Character _cha;
    private final int _skillId;

    public L1Cube(final L1Character effect, final L1Character cha,
            final int skillId) {
        this._effect = effect;
        this._cha = cha;
        this._skillId = skillId;
    }

    @Override
    public void run() {
        try {
            if (this._cha.isDead()) {
                this.stop();
                return;
            }
            if (!this._cha.hasSkillEffect(this._skillId)) {
                this.stop();
                return;
            }
            this._timeCounter++;
            this.giveEffect();

        } catch (final Throwable e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void begin() {
        // 效果时间が8秒のため、4秒每のスキルの场合处理时间を考虑すると实际には1回しか效果が现れない
        // よって开始时间を0.9秒后に设定しておく
        this._future = GeneralThreadPool.get().scheduleAtFixedRate(this, 900,
                1000);
    }

    public void stop() {
        if (this._future != null) {
            this._future.cancel(false);
        }
    }

    public void giveEffect() {
        switch (this._skillId) {
            case STATUS_CUBE_IGNITION_TO_ENEMY:
                if (this._timeCounter % 4 != 0) {
                    return;
                }
                if (this._cha.hasSkillEffect(STATUS_FREEZE)) {
                    return;
                }
                if (this._cha.hasSkillEffect(ABSOLUTE_BARRIER)) {
                    return;
                }
                if (this._cha.hasSkillEffect(ICE_LANCE)) {
                    return;
                }
                if (this._cha.hasSkillEffect(FREEZING_BLIZZARD)) {
                    return;
                }
                if (this._cha.hasSkillEffect(FREEZING_BREATH)) {
                    return;
                }
                if (this._cha.hasSkillEffect(EARTH_BIND)) {
                    return;
                }

                if (this._cha instanceof L1PcInstance) {
                    final L1PcInstance pc = (L1PcInstance) this._cha;
                    pc.sendPacketsAll(new S_DoActionGFX(pc.getId(),
                            ActionCodes.ACTION_Damage));
                    pc.receiveDamage(this._effect, 10, false, true);
                    if (!pc.isHardDelay()) { //动作延时 hjx1000
                    	HardDelay.onHardUse(pc, 150);
                    }

                } else if (this._cha instanceof L1MonsterInstance) {
                    final L1MonsterInstance mob = (L1MonsterInstance) this._cha;
                    mob.broadcastPacketX10(new S_DoActionGFX(mob.getId(),
                            ActionCodes.ACTION_Damage));
                    mob.receiveDamage(this._effect, 10);
                }
                break;

            case STATUS_CUBE_QUAKE_TO_ENEMY:
                if (this._timeCounter % 4 != 0) {
                    return;
                }
                if (this._cha.hasSkillEffect(STATUS_FREEZE)) {
                    return;
                }
                if (this._cha.hasSkillEffect(ABSOLUTE_BARRIER)) {
                    return;
                }
                if (this._cha.hasSkillEffect(ICE_LANCE)) {
                    return;
                }
                if (this._cha.hasSkillEffect(FREEZING_BLIZZARD)) {
                    return;
                }
                if (this._cha.hasSkillEffect(FREEZING_BREATH)) {
                    return;
                }
                if (this._cha.hasSkillEffect(EARTH_BIND)) {
                    return;
                }

                if (this._cha instanceof L1PcInstance) {
                    final L1PcInstance pc = (L1PcInstance) this._cha;
                    pc.setSkillEffect(STATUS_FREEZE, 1000);
                    pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));

                } else if (this._cha instanceof L1MonsterInstance) {
                    final L1MonsterInstance mob = (L1MonsterInstance) this._cha;
                    mob.setSkillEffect(STATUS_FREEZE, 1000);
                    mob.setParalyzed(true);
                }
                break;

            case STATUS_CUBE_SHOCK_TO_ENEMY:
                // if (_timeCounter % 5 != 0) {
                // return;
                // }
                // _cha.addMr(-10);
                // if (_cha instanceof L1PcInstance) {
                // L1PcInstance pc = (L1PcInstance) _cha;
                // pc.sendPackets(new S_SPMR(pc));
                // }
                this._cha.setSkillEffect(STATUS_MR_REDUCTION_BY_CUBE_SHOCK,
                        4000);
                break;

            case STATUS_CUBE_BALANCE:
                if (this._timeCounter % 4 == 0) {
                    int newMp = this._cha.getCurrentMp() + 5;
                    if (newMp < 0) {
                        newMp = 0;
                    }
                    this._cha.setCurrentMp(newMp);
                }
                if (this._timeCounter % 5 == 0) {
                    if (this._cha instanceof L1PcInstance) {
                        final L1PcInstance pc = (L1PcInstance) this._cha;
                        pc.receiveDamage(this._effect, 25, false, true);

                    } else if (this._cha instanceof L1MonsterInstance) {
                        final L1MonsterInstance mob = (L1MonsterInstance) this._cha;
                        mob.receiveDamage(this._effect, 25);
                    }
                }
                break;
        }
    }
}
