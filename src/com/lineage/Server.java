package com.lineage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.LogManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import com.lineage.commons.system.LanSecurityManager;
import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigBad;
import com.lineage.config.ConfigBoxMsg;
import com.lineage.config.ConfigCharSetting;
import com.lineage.config.ConfigDescs;
import com.lineage.config.ConfigIpCheck;
import com.lineage.config.ConfigKill;
import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigRate;
import com.lineage.config.ConfigRecord;
import com.lineage.config.ConfigSQL;
import com.lineage.server.GameServer;

/**
 * 服务器启动
 */
public class Server {

    private static final String LOG_PROP = "./config/logging.properties";

    private static final String LOG_4J = "./config/log4j.properties";

    private static final String _loginfo = "./loginfo";

    private static final String _back = "./back";

    /**
     * MAIN
     * 
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {

        Calendar date = Calendar.getInstance();
        // 1=周日 7=周六
        System.out.println(">>>>" + date.get(Calendar.DAY_OF_WEEK));
        System.out.println(">>>>" + date.get(Calendar.HOUR_OF_DAY));

        // 测试用核心
        try {
            // 在核心启动命令后面加上 test 可以用来作显示测试
            if (args[0].equalsIgnoreCase("test")) {
                Config.DEBUG = true;
            }

        } catch (final Exception e) {
            // e.printStackTrace();
        }

        // 压缩旧档案
        final CompressFile bean = new CompressFile();
        try {
            // 建立备份用资料夹
            final File file = new File(_back);
            if (!file.exists()) {
                file.mkdir();
            }

            final String nowDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
                    .format(new Date());
            bean.zip(_loginfo, "./back/" + nowDate + ".zip");

            final File loginfofile = new File(_loginfo);
            final String[] loginfofileList = loginfofile.list();
            for (final String fileName : loginfofileList) {
                final File readfile = new File(_loginfo + "/" + fileName);
                if (readfile.exists() && !readfile.isDirectory()) {
                    readfile.delete();
                }
            }

        } catch (final IOException e) {
            System.out.println("资料夹不存在: " + _back + " 已经自动建立!");
        }

        boolean error = false;

        try {
            final InputStream is = new BufferedInputStream(new FileInputStream(
                    LOG_PROP));
            LogManager.getLogManager().readConfiguration(is);
            is.close();

        } catch (final IOException e) {
            System.out.println("档案遗失: " + LOG_PROP);
            error = true;
        }

        try {
            PropertyConfigurator.configure(LOG_4J);

        } catch (final Exception e) {
            System.out.println("档案遗失: " + LOG_4J);
            System.exit(0);
        }

        try {
            Config.load();
            ConfigAlt.load();
            ConfigCharSetting.load();
            ConfigOther.load();
            ConfigRate.load();
            ConfigSQL.load();
            ConfigRecord.load();

            ConfigDescs.load();
            ConfigBad.load();
            ConfigKill.load();
            ConfigIpCheck.load();
            ConfigBoxMsg.load();

        } catch (final Exception e) {
            System.out.println("CONFIG 资料加载异常!" + e);
            error = true;
        }

        final Log log = LogFactory.getLog(Server.class);

        final String infoX = "\n\r##################################################"
                + "\n\r       服务器 (核心版本:"
                + Config.VER
                + "/"
                + Config.SRCVER
                + ")"
                + "\n\r##################################################";

        log.info(infoX);

        final File file = new File("./jar");
        final String[] fileNameList = file.list();

        for (final String fileName : fileNameList) {
            final File readfile = new File(fileName);
            if (!readfile.isDirectory()) {
                log.info("加载引用JAR: " + fileName);
            }
        }

        if (error) {
            System.exit(0);
        }

        // log.info("讯息辨识(色彩涵义): [INFO]资讯");
        // log.debug("讯息辨识(色彩涵义): [DEBUG]除错");
        // log.warn("讯息辨识(色彩涵义): [WARN]警告");
        // log.error("讯息辨识(色彩涵义): [ERROR]错误");
        // log.fatal("讯息辨识(色彩涵义): [FATAL]严重错误");

        // SQL读取初始化
        DatabaseFactoryLogin.setDatabaseSettings();
        DatabaseFactory.setDatabaseSettings();

        DatabaseFactoryLogin.get();
        DatabaseFactory.get();

        // 安全管理器
        final LanSecurityManager securityManager = new LanSecurityManager();
        System.setSecurityManager(securityManager);
        log.info("加载 安全管理器: LanSecurityManager");

        final String osname = System.getProperties().getProperty("os.name");
        if (osname.lastIndexOf("Linux") != -1) {
            Config.ISUBUNTU = true;
        }

        GameServer.getInstance().initialize();
    }
}
