package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.QuestTable;
import com.lineage.server.datatables.storage.CharacterQuestStorage;
import com.lineage.server.templates.L1Quest;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 任务纪录
 * 
 * @author dexc
 * 
 */
public class CharacterQuestTable implements CharacterQuestStorage {

    private static final Log _log = LogFactory
            .getLog(CharacterQuestTable.class);

    // 人物OBJID / <任务编号/任务进度>
    private static final Map<Integer, HashMap<Integer, Integer>> _questList = new HashMap<Integer, HashMap<Integer, Integer>>();

    /**
     * 初始化载入
     */
    @Override
    public void load() {
        delete();// 删除每日任务

        final PerformanceTimer timer = new PerformanceTimer();
        Connection co = null;
        PreparedStatement pm = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactory.get().getConnection();
            pm = co.prepareStatement("SELECT * FROM `character_quests`");
            rs = pm.executeQuery();

            while (rs.next()) {
                final int char_id = rs.getInt("char_id");// 人物OBJID

                // 检查该资料所属是否遗失
                if (CharObjidTable.get().isChar(char_id) != null) {
                    final int key = rs.getInt("quest_id");// 任务编号
                    switch (key) {
                    // 每日任务 重启删除
                        case 110:
                        case 117:
                        case 137:
                        case 139:
                            break;

                        default:
                            final int value = rs.getInt("quest_step");// 任务进度

                            HashMap<Integer, Integer> hsMap = _questList
                                    .get(new Integer(char_id));

                            if (hsMap == null) {
                                hsMap = new HashMap<Integer, Integer>();
                                hsMap.put(new Integer(key), new Integer(value));

                                _questList.put(new Integer(char_id), hsMap);

                            } else {
                                hsMap.put(new Integer(key), new Integer(value));
                            }
                            break;
                    }

                } else {
                    // 人物资料遗失 删除相关资讯
                    delete(char_id);
                }
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pm);
            SQLUtil.close(co);
        }
        _log.info("载入人物任务纪录资料数量: " + _questList.size() + "(" + timer.get()
                + "ms)");
    }

    private static void delete() {
        deleteData(110);// 说明:安塔瑞斯栖息地 (全职业80级任务副本)
        deleteData(117);// 说明:法利昂栖息地 (全职业80级任务副本)
        deleteData(137);// 说明:葛拉的请求(全职业85级任务)
        deleteData(139);// 说明:召唤魔物(每日任务)
    }

    /**
     * 删除指定任务编号资料
     * 
     * @param objid
     */
    private static void deleteData(final int questid) {
        Connection co = null;
        PreparedStatement pm = null;
        try {
            co = DatabaseFactory.get().getConnection();
            pm = co.prepareStatement("DELETE FROM `character_quests` WHERE `quest_id`=?");
            pm.setInt(1, questid);

            pm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pm);
            SQLUtil.close(co);
        }
    }

    /**
     * 删除遗失资料
     * 
     * @param objid
     */
    private static void delete(final int objid) {
        _questList.remove(objid);

        Connection co = null;
        PreparedStatement pm = null;
        try {
            co = DatabaseFactory.get().getConnection();
            pm = co.prepareStatement("DELETE FROM `character_quests` WHERE `char_id`=?");
            pm.setInt(1, objid);

            pm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pm);
            SQLUtil.close(co);
        }
    }

    /**
     * 传回任务组
     * 
     * @param char_id
     * @return
     */
    @Override
    public Map<Integer, Integer> get(final int char_id) {
        // System.out.println("取回人物任务纪录:" + char_id + " " + _questList.get(new
        // Integer(char_id)));
        return _questList.get(new Integer(char_id));
    }

    /**
     * 新建任务
     * 
     * @param char_id
     *            人物OBJID
     * @param key
     *            任务编号
     * @param value
     *            任务进度
     */
    @Override
    public void storeQuest(final int char_id, final int key, final int value) {
        // 取回人物任务资料清单
        HashMap<Integer, Integer> hsMap = _questList.get(new Integer(char_id));

        if (hsMap == null) {
            hsMap = new HashMap<Integer, Integer>();
            hsMap.put(new Integer(key), new Integer(value));

            _questList.put(new Integer(char_id), hsMap);

        } else {
            hsMap.put(new Integer(key), new Integer(value));
        }

        // 任务资料
        L1Quest quest = null;
        if (value == 1) {
            quest = QuestTable.get().getTemplate(key);
        }

        Connection co = null;
        PreparedStatement pm = null;
        try {
            String add = "";
            if (quest != null) {
                add = ",`note`=?";
                ;// 任务说明
            }

            co = DatabaseFactory.get().getConnection();
            pm = co.prepareStatement("INSERT INTO `character_quests` SET `char_id`=?,`quest_id`=?,`quest_step`=?"
                    + add);

            int i = 0;
            pm.setInt(++i, char_id);// 人物OBJID
            pm.setInt(++i, key);// 任务编号
            pm.setInt(++i, value);// 任务进度

            if (quest != null) {
                pm.setString(++i, quest.get_note());// 任务说明
            }

            pm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pm);
            SQLUtil.close(co);
        }
    }

    /**
     * 更新任务
     * 
     * @param char_id
     *            人物OBJID
     * @param key
     *            任务编号
     * @param value
     *            任务进度
     */
    @Override
    public void updateQuest(final int char_id, final int key, final int value) {
        // 取回人物任务资料清单
        HashMap<Integer, Integer> hsMap = _questList.get(new Integer(char_id));

        if (hsMap == null) {
            hsMap = new HashMap<Integer, Integer>();
            hsMap.put(new Integer(key), new Integer(value));

            _questList.put(new Integer(char_id), hsMap);

        } else {
            hsMap.put(new Integer(key), new Integer(value));
        }

        Connection co = null;
        PreparedStatement pm = null;
        try {
            co = DatabaseFactory.get().getConnection();
            pm = co.prepareStatement("UPDATE `character_quests` SET `quest_step`=? WHERE `char_id`=? AND `quest_id`=?");

            int i = 0;
            pm.setInt(++i, value);// 任务进度

            pm.setInt(++i, char_id);// 人物OBJID
            pm.setInt(++i, key);// 任务编号

            pm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pm);
            SQLUtil.close(co);
        }
    }

    /**
     * 解除任务
     * 
     * @param char_id
     *            人物OBJID
     * @param key
     *            任务编号
     */
    @Override
    public void delQuest(final int char_id, final int key) {
        // 取回人物任务资料清单
        HashMap<Integer, Integer> hsMap = _questList.get(new Integer(char_id));

        if (hsMap == null) {
            return;

        } else {
            // 移除任务
            hsMap.remove(key);
        }

        Connection co = null;
        PreparedStatement pm = null;
        try {
            co = DatabaseFactory.get().getConnection();
            pm = co.prepareStatement("DELETE FROM `character_quests` WHERE `char_id`=? AND `quest_id`=?");

            int i = 0;
            pm.setInt(++i, char_id);// 人物OBJID
            pm.setInt(++i, key);// 任务编号

            pm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pm);
            SQLUtil.close(co);
        }
    }
}
