package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.datatables.lock.CharacterQuestReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:Chapter.0：穿越时空的探险
 * 
 * @author daien
 * 
 */
public class Chapter00 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(Chapter00.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "q_cha0_1";

    /*
     * <body> <img src="#1210"></img> <font fg=66ff66>步骤.1　幻术士的研究</font><BR>
     * <BR> 到说话之岛村庄找<font
     * fg=0000ff>尤丽娅</font>，他会告诉玩家幻术士们想要研究过去发生的事情来探讨世界各地的缘由。<BR> 谈话完他会交给你<font
     * fg=0000ff>时空之瓮</font>，并且每次使用会获得<font fg=0000ff>2个时空之玉</font>。<BR> <BR>
     * 注意事项：<BR> ※时空之瓶每22小时可以使用1次<BR> <BR> 任务目标：<BR>
     * 和说话之岛村庄的优利爱取得时空之瓶，并且开出时空之玉<BR> <BR> 相关物品：<BR> <font fg=ffff00>时空之瓮 x
     * 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.2　象牙塔的秘密研究所</font><BR> <BR> 将1个<font
     * fg=0000ff>时空之玉</font>和<font fg=0000ff>10,000金币</font>交给<font
     * fg=0000ff>尤丽娅</font>，会帮你传送到<font fg=0000ff>象牙塔的秘密研究所</font>。<BR>
     * 在象牙塔的秘密研究所可以接洽秘谭系列的故事副本。<BR> <BR> 任务目标：<BR>
     * 将1个时空之玉和10,000金币交给优利爱传送到象牙塔的秘密研究所<BR> <BR> 相关物品：<BR> <font fg=ffff00>金币 x
     * 10000</font><BR> <font fg=ffff00>时空之玉 x 1</font><BR> <BR> <br> <img
     * src="#331" action="index"> </body>
     */
    private Chapter00() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new Chapter00();
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
                CharacterQuestReading.get()
                        .delQuest(pc.getId(), QUEST.get_id());
                final String questName = QUEST.get_questname();
                // 3109:\f1%0 任务完成！
                pc.sendPackets(new S_ServerMessage("\\fT" + questName + "任务完成！"));
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
