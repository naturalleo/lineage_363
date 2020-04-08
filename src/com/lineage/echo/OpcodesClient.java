package com.lineage.echo;

import com.lineage.server.serverpackets.OpcodesServer;

/**
 * 服务器封包编组设置.
 */
public class OpcodesClient {

    public OpcodesClient() {
    }

    protected static final int _seed = 0x3b43dd0b; // 353C_TW

    /**
     * 第一组封包.
     */
    protected static final byte[] _firstPacket = {
            (byte) 0x12, // 全部封包长度
            (byte) 0x00,
            // 改版时不需要变动以上2个BYTE
            (byte) OpcodesServer.S_OPCODE_INITOPCODE, // 初始化封包
            (byte) 0x0b, (byte) 0xdd, (byte) 0x43, (byte) 0x3b, (byte) 0x47,
            (byte) 0x71, (byte) 0xd6, (byte) 0x6c, (byte) 0x60, (byte) 0x7e,
            (byte) 0x01, (byte) 0x00, (byte) 0x04, (byte) 0x08, (byte) 0x00 };

    /**
     * 第一组封包.
     */
    // protected static final byte[] _firstPacket = {
    // (byte) 0x12, // 全部封包长度
    // (byte) 0x00,
    // 改版时不需要变动以上2个BYTE
    // (byte) OpcodesServer.S_OPCODE_INITOPCODE, // 初始化封包
    // (byte) 0x68, (byte) 0xb6, (byte) 0x0e, (byte) 0x53, (byte) 0x59,
    // (byte) 0xca, (byte) 0xcf, (byte) 0x28, (byte) 0x29, (byte) 0x76,
    // (byte) 0xd9, (byte) 0x06, (byte) 0xb0, (byte) 0xf0, (byte) 0xa6 };

    // 没有注释的也是未知
    // //////////
    // 登陆进游戏世界时发送(点数重置后) OP:29 0000: 1d 01 00 00 00 20 39 31 ..... 91
    // ////////////////////////////////////////////////////////

