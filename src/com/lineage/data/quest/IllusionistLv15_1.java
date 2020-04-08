package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:眠龙洞穴污染的来源 (幻术士15级以上官方任务)
 * 
 * @author daien
 * 
 */
public class IllusionistLv15_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(IllusionistLv15_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_i15_1";

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1 长老赋予的任务</font><BR> <BR>
     * 在希培莉亚找<font fg=0000ff>长老希莲恩</font>(32772
     * 32811)，她会要求你至眠龙洞穴调查洞穴遭受污染的原因。<BR> <BR> 任务目标：<BR> 跟长老希莲恩接任务，并前往眠龙洞穴<BR>
     * <BR> <img src="#1210"></img> <font fg=66ff66>步骤.2　搜集证物</font><BR> <BR>
     * 在眠龙洞穴狩猎怪物，直到凑齐所需的证物。<BR> 证物包括了<font fg=0000ff>10个污浊妖魔之心</font>、<font
     * fg=0000ff>1个污浊精灵核晶</font>、<font fg=0000ff>1个污浊安特的水果</font>、<font
     * fg=0000ff>1个污浊安特的树枝</font>、<font fg=0000ff>1个污浊安特的树皮</font>。<BR>
     * 之后再回去报告长老希莲恩。<BR> <BR> 任务目标：<BR> 在眠龙洞穴搜集证物，再回去找长老希莲恩<BR> <BR> 相关怪物：<BR>
     * <font fg=ffff00>Lv.12 污浊妖魔弓箭手</font><BR> <font fg=ffff00>Lv.12
     * 污浊妖魔</font><BR> <font fg=ffff00>Lv.13 污浊 妖魔战士</font><BR> <font
     * fg=ffff00>Lv.15 污浊精灵</font><BR> <font fg=ffff00>Lv.15 污浊安特</font><BR>
     * <BR> 相关物品：<BR> <font fg=ffff00>污浊安特的树皮 x 1</font><BR> <font
     * fg=ffff00>污浊安特的树枝 x 1</font><BR> <font fg=ffff00>污浊安特的水果 x 1</font><BR>
     * <font fg=ffff00>污浊妖魔之心 x 10</font><BR> <font fg=ffff00>污浊精灵核晶 x
     * 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.3　回报</font><BR> <BR>
     * 将凑齐的证物交给长老希莲恩，即可获得幻术士魔杖和记忆水晶(立方：燃烧)。<BR> <BR> 任务目标：<BR>
     * 将证物转交给长老希莲恩，并获得奖励<BR> <BR> 相关物品：<BR> <font fg=ffff00>记忆水晶(立方：燃烧) x
     * 1</font><BR> <font fg=ffff00>幻术士魔杖 x 1</font><BR> <BR>
     */
    private IllusionistLv15_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new IllusionistLv15_1();
    }

    @Override
    public void execute(L1Quest quest) {
        try {
            // 设置任务
            QUEST = quest;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            // _log.info("任务启用:" + QUEST.get_note());
        }
    }

    @Override
    public void startQuest(L1PcInstance pc) {
        try {
            // 判断职业
            if (QUEST.check(pc)) {
                // 判断等级
                if (pc.getLevel() >= QUEST.get_questlevel()) {
                    // 任务尚未开始 设置为开始
                    if (pc.getQuest().get_step(QUEST.get_id()) != 1) {
                        pc.getQuest().set_step(QUEST.get_id(), 1);
                    }

                } else {
                    // 该等级 无法执行此任务
                    pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "y_q_not1"));
                }

            } else {
                // 该职业无法执行此任务
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "y_q_not2"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void endQuest(L1PcInstance pc) {
        try {
            // 任务尚未结束 设置为结束
            if (!pc.getQuest().isEnd(QUEST.get_id())) {
                pc.getQuest().set_end(QUEST.get_id());

                final String questName = QUEST.get_questname();
                // 3109:\f1%0 任务完成！
                pc.sendPackets(new S_ServerMessage("\\fT" + questName + "任务完成！"));
                // 任务可以重复
                if (QUEST.is_del()) {
                    // 3110:请注意这个任务可以重复执行，需要重复任务，请在任务管理员中执行解除。
                    pc.sendPackets(new S_ServerMessage(
                            "\\fT请注意这个任务可以重复执行，需要重复任务，请在任务管理员中执行解除。"));

                } else {
                    // 3111:请注意这个任务不能重复执行，无法在任务管理员中解除执行。
                    new S_ServerMessage("\\fR请注意这个任务不能重复执行，无法在任务管理员中解除执行。");
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void showQuest(L1PcInstance pc) {
        try {
            // 展示任务说明
            if (_html != null) {
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), _html));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void stopQuest(L1PcInstance pc) {
        // TODO Auto-generated method stub

    }
}
