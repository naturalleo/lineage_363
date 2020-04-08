package com.lineage.server.datatables.storage;

import java.util.ArrayList;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1BookMark;

/**
 * 记忆座标纪录资料
 * 
 * @author dexc
 * 
 */
public interface CharBookStorage {

    /**
     * 初始化载入
     */
    public void load();

    /**
     * 取回保留记忆座标纪录群
     * 
     * @param pc
     */
    public ArrayList<L1BookMark> getBookMarks(final L1PcInstance pc);

    /**
     * 取回保留记忆座标纪录
     * 
     * @param pc
     */
    public L1BookMark getBookMark(final L1PcInstance pc, final int i);

    /**
     * 删除记忆座标
     * 
     * @param pc
     * @param s
     */
    public void deleteBookmark(final L1PcInstance pc, final String s);

    /**
     * 增加记忆座标
     * 
     * @param pc
     * @param s
     */
    public void addBookmark(final L1PcInstance pc, final String s);
}
