package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.DRAGON4;
import static com.lineage.server.model.skill.L1SkillId.DRAGON5;
import static com.lineage.server.model.skill.L1SkillId.DRAGON7;

import java.util.ConcurrentModificationException;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Skills;

/**
 * 魔法攻击判定
 * 
 * @author dexc
 * 
 */
public abstract class L1MagicMode {

    private static final Log _log = LogFactory.getLog(L1MagicMode.class);

    protected int _calcType;

    protected static final int PC_PC = 1;

    protected static final int PC_NPC = 2;

    protected static final int NPC_PC = 3;

    protected static final int NPC_NPC = 4;

    protected L1PcInstance _pc = null;

    protected L1PcInstance _targetPc = null;

    protected L1NpcInstance _npc = null;

    protected L1NpcInstance _targetNpc = null;

    protected int _leverage = 10; // 1/10倍で表现する。

    protected static final Random _random = new Random();

    /**
     * 伤害为0
     * 
     * @param pc
     * @return true 伤害为0
     */
    protected static boolean dmg0(final L1Character character) {
        try {
            if (character == null) {
                return false;
            }

            if (character.getSkillisEmpty()) {
                return false;
            }

            if (character.getSkillEffect().size() <= 0) {
                return false;
            }

            for (final Integer key : character.getSkillEffect()) {
                final Integer integer = L1AttackList.SKM0.get(key);
                if (integer != null) {
                    return true;
                }
            }

        } catch (final ConcurrentModificationException e) {
            // 技能取回发生其他线程进行修改
        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        }
        return false;
    }

