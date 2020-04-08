package com.lineage.server.model.skill;

import com.lineage.server.model.L1Character;

public class L1SkillTimerCreator {

    public static L1SkillTimer create(final L1Character cha, final int skillId,
            final int timeMillis) {
        return new L1SkillTimerTimerImpl(cha, skillId, timeMillis);
    }
}
