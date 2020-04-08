package com.lineage.server.model.poison;

import static com.lineage.server.model.skill.L1SkillId.STATUS_POISON_SILENCE;

import com.lineage.server.model.L1Character;

/**
 * 沈默型中毒
 * 
 * @author dexc
 * 
 */
public class L1SilencePoison extends L1Poison {

    private final L1Character _target;

    public static boolean doInfection(final L1Character cha) {
        if (!L1Poison.isValidTarget(cha)) {
            return false;
        }

        cha.setPoison(new L1SilencePoison(cha));
        return true;
    }

    private L1SilencePoison(final L1Character cha) {
        this._target = cha;

        this.doInfection();
    }

    private void doInfection() {
        this._target.setPoisonEffect(1);
        sendMessageIfPlayer(this._target, 310);

        this._target.setSkillEffect(STATUS_POISON_SILENCE, 0);
    }

    @Override
    public int getEffectId() {
        return 1;
    }

    @Override
    public void cure() {
        this._target.setPoisonEffect(0);
        sendMessageIfPlayer(this._target, 311);

        this._target.killSkillEffectTimer(STATUS_POISON_SILENCE);
        this._target.setPoison(null);
    }
}
