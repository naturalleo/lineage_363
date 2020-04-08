package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 服务器活动设置
 * 
 * @author dexc
 * 
 */
public final class ConfigOther {

    public static boolean SPEED = false;// 启用加速检测
    
    public static boolean DETECATION = false;// 检测外挂状态 hjx1000
    
    public static int RND_TIME = 60;// 检测外挂状态 hjx1000
    
    public static int XING_COUNT = 3;// 开放装备强化星数 hjx1000
    
    public static boolean CITY_WAR_LOSS = false; //城堡战争损失经验

    public static double SPEED_TIME = 1.2D;// 允许速率范围质

    public static boolean KILLRED = true;// 怪物是否主动攻击红人

    public static int RATE_XP_WHO = 1;

    public static boolean CLANDEL;// 允许盟组解散血盟

    public static boolean CLANTITLE;// 允许盟员自行建立封号

    public static int CLANCOUNT;// 自行建立血盟人数上限

    public static boolean LIGHT;// 启用人物全时光照(true启用 false关闭)

    public static boolean HPBAR;// 显示怪物血条

    public static boolean SHOPINFO;// 一般商店是否显示详细资讯

    public static int HOMEHPR;// 血盟小屋HP恢复增加

    public static int HOMEMPR;// 血盟小屋MP恢复增加

    public static boolean WAR_DOLL;// 攻城旗帜内是否允许携带娃娃 true:允许 false:禁止

    public static int SET_GLOBAL;// 广播扣除金币或是饱食度(0:饱食度 1:金币)

    public static int SET_GLOBAL_COUNT;// 广播扣除质(set_global设置0:扣除饱食度量
                                       // set_global设置1:扣除金币量)

    public static int SET_GLOBAL_TIME;// 广播/买卖频道间隔秒数

    // 战斗特化遭遇的守护等级
    // 设置等级以下角色 被超过10级以上的玩家攻击而死亡时，不会失去经验值，也不会掉落物品
    public static int ENCOUNTER_LV;

    private static final String LIANG = "./config/other.properties";

    public static void load() throws ConfigErrorException {
        final Properties set = new Properties();
        try {
            final InputStream is = new FileInputStream(new File(LIANG));
            set.load(is);
            is.close();

            SPEED = Boolean.parseBoolean(set.getProperty("speed", "false"));
            
            DETECATION = Boolean.parseBoolean(set.getProperty("detection", "false"));
            
            RND_TIME = Integer.parseInt(set.getProperty("rnd_time", "60"));
            
            XING_COUNT = Integer.parseInt(set.getProperty("xing_count", "3"));
            
            CITY_WAR_LOSS = Boolean.parseBoolean(set.getProperty("City_War_Loss", 
            		"false"));

            SPEED_TIME = Double.parseDouble(set
                    .getProperty("speed_time", "1.2"));

            ENCOUNTER_LV = Integer.parseInt(set.getProperty("encounter_lv",
                    "20"));

            KILLRED = Boolean
                    .parseBoolean(set.getProperty("kill_red", "false"));

            RATE_XP_WHO = Integer.parseInt(set.getProperty("rate_xp_who", "1"));

            CLANDEL = Boolean
                    .parseBoolean(set.getProperty("clanadel", "false"));

            CLANTITLE = Boolean.parseBoolean(set.getProperty("clanatitle",
                    "false"));

            CLANCOUNT = Integer.parseInt(set.getProperty("clancount", "100"));

            // 启用人物全时光照(true启用 false关闭)
            LIGHT = Boolean.parseBoolean(set.getProperty("light", "false"));

            // 显示怪物血条(true启用 false关闭)
            HPBAR = Boolean.parseBoolean(set.getProperty("hpbar", "false"));

            SHOPINFO = Boolean.parseBoolean(set
                    .getProperty("shopinfo", "false"));

            HOMEHPR = Integer.parseInt(set.getProperty("homehpr", "100"));

            HOMEMPR = Integer.parseInt(set.getProperty("homempr", "100"));

            SET_GLOBAL = Integer.parseInt(set.getProperty("set_global", "100"));

            SET_GLOBAL_COUNT = Integer.parseInt(set.getProperty(
                    "set_global_count", "100"));

            SET_GLOBAL_TIME = Integer.parseInt(set.getProperty(
                    "set_global_time", "5"));

            WAR_DOLL = Boolean
                    .parseBoolean(set.getProperty("war_doll", "true"));

        } catch (final Exception e) {
            throw new ConfigErrorException("设置档案遗失: " + LIANG);

        } finally {
            set.clear();
        }
    }
}
