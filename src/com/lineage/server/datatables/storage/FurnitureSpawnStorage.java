package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1FurnitureInstance;

/**
 * 家具资料
 * 
 * @author dexc
 * 
 */
public interface FurnitureSpawnStorage {

    public void load();

    public void insertFurniture(L1FurnitureInstance furniture);

    public void deleteFurniture(L1FurnitureInstance furniture);

}
