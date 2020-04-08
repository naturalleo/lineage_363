package com.lineage.server.timecontroller.quest;

import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.quest.DarkElfLv50_1;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.WorldQuest;

/**
 * 寻找黑暗之星 (黑暗妖精50级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class DE50A_Timer extends TimerTask {

    private static final Log _log = LogFactory.getLog(DE50A_Timer.class);

    private static Random _random = new Random();

    private ScheduledFuture<?> _timer;

    private int _qid = -1;

    public void start() {
        this._qid = DarkElfLv50_1.QUEST.get_id();
        final int timeMillis = 1500;
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            // 执行中任务副本
            final ArrayList<L1QuestUser> questList = WorldQuest.get()
                    .getQuests(_qid);
            // 不包含元素
            if (questList.isEmpty()) {
                return;
            }

            for (Object object : questList.toArray()) {
                final L1QuestUser quest = (L1QuestUser) object;
                // 召唤怪物
                for (L1PcInstance tgpc : quest.pcList()) {
                    if (!tgpc.isDead() && tgpc.getMapId() == 306) {// 45582-45587//已经修正，，地图ID=306 hjx1000
                        if (_random.nextInt(100) < 10) {
                            quest.addNpc(spawn(tgpc));
                            Thread.sleep(50);
                        }
                    }
                }
            }
            questList.clear();

        } catch (final Exception e) {
            _log.error("寻找黑暗之星 (黑暗妖精50级以上官方任务)A时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final DE50A_Timer de50ATimer = new DE50A_Timer();
            de50ATimer.start();
        }
    }

    private static L1MonsterInstance spawn(L1PcInstance tgpc) {
        try {
            final L1Location loc = tgpc.getLocation().randomLocation(4, false);
            // 登场效果
            tgpc.sendPackets(new S_EffectLocation(loc, 3992));
            L1MonsterInstance mob = null;
            if (_random.nextBoolean()) {
                mob = L1SpawnUtil.spawnX(45582, loc, tgpc.get_showId());

            } else {
                mob = L1SpawnUtil.spawnX(45587, loc, tgpc.get_showId());
            }
            mob.setExp(1);
            return mob;

        } catch (final Exception e) {

        }
        return null;
    }
}
