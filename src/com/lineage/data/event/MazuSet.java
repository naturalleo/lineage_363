package com.lineage.data.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.templates.L1Event;

/**
 * 天上圣母统<BR>
 * 
 * #天上圣母系统 DELETE FROM `server_event` WHERE `id`='40'; INSERT INTO
 * `server_event` VALUES ('40', '天上圣母系统', 'MazuSet', '1', '0', '说明:启动天上圣母系统');
 * 
 * #新增天上圣母 DELETE FROM `npc` WHERE `npcid`='91100'; INSERT INTO `npc` VALUES
 * ('91100', '天上圣母', '\\f=天上圣母', 'event.Npc_Mazu', '妈祖的祝福', 'L1Merchant',
 * '7348', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '', '0',
 * '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '', '0', '-1',
 * '-1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
 * '0', '0', '0', '0', '0', '0', '0', '-1', '0', '14', '0', '0', '0');
 * 
 * #新增天上圣母召换位置 DELETE FROM `server_event_spawn` WHERE `eventid`='40'; DELETE
 * FROM `server_event_spawn` WHERE `id`='40835'; DELETE FROM
 * `server_event_spawn` WHERE `id`='40851'; INSERT INTO `server_event_spawn`
 * VALUES (40835, 40, '天上圣母', 1, 91100, 0, 33445, 32796, 0, 0, 5, 0, 4, 0, 1);
 * INSERT INTO `server_event_spawn` VALUES (40851, 40, '天上圣母', 1, 91100, 0,
 * 33539, 32842, 0, 0, 5, 0, 4, 0, 1);
 * 
 * @author dexc
 * 
 */
public class MazuSet extends EventExecutor {

    private static final Log _log = LogFactory.getLog(MazuSet.class);

    /**
	 *
	 */
    private MazuSet() {
        // TODO Auto-generated constructor stub
    }

    public static EventExecutor get() {
        return new MazuSet();
    }

    @Override
    public void execute(final L1Event event) {
        try {

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
