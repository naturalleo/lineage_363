package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:法师的考验 (法师45级以上官方任务)
 * 
 * @author daien
 * 
 */
public class WizardLv45_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(WizardLv45_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_w45_1";

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1 神秘的岩石</font><BR> <BR>
     * 在变形怪出没的镜子森林中，突然冒出一个 <font
     * fg=0000ff>神秘的岩石</font>，而且它还会说话，更奇妙的是它还说明了有关邪恶势力的事情， <font
     * fg=0000ff>塔拉斯</font
     * >认为有必要调查神秘的岩石，因此想请玩家前往调查。到镜子森林(33780，33278)里寻找神秘的岩石，和他对话后得知他原来是位法师但是被变成岩石
     * ，所以他需要你帮他找解除诅咒的两样物品，变形怪的血及魔法书( 魔法相消术 )。<BR> <BR> 任务目标：<BR>
     * 与塔拉斯接受任务，前往镜子森林与 <font fg=0000ff>神秘的岩石</font>谈话，并接受任务<BR> <BR> <img
     * src="#1210"></img> <font fg=66ff66>步骤.2 变形怪的血</font><BR> <BR> 将 <font
     * fg=0000ff>伊娃的祝福</font>丢给 <font
     * fg=0000ff>变形怪</font>(只出现在镜子森林的33766.33236周围
     * )并将他打倒，就会得到变形怪的血。之后准备好魔法书(魔法相消术)一同交给神秘的岩石，得到古代恶魔的记载。<BR> <BR> 任务目标：<BR>
     * 搜集变形怪的血和魔法书(魔法相消术)，交给神秘的岩石，得到<font fg=0000ff>古代恶魔的记载</font><BR> <BR>
     * 相关怪物：<BR> <font fg=ffff00>Lv.45 变形怪</font><BR> <BR> 相关物品：<BR> <font
     * fg=ffff00>变形怪的血 x 1</font><BR> <font fg=ffff00>古代恶魔的记载 x 1</font><BR>
     * <font fg=ffff00>魔法书(魔法相消术) x 1</font><BR> <BR> <img src="#1210"></img>
     * <font fg=66ff66>步骤.3 回报塔拉斯</font><BR> <BR>
     * 回到象牙塔3楼后将调查结果的古代恶魔的记载交给塔拉斯后，得到塔拉斯的魔法袋，点二下后得到玛那斗篷及古代人的智慧。<BR> <BR>
     * 任务目标：<BR> 将古代恶魔的记载交给塔拉斯，并领取奖励<BR> <BR> 相关物品：<BR> <font fg=ffff00>塔拉斯的魔法袋
     * x 1</font><BR> <font fg=ffff00>古代人的智慧 x 1</font><BR> <font fg=ffff00>玛那斗篷
     * x 1</font><BR> <BR>
     */
    private WizardLv45_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new WizardLv45_1();
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
