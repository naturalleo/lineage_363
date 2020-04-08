package com.lineage.server.datatables.storage;

import java.util.Map;

import com.lineage.server.templates.L1Castle;

/**
 * 城堡资料
 * 
 * @author dexc
 * 
 */
public interface CastleStorage {

    public void load();

    public Map<Integer, L1Castle> getCastleMap();

    public L1Castle[] getCastleTableList();

    public L1Castle getCastleTable(int id);

    public void updateCastle(L1Castle castle);


}
