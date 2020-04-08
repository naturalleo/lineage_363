package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:达克马勒的威胁 (妖精30级以上官方任务)
 * 
 * @author daien
 * 
 */
public class ElfLv30_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(ElfLv30_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务地图(达克马勒地监)
     */
    public static final int MAPID_1 = 213;

    /**
     * 任务地图(黑暗妖精地监)
     */
    public static final int MAPID_2 = 211;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_e30_1";

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1　被偷的精灵书 </font><BR> <BR>
     * 跟<font fg=0000ff>迷幻森林之母</font>对话得知，都配杰诺被赶走之后，又出现的新的威胁，叫做<font
     * fg=0000ff>达克马勒</font>，他正在妖精森林的某处研究着不可告人的魔法，并将妖精森林的精灵书偷走，但是他总是神出鬼没，只有<font
     * fg=0000ff>精灵公主</font>可以看到他，因此就把他赶走并取回精灵书当成你的考验，把他找出来吧！<BR> 与<font
     * fg=0000f
     * f>迷幻森林之母</font>对话结束之后接下试炼，找到精灵公主(32970,32442)，她会试着帮你传送到达克马勒的房间，但是偶尔会失败
     * ，而被传送到黑暗精灵的洞窟，需想办法逃脱并重新尝试，便会抵达达克马勒的洞窟。<BR> <BR> 任务目标：<BR>
     * 与迷幻森林之母接受任务，寻找精灵公主传送到达克马勒的洞窟<BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.2　击退达克马勒 </font><BR> <BR> 达克马勒拥有强大的魔力，身旁守护着达克马勒的是强而有力的<font
     * fg=0000ff>土</font>、<font fg=0000ff>水</font>、<font
     * fg=0000ff>火</font>、<font
     * fg=0000ff>风之精灵</font>四种精灵，突破重围打倒达克马勒后取得受诅咒的精灵书。将受诅咒的精灵书带回给迷幻森林之母会获得妖精族宝物
     * ，将妖精族宝物点选使用之后，即可得到妖精的试炼道具精灵T恤和精灵水晶-召唤属性精灵。<BR> <BR> 任务目标：<BR>
     * 狩猎达克马勒取得受诅咒的精灵书，带回去交差领取奖励<BR> <BR> 相关怪物：<BR> <font fg=ffff00>Lv.27　
     * 达克马勒45343</font><BR> <font fg=ffff00>Lv.30　 达克马勒之土精灵45306</font><BR>
     * <font fg=ffff00>Lv.30　 达克马勒之风精灵45305</font><BR> <font fg=ffff00>Lv.30　
     * 达克马勒之水精灵45304</font><BR> <font fg=ffff00>Lv.30　 达克马勒之火精灵45303</font><BR>
     * <BR> 相关物品：<BR> <font fg=ffff00>受诅咒的精灵书 x 1</font><BR> <font
     * fg=ffff00>妖精族宝物 x 1</font><BR> <font fg=ffff00>精灵T恤 x 1</font><BR> <font
     * fg=ffff00>精灵水晶(召唤属性精灵) x 1</font><BR> <BR>
     */
    private ElfLv30_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new ElfLv30_1();
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
