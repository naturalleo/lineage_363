package com.lineage.server.model.Instance;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Inventory;
import com.lineage.server.thread.NpcAiThreadPool;
import com.lineage.server.world.World;

/**
 * NPC AI Runnable
 * 
 * @author daien
 * 
 */
public class NpcAI implements Runnable {

    private static final Log _log = LogFactory.getLog(NpcAI.class);

    private final L1NpcInstance _npc;

    /**
     * NPC AI Runnable
     * 
     * @param npc
     */
    public NpcAI(final L1NpcInstance npc) {
        _npc = npc;
    }

    public void startAI() {
        NpcAiThreadPool.get().execute(this);
    }

    @Override
    public void run() {
        try {
        	//System.out.println("===AI执行===");
        	//_npc.setAiRunning(true);           
            while (_npc.getMaxHp() > 0) {
                while (_npc.isParalyzed() || _npc.isSleeped()) {
                    Thread.sleep(200);
                }

                // AI的处理
                if (AIProcess()) {
                    break;
                }// */

                try {
                    // 移动速度延迟
                    Thread.sleep(_npc.getSleepTime());

                } catch (final Exception e) {
                    break;
                }
            }

            _npc.mobSkill().resetAllSkillUseCount();

            do {
                try {
                    Thread.sleep(_npc.getSleepTime());

                } catch (final Exception e) {
                    break;
                }
            } while (_npc.isDeathProcessing());

            _npc.allTargetClear();
            _npc.setAiRunning(false);
            Thread.sleep(10);

        } catch (final Exception e) {
            _log.error("NpcAI发生例外状况: " + this._npc.getName(), e);
        }
    }

    /**
     * AI的处理
     * 
     * @return true:AI终了 false:AI续行
     */
    private boolean AIProcess() {
        try {
            if (_npc.isDead()) {
                return true;
            }

            if (_npc.destroyed()) {
                return true;
            }

            if (_npc.getCurrentHp() <= 0) {
                return true;
            }

            if (_npc.getHiddenStatus() != L1NpcInstance.HIDDEN_STATUS_NONE) {
                return true;
            }
            

            _npc.setSleepTime(300);
            // 现有目标有效性检查
            _npc.checkTarget();

            boolean searchTarget = true;
            if (_npc.is_now_target() != null) {
                searchTarget = false;
            }

            if (_npc.getMaster() != null && searchTarget) {
                searchTarget = false;
            }

            if (searchTarget) {
                // 没有目标/主人 的状态进行目标搜索
                _npc.searchTarget();
            }

            // 物品使用判断
            _npc.onItemUse();

            if (_npc.is_now_target() == null) {
                // 检查可捡取物品
                _npc.checkTargetItem();
                if (_npc.isPickupItem()) {
                    if (_npc.is_now_targetItem() == null) {
                        // 可捡取物品探索
                        _npc.searchTargetItem();
                    }
                }

                if (_npc.is_now_targetItem() == null) {
                    final boolean noTarget = _npc.noTarget();

                    if (noTarget) {
                        return true;
                    }

                } else {
                    // onTargetItem();
                    final L1Inventory groundInventory = World.get()
                            .getInventory(_npc.is_now_targetItem().getX(),
                                    _npc.is_now_targetItem().getY(),
                                    _npc.is_now_targetItem().getMapId());

                    if (groundInventory.checkItem(_npc.is_now_targetItem()
                            .getItemId())) {
                        _npc.onTargetItem();

                    } else {
                        _npc._targetItemList.remove(_npc.is_now_targetItem());
                        _npc.set_now_targetItem(null);
                        _npc.setSleepTime(1000);
                        return false;
                    }
                }
            } else {
                // NPC未躲藏的状态
                if (_npc.getHiddenStatus() == L1NpcInstance.HIDDEN_STATUS_NONE) {
                    _npc.onTarget();

                } else {
                    return true;
                }
            }
            Thread.sleep(10);

        } catch (final Exception e) {
            _log.error("NpcAI发生例外状况: " + this._npc.getName(), e);
        }
        return false; // NPC AI 继续执行
    }
}
