package com.lineage.server.timecontroller.npc;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.ActionCodes;
import com.lineage.server.model.L1DragonSlayer;
import com.lineage.server.model.Instance.L1IllusoryInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldNpc;

/**
 * NPC存在时间时间轴
 * 
 * @author dexc
 * 
 */
public class NpcDeleteTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(NpcDeleteTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 1000;// 1秒
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            final Collection<L1NpcInstance> allNpc = WorldNpc.get().all();
            // 不包含元素
            if (allNpc.isEmpty()) {
                return;
            }

            for (final Iterator<L1NpcInstance> iter = allNpc.iterator(); iter
                    .hasNext();) {
                final L1NpcInstance npc = iter.next();
                // 不具有存在时间
                if (!npc.is_spawnTime()) {
                    continue;
                }

                int time = npc.get_spawnTime();
                time -= 1;
                if (time > 0) {
                    // 更新
                    npc.set_spawnTime(time);

                } else {
                    if ((npc.getNpcId() == 81273)
                            || (npc.getNpcId() == 81274)
                            || (npc.getNpcId() == 81275)
                            || (npc.getNpcId() == 81276)
                            || (npc.getNpcId() == 81277)) {
                        if (npc.getNpcId() == 81277) { // 隐匿的巨龙谷入口关闭
                            L1DragonSlayer.getInstance().setHiddenDragonValleyStstus(0);
                        }
                        // 结束屠龙副本
                        L1DragonSlayer.getInstance().setPortalPack(
                                npc.getPortalNumber(), null);
                        L1DragonSlayer.getInstance().endDragonPortal(
                                npc.getPortalNumber());
                        // 门扉消失动作
                        npc.setStatus(ActionCodes.ACTION_Die);
                        npc.broadcastPacketAll(new S_DoActionGFX(npc.getId(),
                                ActionCodes.ACTION_Die));
                    }
                    remove(npc);
                }
                Thread.sleep(50);
            }

        } catch (final Exception e) {
            _log.error("NPC存在时间时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final NpcDeleteTimer npcDeleteTimer = new NpcDeleteTimer();
            npcDeleteTimer.start();
        }
    }

    /**
     * 删除NPC(解除召唤)
     * 
     * @param tgnpc
     */
    private static void remove(final L1NpcInstance tgnpc) {
        try {
            boolean isRemove = false;

            // 对象是怪物
            if (tgnpc instanceof L1MonsterInstance) {
                if (tgnpc.getNpcId() == 80034) {// 史巴托(史巴托的复仇)
                    tgnpc.outParty(tgnpc);
                }
                isRemove = true;
            }

            // 对象是分身
            if (tgnpc instanceof L1IllusoryInstance) {
                isRemove = true;
            }

            if (isRemove) {
                tgnpc.setCurrentHpDirect(0);
                tgnpc.setDead(true);
                // 解除旧座标障碍宣告
                tgnpc.getMap().setPassable(tgnpc.getLocation(), true);

                tgnpc.setExp(0);
                tgnpc.setKarma(0);
                tgnpc.allTargetClear();
            }

            tgnpc.deleteMe();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
