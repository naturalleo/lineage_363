package com.lineage.data;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.templates.L1Event;

/**
 * Event(活动设置) 模组相关
 * 
 * @author dexc
 * 
 */
public class EventClass {

    private static final Log _log = LogFactory.getLog(EventClass.class);

    // EVENT 执行类清单
    private static final Map<Integer, EventExecutor> _classList = new HashMap<Integer, EventExecutor>();

    private static EventClass _instance;

    public static EventClass get() {
        if (_instance == null) {
            _instance = new EventClass();
        }
        return _instance;
    }

    /**
     * 加入CLASS清单
     * 
     * @param npcid
     * @param className
     */
    public void addList(final int eventid, final String className) {
        if (className.equals("0")) {
            return;
        }
        try {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("com.lineage.data.event.");
            stringBuilder.append(className);

            final Class<?> cls = Class.forName(stringBuilder.toString());
            final EventExecutor exe = (EventExecutor) cls.getMethod("get")
                    .invoke(null);

            _classList.put(new Integer(eventid), exe);

        } catch (final ClassNotFoundException e) {
            String error = "发生[Event(活动设置)档案]错误, 检查档案是否存在:" + className
                    + " EventId:" + eventid;
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
     * EVENT启动
     * 
     * @param event
     */
    public void startEvent(final L1Event event) {
        try {
            // CLASS执行位置取回
            final EventExecutor exe = _classList.get(new Integer(event
                    .get_eventid()));
            if (exe != null) {
                exe.execute(event);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

}
