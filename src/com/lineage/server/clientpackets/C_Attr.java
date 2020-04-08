package com.lineage.server.clientpackets;

import java.util.regex.Matcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigOther;
import com.lineage.data.quest.CrownLv45_1;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.datatables.lock.ClanEmblemReading;
import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.datatables.lock.HouseReading;
import com.lineage.server.datatables.lock.PetReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1ChatParty;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1ClanMatching;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Party;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.L1War;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.serverpackets.S_ChangeName;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_ClanUpdate;
import com.lineage.server.serverpackets.S_OwnCharStatus2;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_Resurrection;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_Trade;
import com.lineage.server.templates.L1House;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1Pet;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import com.lineage.server.world.WorldWar;

/**
 * 要求点选项目的结果(Y/N)
 * 
 * @author dexc
 * 
 */
public class C_Attr extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Attr.class);

    /*
     * public C_Attr() { }
     * 
     * public C_Attr(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    // private static final int HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1
    // };

    // private static final int HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1
    // };

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final L1PcInstance pc = client.getActiveChar();

            if (pc.isGhost()) { // 鬼魂模式
                return;
            }

            if (pc.isTeleport()) { // 传送中
                return;
            }

            int mode = readH();
            int tgobjid = 0;

            if (mode == 0) {
                tgobjid = readD();
                mode = readH();
            }

            int c;

            switch (mode) {
                case 97: // \f3%0%s 想加入你的血盟。你接受吗。(Y/N)
                    c = this.readC();
                    final L1PcInstance joinPc = (L1PcInstance) World.get()
                            .findObject(pc.getTempID());
                    pc.setTempID(0);
                    if (joinPc != null) {
                        if (c == 0) { // No
                            // 96 \f1%0%s 拒绝你的请求
                            joinPc.sendPackets(new S_ServerMessage(96, pc
                                    .getName()));

                        } else if (c == 1) { // Yes
                            final int clan_id = pc.getClanid();
                            final String clanName = pc.getClanname();
                            final L1Clan clan = WorldClan.get().getClan(
                                    clanName);
                            if (clan != null) {
                                int maxMember = 0;
                                final int charisma = pc.getCha();
                                boolean lv45quest = false;
                                if (pc.getQuest().isEnd(
                                        CrownLv45_1.QUEST.get_id())) {
                                    lv45quest = true;
                                }
                                if (pc.getLevel() >= 50) { // Lv50以上
                                    if (lv45quest == true) { // Lv45クエストクリア济み
                                        maxMember = charisma * 9;

                                    } else {
                                        maxMember = charisma * 3;
                                    }
                                } else { // Lv50未满
                                    if (lv45quest == true) { // Lv45クエストクリア济み
                                        maxMember = charisma * 6;

                                    } else {
                                        maxMember = charisma * 2;
                                    }
                                }
                                if (ConfigOther.CLANCOUNT != 0) {
                                    maxMember = ConfigOther.CLANCOUNT;
                                }
                                if (joinPc.getClanid() == 0) { // クラン未加入
                                    final String clanMembersName[] = clan
                                            .getAllMembers();
                                    if (maxMember <= clanMembersName.length) {
                                        // 188 %0%s 无法接受你成为该血盟成员。
                                        joinPc.sendPackets(new S_ServerMessage(
                                                188, pc.getName()));
                                        return;
                                    }

                                    for (final L1PcInstance clanMembers : clan
                                            .getOnlineClanMember()) {
                                        // 94 \f1你接受%0当你的血盟成员。
                                        clanMembers
                                                .sendPackets(new S_ServerMessage(
                                                        94, joinPc.getName()));
                                    }

                                    joinPc.setClanid(clan_id);
                                    joinPc.setClanname(clanName);
                                    joinPc.setClanRank(L1Clan.NORMAL_CLAN_RANK_ATTEND);

                                    joinPc.save();
                                    // 新入盟成员发送更新血盟数据
                                    joinPc.sendPackets(new S_ClanUpdate(joinPc
                                            .getId(), joinPc.getClanname(),
                                            joinPc.getClanRank()));
                                    clan.addMemberName(joinPc.getName());
                                    // 在线上的血盟成员发送新加入成员血盟数据
                                    for (L1PcInstance clanMembers : clan
                                            .getOnlineClanMember()) {
                                        clanMembers
                                                .sendPackets(new S_ClanUpdate(
                                                        joinPc.getId(), joinPc
                                                                .getClanname(),
                                                        joinPc.getClanRank()));
                                    }
                                    // 95 \f1加入%0血盟。
                                    joinPc.sendPackets(new S_ServerMessage(95,
                                            clanName));

                                } else { // クラン加入济み（クラン连合）
                                    if (ConfigAlt.CLAN_ALLIANCE) {
                                        this.changeClan(client, pc, joinPc,
                                                maxMember);

                                    } else {
                                        // 89 \f1你已经有血盟了。
                                        joinPc.sendPackets(new S_ServerMessage(
                                                89));
                                    }
                                }
                            }
                        }
                    }
                    break;

                case 217: // %0 血盟向你的血盟宣战。是否接受？(Y/N)
                case 221: // %0 血盟要向你投降。是否接受？(Y/N)
                case 222: // %0 血盟要结束战争。是否接受？(Y/N)
                    c = this.readC();
                    // 宣战者
                    final L1PcInstance enemyLeader = (L1PcInstance) World.get()
                            .findObject(pc.getTempID());
                    if (enemyLeader == null) {
                        return;
                    }
                    pc.setTempID(0);
                    final String clanName = pc.getClanname();
                    final String enemyClanName = enemyLeader.getClanname();// 宣战盟
                    if (c == 0) { // No
                        if (mode == 217) {
                            // 236 %0 血盟拒绝你的宣战。
                            enemyLeader.sendPackets(new S_ServerMessage(236,
                                    clanName));

                        } else if ((mode == 221) || (mode == 222)) {
                            // 237 %0 血盟拒绝你的提案。
                            enemyLeader.sendPackets(new S_ServerMessage(237,
                                    clanName));
                        }

                    } else if (c == 1) { // Yes
                        if (mode == 217) {
                            final L1War war = new L1War();
                            war.handleCommands(2, enemyClanName, clanName); // 模拟战开始

                        } else if ((mode == 221) || (mode == 222)) {
                            // 取回全部战争清单
                            for (final L1War war : WorldWar.get().getWarList()) {
                                if (war.checkClanInWar(clanName)) { // 战争中
                                    if (mode == 221) {
                                        war.surrenderWar(enemyClanName,
                                                clanName); // 投降

                                    } else if (mode == 222) {
                                        war.ceaseWar(enemyClanName, clanName); // 结束
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    break;

                case 252: // \f2%0%s 要与你交易。愿不愿交易？ (Y/N)
                    c = this.readC();
                    final L1PcInstance trading_partner = (L1PcInstance) World
                            .get().findObject(pc.getTradeID());
                    if (trading_partner != null) {
                        if (c == 0) {// No
                            // 253 %0%d 拒绝与你交易。
                            trading_partner.sendPackets(new S_ServerMessage(
                                    253, pc.getName()));
                            pc.setTradeID(0);
                            trading_partner.setTradeID(0);

                        } else if (c == 1) {// Yes
                            pc.sendPackets(new S_Trade(trading_partner
                                    .getName()));
                            trading_partner.sendPackets(new S_Trade(pc
                                    .getName()));
                        }
                    }
                    break;

                case 321: // 321 是否要复活？ (Y/N)
                    c = this.readC();
                    final L1PcInstance resusepc1 = (L1PcInstance) World.get()
                            .findObject(pc.getTempID());
                    pc.setTempID(0);
                    if (resusepc1 != null) { // 复活スクロール
                        if (c == 0) { // No
                            ;
                        } else if (c == 1) { // Yes
                            pc.sendPacketsX8(new S_SkillSound(pc.getId(),
                                    '\346'));
                            // pc.resurrect(pc.getLevel());
                            // pc.setCurrentHp(pc.getLevel());
                            pc.resurrect(pc.getMaxHp() / 2);
                            pc.setCurrentHp(pc.getMaxHp() / 2);
                            pc.startHpRegeneration();
                            pc.startMpRegeneration();
                            pc.stopPcDeleteTimer();
                            pc.sendPacketsAll(new S_Resurrection(pc, resusepc1,
                                    0));
                            pc.sendPacketsAll(new S_CharVisualUpdate(pc));
                        }
                    }
                    break;

                case 322: // 322 是否要复活？ (Y/N)
                    c = this.readC();
                    final L1PcInstance resusepc2 = (L1PcInstance) World.get()
                            .findObject(pc.getTempID());
                    pc.setTempID(0);
                    if (resusepc2 != null) { // 祝福された 复活スクロール、リザレクション、グレーター
                                             // リザレクション
                        if (c == 0) { // No
                            ;
                        } else if (c == 1) { // Yes

                            pc.sendPacketsX8(new S_SkillSound(pc.getId(),
                                    '\346'));
                            pc.resurrect(pc.getMaxHp());
                            pc.setCurrentHp(pc.getMaxHp());
                            pc.startHpRegeneration();
                            pc.startMpRegeneration();
                            pc.stopPcDeleteTimer();
                            pc.sendPacketsAll(new S_Resurrection(pc, resusepc2,
                                    0));
                            pc.sendPacketsAll(new S_CharVisualUpdate(pc));
                            // EXPロストしている、G-RESを挂けられた、EXPロストした死亡
                            // 全てを满たす场合のみEXP复旧
                            if ((pc.getExpRes() == 1) && pc.isGres()
                                    && pc.isGresValid()) {
                                pc.resExp();
                                pc.setExpRes(0);
                                pc.setGres(false);
                            }
                        }
                    }
                    break;

                case 325: // 你想叫它什么名字？
                    c = readC();
                    String petName = readS();

                    if (pc.is_rname()) {
                        String name = Matcher.quoteReplacement(petName);
                        name = name.replaceAll("\\s", "");
                        name = name.replaceAll("　", "");
                        name = name.substring(0, 1).toUpperCase()
                                + name.substring(1);

                        for (String ban : C_CreateChar.BANLIST) {
                            if (name.indexOf(ban) != -1) {
                                name = "";
                            }
                        }
                        
                        //hjx1000 限制输入只有中文
                        if(!name.matches("[\\u4E00-\\u9FA5]+")) {
                        	pc.sendPackets(new S_ServerMessage(53));
                        	return;
                        }

                        if (name.length() == 0) {
                            // 53 无效的名字。 若您擅自修改，将无法进行游戏。
                            pc.sendPackets(new S_ServerMessage(53));
                            return;
                        }
                        // 名称是否包含禁止字元
                        if (!C_CreateChar.isInvalidName(name)) {
                            // 53 无效的名字。 若您擅自修改，将无法进行游戏。
                            pc.sendPackets(new S_ServerMessage(53));
                            return;
                        }

                        // 检查名称是否以被使用
                        if (CharObjidTable.get().charObjid(name) != 0) {
                            // 58 已经有同样的角色名称。请重新输入。
                            pc.sendPackets(new S_ServerMessage(58));
                            return;
                        }
                        // String srcname = pc.getName();
                        World.get().removeObject(pc);// 移出世界

                        pc.getInventory().consumeItem(41227, 1);

                        pc.setName(name);
                        CharObjidTable.get().reChar(pc.getId(), name);
                        CharacterTable.get().newCharName(pc.getId(), name);
                        World.get().storeObject(pc);// 重新加入世界
                        // 改变显示(复原正常)
                        pc.sendPackets(new S_ChangeName(pc, true));

                        pc.sendPackets(new S_ServerMessage(166,
                                "由于人物名称异动!请重新登入游戏!将于5秒后强制断线!"));
                        final KickPc kickPc = new KickPc(pc);
                        kickPc.start_cmd();

                    } else {
                        final L1PetInstance pet = (L1PetInstance) World.get()
                                .findObject(pc.getTempID());
                        pc.setTempID(0);
                        renamePet(pet, petName);
                    }
                    pc.rename(false);
                    break;
                case 512: // 请输入血盟小屋名称?
                    c = this.readC(); // ?
                    String houseName = this.readS();
                    final int houseId = pc.getTempID();
                    pc.setTempID(0);
                    if (houseName.length() <= 16) {
                        final L1House house = HouseReading.get().getHouseTable(
                                houseId);
                        house.setHouseName(houseName);
                        HouseReading.get().updateHouse(house); // DBに书き迂み

                    } else {
                        pc.sendPackets(new S_ServerMessage(513)); // 血盟小屋名称太长。
                    }
                    break;

                case 630: // %0%s 要与你决斗。你是否同意？(Y/N)
                    c = this.readC();
                    final L1PcInstance fightPc = (L1PcInstance) World.get()
                            .findObject(pc.getFightId());
                    if (c == 0) {
                        pc.setFightId(0);
                        fightPc.setFightId(0);
                        // 631 %0%d 拒绝了与你的决斗。
                        fightPc.sendPackets(new S_ServerMessage(631, pc
                                .getName()));

                    } else if (c == 1) {
                        fightPc.sendPackets(new S_PacketBox(
                                S_PacketBox.MSG_DUEL, fightPc.getFightId(),
                                fightPc.getId()));

                        pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, pc
                                .getFightId(), pc.getId()));
                    }
                    break;

                case 653: // 若你离婚，你的结婚戒指将会消失。你决定要离婚吗？(Y/N)
                    c = this.readC();
                    final L1PcInstance target653 = (L1PcInstance) World.get()
                            .findObject(pc.getPartnerId());
                    if (c == 0) { // No
                        return;
                    } else if (c == 1) { // Yes
                        if (target653 != null) {
                            target653.setPartnerId(0);
                            target653.save();
                            // 662 \f1你(你)目前未婚。
                            target653.sendPackets(new S_ServerMessage(662));

                        } else {
                            // 人物离线状态
                            CharacterTable.get();
                            CharacterTable.updatePartnerId(pc.getPartnerId());
                        }
                    }

                    pc.setPartnerId(0);
                    pc.save(); // 资料存档

                    // 662 \f1你(你)目前未婚。
                    pc.sendPackets(new S_ServerMessage(662));
                    break;

                case 654: // %0 向你(你)求婚，你(你)答应吗?
                    c = this.readC();
                    final L1PcInstance partner = (L1PcInstance) World.get()
                            .findObject(pc.getTempID());
                    pc.setTempID(0);
                    if (partner != null) {
                        if (c == 0) { // No
                            // 656 %0 拒绝你(你)的求婚。
                            partner.sendPackets(new S_ServerMessage(656, pc
                                    .getName()));

                        } else if (c == 1) { // Yes
                            pc.setPartnerId(partner.getId());
                            pc.save();
                            // 790 俩人的结婚在所有人的祝福下完成
                            pc.sendPackets(new S_ServerMessage(790));
                            // 655 恭喜!! %0 已接受你(你)的求婚。
                            pc.sendPackets(new S_ServerMessage(655, partner
                                    .getName()));

                            partner.setPartnerId(pc.getId());
                            partner.save();
                            // 790 俩人的结婚在所有人的祝福下完成
                            partner.sendPackets(new S_ServerMessage(790));
                            // 655 恭喜!! %0 已接受你(你)的求婚。
                            partner.sendPackets(new S_ServerMessage(655, pc
                                    .getName()));
                        }
                    }
                    break;

                case 729: // 盟主正在呼唤你，你要接受他的呼唤吗？(Y/N)
                    c = this.readC();
                    if (c == 0) { // No
                        ;
                    } else if (c == 1) { // Yes
                        this.callClan(pc);
                    }
                    break;

                case 738: // 恢复经验值需消耗%0金币。想要恢复经验值吗?
                    c = this.readC();
                    if (c == 0) { // No
                        ;
                    } else if ((c == 1) && (pc.getExpRes() == 1)) { // Yes
                        int cost = 0;
                        final int level = pc.getLevel();
                        final int lawful = pc.getLawful();
                        if (level < 45) {
                            cost = level * level * 100;

                        } else {
                            cost = level * level * 200;
                        }

                        if (lawful >= 0) {
                            cost = (cost / 2);
                        }

                        if (pc.getInventory().consumeItem(L1ItemId.ADENA, cost)) {
                            pc.resExp();
                            pc.setExpRes(0);

                        } else {
                            // 189 \f1金币不足。
                            pc.sendPackets(new S_ServerMessage(189));
                        }
                    }
                    break;

                case 748: // 你的血盟成员想要传送你。你答应吗？(Y/N)
                    c = this.readC();
                    if (c == 0) { // No
                        ;

                    } else if (c == 1) { // Yes
                        final int newX = pc.getTeleportX();
                        final int newY = pc.getTeleportY();
                        final short mapId = pc.getTeleportMapId();

                        L1Teleport.teleport(pc, newX, newY, mapId, 5, true);
                    }
                    break;

                case 951: // 您要接受玩家 %0%s 提出的队伍对话邀请吗？(Y/N)
                    c = this.readC();
                    final L1PcInstance chatPc = (L1PcInstance) World.get()
                            .findObject(pc.getPartyID());
                    if (chatPc != null) {
                        if (c == 0) { // No
                            // 423 %0%s 拒绝了您的邀请。
                            chatPc.sendPackets(new S_ServerMessage(423, pc
                                    .getName()));
                            pc.setPartyID(0);

                        } else if (c == 1) { // Yes
                            if (chatPc.isInChatParty()) {
                                if (chatPc.getChatParty().isVacancy()
                                        || chatPc.isGm()) {
                                    chatPc.getChatParty().addMember(pc);

                                } else {
                                    // 417 你的队伍已经满了，无法再接受队员。
                                    chatPc.sendPackets(new S_ServerMessage(417));
                                }

                            } else {
                                final L1ChatParty chatParty = new L1ChatParty();
                                chatParty.addMember(chatPc);
                                chatParty.addMember(pc);
                                // 424 %0%s 加入了您的队伍。
                                chatPc.sendPackets(new S_ServerMessage(424, pc
                                        .getName()));
                            }
                        }
                    }
                    break;

                case 953: // 玩家 %0%s 邀请您加入队伍？(Y/N)
                    c = this.readC();
                    final L1PcInstance target = (L1PcInstance) World.get()
                            .findObject(pc.getPartyID());
                    if (target != null) {
                        if (c == 0) {// No
                            // 423 %0%s 拒绝了您的邀请。
                            target.sendPackets(new S_ServerMessage(423, pc
                                    .getName()));
                            pc.setPartyID(0);

                        } else if (c == 1) {// Yes
                            if (target.isInParty()) {
                                // 邀请加入者 已成立队伍
                                if (target.getParty().isVacancy()) {
                                    // 加入新的队伍成员
                                    target.getParty().addMember(pc);

                                } else {
                                    // 417：你的队伍已经满了，无法再接受队员。
                                    target.sendPackets(new S_ServerMessage(417));
                                }

                            } else {
                                // 邀请加入者 尚未成立队伍
                                final L1Party party = new L1Party();
                                party.addMember(target);// 第一个加入队伍者将为队长
                                party.addMember(pc);
                                // 424：%0%s 加入了您的队伍。
                                target.sendPackets(new S_ServerMessage(424, pc
                                        .getName()));
                            }
                        }
                    }
                    break;

                case 479: // 你想提升那一种属性?
                    c = this.readC();
                    if (c == 1) {
                        final String s = this.readS();
                        if (!(pc.getLevel() - 50 > pc.getBonusStats())) {
                            return;
                        }
                        if (s.equalsIgnoreCase("str")) {
                            if (pc.getBaseStr() < ConfigAlt.POWER) {
                                pc.addBaseStr((byte) 1); // 素のSTR值に+1
                                pc.setBonusStats(pc.getBonusStats() + 1);
                                pc.sendPackets(new S_OwnCharStatus2(pc));
                                pc.sendPackets(new S_CharVisualUpdate(pc));
                                pc.save(); // 人物资料记录

                            } else {
                                // 481 \f1属性最大值只能到35。 请重试一次。
                                pc.sendPackets(new S_ServerMessage(481));
                            }

                        } else if (s.equalsIgnoreCase("dex")) {
                            if (pc.getBaseDex() < ConfigAlt.POWER) {
                                pc.addBaseDex((byte) 1); // 素のDEX值に+1
                                pc.resetBaseAc();
                                pc.setBonusStats(pc.getBonusStats() + 1);
                                pc.sendPackets(new S_OwnCharStatus2(pc));
                                pc.sendPackets(new S_CharVisualUpdate(pc));
                                pc.save(); // 人物资料记录

                            } else {
                                // 481 \f1属性最大值只能到35。 请重试一次。
                                pc.sendPackets(new S_ServerMessage(481));
                            }

                        } else if (s.equalsIgnoreCase("con")) {
                            if (pc.getBaseCon() < ConfigAlt.POWER) {
                                pc.addBaseCon((byte) 1); // 素のCON值に+1
                                pc.setBonusStats(pc.getBonusStats() + 1);
                                pc.sendPackets(new S_OwnCharStatus2(pc));
                                pc.sendPackets(new S_CharVisualUpdate(pc));
                                pc.save(); // 人物资料记录

                            } else {
                                pc.sendPackets(new S_ServerMessage(481));
                            }

                        } else if (s.equalsIgnoreCase("int")) {
                            if (pc.getBaseInt() < ConfigAlt.POWER) {
                                pc.addBaseInt((byte) 1); // 素のINT值に+1
                                pc.setBonusStats(pc.getBonusStats() + 1);
                                pc.sendPackets(new S_OwnCharStatus2(pc));
                                pc.sendPackets(new S_CharVisualUpdate(pc));
                                pc.save(); // 人物资料记录

                            } else {
                                // 481 \f1属性最大值只能到35。 请重试一次。
                                pc.sendPackets(new S_ServerMessage(481));
                            }

                        } else if (s.equalsIgnoreCase("wis")) {
                            if (pc.getBaseWis() < ConfigAlt.POWER) {
                                pc.addBaseWis((byte) 1); // 素のWIS值に+1
                                pc.resetBaseMr();
                                pc.setBonusStats(pc.getBonusStats() + 1);
                                pc.sendPackets(new S_OwnCharStatus2(pc));
                                pc.sendPackets(new S_CharVisualUpdate(pc));
                                pc.save(); // 人物资料记录

                            } else {
                                // 481 \f1属性最大值只能到35。 请重试一次。
                                pc.sendPackets(new S_ServerMessage(481));
                            }

                        } else if (s.equalsIgnoreCase("cha")) {
                            if (pc.getBaseCha() < ConfigAlt.POWER) {
                                pc.addBaseCha((byte) 1); // 素のCHA值に+1
                                pc.setBonusStats(pc.getBonusStats() + 1);
                                pc.sendPackets(new S_OwnCharStatus2(pc));
                                pc.sendPackets(new S_CharVisualUpdate(pc));
                                pc.save(); // 人物资料记录

                            } else {
                                // 481 \f1属性最大值只能到35。 请重试一次。
                                pc.sendPackets(new S_ServerMessage(481));
                            }
                        }
                    }
                    break;

                default:
                    break;
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    private void changeClan(final ClientExecutor clientthread,
            final L1PcInstance pc, final L1PcInstance joinPc,
            final int maxMember) {
        final int clanId = pc.getClanid();
        final String clanName = pc.getClanname();
        final L1Clan clan = WorldClan.get().getClan(clanName);
        final String clanMemberName[] = clan.getAllMembers();
        final int clanNum = clanMemberName.length;

        final int oldClanId = joinPc.getClanid();
        final String oldClanName = joinPc.getClanname();
        final L1Clan oldClan = WorldClan.get().getClan(oldClanName);
        final String oldClanMemberName[] = oldClan.getAllMembers();
        final int oldClanNum = oldClanMemberName.length;
        if ((clan != null) && (oldClan != null) && joinPc.isCrown() && // 自分が君主
                (joinPc.getId() == oldClan.getLeaderId())) {
            if (maxMember < clanNum + oldClanNum) { // 空きがない
                // 188 %0%s 无法接受你成为该血盟成员。
                joinPc.sendPackets(new S_ServerMessage(188, pc.getName()));
                return;
            }

            final L1PcInstance clanMember[] = clan.getOnlineClanMember();
            for (int cnt = 0; cnt < clanMember.length; cnt++) {
                // 94 \f1你接受%0当你的血盟成员。
                clanMember[cnt].sendPackets(new S_ServerMessage(94, joinPc
                        .getName()));
            }

            for (int i = 0; i < oldClanMemberName.length; i++) {
                final L1PcInstance oldClanMember = World.get().getPlayer(
                        oldClanMemberName[i]);
                if (oldClanMember != null) { // オンライン中の旧クランメンバー
                    oldClanMember.setClanid(clanId);
                    oldClanMember.setClanname(clanName);
                    // 血盟连合に加入した君主はガーディアン
                    // 君主が连れてきた血盟员は见习い
                    if (oldClanMember.getId() == joinPc.getId()) {
                        oldClanMember.setClanRank(L1Clan.CLAN_RANK_GUARDIAN);

                    } else {
                        oldClanMember
                                .setClanRank(L1Clan.ALLIANCE_CLAN_RANK_ATTEND);
                    }

                    try {
                        // 资料存档
                        oldClanMember.save();

                    } catch (final Exception e) {
                        _log.error(e.getLocalizedMessage(), e);
                    }

                    clan.addMemberName(oldClanMember.getName());
                    // 95 \f1加入%0血盟。
                    oldClanMember
                            .sendPackets(new S_ServerMessage(95, clanName));

                } else { // オフライン中の旧クランメンバー
                    try {
                        final L1PcInstance offClanMember = CharacterTable.get()
                                .restoreCharacter(oldClanMemberName[i]);
                        offClanMember.setClanid(clanId);
                        offClanMember.setClanname(clanName);
                        offClanMember.setClanRank(L1Clan.CLAN_RANK_PROBATION);
                        offClanMember.save(); // 资料存档
                        clan.addMemberName(offClanMember.getName());

                    } catch (final Exception e) {
                        _log.error(e.getLocalizedMessage(), e);
                    }
                }
            }

            // 资料删除
            ClanEmblemReading.get().deleteIcon(oldClanId);
            /*
             * final String emblem_file = String.valueOf(oldClanId); final File
             * file = new File("emblem/" + emblem_file); file.delete();
             */
            ClanReading.get().deleteClan(oldClanName);
            L1ClanMatching Clan = L1ClanMatching.getInstance();//血盟UI hjx1000
            Clan.deleteClanMatching(joinPc);//血盟UI hjx1000
        }
    }

    private static void renamePet(final L1PetInstance pet, final String name) {
        if ((pet == null) || (name == null)) {
            throw new NullPointerException();
        }

        final int petItemObjId = pet.getItemObjId();
        final L1Pet petTemplate = PetReading.get().getTemplate(petItemObjId);
        if (petTemplate == null) {
            throw new NullPointerException();
        }

        final L1PcInstance pc = (L1PcInstance) pet.getMaster();
        if (PetReading.get().isNameExists(name)) {
            // 327 同样的名称已经存在。
            pc.sendPackets(new S_ServerMessage(327));
            return;
        }

        final L1Npc l1npc = NpcTable.get().getTemplate(pet.getNpcId());
        if (!(pet.getName().equalsIgnoreCase(l1npc.get_name()))) {
            // 326 一旦你已决定就不能再变更。
            pc.sendPackets(new S_ServerMessage(326));
            return;
        }

        pet.setName(name);
        petTemplate.set_name(name);
        PetReading.get().storePet(petTemplate);

        final L1ItemInstance item = pc.getInventory().getItem(
                pet.getItemObjId());
        pc.getInventory().updateItem(item);
        pc.sendPacketsAll(new S_ChangeName(pet.getId(), name));
    }

    private void callClan(final L1PcInstance pc) {
        final L1PcInstance callClanPc = (L1PcInstance) World.get().findObject(
                pc.getTempID());
        pc.setTempID(0);

        // 无法攻击/使用道具/技能/回城的状态 XXX
        if (pc.isParalyzedX()) {
            return;
        }// */
        if (callClanPc == null) {
            return;
        }
        if (!pc.getMap().isEscapable() && !pc.isGm()) {
            // 647 这附近的能量影响到瞬间移动。在此地无法使用瞬间移动。
            pc.sendPackets(new S_ServerMessage(647));
            return;
        }
        if (pc.getId() != callClanPc.getCallClanId()) {
            return;
        }

        boolean isInWarArea = false;
        final int castleId = L1CastleLocation.getCastleIdByArea(callClanPc);
        if (castleId != 0) {
            isInWarArea = true;
            if (ServerWarExecutor.get().isNowWar(castleId)) {
                isInWarArea = false; // 战争时间中は旗内でも使用可能
            }
        }
        final short mapId = callClanPc.getMapId();
        if (((mapId != 0) && (mapId != 4) && (mapId != 304)) || isInWarArea) {
            // 626 因太远以致于无法传送到你要去的地方。
            pc.sendPackets(new S_ServerMessage(629));
            return;
        }

        // 副本地图中判断
        if (QuestMapTable.get().isQuestMap(pc.getMapId())) {
            // 626 因太远以致于无法传送到你要去的地方。
            pc.sendPackets(new S_ServerMessage(629));
            return;
        }

        final int HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };

        final int HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

        final L1Map map = callClanPc.getMap();
        int locX = callClanPc.getX();
        int locY = callClanPc.getY();

        int heading = callClanPc.getCallClanHeading();
        locX += HEADING_TABLE_X[heading];
        locY += HEADING_TABLE_Y[heading];
        heading = (heading + 4) % 4;

        /*
         * final Random random = new Random(); locX += (random.nextInt(6) - 3);
         * locY += (random.nextInt(6) - 3);
         */

        boolean isExsistCharacter = false;
        for (final L1Object object : World.get().getVisibleObjects(callClanPc,
                1)) {
            if (object instanceof L1Character) {
                final L1Character cha = (L1Character) object;
                if ((cha.getX() == locX) && (cha.getY() == locY)
                        && (cha.getMapId() == mapId)) {
                    isExsistCharacter = true;
                    break;
                }
            }
        }

        if (((locX == 0) && (locY == 0)) || !map.isPassable(locX, locY, null)
                || isExsistCharacter) {
            // 627 因你要去的地方有障碍物以致于无法直接传送到该处。
            pc.sendPackets(new S_ServerMessage(627));
            return;
        }
        L1Teleport.teleport(pc, locX, locY, mapId, heading, true,
                L1Teleport.CALL_CLAN);
    }
    

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }

    private class KickPc implements Runnable {

        private ClientExecutor _client;

        private KickPc(L1PcInstance pc) {
            _client = pc.getNetConnection();
        }

        private void start_cmd() {
            GeneralThreadPool.get().execute(this);
        }

        @Override
        public void run() {
            try {
                Thread.sleep(5000);
                _client.kick();

            } catch (InterruptedException e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
