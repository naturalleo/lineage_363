package com.lineage.server.serverpackets;

/**
 * 服务器封包编组设置.
 */
public class OpcodesServer {

    public OpcodesServer() {
    }

    // 未知
    /** 雇请佣兵(佣兵购买视窗). */
    protected static final int S_OPCODE_HIRESOLDIER = -123;
    /** 配置城墙上的弓箭手列表(佣兵购买视窗). */
    protected static final int S_OPCODE_PUTBOWSOLDIERLIST = -23;
    /** 佣兵配置清单. */
    protected static final int S_OPCODE_PUTSOLDIER = -77;

    /** 强制登出人物. */
    protected static final int S_OPCODE_CHAROUT = -110;
    /** 服务器登入讯息(使用string.tbl). */
    protected static final int S_OPCODE_COMMONINFO = -88;
    /** 未知购物清单1 Server op: 0. */
    protected static final int S_OPCODE_SHOPX1 = -0;
    /** 未知购物清单2 Server op: 71. */
    protected static final int S_OPCODE_SHOPX2 = -71;

    // 没注释的也是未知
    // //////////////
    // /////////////////
    /** 魔法购买清单(材料). */
    protected static final int S_OPCODE_SKILLBUYITEM = -22;
    /** 阅读邮件(旧). */
    protected static final int S_OPCODE_LETTER = -33;

    /** 物理范围攻击 Server op: 0000. */
    protected static final int S_OPCODE_ATTACKRANGE = -127;

    // //////////////
    // /////////////////
    // 0 佣兵窗口 要有已雇用的士兵才可以配置，你要选择哪一种士兵呢？(没有选项)
    // 46 客户端传来239
    // 52 直接返回角色选择画面
    // 171 何伦对话窗口:那些法术我也只能教你到第3级。
    // 192 推荐血盟大窗口
    // 210 购买窗口 (佣兵购买?)
    // 240 内存错误
    // ////////////////////////
    /** 角色锁定(座标异常重整)【19/23/29/81/82/181/182/211/230】. */
    protected static final int S_OPCODE_CHARLOCK = 135;//19; // TODO 不知官方啥效果 //修改为135 hjx1000

