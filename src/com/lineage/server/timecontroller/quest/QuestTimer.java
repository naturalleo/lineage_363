package com.lineage.server.timecontroller.quest;

import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.serverpackets.S_HelpMessage;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldQuest;

/**
 * 副本任务可执行时间
 * 
 * @author dexc
 * 
 */
public class QuestTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(QuestTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 1000;// 1秒
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            // 全部副本任务
            final Collection<L1QuestUser> allQuest = WorldQuest.get().all();
            // 不包含元素
            if (allQuest.isEmpty()) {
                return;
            }

            for (L1QuestUser quest : allQuest) {
                if (quest.get_time() <= -1) {
                    continue;
                }
                setQuest(quest);
                Thread.sleep(50);
            }

        } catch (final Exception e) {
            _log.error("副本任务可执行时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final QuestTimer questTimer = new QuestTimer();
            questTimer.start();
        }
    }

    private static void setQuest(L1QuestUser quest) {
        try {
            // 任务时间
            switch (quest.get_time()) {
                case 3600:// 60分钟
                case 1800:// 30分钟
                case 900:// 15分钟
                case 600:// 10分钟
                case 300:// 5分钟
                case 240:// 4分钟
                case 180:// 3分钟
                case 120:// 2分钟
                case 60:// 1分钟
                    // 14:副本任务完成时间限制-剩余：%s分
                    quest.sendPackets(new S_HelpMessage("\\fV副本任务-剩余时间："
                            + (quest.get_time() / 60)));
                    break;

                case 30:// 30秒
                case 15:// 15秒
                case 10:// 10秒
                case 5:// 5秒
                case 4:// 4秒
                case 3:// 3秒
                case 2:// 2秒
                case 1:// 1秒
                       // 15:副本任务完成时间限制-剩余：%s秒
                    quest.sendPackets(new S_HelpMessage("\\fV副本任务-剩余时间："
                            + quest.get_time()));
                    break;
            }

            // 时间倒数设置
            quest.set_time(quest.get_time() - 1);

            // 时间已为0
            if (quest.get_time() == 0) {
                // 3112:\f1[系统讯息] 超过任务可执行时间！！任务结束！
                quest.sendPackets(new S_ServerMessage(3112));
                // 时间设置-1
                quest.set_time(-1);
                // 结束任务
                quest.endQuest();
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
