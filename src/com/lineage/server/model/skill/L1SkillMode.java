package com.lineage.server.model.skill;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static com.lineage.server.model.skill.L1SkillId.*;

import com.lineage.server.model.skill.skillmode.*;

public class L1SkillMode {

    private static final Log _log = LogFactory.getLog(L1SkillMode.class);

    // Map<K,V>
    private static final Map<Integer, SkillMode> _skillMode = new HashMap<Integer, SkillMode>();

    private static L1SkillMode _instance;

    public static L1SkillMode get() {
        if (_instance == null) {
            _instance = new L1SkillMode();
        }
        return _instance;
    }

    /**
     * 不会被相消的技能
     */
    public boolean isNotCancelable(final int skillNum) {
        return (skillNum == ENCHANT_WEAPON) || (skillNum == BLESSED_ARMOR)
                || (skillNum == ABSOLUTE_BARRIER)
                || (skillNum == ADVANCE_SPIRIT) || (skillNum == SHOCK_STUN)
                || (skillNum == SHADOW_FANG) || (skillNum == REDUCTION_ARMOR)
                || (skillNum == SOLID_CARRIAGE)
                || (skillNum == COUNTER_BARRIER)
                || (skillNum == AWAKEN_ANTHARAS)
                || (skillNum == AWAKEN_FAFURION)
                || (skillNum == UNCANNY_DODGE)
                || (skillNum == DRESS_EVASION)
                || (skillNum == SHADOW_ARMOR)
                || (skillNum == PHYSICAL_ENCHANT_STR)
                || (skillNum == PHYSICAL_ENCHANT_DEX)
                || (skillNum == DRESS_HALZ) //黑暗籠罩
                || (skillNum == AWAKEN_VALAKAS)
                || (skillNum == STATUS_CHAT_PROHIBITED)
                || (skillNum == STATUS_CURSE_BARLOG)
                || (skillNum == STATUS_CURSE_YAHEE) || (skillNum == CKEW_LV50)
                || (skillNum == DE_LV30) || (skillNum == AWAKEN_VALAKAS);
    }

