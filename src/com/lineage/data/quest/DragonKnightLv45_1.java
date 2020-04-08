package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:与幻术士缔结同盟 (龙骑士45级以上官方任务)
 * 
 * @author daien
 * 
 */
public class DragonKnightLv45_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(DragonKnightLv45_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_dk45_1";

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1　长老赋予的任务 </font><BR> <BR>
     * 至贝希摩斯找长老<font fg=0000ff>普洛凯尔</font>(32817 32831)，他会要求你前往<font
     * fg=0000ff>希培莉亚</font>与<font fg=0000ff>幻术士</font>们进行结盟任务。<BR>
     * 之后长老普洛凯尔会给予你<font fg=0000ff>普洛凯尔的第三次指令书</font>与长老<font
     * fg=0000ff>普洛凯尔的信件</font>及<font fg=0000ff>结盟瞬间移动卷轴</font>。<BR> <BR>
     * 任务目标：<BR> 跟长老普洛凯尔接洽任务，并探听任务地点<BR> <BR> 相关物品：<BR> <font fg=ffff00>结盟瞬间移动卷轴
     * x 1</font><BR> <font fg=ffff00>长老普洛凯尔的信件 x 1</font><BR> <font
     * fg=ffff00>普洛凯尔的第三次指令书 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.2　前往希培莉亚 </font><BR> <BR> 使用结盟瞬间移动卷轴即会立即传送至希培莉亚。<BR>
     * 进入希培莉亚后将长老普洛凯尔的信件交给长老<font fg=0000ff>希莲恩</font>(32772 32811)。<BR>
     * 长老希莲恩会通知你要先通过考验才肯结盟，并给你<font fg=0000ff>希莲恩的指令书</font>。<BR> <BR> 任务目标：<BR>
     * 跟长老希莲恩会面，并探听结盟考验的内容<BR> <BR> 相关物品：<BR> <font fg=ffff00>希莲恩的指令书 x
     * 1</font><BR> <BR> <img src="#1210"></img> <font fg=66ff66>步骤.3　狩猎雪怪
     * </font><BR> <BR> 在欧瑞附近狩猎雪怪，凑齐10个<font fg=0000ff>雪怪之心</font>。<BR>
     * 之后将10个雪怪之心交给长老希莲恩，即可获得<font fg=0000ff>幻术士同盟徽印</font>。<BR>
     * 在完成结盟手续之后，即可回去贝希摩斯找长老普洛凯尔。<BR> <BR> 任务目标：<BR>
     * 将10个雪怪之心交给长老希莲恩，并取得幻术士同盟徽印<BR> <BR> 相关怪物：<BR> <font fg=ffff00>Lv.30　
     * 雪怪</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>幻术士同盟徽印 x 1</font><BR> <font
     * fg=ffff00>雪怪之心 x 10</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.4　回报 </font><BR> <BR> 将幻术士同盟徽印拿回去给长老普洛凯尔，可获得<font
     * fg=0000ff>普洛凯尔的第二次信件</font>。<BR> 之后将普洛凯尔的第二次信件交给塔尔立昂(32828
     * 32844)，即可获得<font fg=0000ff>龙骑士斗篷</font>。<BR> <BR> 任务目标：<BR>
     * 将幻术士同盟徽印交给长老普洛凯尔，并取得奖励<BR> <BR> 相关物品：<BR> <font fg=ffff00>龙骑士斗篷 x
     * 1</font><BR> <font fg=ffff00>普洛凯尔的第二次信件 x 1</font><BR> <BR>
     */
    private DragonKnightLv45_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new DragonKnightLv45_1();
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
