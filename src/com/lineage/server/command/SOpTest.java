package com.lineage.server.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.S_AddSkill;
import com.lineage.server.serverpackets.S_BlueMessage;
import com.lineage.server.serverpackets.S_Board;
import com.lineage.server.serverpackets.S_BoardRead;
import com.lineage.server.serverpackets.S_Bonusstats;
import com.lineage.server.serverpackets.S_CastleMaster;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_ChangeName;
import com.lineage.server.serverpackets.S_ChangeShape;
import com.lineage.server.serverpackets.S_CharReset;
import com.lineage.server.serverpackets.S_CharTitle;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_Chat;
import com.lineage.server.serverpackets.S_ChatGlobal;
import com.lineage.server.serverpackets.S_ChatOut;
import com.lineage.server.serverpackets.S_ChatWhisperFrom;
import com.lineage.server.serverpackets.S_CurseBlind;
import com.lineage.server.serverpackets.S_DelSkill;
import com.lineage.server.serverpackets.S_Deposit;
import com.lineage.server.serverpackets.S_Dexup;
import com.lineage.server.serverpackets.S_Disconnect;
import com.lineage.server.serverpackets.S_DoActionShop;
import com.lineage.server.serverpackets.S_Drawal;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.serverpackets.S_Exp;
import com.lineage.server.serverpackets.S_FixWeaponList;
import com.lineage.server.serverpackets.S_HPMeter;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_HireSoldier;
import com.lineage.server.serverpackets.S_IdentifyDesc;
import com.lineage.server.serverpackets.S_Invis;
import com.lineage.server.serverpackets.S_ItemCount;
import com.lineage.server.serverpackets.S_ItemError;
import com.lineage.server.serverpackets.S_Lawful;
import com.lineage.server.serverpackets.S_Light;
import com.lineage.server.serverpackets.S_Liquor;
import com.lineage.server.serverpackets.S_MPUpdate;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_NewMaster;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_OwnCharStatus2;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_PinkName;
import com.lineage.server.serverpackets.S_Poison;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_Resurrection;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_SelectTarget;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.serverpackets.S_SkillIconBlessOfEva;
import com.lineage.server.serverpackets.S_SkillIconShield;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_Strup;
import com.lineage.server.serverpackets.S_TaxRate;
import com.lineage.server.serverpackets.S_Trade;
import com.lineage.server.serverpackets.S_TradeAddItem;
import com.lineage.server.serverpackets.S_TradeStatus;
import com.lineage.server.serverpackets.S_TrueTarget;
import com.lineage.server.serverpackets.S_War;
import com.lineage.server.serverpackets.S_WarTime;
import com.lineage.server.serverpackets.S_Weather;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.world.World;

/**
 * 测试已知封包正确性
 * 
 * @author DaiEn
 * 
 */
public class SOpTest extends OpcodesServer {

    private static final Log _log = LogFactory.getLog(SOpTest.class);

