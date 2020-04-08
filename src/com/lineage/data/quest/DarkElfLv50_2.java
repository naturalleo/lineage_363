package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:暗黑的武器，死神之证 (黑暗妖精50级以上官方任务)
 * 
 * @author daien
 * 
 */
public class DarkElfLv50_2 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(DarkElfLv50_2.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_d50_2";

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1　暗黑的武器 </font><BR> <BR>
     * 达到限制条件的黑暗妖精，如果到威顿村找<font
     * fg=0000ff>卡立普</font>的话，会告诉你，他有办法帮你制作出暗黑系列的武器。为了制作出你想要的暗黑武器
     * ，必须收集下列对应的材料。<BR> <BR> 制作成品与对应材料：<BR> <img src="#896"></img> 暗黑钢爪<BR>
     * ├─幽暗钢爪 x1<BR> ├─诅咒的皮革(地) x10<BR> ├─龙之心 x1<BR> ├─冰之女王之心 x9<BR> ├─格兰肯之泪
     * x3<BR> ├─高品质绿宝石 x3<BR> └─金币 x100,000<BR> <BR> <img src="#898"></img>
     * 暗黑双刀<BR> ├─幽暗双刀 x1<BR> ├─诅咒的皮革(水) x10<BR> ├─龙之心 x1<BR> ├─冰之女王之心 x9<BR>
     * ├─格兰肯之泪 x3<BR> ├─高品质红宝石 x3<BR> └─金币 x100,000 <BR> <BR> <img
     * src="#900"></img> 暗黑十字弓<BR> ├─幽暗十字弓 x1 <BR> ├─诅咒的皮革(风) x10 <BR> ├─龙之心 x1
     * <BR> ├─冰之女王之心 x9 <BR> ├─格兰肯之泪 x3<BR> ├─高品质蓝宝石 x3 <BR> └─金币 x100,000<BR>
     * <BR> 注意事项：<BR> 必须要等级达到50以上的黑暗妖精，卡立普才会帮忙制作暗黑武器<BR> 只能三种武器选择一种<BR> <BR>
     * 任务目标：<BR> 搜集必需的材料，请卡立普帮忙制作暗黑武器<BR> <BR> 相关物品：<BR> <font fg=ffff00>金币 x
     * 100000</font><BR> <font fg=ffff00>高品质红宝石 x 3</font><BR> <font
     * fg=ffff00>高品质蓝宝石 x 3</font><BR> <font fg=ffff00>高品质绿宝石 x 3</font><BR>
     * <font fg=ffff00>格兰肯之泪 x 3</font><BR> <font fg=ffff00>冰之女王之心 x
     * 9</font><BR> <font fg=ffff00>龙之心 x 1</font><BR> <font fg=ffff00>诅咒的皮革(水)
     * x 10</font><BR> <font fg=ffff00>诅咒的皮革(风) x 10</font><BR> <font
     * fg=ffff00>诅咒的皮革(地) x 10</font><BR> <font fg=ffff00>幽暗十字弓 x 1</font><BR>
     * <font fg=ffff00>幽暗 钢爪 x 1</font><BR> <font fg=ffff00>幽暗 双刀 x 1</font><BR>
     * <BR>
     */
    private DarkElfLv50_2() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new DarkElfLv50_2();
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
