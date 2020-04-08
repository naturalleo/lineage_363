package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 拯救被幽禁的吉姆 (骑士30级以上官方任务)
 * 
 * @author daien
 * 
 */
public class KnightLv30_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(KnightLv30_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务地图(杰瑞德的试炼地监)
     */
    public static final int MAPID = 22;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_k30_1";

    /*
     * <img src="#1210"></img> <font fg=ffff66>步骤.1 解救吉姆 ：</font><BR> <BR>
     * 到沙漠商人的附近与<font fg=0000ff>马克</font>对话，出发到大陆地下监狱，在地下监狱7楼猎杀<font
     * fg=0000ff>食人妖精</font>，取得密室钥匙，到达<font
     * fg=0000ff>欧林</font>房后对着欧林面前的魔法书使用钥匙，便会立刻到达密室。但是<font
     * fg=0000ff>吉姆</font>却是讲着怪物的语言，此时必须变身为<font
     * fg=0000ff>骷髅</font>，重新登入游戏再次进入密室与吉姆对话。<BR> <BR> 注意事项：<BR>
     * 必须变身骷髅才能与吉姆交谈<BR> <BR> 任务目标：<BR> 与马克接受任务，猎杀食人妖精取得密室钥匙，与吉姆交谈。<BR> <BR>
     * 相关怪物：<BR> <font fg=ffff00>Lv.22 食人妖精</font><BR> <BR> 相关物品：<BR> <font
     * fg=ffff00>密室钥匙 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=ffff66>步骤.2 红骑士的试炼 ：</font><BR> <BR> 与吉姆对话后，前往说话之岛找<font
     * fg=0000ff>甘特</font>。甘特要求猎杀岛上的<font fg=0000ff>杨果里恩</font>，取得<font
     * fg=0000ff>杨果里恩之爪</font>，带回来给他便会获得红骑士之剑，他同时叫骑士前往大陆学习更高深的战斗技巧。<BR> <BR>
     * 任务目标：<BR> 与甘特接受任务，猎杀杨果里恩取得杨果里恩之爪，并回去交差换取奖励。<BR> <BR> 相关怪物：<BR> <font
     * fg=ffff00>Lv.18 杨果里恩</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>杨果里恩之爪 x
     * 1</font><BR> <font fg=ffff00>红骑士之剑 x 1</font><BR> <BR> <img
     * src="#1210"></img> <font fg=ffff66>步骤.3 银骑士的试炼 ：</font><BR> <BR>
     * 前往大陆银骑士之村，找寻<font fg=0000ff>杰瑞德</font>与他对话接受试炼，通知去找<font
     * fg=0000ff>守门人</font>，进入试炼地监，进入之后猎杀守护在外的蛇女得到进入蛇女房间的钥匙，进入房间击倒<font
     * fg=0000ff>蛇女</font>，获得蛇女之鳞，之后带回给杰瑞德便可以得到返生药水。将返生药水交给地下监狱密室中的<font
     * fg=0000ff>吉姆</font>，可以获得感谢信，把感谢信交给杰瑞德之后，即可得到骑士的试炼道具红骑士之盾。<BR>
     * 与吉姆交谈之后才知道这是在测试玩家们是否有资格成为红骑士的考验。<BR> <BR> 任务目标：<BR>
     * 与杰瑞德接受任务，猎取杰瑞德试炼地监的蛇女取得蛇女之鳞，回去交差获得返生药水，去找吉姆让他恢复原貌，之后再回去找杰瑞德交差，领取奖励。<BR>
     * <BR> 相关怪物：<BR> <font fg=ffff00>Lv.22 蛇女</font><BR> <BR> 相关物品：<BR> <font
     * fg=ffff00>蛇女房间钥匙 x 1</font><BR> <font fg=ffff00>蛇女之鳞 x 1</font><BR> <font
     * fg=ffff00>返生药水 x 1</font><BR> <font fg=ffff00>感谢信 x 1</font><BR> <font
     * fg=ffff00>红骑士盾牌 x 1</font><BR> <BR>
     */
    private KnightLv30_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new KnightLv30_1();
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
