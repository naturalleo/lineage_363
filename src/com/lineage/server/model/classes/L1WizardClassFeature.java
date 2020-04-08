package com.lineage.server.model.classes;

/**
 * 法师
 * 
 * @author dexc
 * 
 */
class L1WizardClassFeature extends L1ClassFeature {

    @Override
    public int getAcDefenseMax(final int ac) {
        return ac / 5;
    }

    @Override
    public int getMagicLevel(final int playerLevel) {
        return Math.min(10, playerLevel >> 2);// /4
    }

    @Override
    public int getAttackLevel(int playerLevel) {
        return playerLevel / 20;
    }

    @Override
    public int getHitLevel(int playerLevel) {
        return playerLevel / 40;
    }
}
