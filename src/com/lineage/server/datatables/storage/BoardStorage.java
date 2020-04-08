package com.lineage.server.datatables.storage;

import java.util.Map;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Board;

/**
 * 布告栏资料
 * 
 * @author dexc
 * 
 */
public interface BoardStorage {
    /**
     * 初始化载入
     */
    public void load();

    /**
     * 传回公告阵列
     */
    public Map<Integer, L1Board> getBoardMap();

    /**
     * 传回公告阵列
     */
    public L1Board[] getBoardTableList();

    /**
     * 传回指定公告
     */
    public L1Board getBoardTable(int houseId);

    /**
     * 传回已用最大公告编号
     */
    public int getMaxId();

    /**
     * 增加布告栏资料
     * 
     * @param pc
     * @param date
     * @param title
     * @param content
     */
    public void writeTopic(L1PcInstance pc, String date, String title,
            String content);

    /**
     * 删除布告栏资料
     * 
     * @param number
     */
    public void deleteTopic(int number);

}