    /** NPC改变外型(宠物/谜魅用). */
    protected static final int S_OPCODE_NPCPOLY = 164; // TODO 未测试
    // /////////////////////////////////////////////////////////////
    /** 邮件封包 */
    public static final int S_OPCODE_MAIL = 1;
    /** 物件封包 */
    public static final int S_OPCODE_CHARPACK = 3;
    /** 要求传送 ( NPC传送反手 ) */
    public static final int S_OPCODE_TELEPORT = 4;
    /** 角色移除 [ 非立即 ] */
    public static final int S_OPCODE_DETELECHAROK = 5;
    /** 更新血盟数据. */
    protected static final int S_OPCODE_UPDATECLANID = 8;
    /** 广播聊天频道 */
    public static final int S_OPCODE_GLOBALCHAT = 10;
    /** 角色记忆座标名单 */
    public static final int S_OPCODE_BOOKMARKS = 11;
    /** 效果图示 { 水底呼吸 } */
    public static final int S_OPCODE_BLESSOFEVA = 12;
    /** 物件新增主人. */
    protected static final int S_OPCODE_NEWMASTER = 13;
    /** 伺服器讯息(行数, 附加字串 ) */
    public static final int S_OPCODE_SERVERMSG = 14;
    /** 角色防御 & 属性防御 更新 */
    public static final int S_OPCODE_OWNCHARATTRDEF = 15;
    /** 范围魔法 */
    public static final int S_OPCODE_RANGESKILLS = 16;
    /** 移除魔法出魔法名单 */
    public static final int S_OPCODE_DELSKILL = 18;
    /** 血盟小屋名单 */
    public static final int S_OPCODE_HOUSELIST = 24;
    /** 敏捷提升封包 */
    public static final int S_OPCODE_DEXUP = 28;
    /** 公告视窗 */
    public static final int S_OPCODE_COMMONNEWS = 30;
    /** 海底波纹(第三段加速) */
    public static final int S_OPCODE_LIQUOR = 31;
    /** 重置设定 */
    public static final int S_OPCODE_CHARRESET = 33;
    /** 物件属性 (门 开关) */
    public static final int S_OPCODE_ATTRIBUTE = 35;
    /** 可配置排列佣兵数(HTML)(EX:雇用的总佣兵数:XX 可排列的佣兵数:XX ). */
    protected static final int S_OPCODE_PUTHIRESOLDIER = 39;
    /** 角色选择视窗 / 开启拒绝名单 (封包盒子) */
    public static final int S_OPCODE_PACKETBOX = 40;
    /** 体力更新 */
    public static final int S_OPCODE_HPUPDATE = 42;
    /** 物品资讯讯息 { 使用String-h.tbl } */
    public static final int S_OPCODE_IDENTIFYDESC = 43;
    /** 血盟小屋地图 [ 地点 ] */
    public static final int S_OPCODE_HOUSEMAP = 44;
    /** 增加魔法进魔法名单 */
    public static final int S_OPCODE_ADDSKILL = 48;
    /** 围城时间设定 */
    public static final int S_OPCODE_WARTIME = 49;
    /** 角色盟徽 */
    public static final int S_OPCODE_EMBLEM = 50;
    /** 登入状态 */
    public static final int S_OPCODE_LOGINRESULT = 51;
    /** 物件亮度 */
    public static final int S_OPCODE_LIGHT = 53;
    /** 布告栏( 讯息阅读 ) */
    public static final int S_OPCODE_BOARDREAD = 56;
    /** 物件隐形 & 现形 */
    public static final int S_OPCODE_INVIS = 57;
    /** 蓝色讯息 { 使用String-h.tbl } 红色字(地狱显示字) */
    public static final int S_OPCODE_BLUEMESSAGE = 59;
    /** 物品增加封包 */
    public static final int S_OPCODE_ADDITEM = 63;
    /** 布告栏 ( 讯息列表 ) */
    public static final int S_OPCODE_BOARD = 64;
    /** 角色皇冠 */
    public static final int S_OPCODE_CASTLEMASTER = 66;
    /** 魔法效果 : 防御颣 */
    public static final int S_OPCODE_SKILLICONSHIELD = 69;
    /** 税收设定封包 */
    public static final int S_OPCODE_TAXRATE = 72;
    /** 魔力更新 */
    public static final int S_OPCODE_MPUPDATE = 73;
    /** 一般聊天频道 */
    public static final int S_OPCODE_NORMALCHAT = 76;
    /** 交易封包 */
    public static final int S_OPCODE_TRADE = 77;
    /** 播放音效(客户端Sound文件夹内的.wav文件). */
    public static final int S_OPCODE_SOUND = 84;
    /** 增加交易物品封包 */
    public static final int S_OPCODE_TRADEADDITEM = 86;
    /** Ping time[Server] = 60618372. */
    protected static final int S_OPCODE_PINGTIME = 88;
    /** 学习魔法材料不足. */
    protected static final int S_OPCODE_ITEMERROR = 89;
    /** 画面中间出现红色讯息(登入来源)(Account has just logged in form). */
    protected static final int S_OPCODE_RED = 90;
    /** 魔法效果 : 中毒 { 编号 } */
    public static final int S_OPCODE_POISON = 93;
    /** 立即中断连线 */
    public static final int S_OPCODE_DISCONNECT = 95;
    /** 魔法动画 { 精准目标 } */
    public static final int S_OPCODE_TRUETARGET = 110;
    /** 产生动画 [ 地点 ] */
    public static final int S_OPCODE_EFFECTLOCATION = 112;
    /** 物件动作种类 ( 长时间 ) */
    public static final int S_OPCODE_CHARVISUALUPDATE = 113;
    /** 夜视功能 */
    public static final int S_OPCODE_ABILITY = 116;
    /** 产生对话视窗 */
    public static final int S_OPCODE_SHOWHTML = 119;
    /** 力量提升封包 */
    public static final int S_OPCODE_STRUP = 120;
    /** 经验值更新封包 */
    public static final int S_OPCODE_EXP = 121;
    /** 物件移动 */
    public static final int S_OPCODE_MOVEOBJECT = 122;
    /** 血盟战争讯息 { 编号, 血盟名称, 目标血盟名称 } */
    public static final int S_OPCODE_WAR = 123;
    /** 角色列表 */
    public static final int S_OPCODE_CHARAMOUNT = 126;
    /** 物品可用次数 */
    public static final int S_OPCODE_ITEMAMOUNT = 127;
    /** 物件血条 */
    public static final int S_OPCODE_HPMETER = 128;
    /** 宣告进入游戏 */
    public static final int S_OPCODE_UNKNOWN1 = 131;
    /** 非玩家聊天频道 { 一般 & 大喊 } NPC */
    public static final int S_OPCODE_NPCSHOUT = 133;
    /** 人物回硕检测 OR 传送锁定 ( 无动画 ) */
    public static final int S_OPCODE_TELEPORTLOCK = 135;
    /** 正义值更新 */
    public static final int S_OPCODE_LAWFUL = 140;
    /** 物件攻击 */
    public static final int S_OPCODE_ATTACKPACKET = 142;
    /** 物品状态 (祝福 & 诅咒) */
    public static final int S_OPCODE_ITEMCOLOR = 144;
    /** 角色属性与能力值 */
    public static final int S_OPCODE_OWNCHARSTATUS = 145;
    /** 物品删除 */
    public static final int S_OPCODE_DELETEINVENTORYITEM = 148;
    /** 魔法 | 物品效果 { 加速颣 } */
    public static final int S_OPCODE_SKILLHASTE = 149;
    /** 更新角色所在的地图 */
    public static final int S_OPCODE_MAPID = 150;
    /** 伺服器版本 */
    public static final int S_OPCODE_SERVERVERSION = 151;
    /** 角色创造失败 */
    public static final int S_OPCODE_NEWCHARWRONG = 153;
    /** 选项封包 { Yes | No } */
    public static final int S_OPCODE_YES_NO = 155;
    /** 初始化OpCode */
    public static final int S_OPCODE_INITOPCODE = 161;
    /** NPC外型改变 */
    public static final int S_OPCODE_POLY = 164;
    /** 魔法效果 : 诅咒类 { 编号 } 麻痹,瘫痪 */
    public static final int S_OPCODE_PARALYSIS = 165;
    /** NPC物品贩卖 */
    public static final int S_OPCODE_SHOWSHOPSELLLIST = 170;
    /** 魔法攻击力与魔法防御力 */
    public static final int S_OPCODE_SPMR = 174;
    /** 选择一个目标 */
    public static final int S_OPCODE_SELECTTARGET = 177;
    /** 物品名单 */
    public static final int S_OPCODE_INVLIST = 180;
    /** 角色资讯 */
    public static final int S_OPCODE_CHARLIST = 184;
    /** 物件删除 */
    public static final int S_OPCODE_REMOVE_OBJECT = 185;
    /** 角色个人商店 { 购买 } */
    public static final int S_OPCODE_PRIVATESHOPLIST = 190;
    /**
     * 血盟UI hjx1000
     */
    public static final int S_OPCODE_CLANMATCHING = 192;
    /** 游戏天气 */
    public static final int S_OPCODE_WEATHER = 193;
    /** 更新目前游戏时间 */
    public static final int S_OPCODE_GAMETIME = 194;
    /** 物品显示名称 */
    public static final int S_OPCODE_ITEMNAME = 195;
    /** 物件面向 */
    public static final int S_OPCODE_CHANGEHEADING = 199;
    /** 魔法 | 物品效果图示 { 勇敢药水颣 } */
    public static final int S_OPCODE_SKILLBRAVE = 200;
    /** 角色封号 */
    public static final int S_OPCODE_CHARTITLE = 202;
    /** 存入资金城堡宝库 (2) */
    public static final int S_OPCODE_DEPOSIT = 203;
    /** 改变物件名称 */
    public static final int S_OPCODE_CHANGENAME = 81;
    /** 损坏武器名单 */
    public static final int S_OPCODE_SELECTLIST = 208;
    /** 创造角色封包 */
    public static final int S_OPCODE_NEWCHARPACK = 212;
    /** 角色状态 (2) */
    public static final int S_OPCODE_OWNCHARSTATUS2 = 216;
    /** 物件动作种类 ( 短时间 ) */
    public static final int S_OPCODE_DOACTIONGFX = 218;
    /** 魔法购买 (金币) */
    public static final int S_OPCODE_SKILLBUY = 222;
    /** 取出城堡宝库金币 (1) */
    public static final int S_OPCODE_DRAWAL = 224;
    /** 物件复活 */
    public static final int S_OPCODE_RESURRECTION = 227;
    /** 产生动画 [ 物件 ] */
    public static final int S_OPCODE_SKILLSOUNDGFX = 232;
    /** 魔法效果 - 暗盲咒术 { 编号 } */
    public static final int S_OPCODE_CURSEBLIND = 238;
    /** 交易状态 */
    public static final int S_OPCODE_TRADESTATUS = 239;
    /** 仓库物品名单 */
    public static final int S_OPCODE_SHOWRETRIEVELIST = 250;
    /** 角色名称变紫色 */
    public static final int S_OPCODE_PINKNAME = 252;
    /** 拍卖公告栏选取金币数量 选取物品数量 */
    public static final int S_OPCODE_INPUTAMOUNT = 253;
    /** 物品购买 */
    public static final int S_OPCODE_SHOWSHOPBUYLIST = 254;
    /** 要求使用密语聊天频道 */
    public static final int S_OPCODE_WHISPERCHAT = 255;

    // //////////旧版没有的////////////////
    /** 丢弃物品封包 */
    // public static final int S_OPCODE_DROPITEM = 3;
    /** 动作自带魔法动画的范围魔法封包(无指定动画) */
    // public static final int S_OPCODE_NO_GFX_RANGE_ATTACK = 8;
    /** 界面宠物控制菜单消失 */
    // public static final int S_OPCODE_DELPETMENU = 33;
    /** 画面正中出现红色字(Account ? has just logged in form) */
    // public static final int S_OPCODE_REDMESSAGE = 90;
    /** 物品状态更新 */
    // public static final int S_OPCODE_ITEMSTATUS = 127;
}
