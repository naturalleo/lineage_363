package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.*;

import java.util.ConcurrentModificationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.server.ActionCodes;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1Skills;

/**
 * 魔法攻击判定(NPC)
 * 
 * @author daien
 * 
 */
public class L1MagicNpc extends L1MagicMode {

    private static final Log _log = LogFactory.getLog(L1MagicNpc.class);

    /**
     * 魔法攻击判定(NPC)
     * 
     * @param attacker
     * @param target
     */
    public L1MagicNpc(final L1NpcInstance attacker, final L1Character target) {
        if (attacker == null) {
            return;
        }

        if (target instanceof L1PcInstance) {
            this._calcType = NPC_PC;
            this._npc = attacker;
            this._targetPc = (L1PcInstance) target;

        } else {
            this._calcType = NPC_NPC;
            this._npc = attacker;
            this._targetNpc = (L1NpcInstance) target;
        }
    }

    /**
     * 魔法等级
     * 
     * @return
     */
    private int getMagicLevel() {
        final int magicLevel = this._npc.getMagicLevel();
        return magicLevel;
    }

    /**
     * 智力命中魔法追加
     * 
     * @return
     */
    private int getMagicBonus() {
        final int magicBonus = this._npc.getMagicBonus();
        return magicBonus;
    }

    /**
     * 传回正义质
     * 
     * @return
     */
    private int getLawful() {
        final int lawful = this._npc.getLawful();
        return lawful;
    }

    /**
     * ■■■■■■■■■■■■■■ 成功判定 ■■■■■■■■■■■■■ ●●●● 确率系魔法の成功判定 ●●●● 计算方法 攻击侧ポイント：LV +
     * ((MagicBonus * 3) * 魔法固有系数) 防御侧ポイント：((LV / 2) + (MR * 3)) / 2
     * 攻击成功率：攻击侧ポイント - 防御侧ポイント
     */
    @Override
    public boolean calcProbabilityMagic(final int skillId) {
        int probability = 0;
        boolean isSuccess = false;

//        if (skillId == CANCELLATION) {
//            // 对象がNPC、使用者がNPCの场合は100%成功
//            return true;
//        } //修正怪物消人百分百 hjx1000
        switch (this._calcType) {
            case NPC_PC:
                if (this._targetPc.hasSkillEffect(EARTH_BIND)) {
                    if ((skillId != WEAPON_BREAK) && (skillId != CANCELLATION)) {
                        return false;
                    }
                }
                break;

            case NPC_NPC:
                if (this._targetNpc.hasSkillEffect(EARTH_BIND)) {
                    if ((skillId != WEAPON_BREAK) && (skillId != CANCELLATION)) {
                        return false;
                    }
                }
                break;
        }

        probability = this.calcProbability(skillId);

        final int rnd = _random.nextInt(100) + 1;

        // 最大成功率90%
        probability = Math.min(probability, 90);
        if (probability >= rnd) {
            isSuccess = true;

        } else {
            isSuccess = false;
        }

        if (calcEvasion()) {
            return false;
        }
        return isSuccess;
    }

