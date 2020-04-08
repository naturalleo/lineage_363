package com.lineage.server.timecontroller.quest;

import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.quest.ADLv80_2;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldQuest;

/**
 * 水龙副本 伤害计能施放
 * 
 * @author dexc
 * 
 */
public class AD80_2_Timer extends TimerTask {

    private static final Log _log = LogFactory.getLog(AD80_2_Timer.class);

    private static Random _random = new Random();

    private ScheduledFuture<?> _timer;

    private int _qid = -1;

    public void start() {
        this._qid = ADLv80_2.QUEST.get_id();
        final int timeMillis = 5000;
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
                    if (!tgpc.isDead()) {
                        if (!checkLoc(tgpc)) {
                            continue;
                        }
                        if (_random.nextBoolean()) {
                            int skillid = -1;
                            int gfxid = -1;
                            switch (_random.nextInt(3)) {
                                case 0:
                                    skillid = L1SkillId.ADLV80_2_1;
                                    gfxid = 7781;
                                    break;

                                case 1:
                                    skillid = L1SkillId.ADLV80_2_2;
                                    gfxid = 7782;
                                    break;

                                case 2:
                                    skillid = L1SkillId.ADLV80_2_3;
                                    gfxid = 7780;
                                    break;
                            }

                            if (skillid != -1) {
                                if (!tgpc.hasSkillEffect(skillid)) {
                                    tgpc.sendPacketsX8(new S_SkillSound(tgpc
                                            .getId(), gfxid));
                                    tgpc.setSkillEffect(skillid, 12 * 1000);
                                }
                            }
                            Thread.sleep(50);
                        }
                    }
                }
            }
            questList.clear();

        } catch (final Exception e) {
            _log.error("水龙副本 伤害计能施放 时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final AD80_2_Timer ad80_2Timer = new AD80_2_Timer();
            ad80_2Timer.start();
        }
    }

    private static boolean checkLoc(L1PcInstance tgpc) {
        if (tgpc.hasSkillEffect(L1SkillId.ADLV80_2_1)) {
            return false;
        }
        if (tgpc.hasSkillEffect(L1SkillId.ADLV80_2_2)) {
            return false;
        }
        if (tgpc.hasSkillEffect(L1SkillId.ADLV80_2_3)) {
            return false;
        }
        if ((tgpc.getX() > 32942 && tgpc.getX() < 32980)
                && (tgpc.getY() > 32810 && tgpc.getY() < 32871)) {
            return true;
        }
        return false;
    }
}
