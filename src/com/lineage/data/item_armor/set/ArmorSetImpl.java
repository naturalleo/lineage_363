package com.lineage.data.item_armor.set;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 套装使用的判断
 * 
 * @author daien
 * 
 */
public class ArmorSetImpl extends ArmorSet {

    private static final Log _log = LogFactory.getLog(ArmorSetImpl.class);

    // 套装编号
    private final int _id;

    // 套装物品编号阵列
    private final int _ids[];

    // 套装效果组合
    private final ArrayList<ArmorSetEffect> _effects;

    // 套装效果动画
    private final int _gfxids[];

    /**
     * 套装的判断设置
     * 
     * @param ids
     *            套装物品编号阵列
     */
    protected ArmorSetImpl(final int id, final int ids[], final int gfxids[]) {
        this._id = id;
        this._ids = ids;
        this._gfxids = gfxids;
        this._effects = new ArrayList<ArmorSetEffect>();
    }

    /**
     * 套装编号
     * 
     * @return
     */
    public int get_id() {
        return _id;
    }

    /**
     * 套装物品编号阵列
     * 
     * @return
     */
    @Override
    public int[] get_ids() {
        return _ids;
    }

    /**
     * 加入该套装效果组合
     * 
     * @param effect
     */
    public void addEffect(final ArmorSetEffect effect) {
        this._effects.add(effect);
    }

    /**
     * 移除该套装效果组合
     * 
     * @param effect
     */
    public void removeEffect(final ArmorSetEffect effect) {
        this._effects.remove(effect);
    }

    /**
     * 传回该套装附加的效果阵列
     * 
     * @return
     */
    @Override
    public int[] get_mode() {
        int[] mode = new int[21];
        for (final ArmorSetEffect set : _effects) {
            // 六属性(力量,敏捷,体质,精神,魅力,智力)

            if (set instanceof EffectStat_Str) {
                // 套装效果:力量增加
                mode[0] = set.get_mode();
            }

            if (set instanceof EffectStat_Dex) {
                // 套装效果:敏捷增加
                mode[1] = set.get_mode();
            }

            if (set instanceof EffectStat_Con) {
                // 套装效果:体质增加
                mode[2] = set.get_mode();
            }

            if (set instanceof EffectStat_Wis) {
                // 套装效果:精神增加
                mode[3] = set.get_mode();
            }

            if (set instanceof EffectStat_Int) {
                // 套装效果:智力增加
                mode[4] = set.get_mode();
            }

            if (set instanceof EffectStat_Cha) {
                // 套装效果:魅力增加
                mode[5] = set.get_mode();
            }

            // HP相关
            if (set instanceof EffectHp) {
                // 套装效果:HP增加
                mode[6] = set.get_mode();
            }

            // MP相关
            if (set instanceof EffectMp) {
                // 套装效果:MP增加
                mode[7] = set.get_mode();
            }

            // SP(魔攻) XXX
            mode[8] = 0;

            // 加速效果 XXX
            mode[9] = 0;

            if (set instanceof EffectMr) {
                // 套装效果:抗魔增加
                mode[10] = set.get_mode();
            }

            // 4属性(水属性,风属性,火属性,地属性)

            if (set instanceof EffectDefenseFire) {
                // 套装效果:火属性增加
                mode[11] = set.get_mode();
            }

            if (set instanceof EffectDefenseWater) {
                // 套装效果:水属性增加
                mode[12] = set.get_mode();
            }

            if (set instanceof EffectDefenseWind) {
                // 套装效果:风属性增加
                mode[13] = set.get_mode();
            }

            if (set instanceof EffectDefenseEarth) {
                // 套装效果:地属性增加
                mode[14] = set.get_mode();
            }

            // 六耐性

            if (set instanceof EffectRegist_Freeze) {
                // 套装效果:寒冰耐性增加
                mode[15] = set.get_mode();
            }

            if (set instanceof EffectRegist_Stone) {
                // 套装效果:石化耐性增加
                mode[16] = set.get_mode();
            }

            if (set instanceof EffectRegist_Sleep) {
                // 套装效果:睡眠耐性增加
                mode[17] = set.get_mode();
            }

            if (set instanceof EffectRegist_Blind) {
                // 套装效果:暗闇耐性增加
                mode[18] = set.get_mode();
            }

            if (set instanceof EffectRegist_Stun) {
                // 套装效果:晕眩耐性增加
                mode[19] = set.get_mode();
            }

            if (set instanceof EffectRegist_Sustain) {
                // 套装效果:支撑耐性增加
                mode[20] = set.get_mode();
            }
        }
        return mode;
    }

    /**
     * 套装完成效果
     * 
     * @param pc
     */
    @Override
    public void giveEffect(final L1PcInstance pc) {
        try {
            for (final ArmorSetEffect effect : this._effects) {
                effect.giveEffect(pc);
            }
            // 套装效果动画
            if (_gfxids != null) {
                for (int gfx : _gfxids) {
                    // 动画效果
                    pc.sendPacketsX8(new S_SkillSound(pc.getId(), gfx));
                }
            }

        } catch (final Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * 套装解除效果
     * 
     * @param pc
     */
    @Override
    public void cancelEffect(final L1PcInstance pc) {
        try {
            for (final ArmorSetEffect effect : this._effects) {
                effect.cancelEffect(pc);
            }

        } catch (final Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * 套装完成
     * 
     * @param pc
     * @return
     */
    @Override
    public final boolean isValid(final L1PcInstance pc) {
        return pc.getInventory().checkEquipped(this._ids);
    }

    /**
     * 是否为套装中组件
     * 
     * @param id
     * @return
     */
    @Override
    public boolean isPartOfSet(final int id) {
        for (final int i : this._ids) {
            if (id == i) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否装备了相同界指2个
     * 
     * @param pc
     * @return
     */
    @Override
    public boolean isEquippedRingOfArmorSet(final L1PcInstance pc) {
        final L1PcInventory pcInventory = pc.getInventory();
        L1ItemInstance armor = null;
        boolean isSetContainRing = false;

        // 寻找套装物件是否为戒指
        for (final int id : this._ids) {
            armor = pcInventory.findItemId(id);
            if (armor.getItem().getUseType() == 23) {// 戒指
                // if ((armor.getItem().getType2() == 2) &&
                // (armor.getItem().getType() == 9)) { // ring
                isSetContainRing = true;
                break;
            }
        }

        // 是否装备了相同界指2个
        if ((armor != null) && isSetContainRing) {
//            final int itemId = armor.getItem().getItemId();
            // 已经带了 2个戒指
//            if (pcInventory.getTypeEquipped(2, 9) == 2) {
//                L1ItemInstance ring[] = new L1ItemInstance[2];
//                ring = pcInventory.getRingEquipped();// 装备中界指阵列
//                if ((ring[0].getItem().getItemId() == itemId)
//                        && (ring[1].getItem().getItemId() == itemId)) {
//                    return true;
//                }
//            }
        	//已修正套装戒指可以重复增加套装奖励  by hjx10000
        	final String nameid = armor.getItem().getNameId();
            if (pcInventory.getTypeEquipped(2, 9) >= 2) {
            	if (pcInventory.getTypeAndItemIdEquipped(2, 9, nameid) >= 2) {
            		return true;
            	}
            }
            
        }
        return false;
    }
}
