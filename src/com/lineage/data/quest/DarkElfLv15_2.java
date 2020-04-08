package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:妖魔的侵入 (黑暗妖精15级以上官方任务)
 * 
 * @author daien
 * 
 */
public class DarkElfLv15_2 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(DarkElfLv15_2.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_d15_2";

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1 妖魔长老 </font><BR> <BR>
     * 首先到沉默洞穴的大桥附近找<font fg=0000ff>警卫队长－康 </font>(32803 ，
     * 32895)，得知他为了维护治安而分身乏术以致于无法处理妖魔的入侵，所以他会让你去猎杀妖魔长老，并且让你证明你确实有此能力。<BR>
     * 接受任务之后，只要到沉默洞穴的出入口附近的原野平原找寻外貌与妖魔法师相似的<font fg=0000ff>妖魔长老
     * </font>，解决掉之后即可得到妖魔长老首级。<BR> 回到大桥附近找康，并把妖魔长老首级交给他，即可得到康之袋。打开康之袋，即可得到<font
     * fg=0000ff>黑暗精灵水晶(提炼魔石) </font>和<font fg=0000ff>影子面具
     * </font>。再次与康对话，得知等到自己的能力成熟时就可以去寻找伦得的帮助。<BR> <BR> 任务目标：<BR>
     * 与康接受任务，狩猎妖魔长老取得它的首级，回去交差取得奖励<BR> <BR> 相关怪物：<BR> <font fg=ffff00>Lv.12
     * 妖魔长老 (沉默洞穴) </font><BR> <BR> 相关物品：<BR> <font fg=ffff00>妖魔长老首级 x 1
     * </font><BR> <font fg=ffff00>康之袋 x 1 </font><BR> <font fg=ffff00>影子面具 x 1
     * </font><BR> <font fg=ffff00>黑暗精灵水晶(提炼魔石) x 1 </font><BR> <BR>
     */
    private DarkElfLv15_2() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new DarkElfLv15_2();
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
