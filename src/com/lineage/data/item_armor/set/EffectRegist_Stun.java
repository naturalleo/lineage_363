package com.lineage.data.item_armor.set;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 套装效果:晕眩耐性增加
 * 
 * @author daien
 * 
 */
public class EffectRegist_Stun implements ArmorSetEffect {

    private final int _add;// 增加值

    /**
     * 套装效果:晕眩耐性增加
     * 
     * @param add
     *            增加值
     */
    public EffectRegist_Stun(final int add) {
        this._add = add;
    }

    @Override
    public void giveEffect(final L1PcInstance pc) {
        pc.addRegistStun(this._add);
    }

    @Override
    public void cancelEffect(final L1PcInstance pc) {
        pc.addRegistStun(-this._add);
    }

    @Override
    public int get_mode() {
        return this._add;
    }
}