    /**
     * 皇冠测试
     * 
     * @param pc
     * @throws Exception
     */
    public static void testOpA(final L1PcInstance pc, final int opid)
            throws Exception {
        try {
            World.get().broadcastPacketToAll(
                    new S_CastleMaster(opid, pc.getId()));
            System.out.println(opid + "/ " + pc.getId());
            // final Map<Integer, L1Clan> map = L1CastleLocation.mapCastle();
            // System.out.println(map.size());
            /*
             * for (int i = 1 ; i < 9 ; i++) { if (i != 2) {
             * World.get().broadcastPacketToAll(new S_CastleMaster(i,
             * pc.getId())); System.out.println(i + "/ " +pc.getId());
             * Thread.sleep(5000);
             * 
             * System.out.println(i + "/ 移除"); pc.sendPackets(new
             * S_CastleMaster(0, pc.getId())); Thread.sleep(5000); } }//
             */

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 测试已知封包正确性<BR>
     * 封包不正确者 预先注解掉
     * 
     * @param pc
     * @param opid
     * @throws Exception
     */
    public static void testOp(final L1PcInstance pc, final int opid)
            throws Exception {
        /* 初始化 */
        if (opid == S_OPCODE_INITOPCODE) {
            // 免测试

            /* 伺服器版本 */
        } else if (opid == S_OPCODE_SERVERVERSION) {
            // 免测试

            /* 登入状态 */
        } else if (opid == S_OPCODE_LOGINRESULT) {
            // 免测试

            /* 宣告进入游戏 */
        } else if (opid == S_OPCODE_UNKNOWN1) {
            // 免测试

            /* 公告视窗 */
        } else if (opid == S_OPCODE_COMMONNEWS) {
            // 免测试

            /* 封包盒子 */
        } else if (opid == S_OPCODE_PACKETBOX) {
            // GM管理选单 移动至指定人物
            // pc.sendPackets(new S_PacketBoxGm(pc, 1));
            pc.sendPackets(new S_PacketBox(S_PacketBox.DOLL, 1800));
            // pc.sendPackets(new S_SkillIconAura(3819, 600));

            /*
             * for (int i = 0 ; i < 128 ; i++) { Thread.sleep(1000);
             * pc.sendPackets(new S_PacketBox(i, 600)); }
             */

            /* 立即中断连线 */
        } else if (opid == S_OPCODE_DISCONNECT) {
            pc.sendPackets(new S_Disconnect());

            /* 广播频道 */
        } else if (opid == S_OPCODE_GLOBALCHAT) {
            // 广播频道测试
            pc.sendPackets(new S_ChatGlobal(pc, "广播频道测试"));

            /* 一般频道 */
        } else if (opid == S_OPCODE_NORMALCHAT) {
            // 一般频道测试
            pc.sendPackets(new S_Chat(pc, "一般频道测试"));

            /* 使用密语聊天频道 */
        } else if (opid == S_OPCODE_WHISPERCHAT) { // XXX
            // 密语交谈(接收)频道测试
            pc.sendPackets(new S_ChatWhisperFrom(pc, "密语交谈(接收)频道测试"));

            /* NPC 对话(文字对话) */
        } else if (opid == S_OPCODE_NPCSHOUT) {
            // 取回认识物件清单
            for (final L1Object obj : pc.getKnownObjects()) {
                if (obj instanceof L1NpcInstance) {
                    L1NpcInstance npc = (L1NpcInstance) obj;
                    npc.broadcastPacketX8(new S_NpcChat(npc, pc.getName()
                            + " 打3小~~"));
                }
            }

            /* 物件新增主人 */
        } else if (opid == S_OPCODE_NEWMASTER) { // XXX
            // 取回认识物件清单
            for (final L1Object obj : pc.getKnownObjects()) {
                if (obj instanceof L1NpcInstance) {
                    L1NpcInstance npc = (L1NpcInstance) obj;
                    npc.broadcastPacketX8(new S_NewMaster(pc.getName(), npc));
                }
            }

            /* 角色列表 */
        } else if (opid == S_OPCODE_CHARAMOUNT) {
            // 免测试

            /* 角色列表资讯 */
        } else if (opid == S_OPCODE_CHARLIST) {
            // 免测试

            /* 角色创造结果 */
        } else if (opid == S_OPCODE_NEWCHARWRONG) {
            // 免测试

            /* 创造角色(新创) */
        } else if (opid == S_OPCODE_NEWCHARPACK) {
            // 免测试

            /* 角色移除(立即/非立即) */
        } else if (opid == S_OPCODE_DETELECHAROK) {
            // 免测试

            /* 角色资讯 */
        } else if (opid == S_OPCODE_OWNCHARSTATUS) {
            // 测试角色资讯 - 力量101
            pc.sendPackets(new S_OwnCharStatus(pc, 101));

            /* 角色状态 */
        } else if (opid == S_OPCODE_OWNCHARSTATUS2) {
            // 测试角色状态 - 力量103
            pc.sendPackets(new S_OwnCharStatus2(pc, 103));

            /* 角色盟徽 */
        } else if (opid == S_OPCODE_EMBLEM) {
            // 免测试

            /* 角色封号 */
        } else if (opid == S_OPCODE_CHARTITLE) {
            final StringBuilder title = new StringBuilder();
            title.append("\\f=测试角色封号");
            pc.sendPackets(new S_CharTitle(pc.getId(), title));

            /* 角色名称变紫色 */
        } else if (opid == S_OPCODE_PINKNAME) {
            for (final L1Object obj : pc.getKnownObjects()) {
                if (obj instanceof L1NpcInstance) {
                    L1NpcInstance npc = (L1NpcInstance) obj;
                    npc.broadcastPacketX8(new S_PinkName(npc.getId(), 1000));
                }
            }
            pc.sendPacketsAll(new S_PinkName(pc.getId(), 1000));

            /* 角色皇冠 */
        } else if (opid == S_OPCODE_CASTLEMASTER) {
            // 5 海音城
            pc.sendPackets(new S_CastleMaster(5, pc.getId()));

            /* 角色重置升级能力 */
        } else if (opid == S_OPCODE_CHARRESET) {
            pc.sendPackets(new S_CharReset(pc));

            /* 物件封包 */
        } else if (opid == S_OPCODE_CHARPACK) {
            // 免测试

            /* 物件删除 */
        } else if (opid == S_OPCODE_REMOVE_OBJECT) {
            for (final L1Object obj : pc.getKnownObjects()) {
                if (obj instanceof L1NpcInstance) {
                    L1NpcInstance npc = (L1NpcInstance) obj;
                    pc.sendPackets(new S_RemoveObject(npc));
                }
            }

            /* 物件血条 */
        } else if (opid == S_OPCODE_HPMETER) {
            for (final L1Object obj : pc.getKnownObjects()) {
                if (obj instanceof L1NpcInstance) {
                    L1NpcInstance npc = (L1NpcInstance) obj;
                    pc.sendPackets(new S_HPMeter(npc.getId(), 100
                            * npc.getCurrentHp() / npc.getMaxHp()));
                }
            }

            /* 物件属性(门) */
        } else if (opid == S_OPCODE_ATTRIBUTE) {
            // 免测试

            /* 物品增加(背包) */
        } else if (opid == S_OPCODE_ADDITEM) {
            // 免测试

            /* 物品删除(背包) */
        } else if (opid == S_OPCODE_DELETEINVENTORYITEM) {
            // 免测试

            /* 物品色彩状态(背包) */
        } else if (opid == S_OPCODE_ITEMCOLOR) {
            // 免测试

            /* 物件复活 */
        } else if (opid == S_OPCODE_RESURRECTION) {
            // 预先死亡人物
            pc.sendPacketsX8(new S_SkillSound(pc.getId(), '\346'));
            pc.resurrect(pc.getMaxHp());
            pc.setCurrentHp(pc.getMaxHp());
            pc.startHpRegeneration();
            pc.startMpRegeneration();
            pc.stopPcDeleteTimer();
            pc.sendPacketsAll(new S_Resurrection(pc, pc, 0));

            pc.sendPacketsAll(new S_CharVisualUpdate(pc));
            if ((pc.getExpRes() == 1) && pc.isGres() && pc.isGresValid()) {
                pc.resExp();
                pc.setExpRes(0);
                pc.setGres(false);
            }

            /* 物件移动 */
        } else if (opid == S_OPCODE_MOVEOBJECT) {
            // 免测试

            /* 物件攻击 */
        } else if (opid == S_OPCODE_ATTACKPACKET) {
            // 免测试

            /* 物件动作种类(长时间) */
        } else if (opid == S_OPCODE_CHARVISUALUPDATE) {
            // 免测试

            /* 物件动作种类(短时间) */
        } else if (opid == S_OPCODE_DOACTIONGFX) {
            pc.sendPackets(new S_DoActionShop(pc.getId(), "物件动作种类(短时间)"));

            /* 产生动画(物件) */
        } else if (opid == S_OPCODE_SKILLSOUNDGFX) {
            // 闪光
            // pc.sendPackets(new S_SkillSound(pc.getId(), 198));
            pc.sendPackets(new S_SkillSound(pc.getId(), 3819));
            for (int i = 7013; i < 7100; i++) {
                System.out.println("i: " + i);
                pc.sendPackets(new S_SkillSound(pc.getId(), i, 150));
                Thread.sleep(500);
            }

            /* 产生动画(地点) */
        } else if (opid == S_OPCODE_EFFECTLOCATION) {
            // (电)组合动画效果
            int x = pc.getX();
            int y = pc.getY();
            int mapId = pc.getMapId();

            pc.sendPackets(new S_EffectLocation(new L1Location(x - 2, y - 2,
                    mapId), 4842));
            pc.sendPackets(new S_EffectLocation(new L1Location(x + 2, y - 2,
                    mapId), 4842));
            pc.sendPackets(new S_EffectLocation(new L1Location(x + 2, y + 2,
                    mapId), 4842));
            pc.sendPackets(new S_EffectLocation(new L1Location(x - 2, y + 2,
                    mapId), 4842));

            /* 范围魔法 */
        } else if (opid == S_OPCODE_RANGESKILLS) {
            // 免测试

            /* 角色锁定(座标异常重整) */
        } else if (opid == S_OPCODE_CHARLOCK) {
            // 免测试

            /* 邮件系统 */
        } else if (opid == S_OPCODE_MAIL) {
            // 免测试

            /* 血盟战争讯息(编号,血盟名称,目标血盟名称) */
        } else if (opid == S_OPCODE_WAR) {
            // 7 : 毁掉%0 血盟与 %1血盟之间的同盟。
            pc.sendPackets(new S_War(7, "测试0血盟", "测试1血盟"));

            /* NPC对话视窗 */
        } else if (opid == S_OPCODE_SHOWHTML) {
            // 能力质选取资料
            pc.sendPackets(new S_Bonusstats(pc.getId()));

            /* 选取物品数量 */
        } else if (opid == S_OPCODE_INPUTAMOUNT) {
            pc.sendPackets(new S_ItemCount(pc.getId(), 1000, "XXXX"));

            /* 伺服器讯息(行数/行数,附加字串) */
        } else if (opid == S_OPCODE_SERVERMSG) {
            // 96 \f1%0%s 拒绝你的请求。
            pc.sendPackets(new S_ServerMessage(96, "测试"));

            /* 选项(Yes/No) */
        } else if (opid == S_OPCODE_YES_NO) {
            // 你的血盟成员想要传送你。你答应吗？(Y/N)
            pc.sendPackets(new S_Message_YN(748));

            /* 物品鉴定资讯讯息 */
        } else if (opid == S_OPCODE_IDENTIFYDESC) {
            // 物品资讯讯息(使用String-c.tbl) 测试(骰子匕首)
            pc.sendPackets(new S_IdentifyDesc());

            /* 画面中蓝色(红色)讯息 */
        } else if (opid == S_OPCODE_BLUEMESSAGE) {
            // 552 因为你已经杀了 %0(100) 人所以被打入地狱。 你将在这里停留 %1(50) 分钟。
            pc.sendPackets(new S_BlueMessage(552, "100", "50"));

            /* 更新物品显示名称(背包) */
        } else if (opid == S_OPCODE_ITEMNAME) {
            // 免测试

            /* 更新物品使用状态(背包) */
        } else if (opid == S_OPCODE_ITEMAMOUNT) {
            // 免测试

            /* 更新物件亮度 */
        } else if (opid == S_OPCODE_LIGHT) {
            pc.sendPackets(new S_Light(pc.getId(), 0x14));

            /* 更新游戏天气 */
        } else if (opid == S_OPCODE_WEATHER) {
            // 下大雨
            pc.sendPackets(new S_Weather(19));

            /* 更新物件面向 */
        } else if (opid == S_OPCODE_CHANGEHEADING) {
            try {
                pc.setHeading(0);
                pc.sendPackets(new S_ChangeHeading(pc));
                Thread.sleep(500);
                pc.setHeading(1);
                pc.sendPackets(new S_ChangeHeading(pc));
                Thread.sleep(500);
                pc.setHeading(2);
                pc.sendPackets(new S_ChangeHeading(pc));
                Thread.sleep(500);
                pc.setHeading(3);
                pc.sendPackets(new S_ChangeHeading(pc));
                Thread.sleep(500);
                pc.setHeading(4);
                pc.sendPackets(new S_ChangeHeading(pc));
                Thread.sleep(500);
                pc.setHeading(5);
                pc.sendPackets(new S_ChangeHeading(pc));
                Thread.sleep(500);
                pc.setHeading(6);
                pc.sendPackets(new S_ChangeHeading(pc));
                Thread.sleep(500);
                pc.setHeading(7);
                pc.sendPackets(new S_ChangeHeading(pc));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /* 更新物件外型 */
        } else if (opid == S_OPCODE_POLY) {
            // 可用GM命令

            /* 更新物件名称 */
        } else if (opid == S_OPCODE_CHANGENAME) { // XXX 客户端报错掉线
            pc.sendPackets(new S_ChangeName(pc.getId(), "更新物件名称"));

            /* 更新HP显示 */
        } else if (opid == S_OPCODE_HPUPDATE) {
            pc.sendPackets(new S_HPUpdate(32767, 32767));

            /* 更新MP显示 */
        } else if (opid == S_OPCODE_MPUPDATE) {
            pc.sendPackets(new S_MPUpdate(32767, 32767));

            /* 更新角色所在的地图 */
        } else if (opid == S_OPCODE_MAPID) {
            // XXX ?????????

            /* 更新游戏时间 */
        } else if (opid == S_OPCODE_GAMETIME) {

            /* 更新经验值 */
        } else if (opid == S_OPCODE_EXP) {
            // LV 59 95.7553%
            pc.sendPackets(new S_Exp());

            /* 更新正义值 */
        } else if (opid == S_OPCODE_LAWFUL) {
            // -12345
            pc.sendPackets(new S_Lawful(pc.getId()));

            /* 更新魔攻与魔防 */
        } else if (opid == S_OPCODE_SPMR) {
            // +50 +100
            pc.sendPackets(new S_SPMR());

            /* 更新角色防御属性 */
        } else if (opid == S_OPCODE_OWNCHARATTRDEF) {
            // -99 90 85 80 75
            pc.sendPackets(new S_OwnCharAttrDef());

            /* 布告栏列表 */
        } else if (opid == S_OPCODE_BOARD) {
            pc.sendPackets(new S_Board(pc.getId()));

            /* 布告栏(讯息阅读) */
        } else if (opid == S_OPCODE_BOARDREAD) {
            pc.sendPackets(new S_BoardRead());

            /* 盟屋拍卖公告栏列表 */
        } else if (opid == S_OPCODE_HOUSELIST) {

            /* 血盟小屋地图(地点) */
        } else if (opid == S_OPCODE_HOUSEMAP) {

            /* 魔法效果 毒素 */
        } else if (opid == S_OPCODE_POISON) {
            // 绿色
            pc.sendPackets(new S_Poison(pc.getId(), 1));
            Thread.sleep(1000);
            // 灰色
            pc.sendPackets(new S_Poison(pc.getId(), 2));
            Thread.sleep(1000);
            // 通常色
            pc.sendPackets(new S_Poison(pc.getId(), 0));

            /* 魔法效果 勇敢药水颣 */
        } else if (opid == S_OPCODE_SKILLBRAVE) {
            // 3:身体内深刻的感觉到充满了森林的活力。(精灵饼干)
            pc.sendPackets(new S_SkillBrave(pc.getId(), 3));
            Thread.sleep(1000);
            // 5:从身体的深处感到热血沸腾。(第二阶段勇水)
            pc.sendPackets(new S_SkillBrave(pc.getId(), 5));
            Thread.sleep(1000);
            // 0:你的情绪回复到正常。(解除 )
            pc.sendPackets(new S_SkillBrave(pc.getId(), 0));

            /* 魔法效果 防御 */
        } else if (opid == S_OPCODE_SKILLICONSHIELD) {
            pc.sendPackets(new S_SkillIconShield(5, 3600));

            /* 魔法效果 加速颣 */
        } else if (opid == S_OPCODE_SKILLHASTE) {

            /* 魔法效果 精准目标 */
        } else if (opid == S_OPCODE_TRUETARGET) {
            for (final L1Object obj : pc.getKnownObjects()) {
                if (obj instanceof L1NpcInstance) {
                    L1NpcInstance npc = (L1NpcInstance) obj;
                    npc.broadcastPacketX8(new S_TrueTarget(npc.getId(), pc
                            .getId(), "魔法效果 精准目标"));
                }
            }

            /* 魔法效果 水底呼吸 */
        } else if (opid == S_OPCODE_BLESSOFEVA) {
            pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), 300));

            /* 魔法效果 物件隐形 */
        } else if (opid == S_OPCODE_INVIS) {
            pc.sendPackets(new S_Invis(pc.getId(), 1));
            Thread.sleep(1000);
            pc.sendPackets(new S_Invis(pc.getId(), 0));

            /* 魔法效果 操作混乱(醉酒) */
        } else if (opid == S_OPCODE_LIQUOR) {
            pc.sendPackets(new S_Liquor(pc.getId(), 1));
            Thread.sleep(1000);
            pc.sendPackets(new S_Liquor(pc.getId(), 2));
            Thread.sleep(1000);
            pc.sendPackets(new S_Liquor(pc.getId(), 3));
            Thread.sleep(1000);
            pc.sendPackets(new S_Liquor(pc.getId(), 0));

            /* 魔法效果 诅咒 */
        } else if (opid == S_OPCODE_PARALYSIS) {
            // 你的身体完全麻痹了
            pc.sendPackets(new S_Paralysis(0x02, true));

            /* 魔法效果 敏捷提升 */
        } else if (opid == S_OPCODE_DEXUP) {
            pc.sendPackets(new S_Dexup(pc, 5, 1800));

            /* 魔法效果 力量提升 */
        } else if (opid == S_OPCODE_STRUP) {
            pc.sendPackets(new S_Strup(pc, 5, 1800));

            /* 魔法效果 暗盲咒术 */
        } else if (opid == S_OPCODE_CURSEBLIND) {
            pc.sendPackets(new S_CurseBlind(1));
            Thread.sleep(1000);
            pc.sendPackets(new S_CurseBlind(2));
            Thread.sleep(1000);
            pc.sendPackets(new S_CurseBlind(0));

            /* 物品名单(背包) */
        } else if (opid == S_OPCODE_INVLIST) {

            /* 魔法购买清单(金币) */
        } else if (opid == S_OPCODE_SKILLBUY) {

            /* 魔法购买清单(材料) */
        } else if (opid == S_OPCODE_SKILLBUYITEM) {

            /* 魔法清单(增加) */
        } else if (opid == S_OPCODE_ADDSKILL) {
            for (int i = 0; i < 300; i++) {
                // 取得魔法资料(要移除的)
                final L1Skills skill = SkillsTable.get().getTemplate(i);
                if (skill != null) {
                    if (skill.getSkillLevel() > 0) {
                        // 移除魔法
                        pc.sendPackets(new S_DelSkill(pc, i));
                    }
                }
            }

            Thread.sleep(2000);
            System.out.println("TEST START!!");

            int[] level = new int[28];
            int dx = 1;
            for (int mode = 0; mode < 28; mode++) {
                while (dx < 255) {
                    level[mode] += dx;
                    pc.sendPackets(new S_AddSkill(pc, mode, level[mode]));
                    System.out.println("dx: " + dx + " " + level[mode]);
                    dx = dx << 1;
                    Thread.sleep(100);
                }
                if (dx >= 255) {
                    dx = 1;
                }
            }

            /* 魔法清单(移除) */
        } else if (opid == S_OPCODE_DELSKILL) {
            for (int i = 0; i < 300; i++) {
                // 取得魔法资料(要移除的)
                final L1Skills skill = SkillsTable.get().getTemplate(i);
                if (skill != null) {
                    if (skill.getSkillLevel() > 0) {
                        // 移除魔法
                        pc.sendPackets(new S_DelSkill(pc, i));
                    }
                }
                Thread.sleep(10);
            }

            /* 物品名单(仓库) */
        } else if (opid == S_OPCODE_SHOWRETRIEVELIST) {

            /* 角色座标名单 */
        } else if (opid == S_OPCODE_BOOKMARKS) {

            /* 损坏武器清单 */
        } else if (opid == S_OPCODE_SELECTLIST) {
            // 身上必须有损坏武器
            // 暂时清单
            final List<L1ItemInstance> weaponList = new ArrayList<L1ItemInstance>();
            // 背包物件
            final List<L1ItemInstance> itemList = pc.getInventory().getItems();
            for (final L1ItemInstance item : itemList) {
                switch (item.getItem().getType2()) {
                    case 1:
                        if (item.get_durability() > 0) {
                            weaponList.add(item);
                        }
                        break;
                }
            }
            pc.sendPackets(new S_FixWeaponList(weaponList));

            /* NPC物品购买清单(人物卖出) */
        } else if (opid == S_OPCODE_SHOWSHOPSELLLIST) {

            /* NPC物品贩卖清单(人物买入) */
        } else if (opid == S_OPCODE_SHOWSHOPBUYLIST) {

            /* 交易封包 */
        } else if (opid == S_OPCODE_TRADE) {
            // 显示:测试交易封包
            pc.sendPackets(new S_Trade("测试交易封包"));

            /* 交易状态 */
        } else if (opid == S_OPCODE_TRADESTATUS) {
            // 1:交易取消
            pc.sendPackets(new S_TradeStatus(1));

            /* 交易增加物品 */
        } else if (opid == S_OPCODE_TRADEADDITEM) {
            // 须先开起交易视窗
            // 1:交易视窗下半部 恶魔头盔 测试物品(55) 0:祝福
            pc.sendPackets(new S_TradeAddItem());

            /* 交易商店清单(购买/个人商店) */
        } else if (opid == S_OPCODE_PRIVATESHOPLIST) {

            /* 戒指 */
        } else if (opid == S_OPCODE_ABILITY) {

            /* 拨放音效 */
        } else if (opid == S_OPCODE_SOUND) {

            /* 传送锁定(瞬间移动) */
        } else if (opid == S_OPCODE_TELEPORT) {

            /* 传送锁定(座标点) */
        } else if (opid == S_OPCODE_TELEPORTLOCK) {

            /* 选择一个目标 */
        } else if (opid == S_OPCODE_SELECTTARGET) {
            pc.sendPackets(new S_SelectTarget(pc.getId()));

            /* 城堡宝库(要求存入资金) */
        } else if (opid == S_OPCODE_DEPOSIT) {
            // 须先开启一个HTML视窗
            // 显示身上金币
            pc.sendPackets(new S_Deposit(pc.getId()));

            /* 城堡宝库(要求领出资金) */
        } else if (opid == S_OPCODE_DRAWAL) {
            // 须先开启一个HTML视窗
            // 显示 1234567
            pc.sendPackets(new S_Drawal(pc.getId(), 1234567));

            /* 佣兵数量名单 */
        } else if (opid == S_OPCODE_HIRESOLDIER) { // XXX
            // 须先开启一个HTML视窗
            pc.sendPackets(new S_HireSoldier(pc));

            /* 税收设定 */
        } else if (opid == S_OPCODE_TAXRATE) {
            // 须先开启一个HTML视窗
            pc.sendPackets(new S_TaxRate(pc.getId()));

            /* 围城时间设定 */
        } else if (opid == S_OPCODE_WARTIME) {
            // 须先开启一个HTML视窗
            pc.sendPackets(new S_WarTime(S_OPCODE_WARTIME));

            /* 画面中红色讯息(登入来源) */
        } else if (opid == S_OPCODE_RED) {

            /* NPC改变外型 */
        } else if (opid == S_OPCODE_NPCPOLY) { // XXX
            for (final L1Object obj : pc.getKnownObjects()) {
                if (obj instanceof L1NpcInstance) {
                    L1NpcInstance npc = (L1NpcInstance) obj;
                    npc.broadcastPacketX8(new S_ChangeShape(pc, npc, 1080));
                }
            }

            /* Ping Time */
        } else if (opid == S_OPCODE_PINGTIME) {

            /* 强制登出人物 */
        } else if (opid == S_OPCODE_CHAROUT) { // XXX
            pc.sendPackets(new S_ChatOut(pc));

            /* 物件新增主人 */
        } else if (opid == S_OPCODE_NEWMASTER) {

            /* 未知购物清单 */
        } else if (opid == S_OPCODE_SHOPX1) {

            /* 未知购物清单 */
        } else if (opid == S_OPCODE_SHOPX2) {

            // NEW ADD

            /* 学习魔法材料不足 */
        } else if (opid == S_OPCODE_ITEMERROR) { // XXX
            for (int i = 0; i < 10; i++) {
                pc.sendPackets(new S_ItemError(i));
                Thread.sleep(250);
            }

            /* 服务器登入讯息(使用string.tbl) */
        } else if (opid == S_OPCODE_COMMONINFO) {
            // 免测试

        }
    }
}
