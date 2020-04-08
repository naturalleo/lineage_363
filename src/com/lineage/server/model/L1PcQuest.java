package com.lineage.server.model;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.lock.CharacterQuestReading;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 任务纪录
 * 
 * @author admin
 * 
 */
public class L1PcQuest {

    private static final Log _log = LogFactory.getLog(L1PcQuest.class);

    public static final int QUEST_OILSKINMANT = 11;

    public static final int QUEST_DOROMOND = 20;
    public static final int QUEST_RUBA = 21;
    public static final int QUEST_AREX = 22;

    public static final int QUEST_LUKEIN1 = 23;
    public static final int QUEST_TBOX1 = 24;
    public static final int QUEST_TBOX2 = 25;
    public static final int QUEST_TBOX3 = 26;
    public static final int QUEST_SIMIZZ = 27;
    public static final int QUEST_DOIL = 28;
    public static final int QUEST_RUDIAN = 29;
    public static final int QUEST_RESTA = 30;
    public static final int QUEST_CADMUS = 31;
    public static final int QUEST_KAMYLA = 32;
    public static final int QUEST_CRYSTAL = 33;
    public static final int QUEST_LIZARD = 34;
    public static final int QUEST_KEPLISHA = 35;
    public static final int QUEST_DESIRE = 36;
    public static final int QUEST_SHADOWS = 37;
    public static final int QUEST_TOSCROLL = 39;
    public static final int QUEST_MOONOFLONGBOW = 40;
    public static final int QUEST_GENERALHAMELOFRESENTMENT = 41;

    public static final int QUEST_NOT = 0; // 任务尚未开始
    public static final int QUEST_END = 255; // 任务已经结束

    private L1PcInstance _owner = null;
    private Map<Integer, Integer> _quest = null;

    /**
     * 任务纪录模组
     * 
     * @param owner
     */
    public L1PcQuest(final L1PcInstance owner) {
        this._owner = owner;
    }

    /**
     * 传回执行任务者
     * 
     * @return
     */
    public L1PcInstance get_owner() {
        return this._owner;
    }

    /**
     * 传回任务进度
     * 
     * @param quest_id
     *            任务编号
     * @return 进度
     */
    public int get_step(final int quest_id) {
        try {
            final Integer step = this._quest.get(new Integer(quest_id));
            if (step == null) {
                return 0;

            } else {
                return step.intValue();
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return 0;
    }

    /**
     * 建立/更新 任务资料
     * 
     * @param quest_id
     *            任务编号
     * @param step
     *            进度
     */
    public void set_step(final int quest_id, final int step) {
        try {
            final Integer key = this._quest.get(new Integer(quest_id));
            if (key == null) {
                if (step > 1) {
                    _log.error("任务资讯建立过程异常 原因:起始设置任务进度不是1 (questid:" + quest_id
                            + ")");
                    return;
                }
                // 建立 任务资料
                CharacterQuestReading.get().storeQuest(this._owner.getId(),
                        quest_id, step);

            } else {
                if (step > key.intValue() + 1) {
                    _log.error("任务资讯建立过程异常 原因:设置任务进度超过原始进度! (questid:"
                            + quest_id + ")");
                    return;
                }
                // 更新 任务资料
                CharacterQuestReading.get().updateQuest(this._owner.getId(),
                        quest_id, step);
            }

            this._quest.put(new Integer(quest_id), new Integer(step));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 结束任务
     * 
     * @param quest_id
     *            任务编号
     */
    public void set_end(final int quest_id) {
        try {
            // this.set_step(quest_id, QUEST_END);
            // 更新 任务资料
            CharacterQuestReading.get().updateQuest(this._owner.getId(),
                    quest_id, QUEST_END);
            this._quest.put(new Integer(quest_id), new Integer(QUEST_END));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 该任务是否开始 (get_step 大于0 小于255 传回任务已经开始)
     * 
     * @param quest_id
     *            任务编号
     * @return true:已经开始 false:尚未开始
     */
    public boolean isStart(final int quest_id) {
        try {
            final int step = this.get_step(quest_id);
            // 大于0 小于255
            if (step > QUEST_NOT && step < QUEST_END) {
                return true;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    /**
     * 该任务是否结束
     * 
     * @param quest_id
     *            任务编号
     * @return true:已经结束 false:尚未结束
     */
    public boolean isEnd(final int quest_id) {
        try {
            if (this.get_step(quest_id) == QUEST_END) {
                return true;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    /**
     * 取回人物任务纪录
     */
    public void load() {
        try {
            // 取回人物任务纪录
            _quest = CharacterQuestReading.get().get(this._owner.getId());
            if (this._quest == null) {
                _quest = new HashMap<Integer, Integer>();
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
