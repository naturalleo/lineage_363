package com.lineage.server.timecontroller.npc;

import java.util.Timer;
import java.util.TimerTask;
import com.lineage.server.model.Instance.L1NpcInstance;

/**
 * 为冲晕效果的显示准确单独写一个类
 * 指定时间删除NPC 
 * 
 * @author hjx1000
 * 
 */
public class NpcDeleteTimer_1 extends TimerTask {

    private final L1NpcInstance _npc;

    private final int _timeMillis;

    public NpcDeleteTimer_1(final L1NpcInstance npc, final int timeMillis) {
        this._npc = npc;
        this._timeMillis = timeMillis;
    }

    public void begin() {
        final Timer timer = new Timer();
        timer.schedule(this, this._timeMillis);
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (this._npc != null) {
            this._npc.deleteMe();
            this.cancel();
		}
	}
}
