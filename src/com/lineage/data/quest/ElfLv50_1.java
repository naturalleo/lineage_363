package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:协助间谍大逃亡 (妖精50级以上官方任务)
 * 
 * @author daien
 * 
 */
public class ElfLv50_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(ElfLv50_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务地图(赛菲亚地监)
     */
    public static final int MAPID = 302;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_e50_1";

    /**
     * 迪嘉勒廷的女间谍
     */
    public static final int _npcId = 80012;

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1　迪嘉勒廷的委托 </font><BR> <BR>
     * 到象牙塔 3楼寻找<font fg=0000ff>迪嘉勒廷</font>，他会委托玩家们到沙漠的巨蚁洞，调查出没的<font
     * fg=0000ff>巨蚁</font>和<font fg=0000ff>巨大兵蚁</font>。<BR> <BR> 注意事项：<BR>
     * 进行该试炼之前，必须已经完成15级、30级和45级的试炼<BR> <BR> 任务目标：<BR>
     * 跟迪嘉勒廷接洽任务，并前往巨蚁洞狩猎巨蚁和巨大兵蚁<BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.2　古代黑妖之秘笈 </font><BR> <BR> 在狩猎巨蚁和巨大兵蚁获得<font
     * fg=0000ff>古代黑妖之秘笈</font>之后，即可回到象牙塔 3楼寻找迪嘉勒廷。<BR>
     * 并将巨蚁和巨大兵蚁转交给迪嘉勒廷，但是由于线索不足。<BR> 之后迪嘉勒廷会委托玩家继续前往大洞穴，帮助潜伏的间谍逃亡。<BR> <BR>
     * 任务目标：<BR> 将古代黑妖之秘笈交给迪嘉勒廷，并探听下一个任务<BR> <BR> 相关怪物：<BR> <font
     * fg=ffff00>Lv.12　 巨蚁</font><BR> <font fg=ffff00>Lv.20　 巨大兵蚁</font><BR>
     * <BR> 相关物品：<BR> <font fg=ffff00>古代黑妖之秘笈 x 1</font><BR> <BR> <img
     * src="#1210"></img> <font fg=66ff66>步骤.3　协助间谍大逃亡 </font><BR> <BR>
     * 在大洞穴抵抗军通往古代巨人之墓的传送点附近可以找到<font fg=0000ff>迪嘉勒廷的女间谍</font>(32864,
     * 32818)。<BR> 将迪嘉勒廷的女间谍带领到抵抗军村落的路途上，魔族暗杀团可能也会来攻击。<BR>
     * 安全带到抵抗军村落时将会收到密封的情报书，之后再将<font fg=0000ff>密封的情报书</font>拿回去给迪嘉勒廷。<BR> <BR>
     * 注意事项：<BR> 由于迪嘉勒廷的女间谍如果遭受攻击，进而失血致死，将必须重新护送一次<BR> <BR> 任务目标：<BR>
     * 协助间谍逃亡到抵抗军村落，获得密封的情报书，并回去找迪嘉勒廷<BR> <BR> 相关怪物：<BR> <font fg=ffff00>Lv.53　
     * 魔族暗杀团</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>密封的情报书 x 1</font><BR>
     * <BR> <img src="#1210"></img> <font fg=66ff66>步骤.4　崭新的线索 </font><BR> <BR>
     * 将密封的情报书交给迪嘉勒廷，这回才发现不死魔族能够不断重生的线索。<BR> 之后迪嘉勒廷会委托玩家前往魔族神殿寻找再生圣殿的入口。<BR>
     * <BR> 注意事项：<BR> 续接任务请参考不死魔族再生的秘密，该任务为团体试炼，请先凑齐王族、骑士、妖精、法师4名成员。<BR> <BR>
     * 任务目标：<BR> 将密封的情报书交给迪嘉勒廷，并探听下一个目的地，50级试炼上半部结束<BR> <BR>
     */
    private ElfLv50_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new ElfLv50_1();
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
