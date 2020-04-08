package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.AWAKEN_ANTHARAS;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_FAFURION;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_VALAKAS;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.server.datatables.QuestTable;
import com.lineage.server.datatables.lock.CharacterQuestReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.skillmode.SUMMON_MONSTER;
import com.lineage.server.model.skill.skillmode.SkillMode;
import com.lineage.server.serverpackets.S_Bonusstats;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_ShopSellListCnX;
import com.lineage.server.templates.L1Quest;

/**
 * 对话命令来自PC的执行与判断
 * 
 * @author daien
 * 
 */
public class L1ActionPc {

    private static final Log _log = LogFactory.getLog(L1ActionPc.class);

    private final L1PcInstance _pc;

    /**
     * 对话命令来自PC的执行与判断
     * 
     * @param pc
     *            执行者
     */
    public L1ActionPc(final L1PcInstance pc) {
        _pc = pc;
    }

    /**
     * 传回执行命令者
     * 
     * @return
     */
    public L1PcInstance get_pc() {
        return _pc;
    }

    /**
     * 选单命令执行
     * 
     * @param cmd
     * @param amount
     */
    public void action(final String cmd, final long amount) {
        try {
            if (cmd.matches("[0-9]+")) {
                // 解除GM管理状态
                _pc.get_other().set_gmHtml(null);
                // 展开召唤控制选单
                if (_pc.isSummonMonster()) {
                    summonMonster(_pc, cmd);
                    _pc.setShapeChange(false);
                    _pc.setSummonMonster(false);
                }
                return;
            }

            // 展开变身控制选单
            if (_pc.isShapeChange()) {
                // 解除GM管理状态
                _pc.get_other().set_gmHtml(null);
                final int awakeSkillId = _pc.getAwakeSkillId();
                if ((awakeSkillId == AWAKEN_ANTHARAS)
                        || (awakeSkillId == AWAKEN_FAFURION)
                        || (awakeSkillId == AWAKEN_VALAKAS)) {
                    // 目前状态中无法变身。
                    _pc.sendPackets(new S_ServerMessage(1384));
                    return;
                }
                L1PolyMorph.handleCommands(_pc, cmd);
                _pc.setShapeChange(false);
                _pc.setSummonMonster(false);
                return;
            }

            // GM选单不为空
            if (_pc.get_other().get_gmHtml() != null) {
                _pc.get_other().get_gmHtml().action(cmd);
                return;
            }

            // 解除GM管理状态
            _pc.get_other().set_gmHtml(null);

            // 任务选单 FIXME
            if (cmd.equalsIgnoreCase("power")) {// 能力选取视窗
                // 判断是否出现能力选取视窗
                if (_pc.power()) {
                    _pc.sendPackets(new S_Bonusstats(_pc.getId()));
                }

            } else if (cmd.equalsIgnoreCase("shop")) {// 道具商城
                _pc.sendPackets(new S_ShopSellListCnX(_pc, _pc.getId()));

            } else if (cmd.equalsIgnoreCase("index")) {// 任务查询系统
                _pc.isWindows();

//            } else if (cmd.equalsIgnoreCase("locerr1")) {// 解除人物卡点
//                //_pc.set_unfreezingTime(10);//hjx1000
//            	L1Teleport.teleport(_pc, _pc.getLocation(), _pc.getHeading(), false);
//
//            } else if (cmd.equalsIgnoreCase("locerr2")) {// 修正人物错位
//                //_pc.sendPackets(new S_Lock());//hjx1000
//                L1Teleport.teleport(_pc, _pc.getLocation(), _pc.getHeading(), false);

            } else if (cmd.equalsIgnoreCase("qt")) {// 查看执行中任务
                showStartQuest(_pc, _pc.getId());

            } else if (cmd.equalsIgnoreCase("quest")) {// 查看可执行任务
                showQuest(_pc, _pc.getId());

            } else if (cmd.equalsIgnoreCase("questa")) {// 查看全部任务
                showQuestAll(_pc, _pc.getId());

            } else if (cmd.equalsIgnoreCase("i")) {// 任务介绍
                final L1Quest quest = QuestTable.get().getTemplate(
                        _pc.getTempID());
                _pc.setTempID(0);
                // 确认该任务存在
                if (quest == null) {
                    return;
                }
                QuestClass.get().showQuest(_pc, quest.get_id());

            } else if (cmd.equalsIgnoreCase("d")) {// 任务回收
                final L1Quest quest = QuestTable.get().getTemplate(
                        _pc.getTempID());
                _pc.setTempID(0);
                // 确认该任务存在
                if (quest == null) {
                    return;
                }
                // 任务已经完成
                if (_pc.getQuest().isEnd(quest.get_id())) {
                    questDel(quest);
                    return;
                }
                // 任务尚未开始
                if (!_pc.getQuest().isStart(quest.get_id())) {
                    // 很抱歉!!你并未开始执行这个任务!
                    _pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_q_not6"));
                    return;
                }
                // 执行中 未完成任务
                questDel(quest);

            } else if (cmd.equalsIgnoreCase("dy")) {// 任务移除
                final L1Quest quest = QuestTable.get().getTemplate(
                        _pc.getTempID());
                _pc.setTempID(0);
                // 确认该任务存在
                if (quest == null) {
                    return;
                }
                // 任务已经完成
                if (_pc.getQuest().isEnd(quest.get_id())) {
                    isDel(quest);
                    return;
                }
                // 任务尚未开始
                if (!_pc.getQuest().isStart(quest.get_id())) {
                    // 很抱歉!!你并未开始执行这个任务!
                    _pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_q_not6"));
                    return;
                }
                // 执行中 未完成任务
                isDel(quest);

            } else if (cmd.equalsIgnoreCase("up")) {// 上一页(管理)
                final int page = _pc.get_other().get_page() - 1;
                final L1ActionShowHtml show = new L1ActionShowHtml(_pc);
                show.showQuestMap(page);

            } else if (cmd.equalsIgnoreCase("dn")) {// 下一页(管理)
                final int page = _pc.get_other().get_page() + 1;
                final L1ActionShowHtml show = new L1ActionShowHtml(_pc);
                show.showQuestMap(page);

            } else if (cmd.equalsIgnoreCase("q0")) {// 页面内指定位置
                final int key = (_pc.get_other().get_page() * 10) + 0;
                showPage(key);

            } else if (cmd.equalsIgnoreCase("q1")) {// 页面内指定位置
                final int key = (_pc.get_other().get_page() * 10) + 1;
                showPage(key);

            } else if (cmd.equalsIgnoreCase("q2")) {// 页面内指定位置
                final int key = (_pc.get_other().get_page() * 10) + 2;
                showPage(key);

            } else if (cmd.equalsIgnoreCase("q3")) {// 页面内指定位置
                final int key = (_pc.get_other().get_page() * 10) + 3;
                showPage(key);

            } else if (cmd.equalsIgnoreCase("q4")) {// 页面内指定位置
                final int key = (_pc.get_other().get_page() * 10) + 4;
                showPage(key);

            } else if (cmd.equalsIgnoreCase("q5")) {// 页面内指定位置
                final int key = (_pc.get_other().get_page() * 10) + 5;
                showPage(key);

            } else if (cmd.equalsIgnoreCase("q6")) {// 页面内指定位置
                final int key = (_pc.get_other().get_page() * 10) + 6;
                showPage(key);

            } else if (cmd.equalsIgnoreCase("q7")) {// 页面内指定位置
                final int key = (_pc.get_other().get_page() * 10) + 7;
                showPage(key);

            } else if (cmd.equalsIgnoreCase("q8")) {// 页面内指定位置
                final int key = (_pc.get_other().get_page() * 10) + 8;
                showPage(key);

            } else if (cmd.equalsIgnoreCase("q9")) {// 页面内指定位置
                final int key = (_pc.get_other().get_page() * 10) + 9;
                showPage(key);
            } else if (cmd.equalsIgnoreCase("r1")) {// 挂机范围+1
            	final int r = _pc.gethookrange();
            	_pc.sethookrange(r + 1);
            	final String[] R = new String[] {String.valueOf(_pc.gethookrange())};
                _pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "hookrange",
                		R));
            } else if (cmd.equalsIgnoreCase("r2")) {// 挂机范围-1
            	final int r = _pc.gethookrange();
            	if (r > 0) {
            		_pc.sethookrange(r - 1);
            	}
            	final String[] R = new String[] {String.valueOf(_pc.gethookrange())};
                _pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "hookrange",
                		R));
            } else if (cmd.equalsIgnoreCase("r3")) {// 挂机范围+10
            	final int r = _pc.gethookrange();
            	_pc.sethookrange(r + 10);
            	final String[] R = new String[] {String.valueOf(_pc.gethookrange())};
                _pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "hookrange",
                		R));
            } else if (cmd.equalsIgnoreCase("r4")) {// 挂机范围-10
            	final int r = _pc.gethookrange();
            	if (r > 9) {
            		_pc.sethookrange(r - 10);
            	} else {
            		_pc.sethookrange(0);
            	}
            	final String[] R = new String[] {String.valueOf(_pc.gethookrange())};
                _pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "hookrange",
                		R));
            } else if (cmd.equalsIgnoreCase("r5")) {
            	if (!_pc.isskill46()) {
                	_pc.setskill46(true);
                	_pc.sendPackets(new S_ServerMessage("\\aD开启自动烈炎术,结束挂机会自动关闭"));
            	}
           	
            } else if (cmd.equalsIgnoreCase("r6")) {
            	if (!_pc.isskill187()) {
                	_pc.setskill187(true);
                	_pc.sendPackets(new S_ServerMessage("\\aD开启自动屠宰者,结束挂机会自动关闭"));
            	}
            	
            } else if (cmd.equalsIgnoreCase("r7")) {
            	if (!_pc.isskill132()) {
                	_pc.setskill132(true);
                	_pc.sendPackets(new S_ServerMessage("\\aD开启自动三重矢,结束挂机会自动关闭"));
            	}
            	
            } else if (cmd.equalsIgnoreCase("start")) {// 开始挂机
            	if (!_pc.isAiRunning() && _pc.getInventory().checkItem(58030)) {
        			_pc.startAI();
        			_pc.setHomeX(_pc.getX());
        			_pc.setHomeY(_pc.getY());
        			_pc.sendPackets(new S_ServerMessage("\\aD挂机已经启动，若想结束请再次双击挂机符"));
        			_pc.sendPackets(new S_CloseList(_pc.getId()));
            	}
            } else if (cmd.equalsIgnoreCase("over")) {// 结束挂机
            	if (_pc.isActived()) {
            		_pc.setActived(false);
            		_pc.sendPackets(new S_ServerMessage("\\aD自动挂机已经结束。"));
            		_pc.sendPackets(new S_CloseList(_pc.getId()));
            	}
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 任务解除执行
     * 
     * @param quest
     */
    private void questDel(final L1Quest quest) {
        try {
            if (quest.is_del()) {
                _pc.setTempID(quest.get_id());
                String over = null;
                // 该任务完成
                if (_pc.getQuest().isEnd(quest.get_id())) {
                    over = "完成任务";// 完成任务!
                } else {
                    over = _pc.getQuest().get_step(quest.get_id()) + " / "
                            + quest.get_difficulty();
                }

                final String[] info = new String[] { quest.get_questname(), // 任务名称
                        Integer.toString(quest.get_questlevel()), // 任务等级
                        over, // 任务进度
                // 额外说明
                };
                _pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_qi2", info));

            } else {
                // 任务不可删除
                _pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_q_not5"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 确定解除任务执行
     * 
     * @param quest
     */
    private void isDel(final L1Quest quest) {
        try {
            if (quest.is_del()) {
                // 任务终止
                QuestClass.get().stopQuest(_pc, quest.get_id());

                CharacterQuestReading.get().delQuest(_pc.getId(),
                        quest.get_id());
                final String[] info = new String[] { quest.get_questname(), // 任务名称
                        Integer.toString(quest.get_questlevel()), // 任务等级
                };
                // 删除任务
                _pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_qi3", info));

            } else {
                // 任务不可删除
                _pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_q_not5"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 查看执行中任务
     * 
     * @param pc
     * @param id
     */
    public static void showStartQuest(L1PcInstance pc, int objid) {
        try {
            // 清空暂存任务清单
            pc.get_otherList().QUESTMAP.clear();

            int key = 0;
            for (int i = QuestTable.MINQID; i <= QuestTable.MAXQID; i++) {
                final L1Quest value = QuestTable.get().getTemplate(i);
                if (value != null) {
                    // 该任务已经结束
                    if (pc.getQuest().isEnd(value.get_id())) {
                        continue;
                    }
                    // 执行中任务判断
                    if (pc.getQuest().isStart(value.get_id())) {
                        pc.get_otherList().QUESTMAP.put(key++, value);
                    }
                }
            }

            if (pc.get_otherList().QUESTMAP.size() <= 0) {
                // 很抱歉!!你并没有任何执行中的任务!
                pc.sendPackets(new S_NPCTalkReturn(objid, "y_q_not7"));

            } else {
                final L1ActionShowHtml show = new L1ActionShowHtml(pc);
                show.showQuestMap(0);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 可执行任务
     * 
     * @param pc
     * @param objid
     */
    public static void showQuest(L1PcInstance pc, int objid) {
        try {
            // 清空暂存任务清单
            pc.get_otherList().QUESTMAP.clear();

            int key = 0;

            for (int i = QuestTable.MINQID; i <= QuestTable.MAXQID; i++) {
                final L1Quest value = QuestTable.get().getTemplate(i);
                if (value != null) {
                    // 大于可执行等级
                    if (pc.getLevel() >= value.get_questlevel()) {
                        // 该任务已经结束
                        if (pc.getQuest().isEnd(value.get_id())) {
                            continue;
                        }
                        // 该任务已经开始
                        if (pc.getQuest().isStart(value.get_id())) {
                            continue;
                        }
                        // 可执行职业判断
                        if (value.check(pc)) {
                            pc.get_otherList().QUESTMAP.put(key++, value);
                        }
                    }
                }
            }

            if (pc.get_otherList().QUESTMAP.size() <= 0) {
                // 很抱歉!!目前你的任务已经全部完成!
                pc.sendPackets(new S_NPCTalkReturn(objid, "y_q_not4"));

            } else {
                final L1ActionShowHtml show = new L1ActionShowHtml(pc);
                show.showQuestMap(0);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 全部任务
     * 
     * @param pc
     * @param objid
     */
    public static void showQuestAll(L1PcInstance pc, int objid) {
        try {
            // 清空暂存任务清单
            pc.get_otherList().QUESTMAP.clear();

            int key = 0;
            for (int i = QuestTable.MINQID; i <= QuestTable.MAXQID; i++) {
                final L1Quest value = QuestTable.get().getTemplate(i);
                if (value != null) {
                    // 可执行职业判断
                    if (value.check(pc)) {
                        pc.get_otherList().QUESTMAP.put(key++, value);
                    }
                }
            }
            final L1ActionShowHtml show = new L1ActionShowHtml(pc);
            show.showQuestMap(0);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 展示指定任务进度资料
     * 
     * @param key
     */
    private void showPage(int key) {
        try {
            final L1Quest quest = _pc.get_otherList().QUESTMAP.get(key);
            _pc.setTempID(quest.get_id());
            String over = null;
            // 该任务完成
            if (_pc.getQuest().isEnd(quest.get_id())) {
                over = "完成任务";// 完成任务!
            } else {
                over = _pc.getQuest().get_step(quest.get_id()) + " / "
                        + quest.get_difficulty();
            }

            final String[] info = new String[] { quest.get_questname(), // 任务名称
                    Integer.toString(quest.get_questlevel()), // 任务等级
                    over, // 任务进度
                    ""// 额外说明
            };
            _pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_qi1", info));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 展开召唤控制选单
     * 
     * @param pc
     * @param s
     */
    private void summonMonster(final L1PcInstance pc, final String s) {
        try {
            final SkillMode skillMode = new SUMMON_MONSTER();
            skillMode.start(pc, s);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
