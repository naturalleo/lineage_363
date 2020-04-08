package com.lineage.server.clientpackets;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.ClanEmblemReading;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1War;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_HelpMessage;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_War;
import com.lineage.server.templates.L1EmblemIcon;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import com.lineage.server.world.WorldWar;

/**
 * 要求宣战/投降/休战
 * 
 * @author daien
 * 
 */
public class C_War extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_War.class);

    /*
     * public C_War() { }
     * 
     * public C_War(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final L1PcInstance player = client.getActiveChar();
            final String playerName = player.getName();
            final String clanName = player.getClanname();
            final int clanId = player.getClanid();

            if (!player.isCrown()) { // 君主以外
                player.sendPackets(new S_ServerMessage(478)); // 478:\f1只有王子和公主才能宣战。
                return;
            }

            if (clanId == 0) { // 没有血盟
                player.sendPackets(new S_ServerMessage(272)); // 272:\f1若想要战争，首先必须创立血盟。
                return;
            }

            final L1Clan clan = WorldClan.get().getClan(clanName);
            if (clan == null) { // 没有血盟
                return;
            }

            if (player.getId() != clan.getLeaderId()) { // 血盟主
                player.sendPackets(new S_ServerMessage(478)); // 478:\f1只有王子和公主才能宣战。
                return;
            }

            final int type = this.readC();// 模式
            final String tgname = this.readS();

            if (clanName.toLowerCase().equals(tgname.toLowerCase())) { // 对象是自己
                return;
            }

            L1Clan enemyClan = null;// 目标血盟
            String enemyClanName = null;// 目标血盟名称

            final Collection<L1Clan> allClans = WorldClan.get().getAllClans();
            for (final Iterator<L1Clan> iter = allClans.iterator(); iter
                    .hasNext();) {
                final L1Clan checkClan = iter.next();
                if (checkClan.getClanName().toLowerCase()
                        .equals(tgname.toLowerCase())) {
                    enemyClan = checkClan;
                    enemyClanName = checkClan.getClanName();
                    break;
                }
            }
            if (enemyClan == null) { // 对手为空
                if (tgname.equals(" ")) {
                    war_castle(player, clan, null, type);
                }
                return;
            }

            boolean inWar = false;
            final List<L1War> warList = WorldWar.get().getWarList(); // 全部战争清单

            if (enemyClan.getCastleId() == 0) {// 对方不是城盟
                for (final L1War war : warList) {
                    if (war.checkClanInWar(clanName)) { // 战争中
                        if (type == 0) { // 宣战布告
                            player.sendPackets(new S_ServerMessage(234)); // 234:\f1你的血盟已经在战争中。
                            return;
                        }
                        inWar = true;
                        break;
                    }
                }
            }

            if (!inWar && ((type == 2) || (type == 3))) { // 非战争中 投降、结束
                return;
            }

            if (clan.getCastleId() != 0 && ServerWarExecutor.get().isNowWar(clan.getCastleId())) { // 已经是城盟
                if (type == 0) { // 宣战布告
                    player.sendPackets(new S_ServerMessage(474)); // 474:你已经拥有城堡，无法再拥有其他城堡。
                    return;
                } else if ((type == 2) || (type == 3)) { // 城盟不可宣告投降
                    return;
                }
            }

            if ((enemyClan.getCastleId() == 0) && // 对象不为城盟 王族等级小于25
                    (player.getLevel() <= 25)) {
                player.sendPackets(new S_ServerMessage(232)); // 232:\f1等级25以下的君主无法宣战。
                return;
            }

            if (enemyClan.getCastleId() != 0 && ServerWarExecutor.get().isNowWar(enemyClan.getCastleId())) {// 对城堡宣战
                war_castle(player, clan, enemyClan, type);

            } else { // XXX 对血盟宣战
                // 检查城堡战争状态
                if (ServerWarExecutor.get().checkCastleWar() > 0) {
                    // 18:攻城战期间，暂停血盟宣战。
                    player.sendPackets(new S_HelpMessage("\\fS攻城战期间，暂停血盟宣战。"));
                    return;
                }

                boolean enemyInWar = false;
                for (final L1War war : warList) {
                    if (war.checkClanInWar(enemyClanName)) { // 对方战争中
                        switch (type) {
                            case 0: // 宣战布告
                                // 236 %0 血盟拒绝你的宣战。
                                player.sendPackets(new S_ServerMessage(236,
                                        enemyClanName));
                                return;

                            case 2:// 投降
                            case 3:// 结束
                                if (!war.checkClanInSameWar(clanName,
                                        enemyClanName)) { // 自クランと相手クランが别の战争
                                    return;
                                }
                                break;
                        }
                        enemyInWar = true;
                        break;
                    }
                }
                if (!enemyInWar && ((type == 2) || (type == 3))) { // 相手クランが战争中以外で、投降または结束
                    return;
                }

                // 取回对方盟主资料
                final L1PcInstance enemyLeader = World.get().getPlayer(
                        enemyClan.getLeaderName());

                if (enemyLeader == null) {
                    // 218：\f1%0 血盟君主不在线上。
                    player.sendPackets(new S_ServerMessage(218, enemyClanName));
                    return;
                }

                switch (type) {
                    case 0: // 宣战布告
                        enemyLeader.setTempID(player.getId()); // 保存对手ID
                        // 217:%0 血盟向你的血盟宣战。是否接受？(Y/N)
                        enemyLeader.sendPackets(new S_Message_YN(217, clanName,
                                playerName));
                        break;

                    case 2: // 投降
                        enemyLeader.setTempID(player.getId()); // 保存对手ID
                        // 221:%0 血盟要向你投降。是否接受？(Y/N)
                        enemyLeader
                                .sendPackets(new S_Message_YN(221, clanName));
                        break;

                    case 3: // 结束
                        enemyLeader.setTempID(player.getId()); // 保存对手ID
                        // 222:%0 血盟要结束战争。是否接受？(Y/N)
                        enemyLeader
                                .sendPackets(new S_Message_YN(222, clanName));
                        break;
                }
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    /**
     * 对城宣战
     * 
     * @param player
     * @param clan
     * @param enemyClan
     * @param type
     *            0:宣战 2:投降 3:终止
     */
    private void war_castle(final L1PcInstance player, final L1Clan clan,
            final L1Clan enemyClan, final int type) {
        try {
            if (player.getLevel() < 25) {
                // 若要攻城，君主的等级得２５以上。
                player.sendPackets(new S_ServerMessage(475));
                return;
            }

            final L1EmblemIcon emblemIcon = ClanEmblemReading.get().get(
                    clan.getClanId());
            if (emblemIcon == null && !player.isGm()) {
                // 812：想要参加攻城战需有盟徽
                player.sendPackets(new S_ServerMessage(812));
                return;
            }
            
            if (player.getAccessLevel() < 1) { //限制参加城战必须向GM申请 hjx1000
            	player.sendPackets(new S_HelpMessage("\\fS你的血盟必须经过批准才能宣战，如果想参加城战请向GM申请。"));
            	return;
            }

            final String clanName = player.getClanname();// 自己血盟名称

            if (enemyClan == null) {// 攻城 城堡无主人的状况
                L1Object object = World.get().findObject(player.getTempID());
                if (object instanceof L1NpcInstance) {
                    final int id = L1CastleLocation
                            .getCastleIdByArea((L1NpcInstance) object);
                    if (ServerWarExecutor.get().isNowWar(id)) { // 战争时间内
                        final L1PcInstance clanMember[] = clan
                                .getOnlineClanMember();
                        for (int k = 0; k < clanMember.length; k++) {
                            if (L1CastleLocation.checkInWarArea(id,
                                    clanMember[k])) {
                                // 477:包含你，所有的血盟成员到城外，才能宣布攻城。
                                player.sendPackets(new S_ServerMessage(477));
                                return;
                            }
                        }

                        for (int k = 0; k < clanMember.length; k++) {
                            // 226：%0 血盟向 %1血盟宣战。
                            clanMember[k].sendPackets(new S_War(1, clanName,
                                    "NPC"));
                        }
                        player.sendPackets(new S_CloseList(player.getId()));

                    } else { // 战争时间外
                        if (type == 0) { // 宣战布告
                            // 476:尚未到攻城时间。
                            player.sendPackets(new S_ServerMessage(476));
                        }
                        player.sendPackets(new S_CloseList(player.getId()));
                    }
                }
                return;
            }

            final String enemyClanName = enemyClan.getClanName();// 目标血盟名称

            if (WorldWar.get().isWar(clan.getClanName(), enemyClanName)) {
                player.sendPackets(new S_ServerMessage(234)); // 234:\f1你的血盟已经在战争中。
                return;
            }

            final int castle_id = enemyClan.getCastleId();
            if (ServerWarExecutor.get().isNowWar(castle_id)) { // 攻城战期间内
                final L1PcInstance clanMember[] = clan.getOnlineClanMember();
                for (int k = 0; k < clanMember.length; k++) {
                    if (L1CastleLocation.checkInWarArea(castle_id,
                            clanMember[k])) {
                        // 477:包含你，所有的血盟成员到城外，才能宣布攻城。
                        player.sendPackets(new S_ServerMessage(477));
                        return;
                    }
                }
                final List<L1War> warList = WorldWar.get().getWarList(); // 全部战争清单
                boolean enemyInWar = false;
                for (final L1War war : warList) {
                    if (war.checkClanInWar(enemyClanName)) { // 对方战争状态 加入攻击方数据
                        if (type == 0) { // 宣战布告
                            war.declareWar(clanName, enemyClanName);
                            war.addAttackClan(clanName);
                            player.sendPackets(new S_CloseList(player.getId()));

                        } else if ((type == 2) || (type == 3)) {
                            if (!war.checkClanInSameWar(clanName, enemyClanName)) { // 自クランと相手クランが别の战争
                                return;
                            }
                            if (type == 2) { // 投降
                                war.surrenderWar(clanName, enemyClanName);
                            } else if (type == 3) { // 结束
                                war.ceaseWar(clanName, enemyClanName);
                            }
                        }
                        enemyInWar = true;
                        break;
                    }
                }
                if (!enemyInWar && (type == 0)) { // 对方未战争状态 建立战争数据
                    final L1War war = new L1War();
                    war.handleCommands(1, clanName, enemyClanName); // 攻城战开始
                    player.sendPackets(new S_CloseList(player.getId()));
                }

            } else { // 战争时间外
                if (type == 0) { // 宣战布告
                    // 476:尚未到攻城时间。
                    player.sendPackets(new S_ServerMessage(476));
                }
                player.sendPackets(new S_CloseList(player.getId()));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
