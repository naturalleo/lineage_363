package com.lineage.data;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Quest;

/**
 * Quest(任务) 模组相关
 * 
 * @author dexc
 * 
 */
public class QuestClass {

    private static final Log _log = LogFactory.getLog(QuestClass.class);

    // Quest 执行类清单<QuestId, 执行类位置>
    private static final Map<Integer, QuestExecutor> _classList = new HashMap<Integer, QuestExecutor>();

    private static QuestClass _instance;

    public static QuestClass get() {
        if (_instance == null) {
            _instance = new QuestClass();
        }
        return _instance;
    }

    /**
     * 加入CLASS清单
     * 
     * @param npcid
     * @param className
     */
    public void addList(final int questid, final String className) {
        if (className.equals("0")) {
            return;
        }
        try {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("com.lineage.data.quest.");
            stringBuilder.append(className);

            final Class<?> cls = Class.forName(stringBuilder.toString());
            final QuestExecutor exe = (QuestExecutor) cls.getMethod("get")
                    .invoke(null);

            _classList.put(new Integer(questid), exe);

        } catch (final ClassNotFoundException e) {
            String error = "发生[Quest(任务)档案]错误, 检查档案是否存在:" + className
                    + " QuestId:" + questid;
            _log.error(error);
            DataError.isError(_log, error, e);

        } catch (final IllegalArgumentException e) {
            _log.error(e.getLocalizedMessage(), e);

        } catch (final IllegalAccessException e) {
            _log.error(e.getLocalizedMessage(), e);

        } catch (final InvocationTargetException e) {
            _log.error(e.getLocalizedMessage(), e);

        } catch (final SecurityException e) {
            _log.error(e.getLocalizedMessage(), e);

        } catch (final NoSuchMethodException e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * Quest(任务) 设置启用执行
     * 
     * @param event
     */
    public void execute(final L1Quest quest) {
        try {
            // CLASS执行位置取回
            final QuestExecutor exe = _classList
                    .get(new Integer(quest.get_id()));
            if (exe != null) {
                exe.execute(quest);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * Quest(任务) 设置执行<BR>
     * 设置任务开始执行<BR>
     * 相关NPC可与执行者进行对话<BR>
     * 
     * @param pc
     * @param questid
     */
    public void startQuest(final L1PcInstance pc, final int questid) {
        try {
            // CLASS执行位置取回
            final QuestExecutor exe = _classList.get(new Integer(questid));
            if (exe != null) {
                exe.startQuest(pc);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * Quest(任务) 设置结束<BR>
     * 假设该任务可以重复执行<BR>
     * 在此设置任务状态移除<BR>
     * 
     * @param pc
     * @param questid
     */
    public void endQuest(final L1PcInstance pc, final int questid) {
        try {
            // CLASS执行位置取回
            final QuestExecutor exe = _classList.get(new Integer(questid));
            if (exe != null) {
                exe.endQuest(pc);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 展示任务说明<BR>
     * 
     * @param pc
     * @param questid
     */
    public void showQuest(final L1PcInstance pc, final int questid) {
        try {
            // CLASS执行位置取回
            final QuestExecutor exe = _classList.get(new Integer(questid));
            if (exe != null) {
                exe.showQuest(pc);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 任务终止
     * 
     * @param pc
     * @param questid
     */
    public void stopQuest(L1PcInstance pc, int questid) {
        try {
            // CLASS执行位置取回
            final QuestExecutor exe = _classList.get(new Integer(questid));
            if (exe != null) {
                exe.stopQuest(pc);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
