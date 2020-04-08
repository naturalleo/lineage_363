package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.*;

import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.data.event.FeatureItemSet;
import com.lineage.server.ActionCodes;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.poison.L1DamagePoison;
import com.lineage.server.serverpackets.S_AttackPacketPc;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_PacketBoxDk;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_TrueTarget;
import com.lineage.server.serverpackets.S_UseArrowSkill;
import com.lineage.server.timecontroller.pc.HardDelay;
import com.lineage.server.timecontroller.server.ServerWarExecutor;

/**
 * 物理攻击判断项(PC)
 * 
 * @author dexc
 * 
 */
public class L1AttackPc extends L1AttackMode {

    private static final Log _log = LogFactory.getLog(L1AttackPc.class);

    // 攻击模式 0x00:none 0x02:暴击 0x04:双击 0x08:镜反射
    private byte _attackType = 0x00;

    public L1AttackPc(final L1PcInstance attacker, final L1Character target) {
        if (target == null) {
            return;
        }

        if (target.isDead()) {
            return;
        }

        _pc = attacker;

        if (target instanceof L1PcInstance) {
            _targetPc = (L1PcInstance) target;
            _calcType = PC_PC;

        } else if (target instanceof L1NpcInstance) {
            _targetNpc = (L1NpcInstance) target;
            _calcType = PC_NPC;
        }

        // 武器情报の取得
        _weapon = this._pc.getWeapon();
        if (_weapon != null) {
            _weaponId = _weapon.getItem().getItemId();
            _weaponType = _weapon.getItem().getType1();
            _weaponType2 = _weapon.getItem().getType();
            _weaponAddHit = _weapon.getItem().getHitModifier()
                    + _weapon.getHitByMagic();
            _weaponAddDmg = _weapon.getItem().getDmgModifier()
                    + _weapon.getDmgByMagic();

            _weaponSmall = _weapon.getItem().getDmgSmall();
            _weaponLarge = _weapon.getItem().getDmgLarge();
            _weaponRange = _weapon.getItem().getRange();
            _weaponBless = _weapon.getItem().getBless();

            if ((_weaponType != 20) && (_weaponType != 62)) {
                _weaponEnchant = _weapon.getEnchantLevel()
                        - _weapon.get_durability(); // 损伤分マイナス

            } else {
                _weaponEnchant = _weapon.getEnchantLevel();
            }

            _weaponMaterial = _weapon.getItem().getMaterial();
            if (_weaponType == 20) {// 弓 武器类型:箭取回
                _arrow = _pc.getInventory().getArrow();
                if (_arrow != null) {
                    _weaponBless = _arrow.getItem().getBless();
                    _weaponMaterial = _arrow.getItem().getMaterial();
                }
            }

            if (_weaponType == 62) {// 铁手甲 武器类型:飞刀取回
                _sting = _pc.getInventory().getSting();
                if (_sting != null) {
                    _weaponBless = _sting.getItem().getBless();
                    _weaponMaterial = _sting.getItem().getMaterial();
                }
            }
            
			if (_weapon.get_power_name() != null) { //星武器额外隐藏命中 add hjx1000
//				final int safeenchant = _weapon.getItem().get_safeenchant();
//				_weaponAddHit += (Math.max(_weaponEnchant - safeenchant, 1)
//				<< 2) + 8;
				final int xing = _weapon.get_power_name().get_xing_count();
				_weaponAddHit += xing << 1;
			}

            _weaponDoubleDmgChance = _weapon.getItem().getDoubleDmgChance();
            _weaponAttrEnchantKind = _weapon.getAttrEnchantKind();
            _weaponAttrEnchantLevel = _weapon.getAttrEnchantLevel();
        }

        // ステータスによる追加ダメージ补正
        if (_weaponType == 20) {// 弓 增加敏捷伤害
            Integer dmg = L1AttackList.DEXD.get((int) _pc.getDex());
            if (dmg != null) {
                _statusDamage = dmg;
            }

        } else { // それ以外はＳＴＲ值参照abstract
            Integer dmg = L1AttackList.STRD.get((int) _pc.getStr());
            if (dmg != null) {
                _statusDamage = dmg;
            }
        }

        _target = target;
        _targetId = target.getId();
        _targetX = target.getX();
        _targetY = target.getY();
    }

    /**
     * 命中判定
     */
    @Override
    public boolean calcHit() {
        if (this._target == null) {// 物件遗失
            this._isHit = false;
            return this._isHit;
        }

        if (this._weaponRange != -1) {

            // 近距离武器攻击距离判断
            final int location = this._pc.getLocation().getTileLineDistance(
                    this._target.getLocation());

            if (location > (this._weaponRange + 1)) {
                this._isHit = false; // 射程范围外
                return this._isHit;
            }

        } else {
            // 远距离武器攻击距离判断
            if (!this._pc.getLocation().isInScreen(this._target.getLocation())) {
                this._isHit = false; // 射程范围外
                return this._isHit;
            }
        }

        if ((this._weaponType == 20) && (this._weaponId != 190)
                && (this._arrow == null)) {
            this._isHit = false; // 持弓 无箭

        } else if ((this._weaponType == 62) && (this._sting == null)) {
            this._isHit = false; // 持铁手甲 无飞刀

        } else if (!this._pc.glanceCheck(this._targetX, this._targetY)) {
            this._isHit = false; // 攻击方向中途具有障碍

        } else if ((this._weaponId == 247) || (this._weaponId == 248)
                || (this._weaponId == 249)) {
            this._isHit = false; // 试炼武器

        } else if (this._calcType == PC_PC) {
            this._isHit = this.calcPcHit();// PC TO PC

        } else if (this._calcType == PC_NPC) {
            this._isHit = this.calcNpcHit();// PC TO NPC
        }

        return this._isHit;
    }

    private int str_dex_Hit() {
        int hitRate = 0;
        // 力量命中补正
        final Integer hitStr = L1AttackList.STRH.get(this._pc.getStr() - 1);
        if (hitStr != null) {
            hitRate += hitStr;

        } else {
            hitRate += 19;
        }

        // 敏捷命中补正
        final Integer hitDex = L1AttackList.DEXH.get(this._pc.getDex() - 1);
        if (hitDex != null) {
            hitRate += hitDex;

        } else {
            hitRate += 29;
        }
        return hitRate;
    }

