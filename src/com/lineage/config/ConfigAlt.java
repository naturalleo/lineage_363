package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Properties;

/**
 * 服务器限制设置
 * 
 * @author dexc
 * 
 */
public final class ConfigAlt {

    /** AltSettings control */
    public static short GLOBAL_CHAT_LEVEL;

    public static short WHISPER_CHAT_LEVEL;

    /** 设定自动取得道具的方式0-掉落地上, 1-掉落宠物身上, 2-掉落角色身上 */
    public static byte AUTO_LOOT;

    public static int LOOTING_RANGE;

    /** 允许pk */
    public static boolean ALT_NONPVP;

    /** PK处分 */
    public static boolean ALT_PUNISHMENT;

    /** 攻击资讯 */
    public static boolean ALT_ATKMSG;

    /** 联盟系统开关 */
    public static boolean CLAN_ALLIANCE;

    /** 地面物品自动删除时间(单位:分钟) */
    public static int ALT_ITEM_DELETION_TIME;

    // 指定范围内有人物不执行清除 2012-05-07 异动为可见范围内有玩家不执行清除
    // public static int ALT_ITEM_DELETION_RANGE;

    public static boolean ALT_WHO_COMMANDX;

    public static int ALT_WHO_TYPE;

    public static double ALT_WHO_COUNT;

    public static int ALT_WAR_TIME;

    public static int ALT_WAR_TIME_UNIT;

    public static int ALT_WAR_INTERVAL;

    public static int ALT_WAR_INTERVAL_UNIT;

    public static int ALT_RATE_OF_DUTY;

    public static boolean SPAWN_HOME_POINT;

    public static int SPAWN_HOME_POINT_RANGE;

    public static int SPAWN_HOME_POINT_COUNT;

    public static int SPAWN_HOME_POINT_DELAY;

    public static int ELEMENTAL_STONE_AMOUNT;

    public static int HOUSE_TAX_INTERVAL;

    /** 最大娃娃携带量 */
    public static int MAX_DOLL_COUNT;

    /** NPC(Summon, Pet)背包最大容量 */
    public static int MAX_NPC_ITEM;

    /** 个人仓库容许收容数量 */
    public static int MAX_PERSONAL_WAREHOUSE_ITEM;

    /** 血盟仓库容许收容数量 */
    public static int MAX_CLAN_WAREHOUSE_ITEM;

    /** 指定删除保留7天时间等级 */
    public static int DELETE_CHARACTER_AFTER_LV;

    /** 指定等级删除保留7天时间 */
    public static boolean DELETE_CHARACTER_AFTER_7DAYS;

    /** NPC死亡后删除时间(单位:秒) */
    public static int NPC_DELETION_TIME;

    /** 初始可建立人物数量 */
    public static int DEFAULT_CHARACTER_SLOT;

    /** 万能药使用限制(数量) */
    public static int MEDICINE;

    /** 能力质上限 */
    public static int POWER;

    /** 万能药使用限制(能力质上限) */
    public static int POWERMEDICINE;

    /** 是否允许丢弃道具至地面true允许 false不允许 */
    public static boolean DORP_ITEM;

    /** 最大拖怪数量 */
    public static final int MAX_NPC = 35;

    private static final String ALT_SETTINGS_FILE = "./config/altsettings.properties";

