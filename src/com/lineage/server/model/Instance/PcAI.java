package com.lineage.server.model.Instance;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.SprTable;
import com.lineage.server.thread.NpcAiThreadPool;

public class PcAI implements Runnable {
	private static final Log _log = LogFactory.getLog(PcAI.class);
	private static Random _random = new Random();
	private final L1PcInstance _pc;
	
    public PcAI(final L1PcInstance pc) {
        _pc = pc;
    }
    
    public void startAI() {
        NpcAiThreadPool.get().execute(this);
    }
    
    @Override
    public void run() {
        try {
        	//System.out.println("===AI执行===");
        	//_npc.setAiRunning(true);           
            while (_pc.getMaxHp() > 0) {
                while (_pc.isSleeped() || _pc.isParalyzedX() || _pc.isParalyzed()) {
                    Thread.sleep(200);
                }
                //System.out.println("AI启动2222");
                // AI的处理
                if (AIProcess()) {
                	_pc.setAiRunning(false);
                	_pc.allTargetClear();
                    break;
                }// */

                try {
                    // 移动或攻击速度延迟
                	if (_pc.Attack_or_walk()) {
                		Thread.sleep(getRightInterval(1) + 20);
                	} else {
                        Thread.sleep(getRightInterval(2));
                	}

                } catch (final Exception e) {
                    break;
                }
            }

            do {
                try {
                    Thread.sleep(500);

                } catch (final Exception e) {
                    break;
                }
            } while (_pc.isDead());

            _pc.allTargetClear();
            
            _pc.setActived(false);
            Thread.sleep(10);

        } catch (final Exception e) {
            _log.error("pcAI发生例外状况: " + this._pc.getName(), e);
        }
    }
    
    /**
     * AI的处理
     * 
     * @return true:AI终了 false:AI续行
     */
    private boolean AIProcess() {
        try {
            if (_pc.isDead()) {
                return true;
            }

            if (_pc.getOnlineStatus() == 0) {
                return true;
            }

            if (_pc.getCurrentHp() <= 0) {
                return true;
            }
            if (!_pc.isActived()) {
            	return true;
            }

            //_pc.setSleepTime(300);
            // 现有目标有效性检查
            _pc.checkTarget();

            boolean searchTarget = true;
            if (_pc.is_now_target() != null) {
                searchTarget = false;
            }
            
            if (searchTarget) {
                //进行目标搜索
//            	System.out.println("AI启动3333");
                _pc.searchTarget();
            }
            
            if (_pc.is_now_target() == null) {
            	if (!_pc.isPathfinding()) {
            		_pc.setrandomMoveDirection(_random.nextInt(8));
            	}
                _pc.noTarget();
            	Thread.sleep(50);
                return false;
            } else {
            	_pc.onTarget();
            	if (_pc.isPathfinding()) {
            		_pc.setPathfinding(false);
            	}
            }

            Thread.sleep(50);

        } catch (final Exception e) {
            _log.error("pcAI发生例外状况: " + this._pc.getName(), e);
        }
        return false; // NPC AI 继续执行
    }
    
    /**
     * 正常的速度
     * 
     * @param type
     *            检测类型
     * @return 正常应该接收的速度(MS)
     */
    private int getRightInterval(final int type) {
        int interval = 0;

        switch (type) {
            case 1:
                interval = SprTable.get().getAttackSpeed(
                        this._pc.getTempCharGfx(),
                        this._pc.getCurrentWeapon() + 1);
                interval *= 1.05;
                break;

            case 2:
                interval = SprTable.get().getMoveSpeed(
                        this._pc.getTempCharGfx(), this._pc.getCurrentWeapon());
                break;

            default:
                return 0;
        }
        return intervalR(type, interval);
    }

    private int intervalR(final int type, int interval) {
        try {
        	if(this._pc.isskillHardDelay()) { //添加动作延时防止变档修改加速 hjx1000
        		return interval * 2;
        	}
            if(this._pc.isHardDelay() && type == 2) { //添加动作延时防止变档修改加速 hjx1000
            	return interval *= 1.3;
            }
            if (this._pc.isHaste()) {
                interval *= 0.755;// 0.755
            }

            if (type== 2 && this._pc.isFastMovable()) {
                interval *= 0.755;// 0.665
            }

            if (type == 2 && this._pc.isFastAttackable()) {
                interval *= 0.665;// 0.775
            }

            if (this._pc.isBrave()) {
                interval *= 0.755;// 0.755
            }

            if (this._pc.isBraveX()) {
                interval *= 0.755;// 0.755
            }

            if (this._pc.isElfBrave()) {
                interval *= 0.855;// 0.855
            }

            if (type == 1 && this._pc.isElfBrave()) {
                interval *= 0.9;// 0.9
            }
            //System.out.println(interval + _pc.getName());
            if (interval < 100) {
            	interval = 1000; //修正某些图形挂机时速度超快的问题 hjx1000
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return interval;
    }

}
