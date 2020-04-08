package com.lineage.server.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.datatables.QuestTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1QuestUser;

/**
 * 世界副本任务执行管理器<BR>
 * 
 * @author dexc
 * 
 */
public class WorldQuest {

    private static final Log _log = LogFactory.getLog(WorldQuest.class);

    private final Lock _lock;

    private static WorldQuest _instance;

    private AtomicInteger _nextId;

    private final ConcurrentHashMap<Integer, L1QuestUser> _isQuest;

    private Collection<L1QuestUser> _allQuestValues;

    public static WorldQuest get() {
        if (_instance == null) {
            _instance = new WorldQuest();
        }
        return _instance;
    }

    private WorldQuest() {
        _lock = new ReentrantLock(true);
        _isQuest = new ConcurrentHashMap<Integer, L1QuestUser>();
        _nextId = new AtomicInteger(100);
    }

    /**
     * 取回新的副本编号
     * 
     * @return
     */
    public int nextId() {
        this._lock.lock();
        try {
            int objid = _nextId.getAndIncrement();
            return objid;

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 目前的副本编号使用数字
     * 
     * @return
     */
    public int maxId() {
        this._lock.lock();
        try {
            return _nextId.get();

        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 全部执行中副本
     * 
     * @return
     */
    public Collection<L1QuestUser> all() {
        try {
            final Collection<L1QuestUser> vs = _allQuestValues;
            return (vs != null) ? vs : (_allQuestValues = Collections
                    .unmodifiableCollection(_isQuest.values()));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 副本清单
     * 
     * @return
     */
    public ConcurrentHashMap<Integer, L1QuestUser> map() {
        return _isQuest;
    }

    /**
     * 执行中指定任务编号副本数据
     * 
     * @param questId
     *            任务编号
     * 
     * @return 副本清单
     */
    public ArrayList<L1QuestUser> getQuests(final int questId) {
        try {

            final ArrayList<L1QuestUser> questList = new ArrayList<L1QuestUser>();
            if (_isQuest.size() > 0) {
                for (final Iterator<L1QuestUser> iter = all().iterator(); iter
                        .hasNext();) {
                    final L1QuestUser quest = iter.next();
                    // for (L1QuestUser quest : _isQuest.values()) {
                    if (quest.get_questid() == questId) {
                        questList.add(quest);
                    }
                }
            }
            return questList;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 指定副本数据
     * 
     * @param key
     *            副本编号
     * @return
     */
    public L1QuestUser get(final int key) {
        try {
            return _isQuest.get(new Integer(key));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 加入副本清单<BR>
     * 该ID副本不存将会建立新的副本
     * 
     * @param key
     *            副本编号
     * @param mapid
     *            副本地图编号
     * @param questid
     *            副本任务编号
     * @param pc
     *            执行者
     * @return
     */
    public L1QuestUser put(final int key, final int mapid, final int questid,
            final L1PcInstance pc) {
        try {
            // 不是副本地图
            if (!QuestMapTable.get().isQuestMap(mapid)) {
                _log.error("副本地图编号错误: 原因-找不到这个副本地图的设置 MapId:" + mapid);
                return null;
            }
            // 不是任务副本编号
            if (QuestTable.get().getTemplate(questid) == null) {
                _log.error("副本地图编号错误: 原因-找不到这个副本任务的设置 questid:" + questid);
                return null;
            }

            L1QuestUser value = _isQuest.get(new Integer(key));
            if (value != null) {
                pc.set_showId(key);
                value.add(pc);

            } else {
                // 初始化建立副本
                value = new L1QuestUser(key, mapid, questid);
                pc.set_showId(key);
                value.add(pc);

                // 召唤副本怪物
                value.spawnQuestMob();
            }
            _isQuest.put(new Integer(key), value);
            return value;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 移出副本清单(怪物)
     * 
     * @param key
     *            副本编号
     * @param npc
     *            移除的怪物
     */
    public void remove(final int key, final L1NpcInstance npc) {
        try {
            final L1QuestUser value = _isQuest.get(new Integer(key));
            if (value != null) {
                value.removeMob(npc);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 移出副本清单<BR>
     * 当副本人数小于等于0将会结束副本
     * 
     * @param key
     *            副本编号
     * @param pc
     *            执行者
     */
    public void remove(final int key, final L1PcInstance pc) {
        try {
            final L1QuestUser value = _isQuest.get(new Integer(key));
            if (value != null) {
                pc.set_showId(-1);
                value.remove(pc);

                boolean isRemove = false;
                if (value.is_outStop()) {
                    isRemove = true;
                }
                if (value.size() <= 0) {
                    isRemove = true;
                }
                if (isRemove) {
                    // 人数为0移除副本
                    _isQuest.remove(new Integer(key));
                    // 任务失败
                    value.endQuest();
                    // 移除副本中怪物
                    value.removeMob();
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 副本编号 是执行中副本
     * 
     * @param key
     *            副本编号
     * 
     * @return true:是 false:不是<BR>
     *         传回false并非代表该编号无使用价值<BR>
     *         亦有可能是 农场使用编号
     */
    public boolean isQuest(final int key) {
        try {
            final L1QuestUser value = _isQuest.get(new Integer(key));
            if (value != null) {
                return true;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return false;
    }
}
