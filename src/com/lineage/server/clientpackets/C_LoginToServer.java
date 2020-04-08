package com.lineage.server.clientpackets;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigOther;
import com.lineage.data.event.OnlineGiftSet;
import com.lineage.data.event.castle_warOnlineGiftSet;
import com.lineage.data.npc.Npc_clan;
import com.lineage.echo.ClientExecutor;
import com.lineage.list.OnlineUser;
import com.lineage.server.ActionCodes;
import com.lineage.server.datatables.GetBackRestartTable;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.lock.CharBookReading;
import com.lineage.server.datatables.lock.CharBuffReading;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.datatables.lock.CharacterConfigReading;
import com.lineage.server.datatables.lock.ClanEmblemReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1ClanMatching;
import com.lineage.server.model.L1War;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.serverpackets.S_AddSkill;
import com.lineage.server.serverpackets.S_BookMarkLogin;
import com.lineage.server.serverpackets.S_CastleMaster;
import com.lineage.server.serverpackets.S_CharResetInfo;
import com.lineage.server.serverpackets.S_Emblem;
import com.lineage.server.serverpackets.S_EnterGame;
import com.lineage.server.serverpackets.S_EquipmentWindow;
import com.lineage.server.serverpackets.S_InvList;
import com.lineage.server.serverpackets.S_MapID;
import com.lineage.server.serverpackets.S_NewMaster;
import com.lineage.server.serverpackets.S_OtherCharPacks;
import com.lineage.server.serverpackets.S_OwnCharPack;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_PacketBoxConfig;
import com.lineage.server.serverpackets.S_PacketBoxIcon1;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_War;
import com.lineage.server.serverpackets.S_Weather;
import com.lineage.server.templates.L1BookMark;
import com.lineage.server.templates.L1Config;
import com.lineage.server.templates.L1EmblemIcon;
import com.lineage.server.templates.L1GetBackRestart;
import com.lineage.server.templates.L1PcOtherList;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.templates.L1UserSkillTmp;
import com.lineage.server.timecontroller.server.ServerUseMapTimer;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import com.lineage.server.world.WorldSummons;
import com.lineage.server.world.WorldWar;

/**
 * 要求进入游戏
 * 
 * @author daien
 * 
 */
public class C_LoginToServer extends ClientBasePacket {
	private static final Random _random = new Random();
    private static final Log _log = LogFactory.getLog(C_LoginToServer.class);
	private static List<String> accountsWithIcons = new ArrayList<String>();//更新盟辉hjx1000
    /*
     * public C_LoginToServer() { }
     * 
     * public C_LoginToServer(final byte[] abyte0, final ClientExecutor client)
     * { super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public synchronized void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final String loginName = client.getAccountName();
            // System.out.println("帐号: " + login);
            // System.out.println("人物名称: " + charName);

            if (client.getActiveChar() != null) {
                _log.error("帐号重复登入人物: " + loginName + "强制中断连线");
                client.kick();
                return;
            }
            /*
            //限制账号改写 hjx1000 
            final StringBuilder macStr = client.getMac();
            int mac_cou = 0;
			for (final ClientExecutor cl: OnlineUser.get().all()) {
				if (cl.getMac().toString().equalsIgnoreCase(macStr.toString())) {
					mac_cou++;
				}
			}
			if (mac_cou > 2) {
				client.kick();
				return;
			}
		   */
			//限制账号改写 hjx1000 end
            final String charName = this.readS();

            final L1PcInstance pc = L1PcInstance.load(charName);

            if ((pc == null) || !loginName.equals(pc.getAccountName())) {
                _log.info("无效登入要求: " + charName + " 帐号(" + loginName + ", "
                        + client.getIp() + ")");
                client.kick();
                return;
            }

            /*
             * if (Config.LEVEL_DOWN_RANGE != 0) { if (pc.getHighLevel() -
             * pc.getLevel() >= Config.LEVEL_DOWN_RANGE) {
             * _log.info("超出人物可创范围: " + charName + " 帐号(" + loginName + ", " +
             * client.getIp() + ")"); client.kick(); return; } }
             */