    public static void load() throws ConfigErrorException {
        // _log.info("载入服务器限制设置!");
        final Properties set = new Properties();
        try {
            final InputStream is = new FileInputStream(new File(
                    ALT_SETTINGS_FILE));
            // 指定档案编码
            final InputStreamReader isr = new InputStreamReader(is, "utf-8");
            set.load(isr);
            is.close();

            GLOBAL_CHAT_LEVEL = Short.parseShort(set.getProperty(
                    "GlobalChatLevel", "30"));

            WHISPER_CHAT_LEVEL = Short.parseShort(set.getProperty(
                    "WhisperChatLevel", "5"));

            AUTO_LOOT = Byte.parseByte(set.getProperty("AutoLoot", "2"));

            LOOTING_RANGE = Integer.parseInt(set.getProperty("LootingRange",
                    "3"));

            ALT_NONPVP = Boolean
                    .parseBoolean(set.getProperty("NonPvP", "true"));

            ALT_PUNISHMENT = Boolean.parseBoolean(set.getProperty("Punishment",
                    "true"));

            CLAN_ALLIANCE = Boolean.parseBoolean(set.getProperty(
                    "ClanAlliance", "true"));

            ALT_ITEM_DELETION_TIME = Integer.parseInt(set.getProperty(
                    "ItemDeletionTime", "10"));
            if (ALT_ITEM_DELETION_TIME > 60) {
                ALT_ITEM_DELETION_TIME = 60;// 最大设置60分钟
            }

            // ALT_ITEM_DELETION_RANGE =
            // Integer.parseInt(set.getProperty("ItemDeletionRange", "5"));

            ALT_WHO_COMMANDX = Boolean.parseBoolean(set.getProperty(
                    "WhoCommandx", "false"));

            // WHO 显示 额外设置方式 0:对话视窗显示 1:视窗显示
            // 这一项设置必须在WhoCommandx = true才有作用
            ALT_WHO_TYPE = Integer.parseInt(set.getProperty("Who_type", "0"));

            ALT_WHO_COUNT = Double.parseDouble(set.getProperty(
                    "WhoCommandcount", "1.0"));
            if (ALT_WHO_COUNT < 1.0) {
                ALT_WHO_COUNT = 1.0;
            }

            String strWar;
            strWar = set.getProperty("WarTime", "2h");
            if (strWar.indexOf("d") >= 0) {
                ALT_WAR_TIME_UNIT = Calendar.DATE;
                strWar = strWar.replace("d", "");

            } else if (strWar.indexOf("h") >= 0) {
                ALT_WAR_TIME_UNIT = Calendar.HOUR_OF_DAY;
                strWar = strWar.replace("h", "");

            } else if (strWar.indexOf("m") >= 0) {
                ALT_WAR_TIME_UNIT = Calendar.MINUTE;
                strWar = strWar.replace("m", "");
            }

            ALT_WAR_TIME = Integer.parseInt(strWar);
            strWar = set.getProperty("WarInterval", "4d");
            if (strWar.indexOf("d") >= 0) {
                ALT_WAR_INTERVAL_UNIT = Calendar.DATE;
                strWar = strWar.replace("d", "");

            } else if (strWar.indexOf("h") >= 0) {
                ALT_WAR_INTERVAL_UNIT = Calendar.HOUR_OF_DAY;
                strWar = strWar.replace("h", "");

            } else if (strWar.indexOf("m") >= 0) {
                ALT_WAR_INTERVAL_UNIT = Calendar.MINUTE;
                strWar = strWar.replace("m", "");
            }

            ALT_WAR_INTERVAL = Integer.parseInt(strWar);

            SPAWN_HOME_POINT = Boolean.parseBoolean(set.getProperty(
                    "SpawnHomePoint", "true"));

            SPAWN_HOME_POINT_COUNT = Integer.parseInt(set.getProperty(
                    "SpawnHomePointCount", "2"));

            SPAWN_HOME_POINT_DELAY = Integer.parseInt(set.getProperty(
                    "SpawnHomePointDelay", "100"));

            SPAWN_HOME_POINT_RANGE = Integer.parseInt(set.getProperty(
                    "SpawnHomePointRange", "8"));

            ELEMENTAL_STONE_AMOUNT = Integer.parseInt(set.getProperty(
                    "ElementalStoneAmount", "300"));

            HOUSE_TAX_INTERVAL = Integer.parseInt(set.getProperty(
                    "HouseTaxInterval", "10"));

            MAX_DOLL_COUNT = Integer.parseInt(set.getProperty("MaxDollCount",
                    "1"));

            MAX_NPC_ITEM = Integer.parseInt(set.getProperty("MaxNpcItem", "8"));

            MAX_PERSONAL_WAREHOUSE_ITEM = Integer.parseInt(set.getProperty(
                    "MaxPersonalWarehouseItem", "100"));

            MAX_CLAN_WAREHOUSE_ITEM = Integer.parseInt(set.getProperty(
                    "MaxClanWarehouseItem", "200"));

            DELETE_CHARACTER_AFTER_LV = Integer.parseInt(set.getProperty(
                    "DeleteCharacterAfterLV", "60"));

            DELETE_CHARACTER_AFTER_7DAYS = Boolean.parseBoolean(set
                    .getProperty("DeleteCharacterAfter7Days", "True"));

            NPC_DELETION_TIME = Integer.parseInt(set.getProperty(
                    "NpcDeletionTime", "10"));

            DEFAULT_CHARACTER_SLOT = Integer.parseInt(set.getProperty(
                    "DefaultCharacterSlot", "4"));

            MEDICINE = Integer.parseInt(set.getProperty("Medicine", "20"));

            POWER = Integer.parseInt(set.getProperty("Power", "35"));

            POWERMEDICINE = Integer.parseInt(set.getProperty("MedicinePower",
                    "45"));

            DORP_ITEM = Boolean.parseBoolean(set
                    .getProperty("dorpitem", "true"));

        } catch (final Exception e) {
            throw new ConfigErrorException("设置档案遗失: " + ALT_SETTINGS_FILE);

        } finally {
            set.clear();
        }
    }
}
