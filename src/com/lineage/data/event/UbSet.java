package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.datatables.UBSpawnTable;
import com.lineage.server.datatables.UBTable;
import com.lineage.server.templates.L1Event;
import com.lineage.server.timecontroller.event.UbTime;

/**
 * 无线大赛 计时器
 * 
 * @author dexc
 * 
 */
public class UbSet extends EventExecutor {

    /**
	 *
	 */
    private UbSet() {
        // TODO Auto-generated constructor stub
    }

    public static EventExecutor get() {
        return new UbSet();
    }

    @Override
    public void execute(final L1Event event) {

        UBTable.getInstance().load();// 无线大赛设置资料

        UBSpawnTable.getInstance().load();// 无线大赛召唤资料

        // 无线大赛 计时器
        final UbTime ubTimeContoroller = new UbTime();
        ubTimeContoroller.start();
    }
}
