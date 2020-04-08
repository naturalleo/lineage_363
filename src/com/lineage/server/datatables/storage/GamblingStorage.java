package com.lineage.server.datatables.storage;

import com.lineage.server.templates.L1Gambling;

/**
 * 赌场纪录
 * 
 * @author dexc
 * 
 */
public interface GamblingStorage {

    /**
     * 初始化载入
     */
    public void load();

    /**
     * 传回赌场纪录(获胜NPC票号)
     * 
     * @param key
     * @return
     */
    public L1Gambling getGambling(String key);

    /**
     * 传回赌场纪录(场次编号)
     * 
     * @param key
     * @return
     */
    public L1Gambling getGambling(final int key);

    /**
     * 增加赌场纪录
     */
    public void add(L1Gambling gambling);

    /**
     * 更新赌场纪录
     */
    public void updateGambling(final int id, final int outcount);

    /**
     * 传回场次数量 与获胜次数
     * 
     * @param npcid
     * @return
     */
    public int[] winCount(int npcid);

    /**
     * 已用最大ID
     * 
     * @return
     */
    public int maxId();
}
