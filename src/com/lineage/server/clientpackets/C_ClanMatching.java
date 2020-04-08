package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1ClanJoin;
import com.lineage.server.model.L1ClanMatching;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_ClanMatching;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import java.util.Collection;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 血盟推荐
 * @author Administrator
 *
 */
public class C_ClanMatching extends ClientBasePacket
{
  private static final Log _log = LogFactory.getLog(C_ClanMatching.class);

  @Override
public void start(byte[] decrypt, ClientExecutor client)
  {
    try
    {
      read(decrypt);

      L1PcInstance pc = client.getActiveChar();

      if (pc == null) {
        return;
      }

      if (pc.isGhost()) {
        return;
      }

      if (pc.isDead()) {
        return;
      }

      if (pc.isTeleport()) {
        return;
      }

      int type = readC();

      int objid = 0;
      String text = null;
      int htype = 0;
      if (type == 0) {
        L1ClanMatching cml = L1ClanMatching.getInstance();
        htype = readC();
        text = readS();

        if (!cml.isClanMatchingList(pc.getClanname()))
          cml.writeClanMatching(pc.getClanname(), text, htype);
        else {
          cml.updateClanMatching(pc.getClanname(), text, htype);
        }
      }
      else if (type == 1) {
        L1ClanMatching cml = L1ClanMatching.getInstance();
        if (cml.isClanMatchingList(pc.getClanname())) {
          cml.deleteClanMatching(pc);
        }
      }
      else if (type == 4) {
        L1ClanMatching cml = L1ClanMatching.getInstance();
        if (pc.getClanid() == 0) {
          if (!pc.isCrown())
            cml.loadClanMatchingApcList_User(pc);
        }
        else
          switch (pc.getClanRank()) {
          case 3:
          case 4:
          case 6:
          case 9:
          case 10:
            cml.loadClanMatchingApcList_Crown(pc);
          case 5:
          case 7:
          case 8:
          }
      } else if (type == 5) {
        objid = readD();
        L1Clan clan = getClan(objid);
        if ((clan != null) && 
          (!pc.getCMAList().contains(clan.getClanName()))) {
          L1ClanMatching cml = L1ClanMatching.getInstance();
          cml.writeClanMatchingApcList_User(pc, clan);
          //增加血盟UI有人发出申请时 盟主会有提示 hjx1000
          L1Object Leader = World.get().findObject(clan.getLeaderId());
          if ((Leader != null) && ((Leader instanceof L1PcInstance))) {
        	  L1PcInstance user = (L1PcInstance)Leader;
        	  user.sendPackets(new S_ServerMessage(3246));
          }
        }
      }
      else if (type == 6) {
        objid = readD();
        htype = readC();
        L1ClanMatching cml = L1ClanMatching.getInstance();
        if (htype == 1) {
          L1Object target = World.get().findObject(objid);
          if ((target != null) && ((target instanceof L1PcInstance))) {
            L1PcInstance user = (L1PcInstance)target;
            if (!pc.getCMAList().contains(user.getName())) {
              pc.sendPackets(new S_SystemMessage("用戶應用程序將被取消"));
            }
            else if (L1ClanJoin.getInstance().ClanJoin(pc, user)) {
              cml.deleteClanMatchingApcList(user);
            }
          }
          else if (target == null) {
            pc.sendPackets(new S_SystemMessage("是一個非關聯用戶"));
          }
        }
        else if (htype == 2) {
          L1Object target = World.get().findObject(objid);
          if (target != null) {
            if ((target instanceof L1PcInstance)) {
              L1PcInstance user = (L1PcInstance)target;
              user.removeCMAList(pc.getName());
              pc.removeCMAList(user.getName());
              cml.deleteClanMatchingApcList(user, user.getId(), 
                pc.getClan());
            }
          }
          else cml.deleteClanMatchingApcList(null, objid, pc.getClan());

        }
        else if (htype == 3) {
          L1Clan clan = getClan(objid);
          if ((clan != null) && 
            (pc.getCMAList().contains(clan.getClanName()))) {
            cml.deleteClanMatchingApcList(pc, clan);
          }
        }
      }
      //System.out.println(type+"===="+ objid + "===="+ text + "====" + htype);
      pc.sendPackets(new S_ClanMatching(pc, type, objid, text, htype));     
    } catch (Exception e) {
      _log.error(e.getLocalizedMessage(), e);
    }
    finally {
      over();
    }
  }

  private L1Clan getClan(int objid) {
    L1Clan clan = null;
    Collection<L1Clan> allClans = WorldClan.get().getAllClans();
    for (L1Clan c : allClans) {
      if (c.getClanId() == objid) {
        clan = c;
        break;
      }
    }
    return clan;
  }

  @Override
public String getType()
  {
    return getClass().getSimpleName();
  }
}