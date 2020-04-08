package com.lineage.data.item_armor.set;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 六属性状态增加:敏捷
 * 
 * @author daien
 * 
 */
public class EffectStat_Dex implements ArmorSetEffect {

    private final int _add;

    /**
     * 六属性状态增加:敏捷
     * 
     * @param add
     *            敏捷
     */
    public EffectStat_Dex(final int add) {
        this._add = add;
    }

    @Override
    public void giveEffect(final L1PcInstance pc) {
        pc.addDex(this._add);
    }

    @Override
    public void cancelEffect(final L1PcInstance pc) {
        pc.addDex(-this._add);
    }

    @Override
    public int get_mode() {
        return this._add;
    }
}