    /**
     * 血盟技能魔法伤害增加
     * 
     * @return
     */
    protected static double getDamageUpByClan(final L1PcInstance pc) {
        double dmg = 0.0;
        try {
            if (pc == null) {
                return 0.0;
            }
            // 套装增加魔法伤害
            dmg += pc.get_magic_modifier_dmg();

            final L1Clan clan = pc.getClan();
            if (clan == null) {
                return dmg;
            }
            // 具有血盟技能
            if (clan.isClanskill()) {
                // 4:魔击(增加魔法攻击力)
                if (pc.get_other().get_clanskill() == 4) {
                    final int clanMan = clan.getOnlineClanMemberSize();
                    dmg += (0.25 * clanMan);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return 0.0;
        }
        return dmg;
    }

    /**
     * 血盟技能魔法伤害减免
     * 
     * @return
     */
    protected static double getDamageReductionByClan(final L1PcInstance targetPc) {
        double dmg = 0.0;
        try {
            if (targetPc == null) {
                return 0.0;
            }
            // 套装减免魔法伤害
            dmg += targetPc.get_magic_reduction_dmg();

            final L1Clan clan = targetPc.getClan();
            if (clan == null) {
                return 0.0;
            }
            // 具有血盟技能
            if (clan.isClanskill()) {
                // 8:消魔(增加魔法伤害减免)
                if (targetPc.get_other().get_clanskill() == 8) {
                    final int clanMan = clan.getOnlineClanMemberSize();
                    dmg += (0.25 * clanMan);
                }

            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return 0.0;
        }
        return dmg;
    }

    /**
     * 设置该技能魔法等级-攻击倍率(1/10)
     * 
     * @param i
     */
    public void setLeverage(final int i) {
        _leverage = i;
    }

    /**
     * 该技能魔法等级-攻击倍率(1/10)
     * 
     * @return
     */
    protected int getLeverage() {
        return _leverage;
    }

    /**
     * 施展者魔攻
     * 
     * @return
     */
    protected int getTargetSp() {
        int sp = 0;
        switch (_calcType) {
            case PC_PC:
            case PC_NPC:
                sp = _pc.getSp() - _pc.getTrueSp();

                switch (_pc.guardianEncounter()) {
                    case 3:// 邪恶的守护 Lv.1
                        sp += 1;
                        break;

                    case 4:// 邪恶的守护 Lv.2
                        sp += 2;
                        break;

                    case 5:// 邪恶的守护 Lv.3
                        sp += 3;
                        break;
                }

                // 风 魔法重击增加 睡眠耐性+3，持续1200秒
                if (_pc.hasSkillEffect(DRAGON4)) {
                    sp += 1;
                }

                // 生命-物理攻击回避率+10% 魔法伤害减免+50 魔法暴击率+1 额外攻击点数+2 防护中毒状态
                if (_pc.hasSkillEffect(DRAGON5)) {
                    sp += 1;
                }

                // 形象-物理攻击回避率+10% 魔法伤害减免+50 魔法暴击率+1 支撑耐性+3
                if (_pc.hasSkillEffect(DRAGON7)) {
                    sp += 1;
                }

                break;

            case NPC_NPC:
            case NPC_PC:
                sp = _npc.getSp() - _npc.getTrueSp();
                break;
        }
        return sp;
    }

    /**
     * 目标魔防
     * 
     * @return
     */
    protected int getTargetMr() {
        int mr = 0;
        switch (_calcType) {
            case NPC_PC:
            case PC_PC:
                if (_targetPc == null) {
                    return 0;
                }
                mr = _targetPc.getMr();

                switch (_targetPc.guardianEncounter()) {
                    case 0:// 正义的守护 Lv.1
                        mr += 3;
                        break;

                    case 1:// 正义的守护 Lv.2
                        mr += 6;
                        break;

                    case 2:// 正义的守护 Lv.3
                        mr += 9;
                        break;
                }
                break;

            case NPC_NPC:
            case PC_NPC:
                if (_targetNpc == null) {
                    return 0;
                }
                mr = _targetNpc.getMr();
                break;
        }
        return mr;
    }

    /**
     * 回避
     * 
     * @return true:回避成功 false:回避未成功
     */
    protected boolean calcEvasion() {
        if (_targetPc == null) {
            return false;
        }
        final int ev = _targetPc.get_evasion();
        if (ev == 0) {
            return false;
        }
        final int rnd = _random.nextInt(1000) + 1;
        return ev >= rnd;
    }

    /**
     * 属性伤害 减低 与 提升 attr:0.无属性魔法,1.地魔法,2.火魔法,4.水魔法,8.风魔法(,16.光魔法)
     */
    protected double calcAttrResistance(final int attr) {
        int resist = 0;
        switch (_calcType) {
            case PC_PC:
            case NPC_PC:
                if (_targetPc == null) {
                    return 0;
                }
                switch (attr) {
                    case L1Skills.ATTR_EARTH:
                        resist = _targetPc.getEarth();
                        break;

                    case L1Skills.ATTR_FIRE:
                        resist = _targetPc.getFire();
                        break;

                    case L1Skills.ATTR_WATER:
                        resist = _targetPc.getWater();
                        break;

                    case L1Skills.ATTR_WIND:
                        resist = _targetPc.getWind();
                        break;
                }
                break;

            case PC_NPC:
            case NPC_NPC:
                if (_targetNpc == null) {
                    return 0;
                }
                switch (attr) {
                    case L1Skills.ATTR_EARTH:
                        resist = _targetNpc.getEarth();
                        break;

                    case L1Skills.ATTR_FIRE:
                        resist = _targetNpc.getFire();
                        break;

                    case L1Skills.ATTR_WATER:
                        resist = _targetNpc.getWater();
                        break;

                    case L1Skills.ATTR_WIND:
                        resist = _targetNpc.getWind();
                        break;
                }
                break;
        }

        int resistFloor = (int) (0.32 * Math.abs(resist));
        if (resist >= 0) {
            resistFloor *= 1;

        } else {
            resistFloor *= -1;
        }

        final double attrDeffence = resistFloor / 32.0;

        return attrDeffence;
    }

    /**
     * ■■■■■■■■■■■■■■ 成功判定 ■■■■■■■■■■■■■ ●●●● 确率系魔法の成功判定 ●●●● 计算方法 攻击侧ポイント：LV +
     * ((MagicBonus * 3) * 魔法固有系数) 防御侧ポイント：((LV / 2) + (MR * 3)) / 2
     * 攻击成功率：攻击侧ポイント - 防御侧ポイント
     */
    public abstract boolean calcProbabilityMagic(final int skillId);

    /**
     * 魔法伤害值计算
     * 
     * @param skillId
     * @return
     */
    public abstract int calcMagicDamage(final int skillId);

    /**
     * ヒール回复量（对アンデッドにはダメージ）を算出
     * 
     * @param skillId
     * @return
     */
    public abstract int calcHealing(final int skillId);

    /**
     * 计算结果反映
     * 
     * @param damage
     * @param drainMana
     */
    public abstract void commit(final int damage, final int drainMana);
}
