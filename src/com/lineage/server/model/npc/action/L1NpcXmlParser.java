/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.lineage.server.model.npc.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.lineage.server.model.L1PcQuest;
import com.lineage.server.utils.IterableElementList;

public class L1NpcXmlParser {
    public static List<L1NpcAction> listActions(final Element element) {
        final List<L1NpcAction> result = new ArrayList<L1NpcAction>();
        final NodeList list = element.getChildNodes();
        for (final Element elem : new IterableElementList(list)) {
            final L1NpcAction action = L1NpcActionFactory.newAction(elem);
            if (action != null) {
                result.add(action);
            }
        }
        return result;
    }

    public static Element getFirstChildElementByTagName(final Element element,
            final String tagName) {
        final IterableElementList list = new IterableElementList(
                element.getElementsByTagName(tagName));
        for (final Element elem : list) {
            return elem;
        }
        return null;
    }

    public static int getIntAttribute(final Element element, final String name,
            final int defaultValue) {
        int result = defaultValue;
        try {
            result = Integer.valueOf(element.getAttribute(name));
        } catch (final NumberFormatException e) {
        }
        return result;
    }

    public static boolean getBoolAttribute(final Element element,
            final String name, final boolean defaultValue) {
        boolean result = defaultValue;
        final String value = element.getAttribute(name);
        if (!value.equals("")) {
            result = Boolean.valueOf(value);
        }
        return result;
    }

    private final static Map<String, Integer> _questIds = new HashMap<String, Integer>();
    static {
        /*
         * _questIds.put("level15", L1PcQuest.QUEST_LEVEL15);
         * _questIds.put("level30", L1PcQuest.QUEST_LEVEL30);
         * _questIds.put("level45", L1PcQuest.QUEST_LEVEL45);
         * _questIds.put("level50", L1PcQuest.QUEST_LEVEL50);
         * _questIds.put("lyra", L1PcQuest.QUEST_LYRA);
         * _questIds.put("oilskinmant", L1PcQuest.QUEST_OILSKINMANT);
         * _questIds.put("doromond", L1PcQuest.QUEST_DOROMOND);
         * _questIds.put("ruba", L1PcQuest.QUEST_RUBA); _questIds.put("lukein",
         * L1PcQuest.QUEST_LUKEIN1); _questIds.put("tbox1",
         * L1PcQuest.QUEST_TBOX1); _questIds.put("tbox2",
         * L1PcQuest.QUEST_TBOX2); _questIds.put("tbox3",
         * L1PcQuest.QUEST_TBOX3); _questIds.put("cadmus",
         * L1PcQuest.QUEST_CADMUS); _questIds.put("resta",
         * L1PcQuest.QUEST_RESTA); _questIds.put("kamyla",
         * L1PcQuest.QUEST_KAMYLA); _questIds.put("lizard",
         * L1PcQuest.QUEST_LIZARD); _questIds.put("desire",
         * L1PcQuest.QUEST_DESIRE); _questIds.put("shadows",
         * L1PcQuest.QUEST_SHADOWS); _questIds.put("toscroll",
         * L1PcQuest.QUEST_TOSCROLL); _questIds.put("moonoflongbow",
         * L1PcQuest.QUEST_MOONOFLONGBOW);
         * _questIds.put("Generalhamelofresentment",
         * L1PcQuest.QUEST_GENERALHAMELOFRESENTMENT);
         */
    }

    public static int parseQuestId(final String questId) {
        if (questId.equals("")) {
            return -1;
        }
        final Integer result = _questIds.get(questId.toLowerCase());
        if (result == null) {
            throw new IllegalArgumentException();
        }
        return result;
    }

    public static int parseQuestStep(final String questStep) {
        if (questStep.equals("")) {
            return -1;
        }
        if (questStep.equalsIgnoreCase("End")) {
            return L1PcQuest.QUEST_END;
        }
        return Integer.parseInt(questStep);
    }
}
