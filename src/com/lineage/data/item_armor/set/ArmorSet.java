package com.lineage.data.item_armor.set;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.ArmorSetTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1ArmorSets;

/**
 * 套装设置抽象接口
 * 
 * @author daien
 * 
 */
public abstract class ArmorSet {

    private static final Log _log = LogFactory.getLog(ArmorSet.class);

    /**
     * 套装物品编号阵列
     * 
     * @param pc
     */
    public abstract int[] get_ids();

    /**
     * 传回该套装附加的效果阵列
     * 
     * @return
     */
    public abstract int[] get_mode();

    /**
     * 套装完成效果
     * 
     * @param pc
     */
    public abstract void giveEffect(L1PcInstance pc);

    /**
     * 套装解除效果
     * 
     * @param pc
     */
    public abstract void cancelEffect(L1PcInstance pc);

    /**
     * 套装完成
     * 
     * @param pc
     * @return
     */
    public abstract boolean isValid(L1PcInstance pc);

    /**
     * 是否为套装中组件
     * 
     * @param id
     * @return
     */
    public abstract boolean isPartOfSet(int id);

    /**
     * 是否装备了相同界指2个
     * 
     * @param pc
     * @return
     */
    public abstract boolean isEquippedRingOfArmorSet(L1PcInstance pc);

    /**
     * 全部套装设置
     * 
     * @return
     */
    public static HashMap<Integer, ArmorSet> getAllSet() {
        return _allSet;
    }

    // 全部套装设置
    private static final HashMap<Integer, ArmorSet> _allSet = new HashMap<Integer, ArmorSet>();

