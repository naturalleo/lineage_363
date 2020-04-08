package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 王族的自知 (王族15级以上官方任务)
 * 
 * @author daien
 * 
 */
public class CrownLv15_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(CrownLv15_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_c15_1";

    /*
     * <img src="#1210"></img> <font fg=ffff66>步骤.1 杰罗的试炼：</font><BR> <BR>
     * 与肯特村的<font fg=0000ff>杰罗</font>对话后，了解到 身为王族必需知道的事情，并且 为了证明你拥有成为王者的能力
     * ，杰罗将会给予你试炼 。<BR> <BR> 到肯特村与杰罗说话接下试炼后 ，出村寻找黑骑士搜索队，打倒 后取得搜索状，将搜索状交给<font
     * fg=0000ff>杰 罗</font>后获得<font fg=0000ff>"红色斗篷"</font>，并请你前 往寻找<font
     * fg=0000ff>甘特</font>。<BR> <BR> 注意事项：<BR> 黑骑士搜索队位在肯特村庄下方 出村庄之后，过桥右边的荒野，
     * 那里又称 食尸鬼出没地。<BR> <BR> 任务目标：<BR> 打败黑骑士搜索队取得搜索状， 之后拿回去跟杰罗领取奖励。<BR> <BR>
     * 相关怪物：<BR> <font fg=ffff00>Lv.16 黑骑士搜索队</font><BR> <BR> 相关物品：<BR> <font
     * fg=ffff00>搜索状 x 1</font><BR> <font fg=ffff00>红色斗篷 x 1</font><BR> <BR>
     * <img src="#1210"></img> <font fg=ffff66>步骤.2 甘特的试炼：</font><BR> <BR>
     * 到甘特的洞穴 (在说话之岛冒险洞窟入口上方) ，找到<font fg=0000ff>甘特</font>，甘特会要你猎杀高
     * 仑石头怪，将会随机取得<font fg=0000ff>"生命 的卷轴"</font>，带着卷轴回来与甘特 交谈，可得到 <font
     * fg=0000ff>魔法书(精准目标)</font>。<BR> <BR> 任务目标：<BR> 打败高仑石头怪取得生命的卷轴
     * ，之后拿回去跟甘特领取奖励。<BR> <BR> 相关怪物：<BR> <font fg=ffff00>Lv.13 石头高仑</font><BR>
     * <BR> 相关物品：<BR> <font fg=ffff00>生命的卷轴 x 1</font><BR> <font
     * fg=ffff00>魔法书(精准目标) x 1</font><BR> <BR>
     */
    private CrownLv15_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new CrownLv15_1();
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
