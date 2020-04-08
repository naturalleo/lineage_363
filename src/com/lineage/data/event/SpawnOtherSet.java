package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.templates.L1Event;

/**
 * 公用控制项<BR>
 * 
 * @author dexc
 * 
 */
public class SpawnOtherSet extends EventExecutor {

    /**
	 *
	 */
    private SpawnOtherSet() {
        // TODO Auto-generated constructor stub
    }

    public static EventExecutor get() {
        return new SpawnOtherSet();
    }

    @Override
    public void execute(final L1Event event) {

    }

}
