package com.lineage.server.storage;

import com.lineage.server.model.Instance.L1PcInstance;

public interface CharacterStorage {

    public void createCharacter(L1PcInstance pc) throws Exception;

    public void deleteCharacter(String accountName, String charName)
            throws Exception;

    public void storeCharacter(L1PcInstance pc) throws Exception;

    /**
     * 载入PC资料
     * 
     * @param charName
     * @return
     * @throws Exception
     */
    public L1PcInstance loadCharacter(String charName) throws Exception;
}
