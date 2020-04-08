package com.lineage.server.datatables.storage;

import com.lineage.server.templates.L1Config;

/**
 * 快速键纪录
 * 
 * @author dexc
 * 
 */
public interface CharacterConfigStorage {

    public void load();

    public L1Config get(int objectId);

    public void storeCharacterConfig(int objectId, int length, byte[] data);

    public void updateCharacterConfig(int objectId, int length, byte[] data);

}
