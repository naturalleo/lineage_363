package com.lineage.data.item_armor.set;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 六属性状态增加:精神
 * 
 * @author daien
 * 
 */
public class EffectStat_Wis implements ArmorSetEffect {

    private final int _add;

    /**
     * 六属性状态增加:精神
     * 
     * @param add
     *            精神
     */
    public EffectStat_Wis(final int add) {
        this._add = add;
    }

    @Override
    public void giveEffect(final L1PcInstance pc) {
        pc.addWis(this._add);
    }

    @Override
    public void cancelEffect(final L1PcInstance pc) {
        pc.addWis(-this._add);
    }

    @Override
    public int get_mode() {
        return this._add;
    }
}
