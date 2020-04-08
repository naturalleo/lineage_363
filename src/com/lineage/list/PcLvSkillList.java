package com.lineage.list;

import static com.lineage.server.model.skill.L1SkillId.ADDITIONAL_FIRE;
import static com.lineage.server.model.skill.L1SkillId.AQUA_PROTECTER;
import static com.lineage.server.model.skill.L1SkillId.AREA_OF_SILENCE;
import static com.lineage.server.model.skill.L1SkillId.ARM_BREAKER;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_ANTHARAS;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_VALAKAS;
import static com.lineage.server.model.skill.L1SkillId.BLESS_WEAPON;
import static com.lineage.server.model.skill.L1SkillId.BLIND_HIDING;
import static com.lineage.server.model.skill.L1SkillId.BLOODY_SOUL;
import static com.lineage.server.model.skill.L1SkillId.BODY_TO_MIND;
import static com.lineage.server.model.skill.L1SkillId.BONE_BREAK;
import static com.lineage.server.model.skill.L1SkillId.BRAVE_AURA;
import static com.lineage.server.model.skill.L1SkillId.BRING_STONE;
import static com.lineage.server.model.skill.L1SkillId.BURNING_WEAPON;
import static com.lineage.server.model.skill.L1SkillId.CALL_CLAN;
import static com.lineage.server.model.skill.L1SkillId.CALL_OF_NATURE;
import static com.lineage.server.model.skill.L1SkillId.CLEAR_MIND;
import static com.lineage.server.model.skill.L1SkillId.COUNTER_BARRIER;
import static com.lineage.server.model.skill.L1SkillId.COUNTER_DETECTION;
import static com.lineage.server.model.skill.L1SkillId.COUNTER_MIRROR;
import static com.lineage.server.model.skill.L1SkillId.CREATE_MAGICAL_WEAPON;
import static com.lineage.server.model.skill.L1SkillId.CREATE_ZOMBIE;
import static com.lineage.server.model.skill.L1SkillId.CUBE_BALANCE;
import static com.lineage.server.model.skill.L1SkillId.CURE_POISON;
import static com.lineage.server.model.skill.L1SkillId.CURSE_PARALYZE;
import static com.lineage.server.model.skill.L1SkillId.DARKNESS;
import static com.lineage.server.model.skill.L1SkillId.DISEASE;
import static com.lineage.server.model.skill.L1SkillId.DOUBLE_BRAKE;
import static com.lineage.server.model.skill.L1SkillId.DRAGON_SKIN;
import static com.lineage.server.model.skill.L1SkillId.DRESS_DEXTERITY;
import static com.lineage.server.model.skill.L1SkillId.DRESS_EVASION;
import static com.lineage.server.model.skill.L1SkillId.DRESS_MIGHTY;
import static com.lineage.server.model.skill.L1SkillId.EARTH_BIND;
import static com.lineage.server.model.skill.L1SkillId.EARTH_BLESS;
import static com.lineage.server.model.skill.L1SkillId.EARTH_SKIN;
import static com.lineage.server.model.skill.L1SkillId.ELEMENTAL_FALL_DOWN;
import static com.lineage.server.model.skill.L1SkillId.ELEMENTAL_FIRE;
import static com.lineage.server.model.skill.L1SkillId.ELEMENTAL_PROTECTION;
import static com.lineage.server.model.skill.L1SkillId.ENTANGLE;
import static com.lineage.server.model.skill.L1SkillId.ERASE_MAGIC;
import static com.lineage.server.model.skill.L1SkillId.EXOTIC_VITALIZE;
import static com.lineage.server.model.skill.L1SkillId.FIREBALL;
import static com.lineage.server.model.skill.L1SkillId.FIRE_BLESS;
import static com.lineage.server.model.skill.L1SkillId.FIRE_WEAPON;
import static com.lineage.server.model.skill.L1SkillId.FREEZING_BLIZZARD;
import static com.lineage.server.model.skill.L1SkillId.FULL_HEAL;
import static com.lineage.server.model.skill.L1SkillId.GLOWING_AURA;
import static com.lineage.server.model.skill.L1SkillId.GREATER_ELEMENTAL;
import static com.lineage.server.model.skill.L1SkillId.HEAL;
import static com.lineage.server.model.skill.L1SkillId.HEAL_ALL;
import static com.lineage.server.model.skill.L1SkillId.HOLY_WEAPON;
import static com.lineage.server.model.skill.L1SkillId.HORROR_OF_DEATH;
import static com.lineage.server.model.skill.L1SkillId.ILLUSION_LICH;
import static com.lineage.server.model.skill.L1SkillId.INSIGHT;
import static com.lineage.server.model.skill.L1SkillId.IRON_SKIN;
import static com.lineage.server.model.skill.L1SkillId.LESSER_ELEMENTAL;
import static com.lineage.server.model.skill.L1SkillId.LIGHTNING;
import static com.lineage.server.model.skill.L1SkillId.LIGHTNING_STORM;
import static com.lineage.server.model.skill.L1SkillId.MAGMA_BREATH;
import static com.lineage.server.model.skill.L1SkillId.MEDITATION;
import static com.lineage.server.model.skill.L1SkillId.MIRROR_IMAGE;
import static com.lineage.server.model.skill.L1SkillId.MOVING_ACCELERATION;
import static com.lineage.server.model.skill.L1SkillId.NATURES_BLESSING;
import static com.lineage.server.model.skill.L1SkillId.NATURES_TOUCH;
import static com.lineage.server.model.skill.L1SkillId.PANIC;
import static com.lineage.server.model.skill.L1SkillId.PHANTASM;
import static com.lineage.server.model.skill.L1SkillId.POLLUTE_WATER;
import static com.lineage.server.model.skill.L1SkillId.REDUCTION_ARMOR;
import static com.lineage.server.model.skill.L1SkillId.RESIST_ELEMENTAL;
import static com.lineage.server.model.skill.L1SkillId.RESIST_MAGIC;
import static com.lineage.server.model.skill.L1SkillId.RETURN_TO_NATURE;
import static com.lineage.server.model.skill.L1SkillId.RUN_CLAN;
import static com.lineage.server.model.skill.L1SkillId.SHINING_AURA;
import static com.lineage.server.model.skill.L1SkillId.SHOCK_STUN;
import static com.lineage.server.model.skill.L1SkillId.SILENCE;
import static com.lineage.server.model.skill.L1SkillId.SOLID_CARRIAGE;
import static com.lineage.server.model.skill.L1SkillId.SOUL_OF_FLAME;
import static com.lineage.server.model.skill.L1SkillId.STALAC;
import static com.lineage.server.model.skill.L1SkillId.STORM_EYE;
import static com.lineage.server.model.skill.L1SkillId.STORM_SHOT;
import static com.lineage.server.model.skill.L1SkillId.STRIKER_GALE;
import static com.lineage.server.model.skill.L1SkillId.TELEPORT_TO_MATHER;
import static com.lineage.server.model.skill.L1SkillId.THUNDER_GRAB;
import static com.lineage.server.model.skill.L1SkillId.TRIPLE_ARROW;
import static com.lineage.server.model.skill.L1SkillId.TRUE_TARGET;
import static com.lineage.server.model.skill.L1SkillId.VENOM_RESIST;
import static com.lineage.server.model.skill.L1SkillId.WATER_LIFE;
import static com.lineage.server.model.skill.L1SkillId.WEAK_ELEMENTAL;
import static com.lineage.server.model.skill.L1SkillId.WIND_SHACKLE;
import static com.lineage.server.model.skill.L1SkillId.WIND_SHOT;
import static com.lineage.server.model.skill.L1SkillId.WIND_WALK;
import static com.lineage.server.model.skill.L1SkillId.DRESS_HALZ;

