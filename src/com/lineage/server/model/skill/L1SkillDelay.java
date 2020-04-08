package com.lineage.server.model.skill;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Character;
import com.lineage.server.thread.GeneralThreadPool;

/**
 * 技能延迟使用
 * 
 * @author dexc
 * 
 */
public class L1SkillDelay {

    private static final Log _log = LogFactory.getLog(L1SkillDelay.class);

    /**
     * 技能延迟使用
     */
    private L1SkillDelay() {
    }

    static class SkillDelayTimer implements Runnable {

        private L1Character _cha;

        public SkillDelayTimer(final L1Character cha) {
            _cha = cha;
        }

        @Override
        public void run() {
            stopDelayTimer();
        }

        public void stopDelayTimer() {
            _cha.setSkillDelay(false);
        }
    }

    /**
     * 设置技能延迟使用
     * 
     * @param cha
     * @param time
     */
    public static void onSkillUse(final L1Character cha, final int time) {
        try {
            cha.setSkillDelay(true);
            GeneralThreadPool.get().schedule(new SkillDelayTimer(cha), time);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