    /**
     * 魔法命中的判断
     * 
     * @param skillId
     * @return
     */
    private int calcProbability(final int skillId) {
        final L1Skills l1skills = SkillsTable.get().getTemplate(skillId);
        final int attackLevel = this._npc.getLevel();
        ;
        int defenseLevel = 0;
        int probability = 0;

        switch (this._calcType) {
            case NPC_PC:
                defenseLevel = this._targetPc.getLevel();
                break;

            case NPC_NPC:
                defenseLevel = this._targetNpc.getLevel();
                if (skillId == RETURN_TO_NATURE) {
                    if (this._targetNpc instanceof L1SummonInstance) {
                        final L1SummonInstance summon = (L1SummonInstance) this._targetNpc;
                        defenseLevel = summon.getMaster().getLevel();
                    }
                }
                break;
        }

        switch (skillId) {
            case ENTANGLE:// 地面障碍
            case WIND_SHACKLE:// 风之枷锁
            case ELEMENTAL_FALL_DOWN:// 弱化属性
            case RETURN_TO_NATURE:// 释放元素
            case ERASE_MAGIC:// 魔法消除
            case AREA_OF_SILENCE:// 封印禁地
            case STRIKER_GALE:// 精准射击
            case POLLUTE_WATER:// 污浊之水
                // 成功确率は 魔法固有系数 × LV差 + 基本确率
                probability = (int) (((l1skills.getProbabilityDice()) / 10D) * (attackLevel - defenseLevel))
                        + l1skills.getProbabilityValue();
                break;

            case EARTH_BIND:// 大地屏障
                if (attackLevel < defenseLevel) {// 攻击者等级小于被攻击者
                    probability = 3 + (this._pc.getInt() / 3);// SRC 20

                } else if (attackLevel == defenseLevel) { // 攻击者等级 等于 被攻击者
                    probability = 20 + (this._pc.getInt() / 3);// SRC 80

                } else {// 攻击者等级大于被攻击者
                    probability = 40 + (this._pc.getInt() / 3);// SRC 80
                }
                break;

            case SHOCK_STUN:// 冲击之晕
                if (attackLevel < defenseLevel) {// 攻击者等级小于被攻击者
                    probability = 22;// SRC 20

                } else if (attackLevel == defenseLevel) { // 攻击者等级 等于 被攻击者
                    probability = 40;// SRC NO

                } else {// 攻击者等级大于等于被攻击者
                    probability = 60;// SRC 80
                }
                break;

            // case SHOCK_STUN:
            case COUNTER_BARRIER:// 反击屏障
                // 成功确率は 基本确率 + LV差1每に+-1%
                probability = l1skills.getProbabilityValue() + attackLevel
                        - defenseLevel;
                break;

            case GUARD_BRAKE:// 护卫毁灭
                // case RESIST_FEAR:// 恐惧无助
            case HORROR_OF_DEATH:// 惊悚死神
                final int dice1 = l1skills.getProbabilityDice();
                final int value = l1skills.getProbabilityValue();

                final int diceCount1 = Math.max(
                        this.getMagicBonus() + this.getMagicLevel(), 1);
                /*
                 * int diceCount1 = 0; diceCount1 = this.getMagicBonus() +
                 * this.getMagicLevel();
                 * 
                 * if (diceCount1 < 1) { diceCount1 = 1; }
                 */

                for (int i = 0; i < diceCount1; i++) {
                    probability += (_random.nextInt(dice1) + 1 + value);
                }

                probability = (int) (probability * (this.getLeverage() / 10D));

                if (probability >= this.getTargetMr()) {
                    probability = 100;
                } else {
                    probability = 0;
                }
                break;

            /*
             * case SILENCE:// 魔法封印 case WEAPON_BREAK:// 坏物术 case SLOW:// 缓速术
             * final int dice3 = l1skills.getProbabilityDice(); int diceCount3 =
             * 0;
             * 
             * if (this._pc.isWizard()) { diceCount3 = this.getMagicBonus() +
             * this.getMagicLevel() + 1;
             * 
             * } else if (this._pc.isElf()) { diceCount3 = this.getMagicBonus()
             * + this.getMagicLevel() - 1;
             * 
             * } else { diceCount3 = this.getMagicBonus() + this.getMagicLevel()
             * - 1; }
             * 
             * if (diceCount3 < 1) { diceCount3 = 1; }
             * 
             * for (int i = 0; i < diceCount3; i++) { probability +=
             * (_random.nextInt(dice3) + 1); }
             * 
             * probability = probability * this.getLeverage() / 10;
             * 
             * // 智力(依职业)附加魔法命中 probability += 2 *
             * this._pc.getOriginalMagicHit();
             * 
             * // 扣除抗魔减免 probability -= this.getTargetMr();
             * 
             * // 等级差(被攻击者 - 攻击者) / 16 int levelR = defenseLevel / 24;
             * 
             * if (levelR <= 0) { levelR = 1; } probability /= levelR;
             * //System.out.println("probability:" + probability); break;
             */

            default:
                final int dice2 = l1skills.getProbabilityDice();

                final int diceCount2 = Math.max(
                        this.getMagicBonus() + this.getMagicLevel(), 1);
                /*
                 * int diceCount2 = this.getMagicBonus() + this.getMagicLevel();
                 * 
                 * if (diceCount2 < 1) { diceCount2 = 1; }
                 */

                for (int i = 0; i < diceCount2; i++) {
                    probability += (_random.nextInt(dice2) + 1);
                }

                probability = (int) (probability * (this.getLeverage() / 10D));

                probability -= this.getTargetMr();
                break;
        }

        // 耐性
        if (this._calcType == NPC_PC) {
            switch (skillId) {
                case EARTH_BIND:// 大地屏障 - 支撑耐性
                    probability -= (this._targetPc.getRegistSustain() >> 1);
                    break;

                case SHOCK_STUN:// 冲击之晕 - 昏迷耐性
                case BONE_BREAK:// 骷髅毁坏
                    // (>> 1: 除) (<< 1: 乘)
                    probability -= (this._targetPc.getRegistStun() >> 1);
                    // probability -= 2 * this._targetPc.getRegistStun();
                    break;

                case CURSE_PARALYZE:// 木乃伊的诅咒 - 石化耐性
                case PHANTASM:// 幻想
                    probability -= (this._targetPc.getRegistStone() >> 1);
                    break;

                case FOG_OF_SLEEPING:// 沉睡之雾 - 睡眠耐性
                    probability -= (this._targetPc.getRegistSleep() >> 1);
                    break;

                case ICE_LANCE:// 冰矛围篱 - 寒冰耐性
                case FREEZING_BLIZZARD:// 冰雪飓风
                case FREEZING_BREATH:// 寒冰喷吐
                    probability -= (this._targetPc.getRegistFreeze() >> 1);
                    break;

                case CURSE_BLIND:// 闇盲咒术 - 暗黑耐性
                case DARKNESS:// 黑闇之影
                case DARK_BLIND:// 暗黑盲咒
                    probability -= (this._targetPc.getRegistBlind() >> 1);
                    break;
            }

            // 被攻击者转生次数免除命中
            /*
             * int levelup = this._targetPc.get_other().get_levelup(); if
             * (levelup > 0) { probability -= (levelup << 1); }//
             */
        }

        return probability;
    }

