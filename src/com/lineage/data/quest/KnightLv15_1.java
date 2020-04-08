package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 瑞奇的抵抗 (骑士15级以上官方任务)
 * 
 * @author daien
 * 
 */
public class KnightLv15_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(KnightLv15_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_k15_1";

    /*
     * <img src="#1210"></img> <font fg=ffff66>步骤.1 反王的动向 ：</font><BR> <BR>
     * 与银骑士之村的<font fg=0000ff>瑞奇</font>对话后，了 解到反王的忠臣黑骑士手上有一 份誓约书，此誓约书的内容写明
     * 反王的动向，瑞奇指出若要预防 反王的企图，必需取的这份誓约 书。<BR> <BR> 与银骑士之村的瑞奇对话后，他 会要求你取得<font
     * fg=0000ff>黑骑士的誓约</font>，来 了解反王的动向，出村后寻找黑 骑士，打倒后取得黑骑士的誓约 ，将誓约书交给瑞奇后他会送你
     * <font fg=0000ff>骑士头巾</font>。<BR> <BR> 任务目标：<BR> 与瑞奇接受任务，狩猎黑骑士取
     * 得黑骑士的誓约，回去交差换取 奖励。<BR> <BR> 相关怪物：<BR> <font fg=ffff00>Lv.16
     * 黑骑士</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>黑骑士的誓约 x 1</font><BR> <font
     * fg=ffff00>骑士头巾 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=ffff66>步骤.2 强化骑士头巾 ：</font><BR> <BR> 此时再与瑞奇对话，得知他有位 叫<font
     * fg=0000ff>亚南</font>的铁匠朋友，若是你将<font fg=0000ff>古 老的交易文件</font>及<font
     * fg=0000ff>龙龟甲</font>交给他， 他就能帮你加强骑士头巾，出村 后寻找黑骑士以及龙龟，打倒他 们后会分别取得古老的交易文件
     * 及龙龟甲，将这两样物品交给亚 南后，可以将骑士头巾加强成 <font fg=0000ff>"红骑士头巾"</font>。<BR> <BR>
     * 任务目标：<BR> 与亚南接受任务，猎取黑骑士和 龙龟取得古老的交易文件和龙龟 甲，回去交差换取奖励。<BR> <BR> 相关怪物：<BR>
     * <font fg=ffff00>Lv.16 黑骑士</font><BR> <font fg=ffff00>Lv.24 龙龟</font><BR>
     * <BR> 相关物品：<BR> <font fg=ffff00>古老的交易文件 x 1</font><BR> <font fg=ffff00>龙龟甲
     * x 1</font><BR> <font fg=ffff00>红骑士头巾 x 1</font><BR> <BR>
     */
    private KnightLv15_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new KnightLv15_1();
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