    /**
     * PC对PC的命中
     * 
     * @return
     */
    private boolean calcPcHit() {
        if (_targetPc == null) {
            return false;
        }
        if (_pc.hasSkillEffect(attack_fanlse)) { //添加攻击无效状态 hjx1000
        	return false;
        }
        // 伤害为0
        if (dmg0(_targetPc)) {
            return false;
        }

        // 回避攻击
        if (calcEvasion()) {
            return false;
        }

        if (_weaponType2 == 17) {// 奇古兽
            return true;
        }

        _hitRate = _pc.getLevel();

        // 力量命中补正 / 敏捷命中补正
        _hitRate += str_dex_Hit();

        if ((_weaponType != 20) && (_weaponType != 62)) {
            _hitRate +=
            // (this._weaponAddHit + this._pc.getHitup() +
            // this._pc.getOriginalHitup() + (this._weaponEnchant / 2));// TEST
            (_weaponAddHit + _pc.getHitup() + _pc.getOriginalHitup() + (_weaponEnchant * 0.6));

        } else {
            _hitRate +=
            // (this._weaponAddHit + this._pc.getBowHitup() +
            // this._pc.getOriginalBowHitup() + (this._weaponEnchant / 2));//
            // TEST
            (_weaponAddHit + _pc.getBowHitup() + _pc.getOriginalBowHitup() + (_weaponEnchant * 0.6));
        }

        if ((_weaponType != 20) && (_weaponType != 62)) { // 防具による追加命中
            _hitRate += _pc.getHitModifierByArmor();

        } else {
            _hitRate += _pc.getBowHitModifierByArmor();
        }

        final int weight240 = _pc.getInventory().getWeight240();
        if (weight240 > 80) { // 重量による命中补正
            if ((80 < weight240) && (120 >= weight240)) {
                _hitRate -= 1;

            } else if ((121 <= weight240) && (160 >= weight240)) {
                _hitRate -= 3;

            } else if ((161 <= weight240) && (200 >= weight240)) {
                _hitRate -= 5;
            }
        }

        _hitRate += hitUp();
        // int attackerDice = _random.nextInt(20) + 1 + this._hitRate - 10;//
        // TEST
        int attackerDice = _random.nextInt(20) + 2 + _hitRate - 10;

        // 技能增加闪避
        attackerDice += attackerDice(_targetPc);

        int defenderDice = 0;

        final int defenderValue = (int) (_targetPc.getAc() * 1.5) * -1;

        if (_targetPc.getAc() >= 0) {
            defenderDice = 10 - _targetPc.getAc();

        } else if (_targetPc.getAc() < 0) {
            defenderDice = 10 + _random.nextInt(defenderValue) + 1;
        }
        
        final int fumble = _hitRate - 9;
        final int critical = _hitRate + 10;
        if (_pc.isDragonKnight()) {
            attackerDice *= 1.01;
        }
        if (_pc.isElf()) {
            if (_pc.getElfAttr() == 2) {
                attackerDice *= 1.02;
            }
        }

        if (attackerDice <= fumble) {
            _hitRate = 15;

        } else if (attackerDice >= critical) {
            _hitRate = 100;

        } else {
            if (attackerDice > defenderDice) {
                _hitRate = 100;

            } else if (attackerDice <= defenderDice) {
                _hitRate = 15;
            }
        }
        //System.out.println("attackerDice===="+ attackerDice+"defenderDice===="+defenderDice);
        final int rnd = _random.nextInt(100) + 1;
        if (_weaponType == 20) {// 弓 附加ER计算
            if (_hitRate > rnd) {
                return calcErEvasion();
            }
        }
        return _hitRate >= rnd;
    }

    /**
     * PC对NPC的命中
     * 
     * @return
     */
    private boolean calcNpcHit() {
        // 对不可见的怪物额外判断
        final int gfxid = this._targetNpc.getNpcTemplate().get_gfxid();
        switch (gfxid) {
            case 2412:// 南瓜的影子
                if (!_pc.getInventory().checkEquipped(20046)) {// 南瓜帽
                    return false;
                }
                break;
        }
        if (_pc.hasSkillEffect(attack_fanlse)) { //添加攻击无效状态 hjx1000
        	return false;
        }
        // 伤害为0
        if (dmg0(_targetNpc)) {
            return false;
        }

        if (_weaponType2 == 17) {// 奇古兽 命中100%
            return true;
        }

        // ＮＰＣへの命中率
        // ＝（PCのLv＋クラス补正＋STR补正＋DEX补正＋武器补正＋DAIの枚数/2＋魔法补正）×5?{NPCのAC×（-5）}
        _hitRate = _pc.getLevel();

        // 力量命中补正 / 敏捷命中补正
        _hitRate += str_dex_Hit();

        if ((_weaponType != 20) && (_weaponType != 62)) {
            _hitRate +=
            // (this._weaponAddHit + this._pc.getHitup() +
            // this._pc.getOriginalHitup() + (this._weaponEnchant / 2)); // XXX
            (_weaponAddHit + _pc.getHitup() + _pc.getOriginalHitup() + (_weaponEnchant * 0.6));

        } else {
            _hitRate +=
            // (this._weaponAddHit + this._pc.getBowHitup() +
            // this._pc.getOriginalBowHitup() + (this._weaponEnchant / 2));//
            // XXX
            (_weaponAddHit + _pc.getBowHitup() + _pc.getOriginalBowHitup() + (_weaponEnchant * 0.6));
        }

        if ((_weaponType != 20) && (_weaponType != 62)) { // 防具による追加命中
            _hitRate += _pc.getHitModifierByArmor();

        } else {
            _hitRate += _pc.getBowHitModifierByArmor();
        }

        final int weight240 = _pc.getInventory().getWeight240();
        if (weight240 > 80) { // 重量による命中补正
            if ((80 < weight240) && (120 >= weight240)) {
                _hitRate -= 1;

            } else if ((121 <= weight240) && (160 >= weight240)) {
                _hitRate -= 3;

            } else if ((161 <= weight240) && (200 >= weight240)) {
                _hitRate -= 5;
            }
        }

        _hitRate += hitUp();

        // int attackerDice = _random.nextInt(20) + 1 + this._hitRate - 10;//
        // TEST
        int attackerDice = _random.nextInt(20) + 2 + _hitRate - 10;

        // 技能增加闪避
        attackerDice += attackerDice(_targetNpc);

        final int defenderDice = 10 - _targetNpc.getAc();

        final int fumble = _hitRate - 9;
        final int critical = _hitRate + 10;
        if (_pc.isDragonKnight()) {
            attackerDice *= 1.01;
        }
        if (_pc.isElf()) {
            if (_pc.getElfAttr() == 2) {
                attackerDice *= 1.02;
            }
        }

        if (attackerDice <= fumble) {
            _hitRate = 15;

        } else if (attackerDice >= critical) {
            _hitRate = 100;

        } else {
            if (attackerDice > defenderDice) {
                _hitRate = 100;

            } else if (attackerDice <= defenderDice) {
                _hitRate = 15;
            }
        }

        final int npcId = _targetNpc.getNpcTemplate().get_npcId();

        final Integer tgskill = L1AttackList.SKNPC.get(npcId);
        if (tgskill != null) {
            if (!_pc.hasSkillEffect(tgskill)) {
                _hitRate = 0;
            }
        }

        final Integer tgpoly = L1AttackList.PLNPC.get(npcId);
        if (tgpoly != null) {
            if (tgpoly.equals(_pc.getTempCharGfx())) {
                _hitRate = 0;
            }
        }

        final int rnd = _random.nextInt(100) + 1;

        return _hitRate >= rnd;
    }

