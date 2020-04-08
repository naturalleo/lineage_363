package com.lineage.server.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.world.World;

public class L1Clan {

    private static final Log _log = LogFactory.getLog(L1Clan.class);

    private final Lock _lock = new ReentrantLock(true);

    /** [见习] */
    public static final int CLAN_RANK_PROBATION = 1;

    /** [一般] 无仓库使用权 */
    public static final int CLAN_RANK_PUBLIC = 2;

    /** [副君主] */
    public static final int CLAN_RANK_GUARDIAN = 3;

    /** [联盟君主] */
    public static final int CLAN_RANK_PRINCE = 4;

    /** 联合血盟[修习骑士] */
    public static final int ALLIANCE_CLAN_RANK_ATTEND = 5;

    /** 联合血盟[守护骑士] */
    public static final int ALLIANCE_CLAN_RANK_GUARDIAN = 6;

    /** 一般血盟[一般] */
    public static final int NORMAL_CLAN_RANK_GENERAL = 7;

    /** 一般血盟[修习骑士] */
    public static final int NORMAL_CLAN_RANK_ATTEND = 8;

    /** 一般血盟[守护骑士] */
    public static final int NORMAL_CLAN_RANK_GUARDIAN = 9;

    /** 一般血盟[联盟君主] */
    public static final int NORMAL_CLAN_RANK_PRINCE = 10;

    private int _clanId;

    private String _clanName;

    private int _leaderId;

    private String _leaderName;

    private int _castleId;

    private int _houseId;

    private int _warehouse = 0;

    private final L1DwarfForClanInventory _dwarfForClan = new L1DwarfForClanInventory(
            this);

    private final ArrayList<String> _membersNameList = new ArrayList<String>();

    // 全部血盟成员与阶级资料
    // private static final HashMap<String, Integer> _membersNameList = new
    // HashMap<String, Integer>();

    public int getClanId() {
        return this._clanId;
    }

    /**
     * 设置血盟ID
     * 
     * @param clan_id
     */
    public void setClanId(final int clan_id) {
        this._clanId = clan_id;
    }

    /**
     * 血盟名称
     * 
     * @return
     */
    public String getClanName() {
        return this._clanName;
    }

    /**
     * 设置血盟名称
     * 
     * @param clan_name
     */
    public void setClanName(final String clan_name) {
        this._clanName = clan_name;
    }

    /**
     * 盟主OBJID
     * 
     * @return
     */
    public int getLeaderId() {
        return this._leaderId;
    }

    /**
     * 设置盟主OBJID
     * 
     * @param leader_id
     */
    public void setLeaderId(final int leader_id) {
        this._leaderId = leader_id;
    }

    /**
     * 盟主名称
     * 
     * @return
     */
    public String getLeaderName() {
        return this._leaderName;
    }

    /**
     * 设置盟主名称
     * 
     * @param leader_name
     */
    public void setLeaderName(final String leader_name) {
        this._leaderName = leader_name;
    }

    /**
     * 拥有城堡ID
     * 
     * @return
     */
    public int getCastleId() {
        return this._castleId;
    }

    /**
     * 设置拥有城堡ID
     * 
     * @param hasCastle
     */
    public void setCastleId(final int hasCastle) {
        this._castleId = hasCastle;
    }

    /**
     * 拥有小屋ID
     * 
     * @return
     */
    public int getHouseId() {
        return this._houseId;
    }

    /**
     * 设置拥有小屋ID
     * 
     * @param hasHideout
     */
    public void setHouseId(final int hasHideout) {
        this._houseId = hasHideout;
    }

