package com.lineage.server.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 魔法攻击判定
 * 
 * @author dexc
 * 
 */
public class L1Magic {

    private static final Log _log = LogFactory.getLog(L1Magic.class);

    private L1MagicMode _magicMode;

    public L1Magic(final L1Character attacker, final L1Character target) {
        if (attacker == null) {
            return;
        }

        try {
            if (attacker instanceof L1PcInstance) {
                final L1PcInstance pc = (L1PcInstance) attacker;
                this._magicMode = new L1MagicPc(pc, target);

            } else {
                final L1NpcInstance npc = (L1NpcInstance) attacker;
                this._magicMode = new L1MagicNpc(npc, target);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 设置技能等级-攻击倍率(1/10)
     * 
     * @param i
     */
    public void setLeverage(final int i) {
        this._magicMode.setLeverage(i);
    }

    /**
     * 计算结果反映
     * 
     * @param damage
     * @param drainMana
     */
    public void commit(final int damage, final int drainMana) {
        this._magicMode.commit(damage, drainMana);
    }

    /**
     * 攻击成功的判断 ●●●● 确率系魔法の成功判定 ●●●● 计算方法 攻击侧ポイント：LV + ((MagicBonus * 3) *
     * 魔法固有系数) 防御侧ポイント：((LV / 2) + (MR * 3)) / 2 攻击成功率：攻击侧ポイント - 防御侧ポイント
     * 
     * @param skillId
     * @return
     */
    public boolean calcProbabilityMagic(final int skillId) {
        return this._magicMode.calcProbabilityMagic(skillId);
    }

    /**
     * 魔法伤害值计算
     * 
     * @param skillId
     * @return
     */
    public int calcMagicDamage(final int skillId) {
        return this._magicMode.calcMagicDamage(skillId);
    }

    /**
     * ヒール回复量（对アンデッドにはダメージ）を算出
     * 
     * @param skillId
     * @return
     */
    public int calcHealing(final int skillId) {
        return this._magicMode.calcHealing(skillId);
    }

}
