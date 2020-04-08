package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.lineage.list.Announcements;

/**
 * 服务器基础设置
 * 
 * @author dexc
 * 
 */
public final class Config {

    /** 伺服器执行登入器验证 */
    public static boolean LOGINS_TO_AUTOENTICATION;
    public static String RSA_KEY_E;
    public static String RSA_KEY_N;

    // /////////////////////////////////////////////
    /** 版本编号 */
    public static final String VER = "353.00.01";

    /** 客户端对应 */
    public static final String SRCVER = "Lineage3.5";

    public static final int SVer = 0x0734fd33;
    public static final int CVer = 0x0734fd30;
    public static final int AVer = 0x77cf6eba;
    public static final int NVer = 0x0734fd31;
    public static final int Time = (int) (System.currentTimeMillis() / 1000L);

    /** 除错模式 */
    public static boolean DEBUG = false;

    /** 伺服器编号 */
    public static int SERVERNO;

    /** 作业系统是UBUNTU */
    public static boolean ISUBUNTU = false;

    /** 伺服器位置 */
    public static String GAME_SERVER_HOST_NAME;

    /** 伺服器端口 */
    // 服务器监听端口以"-"减号分隔 允许设置多个(允许设置一个)
    public static String GAME_SERVER_PORT;

    /** 服务器名称 */
    public static String SERVERNAME;

    /** 广播伺服器位置 */
    public static String CHAT_SERVER_HOST_NAME;

    /** 广播伺服器端口 */
    public static int CHAT_SERVER_PORT;

    /** 时区设置 */
    public static String TIME_ZONE;

    /** 伺服器语系 */
    public static int CLIENT_LANGUAGE;

    /** 伺服器语系字串源 */
    public static String CLIENT_LANGUAGE_CODE;

    /** 伺服器语系定位阵列 */
    public static String[] LANGUAGE_CODE_ARRAY = { "UTF8", "EUCKR", "UTF8",
            "BIG5", "SJIS", "GBK" };

    /** 重新启动时间设置 */
    public static String[] AUTORESTART = null;

    /** 允许自动注册 */
    public static boolean AUTO_CREATE_ACCOUNTS;

    /** 允许最大玩家 */
    public static short MAX_ONLINE_USERS = 10;

    /** 人物资料自动保存时间 */
    public static int AUTOSAVE_INTERVAL;

    /** 人物背包自动保存时间 */
    public static int AUTOSAVE_INTERVAL_INVENTORY;

    /** 客户端接收信息范围 (-1为画面内可见) */
    public static int PC_RECOGNIZE_RANGE;

    /** 端口重置时间(单位:分钟) */
    public static int RESTART_LOGIN;

    /** 是否显示公告 */
    public static boolean NEWS;

    /** 伺服器素质选取方式 0:玩家自选 1:骰子随机点 */
    // public static int POWER = 0;

    private static final String SERVER_CONFIG_FILE = "./config/server.properties";

    public static void load() throws ConfigErrorException {
        // TODO 伺服器捆绑
        Properties pack = new Properties();
        try {
            InputStream is = new FileInputStream(new File(
                    "./config/pack.properties"));
            pack.load(is);
            is.close();
            LOGINS_TO_AUTOENTICATION = Boolean.parseBoolean(pack.getProperty(
                    "Autoentication", "false"));
            RSA_KEY_E = pack.getProperty("RSA_KEY_E", "0");
            RSA_KEY_N = pack.getProperty("RSA_KEY_N", "0");

        } catch (final Exception e) {
            System.err.println("没有找到登入器加密设置档案: ./config/pack.properties");

        } finally {
            pack.clear();
        }

        // _log.info("载入服务器基础设置!");
        final Properties set = new Properties();
        try {
            final InputStream is = new FileInputStream(new File(
                    SERVER_CONFIG_FILE));
            set.load(is);
            is.close();

            // 伺服器编号
            SERVERNO = Integer.parseInt(set.getProperty("ServerNo", "1"));

            // 通用
            GAME_SERVER_HOST_NAME = set.getProperty("GameserverHostname", "*");

            // 服务器监听端口以"-"减号分隔 允许设置多个(允许设置一个)
            GAME_SERVER_PORT = set.getProperty("GameserverPort", "2000-2001");

            // 语系
            CLIENT_LANGUAGE = Integer.parseInt(set.getProperty(
                    "ClientLanguage", "3"));

            CLIENT_LANGUAGE_CODE = LANGUAGE_CODE_ARRAY[CLIENT_LANGUAGE];

            String tmp = set.getProperty("AutoRestart", "");
            if (!tmp.equalsIgnoreCase("null")) {
                AUTORESTART = tmp.split(",");
            }

            TIME_ZONE = set.getProperty("TimeZone", "CST");

            AUTO_CREATE_ACCOUNTS = Boolean.parseBoolean(set.getProperty(
                    "AutoCreateAccounts", "true"));

            MAX_ONLINE_USERS = Short.parseShort(set.getProperty(
                    "MaximumOnlineUsers", "30"));

            AUTOSAVE_INTERVAL = Integer.parseInt(
                    set.getProperty("AutosaveInterval", "1200"), 10);

            AUTOSAVE_INTERVAL /= 60;
            if (AUTOSAVE_INTERVAL <= 0) {
                AUTOSAVE_INTERVAL = 20;
            }

            AUTOSAVE_INTERVAL_INVENTORY = Integer.parseInt(
                    set.getProperty("AutosaveIntervalOfInventory", "300"), 10);

            AUTOSAVE_INTERVAL_INVENTORY /= 60;
            if (AUTOSAVE_INTERVAL_INVENTORY <= 0) {
                AUTOSAVE_INTERVAL_INVENTORY = 5;
            }

            PC_RECOGNIZE_RANGE = Integer.parseInt(set.getProperty(
                    "PcRecognizeRange", "13"));

            // SEND_PACKET_BEFORE_TELEPORT =
            // Boolean.parseBoolean(set.getProperty("SendPacketBeforeTeleport",
            // "false"));

            RESTART_LOGIN = Integer.parseInt(set.getProperty("restartlogin",
                    "30"));

            NEWS = Boolean.parseBoolean(set.getProperty("News", "false"));

            // POWER = Integer.parseInt(set.getProperty("power", "0"));

            if (NEWS) {
                Announcements.get().load();
            }

        } catch (final Exception e) {
            throw new ConfigErrorException("设置档案遗失: " + SERVER_CONFIG_FILE);

        } finally {
            set.clear();
        }
    }
}
