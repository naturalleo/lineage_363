package com.lineage.server.model;

import com.lineage.DatabaseFactory;
import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigOther;
import com.lineage.data.quest.CrownLv45_1;
import com.lineage.server.datatables.lock.ClanEmblemReading;
import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ClanUpdate;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 血盟推荐
 * @author Administrator
 *
 */
public class L1ClanJoin
{
  private static final Log _log = LogFactory.getLog(L1ClanJoin.class);
  private static L1ClanJoin _instance;

  public static L1ClanJoin getInstance()
  {
    if (_instance == null) {
      _instance = new L1ClanJoin();
    }
    return _instance;
  }

  public boolean ClanJoin(L1PcInstance pc, L1PcInstance joinPc)
  {
    int clan_id = pc.getClanid();
    String clanName = pc.getClanname();
    L1Clan clan = WorldClan.get().getClan(clanName);
    if (clan != null) {
      int maxMember = 0;

      int charisma = 0;
      if (pc.getId() != clan.getLeaderId())
      {
        charisma = pc.getCha();
      }
      else charisma = getOfflineClanLeaderCha(clan.getLeaderId());

      boolean lv45quest = false;

      if (pc.getQuest().isEnd(CrownLv45_1.QUEST.get_id())) {
        lv45quest = true;
      }
      if (pc.getLevel() >= 50) {
        if (lv45quest)
          maxMember = charisma * 9;
        else {
          maxMember = charisma * 3;
        }
      }
      else if (lv45quest)
        maxMember = charisma * 6;
      else {
        maxMember = charisma * 2;
      }

      if (ConfigOther.CLANCOUNT != 0) {
        maxMember = ConfigOther.CLANCOUNT;
      }

      if (joinPc.getClanid() == 0) {
        String[] clanMembersName = clan.getAllMembers();
        if (maxMember <= clanMembersName.length)
        {
          joinPc.sendPackets(new S_ServerMessage(188, pc.getName()));
          return false;
        }

        for (L1PcInstance clanMembers : clan.getOnlineClanMember())
        {
          clanMembers.sendPackets(new S_ServerMessage(94, joinPc.getName()));
        }

        joinPc.setClanid(clan_id);
        joinPc.setClanname(clanName);
        joinPc.setClanRank(8);
        try
        {
          joinPc.save();
        }
        catch (Exception e) {
          e.printStackTrace();
        }

        joinPc.sendPackets(new S_ClanUpdate(joinPc.getId(), joinPc.getClanname(), joinPc.getClanRank()));
        clan.addMemberName(joinPc.getName());

        for (L1PcInstance clanMembers : clan.getOnlineClanMember()) {
          clanMembers.sendPackets(new S_ClanUpdate(joinPc.getId(), joinPc.getClanname(), joinPc.getClanRank()));
        }

        joinPc.sendPackets(new S_ServerMessage(95, clanName));
        L1Teleport.teleport(joinPc, joinPc.getX(), joinPc.getY(), joinPc.getMapId(), joinPc.getHeading(), false);
      }
      else if (ConfigAlt.CLAN_ALLIANCE) {
        changeClan(pc, joinPc, maxMember);
      }
      else
      {
        joinPc.sendPackets(new S_ServerMessage(89));
      }
    }
    else {
      return false;
    }
    return true;
  }

  private void changeClan(L1PcInstance pc, L1PcInstance joinPc, int maxMember) {
    int clanId = pc.getClanid();
    String clanName = pc.getClanname();
    L1Clan clan = WorldClan.get().getClan(clanName);
    String[] clanMemberName = clan.getAllMembers();
    int clanNum = clanMemberName.length;

    int oldClanId = joinPc.getClanid();
    String oldClanName = joinPc.getClanname();
    L1Clan oldClan = WorldClan.get().getClan(oldClanName);
    String[] oldClanMemberName = oldClan.getAllMembers();
    int oldClanNum = oldClanMemberName.length;

    if ((clan != null) && (oldClan != null) && (joinPc.isCrown()) && 
      (joinPc.getId() == oldClan.getLeaderId())) {
      if (maxMember < clanNum + oldClanNum)
      {
        joinPc.sendPackets(new S_ServerMessage(188, pc.getName()));
        return;
      }

      L1PcInstance[] clanMember = clan.getOnlineClanMember();
      for (int cnt = 0; cnt < clanMember.length; cnt++)
      {
        clanMember[cnt].sendPackets(new S_ServerMessage(94, joinPc.getName()));
      }

      for (int i = 0; i < oldClanMemberName.length; i++) {
        L1PcInstance oldClanMember = World.get().getPlayer(oldClanMemberName[i]);
        if (oldClanMember != null) {
          oldClanMember.setClanid(clanId);
          oldClanMember.setClanname(clanName);

          if (oldClanMember.getId() == joinPc.getId()) {
            oldClanMember.setClanRank(3);
          }
          else {
            oldClanMember
              .setClanRank(5);
          }

          try
          {
            oldClanMember.save();
            L1Teleport.teleport(oldClanMember, oldClanMember.getX(), oldClanMember.getY(), oldClanMember.getMapId(), oldClanMember.getHeading(), false);
          } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
          }

          clan.addMemberName(oldClanMember.getName());

          oldClanMember.sendPackets(new S_ServerMessage(95, clanName));
          try
          {
            L1PcInstance offClanMember = CharacterTable.get().restoreCharacter(
              oldClanMemberName[i]);
            offClanMember.setClanid(clanId);
            offClanMember.setClanname(clanName);
            offClanMember.setClanRank(1);
            offClanMember.save();
            clan.addMemberName(offClanMember.getName());
          }
          catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
          }
        }
      }

      ClanEmblemReading.get().deleteIcon(oldClanId);
      ClanReading.get().deleteClan(oldClanName);

      L1ClanMatching Clan = L1ClanMatching.getInstance();
      Clan.deleteClanMatching(joinPc);
    }
  }

  public int getOfflineClanLeaderCha(int member)
  {
    Connection con = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;
    try {
      con = DatabaseFactory.get().getConnection();
      pstm = con
        .prepareStatement("SELECT Cha FROM characters WHERE objid=?");
      pstm.setInt(1, member);
      rs = pstm.executeQuery();
      if (!rs.next()) {
        return 0;
      }
      int i = rs.getInt("Cha");
      return i;
    }
    catch (Exception e) {
      _log.error(e.getLocalizedMessage(), e);
    } finally {
      SQLUtil.close(rs);
      SQLUtil.close(pstm);
      SQLUtil.close(con);
    }
    return 0;
  }
}