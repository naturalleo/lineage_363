package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:行迹可疑的妖魔们 (龙骑士15级以上官方任务)
 * 
 * @author daien
 * 
 */
public class DragonKnightLv15_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(DragonKnightLv15_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_dk15_1";

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1 长老赋予的任务 </font><BR> <BR>
     * 至贝希摩斯村庄找<font fg=0000ff>长老普洛凯尔</font>(32772 32811)，他会要求你取来3份妖魔密使身上的<font
     * fg=0000ff>妖魔搜索文件</font>。<BR> <BR> 任务目标：<BR> 跟长老普洛凯尔接任务，并探听任务地点<BR> <BR>
     * <img src="#1210"></img> <font fg=66ff66>步骤.2 妖魔密使 </font><BR> <BR>
     * 分别前往古鲁丁村庄祭坛、风木村庄及妖魔森林附近，寻找妖魔密使。<BR> 并且狩猎3个地区的妖魔密使，搜集3份不同妖魔搜索文件。<BR>
     * 之后回去找长老普洛凯尔。<BR> <BR> 注意事项：<BR>
     * 狩猎妖魔密使时，最好直接攻击妖魔密使，如果先击退妖魔密使护卫兵，则会发生妖魔密使逃跑的情况<BR> <BR> 任务目标：<BR>
     * 狩猎妖魔密使，搜集妖魔搜索文件<BR> <BR> 相关怪物：<BR> <font fg=ffff00>Lv.12
     * 妖魔密使护卫兵</font><BR> <font fg=ffff00>Lv.17 妖魔密使(妖魔森林)</font><BR> <font
     * fg=ffff00>Lv.17 妖魔密使(风木)</font><BR> <font fg=ffff00>Lv.17
     * 妖魔密使(古鲁丁)</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>妖魔搜索文件(古鲁丁) x
     * 1</font><BR> <font fg=ffff00>妖魔搜索文件(风木城) x 1</font><BR> <font
     * fg=ffff00>妖魔搜索文件(妖魔森林) x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.3 回报 </font><BR> <BR>
     * 将凑齐的妖魔搜索文件交给长老普洛凯尔，即可获得龙骑士双手剑和龙骑士书板(龙之护铠)。 <BR> 任务目标：<BR>
     * 将妖魔搜索文件转交给长老普洛凯尔，并获得奖励<BR> <BR> 相关物品：<BR> <font fg=ffff00>龙骑士书板(龙之护铠) x
     * 1</font><BR> <font fg=ffff00>龙骑士双手剑 x 1</font><BR> <BR>
     */
    private DragonKnightLv15_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new DragonKnightLv15_1();
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
