package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.serverpackets.S_BoxMessage;
import com.lineage.server.world.World;

public class ConfigBoxMsg {

    private static final Log _log = LogFactory.getLog(ConfigBoxMsg.class);

    private static final Map<Integer, String> _box_msg_list = new HashMap<Integer, String>();

    private static final Random _random = new Random();

    public static boolean ISMSG = false;

    private static final String _box_text = "./config/box_desc.txt";

    public static void load() throws ConfigErrorException {
        try {
            // 取回档案
            final InputStream is = new FileInputStream(new File(_box_text));
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
                if (desc.startsWith("ISMSG")) {
                    desc = desc.replaceAll(" ", "");// 取代空白
                    ISMSG = Boolean.parseBoolean(desc.substring(6));

                } else {
                    _box_msg_list.put(new Integer(i++), desc);
                }
            }

            is.close();
            isr.close();
            lnr.close();

        } catch (final Exception e) {
            _log.error("设置档案遗失: " + _box_text);
        }
    }

    public static void msg(final String string1, final String string2,
            final String string3) {
        try {
            final String msg = _box_msg_list.get(_random.nextInt(_box_msg_list
                    .size()) + 1);
            if (msg != null) {
                final String out = String
                        .format(msg, string1, string2, string3);
                // 宝物公告
                World.get().broadcastPacketToAll(new S_BoxMessage(out));
                //World.get().broadcastPacketToAll(new S_BlueMessage(950, ": \\f2"+out, "宝物公告"));
                //System.out.println(out);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