    /**
     * 魔法伤害值计算
     * 
     * @param skillId
     * @return
     */
    @Override
    public int calcMagicDamage(final int skillId) {
        int damage = 0;
        switch (this._calcType) {
            case NPC_PC:
                damage = calcPcMagicDamage(skillId);
                break;

            case NPC_NPC:
                damage = calcNpcMagicDamage(skillId);
                break;
        }

        damage = calcMrDefense(damage);
        return damage;
    }

    /**
     * NPC对PC伤害计算
     * 
     * @param skillId
     * @return
     */
    private int calcPcMagicDamage(final int skillId) {
        if (_targetPc == null) {
            return 0;
        }

        // 伤害为0
        if (dmg0(this._targetPc)) {
            return 0;
        }

        if (!_targetPc.hasSkillEffect(attack_ing)) {
        	_targetPc.setSkillEffect(attack_ing, 120000);//检测外挂状态 hjx1000
        }
        
        int dmg = 0;
        if (skillId == FINAL_BURN) {
            dmg = this._npc.getCurrentMp();

        } else {
            dmg = this.calcMagicDiceDamage(skillId);
            dmg = (int) (dmg * (this.getLeverage() / 10D));
        }

        dmg -= _targetPc.getDamageReductionByArmor(); // 防具减伤 

        dmg -= _targetPc.dmgDowe(); // 机率伤害减免

        if (_targetPc.getClanid() != 0) {
            dmg -= getDamageReductionByClan(this._targetPc);// 血盟技能魔法伤害减免
        }

        if (this._targetPc.hasSkillEffect(REDUCTION_ARMOR)) {
            final int targetPcLvl = Math.max(this._targetPc.getLevel(), 50);
            dmg -= (targetPcLvl - 50) / 5 + 1;
        }

        boolean dmgX2 = false;// 伤害除2
        // 取回技能
        if (!this._targetPc.getSkillisEmpty()
                && this._targetPc.getSkillEffect().size() > 0) {
            try {
                for (final Integer key : this._targetPc.getSkillEffect()) {
                    final Integer integer = L1AttackList.SKD3.get(key);
                    // 伤害减免
                    if (integer != null) {
                        if (integer.equals(key)) {
                            // 技能编号与返回值相等
                            dmgX2 = true;

                        } else {
                            dmg += integer;
                        }
                    }
                }

            } catch (final ConcurrentModificationException e) {
                // 技能取回发生其他线程进行修改
            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }

//        boolean isNowWar = false;
//        final int castleId = L1CastleLocation.getCastleIdByArea(this._targetPc);
//        if (castleId > 0) {
//            isNowWar = ServerWarExecutor.get().isNowWar(castleId);
//        }
//        if (!isNowWar) {
            if (this._npc instanceof L1PetInstance) {
                // (>> 1: 除) (<< 1: 乘)
                dmg = (dmg >> 3);// dmg /= 8;
            	if (_targetPc.isSafetyZone()) {
            		dmg = 0;
            	}
            }
            if (this._npc instanceof L1SummonInstance) {
                final L1SummonInstance summon = (L1SummonInstance) this._npc;
                if (summon.isExsistMaster()) {
                    dmg = (dmg >> 3);// dmg /= 8;
                	if (_targetPc.isSafetyZone()) {
                		dmg = 0;
                	}
//                }//修改无论是否攻城区宠物和召唤物攻击都除8 hjx1000
            } 
        }
        if (dmgX2) {
            dmg = dmg >> 1;// dmg /= 2;
        }

        if (this._targetPc.hasSkillEffect(COUNTER_MIRROR)) {
            final int npcId = this._npc.getNpcTemplate().get_npcId();
            switch (npcId) {
                case 45681:// 林德拜尔
                case 45682:// 安塔瑞斯
                case 45683:// 法利昂
                case 45684:// 巴拉卡斯
                case 91161:// 纪元水龙的影像
                case 91159:// 纪元地龙的影像
                case 91160:// 纪元火龙的影像
                case 91162:// 纪元风龙的影像
                case 71014:// 新安塔瑞斯(1阶段)
                case 71015:// 新安塔瑞斯(2阶段)
                case 71016:// 新安塔瑞斯(3阶段)
                case 71026:// 新法利昂(1阶段)
                case 71027:// 新法利昂(2阶段)
                case 71028:// 新法利昂(3阶段)
                    // 不接受镜反射攻击
                    break;

                default:
                    if (!this._npc.getNpcTemplate().get_IsErase()) {

                    } else {
                        if (this._targetPc.getWis() >= _random.nextInt(100)) {
                            this._npc.broadcastPacketX10(new S_DoActionGFX(
                                    this._npc.getId(),
                                    ActionCodes.ACTION_Damage));
                            this._targetPc.sendPacketsX8(new S_SkillSound(
                                    this._targetPc.getId(), 4395));
                            this._npc.receiveDamage(this._targetPc, dmg);
                            dmg = 0;
                            this._targetPc.killSkillEffectTimer(COUNTER_MIRROR);
                        }
                    }
                    break;
            }
        }
        int dmgOut = Math.max(dmg, 0);
        // System.out.println("NPC对PC伤害计算 010:"+dmgOut);
//        if (!_targetPc.isHardDelay()) { //动作延时 hjx1000
//        	HardDelay.onHardUse(_targetPc, 150);
//        }

        return dmgOut;
    }

    /**
     * プレイヤー?ＮＰＣ から ＮＰＣ へのダメージ算出
     * 
     * @param skillId
     * @return
     */
    private int calcNpcMagicDamage(final int skillId) {
        if (_targetNpc == null) {
            return 0;
        }
        // 伤害为0
        if (dmg0(_targetNpc)) {
            return 0;
        }

        int dmg = 0;
        if (skillId == FINAL_BURN) {
            dmg = this._npc.getCurrentMp();

        } else {
            dmg = this.calcMagicDiceDamage(skillId);
            dmg = (int) (dmg * (this.getLeverage() / 10D));
        }

        return dmg;
    }

    /**
     * damage_dice、damage_dice_count、damage_value、SPから魔法ダメージを算出
     * 
     * @param skillId
     * @return
     */
    private int calcMagicDiceDamage(final int skillId) {
        final L1Skills l1skills = SkillsTable.get().getTemplate(skillId);
        final int dice = l1skills.getDamageDice();
        final int diceCount = l1skills.getDamageDiceCount();
        final int value = l1skills.getDamageValue();
        int magicDamage = 0;
        int charaIntelligence = 0;

        for (int i = 0; i < diceCount; i++) {
            magicDamage += (_random.nextInt(dice) + 1);
        }
        magicDamage += value;

        final int spByItem = this.getTargetSp();// this._npc.getSp() -
                                                // this._npc.getTrueSp(); //
                                                // アイテムによるSP变动
        charaIntelligence = Math.max(this._npc.getInt() + spByItem - 12, 1);
        /*
         * charaIntelligence = this._npc.getInt() + spByItem - 12;
         * 
         * if (charaIntelligence < 1) { charaIntelligence = 1; }
         */

        final double attrDeffence = this.calcAttrResistance(l1skills.getAttr());

        final double coefficient = Math.max(
                (1.0 - attrDeffence + charaIntelligence * 3.0 / 32.0), 0.0);
        /*
         * double coefficient = (1.0 - attrDeffence + charaIntelligence * 3.0 /
         * 32.0); if (coefficient < 0) { coefficient = 0; }
         */

        magicDamage *= coefficient;

        return magicDamage;
    }

    /**
     * ヒール回复量（对アンデッドにはダメージ）を算出
     * 
     * @param skillId
     * @return
     */
    @Override
    public int calcHealing(final int skillId) {
        final L1Skills l1skills = SkillsTable.get().getTemplate(skillId);
        final int dice = l1skills.getDamageDice();
        final int value = l1skills.getDamageValue();
        int magicDamage = 0;

        int magicBonus = Math.min(this.getMagicBonus(), 10);

        /*
         * int magicBonus = this.getMagicBonus(); if (magicBonus > 10) {
         * magicBonus = 10; }
         */

        final int diceCount = value + magicBonus;
        for (int i = 0; i < diceCount; i++) {
            magicDamage += (_random.nextInt(dice) + 1);
        }

        double alignmentRevision = 1.0;
        if (this.getLawful() > 0) {
            alignmentRevision += (this.getLawful() / 32768.0);
        }

        magicDamage *= alignmentRevision;

        magicDamage = (int) (magicDamage * (this.getLeverage() / 10D));

        return magicDamage;
    }

    /**
     * ＭＲ魔法伤害减轻
     * 
     * @param dmg
     * @return
     */
    private int calcMrDefense(int dmg) {
        // 取回目标抗魔
        final int mr = this.getTargetMr();

        double mrFloor = 0;
        double mrCoefficient = 0;

        if (mr == 0) {
            mrFloor = 1;
            mrCoefficient = 1;

        } else if (mr > 0 && mr <= 50) {
            mrFloor = 2;
            mrCoefficient = 1;

        } else if (mr > 50 && mr <= 100) {
            mrFloor = 3;
            mrCoefficient = 0.9;

        } else if (mr > 100 && mr <= 120) {
            mrFloor = 4;
            mrCoefficient = 0.9;

        } else if (mr > 120 && mr <= 140) {
            mrFloor = 5;
            mrCoefficient = 0.8;

        } else if (mr > 140 && mr <= 160) {
            mrFloor = 6;
            mrCoefficient = 0.8;

        } else if (mr > 160 && mr <= 180) {
            mrFloor = 7;
            mrCoefficient = 0.7;

        } else if (mr > 180 && mr <= 200) {
            mrFloor = 8;
            mrCoefficient = 0.7;

        } else if (mr > 200 && mr <= 220) {
            mrFloor = 9;
            mrCoefficient = 0.6;

        } else if (mr > 220 && mr <= 240) {
            mrFloor = 10;
            mrCoefficient = 0.6;

        } else if (mr > 240) {
            mrFloor = 11;
            mrCoefficient = 0.5;
        }

        // 取回NPC智力
        int originalInt = this._npc.getInt();
        int originalMagicHit = 1;

        if (originalInt < 18) {
            originalMagicHit = 1;

        } else if (originalInt >= 18 && originalInt < 36) {
            originalMagicHit = 2;

        } else if (originalInt >= 36 && originalInt < 72) {
            originalMagicHit = 3;

        } else if (originalInt >= 72) {
            originalMagicHit = 4;
        }

        // 计算减低
        dmg *= (mrCoefficient - (0.01 * Math.floor((mr - originalMagicHit)
                / mrFloor)));

        /*
         * final int mr = this.getTargetMr();
         * 
         * final int rnd = _random.nextInt(100) + 1; if (mr >= rnd) { // (>> 1:
         * 除) (<< 1: 乘) dmg = dmg >> 1;//dmg /= 2; }
         */

        return dmg;
    }

    /**
     * 计算结果反映
     * 
     * @param damage
     * @param drainMana
     */
    @Override
    public void commit(int damage, final int drainMana) {

        // BOSS XXX
        if (_npc.getNpcTemplate().is_boss()) {
            damage *= 1.25;
        }
        switch (this._calcType) {
            case NPC_PC:
                this.commitPc(damage, drainMana);
                break;

            case NPC_NPC:
                this.commitNpc(damage, drainMana);
                break;
        }
        // GM攻击讯息
        if (!ConfigAlt.ALT_ATKMSG) {
            return;

        } else {
            if (this._calcType == NPC_NPC) {
                return;
            }
            if (!this._targetPc.isGm()) {
                return;
            }
        }

        final StringBuilder atkMsg = new StringBuilder();
        atkMsg.append("受到NPC技能: ");
        atkMsg.append(this._npc.getNameId() + ">");// 攻击者
        atkMsg.append(this._targetPc.getName() + " ");// 被攻击者
        atkMsg.append("伤害: " + damage);// 资讯
        // 166 \f1%0%s %4%1%3 %2。
        this._targetPc.sendPackets(new S_ServerMessage(166, atkMsg.toString()));

        // final String srcatk = this._targetPc.getName();
        // final String hitinfo = "目标HP:" + this._targetPc.getCurrentHp();
        // final String dmginfo = "伤害: " + damage;
        // final String tgatk = this._targetPc.getName();
        // final String x = srcatk + ">" + tgatk + " " + dmginfo + hitinfo;
        // this._targetPc.sendPackets(new S_ServerMessage(166, "受到NPC技能: " +
        // x));
    }

    /**
     * 对pc伤害的输出
     * 
     * @param damage
     * @param drainMana
     */
    private void commitPc(final int damage, final int drainMana) {
        try {
            this._targetPc.receiveDamage(this._npc, damage, true, false);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 对npc伤害的输出
     * 
     * @param damage
     * @param drainMana
     */
    private void commitNpc(final int damage, final int drainMana) {
        try {
            this._targetNpc.receiveDamage(this._npc, damage);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
