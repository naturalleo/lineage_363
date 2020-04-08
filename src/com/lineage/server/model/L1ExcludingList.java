package com.lineage.server.model;

import java.util.ArrayList;

/**
 * 人物讯息拒绝清单
 * 
 * @author daien
 * 
 */
public class L1ExcludingList {

    private ArrayList<String> _nameList = new ArrayList<String>();

    /**
     * 加入人物讯息拒绝清单
     * 
     * @param name
     */
    public void add(final String name) {
        this._nameList.add(name);
    }

    /**
     * 移出人物讯息拒绝名单
     * 
     * @param name
     *            移除名称
     * @return 移除成功返回人物名称<BR>
     *         清单中无该物件返回null
     */
    public String remove(final String name) {
        for (final String each : this._nameList) {
            if (each.equalsIgnoreCase(name)) {
                this._nameList.remove(each);
                return each;
            }
        }
        return null;
    }

    /**
     * 指定人物讯息拒绝接收
     * 
     * @return true:拒绝 false:接收
     */
    public boolean contains(final String name) {
        for (final String each : this._nameList) {
            if (each.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 拒绝清单人数是否达到上限
     * 
     * @return true:是 false:否
     */
    public boolean isFull() {
        return (this._nameList.size() >= 16) ? true : false;
    }
}
