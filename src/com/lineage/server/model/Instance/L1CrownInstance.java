package com.lineage.server.model.Instance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.DoorSpawnTable;
import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.L1War;
import com.lineage.server.model.L1WarSpawn;
import com.lineage.server.serverpackets.S_CastleMaster;
import com.lineage.server.serverpackets.S_PacketBoxWar;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import com.lineage.server.world.WorldWar;

/**
 * 对象:王冠 控制项
 * 
 * @author dexc
 * 
 */
public class L1CrownInstance extends L1NpcInstance {
    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    private static final Log _log = LogFactory.getLog(L1CrownInstance.class);

    /**
     * 对象:王冠
     * 
     * @param template
     */
    public L1CrownInstance(final L1Npc template) {
        super(template);
    }

    @Override
    public void onAction(final L1PcInstance player) {
        try {

            boolean in_war = false;
            // 例外状况:不具备血盟
            if (player.getClanid() == 0) {
                return;
            }  
            
            if (player.getAccessLevel() < 1) { //限制夺城
            	return;
            }
            
            // 人物所属血盟名称
            final String playerClanName = player.getClanname();
            // 该血盟数据
            final L1Clan clan = WorldClan.get().getClan(playerClanName);

            // 例外状况:不具备血盟
            if (clan == null) {
                return;
            }
            // 例外状况:人物死亡
            if (player.isDead()) {
                return;
            }
            // 例外状况:人物死亡
            if (player.getCurrentHp() <= 0) {
                return;
            }
            // 鬼魂模式
            if (player.isGhost()) {
                return;
            }
            // 传送中
            if (player.isTeleport()) {
                return;
            }
            // 例外状况:不是王族
            if (!player.isCrown()) {
                return;
            }
            // 例外状况:有变身(人物外型不是公主或王子)
            if ((player.getTempCharGfx() != 0)
                    && (player.getTempCharGfx() != 1)) {
                return;
            }
            // 例外状况:非盟主
            if (player.getId() != clan.getLeaderId()) {
                return;
            }
            // 例外状况:距离过远
            if (!this.checkRange(player)) {
                return;
            }
            // 例外状况:其他城堡城主
            if (clan.getCastleId() != 0) {
                // 474:你已经拥有城堡，无法再拥有其他城堡。
                player.sendPackets(new S_ServerMessage(474));
                return;
            }

            // 王冠座标取回城堡编号
            final int castle_id = L1CastleLocation.getCastleId(this.getX(),
                    this.getY(), this.getMapId());

            // 布告しているかチェック。但し、城主が居ない场合は布告不要
            boolean existDefenseClan = false;

            final L1Clan defence_clan = L1CastleLocation.mapCastle().get(
                    new Integer(castle_id));

            if (defence_clan != null) {
                existDefenseClan = true;
            }

            // 取回全部战争讯息
            final List<L1War> wars = WorldWar.get().getWarList();
            for (final L1War war : wars) {
                // 为战争中城堡
                if (castle_id == war.getCastleId()) {
                    in_war = war.checkClanInWar(playerClanName);
                    break;
                }
            }

            // 城主が居て、布告していない场合
            if (existDefenseClan && (in_war == false)) {
                return;
            }

            // メッセージ表示
            for (final L1War war : wars) {
                if (war.checkClanInWar(playerClanName) && existDefenseClan) {
                    // 自クランが参加中で、城主が交代
                    war.winCastleWar(playerClanName);
                    break;
                }
            }

            // 原城盟不为空 消除相关城堡数据
            if (existDefenseClan && (defence_clan != null)) { // 原城主不为空
                defence_clan.setCastleId(0);
                ClanReading.get().updateClan(defence_clan);

                // 原城主王冠消除
                if (L1CastleLocation.mapCastle().get(new Integer(castle_id)) != null) {
                    L1CastleLocation.removeCastle(new Integer(castle_id));
                }
                
                World.get().broadcastPacketToAll(
                        new S_CastleMaster(0, defence_clan.getLeaderId()));
            }

            clan.setCastleId(castle_id);
            ClanReading.get().updateClan(clan);

            // 新城主王冠显示
            if (L1CastleLocation.mapCastle().get(new Integer(castle_id)) == null) {
                L1CastleLocation.putCastle(castle_id, clan);
            }
            if (castle_id == 2) {// 妖魔城主 暂时使用8显示王冠
                World.get().broadcastPacketToAll(
                        new S_CastleMaster(8, player.getId()));

            } else {
                World.get().broadcastPacketToAll(
                        new S_CastleMaster(castle_id, player.getId()));
            }

            // 血盟以外成员强制移出
            int[] loc = new int[3];
            for (final L1PcInstance pc : World.get().getAllPlayers()) {
                if ((pc.getClanid() != player.getClanid()) && !pc.isGm()) {
                    if (L1CastleLocation.checkInWarArea(castle_id, pc)) {
                        // 战争旗帜范围内
                        loc = L1CastleLocation.getGetBackLoc(castle_id);
                        final int locx = loc[0];
                        final int locy = loc[1];
                        final short mapid = (short) loc[2];
                        L1Teleport.teleport(pc, locx, locy, mapid, 5, true);
                    }
                }
            }

            final L1PcInstance[] clanMember = clan.getOnlineClanMember();
            final String nowDate = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss").format(new Date());

            if (clanMember.length > 0) {
                for (final L1PcInstance pc : clanMember) {
                    if (pc.getId() == clan.getLeaderId()) {
                        // (642) 已掌握了城堡的主导权。
                        pc.sendPackets(new S_PacketBoxWar(
                                S_PacketBoxWar.MSG_WAR_INITIATIVE));
                    }
                    // (643) 已占领城堡。
                    pc.sendPackets(new S_PacketBoxWar(
                            S_PacketBoxWar.MSG_WAR_OCCUPY));
                    pc.sendPackets(new S_ServerMessage("\\aD当前时间: " + nowDate));
                }
            }
            _log.info("占领城堡血盟名称: " + clan.getClanName());

            // 地面王冠物件删除
            this.deleteMe();

            // 删除损坏的守护塔
            for (final L1Object l1object : World.get().getObject()) {
                if (l1object instanceof L1TowerInstance) {
                    final L1TowerInstance tower = (L1TowerInstance) l1object;
                    if (L1CastleLocation.checkInWarArea(castle_id, tower)) {
                        tower.deleteMe();
                    }
                }
            }

            final L1WarSpawn warspawn = new L1WarSpawn();
            warspawn.spawnTower(castle_id);

            // 城门を元に戻す
            for (final L1DoorInstance door : DoorSpawnTable.get().getDoorList()) {
                if (L1CastleLocation.checkInWarArea(castle_id, door)) {
                    door.repairGate();
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void deleteMe() {
        this._destroyed = true;
        if (this.getInventory() != null) {
            this.getInventory().clearItems();
        }
        this.allTargetClear();
        this._master = null;
        World.get().removeVisibleObject(this);
        World.get().removeObject(this);

        for (final L1PcInstance pc : World.get().getRecognizePlayer(this)) {
            pc.removeKnownObject(this);
            pc.sendPackets(new S_RemoveObject(this));
        }

        this.removeAllKnownObjects();
    }

    private boolean checkRange(final L1PcInstance pc) {
        return ((this.getX() - 1 <= pc.getX())
                && (pc.getX() <= this.getX() + 1)
                && (this.getY() - 1 <= pc.getY()) && (pc.getY() <= this.getY() + 1));
    }
}
