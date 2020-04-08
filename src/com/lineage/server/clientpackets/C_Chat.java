package com.lineage.server.clientpackets;

import static com.lineage.server.model.skill.L1SkillId.AREA_OF_SILENCE;
import static com.lineage.server.model.skill.L1SkillId.SILENCE;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CHAT_PROHIBITED;
import static com.lineage.server.model.skill.L1SkillId.STATUS_POISON_SILENCE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.config.Config;
import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigRecord;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.command.GMCommands;
import com.lineage.server.datatables.DropTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.lock.AccountReading;
import com.lineage.server.datatables.lock.LogChatReading;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_BlueMessage;
import com.lineage.server.serverpackets.S_CharTitle;
import com.lineage.server.serverpackets.S_Chat;
import com.lineage.server.serverpackets.S_ChatClan;
import com.lineage.server.serverpackets.S_ChatClanUnion;
import com.lineage.server.serverpackets.S_ChatParty;
import com.lineage.server.serverpackets.S_ChatParty2;
import com.lineage.server.serverpackets.S_ChatShouting;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.serverpackets.S_NpcChatShouting;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Drop;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;

/**
 * 要求使用一般聊天频道
 * 
 * @author daien
 * 
 */
public class C_Chat extends ClientBasePacket {
	private static final Random _random = new Random();
    private static final Log _log = LogFactory.getLog(C_Chat.class);