    /**
     * 加入血盟成员清单
     * 
     * @param member_name
     */
    public void addMemberName(final String member_name) {
        this._lock.lock();
        try {
            if (!this._membersNameList.contains(member_name)) {
                this._membersNameList.add(member_name);
            }

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 移出血盟成员清单
     * 
     * @param member_name
     */
    public void delMemberName(final String member_name) {
        this._lock.lock();
        try {
            if (this._membersNameList.contains(member_name)) {
                this._membersNameList.remove(member_name);
            }

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 血盟线上成员数量
     * 
     * @return
     */
    public int getOnlineClanMemberSize() {
        int count = 0;
        try {
            for (final String name : this._membersNameList) {
                final L1PcInstance pc = World.get().getPlayer(name);
                // 人员在线上
                if (pc != null) {
                    count++;
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return count;
    }

    /**
     * 全部血盟成员数量
     * 
     * @return
     */
    public int getAllMembersSize() {
        try {
            return this._membersNameList.size();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return 0;
    }

    /**
     * 对血盟线上成员发送封包
     */
    public void sendPacketsAll(final ServerBasePacket packet) {
        try {
            for (final Object nameobj : _membersNameList.toArray()) {
                final String name = (String) nameobj;
                final L1PcInstance pc = World.get().getPlayer(name);
                if (pc != null) {
                    pc.sendPackets(packet);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 血盟线上成员
     * 
     * @return
     */
    public L1PcInstance[] getOnlineClanMember() {
        // 清单缓存
        final ArrayList<String> temp = new ArrayList<String>();
        // 输出清单
        final ArrayList<L1PcInstance> onlineMembers = new ArrayList<L1PcInstance>();
        try {
            temp.addAll(this._membersNameList);

            for (final String name : temp) {
                final L1PcInstance pc = World.get().getPlayer(name);
                if ((pc != null) && !onlineMembers.contains(pc)) {
                    onlineMembers.add(pc);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return onlineMembers.toArray(new L1PcInstance[onlineMembers.size()]);
    }

    /**
     * 血盟线上成员名单
     * 
     * @return
     */
    public StringBuilder getOnlineMembersFP() {
        // 清单缓存
        final ArrayList<String> temp = new ArrayList<String>();
        // 输出名单
        final StringBuilder result = new StringBuilder();
        try {
            temp.addAll(this._membersNameList);

            for (final String name : temp) {
                final L1PcInstance pc = World.get().getPlayer(name);
                if (pc != null) {
                    result.append(name + " ");
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    /**
     * 全部血盟成员名单(包含离线)
     * 
     * @return
     */
    public StringBuilder getAllMembersFP() {
        // 清单缓存
        final ArrayList<String> temp = new ArrayList<String>();
        // 输出名单
        final StringBuilder result = new StringBuilder();
        try {
            temp.addAll(this._membersNameList);

            for (final String name : temp) {
                result.append(name + " ");
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    /**
     * 血盟线上成员名单(包含阶级)
     * 
     * @return
     */
    public StringBuilder getOnlineMembersFPWithRank() {
        // 清单缓存
        final ArrayList<String> temp = new ArrayList<String>();
        // 输出名单
        final StringBuilder result = new StringBuilder();
        try {
            temp.addAll(this._membersNameList);

            for (final String name : temp) {
                final L1PcInstance pc = World.get().getPlayer(name);
                if (pc != null) {
                    result.append(name + this.getRankString(pc) + " ");
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    /**
     * 全部血盟成员名单(包含离线)
     * 
     * @return
     */
    public StringBuilder getAllMembersFPWithRank() {
        // 清单缓存
        final ArrayList<String> temp = new ArrayList<String>();
        // 输出名单
        final StringBuilder result = new StringBuilder();
        try {
            temp.addAll(this._membersNameList);

            for (final String name : temp) {
                final L1PcInstance pc = CharacterTable.get().restoreCharacter(
                        name);
                if (pc != null) {
                    result.append(name + " ");
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    String[] _rank = new String[] {
            // 2:一般 3:副君主 4:联盟君主 5:修习骑士 6:守护骑士 7:一般 8:修习骑士 9:守护骑士 10:联盟君主
            "", "", "一般", "副君主", "联盟君主", "修习骑士", "守护骑士", "一般", "修习骑士", "守护骑士",
            "联盟君主", };

    /**
     * 血盟阶级
     * 
     * @param pc
     * @return
     */
    private String getRankString(final L1PcInstance pc) {
        if (pc != null) {
            if (pc.getClanRank() > 0) {
                return _rank[pc.getClanRank()];
            }
        }
        return "";
    }

    public String[] getAllMembers() {
        return this._membersNameList.toArray(new String[this._membersNameList
                .size()]);
    }

    /**
     * 血盟仓库资料
     * 
     * @return
     */
    public L1DwarfForClanInventory getDwarfForClanInventory() {
        return this._dwarfForClan;
    }

    public int getWarehouseUsingChar() {// 血盟仓库目前使用者
        return this._warehouse;
    }

    public void setWarehouseUsingChar(final int objid) {
        this._warehouse = objid;
    }

    // 血盟技能
    private boolean _clanskill = false;

    /**
     * 设置是否能启用血盟技能
     * 
     * @param boolean1
     */
    public void set_clanskill(boolean boolean1) {
        this._clanskill = boolean1;
    }

    /**
     * 是否能启用血盟技能
     * 
     * @return true有 false没有
     */
    public boolean isClanskill() {
        return this._clanskill;
    }

    // 血盟技能结束时间
    private Timestamp _skilltime = null;

    /**
     * 设置血盟技能结束时间
     * 
     * @param skilltime
     */
    public void set_skilltime(Timestamp skilltime) {
        this._skilltime = skilltime;
    }

    /**
     * 血盟技能结束时间
     * 
     * @return _skilltime
     */
    public Timestamp get_skilltime() {
        return this._skilltime;
    }
    
    private int _maxuser; //血盟UI hjx1000
    
    public int getOnlineMaxUser() { //血盟UI hjx1000
    	return this._maxuser;
    }
    
    public void setOnlineMaxUser(int i) { //血盟UI hjx1000
        this._maxuser = i;
	}
}
