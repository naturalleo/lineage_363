package com.lineage.server.model.classes;

/**
 * 黑暗精灵
 * 
 * @author dexc
 * 
 */
class L1DarkElfClassFeature extends L1ClassFeature {

    @Override
    public int getAcDefenseMax(final int ac) {
        return ac >> 2; // /4
    }

    @Override
    public int getMagicLevel(final int playerLevel) {
        return Math.min(2, playerLevel / 12);
    }

    @Override
    public int getAttackLevel(int playerLevel) {
        return playerLevel / 16;
    }

    @Override
    public int getHitLevel(int playerLevel) {
        return playerLevel / 30;
    }
}