    /** 要求删除角色 */
    public static final int C_OPCODE_DELETECHAR = 10;
    /** 要求删除公布栏内容 */
    public static final int C_OPCODE_BOARDDELETE = 12;
    /** 要求写入公布栏讯息 */
    public static final int C_OPCODE_BOARDWRITE = 14;
    /** 要求开个人商店 */
    public static final int C_OPCODE_SHOP = 16;
    /** 要求打开邮箱 */
    public static final int C_OPCODE_MAIL = 22;
    /** 要求钓鱼收杆 */
    public static final int C_OPCODE_FISHCLICK = 26;
    /** 要求加入血盟 */
    public static final int C_OPCODE_JOINCLAN = 30;
    /** 要求存入资金 */
    public static final int C_OPCODE_DEPOSIT = 35;
    /** 要求物件对话视窗结果 */
    public static final int C_OPCODE_NPCACTION = 37;
    /** 要求列表物品取得 */
    public static final int C_OPCODE_RESULT = 40;
    /** 3.3新地图系统/视窗失焦. */
    public static final int C_OPCODE_WINDOWS = 41;
    /** 要求查看队伍名单. */
    public static final int C_OPCODE_PARTY = 42;
    /** 要求使用物品 */
    public static final int C_OPCODE_USEITEM = 44;
    /** 要求寄送简讯 未用 */
    public static final int C_OPCODE_SMS = 45;
    /** 要求决斗 */
    public static final int C_OPCODE_FIGHT = 47;
    /** 要求查询游戏人数 */
    public static final int C_OPCODE_WHO = 49;
    /** 要求下一步 ( 公告资讯 ) */
    public static final int C_OPCODE_COMMONCLICK = 53;
    /** 要求丢弃物品 */
    public static final int C_OPCODE_DROPITEM = 54;
    /** 要求登入伺服器/创建新帐号. */
    public static final int C_OPCODE_LOGINPACKET = 57;
    /** 要求物件对话视窗 */
    public static final int C_OPCODE_NPCTALK = 58;
    /** 要求阅读布告单个栏讯息 */
    public static final int C_OPCODE_BOARDREAD = 59;
    /** 要求查询朋友名单 */
    public static final int C_OPCODE_BUDDYLIST = 60;
    /** 要求点选项目的结果 */
    public static final int C_OPCODE_ATTR = 61;
    /** 要求使用广播聊天频道 */
    public static final int C_OPCODE_CHATGLOBAL = 62;
    /** 要求改变角色面向 */
    public static final int C_OPCODE_CHANGEHEADING = 65;
    /** 要求邀请加入队伍(要求创立队伍) 3.3新增委任队长功能 */
    public static final int C_OPCODE_CREATEPARTY = 166;//修正组队封包错误 hjx1000
    /** 要求角色攻击 */
    public static final int C_OPCODE_ATTACK = 68;
    /** 要求踢出队伍 */
    public static final int C_OPCODE_BANPARTY = 70;
    /** 要求死亡后重新开始 */
    public static final int C_OPCODE_RESTART = 71;
    /** 要求读取公布栏 */
    public static final int C_OPCODE_BOARD = 73;
    /** 登入伺服器OK */
    public static final int C_OPCODE_LOGINTOSERVEROK = 75;
    /** 要求变更仓库密码 && 送出仓库密码. */
    public static final int C_OPCODE_PWD = 81;
    /** 要求使用血盟阶级功能功能(/rank 人物 见习). */
    public static final int C_OPCODE_RANK = 88;
    /** 要求角色移动 */
    public static final int C_OPCODE_MOVECHAR = 95;
    /** 要求赋予封号 */
    public static final int C_OPCODE_TITLE = 96;
    /** 要求新增好友 */
    public static final int C_OPCODE_ADDBUDDY = 99;
    /** 要求使用拒绝名单(开启指定人物讯息)/exclude 名字 */
    public static final int C_OPCODE_EXCLUDE = 101;
    /** 要求交易(个人) */
    public static final int C_OPCODE_TRADE = 103;
    /** 要求离开游戏 */
    public static final int C_OPCODE_QUITGAME = 104;
    /** 要求维修物品清单 */
    public static final int C_OPCODE_FIX_WEAPON_LIST = 106;
    /** 要求上传盟标 */
    public static final int C_OPCODE_EMBLEM = 107;
    /** 要求确定数量选取 */
    public static final int C_OPCODE_AMOUNT = 109;
    /** 要求完成交易(个人) */
    public static final int C_OPCODE_TRADEADDOK = 110;
    /** 要求队伍对话控制(命令/chatparty) */
    public static final int C_OPCODE_CAHTPARTY = 113;
    /** 要求使用技能 */
    public static final int C_OPCODE_USESKILL = 115;
    /** 要求船票数量 */
    public static final int C_OPCODE_SHIP = 117;
    /** 要求脱离血盟 */
    public static final int C_OPCODE_LEAVECLANE = 121;
    /** 要求使用密语聊天频道 */
    public static final int C_OPCODE_CHATWHISPER = 122;
    /** 要求设置治安管理 */
    public static final int C_OPCODE_CASTLESECURITY = 125;
    /** 要求登入测试 ( 接收伺服器版本 ) */
    public static final int C_OPCODE_CLIENTVERSION = 127;
    /** 要求纪录快速键 */
    public static final int C_OPCODE_CHARACTERCONFIG = 129;
    /** 要求进入游戏(确定服务器登入讯息). */
    public static final int C_OPCODE_LOGINTOSERVER = 131;
    /** 要求增加记忆座标. */
    public static final int C_OPCODE_BOOKMARK = 134;
    /** 要求查询PK次数 */
    public static final int C_OPCODE_CHECKPK = 137;
    /** 要求召唤到身边(gm) */
    public static final int C_OPCODE_CALL = 144;
    /** 要求治安管理 OK */
    public static final int C_OPCODE_SETCASTLESECURITY = 149;
    /** 要求选择 变更攻城时间 (官方已取消). */
    public static final int C_OPCODE_CHANGEWARTIME = 150;
    /** 要求创立血盟 */
    public static final int C_OPCODE_CREATECLAN = 154;
    /** 要求攻击指定物件(宠物&召唤) */
    public static final int C_OPCODE_SELECTTARGET = 155;
    /** 要求角色表情动作 */
    public static final int C_OPCODE_EXTCOMMAND = 157;
    /** 要求自动登录伺服器 与 师徒系统 */
    public static final int C_OPCODE_AUTO = 162;
    /** 要求取消交易(个人) */
    public static final int C_OPCODE_TRADEADDCANCEL = 167;
    /** 要求学习魔法(金币) */
    public static final int C_OPCODE_SKILLBUY = 173;
    /** 要求更新时间 */
    public static final int C_OPCODE_KEEPALIVE = 182;
    /** 要求结婚 (指令 /求婚) */
    public static final int C_OPCODE_PROPOSE = 185;
    /** 要求捡取物品 */
    public static final int C_OPCODE_PICKUPITEM = 188;
    /** 要求使用一般聊天频道 */
    public static final int C_OPCODE_CHAT = 190;
    /** 要求领出资金 */
    public static final int C_OPCODE_DRAWAL = 192;
    /** 要求个人商店 （物品列表） */
    public static final int C_OPCODE_PRIVATESHOPLIST = 193;
    /** 要求开关门 */
    public static final int C_OPCODE_DOOR = 199;
    /** 要求税收设定封包 */
    public static final int C_OPCODE_TAXRATE = 200;
    /** 要求脱离队伍 */
    public static final int C_OPCODE_LEAVEPARTY = 204;
    /** 要求学习魔法完成 */
    public static final int C_OPCODE_SKILLBUYOK = 207;
    /** 要求删除物品 */
    public static final int C_OPCODE_DELETEINVENTORYITEM = 209;
    /** 要求退出观看模式 */
    public static final int C_OPCODE_EXIT_GHOST = 210;
    /** 要求删除好友 */
    public static final int C_OPCODE_DELBUDDY = 211;
    /** 要求使用宠物装备 */
    public static final int C_OPCODE_USEPETITEM = 213;
    /** 要求宠物回报选单(显示宠物背包物品窗口) */
    public static final int C_OPCODE_PETMENU = 217;
    /** 要求回到登入画面(返回输入帐号密码界面). */
    public static final int C_OPCODE_RETURNTOLOGIN = 218;
    /** 要求下一页 ( 公布栏 ). */
    public static final int C_OPCODE_BOARDBACK = 221;
    /** 要求驱逐人物离开血盟 */
    public static final int C_OPCODE_BANCLAN = 222;
    /** 要求删除记忆座标 */
    public static final int C_OPCODE_BOOKMARKDELETE = 223;
    /** 要求查询血盟成员 */
    public static final int C_OPCODE_PLEDGE = 225;
    /** 玩家传送锁定 (回溯检测用). */
    public static final int C_OPCODE_MOVELOCK = 226;
    /** 要求宣战 */
    public static final int C_OPCODE_WAR = 235;
    /** 要求重置人物点数 */
    public static final int C_OPCODE_CHARRESET = 236;
    /** 要求切换角色 (到选人画面) */
    public static final int C_OPCODE_CHANGECHAR = 237;
    /** 要求物品维修/取出宠物 */
    public static final int C_OPCODE_SELECTLIST = 238;
    /** 要求交易(添加物品) */
    public static final int C_OPCODE_TRADEADDITEM = 241;
    /** 要求传送 更新周围物件 ( 无动画传送后 ) */
    public static final int C_OPCODE_TELEPORT = 242;
    /** 要求给予物品 */
    public static final int C_OPCODE_GIVEITEM = 244;
    /** 要求血盟数据(例如盟标) */
    public static final int C_OPCODE_CLAN = 246;
    /** 要求使用远距武器 */
    public static final int C_OPCODE_ARROWATTACK = 247;
    /** 要求鼠标右键传入洞穴 */
    public static final int C_OPCODE_ENTERPORTAL = 249;
    /** 要求创造角色 */
    public static final int C_OPCODE_NEWCHAR = 253;

