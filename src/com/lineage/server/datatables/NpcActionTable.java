package com.lineage.server.datatables;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.npc.action.L1NpcAction;
import com.lineage.server.model.npc.action.L1NpcXmlParser;
import com.lineage.server.utils.FileUtil;
import com.lineage.server.utils.PerformanceTimer;

/**
 * NPC XML对话结果资料
 * 
 * @author dexc
 * 
 */
public class NpcActionTable {

    private static final Log _log = LogFactory.getLog(LightSpawnTable.class);

    private static NpcActionTable _instance;

    private static final List<L1NpcAction> _actions = new ArrayList<L1NpcAction>();

    private static final List<L1NpcAction> _talkActions = new ArrayList<L1NpcAction>();

    private List<L1NpcAction> loadAction(final File file, final String nodeName)

    throws ParserConfigurationException, SAXException, IOException {
        final DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();
        final Document doc = builder.parse(file);

        if (!doc.getDocumentElement().getNodeName().equalsIgnoreCase(nodeName)) {
            return new ArrayList<L1NpcAction>();
        }
        return L1NpcXmlParser.listActions(doc.getDocumentElement());
    }

    private void loadAction(final File file) throws Exception {
        _actions.addAll(this.loadAction(file, "NpcActionList"));
    }

    private void loadTalkAction(final File file) throws Exception {
        _talkActions.addAll(this.loadAction(file, "NpcTalkActionList"));
    }

    private void loadDirectoryActions(final File dir) throws Exception {
        for (final String file : dir.list()) {
            final File f = new File(dir, file);
            if (FileUtil.getExtension(f).equalsIgnoreCase("xml")) {
                this.loadAction(f);
                this.loadTalkAction(f);
            }
        }
    }

    private NpcActionTable() throws Exception {
        /*
         * final File usersDir = new File("./data/xml/NpcActions/users/"); if
         * (usersDir.exists()) { this.loadDirectoryActions(usersDir); }
         */
        this.loadDirectoryActions(new File("./data/xml/NpcActions/"));
    }

    public static void load() {
        try {
            final PerformanceTimer timer = new PerformanceTimer();
            _instance = new NpcActionTable();
            _log.info("载入NPC XML对话结果资料 (" + timer.get() + "ms)");

            /*
             * for (L1NpcAction action : _actions) { if (action instanceof
             * L1NpcXmlAction) { L1NpcXmlAction aaa = (L1NpcXmlAction)action; if
             * (aaa.acceptsRequest(((L1NpcXmlAction) action)._name, null, null))
             * { aaa.execute(aaa._name, aaa._npcids); } } }//
             */
        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            System.exit(0);
        }
    }

    public static NpcActionTable getInstance() {
        return _instance;
    }

    public L1NpcAction get(final String actionName, final L1PcInstance pc,
            final L1Object obj) {
        // System.out.println("getLevel:" + pc.getLevel() + " actionName:" +
        // actionName+ " obj:" + obj.getId());
        for (final L1NpcAction action : _actions) {
            if (action.acceptsRequest(actionName, pc, obj)) {
                return action;
            }
        }
        return null;
    }

    public L1NpcAction get(final L1PcInstance pc, final L1Object obj) {
        for (final L1NpcAction action : _talkActions) {
            if (action.acceptsRequest("", pc, obj)) {
                return action;
            }
        }
        return null;
    }
}
