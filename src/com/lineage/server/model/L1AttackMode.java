package com.lineage.server.model;

import java.util.ConcurrentModificationException;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 攻击判定
 * 
 * @author dexc
 * 
 */
public abstract class L1AttackMode {

    private static final Log _log = LogFactory.getLog(L1AttackMode.class);

    // 目标物件
    protected L1Character _target = null;

    // 执行PC
    protected L1PcInstance _pc = null;

    // 目标PC
    protected L1PcInstance _targetPc = null;

    // 执行NPC
    protected L1NpcInstance _npc = null;

    // 目标NPC
    protected L1NpcInstance _targetNpc = null;

    protected int _targetId;

    protected int _targetX;

    protected int _targetY;

    protected int _statusDamage = 0;

    protected int _hitRate = 0;

    protected int _calcType;

    protected static final int PC_PC = 1;

    protected static final int PC_NPC = 2;

    protected static final int NPC_PC = 3;

    protected static final int NPC_NPC = 4;

    protected boolean _isHit = false;

    protected int _damage = 0;

    protected int _drainMana = 0;

    protected int _drainHp = 0;

    protected int _attckGrfxId = 0;

    protected int _attckActId = 0;

    // 攻击者がプレイヤーの场合の武器情报
    protected L1ItemInstance _weapon = null;

    protected int _weaponId = 0;

    protected int _weaponType = 0;

    protected int _weaponType2 = 0;

    protected int _weaponAddHit = 0;// 命中追加

    protected int _weaponAddDmg = 0;// 伤害追加

    protected int _weaponSmall = 0;// 对小型

    protected int _weaponLarge = 0;// 对大型

    protected int _weaponRange = 1;// 武器攻击距离

    protected int _weaponBless = 1;// 祝福类型

    protected int _weaponEnchant = 0;// 强化质

    protected int _weaponMaterial = 0;// 材质

    protected int _weaponDoubleDmgChance = 0;

    protected int _weaponAttrEnchantKind = 0;

    protected int _weaponAttrEnchantLevel = 0;

    protected L1ItemInstance _arrow = null;

    protected L1ItemInstance _sting = null;

    protected int _leverage = 10; // 攻击倍率(1/10)

    protected static final Random _random = new Random();

