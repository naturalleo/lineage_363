package com.lineage.server.templates;

/**
 * NPC 队员资料暂存
 * 
 * @author daien
 * 
 */
public class L1NpcCount {

    private final int _id;

    private final int _count;

    public L1NpcCount(final int id, final int count) {
        this._id = id;
        this._count = count;
    }

    /**
     * 队员NPCID
     * 
     * @return
     */
    public int getId() {
        return this._id;
    }

    /**
     * 队员数量
     * 
     * @return
     */
    public int getCount() {
        return this._count;
    }

    public boolean isZero() {
        return (this._id == 0) && (this._count == 0);
    }
}
