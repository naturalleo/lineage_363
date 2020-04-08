package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:帮助罗伊逃脱(等级40以上官方任务)
 * 
 * @author daien
 * 
 */
public class EWLv40_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(EWLv40_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_ew40_1";

    /**
     * 罗伊-人形僵尸
     */
    public static final int _roiid = 81209;

    /**
     * 罗伊
     */
    public static final int _roi2id = 70957;

    /**
     * 巴休
     */
    public static final int _baschid = 70964;

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1 困惑的巴休</font><BR> <BR>
     * 当玩家们来到魔族神殿时，会于入口处附近看到一位骑士不知所措的呆站在一旁，前去打听，才知道远征队有不少人被捉了，并且<font
     * fg=0000ff>亚丁皇家法师“罗伊”</font>在与他逃难时失散了，希望玩家能帮助他，找到罗伊。<BR> <BR> 任务目标：<BR>
     * 与巴休探听消息，并深入魔族神殿探险<BR> <BR> <img src="#1210"></img> <font fg=66ff66>步骤.2
     * 谜样的僵尸</font><BR> <BR> 在快到魔族神殿的迷宫途中，突然遇到了一个有名子的人形僵尸()，才惊觉原来是<font
     * fg=0000ff>巴休</font>提过的<font fg=0000ff>罗伊</font>本人，于是立即帮他施展<font
     * fg=0000ff>
     * 魔法相消术</font>，解除他身上的变身诅咒。事后才知道他是在逃亡途中，被名为堕落的高等魔族给变身的，好再有我们的相助，才能以解除困境。<BR>
     * 之后只要将他带到巴休身边，即会取得罗伊的袋子。<BR> <BR> 任务目标：<BR>
     * 找到人形僵尸，并对他施展魔法相消术，让罗伊变回人貌，并引导他回到巴休身边，即可取得报酬<BR> <BR> 相关物品：<BR> <font
     * fg=ffff00>罗伊的袋子 x 1</font><BR> <BR>
     */
    private EWLv40_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new EWLv40_1();
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
