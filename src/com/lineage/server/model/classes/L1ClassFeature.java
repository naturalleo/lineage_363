package com.lineage.server.model.classes;

import com.lineage.server.model.Instance.L1PcInstance;

public abstract class L1ClassFeature {

    public static L1ClassFeature newClassFeature(final int classId) {
        switch (classId) {
            case L1PcInstance.CLASSID_PRINCE:
            case L1PcInstance.CLASSID_PRINCESS:
                return new L1RoyalClassFeature();

            case L1PcInstance.CLASSID_ELF_MALE:
            case L1PcInstance.CLASSID_ELF_FEMALE:
                return new L1ElfClassFeature();

            case L1PcInstance.CLASSID_KNIGHT_MALE:
            case L1PcInstance.CLASSID_KNIGHT_FEMALE:
                return new L1KnightClassFeature();

            case L1PcInstance.CLASSID_WIZARD_MALE:
            case L1PcInstance.CLASSID_WIZARD_FEMALE:
                return new L1WizardClassFeature();

            case L1PcInstance.CLASSID_DARK_ELF_MALE:
            case L1PcInstance.CLASSID_DARK_ELF_FEMALE:
                return new L1DarkElfClassFeature();

            case L1PcInstance.CLASSID_DRAGON_KNIGHT_MALE:
            case L1PcInstance.CLASSID_DRAGON_KNIGHT_FEMALE:
                return new L1DragonKnightClassFeature();

            case L1PcInstance.CLASSID_ILLUSIONIST_MALE:
            case L1PcInstance.CLASSID_ILLUSIONIST_FEMALE:
                return new L1IllusionistClassFeature();
        }
        throw new IllegalArgumentException();
    }

    /**
     * 职业AC补正
     * 
     * @param ac
     * @return
     */
    public abstract int getAcDefenseMax(int ac);

    /**
     * 职业魔法等级
     * 
     * @param playerLevel
     * @return
     */
    public abstract int getMagicLevel(int playerLevel);

    /**
     * 职业物理攻击补正
     * 
     * @param playerLevel
     * @return
     */
    public abstract int getAttackLevel(int playerLevel);

    /**
     * 职业物理命中补正
     * 
     * @param playerLevel
     * @return
     */
    public abstract int getHitLevel(int playerLevel);
}
