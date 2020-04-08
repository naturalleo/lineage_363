package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.BERSERKERS;

import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.ActionCodes;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.serverpackets.S_TrueTarget;
import com.lineage.server.templates.L1ItemPower_name;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.thread.GeneralThreadPool;
import static com.lineage.server.model.skill.L1SkillId.IMMUNE_TO_HARM;;

/**
 * 隐藏的魔族武器魔法的效果
 * 
 * @author dexc
 * 
 */
public class L1Chaser1 extends TimerTask {

    private static final Log _log = LogFactory.getLog(L1Chaser1.class);

    private static final Random _random = new Random();
    private ScheduledFuture<?> _future = null;
    private int _timeCounter = 0;
    private final L1PcInstance _pc;
    private final L1Character _cha;

    public L1Chaser1(final L1PcInstance pc, final L1Character cha) {
        this._cha = cha;
        this._pc = pc;
    }

    @Override
    public void run() {
        try {
            if ((this._cha == null) || this._cha.isDead()) {
                this.stop();
                return;
            }
            this.attack();
            this._timeCounter++;
            if (this._timeCounter >= 3) {
                this.stop();
                return;
            }

        } catch (final Throwable e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void begin() {
        // 效果时间が8秒のため、4秒每のスキルの场合处理时间を考虑すると实际には1回しか效果が现れない
        // よって开始时间を0.9秒后に设定しておく
        this._future = GeneralThreadPool.get().scheduleAtFixedRate(this, 0,
                1000);
    }

    public void stop() {
        if (this._future != null) {
            this._future.cancel(false);
        }
    }

    private void attack() {
        double damage = this.getDamage(this._pc, this._cha);
        if ((this._cha.getCurrentHp() - (int) damage <= 0)
                && (this._cha.getCurrentHp() != 1)) {
            damage = this._cha.getCurrentHp() - 1;

        } else if (this._cha.getCurrentHp() == 1) {
            damage = 0;
        }
        
      if (this._pc.getWeapon() == null) { // 修正空手会出错的问题
      damage = 0;   
      } else {
    	  //System.out.println("测试003==:" + damage);
          final int _weaponId = this._pc.getWeapon().getItem().getItemId();
          switch (_weaponId) {
      		case 304:
      		case 307:
      		case 308:
      			this._pc.sendPacketsAll(new S_EffectLocation(this._cha.getX(),
                      this._cha.getY(), 8150));
      			final int couHp = (int) damage;//吸血量 = 魔法伤害
      			if (_cha.getCurrentHp() - couHp <= 0) {
      				_pc.setCurrentHp(_pc.getCurrentHp() + _cha.getCurrentHp());
      			}else {
      				_pc.setCurrentHp(_pc.getCurrentHp() + couHp);
      			}
      			break;
      		case 305:
      		case 306:
      		case 309:
      			this._pc.sendPacketsAll(new S_EffectLocation(this._cha.getX(),
                      this._cha.getY(), 8152));
      			final int couMp = (int) damage >> 2;//吸魔量 = 魔法伤害除以4
      			if (_cha.getCurrentMp() - couMp <= 0) {
      				_pc.setCurrentMp(_pc.getCurrentMp() + _cha.getCurrentMp());
      				_cha.setCurrentMp(0);
      			}else {
      				_cha.setCurrentMp(_cha.getCurrentMp() - couMp);
      				_pc.setCurrentMp(_pc.getCurrentMp() + couMp);
      			}
      			break;
      		default:
      			damage = 0;
      			break;
          	}
      	}

        if (this._cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) this._cha;
            pc.sendPacketsAll(new S_DoActionGFX(pc.getId(),
                    ActionCodes.ACTION_Damage));
            pc.receiveDamage(this._pc, damage, false, false);
//        	final int targetId = this._cha.getId();
//        	final String dmg = "" + (int) damage;
//        	this._pc.sendPackets(new S_TrueTarget(targetId, dmg));

        } else if (this._cha instanceof L1NpcInstance) {
            final L1NpcInstance npc = (L1NpcInstance) this._cha;
            npc.broadcastPacketX10(new S_DoActionGFX(npc.getId(),
                    ActionCodes.ACTION_Damage));
            npc.receiveDamage(this._pc, (int) damage);
//        	final int targetId = this._cha.getId();
//        	final String dmg = "" + (int) damage;
//        	this._pc.sendPackets(new S_TrueTarget(targetId, dmg));
        }
    }

    private double getDamage(final L1PcInstance pc, final L1Character cha) {
        double dmg = 0;
        final int spByItem = pc.getSp() - pc.getTrueSp();
        //final int intel = pc.getInt();//修改为魔攻有关 hjx1000
        final int intel = pc.getSp();
        final int charaIntelligence = pc.getInt() + spByItem - 12;
        final L1ItemPower_name power_name = pc.getWeapon().get_power_name();//hjx1000
        //final int attr = _random.nextInt(4) + 1;//随机的魔法属性 hjx1000

        double coefficientA = 1 + 3.0 / 32.0 * charaIntelligence;
        if (coefficientA < 1) {
            coefficientA = 1;
        }

        double coefficientB = 0;
        if (intel > 25) {
			//coefficientB = 25 * 0.065;
			coefficientB = (intel + 2.0) / intel; //修改DBS武器攻击 set hjx1000

        } else if (intel <= 12) {
            coefficientB = 12 * 0.065;

        } else {
            coefficientB = intel * 0.065;
        }
        double coefficientC = 0;
        if (intel > 25) {
            coefficientC = 25;

        } else if (intel <= 12) {
            coefficientC = 12;

        } else {
            coefficientC = intel;
        }
        double bsk = 0;
        if (pc.hasSkillEffect(BERSERKERS)) {
            bsk = 0.1;
        }
        dmg = (_random.nextInt(6) + 1 + 5) * (1 + bsk) * coefficientA
                * coefficientB / 10.5 * coefficientC * 1.5;
        if (power_name != null) { //星武器额外增加魔法武器的伤害
        	dmg *= (double)power_name.get_xing_count() * 0.02 + 1;
        }
		if (cha.hasSkillEffect(IMMUNE_TO_HARM)) { //圣洁界 hjx1000
			dmg /= 2.0;
		}

        return L1WeaponSkill.calcDamageReduction(pc, cha, dmg, L1Skills.ATTR_WATER);
    }

}
