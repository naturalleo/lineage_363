package com.lineage.data.item_armor.set;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 六属性状态增加:体质
 * 
 * @author daien
 * 
 */
public class EffectStat_Con implements ArmorSetEffect {

    private final int _add;

    /**
     * 六属性状态增加:体质
     * 
     * @param add
     *            体质
     */
    public EffectStat_Con(final int add) {
        this._add = add;
    }

    @Override
    public void giveEffect(final L1PcInstance pc) {
        pc.addCon(this._add);
    }

    @Override
    public void cancelEffect(final L1PcInstance pc) {
        pc.addCon(-this._add);
    }

    @Override
    public int get_mode() {
        return this._add;
    }
}
