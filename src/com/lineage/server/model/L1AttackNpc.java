package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.*;

import java.util.ConcurrentModificationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.server.ActionCodes;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.model.poison.L1DamagePoison;
import com.lineage.server.model.poison.L1ParalysisPoison;
import com.lineage.server.model.poison.L1SilencePoison;
import com.lineage.server.serverpackets.S_AttackPacketNpc;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_UseArrowSkill;
import com.lineage.server.serverpackets.S_UseAttackSkill;
import com.lineage.server.timecontroller.pc.HardDelay;
import com.lineage.server.types.Point;

/**
 * 攻击判定
 * 
 * @author dexc
 * 
 */
public class L1AttackNpc extends L1AttackMode {

    private static final Log _log = LogFactory.getLog(L1AttackNpc.class);

    public L1AttackNpc(final L1NpcInstance attacker, final L1Character target) {
        if (attacker == null) {
            return;
        }
        if (target == null) {
            return;
        }
        if (target.isDead()) {
            return;
        }
        if (target.getCurrentHp() <= 0) {
            return;
        }
        _npc = attacker;
        if (target instanceof L1PcInstance) {
            _targetPc = (L1PcInstance) target;
            _calcType = NPC_PC;

        } else if (target instanceof L1NpcInstance) {
            _targetNpc = (L1NpcInstance) target;
            _calcType = NPC_NPC;
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
        if (_target == null) {// 物件遗失
            _isHit = false;
            return _isHit;
        }
        switch (_calcType) {
            case NPC_PC:
                _isHit = calcPcHit();
                break;

            case NPC_NPC:
                _isHit = calcNpcHit();
                break;
        }
        return _isHit;
    }

    /**
     * NPC对PC命中
     * 
     * @return
     */
    private boolean calcPcHit() {
        // 伤害为0
        if (dmg0(_targetPc)) {
            return false;
        }

        // 回避攻击
        if (calcEvasion()) {
            return false;
        }

        // this._hitRate += this._npc.getLevel();// XXX
        _hitRate += _npc.getLevel() + 5;
        _hitRate += _npc.getDex() >> 1;//更改NPC命中与敏捷挂勾， NPC增加命中为 敏捷除以2 hjx1000

        if (_npc instanceof L1PetInstance) { // 宠物武器命中追加
            _hitRate += ((L1PetInstance) _npc).getHitByWeapon();
        }

        _hitRate += _npc.getHitup();

        // int attackerDice = _random.nextInt(20) + 1 + this._hitRate - 1;// XXX
        int attackerDice = _random.nextInt(20) + 1 + _hitRate - 3;

        // 技能增加闪避
        attackerDice += attackerDice(_targetPc);

        // 防御力抵销
        int defenderDice = 0;

        final int defenderValue = (_targetPc.getAc()) * -1;

        if (_targetPc.getAc() >= 0) {
            defenderDice = 10 - _targetPc.getAc();

        } else if (_targetPc.getAc() < 0) {
            defenderDice = 10 + _random.nextInt(defenderValue) + 1;
        }

        // 基础命中
        final int fumble = _hitRate;
        // 基础命中 + 19
        final int critical = _hitRate + 19;
        
		// BOSS XXX
		if (_npc.getNpcTemplate().is_boss()) {
			attackerDice += 20;// 2011-12-10(15)
			//_hitRate += _npc.getLevel();//增加BOSS命中 add hjx1000
		}

        if (attackerDice <= fumble) {
            _hitRate = 15;

        } else if (attackerDice >= critical) {
            _hitRate = 100;

        } else {
            // 防御力抵销
            if (attackerDice > defenderDice) {
                _hitRate = 100;

            } else if (attackerDice <= defenderDice) {
                _hitRate = 15;
            }
        }

//        // BOSS XXX
//        if (_npc.getNpcTemplate().is_boss()) {
//            attackerDice += 30;// 2011-12-10(15)
//        } //改写到上面 hjx1000

        // 比较用机率
        final int rnd = _random.nextInt(100) + 1;

        // NPC攻击距离2格以上附加ER计算
        if ((_npc.get_ranged() >= 10)
                && (_hitRate > rnd)
                && (_npc.getLocation().getTileLineDistance(
                        new Point(_targetX, _targetY)) >= 2)) {
            return calcErEvasion();
        }

        return _hitRate >= rnd;
    }

    /**
     * NPC对NPC命中
     * 
     * @return
     */
    private boolean calcNpcHit() {
        // 伤害为0
        if (dmg0(_targetNpc)) {
            return false;
        }

        // this._hitRate += this._npc.getLevel();// XXX
        _hitRate += _npc.getLevel() + 3;

        if (_npc instanceof L1PetInstance) { // 宠物武器命中追加
            _hitRate += ((L1PetInstance) _npc).getHitByWeapon();
        }

        _hitRate += _npc.getHitup();
        _hitRate += _npc.getDex() >> 1;//更改NPC命中与敏捷挂勾， NPC增加命中为 敏捷除以2 hjx1000
        // int attackerDice = _random.nextInt(20) + 1 + this._hitRate - 1;// XXX
        int attackerDice = _random.nextInt(20) + 1 + _hitRate - 3;

        // 技能增加闪避
        attackerDice += attackerDice(_targetNpc);

        // BOSS XXX
        if (_npc.getNpcTemplate().is_boss()) {
            attackerDice += 30;// 2011-12-10(10)
        }

        int defenderDice = 0;

        final int defenderValue = (_targetNpc.getAc()) * -1;

        if (_targetNpc.getAc() >= 0) {
            defenderDice = 10 - _targetNpc.getAc();

        } else if (_targetNpc.getAc() < 0) {
            defenderDice = 10 + _random.nextInt(defenderValue) + 1;
        }

        final int fumble = _hitRate;
        final int critical = _hitRate + 19;

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

        final int rnd = _random.nextInt(100) + 1;
        return _hitRate >= rnd;
    }

    /**
     * 伤害计算
     */
    @Override
    public int calcDamage() {
        switch (_calcType) {
            case NPC_PC:
                _damage = calcPcDamage();
                break;

            case NPC_NPC:
                _damage = calcNpcDamage();
                break;
        }
        return _damage;
    }

    /**
     * NPC基础伤害提升计算
     * 
     * @return
     */
    private double npcDmgMode(double dmg) {
        // 暴击
        if (_random.nextInt(100) < 15) {
            dmg *= 1.80;
        }

        dmg += _npc.getDmgup();

        if (isUndeadDamage()) {
            dmg *= 1.20;
        }

        dmg = (int) (dmg * (getLeverage() / 10D));

        // BOSS XXX
//        if (_npc.getNpcTemplate().is_boss()) {
//            dmg *= 1.80;// 2011-12-10(1.45)
//        } //取消BOSS增加攻击 hjx1000

//        if (_npc.isWeaponBreaked()) { // ＮＰＣがウェポンブレイク中。
//            dmg /= 2;
//        } //取消NPC坏刀 hjx1000

        return dmg;
    }

    /**
     * NPC对PC伤害
     * 
     * @return
     */
    private int calcPcDamage() {
        if (_targetPc == null) {
            return 0;
        }
        // 伤害为0
        if (dmg0(_targetPc)) {
            _isHit = false;
            return 0;
        }

        if (!_targetPc.hasSkillEffect(attack_ing)) {
        	_targetPc.setSkillEffect(attack_ing, 120000);//检测外挂状态 hjx1000
        }
        final int lvl = _npc.getLevel();
        double dmg = 0D;

        final Integer dmgStr = L1AttackList.STRD.get((int) _npc.getStr());
        dmg = _random.nextInt(lvl) + (_npc.getStr() / 2) + dmgStr;

        if (_npc instanceof L1PetInstance) {
            dmg += (lvl / 7); // 每7级追加1点攻击力 XXX
            dmg += ((L1PetInstance) _npc).getDamageByWeapon();
        }

        // NPC基础伤害提升计算
        dmg = npcDmgMode(dmg);

        dmg -= NpccalcPcDefense();// 被攻击者防御力伤害直减低

        dmg -= _targetPc.getDamageReductionByArmor(); // 防具减伤

        dmg -= _targetPc.dmgDowe(); // 机率伤害减免

//        if (_targetPc.getClanid() != 0) {
//            dmg -= getDamageReductionByClan(_targetPc);// 被攻击者血盟技能伤害减免
//        }

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
            dmg /= 2;
        }

        // ペット、サモンからプレイヤーに攻击
//        boolean isNowWar = false;
//        final int castleId = L1CastleLocation.getCastleIdByArea(_targetPc);
//        if (castleId > 0) {
//            isNowWar = ServerWarExecutor.get().isNowWar(castleId);
//        }
//        if (!isNowWar) {
            if (_npc instanceof L1PetInstance) {
                dmg /= 8;
            }
            if (_npc instanceof L1SummonInstance) {
                final L1SummonInstance summon = (L1SummonInstance) _npc;
                if (summon.isExsistMaster()) {
                    dmg /= 8;
                }
            }
//        } //修改宠物和召唤怪无论是否攻城区都攻击除8 hjx1000

        dmg *= coatArms();

        if (dmg <= 0) {
            _isHit = false;
        } else { 
            if (!_targetPc.isHardDelay()) { //动作延时 hjx1000
            	HardDelay.onHardUse(_targetPc, 150);
            }
        }

        addNpcPoisonAttack(_targetPc);

        // 未命中 伤害归0
        if (!_isHit) {
            dmg = 0.0;
        }

        return (int) dmg;
    }

