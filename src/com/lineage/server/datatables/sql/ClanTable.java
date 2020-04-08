package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.IdFactory;
import com.lineage.server.datatables.storage.ClanStorage;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.WorldClan;

/**
 * 血盟资料
 * 
 * @author dexc
 * 
 */
public class ClanTable implements ClanStorage {

    private static final Log _log = LogFactory.getLog(ClanTable.class);

    private final Map<Integer, L1Clan> _clans = new HashMap<Integer, L1Clan>();

    /**
     * 预先加载血盟资料
     */
    @Override
    public void load() {
        {
            final PerformanceTimer timer = new PerformanceTimer();
            Connection cn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                cn = DatabaseFactory.get().getConnection();
                ps = cn.prepareStatement("SELECT * FROM `clan_data` ORDER BY `clan_id`");

                rs = ps.executeQuery();
                while (rs.next()) {
                    final L1Clan clan = new L1Clan();
                    final int clan_id = rs.getInt("clan_id");
                    clan.setClanId(clan_id);
                    clan.setClanName(rs.getString("clan_name"));
                    clan.setLeaderId(rs.getInt("leader_id"));
                    clan.setLeaderName(rs.getString("leader_name"));
                    clan.setCastleId(rs.getInt("hascastle"));
                    clan.setHouseId(rs.getInt("hashouse"));                   
                    boolean clanskill = rs.getBoolean("clanskill");
                    // 具有血盟技能
                    if (clanskill) {
                        clan.set_clanskill(clanskill);
                        final Timestamp skilltime = rs
                                .getTimestamp("skilltime");
                        clan.set_skilltime(skilltime);
                    }
                    clan.setOnlineMaxUser(rs.getInt("max_online_user"));//血盟UI hjx1000

                    WorldClan.get().storeClan(clan);
                    this._clans.put(clan_id, clan);
                }

            } catch (final SQLException e) {
                _log.error(e.getLocalizedMessage(), e);

            } finally {
                SQLUtil.close(rs);
                SQLUtil.close(ps);
                SQLUtil.close(cn);
            }
            _log.info("载入血盟资料资料数量: " + _clans.size() + "(" + timer.get()
                    + "ms)");
        }