import java.util.ArrayList;

import com.lineage.server.model.Instance.L1PcInstance;

public class PcLvSkillList {

    /**
     * 一般魔法老师(限制教学到3级魔法)
     * 
     * @param pc
     * @return
     */
    public static ArrayList<Integer> scount(final L1PcInstance pc) {
        final ArrayList<Integer> skillList = new ArrayList<Integer>();
        switch (pc.getType()) {
            case 0: // 王族
                if (pc.getLevel() >= 10) {
                    for (int skillid = (HEAL - 1); skillid < HOLY_WEAPON; skillid++) {
                        skillList.add(new Integer(skillid));
                    }

                }
                if (pc.getLevel() >= 20) {
                    for (int skillid = (CURE_POISON - 1); skillid < STALAC; skillid++) {
                        skillList.add(new Integer(skillid));
                    }
                }
                break;

            case 1: // 骑士
                if (pc.getLevel() >= 50) {
                    for (int skillid = (HEAL - 1); skillid < HOLY_WEAPON; skillid++) {
                        skillList.add(new Integer(skillid));
                    }
                }
                break;

            case 2: // 精灵
                if (pc.getLevel() >= 8) {
                    for (int skillid = (HEAL - 1); skillid < HOLY_WEAPON; skillid++) {
                        skillList.add(new Integer(skillid));
                    }
                }
                if (pc.getLevel() >= 16) {
                    for (int skillid = (CURE_POISON - 1); skillid < STALAC; skillid++) {
                        skillList.add(new Integer(skillid));
                    }
                }
                if (pc.getLevel() >= 24) {
                    for (int skillid = (LIGHTNING - 1); skillid < WEAK_ELEMENTAL; skillid++) {
                        skillList.add(new Integer(skillid));
                    }
                }
                break;

            case 3: // 法师
                if (pc.getLevel() >= 4) {
                    for (int skillid = (HEAL - 1); skillid < HOLY_WEAPON; skillid++) {
                        skillList.add(new Integer(skillid));
                    }
                }
                if (pc.getLevel() >= 8) {
                    for (int skillid = (CURE_POISON - 1); skillid < STALAC; skillid++) {
                        skillList.add(new Integer(skillid));
                    }
                }
                if (pc.getLevel() >= 12) {
                    for (int skillid = (LIGHTNING - 1); skillid < WEAK_ELEMENTAL; skillid++) {
                        skillList.add(new Integer(skillid));
                    }
                }
                break;

            case 4: // 黑妖
                if (pc.getLevel() >= 12) {
                    for (int skillid = (HEAL - 1); skillid < HOLY_WEAPON; skillid++) {
                        skillList.add(new Integer(skillid));
                    }
                }
                if (pc.getLevel() >= 24) {
                    for (int skillid = (CURE_POISON - 1); skillid < STALAC; skillid++) {
                        skillList.add(new Integer(skillid));
                    }
                }
                break;
        }
        return skillList;
    }

