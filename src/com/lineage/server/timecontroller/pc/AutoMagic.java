package com.lineage.server.timecontroller.pc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.thread.GeneralThreadPool;

/**
 * 自动施法延时用
 * 
 * @author hjx1000
 * 
 */
public class AutoMagic {

    private static final Log _log = LogFactory.getLog(AutoMagic.class);
    final static int time = 3000; //自动施法时间为三秒 

    /**
     * 自动施法延时用
     */
    private AutoMagic() {
    }

    static class AutoMagicTimer implements Runnable {

        private L1PcInstance _pc;
        private int _skillid;

        public AutoMagicTimer(final L1PcInstance pc, final int skillid) {
            _pc = pc;
            _skillid = skillid;
        }

        @Override
        public void run() {
            stopDelayTimer();
        }

        public void stopDelayTimer() {
        	if (_pc == null) {
        		return;
        	}
            if (_pc.getOnlineStatus() == 0) {
                return;
            }
        	if (_pc.isDead()) {
        		return;
        	}

        	if (_skillid == 46) {
        		if (_pc.isskill46()) {
            		automagic(_pc, 46);
        		}
        	}
        	if (_skillid == 132) {
        		if (_pc.isskill132()) {
            		automagic(_pc, 132);
        		}
        	}
        	if (_skillid == 187) {
        		if (_pc.isskill187()) {
            		automagic(_pc, 187);
        		}
        	}
            //_pc.setHardDelay(false);
//            System.out.println("HardDelay=" + _pc.isHardDelay());
        	if (_pc.getNowTarget() == null) {
        		return;
        	}
        	if (_skillid == 46) {
        		if (!_pc.isAttackPosition(_pc.getNowTarget().getX(),
        				_pc.getNowTarget().getY(), 3)) {
        			return;
        		}
        	}
        	if (_skillid == 132) {
        		if (!_pc.isAttackPosition(_pc.getNowTarget().getX(),
        				_pc.getNowTarget().getY(), 10)) {
        			return;
        		}
        	}
        	if (_skillid == 187) {
        		if (!_pc.isAttackPosition(_pc.getNowTarget().getX(),
        				_pc.getNowTarget().getY(), 2)) {
        			return;
        		}
        	}
            if (_pc.isTeleport()) { // 传送中
                return;
            }
            // 技能延迟状态
            if (_pc.isSkillDelay()) {
            	return;
            }
        	if (_pc.getCurrentMp() < 100 && _skillid != 187) {
        		return;
        	}

        	if (_pc.isParalyzed()) {
        		return;
        	}
//        	if (!_pc.isSkillMastery(_skillid)) { //判断是否有该技能
//        		return;
//        	}
        	if (!_pc.isActived()) {
        		return;
        	}

            final L1SkillUse skilluse = new L1SkillUse();
            skilluse.handleCommands(_pc, _skillid, _pc.getNowTarget().getId(), _pc.getNowTarget().getX(),
            		_pc.getNowTarget().getY(),
                    // message,
                    0, L1SkillUse.TYPE_NORMAL);
        }
    }

    /**
     * 自动施法延时用
     * 
     * @param pc
     * @param time
     */
    public static void automagic(final L1PcInstance pc, final int skillid) {
        try {
        	//pc.setHardDelay(true);
            GeneralThreadPool.get().schedule(new AutoMagicTimer(pc, skillid), time);
//            System.out.println("HardDelay=" + pc.isHardDelay());

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
