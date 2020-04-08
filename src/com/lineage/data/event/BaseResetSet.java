package com.lineage.data.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.templates.L1Event;

/**
 * 回忆蜡烛系统<BR>
 * 
 * # 回忆蜡烛系统 DELETE FROM `server_event` WHERE `id`='53'; INSERT INTO
 * `server_event` VALUES ('53', '回忆蜡烛系统', 'BaseResetSet', '1', '30', '说明:回忆蜡烛系统
 * 使用后HP/MP保留百分比(1/100)');
 * 
 * # 更新回忆蜡烛向导露露 DELETE FROM `npcaction` WHERE `npcid`='71251'; UPDATE `npc` SET
 * `classname`='event.Npc_BaseReset' WHERE `npcid`='71251';# 露露
 * 
 * #新增回忆蜡烛向导露露召换位置 DELETE FROM `server_event_spawn` WHERE `eventid`='53'; DELETE
 * FROM `server_event_spawn` WHERE `id`='40170'; INSERT INTO
 * `server_event_spawn` VALUES (40170, 53, '回忆蜡烛向导露露', 1, 71251, 0, 32610,
 * 32775, 0, 0, 4, 0, 4, 0, 1);
 * 
 * @author dexc
 * 
 */
public class BaseResetSet extends EventExecutor {

    private static final Log _log = LogFactory.getLog(BaseResetSet.class);

    public static int RETAIN = 0;// 1/100

    /**
	 *
	 */
    private BaseResetSet() {
        // TODO Auto-generated constructor stub
    }

    public static EventExecutor get() {
        return new BaseResetSet();
    }

    @Override
    public void execute(final L1Event event) {
        try {
            final String[] set = event.get_eventother().split(",");

            try {
                RETAIN = Integer.parseInt(set[0]);

            } catch (Exception e) {
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
