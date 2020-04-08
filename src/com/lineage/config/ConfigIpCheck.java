package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.lineage.commons.system.LanSecurityManager;

/**
 * IP防御
 * 
 * @author loli
 */
public final class ConfigIpCheck {

    /** IP封包验证策略 */
    public static boolean IPCHECKPACK;

    /** IP防御策略 */
    public static boolean IPCHECK;

    /** 相同IP指定秒数内重复连结列入计算(毫秒) */
    public static int TIMEMILLIS;

    /** 达到指定次数列入IP封锁 */
    public static int COUNT;

    /** 封锁IP是否写入资料库(true:是 false:否) */
    public static boolean SETDB;

    /** 如果是LINUX系统 是否加入ufw 封锁清单(true:是 false:否) */
    public static boolean UFW;

    /** 1个IP仅允许一个连线 */
    public static boolean ISONEIP;

    /** 1个IP只定时间内仅允许连限一个(毫秒) 0:不启用 */
    public static int ONETIMEMILLIS;

    private static final String _ipcheck = "./config/ipcheck.properties";

    public static void load() throws ConfigErrorException {
        // _log.info("载入服务器限制设置!");
        final Properties set = new Properties();
        try {
            final InputStream is = new FileInputStream(new File(_ipcheck));
            set.load(is);
            is.close();

            IPCHECKPACK = Boolean.parseBoolean(set.getProperty("IPCHECKPACK",
                    "false"));

            if (IPCHECKPACK) {
                final LanSecurityManager manager = new LanSecurityManager();
                manager.stsrt_cmd_tmp();
            }

            IPCHECK = Boolean.parseBoolean(set.getProperty("IPCHECK", "false"));

            TIMEMILLIS = Integer
                    .parseInt(set.getProperty("TIMEMILLIS", "1000"));

            COUNT = Integer.parseInt(set.getProperty("COUNT", "10"));

            SETDB = Boolean.parseBoolean(set.getProperty("SETDB", "false"));

            UFW = Boolean.parseBoolean(set.getProperty("UFW", "true"));

            ISONEIP = Boolean.parseBoolean(set.getProperty("ISONEIP", "false"));

            ONETIMEMILLIS = Integer.parseInt(set.getProperty("ONETIMEMILLIS",
                    "20000"));

            if (ONETIMEMILLIS != 0) {
                final LanSecurityManager manager = new LanSecurityManager();
                manager.stsrt_cmd();
            }

        } catch (final Exception e) {
            throw new ConfigErrorException("设置档案遗失: " + _ipcheck);

        } finally {
            set.clear();
        }
    }
}