    /**
     * NPC对NPC伤害
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
            return 0;
        }

        final int lvl = _npc.getLevel();
        double dmg = 0;

        if (_npc instanceof L1PetInstance) {
            dmg = _random.nextInt(_npc.getNpcTemplate().get_level())
                    + (_npc.getStr() / 2) + 1;
            // dmg += (lvl / 16); // 每16级追加1点攻击力// TEST
            dmg += (lvl / 14); // 每14级追加1点攻击力 XXX
            dmg += ((L1PetInstance) _npc).getDamageByWeapon();

        } else {
            final Integer dmgStr = L1AttackList.STRD.get((int) _npc.getStr());
            dmg = _random.nextInt(lvl) + _npc.getStr() / 2 + dmgStr;
        }

        // NPC基础伤害提升计算
        dmg = npcDmgMode(dmg);

        dmg -= calcNpcDamageReduction();// NPC防御力伤害直减低

        addNpcPoisonAttack(_targetNpc);

        if (dmg <= 0) {
            _isHit = false;
        }

        // 未命中 伤害归0
        if (!_isHit) {
            dmg = 0.0;
        }

        return (int) dmg;
    }

    /**
     * ＮＰＣのアンデッドの夜间攻击力の变化
     * 
     * @return
     */
    private boolean isUndeadDamage() {
        boolean flag = false;
        final int undead = this._npc.getNpcTemplate().get_undead();
        final boolean isNight = L1GameTimeClock.getInstance().currentTime()
                .isNight();
        if (isNight) {
            switch (undead) {
                case 1:
                case 3:
                case 4:
                    flag = true;
                    break;
            }
        }
        return flag;
    }

