package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;
import com.lineage.server.timecontroller.quest.W30_Timer;

/**
 * 说明:不死族的叛徒 (法师30级以上官方任务)
 * 
 * @author daien
 * 
 */
public class WizardLv30_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(WizardLv30_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务地图(法师试炼地监)
     */
    public static final int MAPID = 201;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_w30_1";

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1　不死族的背叛者 </font><BR> <BR>
     * 前往说话之岛与<font fg=0000ff>吉伦</font>对话得知，他的研究中需要<font
     * fg=0000ff>不死族的骨头</font>，
     * 但此骨头只有死亡骑士才有，但死亡骑士的身边出现了背叛者，把死亡骑士的骨头偷走了，你必须利用这个好机会
     * ，帮助吉伦完成！接受此任务，并前往冒险洞穴地下一楼，猎杀背叛的骷髅警卫兵取得不死族的钥匙，在冒险洞穴地下一楼与吉伦的徒弟<font
     * fg=0000ff>迪隆</font>对话，他将会带你前往不死族的地监。<BR> <BR> 注意事项：<BR>
     * 不死族的地监允许进入一个人(无法组队前往)<BR> <BR> 任务目标：<BR>
     * 寻找吉伦接受任务，并猎杀背叛的骷髅警卫兵取得不死族的钥匙，之后找迪隆前往不死族的地监<BR> <BR> 相关怪物：<BR> <font
     * fg=ffff00>Lv.10 背叛的骷髅警卫兵</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>不死族的钥匙
     * x 1</font><BR> <BR> <img src="#1210"></img> <font fg=66ff66>步骤.2　不死族的地监
     * </font><BR> <BR> 不死族的地监必须通过四道关卡： 第一道关卡： 使用不死族的钥匙，即可通过。 第二道关卡：
     * 通过第一个门之后，打倒人形僵尸，就可以获得僵尸钥匙，即可通过第二道门。 第三道关卡：
     * 进入第三个房间时，会发现阿鲁巴，必须对他使用相消术，阿鲁巴会变回骷髅，打倒它，就可以获得骷髅钥匙，通过第三道门。 第四道关卡：
     * 通过第四道门进入房间之后
     * ，会看到3个人形僵尸分别站在3个角落，玩家必须要将妖魔僵尸打死，并且使用造尸术，将妖魔僵尸变成人形僵尸，并且让它到没有怪物停留的第4个角落
     * ，这时位在房间中间通往的门就会开启。<BR> <BR> 注意事项：<BR>
     * 在不死族地监的区域中，无法喝水、不能装备武器及防具、魔力不会自动回复，但可以携带宠物
     * ，不过在这里无法召唤怪物，如果在进入地监前召唤，是可以带进去，在其中可用魔法迷魅或抓取宠物。<BR> <BR> 任务目标：<BR>
     * 通过4个关卡，猎取不死族背叛者取得不死族骨头<BR> <BR> 相关怪物：<BR> <font fg=ffff00>Lv.6
     * 人形僵尸</font><BR> <font fg=ffff00>Lv.10 妖魔僵尸</font><BR> <font
     * fg=ffff00>Lv.27 不死族背叛者</font><BR> <font fg=ffff00>Lv.35 骷髅</font><BR>
     * <BR> 相关物品：<BR> <font fg=ffff00>僵尸钥匙 x 1</font><BR> <font fg=ffff00>骷髅钥匙 x
     * 1</font><BR> <font fg=ffff00>不死族骨头 x 1</font><BR> <BR> <img
     * src="#1210"></img> <font fg=66ff66>步骤.3　缺少的材料 </font><BR> <BR>
     * 拿到不死族的骨头前往说话之岛与吉伦对话
     * ，才发现，原本吉伦要交给试炼者的神秘道具缺少神秘水晶球当作材料…辗转得知，这贵重的神秘的水晶球是由在古鲁丁地下监狱7楼的欧林所贩卖
     * ，得到所有物品之后交给吉伦，可以得到神秘魔杖与不死族的骨头碎片。<BR> <BR> 注意事项：<BR>
     * 神秘水晶球在完成不死族的地监任务之前，请先不要购买，以免流程错误造成系统判断试炼错误。<BR> <BR> 任务目标：<BR>
     * 购买神秘水晶球，拿会去交给吉伦，取得神秘魔杖与骨头材料的碎片<BR> <BR> 相关物品：<BR> <font
     * fg=ffff00>不死族的骨头碎片 x 1</font><BR> <font fg=ffff00>神秘水晶球 x 1</font><BR>
     * <font fg=ffff00>神秘魔杖 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.4　神秘魔杖的秘密 </font><BR> <BR> 前往象牙塔3楼拜访<font
     * fg=0000ff>塔拉斯</font
     * >，得知手上的神秘魔杖并没有完成。给他由吉伦给你的神秘魔杖与不死族的骨头碎片，他会帮你把神秘魔杖强化，即可得到水晶魔杖。<BR> <BR>
     * 任务目标：<BR> 将神秘魔杖与不死族的骨头碎片交给塔拉斯，并取得奖励<BR> <BR> 相关物品：<BR> <font
     * fg=ffff00>水晶魔杖 x 1</font><BR> <BR>
     */
    private WizardLv30_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new WizardLv30_1();
    }

    @Override
    public void execute(L1Quest quest) {
        try {
            // 设置任务
            QUEST = quest;

            // 任务时间轴
            final W30_Timer w30Timer = new W30_Timer();
            w30Timer.start();

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
