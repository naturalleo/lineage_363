package com.lineage.data.item_armor.set;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 六属性状态增加:智力
 * 
 * @author daien
 * 
 */
public class EffectStat_Int implements ArmorSetEffect {

    private final int _add;

    /**
     * 六属性状态增加:智力
     * 
     * @param add
     *            智力
     */
    public EffectStat_Int(final int add) {
        this._add = add;
    }

    @Override
    public void giveEffect(final L1PcInstance pc) {
        pc.addInt(this._add);
    }

    @Override
    public void cancelEffect(final L1PcInstance pc) {
        pc.addInt(-this._add);
    }

    @Override
    public int get_mode() {
        return this._add;
    }
}
