package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigKill {

    private static final Log _log = LogFactory.getLog(ConfigKill.class);

    public static final Map<Integer, String> KILL_TEXT_LIST = new HashMap<Integer, String>();

    public static int KILLLEVEL = 90;// 公告等级设置

    private static final String _kill_text = "./config/kill_desc.txt";

    public static void load() throws ConfigErrorException {
        try {
            // 取回档案
            final InputStream is = new FileInputStream(new File(_kill_text));
            // 指定档案编码
            final InputStreamReader isr = new InputStreamReader(is, "utf-8");
            final LineNumberReader lnr = new LineNumberReader(isr);

            boolean isWhile = false;
            int i = 1;
            String desc = null;
            while ((desc = lnr.readLine()) != null) {
                if (!isWhile) {// 忽略第一行
                    isWhile = true;
                    continue;
                }
                if ((desc.trim().length() == 0) || desc.startsWith("#")) {
                    continue;
                }
                if (desc.startsWith("KILLLEVEL")) {
                    desc = desc.replaceAll(" ", "");// 取代空白
                    KILLLEVEL = Integer.parseInt(desc.substring(10));

                } else {
                    KILL_TEXT_LIST.put(new Integer(i++), desc);
                }
            }

            is.close();
            isr.close();
            lnr.close();

        } catch (final Exception e) {
            _log.error("设置档案遗失: " + _kill_text);
        }
    }
}
