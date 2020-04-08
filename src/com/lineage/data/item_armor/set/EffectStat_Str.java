package com.lineage.data.item_armor.set;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 六属性状态增加:力量
 * 
 * @author daien
 * 
 */
public class EffectStat_Str implements ArmorSetEffect {

    private final int _add;

    /**
     * 六属性状态增加:力量
     * 
     * @param add
     *            力量
     */
    public EffectStat_Str(final int add) {
        this._add = add;
    }

    @Override
    public void giveEffect(final L1PcInstance pc) {
        pc.addStr(this._add);
    }

    @Override
    public void cancelEffect(final L1PcInstance pc) {
        pc.addStr(-this._add);
    }

    @Override
    public int get_mode() {
        return this._add;
    }
}
