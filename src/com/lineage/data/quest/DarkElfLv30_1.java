package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:同族的背叛 (黑暗妖精30级以上官方任务)
 * 
 * @author daien
 * 
 */
public class DarkElfLv30_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(DarkElfLv30_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_d30_1";

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1　愤怒的伦得</font><BR> <BR>
     * 到达30级后就可以去寻找位在沉默洞穴的<font
     * fg=0000ff>伦得</font>(32862，32891)，对话之后得知他无法原谅那些与反王合作的黑暗妖精
     * ，所以他会麻烦帮他带回那些与反王合作的秘密名单，而这份名单流落在一位<font fg=0000ff>叛逃的刺客警卫</font>手中。<BR>
     * 随后到燃柳村左边的邪恶神殿附近绕绕
     * ，即可找到叛逃的刺客警卫，杀了他之后即可得到秘密名单。回去找伦得并把秘密名单交给他，他会给你暗杀名单之袋，并会请你杀了那些背叛者
     * ，并带回死亡之证来。 <BR> <BR> 任务目标：<BR> 与伦得接受任务，狩猎叛逃的刺客警卫取得秘密名单，回去交差继续接受任务<BR>
     * <BR> 相关怪物：<BR> <font fg=ffff00>Lv.28　 叛逃的刺客警卫</font><BR> <BR> 相关物品：<BR>
     * <font fg=ffff00>秘密名单 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.2　猎杀背叛者</font><BR> <BR> 在打开暗杀名单之袋，会取得七个暗杀名单，分别要到<font
     * fg=0000ff>古鲁丁村</font>(32620，32641)、<font
     * fg=0000ff>燃柳村</font>(32730，32426)、<font
     * fg=0000ff>肯特村</font>(33046，32806)、<font
     * fg=0000ff>风木村</font>(32580，33260)、<font
     * fg=0000ff>海音都市</font>(33447，33476)、<font
     * fg=0000ff>亚丁城镇</font>(34215，33195)及<font
     * fg=0000ff>奇岩村</font>(33513，32890)去找寻暗杀的对象。<BR> 取得<font
     * fg=0000ff>死亡之证</font>之后，回去找伦得，并把死亡誓约交给他，即可得到伦得之袋。打开伦得之袋，
     * 即可得到黑暗精灵水晶(行走加速)和影子手套。再次与伦得对话，得知等到自己的能力成熟时就可以去寻找布鲁迪卡的帮助。 <BR> <BR>
     * 注意事项：<BR> 进行暗杀动作前必须先接受伦得施放的古代咒术。<BR>
     * 在村庄各处寻找刺客时，需在魔法阵上使用暗杀名单才能将这些背叛者找出来。<BR>
     * 咒术为暂时性的，因此当失去效力时，必须再回去接受伦得施放的古代咒术。<BR> <BR>
     * 在杀完所有的背叛者之后，就会自动取得死亡誓约。由于背叛者并不会做抵抗，所以对付他们并不困难。<BR> <BR> 任务目标：<BR>
     * 接受伦得施放的古代咒术，并马上去狩猎投靠反王的黑暗妖精取得死亡誓约，回去交差取得奖励<BR> <BR> 相关怪物：<BR> <font
     * fg=ffff00>Lv.1　 投靠反王的黑暗妖精</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>伦得之袋
     * x 1</font><BR> <font fg=ffff00>死亡誓约 x 1</font><BR> <font fg=ffff00>影子手套 x
     * 1</font><BR> <font fg=ffff00>黑暗精灵水晶(行走加速) x 1</font><BR> <BR>
     */
    private DarkElfLv30_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new DarkElfLv30_1();
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