    /**
     * 血盟技能伤害增加
     * 
     * @return
     */
    protected static double getDamageUpByClan(final L1PcInstance pc) {
        double dmg = 0.0;
        try {
            if (pc == null) {
                return 0.0;
            }
            L1Clan clan = pc.getClan();
            if (clan == null) {
                return 0.0;
            }
            // 具有血盟技能
            if (clan.isClanskill()) {
                // 1:狂暴(增加物理攻击力)
                if (pc.get_other().get_clanskill() == 1) {
                    final int clanMan = clan.getOnlineClanMemberSize();
                    dmg += (0.25 * clanMan);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return dmg;
    }

    /**
     * 血盟技能伤害减免
     * 
     * @param targetPc
     * @return
     */
    protected static double getDamageReductionByClan(final L1PcInstance targetPc) {
        double dmg = 0.0;
        try {
            if (targetPc == null) {
                return 0.0;
            }
            L1Clan clan = targetPc.getClan();
            if (clan == null) {
                return 0.0;
            }
            // 具有血盟技能
            if (clan.isClanskill()) {
                // 2:寂静(增加物理伤害减免)
                if (targetPc.get_other().get_clanskill() == 2) {
                    final int clanMan = clan.getOnlineClanMemberSize();
                    dmg += (0.25 * clanMan);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return dmg;
    }

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
        }
        return false;
    }

    /**
     * 技能增加闪避
     * 
     * @param character
     * @return
     */
    protected static int attackerDice(final L1Character character) {
        /*
         * int attackerDice = 0; try { if (character == null) { return 0; }
         * 
         * if (character.isDead()) {// 死亡 return 0; }
         * 
         * if (character.getSkillisEmpty()) {// 无技能状态 return 0; }
         * 
         * if (character.getSkillEffect().size() <= 0) {// 无技能状态 return 0; }
         * 
         * for (final Integer key : character.getSkillEffect()) { final Integer
         * integer = L1AttackList.SKU3.get(key); if (integer != null) {
         * attackerDice += integer; } }
         * 
         * } catch (final ConcurrentModificationException e) { // 技能取回发生其他线程进行修改
         * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e);
         * }
         */
        int attackerDice = 0;
        if (character.get_dodge() > 0) {
            attackerDice -= character.get_dodge();
        }
        if (character.get_dodge_down() > 0) {
            attackerDice += character.get_dodge_down();
        }
        return attackerDice;
    }

    /**
     * 攻击倍率(1/10)
     * 
     * @param i
     */
    public void setLeverage(final int i) {
        _leverage = i;
    }

    /**
     * 攻击倍率(1/10)
     * 
     * @return
     */
    protected int getLeverage() {
        return _leverage;
    }

    public void setActId(final int actId) {
        _attckActId = actId;
    }

    public void setGfxId(final int gfxId) {
        _attckGrfxId = gfxId;
    }

    public int getActId() {
        return _attckActId;
    }

    public int getGfxId() {
        return _attckGrfxId;
    }

    /**
     * ER回避率
     * 
     * @return true:命中 false:未命中
     */
    protected boolean calcErEvasion() {
        final int er = _targetPc.getEr();
        final int rnd = _random.nextInt(100) + 1;
        return er < rnd;
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
     * PC_PC减伤装备PK伤害直减低
     * 
     * @return
     */
    protected int calcPcDefense() {
        try {
            if (_targetPc != null) {
                final int dmb = _targetPc.getDamageReductionByArmor() << 1;

//                final int acDefMax = _targetPc.getClassFeature()
//                        .getAcDefenseMax(ac);
                //final int acDefMax = ac
                if (dmb > 0) {
                    // (>> 1: 除) (<< 1: 乘) XXX
                    //final int srcacd = Math.max(1, (acDefMax >> 3));
                    //final int acdown = _random.nextInt(acDefMax) + srcacd;
                    // System.out.println("acdown:"+acdown);
                	//修改为以下的伤害方式 hjx1000
                	final int acdown = _random.nextInt(dmb);//hjx1000
                    return acdown;
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return 0;
    }
    
    /**
     * NPC_PC防御力伤害直减低
     * 
     * @return
     */
    protected int NpccalcPcDefense() {
        try {
            if (_targetPc != null) {
                final int ac = (Math.max(0, 10 - _targetPc.getAc()));
                //final int dmb = _targetPc.getDamageReductionByArmor() << 1;

                final int acDefMax = _targetPc.getClassFeature()
                        .getAcDefenseMax(ac);
                if (acDefMax != 0) {
                    // (>> 1: 除) (<< 1: 乘) XXX
                    final int srcacd = Math.max(1, (acDefMax >> 1));
                    final int acdown = _random.nextInt(acDefMax + srcacd);
                    // System.out.println("acdown:"+acdown);
                    return acdown;
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return 0;
    }

    /**
     * (NPC防御力 + 额外伤害减低) 伤害减低
     * 
     * @return
     */
    protected int calcNpcDamageReduction() {
        // TEST
        int damagereduction = _targetNpc.getNpcTemplate().get_damagereduction();// 额外伤害减低
        try {
            final int srcac = _targetNpc.getAc();
            final int ac = Math.max(0, 10 - srcac);

            final int acDefMax = ac / 7;// 防御力伤害减免降低1/7 XXX
            if (acDefMax != 0) {
                final int srcacd = Math.max(1, acDefMax);// XXX
                return _random.nextInt(acDefMax) + srcacd + damagereduction;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }

        return damagereduction;
    }

    /**
     * 反击屏障的伤害反击计算
     * 
     * @return
     */
    protected int calcCounterBarrierDamage() {
        int damage = 0;
        int xing = 0;
        try {
            // 反击对象是PC
            if (_targetPc != null) {
                final L1ItemInstance weapon = _targetPc.getWeapon();
                if (weapon != null) {
                    if (weapon.getItem().getType() == 3) { // 双手剑
                        // (BIG最大ダメージ+强化数+追加ダメージ)*2
                        // (>> 1: 除) (<< 1: 乘)
                    	if (weapon.get_power_name() != null) {
                    		xing = weapon.get_power_name().get_hole_count();
                    	}
                        damage = (weapon.getItem().getDmgLarge() + xing
                                + weapon.getEnchantLevel() + weapon.getItem()
                                .getDmgModifier()) << 1;// * 2;
                    }
                }

                // 反击对象是NPC
            } else if (_targetNpc != null) {
                // (>> 1: 除) (<< 1: 乘)
                damage = (_targetNpc.getStr() + _targetNpc.getLevel()) << 1;// *
                                                                            // 2;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return damage;
    }

    /**
     * 纹章伤害减免
     * 
     * @return
     */
    protected double coatArms() {
        int damage = 100;
        try {
            if (_targetPc != null) {
                damage -= _targetPc.get_dmgDown();
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return (double) damage / 100;
    }

    public abstract boolean calcHit();

    /**
     * 攻击资讯送出
     */
    public abstract void action();

    /**
     * 伤害计算
     * 
     * @return
     */
    public abstract int calcDamage();

    /**
     * 底比斯武器魔法的效果
     */
    public abstract void addChaserAttack();

    /**
     * 武器MP吸收量计算
     */
    public abstract void calcStaffOfMana();

    /**
     * 计算结果反映
     */
    public abstract void commit();

    /**
     * 受到反击屏障伤害表示
     */
    // public abstract void actionCounterBarrier();

    /**
     * 攻击使用武器是否为近距离武器判断
     * 
     * @return
     */
    public abstract boolean isShortDistance();

    /**
     * 反击屏障的伤害反击
     */
    public abstract void commitCounterBarrier();

    /**
     * 疼痛的欢愉的伤害反击
     */
    // public abstract void commitJoyOfPain();

    /**
     * 致命身躯的伤害反击
     */
    // public abstract void commitMortalBody();

}
