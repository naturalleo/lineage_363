package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.templates.L1NpcChat;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * NPC会话资料
 * 
 * @author dexc
 * 
 */
public class NpcChatTable {

    private static final Log _log = LogFactory.getLog(NpcChatTable.class);

    private static NpcChatTable _instance;

    private static final Map<Integer, L1NpcChat> _npcChatAppearance = new HashMap<Integer, L1NpcChat>();

    private static final Map<Integer, L1NpcChat> _npcChatDead = new HashMap<Integer, L1NpcChat>();

    private static final Map<Integer, L1NpcChat> _npcChatHide = new HashMap<Integer, L1NpcChat>();

    private static final Map<Integer, L1NpcChat> _npcChatGameTime = new HashMap<Integer, L1NpcChat>();

    public static NpcChatTable get() {
        if (_instance == null) {
            _instance = new NpcChatTable();
        }
        return _instance;
    }

    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {

            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `npcchat`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                final L1NpcChat npcChat = new L1NpcChat();
                npcChat.setNpcId(rs.getInt("npc_id"));
                npcChat.setChatTiming(rs.getInt("chat_timing"));
                npcChat.setStartDelayTime(rs.getInt("start_delay_time"));
                npcChat.setChatId1(rs.getString("chat_id1"));
                npcChat.setChatId2(rs.getString("chat_id2"));
                npcChat.setChatId3(rs.getString("chat_id3"));
                npcChat.setChatId4(rs.getString("chat_id4"));
                npcChat.setChatId5(rs.getString("chat_id5"));
                npcChat.setChatInterval(rs.getInt("chat_interval"));
                npcChat.setShout(rs.getBoolean("is_shout"));
                npcChat.setWorldChat(rs.getBoolean("is_world_chat"));
                npcChat.setRepeat(rs.getBoolean("is_repeat"));
                npcChat.setRepeatInterval(rs.getInt("repeat_interval"));
                npcChat.setGameTime(rs.getInt("game_time"));

                if (npcChat.getChatTiming() == L1NpcInstance.CHAT_TIMING_APPEARANCE) {
                    _npcChatAppearance.put(new Integer(npcChat.getNpcId()),
                            npcChat);

                } else if (npcChat.getChatTiming() == L1NpcInstance.CHAT_TIMING_DEAD) {
                    _npcChatDead.put(new Integer(npcChat.getNpcId()), npcChat);

                } else if (npcChat.getChatTiming() == L1NpcInstance.CHAT_TIMING_HIDE) {
                    _npcChatHide.put(new Integer(npcChat.getNpcId()), npcChat);

                } else if (npcChat.getChatTiming() == L1NpcInstance.CHAT_TIMING_GAME_TIME) {
                    _npcChatGameTime.put(new Integer(npcChat.getNpcId()),
                            npcChat);
                }
            }
        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入NPC会话资料数量: "
                + (_npcChatAppearance.size() + _npcChatDead.size()
                        + _npcChatHide.size() + _npcChatGameTime.size()) + "("
                + timer.get() + "ms)");
    }

    /**
     * 出现时NPC对话
     * 
     * @param i
     *            NPCID
     * @return
     */
    public L1NpcChat getTemplateAppearance(final int i) {
        return _npcChatAppearance.get(new Integer(i));
    }

    /**
     * 死亡时NPC对话
     * 
     * @param i
     *            NPCID
     * @return
     */
    public L1NpcChat getTemplateDead(final int i) {
        return _npcChatDead.get(new Integer(i));
    }

    /**
     * 取消隐藏时NPC对话
     * 
     * @param i
     *            NPCID
     * @return
     */
    public L1NpcChat getTemplateHide(final int i) {
        return _npcChatHide.get(new Integer(i));
    }

    /**
     * 定时NPC对话
     * 
     * @param i
     *            NPCID
     * @return
     */
    public L1NpcChat getTemplateGameTime(final int i) {
        return _npcChatGameTime.get(new Integer(i));
    }

    private Collection<L1NpcChat> _allTimeValues;

    /**
     * 全部定时NPC对话
     * 
     * @return
     */
    public Collection<L1NpcChat> all() {
        try {
            final Collection<L1NpcChat> vs = _allTimeValues;
            return (vs != null) ? vs : (_allTimeValues = Collections
                    .unmodifiableCollection(_npcChatGameTime.values()));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public L1NpcChat[] getAllGameTime() {
        return _npcChatGameTime.values().toArray(
                new L1NpcChat[_npcChatGameTime.size()]);
    }
}
