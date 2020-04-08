package com.lineage.server.model.drop;

import java.util.ArrayList;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;

/**
 * NPC持有物品取回
 * 
 * @author dexc
 * 
 */
public interface DropShareExecutor {

    /**
     * 掉落物品的分配
     * 
     * @param npc
     * @param acquisitorList
     * @param hateList
     */
    public void dropShare(L1NpcInstance npc,
            ArrayList<L1Character> acquisitorList, ArrayList<Integer> hateList);
}