            _log.info("登入游戏: " + charName + " 帐号(" + loginName + ", "
                    + client.getIp() + ")");

            pc.setNetConnection(client);// 登记封包接收组
            pc.setPacketOutput(client.out());// 登记封包发送组

            final int currentHpAtLoad = pc.getCurrentHp();
            final int currentMpAtLoad = pc.getCurrentMp();

            // 重置错误次数
            client.set_error(0);

            pc.clearSkillMastery();// 清除技能资讯
            pc.setOnlineStatus(1);// 设定连线状态

            CharacterTable.updateOnlineStatus(pc);
            World.get().storeObject(pc);

            pc.setNetConnection(client);// 登记封包接收组
            pc.setPacketOutput(client.out());// 登记封包发送组
            client.setActiveChar(pc);// 登记玩家资料

            this.getOther(pc);// 额外纪录资料
            

            // 宣告进入游戏
            pc.sendPackets(new S_EnterGame());

            // 读取角色道具
            this.items(pc);

            // 取得记忆座标资料
            bookmarks(pc);

            // 判断座标资料
            this.backRestart(pc);

            // 取得游戏焦点
            this.getFocus(pc);

            pc.sendVisualEffectAtLogin();

            this.skills(pc);// 取得角色魔法技能资料

            pc.turnOnOffLight();

            if (pc.getCurrentHp() > 0) {
                pc.setDead(false);
                pc.setStatus(0);

            } else {
                pc.setDead(true);
                pc.setStatus(ActionCodes.ACTION_Die);
            }

            // 取回快速键纪录
            final L1Config config = CharacterConfigReading.get()
                    .get(pc.getId());
            if (config != null) {
                pc.sendPackets(new S_PacketBoxConfig(config));
            }

            this.serchSummon(pc);// 取得残留宠物资讯
            
            updateIcons(pc);//登陆时更新血盟的盟辉 hjx1000

            ServerWarExecutor.get().checkCastleWar(pc);// 检查城堡战争状态

            this.war(pc);// 战争状态

            this.marriage(pc);// 取得婚姻资料

            if (currentHpAtLoad > pc.getCurrentHp()) {
                pc.setCurrentHp(currentHpAtLoad);
            }
            if (currentMpAtLoad > pc.getCurrentMp()) {
                pc.setCurrentMp(currentMpAtLoad);
            }

            this.buff(pc);// 取得物品与魔法特殊效果

            pc.startHpRegeneration();
            pc.startMpRegeneration();

            pc.startObjectAutoUpdate();// PC 可见物更新处理

            this.crown(pc);// 送出王冠资料

            pc.save(); // 资料回存

            if (pc.getHellTime() > 0) {
                pc.beginHell(false);
            }
            // 送出人物属性资料
            pc.sendPackets(new S_CharResetInfo(pc));

            // 载入人物任务资料
            pc.getQuest().load();

            // 送出展示视窗
            pc.showWindows();

            if (pc.get_food() >= 225) {// LOLI 生存呐喊
                final Calendar cal = Calendar.getInstance();
                long h_time = cal.getTimeInMillis() / 1000;// 换算为秒
                pc.set_h_time(h_time);// 纪录登入时间
            }

//            if (pc.getLevel() <= 20) {// LOLI 战斗特化
//                pc.sendPackets(new S_PacketBoxProtection(
//                        S_PacketBoxProtection.ENCOUNTER, 1));
//            }

            pc.setLoginToServer(true); // 设置为登陆游戏世界状态

