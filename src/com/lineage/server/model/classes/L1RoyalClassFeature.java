package com.lineage.server.model.classes;

/**
 * 王族
 * 
 * @author dexc
 * 
 */
class L1RoyalClassFeature extends L1ClassFeature {

    @Override
    public int getAcDefenseMax(final int ac) {
        return ac / 3;
    }

    @Override
    public int getMagicLevel(final int playerLevel) {
        return Math.min(2, playerLevel / 10);
    }

    @Override
    public int getAttackLevel(int playerLevel) {
        return playerLevel / 10;
    }

    @Override
    public int getHitLevel(int playerLevel) {
        return playerLevel / 15;
    }
}
