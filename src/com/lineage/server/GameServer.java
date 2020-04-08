package com.lineage.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigRate;
import com.lineage.config.ConfigSQL;
import com.lineage.echo.PacketHandler;
import com.lineage.list.BadNamesList;
import com.lineage.server.datatables.*;
import com.lineage.server.datatables.lock.*;
import com.lineage.server.datatables.sql.*;
import com.lineage.server.model.L1AttackList;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1ClanMatching;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1ItemPower;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.model.skill.L1SkillMode;
import com.lineage.server.templates.L1PcOther;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.thread.NpcAiThreadPool;
import com.lineage.server.thread.PcOtherThreadPool;
import com.lineage.server.timecontroller.StartTimer_Event;
import com.lineage.server.timecontroller.StartTimer_Pc;
import com.lineage.server.timecontroller.StartTimer_Npc;
import com.lineage.server.timecontroller.StartTimer_Pet;
import com.lineage.server.timecontroller.StartTimer_Server;
import com.lineage.server.timecontroller.StartTimer_Skill;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.world.*;

/**
 * 加载服务器设置
 * 
 * @author dexc
 * 
 */
public class GameServer {

    private static final Log _log = LogFactory.getLog(GameServer.class);

    private static GameServer _instance;

    public static GameServer getInstance() {
        if (_instance == null) {
            _instance = new GameServer();
        }
        return _instance;
    }

