package com.lineage.server.timecontroller.skill;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.ActionCodes;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1AttackList;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1EffectType;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.timecontroller.pc.HardDelay;
import com.lineage.server.world.WorldEffect;

/**
 * 技能NPC状态送出时间轴 法师技能(火牢)
 * 
 * @author dexc
 * 
 */
public class EffectFirewallTimer extends TimerTask {

    private static final Log _log = LogFactory
            .getLog(EffectFirewallTimer.class);

    private ScheduledFuture<?> _timer;

    private static Random _random = new Random();

    public void start() {
        final int timeMillis = L1EffectInstance.FW_DAMAGE_INTERVAL;
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    @Override
    public void run() {
        try {
            final Collection<L1EffectInstance> allNpc = WorldEffect.get().all();
            // 不包含元素
            if (allNpc.isEmpty()) {
                return;
            }

            for (final Iterator<L1EffectInstance> iter = allNpc.iterator(); iter
                    .hasNext();) {
                final L1EffectInstance effect = iter.next();
                // 不是法师技能(火牢)
                if (effect.effectType() != L1EffectType.isFirewall) {
                    continue;
                }
                // 计算结果
                firewall(effect);
                Thread.sleep(1);
            }

            /*
             * for (final L1EffectInstance effect : allNpc) { // 不是法师技能(火牢) if
             * (effect.effectType() != L1EffectType.isFirewall) { continue; } //
             * 计算结果 firewall(effect); Thread.sleep(1); }
             */

        } catch (final Exception e) {
            _log.error("Npc L1Effect法师技能(火牢)状态送出时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final EffectFirewallTimer firewallTimer = new EffectFirewallTimer();
            firewallTimer.start();
        }
    }

    /**
     * 计算结果
     * 
     * @param effect
     */
    private static void firewall(final L1EffectInstance effect) {
        try {
            // 取回火牢使用者
            final L1PcInstance user = (L1PcInstance) effect.getMaster();

            // 取回目标清单
            final ArrayList<L1Character> list = WorldEffect.get().getFirewall(
                    effect);

            for (final L1Character object : list) {
                // 副本ID不相等
                if (effect.get_showId() != object.get_showId()) {
                    continue;
                }
                // 对象是PC
                if (object instanceof L1PcInstance) {
                    L1PcInstance tgpc = (L1PcInstance) object;
                    topc(user, tgpc);

                    // 对象是怪物
                } else if (object instanceof L1MonsterInstance) {
                    L1MonsterInstance tgmob = (L1MonsterInstance) object;
                    tonpc(user, tgmob);
                }
            }

        } catch (final Exception e) {
            _log.error("Npc L1Effect法师技能(火牢)状态送出时间轴发生异常", e);
            effect.deleteMe();
        }
    }

    /**
     * 对NPC的伤害
     * 
     * @param user
     *            施展者
     * @param objects
     *            对象
     */
    private static void tonpc(final L1PcInstance user,
            final L1MonsterInstance tgmob) {
        // 伤害为0
        if (dmg0(tgmob)) {
            return;
        }

        double attrDeffence = 0;// 伤害减免

        final int weakAttr = tgmob.getFire();
        if (weakAttr > 0) {
            attrDeffence = calcAttrResistance(weakAttr);
        }

        final int srcDmg = 19 + _random.nextInt(Math.max(user.getInt() / 2, 1));
        int damage = (int) ((1.0 - attrDeffence) * srcDmg);

        damage = Math.max(damage, 0);

        if (damage <= 0) {
            return;
        }

        tgmob.broadcastPacketX10(new S_DoActionGFX(tgmob.getId(),
                ActionCodes.ACTION_Damage));
        // 火牢伤害计算直接传回施展者
        tgmob.receiveDamage(user, damage);
    }

    /**
     * 对PC的伤害
     * 
     * @param user
     * @param tgpc
     */
    private static void topc(final L1PcInstance user, final L1PcInstance tgpc) {
        // 相同血盟
        if (user.getClanid() != 0) {
            if (tgpc.getClanid() == user.getClanid()) {
                return;
            }
        }
        // 安全区中
        if (tgpc.isSafetyZone()) {
            return;
        }
        // 伤害为0
        if (dmg0(tgpc)) {
            return;
        }

        double attrDeffence = 0;// 伤害减免

        final int weakAttr = tgpc.getFire();
        if (weakAttr > 0) {
            attrDeffence = calcAttrResistance(weakAttr);
        }

        final int srcDmg = 19 + _random.nextInt(Math.max(user.getInt() / 2, 1));
        int damage = (int) ((1.0 - attrDeffence) * srcDmg);

        damage = Math.max(damage, 0);

        boolean dmgX2 = false;// 伤害除2

        // 取回技能
        if (!tgpc.getSkillisEmpty() && tgpc.getSkillEffect().size() > 0) {
            try {
                for (final Object key : tgpc.getSkillEffect().toArray()) {
                    final Integer integer = L1AttackList.SKD3.get(key);
                    // 伤害减免
                    if (integer != null) {
                        if (integer.equals(key)) {
                            // 技能编号与返回值相等
                            dmgX2 = true;

                        } else {
                            damage += integer;
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
            damage /= 2;
        }

        if (damage <= 0) {
            return;
        }

        tgpc.sendPacketsAll(new S_DoActionGFX(tgpc.getId(),
                ActionCodes.ACTION_Damage));
        // 火牢伤害计算直接传回施展者
        tgpc.receiveDamage(user, damage, false, true);
        if (!tgpc.isHardDelay()) { //动作延时 hjx1000
        	HardDelay.onHardUse(tgpc, 150);
        }
    }

    /**
     * 抗火属性伤害减低 attr:0.无属性魔法,1.地魔法,2.火魔法,4.水魔法,8.风魔法(,16.光魔法)
     */
    private static double calcAttrResistance(final int resist) {
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
     * 伤害为0
     * 
     * @param pc
     * @return true 伤害为0
     */
    private static boolean dmg0(final L1Character character) {
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
}