            // 如在奇岩地监内则开始计时
            final short map = pc.getMapId();
            if ((map == 53) || (map == 54) || (map == 55) || (map == 56)) {
                pc.startRocksPrison();
                // System.out.println("开始计时、登陆");
            }

//            if (!pc.hasSkillEffect(Card_Fee)) {//hjx1000
//            	final int card_cou = pc.getNetConnection().getAccount().get_card_fee() - 1;
//            	if (card_cou >= 0) {
//            		pc.getNetConnection().getAccount().set_card_fee(card_cou);
//                    AccountReading.get().updatecard_fee(pc.getAccountName(), card_cou);//扣一小时
//                    pc.setSkillEffect(Card_Fee, 60 * 1000);//一分钟
//            	} else {
//                	pc.sendPackets(new S_SystemMessage("点卡不足某些功能限制，请您及时冲值点卡。"));
//            	}
//            }
            if (ConfigOther.DETECATION){
            	final int rndtime = _random.nextInt(ConfigOther.RND_TIME)+1;
            	pc.setSkillEffect(7902,rndtime * 60000);//检测外挂状态 hjx1000
            } 
//            pc.sendPackets(new S_ServerMessage("\\aD欢迎进入怀旧天堂八区，目前正式开区"));
            //pc.sendPackets(new S_ServerMessage("\\aD欢迎光临倚剑天堂"));
            //pc.sendPackets(new S_ServerMessage("\\aD游戏自带辅助工具,请按两下 HOME 呼出"));
            //pc.sendPackets(new S_ServerMessage("\\aD管理QQ号码:3335336666"));
           // pc.sendPackets(new S_ServerMessage("\\aD游戏玩家交流QQ群号[675223561]询问"));
//            pc.sendPackets(new S_ServerMessage("\\fR怀旧天堂网址http://hjtiantang.com"));            
            ClanMatching(pc); //血盟UI hjx1000
            Clanclan(pc); //血盟UI hjx1000
//            if (pc.get_trade_items() != null) { //登陆时取回上次交易掉线的物品 hjx1000
//            	final L1Trade trade = new L1Trade();
//            	trade.RecoveryTrade(pc);
//            }
        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }
    
    /**
     * 登陆时更新盟辉 hjx1000
     */
	private void updateIcons(final L1PcInstance pc) {
		if (accountsWithIcons.contains(pc.getAccountName())) {
			return;
		}
		accountsWithIcons.add(pc.getAccountName());
		for (L1Clan clan : WorldClan.get().getAllClans()) {
			pc.sendPackets(new S_Emblem(clan.getClanId()));
		}
		System.out.println("盟辉更新");
	}

    /**
     * 送出王冠资料
     * 
     * @param pc
     */
    private void crown(final L1PcInstance pc) {
        try {
            final Map<Integer, L1Clan> map = L1CastleLocation.mapCastle();
            for (Integer key : map.keySet()) {
                final L1Clan clan = map.get(key);
                if (clan != null) {
                    if (key.equals(2)) {
                        pc.sendPackets(new S_CastleMaster(8, clan.getLeaderId()));

                    } else {
                        pc.sendPackets(new S_CastleMaster(key, clan
                                .getLeaderId()));
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 取得焦点
     * 
     * @param pc
     */
    private void getFocus(L1PcInstance pc) {
        try {
            // 重置副本编号
            pc.set_showId(-1);

            // 将物件增加到MAP世界里
            World.get().addVisibleObject(pc);

            // 角色资讯
            pc.sendPackets(new S_OwnCharStatus(pc));

            // 更新角色所在的地图
            pc.sendPackets(new S_MapID(pc, pc.getMap().getBaseMapId(), pc.getMap()
                    .isUnderwater()));

            // 物件封包(本身)
            pc.sendPackets(new S_OwnCharPack(pc));

            final ArrayList<L1PcInstance> otherPc = World.get()
                    .getVisiblePlayer(pc);
            if (otherPc.size() > 0) {
                for (final L1PcInstance tg : otherPc) {
                    // 物件封包(其他人物)
                    tg.sendPackets(new S_OtherCharPacks(pc));
                }
            }

            // 更新魔攻与魔防
            pc.sendPackets(new S_SPMR(pc));

            // 闪避率更新 修正 thatmystyle (UID: 3602)
            pc.sendPackets(new S_PacketBoxIcon1(true, pc.get_dodge()));

            // 天气效果
            pc.sendPackets(new S_Weather(World.get().getWeather()));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 能力质选取资料
     * 
     * @param pc
     */
    /*
     * private void bonusstats(final L1PcInstance pc) { try { // 人物属性变更选取 if
     * ((pc.getLevel() >= 51) && (pc.getLevel() - 50 > pc.getBonusStats())) { if
     * ((pc.getBaseStr() + pc.getBaseDex() + pc.getBaseCon() + pc.getBaseInt() +
     * pc.getBaseWis() + pc.getBaseCha()) < (ConfigAlt.POWER * 6)) {
     * pc.sendPackets(new S_Bonusstats(pc.getId())); } }
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    /**
     * 取得婚姻资料
     * 
     * @param pc
     */
    private void marriage(final L1PcInstance pc) {
        try {
            if (pc.getPartnerId() != 0) { // 结婚中
                final L1PcInstance partner = (L1PcInstance) World.get()
                        .findObject(pc.getPartnerId());
                if ((partner != null) && (partner.getPartnerId() != 0)) {
                    if ((pc.getPartnerId() == partner.getId())
                            && (partner.getPartnerId() == pc.getId())) {
                        // 548 你的情人目前正在线上。
                        pc.sendPackets(new S_ServerMessage(548));
                        // 549 你的情人上线了!!
                        partner.sendPackets(new S_ServerMessage(549));
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 其它数据
     * 
     * @param pc
     * @throws Exception
     */
    private void getOther(final L1PcInstance pc) throws Exception {
        try {
            pc.set_otherList(new L1PcOtherList(pc));

            pc.addMaxHp(pc.get_other().get_addhp());
            pc.addMaxMp(pc.get_other().get_addmp());

            // 在线奖励
            OnlineGiftSet.add(pc);
            if (pc.isCrown()) {
                //城战奖励
                castle_warOnlineGiftSet.add(pc);
            }

            final int time = pc.get_other().get_usemapTime();

            if (time > 0) {
                ServerUseMapTimer.put(pc, time);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 取得血盟 与 血盟战争资料
     * 
     * @param pc
     */
    private void war(final L1PcInstance pc) {
        try {
            if (pc.getClanid() != 0) { // 血盟资料不为0
                final L1Clan clan = WorldClan.get().getClan(pc.getClanname());
                if (clan != null) {
                    // 判断血盟名称相等 雨 血盟编号相等
                    if ((pc.getClanid() == clan.getClanId())
                            && pc.getClanname().toLowerCase()
                                    .equals(clan.getClanName().toLowerCase())) {
                        final L1PcInstance[] clanMembers = clan
                                .getOnlineClanMember();
                        for (final L1PcInstance clanMember : clanMembers) {
                            if (clanMember.getId() != pc.getId()) {
                                // 843 血盟成员%0%s刚进入游戏。
                                clanMember.sendPackets(new S_ServerMessage(843,
                                        pc.getName()));
                            }
                        }

                        final int clanMan = clan.getOnlineClanMember().length;
                        pc.sendPackets(new S_ServerMessage("\\fU线上血盟成员:"
                                + clanMan));

                        if (clan.isClanskill()) {
                            switch (pc.get_other().get_clanskill()) {
                                case 1:// 狂暴
                                    pc.sendPackets(new S_ServerMessage(
                                            Npc_clan.SKILLINFO[0]));
                                    break;
                                case 2:// 寂静
                                    pc.sendPackets(new S_ServerMessage(
                                            Npc_clan.SKILLINFO[1]));
                                    break;
                                case 4:// 魔击
                                    pc.sendPackets(new S_ServerMessage(
                                            Npc_clan.SKILLINFO[2]));
                                    break;
                                case 8:// 消魔
                                    pc.sendPackets(new S_ServerMessage(
                                            Npc_clan.SKILLINFO[3]));
                                    break;
                            }
                        }

                        // 送出盟辉
                        final L1EmblemIcon emblemIcon = ClanEmblemReading.get()
                                .get(clan.getClanId());
                        if (emblemIcon != null) {
                            pc.sendPackets(new S_Emblem(emblemIcon));
                        }

                        // 目前全部战争资讯取得
                        for (final L1War war : WorldWar.get().getWarList()) {
                            final boolean ret = war.checkClanInWar(pc
                                    .getClanname());
                            if (ret) { // 是否正在战争中
                                final String enemy_clan_name = war
                                        .getEnemyClanName(pc.getClanname());
                                if (enemy_clan_name != null) {
                                    // \f1目前你的血盟与 %0 血盟交战当中。
                                    pc.sendPackets(new S_War(8, pc
                                            .getClanname(), enemy_clan_name));
                                }
                                break;
                            }
                        }
                    }

                } else {
                    pc.setClanid(0);
                    pc.setClanname("");
                    pc.setClanRank(0);
                    pc.save();
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 所在座标位置资料判断
     * 
     * @param pc
     */
    private void backRestart(final L1PcInstance pc) {
        try {

            // 指定MAP回村设置
            final L1GetBackRestart gbr = GetBackRestartTable.get()
                    .getGetBackRestart(pc.getMapId());
            if (gbr != null) {
                pc.setX(gbr.getLocX());
                pc.setY(gbr.getLocY());
                pc.setMap(gbr.getMapId());
            }

            // 战争区域回村设置
            final int castle_id = L1CastleLocation.getCastleIdByArea(pc);
            if (castle_id > 0) {
                if (ServerWarExecutor.get().isNowWar(castle_id)) {
                    final L1Clan clan = WorldClan.get().getClan(
                            pc.getClanname());
                    if (clan != null) {
                        if (clan.getCastleId() != castle_id) {
                            // 城主クランではない
                            int[] loc = new int[3];
                            loc = L1CastleLocation.getGetBackLoc(castle_id);
                            pc.setX(loc[0]);
                            pc.setY(loc[1]);
                            pc.setMap((short) loc[2]);
                        }

                    } else {
                        // クランに所属して居ない场合は归还
                        int[] loc = new int[3];
                        loc = L1CastleLocation.getGetBackLoc(castle_id);
                        pc.setX(loc[0]);
                        pc.setY(loc[1]);
                        pc.setMap((short) loc[2]);
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 取得物品资料
     * 
     * @param pc
     */
    private void items(final L1PcInstance pc) {
        try {
            // 背包物品封包传递
            CharacterTable.restoreInventory(pc);
            final List<L1ItemInstance> items = pc.getInventory().getItems();
            if (items.size() > 0) {
                pc.sendPackets(new S_InvList(pc.getInventory().getItems(), pc));
                equipmentWindow(pc, items); // 显示装备中的武器与防具
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 武器防具装备中的显示.
     */
    private void equipmentWindow(L1PcInstance pc, List<L1ItemInstance> items) {
        for (final L1ItemInstance item : items) {
            if (item.isEquipped()) {
                switch (item.getItem().getUseType()) {
                    case 1: // 武器
                        pc.sendPackets(new S_EquipmentWindow(item.getId(),
                                S_EquipmentWindow.EQUIPMENT_INDEX_WEAPON, true));
                        break;
                    case 2: // 盔甲
                        pc.sendPackets(new S_EquipmentWindow(item.getId(),
                                S_EquipmentWindow.EQUIPMENT_INDEX_ARMOR, true));
                        break;
                    case 18: // T恤
                        pc.sendPackets(new S_EquipmentWindow(item.getId(),
                                S_EquipmentWindow.EQUIPMENT_INDEX_T, true));
                        break;
                    case 19: // 斗篷
                        pc.sendPackets(new S_EquipmentWindow(item.getId(),
                                S_EquipmentWindow.EQUIPMENT_INDEX_CLOAK, true));
                        break;
                    case 20: // 手套
                        pc.sendPackets(new S_EquipmentWindow(item.getId(),
                                S_EquipmentWindow.EQUIPMENT_INDEX_GLOVE, true));
                        break;
                    case 21: // 长靴
                        pc.sendPackets(new S_EquipmentWindow(item.getId(),
                                S_EquipmentWindow.EQUIPMENT_INDEX_BOOTS, true));
                        break;
                    case 22: // 头盔
                        pc.sendPackets(new S_EquipmentWindow(item.getId(),
                                S_EquipmentWindow.EQUIPMENT_INDEX_HEML, true));
                        break;
                    case 23: // 戒指
                        // final int type = armor.getItem().getType();
                        // if (pc.getInventory().getTypeEquipped(2, 9) == 1) {
                        if (pc.getEquipmentRing1ID() == 0) {
                            pc.setEquipmentRing1ID(item.getId());
                            pc.sendPackets(new S_EquipmentWindow(pc
                                    .getEquipmentRing1ID(),
                                    S_EquipmentWindow.EQUIPMENT_INDEX_RING1,
                                    true));
                        } else if (pc.getEquipmentRing2ID() == 0
                                && item.getId() != pc.getEquipmentRing1ID()) {
                            pc.setEquipmentRing2ID(item.getId());
                            pc.sendPackets(new S_EquipmentWindow(pc
                                    .getEquipmentRing2ID(),
                                    S_EquipmentWindow.EQUIPMENT_INDEX_RING2,
                                    true));
                        } else if (pc.getEquipmentRing3ID() == 0
                                && item.getId() != pc.getEquipmentRing1ID()
                                && item.getId() != pc.getEquipmentRing2ID()) {
                            pc.setEquipmentRing3ID(item.getId());
                            pc.sendPackets(new S_EquipmentWindow(pc
                                    .getEquipmentRing3ID(),
                                    S_EquipmentWindow.EQUIPMENT_INDEX_RING3,
                                    true));
                        } else if (pc.getEquipmentRing4ID() == 0
                                && item.getId() != pc.getEquipmentRing1ID()
                                && item.getId() != pc.getEquipmentRing2ID()
                                && item.getId() != pc.getEquipmentRing3ID()) {
                            pc.setEquipmentRing4ID(item.getId());
                            pc.sendPackets(new S_EquipmentWindow(pc
                                    .getEquipmentRing4ID(),
                                    S_EquipmentWindow.EQUIPMENT_INDEX_RING4,
                                    true));
                        }
                        break;
                    case 24: // 项链
                        pc.sendPackets(new S_EquipmentWindow(item.getId(),
                                S_EquipmentWindow.EQUIPMENT_INDEX_NECKLACE,
                                true));
                        break;
                    case 25: // 盾牌
                        pc.sendPackets(new S_EquipmentWindow(item.getId(),
                                S_EquipmentWindow.EQUIPMENT_INDEX_SHIELD, true));
                        break;
                    case 37: // 腰带
                        pc.sendPackets(new S_EquipmentWindow(item.getId(),
                                S_EquipmentWindow.EQUIPMENT_INDEX_BELT, true));
                        break;
                    case 40: // 耳环
                        pc.sendPackets(new S_EquipmentWindow(item.getId(),
                                S_EquipmentWindow.EQUIPMENT_INDEX_EARRING, true));
                        break;
                    case 43: // 副助道具-右
                        pc.sendPackets(new S_EquipmentWindow(item.getId(),
                                S_EquipmentWindow.EQUIPMENT_INDEX_AMULET1, true));
                        break;
                    case 44: // 副助道具-左
                        pc.sendPackets(new S_EquipmentWindow(item.getId(),
                                S_EquipmentWindow.EQUIPMENT_INDEX_AMULET2, true));
                        break;
                    case 45: // 副助道具-中
                        pc.sendPackets(new S_EquipmentWindow(item.getId(),
                                S_EquipmentWindow.EQUIPMENT_INDEX_AMULET3, true));
                        break;
                    case 48: // 副助道具-右下
                        pc.sendPackets(new S_EquipmentWindow(item.getId(),
                                S_EquipmentWindow.EQUIPMENT_INDEX_AMULET4, true));
                        break;
                    case 47: // 副助道具-左下
                        pc.sendPackets(new S_EquipmentWindow(item.getId(),
                                S_EquipmentWindow.EQUIPMENT_INDEX_AMULET5, true));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 取得记忆座标资料
     * 
     * @param pc
     */
    private void bookmarks(final L1PcInstance pc) {
        try {
            final ArrayList<L1BookMark> bookList = CharBookReading.get()
                    .getBookMarks(pc);
            if (bookList != null) {
                if (bookList.size() > 0) {
                    pc.sendPackets(new S_BookMarkLogin(pc, true));
                    // for (final L1BookMark book : bookList) {
                    // pc.sendPackets(new S_Bookmarks(book.getName(), book
                    // .getMapId(), book.getLocX(), book.getLocY(),
                    // book.getId()));
                    // }
                }
            } else {
                pc.sendPackets(new S_BookMarkLogin(pc, false));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 取得人物技能清单
     * 
     * @param pc
     */
    private void skills(final L1PcInstance pc) {
        try {
            final ArrayList<L1UserSkillTmp> skillList = CharSkillReading.get()
                    .skills(pc.getId());

            final int[] skills = new int[28];

            if (skillList != null) {
                if (skillList.size() > 0) {
                    for (final L1UserSkillTmp userSkillTmp : skillList) {
                        // 取得魔法资料
                        final L1Skills skill = SkillsTable.get().getTemplate(
                                userSkillTmp.get_skill_id());
                        skills[(skill.getSkillLevel() - 1)] += skill.getId();
                        // _log.error("skill.getSkillLevel() - 1" +
                        // (skill.getSkillLevel() - 1) + " skill.getId():" +
                        // skill.getId());
                    }
                    // 送出资料
                    pc.sendPackets(new S_AddSkill(pc, skills));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 取得残留宠物资讯
     * 
     * @param pc
     */
    private void serchSummon(final L1PcInstance pc) {
        try {
            final Collection<L1SummonInstance> summons = WorldSummons.get()
                    .all();
            if (summons.size() > 0) {
                for (final L1SummonInstance summon : summons) {
                    if (summon.getMaster().getId() == pc.getId()) {
                        summon.setMaster(pc);
                        pc.addPet(summon);
                        S_NewMaster packet = new S_NewMaster(pc.getName(),
                                summon);
                        for (final L1PcInstance visiblePc : World.get()
                                .getVisiblePlayer(summon)) {
                            visiblePc.sendPackets(packet);
                        }
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 取得保留技能纪录
     * 
     * @param pc
     */
    private void buff(final L1PcInstance pc) {
        try {
            // 保留技能纪录
            CharBuffReading.get().buff(pc);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
    
	private void ClanMatching(L1PcInstance pc) { //血盟UI hjx1000
		L1ClanMatching cml = L1ClanMatching.getInstance();
		if (pc.getClanid() == 0) {
			if (!pc.isCrown()) {
				cml.loadClanMatchingApcList_User(pc);
			}
		} else {
			switch (pc.getClanRank()) {
				case 3:	case 10: case 9:
					cml.loadClanMatchingApcList_Crown(pc);
				break; 
			}
		}
	}

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
    
	private void Clanclan(L1PcInstance pc) { //血盟UI hjx1000
		//3245焙林狼 何撫: 趨竿俊 啊澇竅技夸//3246焙林狼 何撫: 趨盔闌 葛籠竅技夸
		//3247趨竿闌 芒汲竅絆 獎霸 舅府技夸//3248趨竿 啊澇 夸沒撈 吭嚼聰促
		L1Clan clan = WorldClan.get().getClan(pc.getClanname());//這句不知是否正確自己加的
		if (clan == null && pc.isCrown()) {
			pc.sendPackets(new S_ServerMessage(3247)); //趨竿闌 芒汲竅絆 獎霸 舅府技夸
		} else if (clan != null && pc.isCrown()) {
			pc.sendPackets(new S_ServerMessage(3246)); //趨竿盔闌 葛籠竅技夸
		} else if (clan == null && !pc.isCrown()){
			pc.sendPackets(new S_ServerMessage(3245)); //趨竿俊 啊澇竅技夸
		}
	}
}