    // //////旧版没有的////////////

    /** 要求血盟推荐数据 */
    public static final int C_OPCODE_CLAN_RECOMMEND = 228;
    // 客户端点击 0000: e4 04 00 00

    // XXX 未知 unknown
    /** 要求提取天宝. */
    public static final int C_OPCODE_CNITEM = -1;
    /** 要求确认未知购物清单2. */
    public static final int C_OPCODE_SHOPX2 = -2;
    /** 要求简讯服务. */
    public static final int C_OPCODE_MSG = -3;
    /** 要求完成学习魔法(材料). */
    public static final int C_OPCODE_SKILLBUYOKITEM = -19;
    /** 要求更新周围物件(座标点/洞穴点切换进出后). */
    public static final int C_OPCODE_TELEPORT2 = -20;
    /** 要求学习魔法清单(材料). */
    public static final int C_OPCODE_SKILLBUYITEM = -26;
    /** 要求配置已雇用的士兵. */
    public static final int C_OPCODE_PUTSOLDIER = -57;
    /** 要求新增帐号. */
    public static final int C_OPCODE_NEWACC = -59;
    /** 要求配置已雇用的士兵OK. */
    public static final int C_OPCODE_PUTHIRESOLDIER = -94;
    /** 雇请佣兵(购买佣兵完成). */
    public static final int C_OPCODE_HIRESOLDIER = -97;
    /** 要求配置城墙上的弓箭手OK. */
    public static final int C_OPCODE_PUTBOWSOLDIER = -110;
}
