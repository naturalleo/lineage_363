package com.lineage.server.model.npc.action;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

public class L1NpcActionFactory {

    private static final Log _log = LogFactory.getLog(L1NpcActionFactory.class);

    private static Map<String, Constructor<?>> _actions = new HashMap<String, Constructor<?>>();

    private static Constructor<?> loadConstructor(final Class<?> c)
            throws NoSuchMethodException {
        return c.getConstructor(new Class[] { Element.class });
    }

    static {
        try {
            _actions.put("Action", loadConstructor(L1NpcListedAction.class));
            _actions.put("MakeItem", loadConstructor(L1NpcMakeItemAction.class));
            _actions.put("ShowHtml", loadConstructor(L1NpcShowHtmlAction.class));
            _actions.put("SetQuest", loadConstructor(L1NpcSetQuestAction.class));
            _actions.put("Teleport", loadConstructor(L1NpcTeleportAction.class));

        } catch (final NoSuchMethodException e) {
            _log.error("NpcAction加载失败", e);
        }
    }

    public static L1NpcAction newAction(final Element element) {
        try {
            final Constructor<?> con = _actions.get(element.getNodeName());
            L1NpcAction action = (L1NpcAction) con.newInstance(element);
            return action;

        } catch (final NullPointerException e) {
            _log.error("未定义的NPC对话设置", e);

        } catch (final Exception e) {
            _log.error("NpcAction加载失败", e);

        }
        return null;
    }
}
