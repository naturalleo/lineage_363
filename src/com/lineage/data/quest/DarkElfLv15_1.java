package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:皮尔斯的忧郁 (黑暗妖精15级以上官方任务)
 * 
 * @author daien
 * 
 */
public class DarkElfLv15_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(DarkElfLv15_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_d15_1";

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1 烦恼的皮尔斯 </font><BR> <BR>
     * 在沉默洞穴找到<font fg=0000ff>皮尔斯</font>(32858,
     * 32880)，他会跟玩家说由于他需要大量的提炼过的黑魔石，但又碍于自己十分不善长提炼黑魔石
     * ，因次如果玩家能够提供二级以上的黑魔石给他，他会拿出私家物品来和你交换。<BR> <BR> 二级黑魔石 → 银飞刀(1,000)<BR>
     * 三级黑魔石 → 重飞刀(2,000)<BR> 四级黑魔石 → 皮尔斯的礼物<BR> 五级黑魔石 → 真铁手甲<BR> <BR> 任务目标：<BR>
     * 提炼黑魔石来和皮尔斯换取物品<BR> <BR> 相关物品：<BR> <font fg=ffff00>二级黑魔石 x 1<BR> <font
     * fg=ffff00>三级黑魔石 x 1<BR> <font fg=ffff00>四级黑魔石 x 1<BR> <font
     * fg=ffff00>五级黑魔石 x 1<BR> <font fg=ffff00>银飞刀 x 1000<BR> <font
     * fg=ffff00>重飞刀 x 2000<BR> <font fg=ffff00>真铁手甲 x 1<BR> <font
     * fg=ffff00>皮尔斯的礼物 x 1<BR> <BR>
     */
    private DarkElfLv15_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new DarkElfLv15_1();
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