    public void load() {
        try {
            // 法师
            _skillMode.put(TELEPORT, new TELEPORT());// 指定传送
            _skillMode.put(MASS_TELEPORT, new MASS_TELEPORT());// 集体传送术
            _skillMode.put(HASTE, new HASTE());// 加速术
            _skillMode.put(CANCELLATION, new CANCELLATION());// 魔法相消术
            _skillMode.put(CURE_POISON, new CURE_POISON());// 解毒术
            _skillMode.put(REMOVE_CURSE, new REMOVE_CURSE());// 圣洁之光
            _skillMode.put(SUMMON_MONSTER, new SUMMON_MONSTER());// 召唤术
            _skillMode.put(RESURRECTION, new RESURRECTION());// 返生术
            _skillMode.put(GREATER_RESURRECTION, new GREATER_RESURRECTION());// 终极返生术
            _skillMode.put(ADVANCE_SPIRIT, new ADVANCE_SPIRIT());// 灵魂升华
            _skillMode.put(CURSE_PARALYZE, new CURSE_PARALYZE());// 木乃伊的诅咒
            _skillMode.put(CURSE_PARALYZE2, new CURSE_PARALYZE());// 魔法效果:麻痹
            _skillMode.put(CURSE_BLIND, new CURSE_BLIND());// 闇盲咒术20
            _skillMode.put(DARKNESS, new CURSE_BLIND());// 黑闇之影40

            // 王族
            _skillMode.put(CALL_CLAN, new CALL_CLAN());// 呼唤盟友
            _skillMode.put(RUN_CLAN, new RUN_CLAN());// 援护盟友
            _skillMode.put(TRUE_TARGET, new TRUE_TARGET());// 精准目标

            // 骑士
            _skillMode.put(SHOCK_STUN, new SHOCK_STUN());// 冲击之晕
            _skillMode.put(SOLID_CARRIAGE, new SOLID_CARRIAGE());// 坚固防护

            // 精灵
            _skillMode.put(CALL_OF_NATURE, new CALL_OF_NATURE());// 生命呼唤
            _skillMode.put(ELEMENTAL_FALL_DOWN, new ELEMENTAL_FALL_DOWN());// 弱化属性
            _skillMode.put(BODY_TO_MIND, new BODY_TO_MIND());// 心灵转换
            _skillMode.put(BLOODY_SOUL, new BLOODY_SOUL());// 魂体转换
            _skillMode.put(TRIPLE_ARROW, new TRIPLE_ARROW());// 三重矢
            _skillMode.put(TELEPORT_TO_MATHER, new TELEPORT_TO_MATHER());// 世界树的呼唤
            _skillMode.put(AQUA_PROTECTER, new AQUA_PROTECTER());// 水之防护
            _skillMode.put(GREATER_ELEMENTAL, new GREATER_ELEMENTAL());// 召唤强力属性精灵
            _skillMode.put(LESSER_ELEMENTAL, new LESSER_ELEMENTAL());// 召唤属性精灵
            _skillMode.put(WIND_SHACKLE, new WIND_SHACKLE());// 风之枷锁
            _skillMode.put(FIRE_BLESS, new FIRE_BLESS());// 烈炎气息

            // 黑暗精灵
            _skillMode.put(UNCANNY_DODGE, new UNCANNY_DODGE());// 暗影闪避106
            _skillMode.put(DARK_BLIND, new CURSE_BLIND());// 暗黑盲咒103

            // 龙骑士
            _skillMode.put(AWAKEN_ANTHARAS, new AWAKEN_ANTHARAS());// 觉醒：安塔瑞斯
            _skillMode.put(AWAKEN_FAFURION, new AWAKEN_FAFURION());// 觉醒：法利昂
            _skillMode.put(AWAKEN_VALAKAS, new AWAKEN_VALAKAS());// 觉醒：巴拉卡斯
            _skillMode.put(FOE_SLAYER, new FOE_SLAYER());// 屠宰者
            _skillMode.put(BLOODLUST, new BLOODLUST());// 血之渴望
            _skillMode.put(RESIST_FEAR, new RESIST_FEAR());// 恐惧无助188

            // 幻术师
            _skillMode.put(CONFUSION, new CONFUSION());// 混乱
            _skillMode.put(PHANTASM, new PHANTASM());// 幻想
            _skillMode.put(ARM_BREAKER, new ARM_BREAKER());// 武器破坏者
            _skillMode.put(PANIC, new PANIC());// 恐慌
            _skillMode.put(BONE_BREAK, new BONE_BREAK());// 骷髅毁坏
            _skillMode.put(MIND_BREAK, new MIND_BREAK());// 心灵破坏
            _skillMode.put(ILLUSION_AVATAR, new ILLUSION_AVATAR());// 幻觉：化身
            _skillMode.put(MIRROR_IMAGE, new MIRROR_IMAGE());// 镜像

            // 魔法效果
            _skillMode.put(STATUS_FREEZE, new STATUS_FREEZE());

            // 道具效果
            _skillMode.put(DRAGON1, new DRAGON1());// 火 额外攻击点+2，持续1200秒
            _skillMode.put(DRAGON2, new DRAGON2());// 地 攻击回避提升 石化耐性+3，持续1200秒
            _skillMode.put(DRAGON3, new DRAGON3());// 水 魔法伤害减免 寒冰耐心+3，持续1200秒
            _skillMode.put(DRAGON4, new DRAGON4());// 风 魔法重击增加 睡眠耐性+3，持续1200秒
            _skillMode.put(DRAGON5, new DRAGON5());// 生命-物理攻击回避率+10% 魔法伤害减免+50
                                                   // 魔法暴击率+1 额外攻击点数+2 防护中毒状态
            _skillMode.put(DRAGON6, new DRAGON6());// 诞生-物理攻击回避率+10% 魔法伤害减免+50
                                                   // 暗黑耐性+3
            _skillMode.put(DRAGON7, new DRAGON7());// 形象-物理攻击回避率+10% 魔法伤害减免+50
                                                   // 魔法暴击率+1 支撑耐性+3

            // NPC 特殊效果
            _skillMode.put(ADLV80_1, new ADLV80_1());// 卡瑞的祝福(地龙副本)
            _skillMode.put(ADLV80_2, new ADLV80_2());// 莎尔的祝福(水龙副本)
            _skillMode.put(AGLV85_1X, new AGLV85_1X());// 莎尔的祝福(水龙副本-强化版)

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        }
    }

    public SkillMode getSkill(final int skillid) {
        return _skillMode.get(skillid);
    }
}