    /**
     * 设置资料初始化
     */
    public static void load() {
        try {
            for (final L1ArmorSets armorSets : ArmorSetTable.get().getAllList()) {
                final int id = armorSets.getId();
                int[] gfxs = null;
                if (armorSets.get_gfxs() != null) {
                    gfxs = armorSets.get_gfxs();
                }

                final ArmorSetImpl value = new ArmorSetImpl(id, getArray(id,
                        armorSets.getSets()), gfxs);

                if (armorSets.getPolyId() != -1) {
                    // 套装效果:装备者变形
                    value.addEffect(new EffectPolymorph(armorSets.getPolyId()));
                }

                // 攻击与防御

                if (armorSets.getAc() != 0) {
                    // 套装效果:防御力增加
                    value.addEffect(new EffectAc(armorSets.getAc()));
                }

                if (armorSets.getMr() != 0) {
                    // 套装效果:抗魔增加
                    value.addEffect(new EffectMr(armorSets.getMr()));
                }

                // HP相关

                if (armorSets.getHp() != 0) {
                    // 套装效果:HP增加
                    value.addEffect(new EffectHp(armorSets.getHp()));
                }

                if (armorSets.getHpr() != 0) {
                    // 套装效果:HP回复增加
                    value.addEffect(new EffectHpR(armorSets.getHpr()));
                }

                // MP相关

                if (armorSets.getMp() != 0) {
                    // 套装效果:MP增加
                    value.addEffect(new EffectMp(armorSets.getMp()));
                }

                if (armorSets.getMpr() != 0) {
                    // 套装效果:MP回复增加
                    value.addEffect(new EffectMpR(armorSets.getMpr()));
                }

                // 4属性(水属性,风属性,火属性,地属性)

                if (armorSets.getDefenseWater() != 0) {
                    // 套装效果:水属性增加
                    value.addEffect(new EffectDefenseWater(armorSets
                            .getDefenseWater()));
                }

                if (armorSets.getDefenseWind() != 0) {
                    // 套装效果:风属性增加
                    value.addEffect(new EffectDefenseWind(armorSets
                            .getDefenseWind()));
                }

                if (armorSets.getDefenseFire() != 0) {
                    // 套装效果:火属性增加
                    value.addEffect(new EffectDefenseFire(armorSets
                            .getDefenseFire()));
                }

                if (armorSets.getDefenseEarth() != 0) {
                    // 套装效果:地属性增加
                    value.addEffect(new EffectDefenseEarth(armorSets
                            .getDefenseEarth()));
                }

                // 六耐性

                if (armorSets.get_regist_stun() != 0) {
                    // 套装效果:晕眩耐性增加
                    value.addEffect(new EffectRegist_Stun(armorSets
                            .get_regist_stun()));
                }

                if (armorSets.get_regist_stone() != 0) {
                    // 套装效果:石化耐性增加
                    value.addEffect(new EffectRegist_Stone(armorSets
                            .get_regist_stone()));
                }

                if (armorSets.get_regist_sleep() != 0) {
                    // 套装效果:睡眠耐性增加
                    value.addEffect(new EffectRegist_Sleep(armorSets
                            .get_regist_sleep()));
                }

                if (armorSets.get_regist_freeze() != 0) {
                    // 套装效果:寒冰耐性增加
                    value.addEffect(new EffectRegist_Freeze(armorSets
                            .get_regist_freeze()));
                }

                if (armorSets.get_regist_sustain() != 0) {
                    // 套装效果:支撑耐性增加
                    value.addEffect(new EffectRegist_Sustain(armorSets
                            .get_regist_sustain()));
                }

                if (armorSets.get_regist_blind() != 0) {
                    // 套装效果:暗闇耐性增加
                    value.addEffect(new EffectRegist_Blind(armorSets
                            .get_regist_blind()));
                }

                // 六属性(力量,敏捷,体质,精神,魅力,智力)

                if (armorSets.getStr() != 0) {
                    // 套装效果:力量增加
                    value.addEffect(new EffectStat_Str(armorSets.getStr()));
                }

                if (armorSets.getDex() != 0) {
                    // 套装效果:敏捷增加
                    value.addEffect(new EffectStat_Dex(armorSets.getDex()));
                }

                if (armorSets.getCon() != 0) {
                    // 套装效果:体质增加
                    value.addEffect(new EffectStat_Con(armorSets.getCon()));
                }

                if (armorSets.getWis() != 0) {
                    // 套装效果:精神增加
                    value.addEffect(new EffectStat_Wis(armorSets.getWis()));
                }

                if (armorSets.getCha() != 0) {
                    // 套装效果:魅力增加
                    value.addEffect(new EffectStat_Cha(armorSets.getCha()));
                }

                if (armorSets.getIntl() != 0) {
                    // 套装效果:智力增加
                    value.addEffect(new EffectStat_Int(armorSets.getIntl()));
                }

                // XXX
                if (armorSets.get_modifier_dmg() != 0) {
                    // 套装效果:套装增加物理伤害
                    value.addEffect(new Effect_Modifier_dmg(armorSets
                            .get_modifier_dmg()));
                }

                if (armorSets.get_reduction_dmg() != 0) {
                    // 套装效果:套装减免物理伤害
                    value.addEffect(new Effect_Reduction_dmg(armorSets
                            .get_reduction_dmg()));
                }

                if (armorSets.get_magic_modifier_dmg() != 0) {
                    // 套装效果:套装增加魔法伤害
                    value.addEffect(new Effect_Magic_modifier_dmg(armorSets
                            .get_magic_modifier_dmg()));
                }

                if (armorSets.get_magic_reduction_dmg() != 0) {
                    // 套装效果:套装减免魔法伤害
                    value.addEffect(new Effect_Magic_reduction_dmg(armorSets
                            .get_magic_reduction_dmg()));
                }

                if (armorSets.get_bow_modifier_dmg() != 0) {
                    // 套装效果:套装增加弓的物理伤害
                    value.addEffect(new Effect_Bow_modifier_dmg(armorSets
                            .get_bow_modifier_dmg()));
                }

                _allSet.put(armorSets.getId(), value);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            // 具有套装设置的误件 加入效果数字阵列
            ItemTable.get().se_mode();
        }
    }

    /**
     * 将文字串 转为数字阵列
     * 
     * @param id
     *            套件编号
     * @param s
     *            转换的字串
     * @return
     */
    private static int[] getArray(final int id, final String s) {
        // 根据给定正则表达式的匹配拆分此字符串。
        final String[] clientStrAry = s.split(",");
        final int[] array = new int[clientStrAry.length];
        try {
            for (int i = 0; i < clientStrAry.length; i++) {
                array[i] = Integer.parseInt(clientStrAry[i]);
            }

        } catch (final Exception e) {
            _log.error("编号:" + id + " 套件设置错误!!检查资料库!!", e);
        }
        return array;
    }

    /**
     * 将文字串 转为数字阵列
     * 
     * @param s
     * @param sToken
     * @return
     */
    /*
     * private static int[] getArray(final String s, final String sToken) {
     * final StringTokenizer st = new StringTokenizer(s, sToken); //
     * 计算在生成异常之前可以调用此 tokenizer 的 nextToken 方法的次数。当前位置没有提前。 final int size =
     * st.countTokens(); String temp = null; final int[] array = new int[size];
     * for (int i = 0; i < size; i++) { temp = st.nextToken(); array[i] =
     * Integer.parseInt(temp); } return array; }
     */
}