        // 加入血盟人员名称清单
        final Collection<L1Clan> AllClan = WorldClan.get().getAllClans();
        for (final L1Clan clan : AllClan) {
            Connection cn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                cn = DatabaseFactory.get().getConnection();
                ps = cn.prepareStatement("SELECT `char_name` FROM `characters` WHERE `ClanID`=?");
                ps.setInt(1, clan.getClanId());
                rs = ps.executeQuery();

                while (rs.next()) {
                    clan.addMemberName(rs.getString("char_name"));
                }

            } catch (final SQLException e) {
                _log.error(e.getLocalizedMessage(), e);

            } finally {
                SQLUtil.close(rs);
                SQLUtil.close(ps);
                SQLUtil.close(cn);
            }
        }
        // 加载血盟仓库资料
        for (final L1Clan clan : AllClan) {
            clan.getDwarfForClanInventory().loadItems();
        }
    }

    /**
     * 加入虚拟血盟
     * 
     * @param integer
     * @param l1Clan
     */
    @Override
    public void addDeClan(Integer integer, L1Clan l1Clan) {
        WorldClan.get().storeClan(l1Clan);
        this._clans.put(integer, l1Clan);
    }

    /**
     * 建立血盟资料
     * 
     * @param player
     * @param clan_name
     * @return
     */
    @Override
    public L1Clan createClan(final L1PcInstance player, final String clan_name) {
        final Collection<L1Clan> allClans = WorldClan.get().getAllClans();
        for (final Iterator<L1Clan> iter = allClans.iterator(); iter.hasNext();) {
            final L1Clan oldClans = iter.next();
            if (oldClans.getClanName().equalsIgnoreCase(clan_name)) {
                return null;
            }
        }
        final L1Clan clan = new L1Clan();
        clan.setClanId(IdFactory.get().nextId());
        clan.setClanName(clan_name);
        clan.setLeaderId(player.getId());
        clan.setLeaderName(player.getName());
        clan.setCastleId(0);
        clan.setHouseId(0);
        clan.set_clanskill(false);

        Connection cn = null;
        PreparedStatement ps = null;

        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `clan_data` SET `clan_id`=?,`clan_name`=?,"
                    + "`leader_id`=?,`leader_name`=?,`hascastle`=?,`hashouse`=?,"
                    + "`clanskill`=?,`skilltime`=?,`max_online_user`=?"); //血盟UI hjx1000
            int i = 0;
            ps.setInt(++i, clan.getClanId());
            ps.setString(++i, clan.getClanName());
            ps.setInt(++i, clan.getLeaderId());
            ps.setString(++i, clan.getLeaderName());
            ps.setInt(++i, clan.getCastleId());
            ps.setInt(++i, clan.getHouseId());
            ps.setBoolean(++i, clan.isClanskill());
            ps.setTimestamp(++i, clan.get_skilltime());
            ps.setInt(++i, clan.getOnlineMaxUser()); //血盟UI hjx1000
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }

        WorldClan.get().storeClan(clan);
        this._clans.put(clan.getClanId(), clan);

        player.setClanid(clan.getClanId());
        player.setClanname(clan.getClanName());
        player.setClanRank(L1Clan.CLAN_RANK_PRINCE);
        clan.addMemberName(player.getName());
        try {
            // 资料存档
            player.save();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        }
        return clan;
    }

    /**
     * 更新血盟资料
     * 
     * @param clan
     */
    @Override
    public void updateClan(final L1Clan clan) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("UPDATE clan_data SET `clan_id`=?,`leader_id`=?,"
                    + "`leader_name`=?,`hascastle`=?,`hashouse`=?,"
                    + "`clanskill`=?,`skilltime`=? " + "WHERE `clan_name`=?");
            int i = 0;
            ps.setInt(++i, clan.getClanId());
            ps.setInt(++i, clan.getLeaderId());
            ps.setString(++i, clan.getLeaderName());
            ps.setInt(++i, clan.getCastleId());
            ps.setInt(++i, clan.getHouseId());
            ps.setBoolean(++i, clan.isClanskill());
            ps.setTimestamp(++i, clan.get_skilltime());
            //ps.setInt(++i, clan.getOnlineMaxUser()); //血盟UI hjx1000
            ps.setString(++i, clan.getClanName());
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    /**
     * 删除血盟资料
     * 
     * @param clan_name
     */
    @Override
    public void deleteClan(final String clan_name) {
        final L1Clan clan = WorldClan.get().getClan(clan_name);
        if (clan == null) {
            return;
        }
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `clan_data` WHERE `clan_name`=?");
            ps.setString(1, clan_name);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        clan.getDwarfForClanInventory().clearItems();
        clan.getDwarfForClanInventory().deleteAllItems();

        WorldClan.get().removeClan(clan);
        this._clans.remove(clan.getClanId());
    }

    /**
     * 指定血盟资料
     * 
     * @param clan_id
     * @return
     */
    @Override
    public L1Clan getTemplate(final int clan_id) {
        return this._clans.get(clan_id);
    }

    /**
     * 全部血盟资料
     * 
     * @return
     */
    @Override
    public Map<Integer, L1Clan> get_clans() {
        return this._clans;
    }
    
    /**
     * 血盟UI hjx1000
     * @param clan
     */
    @Override
	public void updateClanOnlineMaxUser(L1Clan clan)
    {
      Connection cn = null;
      PreparedStatement ps = null;
      try {
        cn = DatabaseFactory.get().getConnection();
        ps = cn.prepareStatement(
          "UPDATE clan_data SET `max_online_user`=? WHERE `clan_name`=?");
        ps.setInt(1, clan.getOnlineMaxUser());

        ps.setString(2, clan.getClanName());
        ps.execute();
      }
      catch (SQLException e) {
        _log.error(e.getLocalizedMessage(), e);
      }
      finally {
        SQLUtil.close(ps);
        SQLUtil.close(cn);
      }
    }

}
