package com.lineage.server.datatables.storage;

import java.util.Map;

/**
 * 任务纪录
 * 
 * @author dexc
 * 
 */
public interface CharacterQuestStorage {

    /**
     * 初始化载入
     */
    public void load();

    /**
     * 传回任务组
     * 
     * @param char_id
     *            人物OBJID
     * @return
     */
    public Map<Integer, Integer> get(final int char_id);

    /**
     * 新建任务
     * 
     * @param char_id
     *            人物OBJID
     * @param key
     *            任务编号
     * @param value
     *            任务进度
     */
    public void storeQuest(final int char_id, final int key, final int value);

    /**
     * 更新任务
     * 
     * @param char_id
     *            人物OBJID
     * @param key
     *            任务编号
     * @param value
     *            任务进度
     */
    public void updateQuest(final int char_id, final int key, final int value);

    /**
     * 解除任务
     * 
     * @param char_id
     *            人物OBJID
     * @param key
     *            任务编号
     */
    public void delQuest(final int char_id, final int key);

}
