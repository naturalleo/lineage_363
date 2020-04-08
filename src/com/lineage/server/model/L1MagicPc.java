package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.*;

import java.util.ConcurrentModificationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.server.ActionCodes;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_TrueTarget;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.timecontroller.pc.HardDelay;
import com.lineage.server.timecontroller.server.ServerWarExecutor;

/**
 * 魔法攻击判定(PC)
 * 
 * @author daien
 * 
 */
public class L1MagicPc extends L1MagicMode {

    private static final Log _log = LogFactory.getLog(L1MagicPc.class);

    /**
     * 魔法攻击判定(PC)
     * 
     * @param attacker
     * @param target
     */
    public L1MagicPc(final L1PcInstance attacker, final L1Character target) {
        if (attacker == null) {
            return;
        }

        _pc = attacker;

        if (target instanceof L1PcInstance) {
            _calcType = PC_PC;
            _targetPc = (L1PcInstance) target;

        } else {
            _calcType = PC_NPC;
            _targetNpc = (L1NpcInstance) target;
        }

    }

    /**
     * 职业魔法等级
     * 
     * @return
     */
    private int getMagicLevel() {
        return _pc.getMagicLevel();
    }

    /**
     * 智力命中魔法追加
     * 
     * @return
     */
    private int getMagicBonus() {
        return _pc.getMagicBonus();
    }

    /**
     * 传回正义质
     * 
     * @return
     */
    private int getLawful() {
        return _pc.getLawful();
    }

    /**
     * 攻击成功的判断 ●●●● 确率系魔法の成功判定 ●●●● 计算方法 攻击侧ポイント：LV + ((MagicBonus * 3) *
     * 魔法固有系数) 防御侧ポイント：((LV / 2) + (MR * 3)) / 2 攻击成功率：攻击侧ポイント - 防御侧ポイント
     */
    @Override
    public boolean calcProbabilityMagic(final int skillId) {
        int probability = 0;// 魔法成功机率
        boolean isSuccess = false;
        if (_pc.hasSkillEffect(attack_fanlse)) { //添加攻击无效状态 hjx1000
        	return false;
        }
        switch (_calcType) {
            case PC_PC:
                // 魔法相消术
                if (skillId == CANCELLATION) {
                    if (_pc != null && _targetPc != null) {
                        // 对象为自己100%成功
                        if (_pc.getId() == _targetPc.getId()) {
                            return true;
                        }

                        // 相同血盟100%成功
                        if (_pc.getClanid() > 0) {
                            if (_pc.getClanid() == _targetPc.getClanid()) {
                                return true;
                            }
                        }

                        // 相同队伍100%成功
                        if (_pc.isInParty()) {
                            if (_pc.getParty().isMember(_targetPc)) {
                                return true;
                            }
                        }
                    }
                }

                // 攻击者 或是 被攻击者 在安全区内
                if (!checkZone(skillId)) {
                    return false;
                }

                // 被攻击者受到大地屏障
                if (_targetPc.hasSkillEffect(EARTH_BIND)) {
                    // 被大地冻结时魔法相消术百分百中 hjx1000
//                    if (skillId == CANCELLATION) {
//                        return true;
//                    }
                    // 施展法术不是坏物术或魔法相消术
                    if ((skillId != WEAPON_BREAK) && (skillId != CANCELLATION)) {
                        return false;
                    }
                }

                // 回避
                if (calcEvasion()) {
                    return false;
                }
                break;

            case PC_NPC:
                if (_targetNpc != null) {
                    // 对不可见的怪物额外判断
                    final int gfxid = this._targetNpc.getNpcTemplate()
                            .get_gfxid();
                    switch (gfxid) {
                        case 2412:// 南瓜的影子
                            if (!_pc.getInventory().checkEquipped(20046)) {// 南瓜帽
                                return false;
                            }
                            break;
                    }
                    // NPC需附加技能可攻击
                    final int npcId = _targetNpc.getNpcTemplate().get_npcId();
                    final Integer tgskill = L1AttackList.SKNPC.get(npcId);
                    if (tgskill != null) {
                        if (!_pc.hasSkillEffect(tgskill)) {
                            return false;
                        }
                    }

                    // NPC指定外型不可攻击
                    final Integer tgpoly = L1AttackList.PLNPC.get(npcId);
                    if (tgpoly != null) {
                        if (tgpoly.equals(_pc.getTempCharGfx())) {
                            return false;
                        }
                    }

                    // NPC抵抗技能(NPCID / 技能编号) 列表中该技能对该NPC施展失败
                    final boolean dgskill = L1AttackList.DNNPC
                            .containsKey(npcId);
                    if (dgskill) {
                        Integer[] dgskillids = L1AttackList.DNNPC.get(npcId);
                        for (Integer dgskillid : dgskillids) {
                            if (dgskillid.equals(skillId)) {
                                return false;
                            }
                        }
                    }
                }

                // 魔法相消术
                if (skillId == CANCELLATION) {
                    return true;
                }

                // 被攻击者受到大地屏障
                if (this._targetNpc.hasSkillEffect(EARTH_BIND)) {
                    // 施展法术不是坏物术或魔法相消术
                    if ((skillId != WEAPON_BREAK) && (skillId != CANCELLATION)) {
                        return false;
                    }
                }
                break;
        }

        // 计算魔法成功机率
        probability = calcProbability(skillId);

        // 法师提高机率 +智力/4 (>> 1: 除) (<< 1: 乘)
        /*
         * if (_pc.isWizard()) { probability += _pc.getInt() / 4;//
         * 2012-05-12(9) }
         */

        final int rnd = _random.nextInt(100) + 1;

        // 最大成功率90%
        probability = Math.min(probability, 90);
        // 最小成功率1%
        probability = Math.max(probability, 1);

        if (probability >= rnd) {
            isSuccess = true;

        } else {
            isSuccess = false;
        }

        // gm攻击资讯
        if (!ConfigAlt.ALT_ATKMSG) {
            return isSuccess;

        } else {
            switch (_calcType) {
                case PC_PC:
                    if (!_pc.isGm()) {
                        if (!_targetPc.isGm()) {
                            return isSuccess;
                        }
                    }
                    break;

                case PC_NPC:
                    if (!_pc.isGm()) {
                        return isSuccess;
                    }
                    break;
            }
        }

        switch (_calcType) {
            case PC_PC:
                if (_pc.isGm()) {
                    final StringBuilder atkMsg = new StringBuilder();
                    atkMsg.append("对PC送出技能: ");
                    atkMsg.append(_pc.getName() + ">");// 攻击者
                    atkMsg.append(_targetPc.getName() + " ");// 被攻击者
                    atkMsg.append(isSuccess ? "成功" : "失败");// 资讯
                    atkMsg.append(" 成功机率:" + probability + "%");// 最终资讯
                    // 166 \f1%0%s %4%1%3 %2。
                    _pc.sendPackets(new S_ServerMessage(166, atkMsg.toString()));
                }
                if (this._targetPc.isGm()) {
                    final StringBuilder atkMsg = new StringBuilder();
                    atkMsg.append("受到PC技能: ");
                    atkMsg.append(_pc.getName() + ">");// 攻击者
                    atkMsg.append(_targetPc.getName() + " ");// 被攻击者
                    atkMsg.append(isSuccess ? "成功" : "失败");// 资讯
                    atkMsg.append(" 成功机率:" + probability + "%");// 最终资讯
                    // 166 \f1%0%s %4%1%3 %2。
                    _targetPc.sendPackets(new S_ServerMessage(166, atkMsg
                            .toString()));
                }
                break;

            case PC_NPC:
                if (_pc.isGm()) {
                    final StringBuilder atkMsg = new StringBuilder();
                    atkMsg.append("对NPC送出技能: ");
                    atkMsg.append(_pc.getName() + ">");// 攻击者
                    atkMsg.append(_targetNpc.getName() + " ");// 被攻击者
                    atkMsg.append(isSuccess ? "成功" : "失败");// 资讯
                    atkMsg.append(" 成功机率:" + probability + "%");// 最终资讯
                    // 166 \f1%0%s %4%1%3 %2。
                    _pc.sendPackets(new S_ServerMessage(166, atkMsg.toString()));
                }
                break;
        }
        return isSuccess;
    }