    public void initialize() throws Exception {
        final PerformanceTimer timer = new PerformanceTimer();
        try {
            _log.info("\n\r--------------------------------------------------"
                    + "\n\r       外部设置：经验倍率: " + ConfigRate.RATE_XP
                    + "\n\r       外部设置：正义质倍率: " + ConfigRate.RATE_LA
                    + "\n\r       外部设置：有好度倍率: " + ConfigRate.RATE_KARMA
                    + "\n\r       外部设置：物品掉落倍率: " + ConfigRate.RATE_DROP_ITEMS
                    + "\n\r       外部设置：金币掉落倍率: " + ConfigRate.RATE_DROP_ADENA
                    + "\n\r       外部设置：广播等级限制: " + ConfigAlt.GLOBAL_CHAT_LEVEL
                    + "\n\r       外部设置：PK设置: "
                    + (ConfigAlt.ALT_NONPVP ? "允许" : "不允许")
                    + "\n\r       外部设置：最大连线设置: " + Config.MAX_ONLINE_USERS
                    + "\n\r--------------------------------------------------");

            // 客户端封包资料加载
            PacketHandler.load();

            ServerReading.get().load();// 载入保留的服务器资料

            IdFactory.get().load();// OBJID

            CharObjidTable.get().load();// 人物已用OBJID预先加载/血盟已用OBJID预先加载

            AccountReading.get().load();// 帐户名称资料
            
            ExchangeReading.get().load(); //载入 金币兑换元宝资料 hjx1000

            GeneralThreadPool.get();// 线程工厂设置

            PcOtherThreadPool.get();// 线程工厂设置

            NpcAiThreadPool.get();// 线程工厂设置

            ExpTable.get().load();// 经验资料库

            SprTable.get().load();// 图形影格资料

            MapsTable.get().load();// 地图设置

            MapExpTable.get().load();// 地图经验设置

            MapLevelTable.get().load();// 地图等级限制

            ItemTimeTable.get().load();// 物品可用时间指定

            L1WorldMap.get().load();// MAP

            L1GameTimeClock.init();// 游戏时间时计

            NpcTable.get().load();// NPC资料

            NpcScoreTable.get().load();// NPC积分资料

            CharacterTable.loadAllCharName();// 载入已用名称

            CharacterTable.clearOnlineStatus();// 全部状态离线

            // 世界储存中心资料建立
            World.get();

            WorldCrown.get();

            WorldKnight.get();

            WorldElf.get();

            WorldWizard.get();

            WorldDarkelf.get();

            WorldDragonKnight.get();

            WorldIllusionist.get();

            WorldPet.get();

            WorldSummons.get();

            TrapTable.get().load();// 陷阱资料

            TrapsSpawn.get().load();// 陷阱召唤资料

            ItemTable.get().load();// 道具物品资料

            DropTable.get().load();// 掉落物品资料

            DropMapTable.get().load();// 掉落物品资料

            DropItemTable.get().load();// 掉落物品机率资料

            SkillsTable.get().load();// 技能设置资料

            SkillsItemTable.get().load();// 购买技能 金币/材料 设置资料

            MobGroupTable.get().load();// MOB队伍资料

            SpawnTable.get().load();// 召唤清单

            PolyTable.get().load();// 人物变身资料

            ShopTable.get().load();// 商店贩卖资料

            ShopCnTable.get().load();// 特殊商店贩卖资料

            DungeonTable.get().load();// 地图切换点设置

            DungeonRTable.get().load();// 地图切换点设置(多点)

            NPCTalkDataTable.get().load();// NPC对话资料

            NpcSpawnTable.get().load();// 召唤NPC资料

            DwarfForClanReading.get().load();// 血盟仓库资料建立

            ClanReading.get().load();// 血盟资料

            ClanEmblemReading.get().load();// 血盟盟辉资料

            CastleReading.get().load();// 城堡资料

            L1CastleLocation.setCastleTaxRate(); // 城堡税收数据

            GetBackRestartTable.get().load();// 回城座标资料

            DoorSpawnTable.get().load();// 门资料

            WeaponSkillTable.get().load();// 技能武器资料

            NpcActionTable.load();// NPC XML对话结果资料

            GetbackTable.loadGetBack();// 回村座标设置

            PetTypeTable.load();// 宠物种族资料

            PetItemTable.get().load();// 宠物道具资料

            ItemBoxTable.get().load();// 箱子开出物设置

            ResolventTable.get().load();// 溶解物品设置

            NpcTeleportTable.get().load();// NPC传送点设置

            NpcChatTable.get().load();// NPC会话资料

            ArmorSetTable.get().load();// 套装设置

            ItemTeleportTable.get().load();// 传送卷轴传送点定义

            ItemPowerUpdateTable.get().load();// 特殊物品升级资料

            CommandsTable.get().load();// GM命令

            BeginnerTable.get().load();// 新手物品资料

            ItemRestrictionsTable.get().load();// 物品交易限制清单

            // TODO 预先加载SQL资料

            // 召唤BOSS资料
            SpawnBossReading.get().load();

            // 血盟小屋
            HouseReading.get().load();

            // 禁止位置
            IpReading.get().load();

            // 村庄资料
            TownReading.get().load();

            // 信件资料
            MailReading.get().load();

            // 拍卖公告栏资料
            AuctionBoardReading.get().load();

            // 布告栏资料
            BoardReading.get().load();

            // 保留技能纪录
            CharBuffReading.get().load();

            // 人物技能纪录
            CharSkillReading.get().load();

            // 人物快速键纪录
            CharacterConfigReading.get().load();

            // 人物好友纪录
            BuddyReading.get().load();

            // 人物记忆座标纪录资料
            CharBookReading.get().load();

            // 人物额外纪录资料
            CharOtherReading.get().load();

            // 任务纪录
            CharacterQuestReading.get().load();

            // 建立角色名称时禁止使用的字元
            BadNamesList.get().load();

            // 景观设置资料
            SceneryTable.get().load();

            // 各项技能设置启用
            L1SkillMode.get().load();

            // 物理攻击/魔法攻击判定
            L1AttackList.load();

            // 物品能力值
            L1ItemPower.load();

            // 加载连续魔法减低损伤资料
            L1PcInstance.load();

            // 背包资料建立
            CharItemsReading.get().load();

            // 仓库资料建立
            DwarfReading.get().load();

            // 精灵仓库资料建立
            DwarfForElfReading.get().load();

            // 娃娃能力资料
            DollPowerTable.get().load();

            // 宠物资料
            PetReading.get().load();

            // 人物背包物品使用期限资料
            CharItemsTimeReading.get().load();

            // 人物其他相关设置表
            L1PcOther.load();

            // 各项特殊活动设置启动
            EventTable.get().load();

            // 特殊活动设置召唤启动
            if (EventTable.get().size() > 0) {
                EventSpawnTable.get().load();
            }

            // 任务(副本)地图设置加载
            QuestMapTable.get().load();

            // 家具召唤资料
            FurnitureSpawnReading.get().load();

            // 打宝公告
            ItemMsgTable.get().load();

            // 武器额外伤害资料
            WeaponPowerTable.get().load();

            // 渔获资料
            FishingTable.get().load();

            // 攻城奖励
            CastleWarGiftTable.get().load();

            // TODO TIMER

            // 服务器专用时间轴
            final StartTimer_Server startTimer = new StartTimer_Server();
            startTimer.start();

            // PC专用时间轴
            final StartTimer_Pc pcTimer = new StartTimer_Pc();
            pcTimer.start();

            // NPC专用时间轴
            final StartTimer_Npc npcTimer = new StartTimer_Npc();
            npcTimer.start();

            // PET专用时间轴
            final StartTimer_Pet petTimer = new StartTimer_Pet();
            petTimer.start();

            // SKILL专用时间轴
            final StartTimer_Skill skillTimer = new StartTimer_Skill();
            skillTimer.start();

            // EVENT专用时间轴
            final StartTimer_Event eventTimer = new StartTimer_Event();
            eventTimer.start();

            // 设置关机导向
            Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());

            // TODO 它向设置

            // 监听端口启动重置作业
            EchoServerTimer.get().start();

            // 打开关闭的门
            L1DoorInstance.openDoor();
            L1ClanMatching.getInstance().loadClanMatching();//血盟UI hjx1000

            // _log.info("游戏伺服器启动完成!!");

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            final String osname = System.getProperties().getProperty("os.name");
            final String username = System.getProperties().getProperty(
                    "user.name");

            final String ver = "\n\r--------------------------------------------------"
                    + "\n\r       游戏伺服器核心版本: "
                    + Config.VER
                    + " "
                    + Config.SRCVER
                    + "\n\r\n\r       Server Verion: "
                    + Config.SVer
                    + "\n\r       Cache Verion: "
                    + Config.CVer
                    + "\n\r       Auth Verion: "
                    + Config.AVer
                    + "\n\r       Npc Verion: "
                    + Config.NVer
                    + "\n\r\n\r       主机位置: "
                    + Config.GAME_SERVER_HOST_NAME
                    + "\n\r       监听端口: "
                    + Config.GAME_SERVER_PORT
                    + "\n\r\n\r       伺服器作业系统: "
                    + osname
                    + "\n\r       伺服器使用者: "
                    + username
                    + "\n\r       使用者名称资料库: "
                    + ConfigSQL.DB_URL2_LOGIN
                    + "\n\r       伺服器档案资料库: "
                    + ConfigSQL.DB_URL2
                    + "\n\r       绑定登入器设置: "
                    + Config.LOGINS_TO_AUTOENTICATION
                    + "\n\r--------------------------------------------------"
                    + "\n\r       伺服器由[伊薇JAVA技术团队]学术研究修改"
                    + "\n\r       伊薇JAVA核心只会在伊薇网站公布"
                    + "\n\r       核心仅供JAVA程式研究，禁止商业性出售核心"
                    + "\n\r       任何违法行为[伊薇JAVA技术团队]不附连带责任"
                    + "\n\r       伊薇JAVA技术团队保留对此核心违法行为追诉权"
                    + "\n\r       权力范围:JAVA原始码以及衍生出的图片"
                    + "\n\r       伊薇JAVA论坛网址http://yiwei.co"
                    + "\n\r       更新时间:2012-08-16 SVN编号:160"
                    + "\n\r--------------------------------------------------";
            _log.info(ver);

            // 启动视窗命令接收器
            CmdEcho cmdEcho = new CmdEcho(timer.get());
            cmdEcho.runCmd();
        }
    }
}