    /*
     * public C_Chat() { }
     * 
     * public C_Chat(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final L1PcInstance pc = client.getActiveChar();

            if (decrypt.length > 108) {
                _log.warn("人物:" + pc.getName() + "对话长度超过限制:"
                        + client.getIp().toString());
                client.set_error(client.get_error() + 1);
                return;
            }

            boolean isStop = false;// 停止输出

            boolean errMessage = false;// 异常讯息

            // 中毒状态
            if (pc.hasSkillEffect(SILENCE)) {
                if (!pc.isGm()) {
                    isStop = true;
                }
            }

            // 中毒状态
            if (pc.hasSkillEffect(AREA_OF_SILENCE)) {
                if (!pc.isGm()) {
                    isStop = true;
                }
            }

            // 中毒状态
            if (pc.hasSkillEffect(STATUS_POISON_SILENCE)) {
                if (!pc.isGm()) {
                    isStop = true;
                }
            }

            // 你从现在被禁止闲谈。
            if (pc.hasSkillEffect(STATUS_CHAT_PROHIBITED)) {
                isStop = true;
                errMessage = true;
            }

            if (isStop) {
                if (errMessage) {
                    pc.sendPackets(new S_ServerMessage(242));
                }
                return;
            }

            // 取回对话内容
            final int chatType = this.readC();
            final String chatText = this.readS();

            switch (chatType) {
                case 0:// 一般频道
                    if (pc.is_retitle()) {
                        re_title(pc, chatText.trim());
                        return;
                    }
                    if (pc.is_repass() != 0) {
                        re_repass(pc, chatText.trim());
                        return;
                    }
                    chatType_0(pc, chatText);
                    break;

                case 2: // 大叫频道(!)
                    chatType_2(pc, chatText);
                    break;

                case 4: // 血盟频道(@)
                    chatType_4(pc, chatText);
                    break;

                case 11: // 队伍频道(#)
                    chatType_11(pc, chatText);
                    break;

                case 13: // 连盟频道(%)
                    chatType_13(pc, chatText);
                    break;

                case 14: // 队伍频道(聊天)
                    chatType_14(pc, chatText);
                    break;
            }

            if (!pc.isGm()) {
                pc.checkChatInterval();
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    private static final String _check_pwd = "abcdefghijklmnopqrstuvwxyz0123456789!_=+-?.#";

    private void re_repass(L1PcInstance pc, String password) {
        try {
            switch (pc.is_repass()) {
                case 1:// 输入旧密码
                    if (!pc.getNetConnection().getAccount().get_password()
                            .equals(password)) {
                        // 1,744：密码错误
                        pc.sendPackets(new S_ServerMessage(1744));
                        return;
                    }
                    pc.repass(2);
                    pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "y_pass_01",
                            new String[] { "请输入您的新密码" }));
                    break;

                case 2:// 输入新密码
                    boolean iserr = false;
                    for (int i = 0; i < password.length(); i++) {
                        final String ch = password.substring(i, i + 1);
                        if (!_check_pwd.contains(ch.toLowerCase())) {
                            // 1,742：帐号或密码中有无效的字元
                            pc.sendPackets(new S_ServerMessage(1742));
                            iserr = true;
                            break;
                        }
                    }
                    if (password.length() > 13) {
                        // 1,742：帐号或密码中有无效的字元
                        pc.sendPackets(new S_ServerMessage(166, "密码长度过长"));
                        iserr = true;
                    }
                    if (password.length() < 3) {
                        // 1,742：帐号或密码中有无效的字元
                        pc.sendPackets(new S_ServerMessage(166, "密码长度过长"));
                        iserr = true;
                    }
                    if (iserr) {
                        return;
                    }
                    pc.setText(password);
                    pc.repass(3);
                    pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "y_pass_01",
                            new String[] { "请确认您的新密码" }));
                    break;

                case 3:// 确认新密码
                    if (!pc.getText().equals(password)) {
                        // 1,982：所输入的密码不一致.请重新输入.
                        pc.sendPackets(new S_ServerMessage(1982));
                        return;
                    }
                    if (!pc.getInventory().consumeItem(49538, 1)) {
                    	return;
                    }
                    pc.sendPackets(new S_CloseList(pc.getId()));
                    // 1,985：角色密码成功地变更.(忘记密码时请至天堂网站询问)
                    pc.sendPackets(new S_ServerMessage(1985));
                    AccountReading.get().updatePwd(pc.getAccountName(),
                            password);
                    pc.setText(null);
                    pc.repass(0);
                    break;
            }

        } catch (final Exception e) {
            pc.sendPackets(new S_CloseList(pc.getId()));
            // 45：未知的错误%d
            pc.sendPackets(new S_ServerMessage(45));
            pc.setText(null);
            pc.repass(0);
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 变更封号
     * 
     * @param pc
     * @param chatText
     */
    private void re_title(final L1PcInstance pc, final String chatText) {
        try {
            final String newchatText = chatText.trim();
            if (newchatText.isEmpty() || newchatText.length() <= 0) {
                pc.sendPackets(new S_ServerMessage("\\fU请输入封号内容"));
                return;
            }
            final int length = Config.LOGINS_TO_AUTOENTICATION ? 18 : 13;
            if (newchatText.getBytes().length > length) {
                pc.sendPackets(new S_ServerMessage("\\fU封号长度过长"));
                return;
            }
            final StringBuilder title = new StringBuilder();
            title.append(newchatText);

            pc.setTitle(title.toString());
            pc.sendPacketsAll(new S_CharTitle(pc.getId(), title));
            pc.save();
            pc.retitle(false);
            pc.sendPackets(new S_ServerMessage("\\fU封号变更完成"));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 队伍频道(聊天)
     * 
     * @param pc
     * @param chatText
     */
    private void chatType_14(final L1PcInstance pc, final String chatText) {
        if (pc.isInChatParty()) {
            S_ChatParty2 chatpacket = new S_ChatParty2(pc, chatText);
            final L1PcInstance[] partyMembers = pc.getChatParty().getMembers();
            for (final L1PcInstance listner : partyMembers) {
                if (!listner.getExcludingList().contains(pc.getName())) {
                    listner.sendPackets(chatpacket);
                }
            }

            if (ConfigRecord.LOGGING_CHAT_CHAT_PARTY) {
                LogChatReading.get().noTarget(pc, chatText, 14);
            }
        }
    }

    /**
     * 连盟频道(%)
     * 
     * @param pc
     * @param chatText
     */
    private void chatType_13(final L1PcInstance pc, final String chatText) {
        if (pc.getClanid() != 0) {
            final L1Clan clan = WorldClan.get().getClan(pc.getClanname());
            if (clan == null) {
                return;
            }
            switch (pc.getClanRank()) {
                case L1Clan.ALLIANCE_CLAN_RANK_GUARDIAN:// 6:守护骑士
                case L1Clan.NORMAL_CLAN_RANK_GUARDIAN:// 9:守护骑士
                case L1Clan.CLAN_RANK_GUARDIAN:// 3:副君主
                case L1Clan.CLAN_RANK_PRINCE:// 4:联盟君主
                case L1Clan.NORMAL_CLAN_RANK_PRINCE:// 10:联盟君主
                    final S_ChatClanUnion chatpacket = new S_ChatClanUnion(pc,
                            chatText);
                    final L1PcInstance[] clanMembers = clan
                            .getOnlineClanMember();
                    for (final L1PcInstance listner : clanMembers) {
                        if (!listner.getExcludingList().contains(pc.getName())) {
                            switch (listner.getClanRank()) {
                                case L1Clan.ALLIANCE_CLAN_RANK_GUARDIAN:// 6:守护骑士
                                case L1Clan.NORMAL_CLAN_RANK_GUARDIAN:// 9:守护骑士
                                case L1Clan.CLAN_RANK_GUARDIAN:// 3:副君主
                                case L1Clan.CLAN_RANK_PRINCE:// 4:联盟君主
                                case L1Clan.NORMAL_CLAN_RANK_PRINCE:// 10:联盟君主
                                    listner.sendPackets(chatpacket);
                                    break;
                            }
                        }
                    }

                    if (ConfigRecord.LOGGING_CHAT_COMBINED) {
                        LogChatReading.get().noTarget(pc, chatText, 13);
                    }
                    break;
            }
        }
    }

    /**
     * 队伍频道(#)
     * 
     * @param pc
     * @param chatText
     */
    private void chatType_11(final L1PcInstance pc, final String chatText) {
        if (pc.isInParty()) {
            S_ChatParty chatpacket = new S_ChatParty(pc, chatText);

            final ConcurrentHashMap<Integer, L1PcInstance> pcs = pc.getParty()
                    .partyUsers();

            if (pcs.isEmpty()) {
                return;
            }
            if (pcs.size() <= 0) {
                return;
            }

            for (final Iterator<L1PcInstance> iter = pcs.values().iterator(); iter
                    .hasNext();) {
                final L1PcInstance listner = iter.next();
                if (!listner.getExcludingList().contains(pc.getName())) {
                    if (listner.isShowPartyChat()) {
                        listner.sendPackets(chatpacket);
                    }
                }
            }

            if (ConfigRecord.LOGGING_CHAT_PARTY) {
                LogChatReading.get().noTarget(pc, chatText, 11);
            }
        }
    }

    /**
     * 血盟频道(@)
     * 
     * @param pc
     * @param chatText
     */
    private void chatType_4(final L1PcInstance pc, final String chatText) {
        if (pc.getClanid() != 0) {
            final L1Clan clan = WorldClan.get().getClan(pc.getClanname());
            if (clan != null) {
                final S_ChatClan chatpacket = new S_ChatClan(pc, chatText);
                final L1PcInstance[] clanMembers = clan.getOnlineClanMember();
                for (final L1PcInstance listner : clanMembers) {
                    if (!listner.getExcludingList().contains(pc.getName())) {
                        if (listner.isShowClanChat()) {
                            listner.sendPackets(chatpacket);
                        }
                    }
                }

                if (ConfigRecord.LOGGING_CHAT_CLAN) {
                    LogChatReading.get().noTarget(pc, chatText, 4);
                }
            }
        }
    }

    /**
     * 大叫频道(!)
     * 
     * @param pc
     * @param chatText
     */
    private void chatType_2(final L1PcInstance pc, final String chatText) {
        if (pc.isGhost()) {
            return;
        }
        S_ChatShouting chatpacket = new S_ChatShouting(pc, chatText);
        pc.sendPackets(chatpacket);
        for (final L1PcInstance listner : World.get().getVisiblePlayer(pc, 50)) {
            if (!listner.getExcludingList().contains(pc.getName())) {
                // 副本ID相等
                if (pc.get_showId() == listner.get_showId()) {
                    listner.sendPackets(chatpacket);
                }
            }
        }

        if (ConfigRecord.LOGGING_CHAT_SHOUT) {
            LogChatReading.get().noTarget(pc, chatText, 2);
        }
        // 变形怪重复对话
        this.doppelShouting(pc, chatText);
    }

    /**
     * 一般频道
     * 
     * @param pc
     * @param chatText
     */
    private void chatType_0(final L1PcInstance pc, final String chatText) {
        if (pc.isGhost() && !(pc.isGm() || pc.isMonitor())) {
            return;
        }
//        if (pc.getAccessLevel() > 0) {
            // GM命令
            if (chatText.startsWith(".")) {
                final String cmd = chatText.substring(1);
                GMCommands.getInstance().handleCommands(pc, cmd);
                return;
            }
//        }
        
        if (chatText.startsWith("倚剑充值卡 ")) {
        	final String cardid = chatText.substring(6);
        	actionRecharge(pc, cardid);
        	return;
        }
        
        if (chatText.startsWith("物品掉落 ")) {
        	final String sitem = chatText.substring(5);

            // 物品编号
            int itemid = 0;
            try {
                itemid = Integer.parseInt(sitem);

            } catch (final NumberFormatException e) {
                itemid = ItemTable.get().findItemIdByNameWithoutSpace(sitem);
                if (itemid == 0) {
                    pc.sendPackets(new S_SystemMessage("没有找到条件吻合的物品。"));
                    return;
                }
            }
            Connection con = null;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            try {
                con = DatabaseFactory.get().getConnection();
                pstm = con.prepareStatement("SELECT * FROM `droplist` WHERE itemId=?");
                pstm.setInt(1, itemid);
                rs = pstm.executeQuery();
                while (rs.next()) {
                	int chance = rs.getInt("chance");
                	final String npc_name = rs.getString("mobname");
                	final int npcid = rs.getInt("mobId");
                	if (itemid == 40466) {//修改龙之心的掉落显示为0.01
                		chance = 100;
                	}
                	if (chance > 0) {
                		final double chance1 = chance / 10000.0D;
                		pc.sendPackets(new S_SystemMessage("\\aD怪物:" + npc_name  + "[" + npcid + "]" + "机率:" + chance1 + "%"));
                	}
                }
            } catch (final SQLException e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(rs);
                SQLUtil.close(pstm);
                SQLUtil.close(con);
            }
        	return;
        }
        if (chatText.startsWith("怪物掉落 ")) {
        	final String smob = chatText.substring(5);

        	int mobID = this.parseNpcId(smob);
        	//System.out.println(mobID);
            Connection con = null;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            try {            	
                con = DatabaseFactory.get().getConnection();
                pstm = con.prepareStatement("SELECT gfxid FROM npc WHERE npcid=?");
                pstm.setInt(1, mobID);
                rs = pstm.executeQuery();
                rs.next();
                int gfxID = rs.getInt("gfxid");
                rs.close();
                pstm.close();
                con.close();
                final ArrayList<L1Drop> mobDrops = DropTable.get().getDrops(mobID);
                L1Npc theMob = NpcTable.get().getTemplate(mobID);
                pc.sendPackets(new S_SystemMessage(theMob.get_name() + "(" + mobID + ") | gfxID: " + gfxID + " | 掉落查询:"));
                for (L1Drop drop : mobDrops) {
                	L1Item item = ItemTable.get().getTemplate(drop.getItemid());
                	String blessed;
                	if (item.getBless() == 1) {
                		blessed = "";
                	} else if (item.getBless() == 0) {
                		blessed = "\\fR";
                	} else {
                		blessed = "\\fY";
                	}
                	double chance = drop.getChance() / 10000.0D;
                	if (drop.getItemid() == 40466) { //修改龙之心的掉落显示为0.01
                		chance = 0.01;
                	}
                	pc.sendPackets(new S_SystemMessage(blessed  + "物品:" + item.getName() + " [" + drop.getItemid() + "]机率:" + chance + "%"));
                }
            } catch (Exception e) {
               // _log.error(e.getLocalizedMessage(), e);
                pc.sendPackets(new S_SystemMessage("请输入 ." + "[NPC名字]或 " + "[NPC编号]。"));

            } finally {
                SQLUtil.close(rs);
                SQLUtil.close(pstm);
                SQLUtil.close(con);
            }
        	//System.out.println(smob);
        	return;
        }

        if (pc.hasSkillEffect(7903)) {
        	if (chatText.startsWith(String.valueOf(pc.getCheck_number()))) {//检测外挂状态 hjx1000
        		final int rndtime = _random.nextInt(ConfigOther.RND_TIME)+1;
        		final int count = pc.getCheck_number() * 10;
        		final L1ItemInstance item = ItemTable.get().createItem(40308);
        		pc.sendPackets(new S_BlueMessage(166,"\\f2恭喜你答对了."));
        		pc.killSkillEffectTimer(7903);
        		pc.killSkillEffectTimer(87);//取消冲击之晕状态
        		//pc.setCheck_cou(0);
        		pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, false));
        		pc.setSkillEffect(7902,rndtime * 60000);
        		item.setCount(count);
        		if (pc.getInventory().checkAddItem(item, 1) == 0) {
        			pc.getInventory().storeItem(item);
        			pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
        		}
        	} else {
        		pc.sendPackets(new S_BlueMessage(166,"\\f3答错了.请重新输入."));
        	}
        	return;
        }
        // 产生封包
        S_Chat chatpacket = new S_Chat(pc, chatText);
        pc.sendPackets(chatpacket);

        for (final L1PcInstance listner : World.get().getRecognizePlayer(pc)) {
            if (!listner.getExcludingList().contains(pc.getName())) {
                // 副本ID相等
                if (pc.get_showId() == listner.get_showId()) {
                    listner.sendPackets(chatpacket);
                }
            }
        }

        // 对话纪录
        if (ConfigRecord.LOGGING_CHAT_NORMAL) {
            LogChatReading.get().noTarget(pc, chatText, 0);
        }
        // 变形怪重复对话
        doppelGenerally(pc, chatText);
    }

