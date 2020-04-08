package com.lineage.data.item_armor.set;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 套装效果:HP回复增加
 * 
 * @author daien
 * 
 */
public class EffectHpR implements ArmorSetEffect {

    private final int _add;// 增加值

    /**
     * 套装效果:HP回复增加
     * 
     * @param add
     *            增加值
     */
    public EffectHpR(final int add) {
        this._add = add;
    }

    @Override
    public void giveEffect(final L1PcInstance pc) {
        pc.addHpr(this._add);
    }

    @Override
    public void cancelEffect(final L1PcInstance pc) {
        pc.addHpr(-this._add);
    }

    @Override
    public int get_mode() {
        return this._add;
    }
}