    /**
     * ＮＰＣの毒攻击を付加
     * 
     * @param attacker
     * @param target
     */
    private void addNpcPoisonAttack(final L1Character target) {
        switch (_npc.getNpcTemplate().get_poisonatk()) {
            case 1:// 通常毒
                if (15 >= _random.nextInt(100) + 1) {
                    // 3秒周期でダメージ5
                    L1DamagePoison.doInfection(_npc, target, 3000, 5);
                }
                break;

            case 2:// 沈默毒
                if (15 >= _random.nextInt(100) + 1) {
                    L1SilencePoison.doInfection(target);
                }
                break;

            case 4:// 麻痹毒
                if (15 >= _random.nextInt(100) + 1) {
                    // 20秒后に45秒间麻痹
                    L1ParalysisPoison.doInfection(target, 20000, 45000);
                }
                break;
        }
        if (_npc.getNpcTemplate().get_paralysisatk() != 0) { // 麻痹攻击あり
        }
    }

    /**
     * 攻击资讯送出
     */
    @Override
    public void action() {
        try {
            if (_npc == null) {
                return;
            }
            if (_npc.isDead()) {
                return;
            }

            _npc.setHeading(_npc.targetDirection(_targetX, _targetY)); // 设置新面向

            // 距离2格以上攻击
            final boolean isLongRange = (_npc.getLocation()
                    .getTileLineDistance(new Point(_targetX, _targetY)) > 1);
            int bowActId = _npc.getBowActId();

            // 远距离武器
            if (isLongRange && (bowActId > 0)) {
                actionX1();

                // 近距离武器
            } else {
                actionX2();
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
            int bowActId = _npc.getBowActId();
            // 攻击资讯封包
            _npc.broadcastPacketX10(new S_UseArrowSkill(_npc, _targetId,
                    bowActId, _targetX, _targetY, _damage));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 近距离武器
     */
    private void actionX2() {
        try {
            int actId = 0;
            if (getActId() > 0) {
                actId = getActId();

            } else {
                actId = ActionCodes.ACTION_Attack;
            }

            if (_isHit) {// 命中
                if (getGfxId() > 0) {
                    // 攻击资讯封包
                    _npc.broadcastPacketX10(new S_UseAttackSkill(_target, _npc
                            .getId(), getGfxId(), _targetX, _targetY, actId,
                            _damage));
                } else {
                    gfx7049();
                    _npc.broadcastPacketX10(new S_AttackPacketNpc(_npc,
                            _target, actId, _damage));
                }
                if (_npc.hasSkillEffect(_npc.getId() + 100000)) { //消除20秒不打状态
                	_npc.removeSkillEffect(_npc.getId() + 100000);
                }
            } else {// 未命中
                if (getGfxId() > 0) {
                    // 攻击资讯封包
                    _npc.broadcastPacketX10(new S_UseAttackSkill(_target, _npc
                            .getId(), getGfxId(), _targetX, _targetY, actId, 0));
                } else {
                    _npc.broadcastPacketX10(new S_AttackPacketNpc(_npc,
                            _target, actId));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 幻术师外型 使用古奇兽
     */
    private void gfx7049() {
        if (_npc.getStatus() != 58) {
            return;
        }
        boolean is = false;
        if (_npc.getTempCharGfx() == 6671 && _npc.getGfxId() == 6671) {
            is = true;
        }
        if (_npc.getTempCharGfx() == 6650 && _npc.getGfxId() == 6650) {
            is = true;
        }

        if (is) {// 幻术师外型 使用古奇兽
            _npc.broadcastPacketAll(new S_SkillSound(_npc.getId(), 7049));
        }
    }

    /**
     * 计算结果反映
     */
    @Override
    public void commit() {
        if (_isHit) {
            switch (_calcType) {
                case NPC_PC:
                    commitPc();
                    break;

                case NPC_NPC:
                    commitNpc();
                    break;
            }
        }

        // gm攻击资讯
        if (!ConfigAlt.ALT_ATKMSG) {
            return;

        } else {
            if (_calcType == NPC_NPC) {
                return;
            }
            if (!_targetPc.isGm()) {
                return;
            }
        }

        final String srcatk = _npc.getName();
        ;// 攻击者
        final String tgatk = _targetPc.getName();
        ;// 被攻击者
        final String dmginfo = _isHit ? "伤害:" + _damage : "失败";// 伤害
        final String hitinfo = " 命中:" + _hitRate + "% 剩余hp:"
                + _targetPc.getCurrentHp();// 资讯
        final String x = srcatk + ">" + tgatk + " " + dmginfo + hitinfo;

        _targetPc.sendPackets(new S_ServerMessage(166, "受到NPC攻击: " + x));

    }

    /**
     * 对PC攻击伤害结果
     */
    private void commitPc() {
        _targetPc.receiveDamage(_npc, _damage, false, false);
    }

    /**
     * 对NPC攻击伤害结果
     */
    private void commitNpc() {
        _targetNpc.receiveDamage(_npc, _damage);
    }

    /**
     * 受到反击屏障伤害表示
     */
    /*
     * @Override public void actionCounterBarrier() { // 受伤动作
     * _npc.broadcastPacketAll( new S_DoActionGFX(_npc.getId(),
     * ActionCodes.ACTION_Damage )); }
     */

    /**
     * 相手の攻击に对してカウンターバリアが有效かを判别
     */
    @Override
    public boolean isShortDistance() {
        boolean isShortDistance = true;
        final boolean isLongRange = (_npc.getLocation().getTileLineDistance(
                new Point(_targetX, _targetY)) > 1);
        final int bowActId = _npc.getBowActId();
        // 距离が2以上、攻击者の弓のアクションIDがある场合は远攻击
        if (isLongRange && (bowActId > 0)) {
            isShortDistance = false;
        }
        return isShortDistance;
    }

    /**
     * 反击屏障的伤害反击
     */
    @Override
    public void commitCounterBarrier() {
        final int damage = this.calcCounterBarrierDamage();
        if (damage == 0) {
            return;
        }
        _npc.receiveDamage(_target, damage);
        // 受伤动作
        _npc.broadcastPacketAll(new S_DoActionGFX(_npc.getId(),
                ActionCodes.ACTION_Damage));
        /*
         * if (_targetPc != null) { _npc.receiveDamage(_targetPc, damage); //
         * 受伤动作 _targetPc.sendPacketsAll( new S_DoActionGFX( _targetPc.getId(),
         * ActionCodes.ACTION_Damage ));
         * 
         * } else if (_targetNpc != null) { _npc.receiveDamage(_targetNpc,
         * damage); // 受伤动作 _targetNpc.broadcastPacketAll( new
         * S_DoActionGFX(_targetNpc.getId(), ActionCodes.ACTION_Damage ));
         * 
         * }
         */
    }

    /**
     * 疼痛的欢愉的伤害反击
     */
    /*
     * @Override public void commitJoyOfPain() {
     * 
     * }
     */

    /**
     * 致命身躯的伤害反击
     */
    /*
     * @Override public void commitMortalBody() {
     * 
     * }
     */

    @Override
    public void addChaserAttack() {
        // Auto-generated method stub
    }

    @Override
    public void calcStaffOfMana() {
        // Auto-generated method stub
    }
}
