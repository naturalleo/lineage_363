package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:小恶魔的可疑行动 (王族50级以上官方任务)
 * 
 * @author daien
 * 
 */
public class CrownLv50_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(CrownLv50_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_c50_1";

    /*
     * <img src="#1210"></img> <font fg=ffff66>步骤.1　迪嘉勒廷的委托 </font><BR> <BR>
     * 到象牙塔 3楼寻找<font fg=0000ff>迪嘉勒廷</font>，他会委托玩家们到<font fg=0000ff>傲慢之塔
     * 21楼</font>以上地区，调查出没的<font fg=0000ff>小恶魔</font>。<BR> <BR> 注意事项：<BR>
     * 进行该试炼之前，必须已经完成15级、30级和45级的试炼<BR> <BR> 任务目标：<BR> 跟迪嘉勒廷接洽任务，并前往傲慢之塔
     * 21楼以上狩猎小恶魔<BR> <BR> <img src="#1210"></img> <font fg=ffff66>步骤.2　调职命令书
     * </font><BR> <BR> 在狩猎小恶魔获得<font fg=0000ff>调职命令书</font>之后，即可回到象牙塔
     * 3楼寻找迪嘉勒廷。<BR> 并将调职命令书转交给迪嘉勒廷，这回才发现不死魔族能够不断重生的线索。<BR>
     * 之后迪嘉勒廷会委托玩家们潜入魔族神殿去找<font
     * fg=0000ff>行政官奇浩(32927,32830)</font>，并且为了方便潜入，会先将你变身为小恶魔。<BR> <BR>
     * 注意事项：<BR> 变身为小恶魔后，将不会被魔族神殿里的魔族攻击。<BR> <BR> 任务目标：<BR>
     * 将调职命令书交给迪嘉勒廷，并探听下一个目的地<BR> <BR> 相关怪物：<BR> <font fg=ffff00>Lv.44　
     * 小恶魔</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>调职命令书 x 1</font><BR> <BR>
     */

    private CrownLv50_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new CrownLv50_1();
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
