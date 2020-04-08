package com.lineage.server.model.classes;

/**
 * 龙骑士
 * 
 * @author dexc
 * 
 */
class L1DragonKnightClassFeature extends L1ClassFeature {

    @Override
    public int getAcDefenseMax(final int ac) {
        return ac / 3;
    }

    @Override
    public int getMagicLevel(final int playerLevel) {
        return Math.min(4, playerLevel / 9);
    }

    @Override
    public int getAttackLevel(int playerLevel) {
        return playerLevel / 13;
    }

    @Override
    public int getHitLevel(int playerLevel) {
        return playerLevel / 25;
    }
}
