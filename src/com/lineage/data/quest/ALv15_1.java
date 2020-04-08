package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:击退妖魔的契约(全职业15级任务)
 * 
 * @author daien
 * 
 */
public class ALv15_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(ALv15_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_a15_1";

    /*
     * <body> <img src="#1210"></img> <font fg=66ff66>步骤.1　签订契约 </font><BR> <BR>
     * 在燃柳村庄商人处的左边屋子里，可以找到带领人们开垦此地的<font
     * fg=0000ff>莱拉</font>，与他对谈会知道妖魔们一直再阻碍他们的开发，希望玩家们能协助扫荡妖魔。<BR> <BR> 任务目标：<BR>
     * 找到莱拉，签订契约<BR> <BR> <img src="#1210"></img> <font fg=66ff66>步骤.2　击退妖魔
     * </font><BR> <BR> 在妖魔森林狩猎妖魔类怪物即可取得图腾。<BR> <BR> 注意事项：<BR>
     * 必须单独击退妖魔才会掉落图腾<BR> <BR> 任务目标：<BR> 击退妖魔，取得图腾<BR> <BR> 相关怪物：<BR> <font
     * fg=ffff00>Lv.10　 甘地妖魔</font><BR> <font fg=ffff00>Lv.13　 罗孚妖魔</font><BR>
     * <font fg=ffff00>Lv.15　 阿吐巴妖魔</font><BR> <font fg=ffff00>Lv.15　
     * 都达玛拉妖魔</font><BR> <font fg=ffff00>Lv.17　 那鲁加妖魔</font><BR> <BR> 相关物品：<BR>
     * <font fg=ffff00>阿吐巴图腾 x 1</font><BR> <font fg=ffff00>那鲁加图腾 x 1</font><BR>
     * <font fg=ffff00>甘地图腾 x 1</font><BR> <font fg=ffff00>罗孚图腾 x 1</font><BR>
     * <font fg=ffff00>都达玛拉图腾 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.3　领取奖金 </font><BR> <BR> 当你觉得不想继续收集图腾时，可以回去村庄里将<font
     * fg=0000ff>图腾</font>交给<font
     * fg=0000ff>莱拉</font>领取奖金。领完奖金即结束此契约，如想还要还是可以继续签订契约直到下回领完奖金。<BR> <BR>
     * 注意事项：<BR> <font fg=ffff00>【阿吐巴图腾】价值：200金币</font><BR> <font
     * fg=ffff00>【那鲁加图腾】价值：100金币</font><BR> <font
     * fg=ffff00>【甘地图腾】价值：30金币</font><BR> <font
     * fg=ffff00>【罗孚图腾】价值：50金币</font><BR> <font
     * fg=ffff00>【都达玛拉图腾】价值：50金币</font><BR> <BR> 任务目标：<BR> 将图腾交给莱拉领取奖金 <BR> <BR>
     * <br> <img src="#331" action="index"> </body>
     */
    private ALv15_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new ALv15_1();
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
