package com.lineage.data.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.datatables.QuestTable;
import com.lineage.server.datatables.QuesttSpawnTable;
import com.lineage.server.templates.L1Event;
import com.lineage.server.timecontroller.quest.QuestTimer;

/**
 * 任务/副本系统
 * 
 * @author daien
 * 
 */
public class QuestSet extends EventExecutor {

    private static final Log _log = LogFactory.getLog(QuestSet.class);

    public static boolean ISQUEST = false;// 任务/副本系统启动

    /**
	 *
	 */
    private QuestSet() {
        // TODO Auto-generated constructor stub
    }

    public static EventExecutor get() {
        return new QuestSet();
    }

    @Override
    public void execute(final L1Event event) {
        try {
            ISQUEST = true;

            // 任务设置启动
            QuestTable.get().load();

            // 任务设置召唤启动
            if (QuestTable.get().size() > 0) {
                QuesttSpawnTable.get().load();
            }

            // 启动时间限制 时间轴
            final QuestTimer questTimer = new QuestTimer();
            questTimer.start();

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
