package com.lineage.server.timecontroller.npc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.server.datatables.NpcChatTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.templates.L1NpcChat;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldNpc;

/**
 * NPC对话时间轴
 * 
 * @author dexc
 * 
 */
public class NpcChatTimer extends TimerTask {

    private static final Log _log = LogFactory.getLog(NpcChatTimer.class);

    private ScheduledFuture<?> _timer;

    public void start() {
        final int timeMillis = 60 * 1000;
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            final Collection<L1NpcChat> allChat = NpcChatTable.get().all();
            // 不包含元素
            if (allChat.isEmpty()) {
                return;
            }

            for (final Iterator<L1NpcChat> iter = allChat.iterator(); iter
                    .hasNext();) {
                final L1NpcChat npcChat = iter.next();
                // 检查是否对话
                if (this.isChatTime(npcChat.getGameTime())) {
                    final int npcId = npcChat.getNpcId();
                    for (final L1NpcInstance npc : WorldNpc.get().all()) {
                        if (npc.getNpcTemplate().get_npcId() == npcId) {
                            npc.startChat(L1NpcInstance.CHAT_TIMING_GAME_TIME);
                        }
                    }
                }
                Thread.sleep(50);
            }

        } catch (final Exception e) {
            _log.error("NPC对话时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final NpcChatTimer npcChatTimer = new NpcChatTimer();
            npcChatTimer.start();
        }
    }

    private boolean isChatTime(final int chatTime) {
        final SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        final Calendar realTime = getRealTime();
        final int nowTime = Integer.valueOf(sdf.format(realTime.getTime()));
        return (nowTime == chatTime);
    }

    private static Calendar getRealTime() {
        final TimeZone _tz = TimeZone.getTimeZone(Config.TIME_ZONE);
        final Calendar cal = Calendar.getInstance(_tz);
        return cal;
    }
}
