package com.lineage.server.datatables.storage;

import java.util.Map;

import com.lineage.server.templates.L1House;

/**
 * 盟屋资料
 * 
 * @author dexc
 * 
 */
public interface HouseStorage {

    /**
     * 初始化载入
     */
    public void load();

    /**
     * 传回小屋列表
     * 
     * @return
     */
    public Map<Integer, L1House> getHouseTableList();

    /**
     * 传回指定小屋资料
     * 
     * @param houseId
     * @return
     */
    public L1House getHouseTable(int houseId);

    /**
     * 更新小屋资料
     * 
     * @param house
     */
    public void updateHouse(L1House house);
}