    /**
     * 追加命中
     * 
     * @return
     */
    private int hitUp() {
        int hitUp = 0;
        if (_pc.getSkillEffect().size() <= 0) {
            return hitUp;
        }

        if (!_pc.getSkillisEmpty()) {
            try {
                // 追加命中(近距离武器)
                if ((_weaponType != 20) && (_weaponType != 62)) {
                    for (final Integer key : _pc.getSkillEffect()) {
                        final Integer integer = L1AttackList.SKU1.get(key);
                        if (integer != null) {
                            hitUp += integer;
                        }
                    }

                    // 追加命中(远距离武器)
                } else {
                    for (final Integer key : _pc.getSkillEffect()) {
                        final Integer integer = L1AttackList.SKU2.get(key);
                        if (integer != null) {
                            hitUp += integer;
                        }
                    }
                }

            } catch (final ConcurrentModificationException e) {
                // 技能取回发生其他线程进行修改
            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }

        return hitUp;
    }

    /**
     * 伤害计算
     */
    @Override
    public int calcDamage() {
        switch (_calcType) {
            case PC_PC:
                _damage = calcPcDamage();
                showNum(true);
                break;

            case PC_NPC:
                _damage = calcNpcDamage();
                showNum(false);
                break;
        }
        if (_damage > 0) {
            // 特殊魔法武器
            if (_weapon != null) {
                if (FeatureItemSet.POWER_START) {
                    final L1AttackPower attackPower = new L1AttackPower(_pc,
                            _target, _weaponAttrEnchantKind,
                            _weaponAttrEnchantLevel);
                    _damage = attackPower.set_item_power(_damage);
                }
            }
        }       
        
        return _damage;
    }
    
	/*
	 * 飘血功能 by:
	 */
	private void showNum(final boolean isPc){
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
     * 伤害质初始化
     * 
     * @param weaponMaxDamage
     *            可发出的最大攻击质
     * @return
     */
    private int weaponDamage1(int weaponMaxDamage) {
        int weaponDamage = 0;
        // boolean soulFlame = false;// 技能(烈焰之魂)
        // 武器类型核心分类
		switch (_weaponType2) {
		case 0:// 空手
			break;
		case 4:// 弓
		case 10:// 鐵手甲
		case 13:// 弓(單手)
			weaponDamage = _random.nextInt(weaponMaxDamage) + 1;//补回弓的普通攻击 hjx1000
			break;

		case 11:// 鋼爪
			if ((_random.nextInt(100) + 1) <= _weaponDoubleDmgChance) {
				weaponDamage  = weaponMaxDamage;
				_attackType = 0x02;
			} else {
				weaponDamage = _random.nextInt(weaponMaxDamage) + 1; //补回爪的普通攻击 hjx1000
			}
			break;
		case 12:// 雙刀
			weaponDamage = _random.nextInt(weaponMaxDamage) + 1;
			// 雙擊
			if ((_random.nextInt(100) + 1) <= _weaponDoubleDmgChance) {
				_attackType = 0x04;
			}
			break;

		case 15:// 雙手斧
		case 1:// 劍
		case 2:// 匕首
		case 3:// 雙手劍
		case 5:// 矛(雙手)
		case 6:// 斧(單手)
		case 7:// 魔杖
		case 8:// 飛刀
		case 9:// 箭
		//case 12:// 雙刀
		case 14:// 矛(單手)
		case 16:// 魔杖(雙手)
		case 17:// 奇古獸
		case 18:// 鎖鏈劍
			if (_pc.hasSkillEffect(SOUL_OF_FLAME)) {
				weaponDamage  = weaponMaxDamage;
			} else {
				weaponDamage = _random.nextInt(weaponMaxDamage) + 1;
			}
			weaponDamage += dk_dmgUp(); //弱点暴光算在武器的攻击上面 hjx1000
			break;
		}

//        if (_pc.getClanid() != 0) {
//            weaponDamage += getDamageUpByClan(_pc);// 血盟技能伤害提升
//        }
//        switch (_pc.guardianEncounter()) {
//            case 3:// 邪恶的守护 Lv.1
//                weaponDamage += 1;
//                break;
//
//            case 4:// 邪恶的守护 Lv.2
//                weaponDamage += 3;
//                break;
//
//            case 5:// 邪恶的守护 Lv.3
//                weaponDamage += 5;
//                break;
//        }
        return weaponDamage;
    }

    /**
     * 伤害质最终计算
     * 
     * @param weaponTotalDamage
     * @return
     */
    private double weaponDamage2(double weaponTotalDamage) {
        double dmg = 0.0;
        boolean doubleBrake = false;// 技能(双重破坏)
        switch (_weaponType2) {
		case 1:// 劍
		case 2:// 匕首
		case 3:// 雙手劍
		case 5:// 矛(雙手)
		case 6:// 斧(單手)
		case 7:// 魔杖
		case 8:// 飛刀
		case 9:// 箭
		case 14:// 矛(單手)
		case 15:// 雙手斧
		case 16:// 魔杖(雙手)
		case 18:// 鎖鏈劍
			dmg = weaponTotalDamage + _statusDamage + _pc.getDmgup() + _pc.getOriginalDmgup();
			break;

		case 12:// 雙刀
		case 11:// 鋼爪
			// 技能(雙重破壞)
			if (_pc.hasSkillEffect(DOUBLE_BRAKE)) {
				doubleBrake = true;
			}
			dmg = weaponTotalDamage + _statusDamage + _pc.getDmgup() + _pc.getOriginalDmgup();
			//dmg += 5.0;
			break;
			
		/*case 12:// 雙刀
			// 雙擊
			if ((_random.nextInt(100) + 1) <= _weaponDoubleDmgChance) {
				_attackType = 0x04;
			}
			
			// 技能(雙重破壞)
			if (_pc.hasSkillEffect(DOUBLE_BRAKE)) {
				doubleBrake = true;
			}
			dmg = weaponTotalDamage + _statusDamage + _pc.getDmgup() + _pc.getOriginalDmgup();
			break;*/

            case 0:// 空手
                dmg = (_random.nextInt(5) + 4) >> 2;// / 4;
                break;

            case 4:// 弓
            case 13:// 弓(单手)
//                double add = _statusDamage;
//                if (_calcType == PC_NPC) {
//                    case PC_PC:
//                        add *= 2.5D;
//                        break;
//
//                    case PC_NPC:
//                        add *= 1.5D;
//                        break;
//                }
                dmg = weaponTotalDamage + _statusDamage + _pc.getBowDmgup()
                        + _pc.getOriginalBowDmgup();
                if (_arrow != null) {
                    final int add_dmg = Math.max(
                            _arrow.getItem().getDmgSmall(), 1);
                    dmg = dmg + _random.nextInt(add_dmg) + 1;

                } else if (_weaponId == 190) { // 沙哈之弓
                    dmg = dmg + _random.nextInt(12) + 1;
                }
                break;

            case 10:// 铁手甲
                dmg = weaponTotalDamage + _statusDamage + _pc.getBowDmgup()
                        + _pc.getOriginalBowDmgup();
                final int add_dmg = Math.max(_sting.getItem().getDmgSmall(), 1);
                dmg = dmg + _random.nextInt(add_dmg) + 1;
                break;

            case 17:// 奇古兽
                //dmg += weaponTotalDamage;
                dmg = weaponTotalDamage + _pc.getDmgup() + L1WeaponSkill.getKiringkuDamage(_pc, _target);
                break;
        }

		// 技能(雙重破壞)
		if (doubleBrake) {
			final int PcLvl = Math.max(_pc.getLevel(), 45);
			final int up_rnd =(PcLvl - 45) / 5;
			if ((_random.nextInt(100) + 1) <= (33 + up_rnd)) { //黑妖技能 升级影响机率 hjx1000
				dmg *= 2.0;// 2010-11-26(1.89)//改回双倍 hjx1000
			}
		}

        if (_weaponType2 != 0) {
            final int add_dmg = _weapon.getItem().get_add_dmg();
            if (add_dmg != 0) {
                dmg += add_dmg;
            }
        }
        return dmg;
    }

	/**
	 * 计算星武器伤害 //add hjx1000
	 * @param weaponTotalDamage
	 * @return
	 */
	private double xing_weaponDamage3(double weaponTotalDamage) {
		final int chance = _random.nextInt(100)+1;
		final int xing_count = _weapon.get_power_name().get_xing_count();
		//final int pro = this._weaponEnchant - Math.max(4, this._weapon.getItem().get_safeenchant()) - 1;

		switch (_weaponType2) {
		case 4:// 弓
		case 10:// 鐵手甲
		case 13:// 弓(單手)
			final int DEX_1 = _pc.getDex();//敏捷 == 暴击机率
			if (DEX_1 > chance) {
				weaponTotalDamage += weaponTotalDamage * (xing_count * 0.4 + 0.6);	
//                _pc.sendPackets(new S_UseArrowSkill(_pc,
//                        _targetId, 7972, _targetX, _targetY,
//                        _damage));
			}
			break;
		case 11: //钢爪
		case 12: //双刀
			final int STR_0 = _pc.getStr();//力量 == 暴击机率
			if (STR_0 > chance) {
				weaponTotalDamage += weaponTotalDamage * (xing_count * 0.3 + 0.55);
//				_pc.sendPackets(new S_SkillSound(_pc.getId(), 5679));
			}				
			break;
		case 15:// 雙手斧
		case 1:// 劍
		case 2:// 匕首
		case 3:// 雙手劍
		case 5:// 矛(雙手)
		case 6:// 斧(單手)
		case 14:// 矛(單手)
		case 18:// 鎖鏈劍
			final int STR_1 = _pc.getStr();//力量 == 暴击机率
			if (STR_1 > chance) {
				weaponTotalDamage += weaponTotalDamage * (xing_count * 0.3 + 0.55);
//				_pc.sendPackets(new S_SkillSound(_pc.getId(), 4628));
			}				
			break;
		case 17:// 奇古獸
		case 7:// 魔杖
		case 16:// 魔杖(雙手)
			final int jinagji = _pc.getInt();//智力  == 暴击机率
			if (jinagji > chance) {
				weaponTotalDamage += weaponTotalDamage * (xing_count * 0.4 + 0.6);
//				_pc.sendPackets(new S_SkillSound(_pc.getId(), 4374));
			}
			break;
		}
		weaponTotalDamage += xing_count;//每星加额外1点伤
		return weaponTotalDamage;
	}
	
    /**
     * PC基础伤害提升计算
     * 
     * @param dmg
     * @param weaponTotalDamage
     * @return
     */
    private double pcDmgMode(double dmg, double weaponTotalDamage) {

        dmg = calcBuffDamage(dmg);

        dmg += weaponSkill(_pc, _target, weaponTotalDamage);// 武器附加魔法

        addPcPoisonAttack(_target);

        if ((_weaponType != 20) && (_weaponType != 62)) { // 防具追加伤害
            dmg += _pc.getDmgModifierByArmor();

        } else {
            dmg += _pc.getBowDmgModifierByArmor();
        }

        dmg += dmgUp();
        //dmg += dk_dmgUp(); 修改到武器下面 hjx1000
        switch (_pc.get_weaknss()) {
            case 1:
                if (_pc.isFoeSlayer()) {// 使用屠宰者
                    _pc.set_weaknss(0, 0);
                    _pc.sendPackets(new S_PacketBoxDk(0));
                    dmg *= 1.3D;
                }
                break;
            case 2:
                if (_pc.isFoeSlayer()) {// 使用屠宰者
                    _pc.set_weaknss(0, 0);
                    _pc.sendPackets(new S_PacketBoxDk(0));
                    dmg *= 1.6D;
                }
                break;
            case 3:
                if (_pc.isFoeSlayer()) {// 使用屠宰者
                    _pc.set_weaknss(0, 0);
                    _pc.sendPackets(new S_PacketBoxDk(0));
                    dmg *= 2.0D;
                }
                break;
        }
        return dmg;
    }

    /**
     * PC对PC伤害计算
     */
    public int calcPcDamage() {
        if (_targetPc == null) {
            return 0;
        }

        // 伤害为0
        if (dmg0(_targetPc)) {
            _isHit = false;
            _drainHp = 0;
            return 0;
        }

        if (!_isHit) {
            return 0;
        }

        final int weaponMaxDamage = _weaponSmall;

        // 伤害直初始化
        int weaponDamage = weaponDamage1(weaponMaxDamage);

		if (_pc.is_mazu()) {// 媽祖祝福
			weaponDamage += 1;
		}

		double weaponTotalDamage = weaponDamage + _weaponAddDmg;
		
		if (_weaponEnchant > 0 && _weapon.get_power_name() != null) { //添加星武器伤害..hjx1000
			weaponTotalDamage = xing_weaponDamage3(weaponTotalDamage);
		}

		weaponTotalDamage += _weaponEnchant;
		
		// 雙刀重擊
		if (_attackType == 0x04) {
			weaponTotalDamage *= 2.0;// 2010-11-26(1.89) 改回双刀双击双倍
			if (_weaponId == 76) { //伦得双刀 hjx1000
				weaponTotalDamage *= 1.3;//暂定 增加 1.3倍双击
			}
		}

        weaponTotalDamage += calcAttrEnchantDmg(); // 属性强化
        switch (_pc.guardianEncounter()) {
        case 3:// 邪恶的守护 Lv.1
        	weaponTotalDamage += 1;
            break;

        case 4:// 邪恶的守护 Lv.2
        	weaponTotalDamage += 3;
            break;

        case 5:// 邪恶的守护 Lv.3
        	weaponTotalDamage += 5;
            break;
        }
        // 伤害直最终计算
        double dmg = weaponDamage2(weaponTotalDamage);

        // PC基础伤害提升计算
        dmg = pcDmgMode(dmg, weaponTotalDamage);

        //dmg -= calcPcDefense();// 减伤装备PK伤害直减低 hjx1000

        dmg -= _targetPc.getDamageReductionByArmor(); // 防具减伤

        dmg -= _targetPc.dmgDowe(); // 机率伤害减免

//        if (_targetPc.getClanid() != 0) {
//            dmg -= getDamageReductionByClan(_targetPc);// 被攻击者血盟技能伤害减免
//        }

        // 增幅防御
        if (_targetPc.hasSkillEffect(REDUCTION_ARMOR)) {
            final int targetPcLvl = Math.max(_targetPc.getLevel(), 50);
            dmg -= (targetPcLvl - 50) / 5 + 1;
        }

        boolean dmgX2 = false;// 伤害除2
        // 取回技能
        if (!_targetPc.getSkillisEmpty()
                && _targetPc.getSkillEffect().size() > 0) {
            try {
                for (final Integer key : _targetPc.getSkillEffect()) {
                    final Integer integer = L1AttackList.SKD3.get(key);
                    if (integer != null) {
                        if (integer.equals(key)) {
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
            dmg *= 0.7;
        }

        // 魔法娃娃特殊技能
        if (!_pc.getDolls().isEmpty()) {
            for (final Iterator<L1DollInstance> iter = _pc.getDolls().values()
                    .iterator(); iter.hasNext();) {
                final L1DollInstance doll = iter.next();
                doll.startDollSkill(_targetPc, dmg);
            }
        }

//        // 双刀重击
//        if (_attackType == 0x04) {
//            dmg *= 1.80;// 2010-11-26(1.89)
//        } //双刀已写回武器上 hjx1000
        if (_targetPc.hasSkillEffect(DRESS_HALZ)
        	&& _weaponRange > 0) { //黑暗籠罩  近身攻击有效
        	dmg *= 1.58;
        }
//    	switch (_weaponEnchant) {
//    		case 7:
//    			dmg *= 1.15;
//    			break;
//    		case 8:
//    			dmg *= 1.25;
//    			break;
//    		case 9:
//    			dmg *= 1.35;
//    			break;
//    		case 10:
//    			dmg *= 1.5;
//    			break;
//    	}
        dmg *= coatArms();

        // 未命中伤害归0
        if (!this._isHit) {
            dmg = 0.0;
        }

        if (dmg <= 0) {
            _isHit = false;
            _drainHp = 0;
        } else {
            if (!_targetPc.isHardDelay()) { //动作延时 hjx1000
            	HardDelay.onHardUse(_targetPc, 150);
            }
        }

        // System.out.println("PC对PC伤害 武器系统:" + dmg);
        return (int) dmg;
    }

    /**
     * PC对NPC伤害
     * 
     * @return
     */
    private int calcNpcDamage() {
        if (_targetNpc == null) {
            return 0;
        }

        // 伤害为0
        if (dmg0(_targetNpc)) {
            _isHit = false;
            _drainHp = 0;
            return 0;
        }

        if (!_isHit) {
            return 0;
        }
        if (!_pc.hasSkillEffect(attack_ing)) {
        	_pc.setSkillEffect(attack_ing, 120000);//检测外挂状态 hjx1000
        }
        int weaponMaxDamage = 0;
        if (_targetNpc.getNpcTemplate().isSmall() && (_weaponSmall > 0)) {
            if (_weaponSmall > 0) {
                weaponMaxDamage = _weaponSmall;
            }

        } else if (_targetNpc.getNpcTemplate().isLarge() && (_weaponLarge > 0)) {
            if (_weaponLarge > 0) {
                weaponMaxDamage = _weaponLarge;
            }

        } else {
            if (_weaponSmall > 0) {
                weaponMaxDamage = _weaponSmall;
            }
        }
        // 伤害直初始化
        int weaponDamage = weaponDamage1(weaponMaxDamage);

		if (_pc.is_mazu()) {// 媽祖祝福
			weaponDamage += 1;
		}

		double weaponTotalDamage = weaponDamage + _weaponAddDmg;

		weaponTotalDamage += calcMaterialBlessDmg(); // 祝福武器 銀/米索莉/奧里哈魯根材質武器
		
		if (_weaponEnchant > 0 && _weapon.get_power_name() != null) { //添加星武器伤害..hjx1000
			weaponTotalDamage = xing_weaponDamage3(weaponTotalDamage);
		}
		
		weaponTotalDamage += _weaponEnchant;
		
		// 雙刀重擊
		if (_attackType == 0x04) {
			weaponTotalDamage *= 2.0;// 2010-11-26(1.89) 改回双刀双击双倍 hjx1000
		}

        weaponTotalDamage += calcAttrEnchantDmg(); // 属性强化
        switch (_pc.guardianEncounter()) {
        case 3:// 邪恶的守护 Lv.1
        	weaponTotalDamage += 1;
            break;

        case 4:// 邪恶的守护 Lv.2
        	weaponTotalDamage += 3;
            break;

        case 5:// 邪恶的守护 Lv.3
        	weaponTotalDamage += 5;
            break;
        }

        // 伤害直最终计算
        double dmg = weaponDamage2(weaponTotalDamage);

        // PC基础伤害提升计算
        dmg = pcDmgMode(dmg, weaponTotalDamage);

        dmg -= calcNpcDamageReduction();// NPC防御力额外伤害减低

        // プレイヤーからペット、サモンに攻击
        boolean isNowWar = false;
        final int castleId = L1CastleLocation.getCastleIdByArea(_targetNpc);
        if (castleId > 0) {
            isNowWar = ServerWarExecutor.get().isNowWar(castleId);
        }

        if (!isNowWar) {
            if (_targetNpc instanceof L1PetInstance) {
                dmg /= 8;
            }
            if (_targetNpc instanceof L1SummonInstance) {
                final L1SummonInstance summon = (L1SummonInstance) _targetNpc;
                if (summon.isExsistMaster()) {
                    dmg /= 8;
                }
            }
        } else if (_targetNpc instanceof L1PetInstance || //城战区秒杀宠物 hjx1000
        		_targetNpc instanceof L1SummonInstance) {
        	dmg *= 1000;
        }

        // 魔法娃娃特殊技能
        if (!_pc.getDolls().isEmpty()) {
            for (final Iterator<L1DollInstance> iter = _pc.getDolls().values()
                    .iterator(); iter.hasNext();) {
                final L1DollInstance doll = iter.next();
                doll.startDollSkill(_targetNpc, dmg);
            }
        }
      
//        // 双刀重击
//        if (_attackType == 0x04) {
//            dmg *= 1.80;// 2010-11-26(1.89)
//        } //双刀双击已修改到武器上 hjx1000

        // 未命中伤害归0
        if (!_isHit) {
            dmg = 0D;
        }

        if (dmg <= 0D) {
            _isHit = false;
            _drainHp = 0; // ダメージ无しの场合は吸收による回复はしない
        }

        // System.out.println("PC对NPC伤害 武器系统:" + dmg);
        return (int) dmg;
    }

    private int dk_dmgUp() {
        int dmg = 0;
        if (_weaponType2 == 18) {// 锁炼剑
            long h_time = Calendar.getInstance().getTimeInMillis() / 1000;// 换算为秒
            final int random = _random.nextInt(100) + 1;
            switch (_pc.get_weaknss()) {
             	case 0:
             		if (random <= 24) {
             			_pc.set_weaknss(1, h_time);
             		}
					break;
             	case 1:
             		if (_pc.isFoeSlayer()) {// 使用屠宰者
             			return 0;
             		}
             		if (random <= 24) {
             			_pc.set_weaknss(2, h_time);
             		}
             		dmg += 3;//修改为固定3攻 hjx1000
             		break;
             	case 2:
             		if (_pc.isFoeSlayer()) {// 使用屠宰者
             			return 0;
             		}
             		if (random <= 24) {
             			_pc.set_weaknss(3, h_time);
             		}
             		dmg += 6;//修改为固定6攻 hjx1000
             		break;
             	case 3:
             		if (_pc.isFoeSlayer()) {// 使用屠宰者
             			return 0;
             		}
             		if (random <= 24) {
             			_pc.set_weaknss(3, h_time);
             		}
             		dmg += 9; //修改为固定9攻 hjx1000
                    break;
            	}
        	}
        	return dmg;
    }

    /**
     * 技能对武器追加伤害
     * 
     * @return
     */
    private double dmgUp() {
        double dmg = 0.0;

        if (_pc.getSkillEffect().size() <= 0) {
            return dmg;
        }

        if (!_pc.getSkillisEmpty()) {
            try {
                HashMap<Integer, Integer> skills = null;
                switch (_weaponType) {
                    case 20:// 弓
                    case 62:// 铁手甲
                        skills = L1AttackList.SKD2;
                        break;

                    case 24:// 锁炼剑
                    default:
                        skills = L1AttackList.SKD1;
                        break;
                }

                if (skills != null) {
                    for (final Integer key : _pc.getSkillEffect()) {
                        final Integer integer = L1AttackList.SKD2.get(key);
                        if (integer != null) {
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
        dmg += _pc.dmgAdd();
        return dmg;
    }

    /**
     * 武器附加魔法
     * 
     * @param pcInstance
     * @param character
     * @param weaponTotalDamage
     * @return
     */
    private double weaponSkill(final L1PcInstance pcInstance,
            final L1Character character, double weaponTotalDamage) {
        double dmg = 0D;

        switch (_weaponId) {
            /*case 265:// 底比斯欧西里斯双刀
            case 266:// 底比斯欧西里斯双手剑
            case 267:// 底比斯欧西里斯弓
            case 268:// 底比斯欧西里斯魔杖
                // dmg = L1WeaponSkill.getChaserDamage(_pc, _target);
                break;*/ //hjx1000

            case 2:// 骰子匕首
            case 200002:// 骰子匕首
                if (this._targetPc != null) {
                    dmg = L1WeaponSkill.getDiceDaggerDamage(_pc, _targetPc,
                            _weapon);
                }
                break;

            case 124:// 巴风特魔杖
                dmg = L1WeaponSkill.getBaphometStaffDamage(_pc, _target);
                break;
                
            case 134:// 圣晶魔杖
                dmg = L1WeaponSkill.getHoly_CrystalStaffDamage(_pc, _target);
                break;

            case 204:// 深红之弩
            case 100204:// 深红之弩
            case 86://红影双刀
                L1WeaponSkill.giveFettersEffect(_pc, _target);
                break;

            case 261:// 大法师魔仗
                L1WeaponSkill.giveArkMageDiseaseEffect(_pc, _target);
                break;

            case 260:// 狂风之斧
            case 263:// 酷寒之矛
                dmg = L1WeaponSkill.getAreaSkillWeaponDamage(_pc, _target,
                        _weaponId);
                break;

            case 264:// 雷雨之剑
                dmg = L1WeaponSkill.getLightningEdgeDamage(_pc, _target);
                break;
            case 319://弑神者之弓
            	final int random0 = _random.nextInt(100) + 1;
            	if (random0 <= _weaponEnchant + 1) {//机率等于  1 + 强化值
                    dmg = weaponTotalDamage;
                	_pc.sendPacketsX8(new S_SkillSound(_target.getId(), 4394));
            	}
                break;

            case 262: // 毁灭巨剑HP夺取成功确率(暂定)75%
            case 12:// 风刃短剑
            case 10289: //嗜血者锁链剑
                final int random = _random.nextInt(100) + 1;
                if (random <= 75) {
                    dmg = calcDestruction(weaponTotalDamage);
                }
                break;
                
            case 100310:
            case 310://极寒锁链剑
            case 317://席琳锁链剑
            	dmg = L1WeaponSkill.getChaserDamageliliang(_pc, _target);
            	break;
            	
            case 311://极寒奇古兽
            case 318://席琳奇古兽
            	dmg = L1WeaponSkill.getMindBreak(_pc, _target);
            	break;
            case 121://冰之女王魔杖
            	dmg = L1WeaponSkill.getThe_ice_queenDamage(_pc, _target);
            	break;
            case 315://席琳法杖
            	dmg = L1WeaponSkill.getcelion_queenDamage(_pc, _target);
            	break;
            default:
                dmg = L1WeaponSkill.getWeaponSkillDamage(_pc, _target,
                        _weaponId);
                break;
        }

        return dmg;
    }

    /**
     * 武器强化魔法
     * 
     * @param dmg
     * @return
     */
    private double calcBuffDamage(double dmg) {
        if (_weaponType == 20) {// 弓
            return dmg;
        }
        if (_weaponType == 62) {// 铁手甲
            return dmg;
        }
        if (_weaponType2 == 17) {// 奇古兽
            return dmg;
        }

        boolean isDmgUp = false;
        double dmgDouble = 1.0;
        final int random = _random.nextInt(100) + 1;

        // 属性之火
        if (_pc.hasSkillEffect(ELEMENTAL_FIRE)) {
            if (random <= 50) { //由33变更到50机率 hjx1000
                isDmgUp = true;
                dmgDouble += 0.50;// 2010-11-26(0.45)//更改为1.5倍正仿官方 hjx1000
            }
        }

        // 燃烧斗志
        if (_pc.hasSkillEffect(BURNING_SPIRIT)) {
            if (random <= 33) {
                isDmgUp = true;
                dmgDouble += 0.50;// 2010-11-26(0.45)//更改为1.5倍正仿官方 hjx1000
            }
        }

        // 烈焰之魂
//        if (_pc.hasSkillEffect(SOUL_OF_FLAME)) {
//            if (random <= 33) {
//                isDmgUp = true;
//                dmgDouble += 0.7;// 2011-10-13(5)
//            }
//        } //屏掉这句改写回武器攻击上 hjx1000

        if (isDmgUp) {
            double tempDmg = dmg;
            // 火焰武器
            if (_pc.hasSkillEffect(FIRE_WEAPON)) {
                tempDmg -= 4;
            }

            // 烈炎气息
//            if (_pc.hasSkillEffect(FIRE_BLESS)) {
//                tempDmg -= 4;
//            }

            // 烈炎武器
            if (_pc.hasSkillEffect(BURNING_WEAPON)) {
                tempDmg -= 6;
            }

            // 狂暴术
            if (_pc.hasSkillEffect(BERSERKERS)) {
                tempDmg -= 5;
            }
            final double diffDmg = dmg - tempDmg;
            dmg = (tempDmg * dmgDouble) + diffDmg;
        }
        return dmg;
    }

    /**
     * 祝福武器 银/米索莉/奥里哈鲁根材质武器<BR>
     * 其他属性定义
     * 
     * @return
     */
    private int calcMaterialBlessDmg() {
        int damage = 0;
        if (_pc.getWeapon() != null) {
            final int undead = _targetNpc.getNpcTemplate().get_undead();
            switch (undead) {
                case 1:// 不死系
                    if ((_weaponMaterial == 14) || (_weaponMaterial == 17)
                            || (_weaponMaterial == 22)) {// 银/米索莉/奥里哈鲁根
                        damage += _random.nextInt(20) + 1;
                    }
                    if (_weaponBless == 0) { // 祝福武器
                        damage += _random.nextInt(4) + 1;
                    }
                    switch (_weaponType) {
                        case 20:
                        case 62:
                            break;
                        default:
                            if (_weapon.getHolyDmgByMagic() != 0) {
                                damage += _weapon.getHolyDmgByMagic();// 武器强化魔法
                            }
                            break;
                    }
                    break;
                case 2:// 恶魔系
                    if ((_weaponMaterial == 17) || (_weaponMaterial == 22)) {// 米索莉/奥里哈鲁根
                        damage += _random.nextInt(3) + 1;
                    }
                    if (_weaponBless == 0) { // 祝福武器
                        damage += _random.nextInt(4) + 1;
                    }
                    break;
                case 3:// 僵尸系
                    if ((_weaponMaterial == 14) || (_weaponMaterial == 17)
                            || (_weaponMaterial == 22)) {// 银/米索莉/奥里哈鲁根
                        damage += _random.nextInt(20) + 1;
                    }
                    if (_weaponBless == 0) { // 祝福武器
                        damage += _random.nextInt(4) + 1;
                    }
                    switch (_weaponType) {
                        case 20:
                        case 62:
                            break;
                        default:
                            if (_weapon.getHolyDmgByMagic() != 0) {
                                damage += _weapon.getHolyDmgByMagic();// 武器强化魔法
                            }
                            break;
                    }
                    break;
                case 5:// 狼人系
                    if ((_weaponMaterial == 14) || (_weaponMaterial == 17)
                            || (_weaponMaterial == 22)) {// 银/米索莉/奥里哈鲁根
                        damage += _random.nextInt(20) + 1;
                    }
                    break;
            }
        }
        return damage;
    }

    /**
     * 武器の属性强化による追加ダメージ算出
     * 
     * @return
     */
    private int calcAttrEnchantDmg() {
        int damage = 0;
        switch (this._weaponAttrEnchantLevel) {
            case 1:
                damage = 1;
                break;

            case 2:
                damage = 3;
                break;

            case 3:
                damage = 5;
                break;
                
            case 4:
                damage = 7;
                break;
                
            case 5:
                damage = 9;
                break;
        }

        // 对地火火风抗性的处理
        int resist = 0;
        switch (this._calcType) {
            /*case PC_PC:
                switch (this._weaponAttrEnchantKind) {
                    case 1: // 地
                        resist = this._targetPc.getEarth();
                        break;

                    case 2: // 火
                        resist = this._targetPc.getFire();
                        break;

                    case 4: // 水
                        resist = this._targetPc.getWater();
                        break;

                    case 8: // 风
                        resist = this._targetPc.getWind();
                        break;

                    case 16: // 光
                        resist = this._targetPc.getEarth();
                        break;

                    case 32: // 暗
                        resist = this._targetPc.getFire();
                        break;

                    case 64: // 圣
                        resist = this._targetPc.getWater();
                        break;

                    case 128: // 邪
                        resist = this._targetPc.getWind();
                        break;
                }
                break;*/

            case PC_NPC:
                switch (this._weaponAttrEnchantKind) {
                    case 1: // 地
                        resist = this._targetNpc.getEarth();
                        break;

                    case 2: // 火
                        resist = this._targetNpc.getFire();
                        break;

                    case 4: // 水
                        resist = this._targetNpc.getWater();
                        break;

                    case 8: // 风
                        resist = this._targetNpc.getWind();
                        break;

                    case 16: // 光
                        resist = this._targetNpc.getEarth();
                        break;

                    case 32: // 暗
                        resist = this._targetNpc.getFire();
                        break;

                    case 64: // 圣
                        resist = this._targetNpc.getWater();
                        break;

                    case 128: // 邪
                        resist = this._targetNpc.getWind();
                        break;
                }
                break;
        }

        int resistFloor = (int) (0.32 * Math.abs(resist));

        if (resist < 0) {
            resistFloor *= -1;
        }

        final double attrDeffence = resistFloor / 32.0;
        final double attrCoefficient = 1 - attrDeffence;

        damage *= attrCoefficient;

        return damage;
    }

    /**
     * 魔力夺取武器 MP夺取质计算
     */
    @Override
    public void calcStaffOfMana() {
        switch (this._weaponId) {
            case 126: // 玛那魔杖
            case 127: // 钢铁玛那魔杖
                int som_lvl = this._weaponEnchant + 3; // 最大MP吸收量を设定
                if (som_lvl < 0) {
                    som_lvl = 0;
                }
                // MP修收量取得(最大吸收9)
                this._drainMana = Math.min(_random.nextInt(som_lvl) + 1, 9);
                break;

            case 259: // 魔力短剑
                switch (this._calcType) {
                    case PC_PC:
                        if (this._targetPc.getMr() <= _random.nextInt(100) + 1) { // 确率はターゲットのMRに依存
                            this._drainMana = 1; // 吸收量は1固定
                        }
                        break;

                    case PC_NPC:
                        if (this._targetNpc.getMr() <= _random.nextInt(100) + 1) { // 确率はターゲットのMRに依存
                            this._drainMana = 1; // 吸收量は1固定
                        }
                        break;
                }
                break;
        }
    }

    /**
     * HP吸收量算出
     * 
     * @param dmg
     * @return
     */
    private double calcDestruction(final double dmg) {
        // HP吸收量(最小1)
        _drainHp = Math.max((int) ((dmg / 8) + 1), 1);
        return _drainHp;
    }

    /**
     * PC附加毒性攻击
     * 
     * @param attacker
     * @param target
     */
    private void addPcPoisonAttack(final L1Character target) {
        boolean isCheck = false;
        switch (_weaponId) {
            case 0:// 空手
                break;

            case 13:// 死亡之指
            case 14:// 混沌之刺
                isCheck = true;
                break;

            default:
                if (_pc.hasSkillEffect(ENCHANT_VENOM)) {
                    isCheck = true;
                }
                break;
        }

        if (isCheck) {
            final int chance = _random.nextInt(100) + 1;
            if (chance <= 10) {
                // 通常毒、3秒周期、ダメージHP-5
                L1DamagePoison.doInfection(_pc, target, 3000, 5);
            }
        }
    }

    /**
     * 底比斯武器魔法的效果
     */
    @Override
    public void addChaserAttack() {
        /*int mr = 0;
        switch (_calcType) {
            case PC_PC:
                mr = _targetPc.getMr() - (2 * _pc.getOriginalMagicHit());
                break;

            case PC_NPC:
                mr = _targetNpc.getMr() - (2 * _pc.getOriginalMagicHit());
                break;
        }

		//double probability = 3 + (_pc.getTrueSp() * 0.25);
		double probability = 6; //固定DBS武器的魔法机率 set hjx1000
		probability -= (mr / 10) * 0.1;*/
    	int rnd = 0;
    	if (_pc.getSp() > 22) {
    		rnd = (_pc.getSp() - 22) >> 1;
           //System.out.println("rnd==:"+ rnd);
    	}
        switch (_weaponId) {
            case 265:// 底比斯欧西里斯双刀
            case 266:// 底比斯欧西里斯双手剑
            case 267:// 底比斯欧西里斯弓
            case 268:// 底比斯欧西里斯魔杖
                if (5 + rnd >= _random.nextInt(100) + 1) {
                    final L1Chaser chaser = new L1Chaser(_pc, _target);
                    chaser.begin();
                }
                break;
            case 276: //库库尔坎之矛
            case 277: //库库尔坎之铁手甲
                if (5 + rnd >= _random.nextInt(100) + 1) {
                    final L1Chaser chaser0 = new L1Chaser(_pc, _target);
                    chaser0.begin();
                }
            	break;
            case 304:
            case 307:
            case 308:
                if (5 + rnd >= _random.nextInt(100) + 1) {
                    final L1Chaser1 chaser1 = new L1Chaser1(_pc, _target);
                    chaser1.begin();
                }
            	break;
            case 305:
            case 306:
            case 309:
                if (5 + rnd >= _random.nextInt(100) + 1) {
                    final L1Chaser1 chaser2 = new L1Chaser1(_pc, _target);
                    chaser2.begin();
                }
            	break;
            	
        }
    }

    /**
     * 攻击资讯送出
     */
    @Override
    public void action() {
        try {
            if (_pc == null) {
                return;
            }
            if (_target == null) {
                return;
            }
            // 改变面向
            _pc.setHeading(_pc.targetDirection(_targetX, _targetY));

            if (_weaponRange == -1) {// 远距离武器
                actionX1();

            } else {// 近距离武器
                actionX2();
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 近距离武器/空手
     */
    private void actionX2() {
        try {
            if (_isHit) {// 命中
                // System.out.println("命中");
                _pc.sendPacketsX10(new S_AttackPacketPc(_pc, _target,
                        _attackType, _damage));

            } else {// 未命中
                if (_targetId > 0) {
                    _pc.sendPacketsX10(new S_AttackPacketPc(_pc, _target));

                } else {
                    _pc.sendPacketsAll(new S_AttackPacketPc(_pc));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 远距离武器
     */
    private void actionX1() {
        try {
            switch (_weaponType) {
                case 20:// 弓
                    switch (_weaponId) {
                        case 190: // 沙哈之弓 不具有箭
                            if (_arrow != null) { // 具有箭
                                _pc.getInventory().removeItem(_arrow, 1);
                            }
                            _pc.sendPacketsX10(new S_UseArrowSkill(_pc,
                                    _targetId, 2349, _targetX, _targetY,
                                    _damage));
                            break;

                        default:// 其他武器 没有箭
                            if (_arrow != null) { // 具有箭
                                int arrowGfxid = 66;
                                switch (_pc.getTempCharGfx()) {
                                    case 8842:
                                    case 8900:// 海露拜
                                        arrowGfxid = 8904;// 黑
                                        break;

                                    case 8913:
                                    case 8845:// 朱里安
                                        arrowGfxid = 8916;// 白
                                        break;

                                    case 7959:
                                    case 7967:
                                    case 7968:
                                    case 7969:
                                    case 7970:// 天上骑士
                                        arrowGfxid = 7972;// 火
                                        break;
                                }
                                _pc.sendPacketsX10(new S_UseArrowSkill(_pc,
                                        _targetId, arrowGfxid, _targetX,
                                        _targetY, _damage));
                                _pc.getInventory().removeItem(_arrow, 1);

                            } else {
                                int aid = 1;
                                // 外型编号改变动作
                                if (_pc.getTempCharGfx() == 3860) {
                                    aid = 21;
                                }
                                _pc.sendPacketsAll(new S_ChangeHeading(_pc));
                                // 送出封包(动作)
                                _pc.sendPacketsAll(new S_DoActionGFX(_pc
                                        .getId(), aid));
                            }
                    }
                    break;

                case 62: // 铁手甲
                    if (_sting != null) {// 具有飞刀
                        int stingGfxid = 2989;
                        switch (_pc.getTempCharGfx()) {
                            case 8842:
                            case 8900:// 海露拜
                                stingGfxid = 8904;// 黑
                                break;

                            case 8913:
                            case 8845:// 朱里安
                                stingGfxid = 8916;// 白
                                break;

                            case 7959:
                            case 7967:
                            case 7968:
                            case 7969:
                            case 7970:// 天上骑士
                                stingGfxid = 7972;// 火
                                break;
                        }
                        _pc.sendPacketsX10(new S_UseArrowSkill(_pc, _targetId,
                                stingGfxid, _targetX, _targetY, _damage));
                        _pc.getInventory().removeItem(_sting, 1);

                    } else {// 没有飞刀
                        int aid = 1;
                        // 外型编号改变动作
                        if (_pc.getTempCharGfx() == 3860) {
                            aid = 21;
                        }

                        _pc.sendPacketsAll(new S_ChangeHeading(_pc));
                        // 送出封包(动作)
                        _pc.sendPacketsAll(new S_DoActionGFX(_pc.getId(), aid));
                    }
                    break;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 计算结果
     */
    @Override
    public void commit() {
        if (_isHit) {
            switch (_calcType) {
                case PC_PC:
                    commitPc();
//                    if (_pc.showDmg()) {
//                    	final int targetId = _targetPc.getId();
//                    	final String dmg = "" + _damage;
//                    	_pc.sendPackets(new S_TrueTarget(targetId, dmg));
//                    }
                    break;

                case PC_NPC:
                    commitNpc();
//                    if (_pc.showDmg()) {
//                    	final int targetId = _targetNpc.getId();
//                    	final String dmg = "" + _damage;
//                    	_pc.sendPackets(new S_TrueTarget(targetId, dmg));
//                    }
                    break;
            }
        }

        // gm攻击资讯
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

        final String srcatk = _pc.getName();// 攻击者
        String tgatk = "";// 被攻击者
        String hitinfo = "";// 资讯
        String dmginfo = "";// 伤害
        String x = "";// 最终资讯

        switch (this._calcType) {
            case PC_PC:
                tgatk = _targetPc.getName();
                hitinfo = " 命中:" + _hitRate + "% 剩余hp:"
                        + _targetPc.getCurrentHp();
                dmginfo = _isHit ? "伤害:" + _damage : "失败";
                x = srcatk + ">" + tgatk + " " + dmginfo + hitinfo;
                if (_pc.isGm()) {
                    // 166 \f1%0%s %4%1%3 %2。
                    _pc.sendPackets(new S_ServerMessage(166, "对PC送出攻击: " + x));
                }

                if (_targetPc.isGm()) {
                    // 166 \f1%0%s %4%1%3 %2。
                    _targetPc.sendPackets(new S_ServerMessage(166, "受到PC攻击: "
                            + x));
                }
                break;

            case PC_NPC:
                tgatk = this._targetNpc.getName();
                hitinfo = " 命中:" + this._hitRate + "% 剩余hp:"
                        + this._targetNpc.getCurrentHp();
                dmginfo = this._isHit ? "伤害:" + this._damage : "失败";
                x = srcatk + ">" + tgatk + " " + dmginfo + hitinfo;
                if (this._pc.isGm()) {
                    // 166 \f1%0%s %4%1%3 %2。
                    this._pc.sendPackets(new S_ServerMessage(166, "对NPC送出攻击: "
                            + x));
                }
                break;
        }
    }

    /**
     * 对PC攻击伤害结果
     */
    private void commitPc() {
        if ((_drainMana > 0) && (_targetPc.getCurrentMp() > 0)) {
            if (_drainMana > _targetPc.getCurrentMp()) {
                _drainMana = _targetPc.getCurrentMp();
            }
            short newMp = (short) (_targetPc.getCurrentMp() - _drainMana);
            _targetPc.setCurrentMp(newMp);
            newMp = (short) (this._pc.getCurrentMp() + _drainMana);
            _pc.setCurrentMp(newMp);
        }

        if (_drainHp > 0) { // HP吸收回复
            final short newHp = (short) (_pc.getCurrentHp() + _drainHp);
            _pc.setCurrentHp(newHp);
        }

        damagePcWeaponDurability(); // 武器受到伤害
        _targetPc.receiveDamage(_pc, _damage, false, false);
    }

    /**
     * 对NPC攻击伤害结果
     */
    private void commitNpc() {
        if (_drainMana > 0) {
            final int drainValue = _targetNpc.drainMana(_drainMana);
            final int newMp = _pc.getCurrentMp() + drainValue;
            _pc.setCurrentMp(newMp);
            if (drainValue > 0) {
                final int newMp2 = _targetNpc.getCurrentMp() - drainValue;
                _targetNpc.setCurrentMpDirect(newMp2);
            }
        }

        if (this._drainHp > 0) { // HP吸收回复
            final short newHp = (short) (_pc.getCurrentHp() + _drainHp);
            _pc.setCurrentHp(newHp);
        }

        //damageNpcWeaponDurability(); // 武器受到伤害  hjx1000 暂时关闭坏刀
        _targetNpc.receiveDamage(_pc, _damage);
    }

    /**
     * 相手の攻击に对してカウンターバリアが有效かを判别
     */
    @Override
    public boolean isShortDistance() {
        boolean isShortDistance = true;
        if ((_weaponType == 20) || (_weaponType == 62)) { // 弓かガントレット
            isShortDistance = false;
        }
        return isShortDistance;
    }

    /**
     * 反击屏障的伤害反击
     */
    @Override
    public void commitCounterBarrier() {
        int damage = calcCounterBarrierDamage();
        if (damage == 0) {
            return;
        }
        if (_pc.hasSkillEffect(IMMUNE_TO_HARM)) { //攻击方有圣洁界反击伤害减半 hjx1000
        	damage *= 0.7;
        }
        // 受伤动作
        _pc.sendPacketsAll(new S_DoActionGFX(_pc.getId(),
                ActionCodes.ACTION_Damage));
        _pc.receiveDamage(_target, damage, false, true);
        if (!_pc.isHardDelay()) { //动作延时 hjx1000
        	HardDelay.onHardUse(_pc, 150);
        }
    }

    /**
     * 武器受到伤害 对NPCの场合、损伤确率は10%とする。祝福武器は3%とする。
     */
    private void damageNpcWeaponDurability() {
        /*
         * 损伤しないNPC、素手、损伤しない武器使用、SOF中の场合何もしない。
         */
        if (this._calcType != PC_NPC) {
            return;
        }

        if (this._targetNpc.getNpcTemplate().is_hard() == false) {
            return;
        }

        if (this._weaponType == 0) {
            return;
        }
        
		if (this._weapon.get_power_name() != null) { //鉴定为星武器怪物不会坏刀 hjx1000
			return;
		}

        if (this._weapon.getItem().get_canbedmg() == 0) {
            return;
        }

        if (this._pc.hasSkillEffect(SOUL_OF_FLAME)) {
            return;
        }

        final int random = _random.nextInt(100) + 1;
        switch (this._weaponBless) {
            case 0:// 祝福
                if (random < 3) {
                    // \f1你的%0%s坏了。
                    this._pc.sendPackets(new S_ServerMessage(268, this._weapon
                            .getLogName()));
                    this._pc.getInventory().receiveDamage(this._weapon);
                }
                break;

            case 1:// 一般
            case 2:// 诅咒
                if (random < 5) {
                    // \f1你的%0%s坏了。
                    this._pc.sendPackets(new S_ServerMessage(268, this._weapon
                            .getLogName()));
                    this._pc.getInventory().receiveDamage(this._weapon);
                }
                break;
        }
    }

    /**
     * バウンスアタックにより武器受到伤害 バウンスアタックの损伤确率は10%
     */
    private void damagePcWeaponDurability() {
        if (this._calcType != PC_PC) {
            return;
        }

        if (this._weaponType == 0) {
            return;
        }

        if (this._weaponType == 20) {
            return;
        }

        if (this._weaponType == 62) {
            return;
        }

        if (this._targetPc.hasSkillEffect(BOUNCE_ATTACK) == false) {
            return;
        }

        if (this._pc.hasSkillEffect(SOUL_OF_FLAME)) {
            return;
        }

        if (_random.nextInt(100) + 1 <= 4) {
            // \f1你的%0%s坏了。
            this._pc.sendPackets(new S_ServerMessage(268, this._weapon
                    .getLogName()));
            this._pc.getInventory().receiveDamage(this._weapon);
        }
    }
}
