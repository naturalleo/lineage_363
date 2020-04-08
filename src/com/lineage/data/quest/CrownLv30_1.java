package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 艾莉亚的请求 (王族30级以上官方任务)
 * 
 * @author daien
 * 
 */
public class CrownLv30_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(CrownLv30_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务地图(变种巨蚁地监)
     */
    public static final int MAPID = 217;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_c30_1";

    /*
     * <img src="#1210"></img> <font fg=ffff66>步骤.1 村民的烦恼 ：</font><BR> <BR>
     * 与风木村村长的女儿<font fg=0000ff>艾莉亚</font>对话 中得知，风木村遭受到沙漠中蚂 蚁的攻击，村民也被沙漠中的蚂
     * 蚁抓走，而这些蚂蚁的行为都是 受到蚂蚁女王来控制，所以希望 你能帮助他们，将蚂蚁女王赶出 沙漠，并且救出村民及她的父亲 。<BR> <BR>
     * 到达风木村寻找村长的女儿艾莉 亚得知风木村正受到沙漠中蚂蚁 的攻击，透过对话之后接下任务 ，来到沙漠右下的蚁穴进入洞穴
     * 遇到一只搜查蚂蚁，但是说着蚂 蚁的语言，此时须变身为巨蚁之 后，重新进入游戏再次与蚂蚁对 话，此时才得知攻击风木村民是 <font
     * fg=0000ff>变种蚂蚁</font>。<BR> <BR> 注意事项：<BR> 必须变身为巨蚁才能和搜查蚂蚁 对话<BR> <BR>
     * 任务目标：<BR> 与艾莉亚接受任务后，前往巨蚁 洞穴寻找搜查蚂蚁，并与他对话 接受任务<BR> <BR> <img
     * src="#1210"></img> <font fg=ffff66>步骤.2 蚂蚁的战争 ：</font><BR> <BR>
     * 接下委托之后，找寻附近的看守 蚂蚁，此时也必须变身为巨大兵 蚁重登后才能对话，它会帮你传 送到变种巨蚁女王的巢穴，里面
     * 有变种巨蚁女王，通过并打败变 种巨蚁女王后，得到了<font fg=0000ff>村民的遗 物</font>。<BR> <BR>
     * 注意事项：<BR> 建议多带血盟成员进入帮忙打。 <BR> 必须变身为巨大兵蚁才能和看守 蚂蚁对话。<BR> <BR> 任务目标：<BR>
     * 击败变种巨蚁女王取得村民的遗 物。<BR> <BR> 相关怪物：<BR> <font fg=ffff00>Lv.10
     * 变种巨大兵蚁</font><BR> <font fg=ffff00>Lv.20 变种巨蚁女皇</font><BR> <BR> 相关物品：<BR>
     * <font fg=ffff00>村民的遗物 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=ffff66>步骤.3 回报村民 ：</font><BR> <BR> 将村民的遗物带回给风木村村长 女儿<font
     * fg=0000ff>艾莉亚</font>，为了感谢你对村中 的帮助，便交给你回报的礼物艾 莉亚的回报，将艾莉亚的回报点
     * 选使用之后，即可得到王族的试 炼道具<font fg=0000ff>君主的威严</font>和 <font
     * fg=0000ff>魔法书(呼唤盟友)</font>。<BR> <BR> 任务目标：<BR> 将村民的遗物交给艾莉亚，取得 奖励。<BR>
     * <BR> 相关物品：<BR> <font fg=ffff00>君主的威严 x 1</font><BR> <font
     * fg=ffff00>魔法书(呼唤盟友) x 1</font><BR> <BR>
     */

    private CrownLv30_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new CrownLv30_1();
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
