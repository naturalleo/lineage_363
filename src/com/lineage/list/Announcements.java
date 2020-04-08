package com.lineage.list;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.utils.StreamUtil;
import com.lineage.server.world.World;

/**
 * 公告事项
 * 
 * @author dexc
 * 
 */
public class Announcements {

    private static final Log _log = LogFactory.getLog(Announcements.class);

    private static Announcements _instance;

    private final ArrayList<String> _announcements = new ArrayList<String>();

    public static Announcements get() {
        if (_instance == null) {
            _instance = new Announcements();
        }

        return _instance;
    }

    public void load() {
        LineNumberReader lnr = null;

        try {
            final File file = new File("data/announcements.txt");
            final FileInputStream fileIn = new FileInputStream(file);
            final InputStreamReader inputStream = new InputStreamReader(fileIn,
                    "utf8");
            lnr = new LineNumberReader(inputStream);

            boolean isWhile = false;
            String line = null;
            while ((line = lnr.readLine()) != null) {
                if (!isWhile) {// 忽略第一行
                    isWhile = true;
                    continue;
                }
                if ((line.trim().length() == 0) || line.startsWith("#")) {
                    continue;
                }
                final StringTokenizer st = new StringTokenizer(line, "\r\n");
                final String info = st.nextToken();
                this._announcements.add(info);
            }

            _log.info("载入公告事项数量: " + this._announcements.size());

        } catch (final FileNotFoundException e) {
            _log.error(e.getLocalizedMessage(), e);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            StreamUtil.close(lnr);
        }
    }

    public void showAnnouncements(final L1PcInstance showTo) {
        for (final String msg : this._announcements) {
            showTo.sendPackets(new S_SystemMessage(msg));
        }
    }

    public void announceToAll(final String msg) {
        World.get().broadcastServerMessage(msg);
    }

    public ArrayList<String> list() {
        return this._announcements;
    }
}
