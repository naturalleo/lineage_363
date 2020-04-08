package com.lineage.server.model;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_HPMeter;
import com.lineage.server.serverpackets.S_PacketBoxParty;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 队伍
 * 
 * @author dexc
 * 
 */
public class L1Party {

    private static final Log _log = LogFactory.getLog(L1Party.class);

    public static final int MAXPT = 8;// 最大队伍成员数量

    private final ConcurrentHashMap<Integer, L1PcInstance> _membersList = new ConcurrentHashMap<Integer, L1PcInstance>();

    private L1PcInstance _leader = null;

    private boolean _isLeader = false;// 队长已经加入

    /**
     * 加入新的队伍成员
     * 
     * @param pc
     */
    public void addMember(final L1PcInstance pc) {
        try {
            int key = 1;// 队伍成员编号
            if (pc == null) {
                throw new NullPointerException();
            }

            if (this._membersList.size() == MAXPT
                    || this._membersList.contains(pc)) {
                return;
            }

            boolean hpUpdate = false;
            if (this._membersList.isEmpty()) {
                // 队伍初始化成立 设置队长
                this.setLeader(pc);

            } else {
                hpUpdate = true;
            }

            while (this._membersList.get(key) != null) {
                key++;
            }
            this._membersList.put(key, pc);

            pc.setParty(this);

            for (L1PcInstance member : this._membersList.values()) {
                if (!member.equals(this._leader)) {
                    member.sendPackets(new S_PacketBoxParty(member, this));

                } else {
                    if (_isLeader) {
                        member.sendPackets(new S_PacketBoxParty(pc));
                    }
                }
                // 队伍UI更新
                member.sendPackets(new S_PacketBoxParty(this, member));
            }

            if (!_isLeader) {
                _isLeader = true;
            }
            if (hpUpdate) {
                this.createMiniHp(pc);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 移出队伍成员
     * 
     * @param pc
     */
    private void removeMember(final L1PcInstance pc) {
        try {
            int removeKey = -1;
            for (final int key : this._membersList.keySet()) {
                L1PcInstance tgpc = this._membersList.get(key);
                if (pc.equals(tgpc)) {
                    removeKey = key;
                }
            }
            if (removeKey != -1) {
                this._membersList.remove(removeKey);

                pc.setParty(null);
                if (!this._membersList.isEmpty()) {
                    this.deleteMiniHp(pc);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 任意一名队伍成员
     * 
     * @return
     */
    public L1PcInstance partyUser() {
        L1PcInstance user = null;
        for (final L1PcInstance pc : _membersList.values()) {
            if (user == null && !_leader.equals(pc)) {
                user = pc;
            }
        }
        return user;
    }

    /**
     * 该队伍目前人数(同地图)
     * 
     * @return
     */
    public int partyUserInMap(final short mapid) {
        int i = 0;
        if (this._membersList.isEmpty()) {
            return 0;
        }
        if (this._membersList.size() <= 0) {
            return 0;
        }

        for (final L1PcInstance tgpc : this._membersList.values()) {
            short tgpcMapid = tgpc.getMapId();
            if (tgpcMapid == mapid) {
                i += 1;
            }
        }

        return i;
    }

    /**
     * 队伍成员尚未饱和
     * 
     * @return
     */
    public boolean isVacancy() {
        return this._membersList.size() < MAXPT;
    }

    /**
     * 剩余可加入队伍人数
     * 
     * @return
     */
    public int getVacancy() {
        return MAXPT - this._membersList.size();
    }

    /**
     * 是否为队员
     * 
     * @param pc
     * @return
     */
    public boolean isMember(final L1PcInstance pc) {
        for (final L1PcInstance tgpc : this._membersList.values()) {
            if (pc.equals(tgpc)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置队长
     * 
     * @param pc
     */
    public void setLeader(final L1PcInstance pc) {
        this._leader = pc;
    }

    /**
     * 传回队长
     * 
     * @return
     */
    public L1PcInstance getLeader() {
        return this._leader;
    }

    /**
     * 传回队长OBJID
     * 
     * @return
     */
    public int getLeaderID() {
        return this._leader.getId();
    }

    /**
     * 是否为队长
     * 
     * @param pc
     * @return
     */
    public boolean isLeader(final L1PcInstance pc) {
        return pc.getId() == this._leader.getId();
    }

    /**
     * 全队员名称
     * 
     * @return
     */
    public String getMembersNameList() {
        StringBuilder stringBuilder = new StringBuilder();

        if (this._membersList.isEmpty()) {
            return null;
        }
        if (this._membersList.size() <= 0) {
            return null;
        }
        for (final L1PcInstance pc : this._membersList.values()) {
            stringBuilder.append(pc.getName() + " ");
        }
        return stringBuilder.toString();
    }

    /**
     * 队员加入时HP显示的增加
     * 
     * @param pc
     */
    private void createMiniHp(final L1PcInstance pc) {
        if (this._membersList.isEmpty()) {
            return;
        }
        if (this._membersList.size() <= 0) {
            return;
        }
        for (final L1PcInstance member : this._membersList.values()) {
            member.sendPackets(new S_HPMeter(pc.getId(), 100
                    * pc.getCurrentHp() / pc.getMaxHp()));
            pc.sendPackets(new S_HPMeter(member.getId(), 100
                    * member.getCurrentHp() / member.getMaxHp()));
        }
    }

    /**
     * 队员离开时HP显示的移除
     * 
     * @param pc
     */
    private void deleteMiniHp(final L1PcInstance pc) {
        if (this._membersList.isEmpty()) {
            return;
        }
        if (this._membersList.size() <= 0) {
            return;
        }
        for (final L1PcInstance member : this._membersList.values()) {
            pc.sendPackets(new S_HPMeter(member.getId(), 0xff));
            member.sendPackets(new S_HPMeter(pc.getId(), 0xff));
        }
    }

    /**
     * 队员血条更新
     * 
     * @param pc
     */
    public void updateMiniHP(final L1PcInstance pc) {
        if (this._membersList.isEmpty()) {
            return;
        }
        if (this._membersList.size() <= 0) {
            return;
        }
        for (final L1PcInstance member : this._membersList.values()) { // 队员血条更新
            member.sendPackets(new S_HPMeter(pc.getId(), 100
                    * pc.getCurrentHp() / pc.getMaxHp()));
        }
    }

    /**
     * 解散队伍
     */
    public void breakup() {
        if (this._membersList.isEmpty()) {
            return;
        }
        if (this._membersList.size() <= 0) {
            return;
        }
        for (final L1PcInstance member : this._membersList.values()) {
            this.removeMember(member);
            // 您解散您的队伍了!!
            member.sendPackets(new S_ServerMessage(418));
        }
    }

    public void leaveMember(final L1PcInstance pc) {
        if (this.isLeader(pc)) {
            // パーティーリーダーの场合
            this.breakup();

        } else {
            // パーティーリーダーでない场合
            if (this.getNumOfMembers() == 2) {
                // 队伍成员剩余2人
                this.removeMember(pc);
                final L1PcInstance leader = this.getLeader();
                this.removeMember(leader);

                this.sendLeftMessage(pc, pc);
                this.sendLeftMessage(leader, pc);

                // 您解散您的队伍了!!
                leader.sendPackets(new S_ServerMessage(418));
                pc.sendPackets(new S_ServerMessage(418));

            } else {
                // 队伍成员余2人以上
                this.removeMember(pc);
                for (final L1PcInstance member : this._membersList.values()) {
                    this.sendLeftMessage(member, pc);
                }
                this.sendLeftMessage(pc, pc);
            }
        }
    }

    /**
     * 驱逐队员
     * 
     * @param pc
     */
    public void kickMember(final L1PcInstance pc) {
        if (this.getNumOfMembers() == 2) {
            // 队伍成员剩余2人
            this.removeMember(pc);
            final L1PcInstance leader = this.getLeader();
            this.removeMember(leader);

            this.sendLeftMessage(pc, pc);
            this.sendLeftMessage(leader, pc);

            // 您解散您的队伍了!!
            leader.sendPackets(new S_ServerMessage(418));
            pc.sendPackets(new S_ServerMessage(418));

        } else {
            // 队伍成员余2人以上
            this.removeMember(pc);
            for (final L1PcInstance member : this._membersList.values()) {
                this.sendLeftMessage(member, pc);
            }
            this.sendLeftMessage(pc, pc);
        }
        // 您从队伍中被驱逐了。
        pc.sendPackets(new S_ServerMessage(419));
    }

    /**
     * 队伍成员清单
     * 
     * @return
     */
    public ConcurrentHashMap<Integer, L1PcInstance> partyUsers() {
        return _membersList;
    }

    /**
     * 成员数量
     * 
     * @return
     */
    public int getNumOfMembers() {
        return this._membersList.size();
    }

    /**
     * 成员离开的讯息
     * 
     * @param sendTo
     * @param left
     */
    private void sendLeftMessage(final L1PcInstance sendTo,
            final L1PcInstance left) {
        // %0%s 离开了队伍。
        sendTo.sendPackets(new S_ServerMessage(420, left.getName()));
    }

    /**
     * 传递新队长讯息
     */
    public void msgToAll() {
        for (final L1PcInstance member : this._membersList.values()) {
            if (!member.equals(this._leader)) {
                // 传递新队长讯息
                member.sendPackets(new S_PacketBoxParty(this._leader.getId(),
                        this._leader.getName()));
            }
        }
    }
}
