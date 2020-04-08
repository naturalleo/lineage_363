package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:远征队的遗物 (妖精15级以上官方任务)
 * 
 * @author daien
 * 
 */
public class ElfLv15_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(ElfLv15_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_e15_1";

    /*
     * <img src="#1210"></img> <font fg=ffff66>步骤.1 困惑的玛勒巴 ：</font><BR> <BR>
     * 在妖精森林里找到<font fg=0000ff>玛勒巴</font>(33053, 32315)，和他交谈后会给你玛勒巴的信。<BR>
     * 并且请玩家将信拿去给阿拉斯。<BR> <BR> 任务目标：<BR> 跟玛勒巴接洽任务，并探听下一个步骤<BR> <BR> 相关物品：<BR>
     * <font fg=ffff00>玛勒巴的信 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=ffff66>步骤.2 着急的阿拉斯 ：</font><BR> <BR> 在妖精森林的眠龙洞穴入口旁，可以找到<font
     * fg=0000ff>阿拉斯</font>，并且将玛勒巴的信交给他。<BR>
     * 之后，他会请求玩家们帮他搜集远征队的遗物，并给你阿拉斯的护身符。<BR> <BR> 任务目标：<BR> 跟阿拉斯接洽任务，并探听下一个步骤<BR>
     * <BR> 相关物品：<BR> <font fg=ffff00>阿拉斯的护身符 x 1</font><BR> <BR> <img
     * src="#1210"></img> <font fg=ffff66>步骤.3 远征队的遗物 ：</font><BR> <BR> 在眠龙洞穴
     * 1楼和眠龙洞穴 2楼狩猎污浊独眼巨人，可以获得远征队的遗物。<BR> 打开远征队的遗物可以获得污浊装备(全部6种)，将污浊装备拿去给<font
     * fg=0000ff>阿拉斯</font>，他会给予你同样款式的远征队装备。<BR> <BR> 污浊的弓 → 远征队弓<BR> 污浊的金甲 →
     * 远征队金甲<BR> 污浊的腕甲 → 远征队腕甲<BR> 污浊的头盔 → 远征队头盔<BR> 污浊的钢靴 → 远征队钢靴<BR> 污浊斗篷 →
     * 远征队斗篷<BR> <BR> 当凑齐六种远征队装备，在与阿拉斯交谈以及归还阿拉斯的护身符。<BR>
     * 之后他会给予阿拉斯的信，并且跟你说如果想要修好这些装备就去找玛勒巴。<BR> <BR> 注意事项：<BR>
     * 两楼层所获得的远征队的遗物，能够得到的污浊装备不相同，因此要凑齐整套污浊装备，需要两个楼层都搜集<BR> <BR> 任务目标：<BR>
     * 凑齐远征队装备，并获得阿拉斯的信<BR> <BR> 相关怪物：<BR> <font fg=ffff00>Lv.14
     * 污浊独眼巨人</font><BR> <font fg=ffff00>Lv.18 污浊独眼巨人</font><BR> <BR> 相关物品：<BR>
     * <font fg=ffff00>污浊的弓 x 1</font><BR> <font fg=ffff00>污浊的金甲 x 1</font><BR>
     * <font fg=ffff00>污浊的腕甲 x 1</font><BR> <font fg=ffff00>污浊的头盔 x 1</font><BR>
     * <font fg=ffff00>污浊的钢靴 x 1</font><BR> <font fg=ffff00>污浊斗篷 x 1</font><BR>
     * <font fg=ffff00>阿拉斯的信 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=ffff66>步骤.4 修复装备 ：</font><BR> <BR> 将阿拉斯的信交给玛勒巴，之后<font
     * fg=0000ff>玛勒巴</font>就会跟你说修复这些装备需要那些材料。<BR>
     * 接下来只要搜集好材料就能修复远征队装备，并且修好的装备会显示出自己的名子，证明修复者的身份。<BR> <BR> 远征队弓 → ID．妖精弓<BR>
     * 远征队金甲 → ID．妖精金甲<BR> 远征队腕甲 → ID．妖精腕甲<BR> 远征队头盔 → ID．妖精头盔<BR> 远征队钢靴 →
     * ID．妖精钢靴<BR> 远征队斗篷 → ID．妖精斗篷<BR> <BR> 任务目标：<BR>
     * 将阿拉斯的信交给玛勒巴，即可开始搜集材料修复装备<BR> <BR> 相关物品：<BR> <font fg=ffff00>ID．妖精弓 x
     * 1</font><BR> <font fg=ffff00>ID．妖精腕甲 x 1</font><BR> <font
     * fg=ffff00>ID．妖精金甲 x 1</font><BR> <font fg=ffff00>ID．妖精头盔 x 1</font><BR>
     * <font fg=ffff00>ID．妖精钢靴 x 1</font><BR> <font fg=ffff00>ID．妖精斗篷 x
     * 1</font><BR> <BR>
     */
    private ElfLv15_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new ElfLv15_1();
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