    /**
     * 攻击者 或是 被攻击者 在安全区内
     * 
     * @param skillId
     * @return
     */
    private boolean checkZone(final int skillId) {
        if ((_pc != null) && (_targetPc != null)) {
            // 攻击者 或是 被攻击者 在安全区内
            if (_pc.isSafetyZone() || _targetPc.isSafetyZone()) {
                // 施展技能限制安全区域无法使用
                final Boolean isBoolean = L1AttackList.NZONE.get(skillId);
                if (isBoolean != null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 魔法命中的判断
     * 
     * @param skillId
     * @return
     */
    private int calcProbability(final int skillId) {
        final L1Skills l1skills = SkillsTable.get().getTemplate(skillId);
        final int attackLevel = _pc.getLevel();// 攻击者等级
        int defenseLevel = 0;// 对手等级
        int probability = 0;// 输出机率

        switch (this._calcType) {
            case PC_PC:
    			/*if (_targetPc.isGm()) {
				return -1;
				}*/ //关掉方便测试 hjx1000
                defenseLevel = _targetPc.getLevel();
                break;

            case PC_NPC:
                defenseLevel = _targetNpc.getLevel();
                if (skillId == RETURN_TO_NATURE) {
                    if (_targetNpc instanceof L1SummonInstance) {
                        final L1SummonInstance summon = (L1SummonInstance) _targetNpc;
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
            case POLLUTE_WATER:// 污浊之水
                // 成功确率は 魔法固有系数 × LV差 + 基本确率
                probability = (int) (((l1skills.getProbabilityDice()) / 10D) * (attackLevel - defenseLevel))
                        + l1skills.getProbabilityValue();
                // 追加2倍智力影响(>> 1: 除) (<< 1: 乘)
                probability += (_pc.getOriginalMagicHit() << 1);
                probability -= getTargetMr() / 80;
                break;

            case AREA_OF_SILENCE:// 封印禁地
            case STRIKER_GALE:// 精准射击
                // 成功确率は 魔法固有系数 × LV差 + 基本确率
                probability = (int) (((l1skills.getProbabilityDice()) / 10D) * (attackLevel - defenseLevel))
                        + l1skills.getProbabilityValue();
                // 追加2倍智力影响(>> 1: 除) (<< 1: 乘)
                probability += (_pc.getOriginalMagicHit() << 1);
                break;

            case ERASE_MAGIC:// 魔法消除
                // 成功确率 魔法固有系数 × LV差 + 基本确率
                probability = (int) (((l1skills.getProbabilityDice()) / 10D) * (attackLevel - defenseLevel))
                        + l1skills.getProbabilityValue();
                // 追加2倍智力影响(>> 1: 除) (<< 1: 乘)
                probability += (_pc.getOriginalMagicHit() << 1);
                probability -= getTargetMr() / 80;
                break;
            case EARTH_BIND:// 大地屏障
    			// 追加智力影響(>> 1: 除)  (<< 1: 乘)
    			//probability += l1skills.getProbabilityValue();//DB基数控制机率 hjx1000
    			if (attackLevel < defenseLevel) {// 攻擊者等級小於被攻擊者
    				probability = 5 + (_pc.getInt() >> 3);// /8

    			} else if (attackLevel == defenseLevel) { // 攻擊者等級 等於 被攻擊者
    				probability = 15 + (_pc.getInt() >> 2);// /4
    							
    			} else {// 攻擊者等級大於被攻擊者
    				probability = 30 + (_pc.getInt() >> 1);// /2
    			}
    			probability += attackLevel - defenseLevel;
                break;

            case SHOCK_STUN:// 冲击之晕
            case DRESS_HALZ:// 黑暗籠罩	
            case THUNDER_GRAB://夺命之雷 add hjx1000	
    			/*if (attackLevel < defenseLevel) {// 攻擊者等級小於被攻擊者
					probability = 22;// SRC 20
				
				} else if (attackLevel == defenseLevel) { // 攻擊者等級 等於 被攻擊者
					probability = 40;// SRC NO
				
				} else {// 攻擊者等級大於等於被攻擊者
					probability = 60;// SRC 80
				}*/
            	probability = l1skills.getProbabilityValue() + (attackLevel - defenseLevel) * 5; //冲晕机率等于 45+等级差*5 set hjx1000
                break;

            case COUNTER_BARRIER:// 反击屏障
                // 成功机率 基本确率 + LV差1每 +-1%
                probability = l1skills.getProbabilityValue() + attackLevel
                        - defenseLevel;
                // 追加2倍智力影响(>> 1: 除) (<< 1: 乘)
                probability += (_pc.getOriginalMagicHit() << 1);
                break;

            case PANIC:// 恐慌
                // 成功机率 基本确率 + LV差1每 +-1%
                probability = l1skills.getProbabilityValue() + attackLevel
                        - defenseLevel << 4;
                // 追加2倍智力影响 (>> 1: 除) (<< 1: 乘)
                probability += _pc.getOriginalMagicHit();
                break;

            case PHANTASM:// 幻想
                final int rnd = _random.nextInt(100) + 1;
                if (rnd <= 30) {
                    probability = 100;

                } else {
                    probability = 0;
                }
                break;

            case CONFUSION:// 混乱
                final int rad = _random.nextInt(100) + 1;
                final int trad = _random.nextInt(10) + 20;
                if (rad <= trad) {
                    probability = 100;

                } else {
                    probability = 0;
                }
                break;

            case RESIST_FEAR:// 恐惧无助
                final int dice4 = l1skills.getProbabilityDice();
                int diceCount4 = Math.max(
                        getMagicBonus() + getMagicLevel() + 1, 1);

                for (int i = 0; i < diceCount4; i++) {
                    probability += (_random.nextInt(dice4) + 1);
                }

                probability = (int) (probability * (getLeverage() / 10D));
                // System.out.println("probability:" +probability +
                // " getTargetMr:"+getTargetMr());
                // 智力(依职业)附加魔法命中
                probability += 2 * _pc.getOriginalMagicHit();
                // System.out.println("智力(依职业)附加魔法命中:" +probability +
                // " getTargetMr:"+getTargetMr());

                // 扣除抗魔减免
                probability -= getTargetMr();
                // System.out.println("扣除抗魔减免:" +probability +
                // " getTargetMr:"+getTargetMr());

                // 等级差(被攻击者 - 攻击者) / 24
                int levelR1 = Math.max(defenseLevel / 20, 1);// 2011-11-26 24

                probability /= levelR1;
                // System.out.println("等级差(被攻击者 - 攻击者) / 24:" +probability +
                // " getTargetMr:"+getTargetMr());
                break;

            case GUARD_BRAKE:// 护卫毁灭
            case HORROR_OF_DEATH:// 惊悚死神
                final int dice1 = l1skills.getProbabilityDice();
                final int value = l1skills.getProbabilityValue();
                final int diceCount1 = Math.max(getMagicBonus()
                        + getMagicLevel(), 1);

                for (int i = 0; i < diceCount1; i++) {
                    probability += (_random.nextInt(dice1) + 1 + value);
                }

                probability = (int) (probability * (getLeverage() / 10D));
                // System.out.println("probability:" +probability +
                // " getTargetMr:"+getTargetMr());

                // 追加2倍智力影响 (>> 1: 除) (<< 1: 乘)
                probability += (_pc.getOriginalMagicHit() << 1);
                // System.out.println("追加2倍智力影响:" +probability +
                // " getTargetMr:"+getTargetMr());

                if (probability >= getTargetMr()) {
                    probability = 100;

                } else {
                    probability = 0;
                }
                break;

            // case RESIST_FEAR:// 恐惧无助*/
            case SILENCE:// 魔法封印
            case WEAPON_BREAK:// 坏物术
            case SLOW:// 缓速术
            	probability += l1skills.getProbabilityValue();//补上DB基础机率 add hjx1000
                final int dice3 = l1skills.getProbabilityDice();
                int diceCount3 = 0;

                if (_pc.isWizard()) {
                    diceCount3 = getMagicBonus() + getMagicLevel() + 1;

                } else {
                    diceCount3 = getMagicBonus() + getMagicLevel() - 1;
                }

                diceCount3 = Math.max(diceCount3, 1);

                for (int i = 0; i < diceCount3; i++) {
                    probability += (_random.nextInt(dice3) + 1);
                }

                probability = (int) (probability * (getLeverage() / 10D));

                // 智力(依职业)附加魔法命中
                probability += 2 * _pc.getOriginalMagicHit();

                // 扣除抗魔减免
                probability -= getTargetMr();

                // 等级差(被攻击者 - 攻击者) / 24
                int levelR = Math.max(defenseLevel / 24, 1);

                probability /= levelR;
                
                break;

            case ICE_LANCE:// 冰矛围篱 - 寒冰耐性
                // 取回技能计算机率
                final int diceICE = l1skills.getProbabilityDice();
                int diceCountICE = Math.max(getMagicBonus() + getMagicLevel()
                        + 1, 1);

                for (int i = 0; i < diceCountICE; i++) {
                    if (diceICE > 0) {
                        probability += (_random.nextInt(diceICE) + 1);
                    }
                }

                probability = (int) (probability * (getLeverage() / 10D));

                // (>> 1: 除) (<< 1: 乘)
                probability += (_pc.getOriginalMagicHit() << 1);

                probability -= getTargetMr();
                break;

            default:
                // 取回技能计算机率
                final int dice2 = l1skills.getProbabilityDice();
                int diceCount2 = 0;

                if (_pc.isWizard()) {
                    diceCount2 = getMagicBonus() + getMagicLevel() + 1;

                } else if (_pc.isElf()) {
                    diceCount2 = getMagicBonus() + getMagicLevel() - 1;

                } else {
                    diceCount2 = getMagicBonus() + getMagicLevel() - 1;
                }

                diceCount2 = Math.max(diceCount2, 1);

                for (int i = 0; i < diceCount2; i++) {
                    if (dice2 > 0) {
                        probability += (_random.nextInt(dice2) + 1);
                    }
                }

                probability = (int) (probability * (getLeverage() / 10D));

                // 智力(依职业)附加魔法命中 *2 (>> 1: 除) (<< 1: 乘)
                probability += (_pc.getOriginalMagicHit() << 1);

                probability -= getTargetMr();
                
                probability -= Math.max((defenseLevel - attackLevel) << 3 , 0);//对于比自己等级高的目标每高一级机率减8 hjx1000
                	
                if (skillId == TAMING_MONSTER) {
                    double probabilityRevision = 1;
                    if (((_targetNpc.getMaxHp()) >> 2) > _targetNpc
                            .getCurrentHp()) {
                        probabilityRevision = 1.3;

                    } else if (((_targetNpc.getMaxHp() << 2) >> 2) > _targetNpc
                            .getCurrentHp()) {
                        probabilityRevision = 1.2;

                    } else if (((_targetNpc.getMaxHp() * 3) >> 2) > _targetNpc
                            .getCurrentHp()) {
                        probabilityRevision = 1.1;
                    }
                    probability *= probabilityRevision;
                }
                break;
        }

        // 耐性 (>> 1: 除) (<< 1: 乘)
        switch (_calcType) {
            case PC_PC:
                switch (skillId) {
                    case THUNDER_GRAB: //夺命之雷 
                    case EARTH_BIND:// 大地屏障 - 支撑耐性
                        probability -= (_targetPc.getRegistSustain() >> 1);
                        break;

                    case SHOCK_STUN:// 冲击之晕 - 昏迷耐性
                    case BONE_BREAK:// 骷髅毁坏
                        probability -= (_targetPc.getRegistStun() >> 1);
                        break;

                    case CURSE_PARALYZE:// 木乃伊的诅咒 - 石化耐性
                    case PHANTASM:// 幻想
                        probability -= (_targetPc.getRegistStone() >> 1);
                        break;

                    case FOG_OF_SLEEPING:// 沉睡之雾 - 睡眠耐性
                        probability -= (_targetPc.getRegistSleep() >> 1);
                        break;

                    case ICE_LANCE:// 冰矛围篱 - 寒冰耐性
                    case FREEZING_BLIZZARD:// 冰雪飓风
                    case FREEZING_BREATH:// 寒冰喷吐
                        probability -= (_targetPc.getRegistFreeze() >> 1);
                        break;

                    case CURSE_BLIND:// 闇盲咒术 - 暗黑耐性
                    case DARKNESS:// 黑闇之影
                    case DARK_BLIND:// 暗黑盲咒
                        probability -= (_targetPc.getRegistBlind() >> 1);
                        break;
                }
                break;
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
        if (_pc.hasSkillEffect(attack_fanlse)) { //添加攻击无效状态 hjx1000
        	return 0;
        }
        switch (_calcType) {
            case PC_PC:
                damage = calcPcMagicDamage(skillId);
                showNum(damage, true);
                break;

            case PC_NPC:
                damage = calcNpcMagicDamage(skillId);
                showNum(damage, false);
                break;
        }

        damage = calcMrDefense(damage);
        return damage;
    }

	/*
	 * 飘血功能 by:
	 */
	private void showNum(final int _damage, final boolean isPc){
		//if(ConfigOther.SHOWNUM){
			int targetid=0;
			if(isPc){
				targetid = _targetPc.getId();
			}else{
				targetid = _targetNpc.getId();
			}
			if(_damage<=0){
				_pc.sendPackets(new S_SkillSound(targetid, 2040));	//MISS
			}else{
				int units = _damage%10+2010;
				int tens = (_damage/10)%10+2020;
				int hundreads =(_damage/100)%10+2030;
				
				if(false){// 全屏显示
					if(isPc){
						if(_damage>999){
							//不显示4位数的变态攻击！
						}else if(_damage>99){
							_targetPc.sendPackets(new S_SkillSound(targetid, hundreads));	//百
							_targetPc.sendPackets(new S_SkillSound(targetid, tens));			//十
							_targetPc.sendPackets(new S_SkillSound(targetid, units));		//个
							
							_pc.sendPackets(new S_SkillSound(targetid, hundreads));	//百
							_pc.sendPackets(new S_SkillSound(targetid, tens));			//十
							_pc.sendPackets(new S_SkillSound(targetid, units));		//个
							
						}else if(_damage>9){
							_targetPc.sendPackets(new S_SkillSound(targetid, tens));			//十
							_targetPc.sendPackets(new S_SkillSound(targetid, units));		//个
							
							_pc.sendPackets(new S_SkillSound(targetid, tens));			//十
							_pc.sendPackets(new S_SkillSound(targetid, units));		//个
						}else if(_damage>0){
							_targetPc.sendPackets(new S_SkillSound(targetid, _damage+2010));		//个
							
							_pc.sendPackets(new S_SkillSound(targetid, units));		//个
						}
					}else{
						if(_damage>999){
							//不显示4位数的变态攻击！
						}else if(_damage>99){
							_targetNpc.broadcastPacketX10(new S_SkillSound(targetid, hundreads));	//百
							_targetNpc.broadcastPacketX10(new S_SkillSound(targetid, tens));			//十
							_targetNpc.broadcastPacketX10(new S_SkillSound(targetid, units));		//个
						}else if(_damage>9){
							_targetNpc.broadcastPacketX10(new S_SkillSound(targetid, tens));			//十
							_targetNpc.broadcastPacketX10(new S_SkillSound(targetid, units));		//个
						}else if(_damage>0){
							_targetNpc.broadcastPacketX10(new S_SkillSound(targetid, _damage+2010));		//个
						}
					}
				}else{	//飘血仅自己可见
					if(_damage>999){
						//不显示4位数的变态攻击！
					}else if(_damage>99){
						_pc.sendPackets(new S_SkillSound(targetid, hundreads));	//百
						_pc.sendPackets(new S_SkillSound(targetid, tens));			//十
						_pc.sendPackets(new S_SkillSound(targetid, units));		//个
					}else if(_damage>9){
						_pc.sendPackets(new S_SkillSound(targetid, tens));			//十
						_pc.sendPackets(new S_SkillSound(targetid, units));		//个
					}else if(_damage>0){
						_pc.sendPackets(new S_SkillSound(targetid, _damage+2010));		//个
					}
				}
			}
	}
    
    /**
     * PC对PC魔法伤害计算
     * 
     * @param skillId
     * @return
     */
    private int calcPcMagicDamage(final int skillId) {
        if (_targetPc == null) {
            return 0;
        }
        // 伤害为0
        if (dmg0(_targetPc)) {
            return 0;
        }

        int dmg = 0;
        if (skillId == FINAL_BURN) {
            dmg = _pc.getCurrentMp();

        } else {
            dmg = calcMagicDiceDamage(skillId);
            dmg = (int) (dmg * (getLeverage() / 10D));
        }

        //魔杖超安定值增加魔法伤害及星武器额外增加魔法伤害 hjx1000
    	int weaponEnchant = 0;
    	int weaponType2 = 0;
    	int weaponAddSp = 0;
        final L1ItemInstance _weapon = _pc.getWeapon();
        if (_weapon != null) {
        	weaponEnchant = _weapon.getEnchantLevel();
        	weaponType2 = _weapon.getItem().getType1();
        	weaponAddSp = _weapon.getItem().get_addsp();
        	if (weaponType2 == 40 &&  weaponEnchant > _weapon.getItem().get_safeenchant()) {
        		final int i = weaponEnchant - _weapon.getItem().get_safeenchant();
        		dmg *= (i + weaponAddSp) * 0.03 + 1;
        	}
        	if (_weapon.get_power_name() != null) {
            	switch (_weapon.get_power_name().get_xing_count()) {
            	case 1:
            		dmg *= 1.1;
            		break;
            	case 2:
            		dmg *= 1.2;
            		break;
            	case 3:
            		dmg *= 1.3;
            		break;
            	case 4:
            		dmg *= 1.4;
            		break;
            	case 5:
            		dmg *= 1.5;
            		break;
            	case 6:
            		dmg *= 1.6;
            		break;
            	case 7:
            		dmg *= 1.7;
            		break;
            	case 8:
            		dmg *= 1.8;
            		break;
            	case 9:
            		dmg *= 1.9;
            		break;
            	}
        	}
        }
        
        dmg -= _targetPc.getDamageReductionByArmor(); // 防具减伤 

        dmg -= _targetPc.dmgDowe(); // 机率伤害减免

        if (_targetPc.getClanid() != 0) {
            dmg -= getDamageReductionByClan(_targetPc);// 血盟技能魔法伤害减免
        }

        if (_targetPc.hasSkillEffect(REDUCTION_ARMOR)) {
            int targetPcLvl = Math.max(_targetPc.getLevel(), 50);
            dmg -= (targetPcLvl - 50) / 5 + 1;
        }

        boolean dmgX2 = false;// 伤害除2
        // 取回技能
        if (!_targetPc.getSkillisEmpty()
                && _targetPc.getSkillEffect().size() > 0) {
            try {
                for (final Integer key : _targetPc.getSkillEffect()) {
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

        if (dmgX2) {
            dmg = (dmg >> 1);// dmg /= 2;
        }

        // 技能镜反射
        if (_targetPc.hasSkillEffect(COUNTER_MIRROR)) {
            if (_calcType == PC_PC) {
                if (_targetPc.getWis() >= _random.nextInt(100)) {
                    _pc.sendPacketsAll(new S_DoActionGFX(_pc.getId(),
                            ActionCodes.ACTION_Damage));
                    if (!_pc.isHardDelay()) { //动作延时 hjx1000
                    	HardDelay.onHardUse(_pc, 150);
                    }
                    _targetPc.sendPacketsX8(new S_SkillSound(_targetPc.getId(),
                            4395));
                    _pc.receiveDamage(_targetPc, dmg, false, false);
                    dmg = 0;
                    _targetPc.killSkillEffectTimer(COUNTER_MIRROR);
                }
            }
        }
        if (!_targetPc.isHardDelay()) { //动作延时 hjx1000
        	HardDelay.onHardUse(_targetPc, 150);
        }
        return Math.max(dmg, 0);
    }

    /**
     * PC对NPC魔法伤害计算
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
        if (!_pc.hasSkillEffect(attack_ing)) {
        	_pc.setSkillEffect(attack_ing, 120000);//检测外挂状态 hjx1000
        }
        final int npcId = _targetNpc.getNpcTemplate().get_npcId();
        final Integer tgskill = L1AttackList.SKNPC.get(npcId);
        if (tgskill != null) {
            if (!_pc.hasSkillEffect(tgskill)) {
                return 0;
            }
        }

        final Integer tgpoly = L1AttackList.PLNPC.get(npcId);
        if (tgpoly != null) {
            if (tgpoly.equals(_pc.getTempCharGfx())) {
                return 0;
            }
        }

        int dmg = 0;
        if (skillId == FINAL_BURN) {
            dmg = _pc.getCurrentMp();

        } else {
            dmg = calcMagicDiceDamage(skillId);
            dmg = (int) (dmg * (getLeverage() / 10D));
        }

        //魔杖超安定值增加魔法伤害及星武器额外增加魔法伤害 hjx1000
    	int weaponEnchant = 0;
    	int weaponType2 = 0;
    	int weaponAddSp = 0;
        final L1ItemInstance _weapon = _pc.getWeapon();
        if (_weapon != null) {
        	weaponEnchant = _weapon.getEnchantLevel();
        	weaponType2 = _weapon.getItem().getType1();
        	weaponAddSp = _weapon.getItem().get_addsp();
        	if (weaponType2 == 40 &&  weaponEnchant > _weapon.getItem().get_safeenchant()) {
        		final int i = weaponEnchant - _weapon.getItem().get_safeenchant();
        		dmg *= (i + weaponAddSp) * 0.03 + 1;
        	}
        	if (_weapon.get_power_name() != null) {
            	switch (_weapon.get_power_name().get_xing_count()) {
            	case 1:
            		dmg *= 1.1;
            		break;
            	case 2:
            		dmg *= 1.2;
            		break;
            	case 3:
            		dmg *= 1.3;
            		break;
            	case 4:
            		dmg *= 1.4;
            		break;
            	case 5:
            		dmg *= 1.5;
            		break;
            	case 6:
            		dmg *= 1.6;
            		break;
            	case 7:
            		dmg *= 1.7;
            		break;
            	case 8:
            		dmg *= 1.8;
            		break;
            	case 9:
            		dmg *= 1.9;
            		break;
            	}
        	}
        }
        
        boolean isNowWar = false;// 战争中
        final int castleId = L1CastleLocation.getCastleIdByArea(_targetNpc);
        if (castleId > 0) {
            isNowWar = ServerWarExecutor.get().isNowWar(castleId);
        }

        boolean isPet = false;// 是宠物
        if (_targetNpc instanceof L1PetInstance) {
            isPet = true;
            if (_targetNpc.getMaster().equals(_pc)) {
                dmg = 0;
            }
        }
        if (_targetNpc instanceof L1SummonInstance) {
            final L1SummonInstance summon = (L1SummonInstance) _targetNpc;
            if (summon.isExsistMaster()) {
                isPet = true;
            }
            if (_targetNpc.getMaster().equals(_pc)) {
                dmg = 0;
            }
        }

        if (!isNowWar && isPet) {// 非战争中 对象是宠物
            if (dmg != 0) {
                dmg = (dmg >> 3);// dmg /= 8;
            }
        }
        if (isNowWar && isPet) {  //城战区秒杀宠物 hjx1000
        	dmg *= 1000;
        }
        
        if (_targetNpc.getNpcTemplate().is_boss()) { //BOSS魔法伤害减免 hjx1000
        	dmg *= 0.8D; //所有魔法对BOSS伤害只有 80%
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

        if (_pc.getClanid() != 0) {
            // 血盟技能魔法伤害增加
            magicDamage += getDamageUpByClan(_pc);
        }

        switch (_pc.guardianEncounter()) {
            case 3:// 邪恶的守护 Lv.1
                magicDamage += 1;
                break;

            case 4:// 邪恶的守护 Lv.2
                magicDamage += 2;
                break;

            case 5:// 邪恶的守护 Lv.3
                magicDamage += 3;
                break;
        }

        final int spByItem = getTargetSp();// this._pc.getSp() -
                                           // this._pc.getTrueSp(); //
                                           // アイテムによるSP变动
        charaIntelligence = Math.max(_pc.getInt() + spByItem - 12, 1);//修改魔法攻击太强的问题 hjx1000

        /*
         * if (charaIntelligence < 1) { charaIntelligence = 1; }
         */

        final double attrDeffence = calcAttrResistance(l1skills.getAttr());

        double coefficient = Math.max(
                (1.0 - attrDeffence + charaIntelligence * 1.5 / /*32.0原*/ 40.0), 0.0);
        /*
         * if (coefficient < 0) { coefficient = 0; }
         */
        magicDamage *= coefficient;
        //System.out.println("coefficient===:" + coefficient);

        final int rnd = _random.nextInt(100) + 1;
        if (l1skills.getSkillLevel() <= 6) {
            if (rnd <= (10 + _pc.getOriginalMagicCritical())) {
                final double criticalCoefficient = 1.5; // 魔法クリティカル
                magicDamage *= criticalCoefficient;
            }
        }

        magicDamage += _pc.getOriginalMagicDamage();

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
    	if (skillId == 68) { //修改圣结界为隔墙无法施法 hjx1000 DB修改Skills type = 16
    		return 0;
    	}
        final L1Skills l1skills = SkillsTable.get().getTemplate(skillId);
        final int dice = l1skills.getDamageDice();
        final int value = l1skills.getDamageValue();
        int magicDamage = 0;

        int magicBonus = Math.min(getMagicBonus(), 10);

        /*
         * int magicBonus = this.getMagicBonus(); if (magicBonus > 10) {
         * magicBonus = 10; }
         */

        final int diceCount = value + magicBonus;
        for (int i = 0; i < diceCount; i++) {
            magicDamage += (_random.nextInt(dice) + 1);
        }

        double alignmentRevision = 1.0;
        if (getLawful() > 0) {
            alignmentRevision += (getLawful() / 32768.0);
        }

        magicDamage *= alignmentRevision;

        magicDamage = (int) (magicDamage * (getLeverage() / 10D));
        
        switch (_pc.getClassId()) {//限各职业的治疗加血量 hjx1000
        
        case 37:
        case 138:
        case 0:
        case 1:
        	magicDamage *= 0.8; //王族 and 妖精
        	break;
        case 48:
        case 61:
        case 2786:
        case 2796:
        case 6658:
        case 6661:
        	magicDamage *= 0.6; //骑士 and 黑妖 and 龙骑
        	break;
        }

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
        /*final int mr = getTargetMr();

        double mrFloor = 0;
        double mrCoefficient = 0;
        Double[] mrF = L1AttackList.MRDMG.get(new Integer(mr));
        if (mrF != null) {
            mrFloor = mrF[0].doubleValue();
            mrCoefficient = mrF[1].doubleValue();
            
        } else {
            mrFloor = 11;
            mrCoefficient = 0.5;
        }

        // 计算减低的伤害
        dmg *= (mrCoefficient - (0.01 * Math.floor((mr - _pc
                .getOriginalMagicHit()) / mrFloor)));

        return dmg;*/
    	//修改魔防 如需要还原删除以下段。。恢复上面段 hjx1000
		double PowerMr = 0;
		if (getTargetMr() < 101) {
			PowerMr = getTargetMr() / 200;
		} else {
			PowerMr = 0.5 + (getTargetMr() - 100) / 1000;
		}
		dmg -= dmg * PowerMr;
		return dmg;
		//修改魔防  end hjx1000
    }

    /**
     * 计算结果反映
     * 
     * @param damage
     * @param drainMana
     */
    @Override
    public void commit(final int damage, final int drainMana) {
        switch (_calcType) {
            case PC_PC:
                commitPc(damage, drainMana);
                if (_pc.showDmg()) {
                	final int targetId = _targetPc.getId();
                	final String dmg = "" + damage;
                	_pc.sendPackets(new S_TrueTarget(targetId, dmg));
                }
                break;

            case PC_NPC:
                commitNpc(damage, drainMana);
                if (_pc.showDmg()) {
                	final int targetId = _targetNpc.getId();
                	final String dmg = "" + damage;
                	_pc.sendPackets(new S_TrueTarget(targetId, dmg));
                }
                break;
        }
        // ダメージ值及び命中率确认用メッセージ
        if (!ConfigAlt.ALT_ATKMSG) {
            return;
        } else {
            switch (_calcType) {
                case PC_PC:
                    if (!_pc.isGm()) {
                        if (!_targetPc.isGm()) {
                            return;
                        }
                    }
                    break;

                case PC_NPC:
                    if (!_pc.isGm()) {
                        return;
                    }
                    break;
            }
        }

        switch (_calcType) {
            case PC_PC:
                if (_pc.isGm()) {
                    final StringBuilder atkMsg = new StringBuilder();
                    atkMsg.append("对PC送出技能: ");
                    atkMsg.append(_pc.getName() + ">");// 攻击者
                    atkMsg.append(_targetPc.getName() + " ");// 被攻击者
                    atkMsg.append("伤害: " + damage);// 资讯
                    atkMsg.append(" 目标HP:" + _targetPc.getCurrentHp());// 最终资讯
                    // 166 \f1%0%s %4%1%3 %2。
                    _pc.sendPackets(new S_ServerMessage(166, atkMsg.toString()));
                }
                if (this._targetPc.isGm()) {
                    final StringBuilder atkMsg = new StringBuilder();
                    atkMsg.append("受到PC技能: ");
                    atkMsg.append(_pc.getName() + ">");// 攻击者
                    atkMsg.append(_targetPc.getName() + " ");// 被攻击者
                    atkMsg.append("伤害: " + damage);// 资讯
                    atkMsg.append(" 目标HP:" + _targetPc.getCurrentHp());// 最终资讯
                    // 166 \f1%0%s %4%1%3 %2。
                    _targetPc.sendPackets(new S_ServerMessage(166, atkMsg
                            .toString()));
                }
                break;

            case PC_NPC:
                if (_pc.isGm()) {
                    final StringBuilder atkMsg = new StringBuilder();
                    atkMsg.append("对NPC送出技能: ");
                    atkMsg.append(_pc.getName() + ">");// 攻击者
                    atkMsg.append(_targetNpc.getNameId() + " ");// 被攻击者
                    atkMsg.append("伤害: " + damage);// 资讯
                    atkMsg.append(" 目标HP:" + _targetNpc.getCurrentHp());// 最终资讯
                    // 166 \f1%0%s %4%1%3 %2。
                    _pc.sendPackets(new S_ServerMessage(166, atkMsg.toString()));
                }
                break;
        }
    }

    /**
     * 对pc伤害的输出
     * 
     * @param damage
     * @param drainMana
     */
    private void commitPc(final int damage, int drainMana) {
        try {
            if (drainMana > 0) {
                if (_targetPc.getCurrentMp() > 0) {
                    drainMana = Math.min(drainMana, _targetPc.getCurrentMp());
                    final int newMp = _pc.getCurrentMp() + drainMana;
                    _pc.setCurrentMp(newMp);

                } else {
                    drainMana = 0;
                }
            }
            _targetPc.receiveManaDamage(_pc, drainMana);
            _targetPc.receiveDamage(_pc, damage, true, false);

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
    private void commitNpc(final int damage, int drainMana) {
        try {
            if (drainMana > 0) {
                if (_targetNpc.getCurrentMp() > 0) {
                    final int drainValue = _targetNpc.drainMana(drainMana);
                    final int newMp = _pc.getCurrentMp() + drainValue;
                    _pc.setCurrentMp(newMp);

                } else {
                    drainMana = 0;
                }
            }
            _targetNpc.ReceiveManaDamage(_pc, drainMana);
            _targetNpc.receiveDamage(_pc, damage);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
