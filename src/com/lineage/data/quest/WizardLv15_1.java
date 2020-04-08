package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:詹姆的请求 (法师15级以上官方任务)
 * 
 * @author daien
 * 
 */
public class WizardLv15_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(WizardLv15_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_w15_1";

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1 力量转移研究 </font><BR> <BR>
     * 到说话之岛找<font
     * fg=0000ff>詹姆</font>，可以得知他正在研究将不死族骨骸上的力量转移到物品上，此时玩家可以接受收集材料。<BR> <BR>
     * 任务目标：<BR> 寻找詹姆，并接受此任务<BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.2 狩猎食尸鬼 </font><BR> <BR>
     * 在说话之岛或其他地区，狩猎食尸鬼，有一定机率可获得食尸鬼的指甲和食尸鬼的牙齿
     * 。之后拿回去交给交给詹姆，即可得到受诅咒的魔法书。并再次寻问关于魔法能量之书的细节。<BR> <BR> 任务目标：<BR> 搜集<font
     * fg=0000ff>食尸鬼的指甲</font>和<font
     * fg=0000ff>食尸鬼的牙齿</font>，带回去给詹姆领取奖励，并寻问魔法能量之书的细节。<BR> <BR> 相关怪物：<BR> <font
     * fg=ffff00>Lv.16 食尸鬼</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>食尸鬼的指甲 x
     * 1</font><BR> <font fg=ffff00>食尸鬼的牙齿 x 1</font><BR> <font
     * fg=ffff00>受诅咒的魔法书 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.3 最后的步骤 </font><BR> <BR> 在说话之岛或其他地区，狩猎骷髅，有一定机率可获得<font
     * fg=0000ff>骷髅头</font>。之后拿回去交给交给詹姆，即可得到魔法能量之书。<BR> <BR> 任务目标：<BR>
     * 搜集骷髅头，带回去给詹姆，并领取<font fg=0000ff>魔法能量之书</font><BR> <BR> 相关怪物：<BR> <font
     * fg=ffff00>Lv.10 骷髅</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>骷髅头 x
     * 1</font><BR> <font fg=ffff00>魔法能量之书 x 1</font><BR> <BR>
     */
    private WizardLv15_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new WizardLv15_1();
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
