package com.lineage.data.event;

import java.util.HashMap;
import java.util.Map;
import com.lineage.data.executor.EventExecutor;
import com.lineage.server.templates.L1Event;

/**
 * 全职技能导师<BR>
 * 
 * @author dexc
 * 
 */
public class SkillTeacherSet extends EventExecutor {

    // 不开放学习的技能
    public static final Map<Integer, Integer> RESKILLLIST = new HashMap<Integer, Integer>();

    /**
	 *
	 */
    private SkillTeacherSet() {
        // TODO Auto-generated constructor stub
    }

    public static EventExecutor get() {
        return new SkillTeacherSet();
    }

    @Override
    public void execute(final L1Event event) {
        // 加载不开放学习的技能
        final String[] set = event.get_eventother().split(",");
        for (final String string : set) {
            RESKILLLIST.put(Integer.parseInt(string) - 1,
                    Integer.parseInt(string));
        }
    }

}
