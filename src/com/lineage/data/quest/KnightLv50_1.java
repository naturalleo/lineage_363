package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 精灵们的骚动 (骑士50级以上官方任务)
 * 
 * @author daien
 * 
 */
public class KnightLv50_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(KnightLv50_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_k50_1";

    /*
     * <img src="#1210"></img> <font fg=ffff66>步骤.1　迪嘉勒廷的委托 <BR> 到象牙塔 3楼寻找<font
     * fg=0000ff>迪嘉勒廷</font>，他会委托玩家们到大洞穴地区，调查出没的<font
     * fg=0000ff>黑暗妖精将军</font>。<BR> <BR> 注意事项：<BR>
     * 进行该试炼之前，必须已经完成15级、30级和45级的试炼<BR> <BR> 任务目标：<BR>
     * 跟迪嘉勒廷接洽任务，并前往大洞穴狩猎黑暗妖精将军<BR> <BR> <img src="#1210"></img> <font
     * fg=ffff66>步骤.2　丹特斯的召书 <BR> <BR> 在狩猎黑暗妖精将军获得<font
     * fg=0000ff>丹特斯的召书</font>之后，即可回到象牙塔 3楼寻找迪嘉勒廷。<BR>
     * 并将丹特斯的召书转交给迪嘉勒廷，但是由于线索不足。<BR> 之后迪嘉勒廷会委托玩家继续前往精灵墓穴，搜集10个精灵的私语。<BR> <BR>
     * 任务目标：<BR> 将丹特斯的召书交给迪嘉勒廷，并探听下一个任务<BR> <BR> 相关怪物：<BR> <font
     * fg=ffff00>Lv.43　 黑暗妖精将军 (1)</font><BR> <BR> 相关物品：<BR> <font
     * fg=ffff00>丹特斯的召书 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=ffff66>步骤.3　精灵的私语 </font><BR> <BR> 在<font
     * fg=0000ff>精灵墓穴</font>狩猎精灵类怪物凑齐10个<font fg=0000ff>精灵的私语</font>之后，即可回到象牙塔
     * 3楼寻找迪嘉勒廷。<BR> 并将10个精灵的私语转交给迪嘉勒廷，这回才发现不死魔族能够不断重生的线索。<BR>
     * 之后迪嘉勒廷会委托玩家前往魔族神殿寻找再生圣殿的入口。<BR> <BR> 注意事项：<BR>
     * 续接任务请参考不死魔族再生的秘密，该任务为团体试炼，请先凑齐王族、骑士、妖精、法师4名成员。<BR> <BR> 任务目标：<BR>
     * 将10个精灵的私语交给迪嘉勒廷，并探听下一个目的地，50级试炼上半部结束<BR> <BR> 相关怪物：<BR> <font
     * fg=ffff00>Lv.40　 火之牙</font><BR> <font fg=ffff00>Lv.40　 地之牙</font><BR>
     * <font fg=ffff00>Lv.40　 风之牙</font><BR> <font fg=ffff00>Lv.40　
     * 水之牙</font><BR> <font fg=ffff00>Lv.45　 风灵之主</font><BR> <font
     * fg=ffff00>Lv.45　 水灵之主</font><BR> <font fg=ffff00>Lv.45　 火灵之主</font><BR>
     * <font fg=ffff00>Lv.45　 地灵之主</font><BR> <font fg=ffff00>Lv.55　
     * 深渊风灵</font><BR> <font fg=ffff00>Lv.55　 深渊水灵</font><BR> <font
     * fg=ffff00>Lv.55　 深渊火灵</font><BR> <font fg=ffff00>Lv.55　 深渊地灵</font><BR>
     * <BR> 相关物品：<BR> <font fg=ffff00>精灵的私语 x 10</font><BR> <BR>
     */
    private KnightLv50_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new KnightLv50_1();
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
