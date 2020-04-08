package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 服务器储存设置
 * 
 * @author dexc
 * 
 */
public final class ConfigRecord {

    // 防具 武器 强化失败纪录
    public static boolean LOGGING_BAN_ENCHANT = false;

    // 对话纪录
    public static boolean LOGGING_CHAT_NORMAL = false;

    public static boolean LOGGING_CHAT_SHOUT = false;

    public static boolean LOGGING_CHAT_WORLD = false;

    public static boolean LOGGING_CHAT_CLAN = false;

    public static boolean LOGGING_CHAT_WHISPER = false;

    public static boolean LOGGING_CHAT_PARTY = false;

    public static boolean LOGGING_CHAT_BUSINESS = false;

    public static boolean LOGGING_CHAT_COMBINED = false;

    public static boolean LOGGING_CHAT_CHAT_PARTY = false;

    private static final String RECORD_FILE = "./config/record.properties";

    public static void load() throws ConfigErrorException {
        // _log.info("载入服务器储存设置!");
        final Properties set = new Properties();
        try {
            final InputStream is = new FileInputStream(new File(RECORD_FILE));
            set.load(is);
            is.close();

            LOGGING_BAN_ENCHANT = Boolean.parseBoolean(set.getProperty(
                    "LoggingBanEnchant", "false"));

            LOGGING_CHAT_NORMAL = Boolean.parseBoolean(set.getProperty(
                    "LoggingChatNormal", "false"));

            LOGGING_CHAT_SHOUT = Boolean.parseBoolean(set.getProperty(
                    "LoggingChatShout", "false"));

            LOGGING_CHAT_WORLD = Boolean.parseBoolean(set.getProperty(
                    "LoggingChatWorld", "false"));

            LOGGING_CHAT_CLAN = Boolean.parseBoolean(set.getProperty(
                    "LoggingChatClan", "false"));

            LOGGING_CHAT_WHISPER = Boolean.parseBoolean(set.getProperty(
                    "LoggingChatWhisper", "false"));

            LOGGING_CHAT_PARTY = Boolean.parseBoolean(set.getProperty(
                    "LoggingChatParty", "false"));

            LOGGING_CHAT_BUSINESS = Boolean.parseBoolean(set.getProperty(
                    "LoggingBusiness", "false"));

            LOGGING_CHAT_COMBINED = Boolean.parseBoolean(set.getProperty(
                    "LoggingChatCombined", "false"));

            LOGGING_CHAT_CHAT_PARTY = Boolean.parseBoolean(set.getProperty(
                    "LoggingChatChatParty", "false"));

        } catch (final Exception e) {
            throw new ConfigErrorException("设置档案遗失: " + RECORD_FILE);

        } finally {
            set.clear();
        }
    }
}
