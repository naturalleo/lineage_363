package com.lineage.server.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class L1ChatParty {

    private static final Log _log = LogFactory.getLog(L1ChatParty.class);

    private final List<L1PcInstance> _membersList = new ArrayList<L1PcInstance>();

    private L1PcInstance _leader = null;

    public void addMember(final L1PcInstance pc) {
        try {
            if (pc == null) {
                throw new NullPointerException();
            }
            if (_membersList.contains(pc)) {
                return;
            }

            if (_membersList.isEmpty()) {
                // 最初のPTメンバーであればリーダーにする
                setLeader(pc);
            }

            _membersList.add(pc);
            pc.setChatParty(this);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void removeMember(final L1PcInstance pc) {
        try {
            if (!_membersList.contains(pc)) {
                return;
            }

            this._membersList.remove(pc);
            pc.setChatParty(null);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public boolean isVacancy() {
        return this._membersList.size() < 8;
    }

    public int getVacancy() {
        return 8 - this._membersList.size();
    }

    public boolean isMember(final L1PcInstance pc) {
        return this._membersList.contains(pc);
    }

    private void setLeader(final L1PcInstance pc) {
        this._leader = pc;
    }

    public L1PcInstance getLeader() {
        return this._leader;
    }

    public boolean isLeader(final L1PcInstance pc) {
        return pc.getId() == this._leader.getId();
    }

    public String getMembersNameList() {
        String _result = new String("");
        for (final L1PcInstance pc : this._membersList) {
            _result = _result + pc.getName() + " ";
        }
        return _result;
    }

    private void breakup() {
        try {
            final L1PcInstance[] members = this.getMembers();

            for (final L1PcInstance member : members) {
                this.removeMember(member);
                member.sendPackets(new S_ServerMessage(418)); // パーティーを解散しました。
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void leaveMember(final L1PcInstance pc) {
        try {
            final L1PcInstance[] members = this.getMembers();
            if (this.isLeader(pc)) {
                // パーティーリーダーの场合
                this.breakup();
            } else {
                // パーティーリーダーでない场合
                if (this.getNumOfMembers() == 2) {
                    // パーティーメンバーが自分とリーダーのみ
                    this.removeMember(pc);
                    final L1PcInstance leader = this.getLeader();
                    this.removeMember(leader);

                    this.sendLeftMessage(pc, pc);
                    this.sendLeftMessage(leader, pc);
                } else {
                    // 残りのパーティーメンバーが２人以上いる
                    this.removeMember(pc);
                    for (final L1PcInstance member : members) {
                        this.sendLeftMessage(member, pc);
                    }
                    this.sendLeftMessage(pc, pc);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void kickMember(final L1PcInstance pc) {
        try {
            if (this.getNumOfMembers() == 2) {
                // パーティーメンバーが自分とリーダーのみ
                this.removeMember(pc);
                final L1PcInstance leader = this.getLeader();
                this.removeMember(leader);
            } else {
                // 残りのパーティーメンバーが２人以上いる
                this.removeMember(pc);
            }
            pc.sendPackets(new S_ServerMessage(419)); // パーティーから追放されました。

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public L1PcInstance[] getMembers() {
        return this._membersList.toArray(new L1PcInstance[this._membersList
                .size()]);
    }

    public int getNumOfMembers() {
        return this._membersList.size();
    }

    private void sendLeftMessage(final L1PcInstance sendTo,
            final L1PcInstance left) {
        try {
            // %0がパーティーから去りました。
            sendTo.sendPackets(new S_ServerMessage(420, left.getName()));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

}
