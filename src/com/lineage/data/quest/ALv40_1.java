package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:魔法书的合成(全职业40级任务)
 * 
 * @author daien
 * 
 */
public class ALv40_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(ALv40_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_a40_1";

    /*
     * <body> <img src="#1210"></img> <font fg=66ff66>步骤.1　烦恼的拉比安尼 </font><BR>
     * <BR> 到说话之岛(32558，32965)找拉比安尼对话后得知他为了研究的材料而在烦恼。<BR>
     * 只要帮他搜集特定材料，就会赠送稀有的魔法书给玩家。<BR> <BR> 任务目标：<BR> 跟拉比安尼探听换取魔法书的材料<BR> <BR>
     * <img src="#1210"></img> <font fg=66ff66>步骤.2　治愈能量风暴 </font><BR> <BR>
     * 拉比安尼指出如果想要学习治愈能量风暴，只要凑齐下列材料给他，他可以用该魔法书做为报酬。<BR>
     * 材料：1个不死鸟之心、1个冰之女王之心、1个高仑之心、1个飞龙之心。<BR> <BR> 任务目标：<BR>
     * 凑齐材料和拉比安尼交换魔法书(治愈能量风暴)<BR> <BR> 相关物品：<BR> <font fg=ffff00>不死鸟之心 x
     * 1</font><BR> <font fg=ffff00>冰之女王之心 x 1</font><BR> <font fg=ffff00>高仑之心 x
     * 1</font><BR> <font fg=ffff00>飞龙之心 x 1</font><BR> <font
     * fg=ffff00>魔法书(治愈能量风暴) x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.3　激励士气 </font><BR> <BR>
     * 拉比安尼指出如果想要学习激励士气，只要凑齐下列材料给他，他可以用该魔法书做为报酬。<BR>
     * 材料：1个不死鸟之心、1个冰之女王之心、1个高仑之心、1个飞龙之心。<BR> <BR> 任务目标：<BR>
     * 凑齐材料和拉比安尼交换魔法书(激励士气)<BR> <BR> 相关物品：<BR> <font fg=ffff00>不死鸟之心 x
     * 1</font><BR> <font fg=ffff00>冰之女王之心 x 1</font><BR> <font fg=ffff00>高仑之心 x
     * 1</font><BR> <font fg=ffff00>飞龙之心 x 1</font><BR> <font
     * fg=ffff00>魔法书(激励士气) x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.4　冲击士气 </font><BR> <BR>
     * 拉比安尼指出如果想要学习冲击士气，只要凑齐下列材料给他，他可以用该魔法书做为报酬。<BR>
     * 材料：2个不死鸟之心、2个冰之女王之心、2个高仑之心、2个飞龙之心。<BR> <BR> 任务目标：<BR>
     * 凑齐材料和拉比安尼交换魔法书(冲击士气)<BR> <BR> 相关物品：<BR> <font fg=ffff00>不死鸟之心 x
     * 2</font><BR> <font fg=ffff00>冰之女王之心 x 2</font><BR> <font fg=ffff00>高仑之心 x
     * 2</font><BR> <font fg=ffff00>飞龙之心 x 2</font><BR> <font
     * fg=ffff00>魔法书(冲击士气) x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.5　钢铁士气 </font><BR> <BR>
     * 拉比安尼指出如果想要学习钢铁士气，只要凑齐下列材料给他，他可以用该魔法书做为报酬。<BR>
     * 材料：4个不死鸟之心、4个冰之女王之心、4个高仑之心、4个飞龙之心。<BR> <BR> 任务目标：<BR>
     * 凑齐材料和拉比安尼交换魔法书(钢铁士气)<BR> <BR> 相关物品：<BR> <font fg=ffff00>不死鸟之心 x
     * 4</font><BR> <font fg=ffff00>冰之女王之心 x 4</font><BR> <font fg=ffff00>高仑之心 x
     * 4</font><BR> <font fg=ffff00>飞龙之心 x 4</font><BR> <font
     * fg=ffff00>魔法书(钢铁士气) x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.6　格兰肯之泪</font><BR> <BR>
     * 拉比安尼指出要制作那个具有邪恶气息的水晶的话，需要很多贵重的材料。<BR> 材料：1个五级黑魔石、1个黑色米索莉、3个黑色血痕。<BR> <BR>
     * 任务目标：<BR> 凑齐材料和拉比安尼交换格兰肯之泪<BR> <BR> 相关物品：<BR> <font fg=ffff00>五级黑魔石 x
     * 1</font><BR> <font fg=ffff00>黑色米索莉 x 1</font><BR> <font fg=ffff00>黑色血痕 x
     * 3</font><BR> <BR> <br> <img src="#331" action="index"> </body>
     */
    private ALv40_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new ALv40_1();
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
