package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:安塔瑞斯栖息地 (全职业80级任务副本)
 * 
 * @author daien
 * 
 */
public class ADLv80_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(ADLv80_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务地图(安塔瑞斯 洞穴)
     */
    public static final int MAPID = 1005;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_ad80_1";

    /*
     * <body> <img src="#1210"></img> <font fg=66ff66>步骤.1　寻找卡娜</font><BR> <BR>
     * 找到奇怪的村落居民<font fg=0000ff>卡娜</font>并完成委托。<BR> <BR> 注意事项：<BR> 卡娜：34036,
     * 32258<BR> <BR> 任务目标：<BR> 找到卡娜接受任务，并完成委托<BR> <BR> <img src="#1210"></img>
     * <font fg=66ff66>步骤.2　寻找龙之门扉</font><BR> <BR> 在找到龙之门扉，聚集<font
     * fg=0000ff>5名以上玩家组成队伍</font>与NPC对话，由队长执行任务。 <BR> <BR> 任务目标：<BR>
     * 进入安塔瑞斯栖息地执行副本<BR> <BR> 注意事项：<BR> 完成卡娜委托任务后奇怪的村落的其他居民(NPC)才会愿意与你说话<BR>
     * 副本每次服务器重开可以执行一次<BR> <BR> <br> <img src="#331" action="index"> </body>
     */
    private ADLv80_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new ADLv80_1();
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
                        // System.out.println("任务尚未开始 设置为开始");
                        pc.getQuest().set_step(QUEST.get_id(), 1);
                    }
                    // System.out.println("任务尚未开始 设置为开始AAAAAAAAAAAAA");

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

                /*
                 * final String questName = QUEST.get_questname(); // 3109:\f1%0
                 * 任务完成！ pc.sendPackets(new S_ServerMessage("\\fT" + questName +
                 * "任务完成！")); // 任务可以重复 if (QUEST.is_del()) { //
                 * 3110:请注意这个任务可以重复执行，需要重复任务，请在任务管理员中执行解除。 pc.sendPackets(new
                 * S_ServerMessage("\\fT请注意这个任务可以重复执行，需要重复任务，请在任务管理员中执行解除。"));
                 * 
                 * } else { // 3111:请注意这个任务不能重复执行，无法在任务管理员中解除执行。 new
                 * S_ServerMessage("\\fR请注意这个任务不能重复执行，无法在任务管理员中解除执行。"); }
                 */
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
