package com.lineage.server.templates;

/**
 * 掉落物品资料(指定地图)
 * 
 * @author daien
 * 
 */
public class L1DropMap {

    int _mobId;
    int _mapid;
    int _itemId;
    int _min;
    int _max;
    int _chance;

    public L1DropMap(final int mobId, final int mapid, final int itemId,
            final int min, final int max, final int chance) {
        this._mobId = mobId;
        this._itemId = itemId;
        this._min = min;
        this._max = max;
        this._chance = chance;
    }

    /**
     * 指定地图
     * 
     * @return
     */
    public int get_mapid() {
        return this._mapid;
    }

    /**
     * 机率
     * 
     * @return
     */
    public int getChance() {
        return this._chance;
    }

    /**
     * 物品编号
     * 
     * @return
     */
    public int getItemid() {
        return this._itemId;
    }

    /**
     * 最大量
     * 
     * @return
     */
    public int getMax() {
        return this._max;
    }

    /**
     * 最小量
     * 
     * @return
     */
    public int getMin() {
        return this._min;
    }

    /**
     * NPC编号
     * 
     * @return
     */
    public int getMobid() {
        return this._mobId;
    }
}