    /**
     * 幻术
     * 
     * @param pc
     */
    public static ArrayList<Integer> isIllusionist(final L1PcInstance pc) {
        final ArrayList<Integer> skillList = new ArrayList<Integer>();
        if (pc.getLevel() >= 10) {
            for (int skillid = (MIRROR_IMAGE - 1); skillid < BONE_BREAK; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 20) {
            for (int skillid = (ILLUSION_LICH - 1); skillid < PHANTASM; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 30) {
            for (int skillid = (ARM_BREAKER - 1); skillid < INSIGHT; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 40) {
            for (int skillid = (PANIC - 1); skillid < CUBE_BALANCE; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        return skillList;
    }

    /**
     * 龙骑
     * 
     * @param pc
     */
    public static ArrayList<Integer> isDragonKnight(final L1PcInstance pc) {
        final ArrayList<Integer> skillList = new ArrayList<Integer>();
        if (pc.getLevel() >= 15) {
            for (int skillid = (DRAGON_SKIN - 1); skillid < MAGMA_BREATH; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 30) {
            for (int skillid = (AWAKEN_ANTHARAS - 1); skillid < THUNDER_GRAB; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 45) {
            for (int skillid = (HORROR_OF_DEATH - 1); skillid < AWAKEN_VALAKAS; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        return skillList;
    }

    /**
     * 黑妖
     * 
     * @param pc
     */
    public static ArrayList<Integer> isDarkelf(final L1PcInstance pc) {
        final ArrayList<Integer> skillList = new ArrayList<Integer>();
        if (pc.getLevel() >= 12) {
            for (int skillid = (HEAL - 1); skillid < HOLY_WEAPON; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 24) {
            for (int skillid = (CURE_POISON - 1); skillid < STALAC; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 15) {
            for (int skillid = (BLIND_HIDING - 1); skillid < BRING_STONE; skillid++) {
                skillList.add(new Integer(skillid));
            }
            skillList.add(new Integer(DRESS_MIGHTY - 1));
        }
        if (pc.getLevel() >= 30) {
            for (int skillid = (MOVING_ACCELERATION - 1); skillid < VENOM_RESIST; skillid++) {
                skillList.add(new Integer(skillid));
            }
            skillList.add(new Integer(DRESS_DEXTERITY - 1));
        }
        if (pc.getLevel() >= 45) {
            for (int skillid = (DOUBLE_BRAKE - 1); skillid < DRESS_HALZ; skillid++) {
                skillList.add(new Integer(skillid));
            }
            skillList.add(new Integer(DRESS_EVASION - 1));
        }
        return skillList;
    }

    /**
     * 法师
     * 
     * @param pc
     */
    public static ArrayList<Integer> isWizard(final L1PcInstance pc) {
        final ArrayList<Integer> skillList = new ArrayList<Integer>();
        if (pc.getLevel() >= 4) {
            for (int skillid = (HEAL - 1); skillid < HOLY_WEAPON; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 8) {
            for (int skillid = (CURE_POISON - 1); skillid < STALAC; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 12) {
            for (int skillid = (LIGHTNING - 1); skillid < WEAK_ELEMENTAL; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 16) {
            for (int skillid = (FIREBALL - 1); skillid < MEDITATION; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 20) {
            for (int skillid = (CURSE_PARALYZE - 1); skillid < DARKNESS; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 24) {
            for (int skillid = (CREATE_ZOMBIE - 1); skillid < BLESS_WEAPON; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 28) {
            for (int skillid = (HEAL_ALL - 1); skillid < DISEASE; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 32) {
            for (int skillid = (FULL_HEAL - 1); skillid < SILENCE; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 36) {
            for (int skillid = (LIGHTNING_STORM - 1); skillid < COUNTER_DETECTION; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 40) {
            for (int skillid = (CREATE_MAGICAL_WEAPON - 1); skillid < FREEZING_BLIZZARD; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        return skillList;
    }

    /**
     * 精灵
     * 
     * @param pc
     */
    public static ArrayList<Integer> isElf(final L1PcInstance pc) {
        final ArrayList<Integer> skillList = new ArrayList<Integer>();
        if (pc.getLevel() >= 8) {
            for (int skillid = (HEAL - 1); skillid < HOLY_WEAPON; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 16) {
            for (int skillid = (CURE_POISON - 1); skillid < STALAC; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 24) {
            for (int skillid = (LIGHTNING - 1); skillid < WEAK_ELEMENTAL; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 32) {
            for (int skillid = (FIREBALL - 1); skillid < MEDITATION; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 40) {
            for (int skillid = (CURSE_PARALYZE - 1); skillid < DARKNESS; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 48) {
            for (int skillid = (CREATE_ZOMBIE - 1); skillid < BLESS_WEAPON; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }

        if (pc.getLevel() >= 10) {
            skillList.add(new Integer(RESIST_MAGIC - 1));
            skillList.add(new Integer(BODY_TO_MIND - 1));
            skillList.add(new Integer(TELEPORT_TO_MATHER - 1));
        }
        if (pc.getLevel() >= 20) {
            skillList.add(new Integer(CLEAR_MIND - 1));
            skillList.add(new Integer(RESIST_ELEMENTAL - 1));
        }
        if (pc.getLevel() >= 30) {
            skillList.add(new Integer(TRIPLE_ARROW - 1));
            skillList.add(new Integer(RETURN_TO_NATURE - 1));
            skillList.add(new Integer(BLOODY_SOUL - 1));
            skillList.add(new Integer(ELEMENTAL_PROTECTION - 1));

            switch (pc.getElfAttr()) {
                case 1:// 地属性
                    skillList.add(new Integer(EARTH_SKIN - 1));
                    skillList.add(new Integer(ENTANGLE - 1));
                    break;

                case 2:// 火属性
                    skillList.add(new Integer(FIRE_WEAPON - 1));
                    break;

                case 4:// 水属性
                    skillList.add(new Integer(WATER_LIFE - 1));
                    break;

                case 8:// 风属性
                    skillList.add(new Integer(WIND_SHOT - 1));
                    skillList.add(new Integer(WIND_WALK - 1));
                    break;
            }
        }
        if (pc.getLevel() >= 40) {
            skillList.add(new Integer(ELEMENTAL_FALL_DOWN - 1));
            skillList.add(new Integer(ERASE_MAGIC - 1));
            skillList.add(new Integer(LESSER_ELEMENTAL - 1));

            switch (pc.getElfAttr()) {
                case 1:// 地属性
                    skillList.add(new Integer(EARTH_BIND - 1));
                    skillList.add(new Integer(EARTH_BLESS - 1));
                    break;

                case 2:// 火属性
                    skillList.add(new Integer(FIRE_BLESS - 1));
                    break;

                case 4:// 水属性
                    skillList.add(new Integer(NATURES_TOUCH - 1));
                    skillList.add(new Integer(AQUA_PROTECTER - 1));
                    break;

                case 8:// 风属性
                    skillList.add(new Integer(STORM_EYE - 1));
                    break;
            }
        }
        if (pc.getLevel() >= 50) {
            skillList.add(new Integer(COUNTER_MIRROR - 1));
            skillList.add(new Integer(AREA_OF_SILENCE - 1));
            skillList.add(new Integer(GREATER_ELEMENTAL - 1));

            switch (pc.getElfAttr()) {
                case 1:// 地属性
                    skillList.add(new Integer(IRON_SKIN - 1));
                    skillList.add(new Integer(EXOTIC_VITALIZE - 1));
                    break;

                case 2:// 火属性
                    skillList.add(new Integer(BURNING_WEAPON - 1));
                    skillList.add(new Integer(ELEMENTAL_FIRE - 1));
                    skillList.add(new Integer(SOUL_OF_FLAME - 1));
                    skillList.add(new Integer(ADDITIONAL_FIRE - 1));
                    break;

                case 4:// 水属性
                    skillList.add(new Integer(NATURES_BLESSING - 1));
                    skillList.add(new Integer(CALL_OF_NATURE - 1));
                    skillList.add(new Integer(POLLUTE_WATER - 1));
                    break;

                case 8:// 风属性
                    skillList.add(new Integer(STORM_SHOT - 1));
                    skillList.add(new Integer(WIND_SHACKLE - 1));
                    skillList.add(new Integer(STRIKER_GALE - 1));
                    break;
            }
        }
        return skillList;
    }

    /**
     * 骑士
     * 
     * @param pc
     */
    public static ArrayList<Integer> isKnight(final L1PcInstance pc) {
        final ArrayList<Integer> skillList = new ArrayList<Integer>();
        if (pc.getLevel() >= 50) {// magic lv1
            for (int skillid = (HEAL - 1); skillid < HOLY_WEAPON; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 50) {// Knight magic lv1
            skillList.add(new Integer(SHOCK_STUN - 1));
            skillList.add(new Integer(REDUCTION_ARMOR - 1));
            skillList.add(new Integer(SOLID_CARRIAGE - 1));
            skillList.add(new Integer(COUNTER_BARRIER - 1));
        }
        if (pc.getLevel() >= 60) {// Knight magic lv2
            skillList.add(new Integer(SOLID_CARRIAGE - 1));

        }
        return skillList;
    }

    /**
     * 王族
     * 
     * @param pc
     */
    public static ArrayList<Integer> isCrown(final L1PcInstance pc) {
        final ArrayList<Integer> skillList = new ArrayList<Integer>();
        if (pc.getLevel() >= 10) {// magic lv1
            for (int skillid = (HEAL - 1); skillid < HOLY_WEAPON; skillid++) {
                skillList.add(new Integer(skillid));
            }

        }
        if (pc.getLevel() >= 20) {// magic lv2
            for (int skillid = (CURE_POISON - 1); skillid < STALAC; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 15) {// Crown magic lv1
            skillList.add(new Integer(TRUE_TARGET - 1));
        }
        if (pc.getLevel() >= 30) {// Crown magic lv2
            skillList.add(new Integer(CALL_CLAN - 1));
        }
        if (pc.getLevel() >= 40) {// Crown magic lv3
            skillList.add(new Integer(GLOWING_AURA - 1));
        }
        if (pc.getLevel() >= 45) {// Crown magic lv4
            skillList.add(new Integer(RUN_CLAN - 1));
        }
        if (pc.getLevel() >= 50) {// Crown magic lv5
            skillList.add(new Integer(BRAVE_AURA - 1));
        }
        if (pc.getLevel() >= 55) {// Crown magic lv6
            skillList.add(new Integer(SHINING_AURA - 1));
        }
        return skillList;
    }

}
