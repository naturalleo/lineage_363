package com.lineage.server.templates;

import java.util.Collections;
import java.util.List;

import com.lineage.server.utils.collections.Lists;

/**
 * NPC队伍资讯暂存
 * 
 * @author daien
 * 
 */
public class L1MobGroup {

    private final int _id;

    private final int _leaderId;

    private final List<L1NpcCount> _minions = Lists.newArrayList();

    private final boolean _isRemoveGroupIfLeaderDie;// 是否解散队伍

    /**
     * NPC队伍资讯暂存
     * 
     * @param id
     *            队伍编号
     * @param leaderId
     *            队长NPCID
     * @param minions
     *            队员组
     * @param isRemoveGroupIfLeaderDie
     *            是否解散队伍
     */
    public L1MobGroup(final int id, final int leaderId,
            final List<L1NpcCount> minions,
            final boolean isRemoveGroupIfLeaderDie) {
        this._id = id;
        this._leaderId = leaderId;
        this._minions.addAll(minions);
        this._isRemoveGroupIfLeaderDie = isRemoveGroupIfLeaderDie;
    }

    /**
     * 队伍编号
     * 
     * @return
     */
    public int getId() {
        return this._id;
    }

    /**
     * 队长NPCID
     * 
     * @return
     */
    public int getLeaderId() {
        return this._leaderId;
    }

    /**
     * 队员组
     * 
     * @return
     */
    public List<L1NpcCount> getMinions() {
        return Collections.unmodifiableList(this._minions);
    }

    /**
     * 是否解散队伍
     * 
     * @return
     */
    public boolean isRemoveGroupIfLeaderDie() {
        return this._isRemoveGroupIfLeaderDie;
    }

}