    /**
     * 变形怪重复对话(一般频道)
     * 
     * @param pc
     * @param chatType
     * @param chatText
     */
    private void doppelGenerally(final L1PcInstance pc, final String chatText) {
        // 变形怪重复对话
        for (final L1Object obj : pc.getKnownObjects()) {
            if (obj instanceof L1MonsterInstance) {
                final L1MonsterInstance mob = (L1MonsterInstance) obj;
                if (mob.getNpcTemplate().is_doppel()
                        && mob.getName().equals(pc.getName())) {
                    mob.broadcastPacketX8(new S_NpcChat(mob, chatText));
                }
            }
        }
    }

    /**
     * 变形怪重复对话(大喊频道)
     * 
     * @param pc
     * @param chatType
     * @param chatText
     */
    private void doppelShouting(final L1PcInstance pc, final String chatText) {
        // 变形怪重复对话
        for (final L1Object obj : pc.getKnownObjects()) {
            if (obj instanceof L1MonsterInstance) {
                final L1MonsterInstance mob = (L1MonsterInstance) obj;
                if (mob.getNpcTemplate().is_doppel()
                        && mob.getName().equals(pc.getName())) {
                    mob.broadcastPacketX8(new S_NpcChatShouting(mob, chatText));
                }
            }
        }
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
    /**
     * 取回NPCID
     * 
     * @param nameId
     * @return
     */
    private int parseNpcId(final String nameId) {
        int npcid = 0;
        try {
            // 依照ID取回
            npcid = Integer.parseInt(nameId);

        } catch (final NumberFormatException e) {
            // 依照名称取回
            npcid = NpcTable.get().findNpcIdByNameWithoutSpace(nameId);
        }
        return npcid;
    }
    
    /**
     * 执行充值
     * @param pc
     * @param usercarid
     */
    private synchronized void actionRecharge(final L1PcInstance pc, final String usercarid) {
    	String user_carid1 = null;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `card_table` WHERE user_carid=?");
            pstm.setString(1, usercarid);
            rs = pstm.executeQuery();
            while (rs.next()) {
            	user_carid1 = rs.getString("user_carid");
            	final int isuser = rs.getInt("is_used");
            	if (isuser == 0) {
            		int count = 0;
            		if (user_carid1.length() == 13) {
            			count = 1;
            			CreateNewItem.createNewItem(pc, 58030, 1);
            		} else if (user_carid1.length() == 14) {
            			count = 10;
            			CreateNewItem.createNewItem(pc, 44070, 10);
            			CreateNewItem.createNewItem(pc, 44071, 1);
            		} else if (user_carid1.length() == 15) {
            			count = 50;
            			CreateNewItem.createNewItem(pc, 44070, 50);
            			CreateNewItem.createNewItem(pc, 44071, 5);
            		} else if (user_carid1.length() == 16) {
            			count = 100;
            			CreateNewItem.createNewItem(pc, 44070, 100);
            			CreateNewItem.createNewItem(pc, 44071, 10);
            		}
            		
            		final String accname = pc.getAccountName();
                    final Timestamp lastactive = new Timestamp(
                            System.currentTimeMillis());
                    Connection con1 = null;
                    PreparedStatement pstm1 = null;
                    try {
                        con1 = DatabaseFactory.get().getConnection();
                        final String sqlstr = "UPDATE `card_table` SET `is_used`=?,`user_time`=?,`account_name`=?, `get_adena`=? WHERE `user_carid`=?";
                        pstm1 = con1.prepareStatement(sqlstr);
                        pstm1.setInt(1, 1);
                        pstm1.setTimestamp(2, lastactive);
                        pstm1.setString(3, accname);
                        pstm1.setInt(4, count);
                        pstm1.setString(5, usercarid);
                        pstm1.execute();
                    } catch (final Exception e) {
                        _log.error(e.getLocalizedMessage(), e);

                    } finally {
                        SQLUtil.close(pstm1);
                        SQLUtil.close(con1);
                    }
            	} else {
            		pc.sendPackets(new S_SystemMessage("这张卡已经使用,请核准您的冲值卡号。"));
            	}
            }
        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        if (user_carid1 == null) {
        	pc.sendPackets(new S_SystemMessage("因为您输入了错误冲值卡号，系统将你踢下线。"));
        	pc.getNetConnection().kick();// 中断
        }
    }
}
