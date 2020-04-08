package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:取回间谍的报告书 (法师50级以上官方任务)
 * 
 * @author daien
 * 
 */
public class WizardLv50_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(WizardLv50_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_w50_1";

    /**
     * 迪嘉勒廷的男间谍(欧姆民兵)
     */
    public static final int _npcId = 80013;

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1　迪嘉勒廷的委托 </font><BR> <BR>
     * 到象牙塔 3楼寻找<font fg=0000ff>迪嘉勒廷</font>，他会委托玩家们到魔族神殿，寻找<font
     * fg=0000ff>失联的间谍</font>(32884, 32947)附近。<BR> <BR> 注意事项：<BR>
     * 进行该试炼之前，必须已经完成15级、30级和45级的试炼<BR> <BR> 任务目标：<BR> 跟迪嘉勒廷接洽任务，并前往魔族神殿<BR>
     * <BR> <img src="#1210"></img> <font fg=66ff66>步骤.2　失联的间谍 </font><BR> <BR>
     * 前往魔族神殿寻找伪装成<font fg=0000ff>欧姆民兵</font>样子的间谍，并且需使用<font
     * fg=0000ff>魔法相消术</font>来分辨原来的样子。<BR> 和间谍谈过之后，才知道<font
     * fg=0000ff>间谍报告书</font>似乎被魔族神殿的怪物们捡走了。<BR> 因此需要玩家们帮忙找回来。<BR> <BR>
     * 注意事项：<BR> 不能攻击的欧姆民兵(重装欧姆外型)，就是要寻找的间谍，需要使用魔法相消术之后(变成警卫)才能对话<BR> <BR>
     * 任务目标：<BR> 找到失联的间谍，并探听下一个任务<BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.3　间谍报告书 </font><BR> <BR> 狩猎魔族神殿的怪物，直到找回间谍报告书，即可回到象牙塔
     * 3楼寻找迪嘉勒廷。<BR> 并将间谍报告书转交给迪嘉勒廷，这回才发现不死魔族能够不断重生的线索。<BR>
     * 之后迪嘉勒廷会委托玩家前往魔族神殿寻找再生圣殿的入口。 <BR> <BR> 注意事项：<BR>
     * 续接任务请参考不死魔族再生的秘密，该任务为团体试炼，请先凑齐王族、骑士、妖精、法师4名成员。<BR> <BR> 任务目标：<BR>
     * 将间谍报告书交给迪嘉勒廷，并探听下一个目的地，50级试炼上半部结束<BR> <BR> 相关怪物：<BR> <font
     * fg=ffff00>Lv.37　 炎魔的思克巴</font><BR> <font fg=ffff00>Lv.41　
     * 炎魔的思克巴女皇</font><BR> <font fg=ffff00>Lv.44　 炎魔的小恶魔</font><BR> <font
     * fg=ffff00>Lv.51　 堕落的司祭 (烈炎蛙)</font><BR> <font fg=ffff00>Lv.51　
     * 炎魔的巴风特</font><BR> <font fg=ffff00>Lv.51　 堕落的司祭 (暗杀者)</font><BR> <font
     * fg=ffff00>Lv.53　 堕落的司祭 (镰刀手)</font><BR> <font fg=ffff00>Lv.53　
     * 炎魔的巴列斯</font><BR> <font fg=ffff00>Lv.54　 堕落的司祭 (喷毒兽)</font><BR> <font
     * fg=ffff00>Lv.56　 堕落的司祭 (三头魔)</font><BR> <font fg=ffff00>Lv.57　
     * 炎魔的分身</font><BR> <font fg=ffff00>Lv.61　 炎魔的恶魔</font><BR> <BR> 相关物品：<BR>
     * <font fg=ffff00>间谍报告书 x 1 </font><BR> <BR>
     */
    private WizardLv50_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new WizardLv50_1();
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
