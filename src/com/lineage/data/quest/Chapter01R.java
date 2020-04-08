package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Party;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.thread.GeneralThreadPool;

/**
 * 说明:魔法师．哈汀(故事)
 * 
 * @author loli
 * 
 */
public class Chapter01R implements Runnable {

    private static final Log _log = LogFactory.getLog(Chapter01R.class);

    public boolean DOOR_1;

    public boolean DOOR_2;

    public boolean DOOR_4;

    public boolean DOOR_4OPEN;

    public Chapter01R(final L1Party party, final int qid, int i) {
        // TODO 等待修正
    }

    public void startR() {
        GeneralThreadPool.get().execute(this);
    }

    @Override
    public void run() {

    }

    public int get_time() {
        // TODO 等待修正
        return 0;
    }

    public void boss_a_death() {
        // TODO 等待修正
    }

    public void boss_b_death() {
        // TODO 等待修正
    }

    public void down_count(L1NpcInstance npc) {
        // TODO 等待修正
    }
}
