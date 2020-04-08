package com.lineage.server.model.classes;

/**
 * 幻术师
 * 
 * @author dexc
 * 
 */
class L1IllusionistClassFeature extends L1ClassFeature {

    @Override
    public int getAcDefenseMax(final int ac) {
        return ac >> 2; // /4
    }

    @Override
    public int getMagicLevel(final int playerLevel) {
        return Math.min(6, playerLevel >> 3); // /8
    }

    @Override
    public int getAttackLevel(int playerLevel) {
        return playerLevel / 16;
    }

    @Override
    public int getHitLevel(int playerLevel) {
        return playerLevel / 40;
    }
}
