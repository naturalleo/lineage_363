package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:妖精族传说中的弓 (妖精45级以上官方任务)
 * 
 * @author daien
 * 
 */
public class ElfLv45_2 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(ElfLv45_2.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_e45_2";

    /**
     * 独角兽
     */
    public static final int _npcId = 91123;

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1　套出制作秘方 </font><BR> <BR>
     * 到妖精森林寻找“<font fg=0000ff>罗宾孙</font>(33031 32344)
     * ”，跟他询问妖精族传说中的弓，会很不想理你。<BR> 之后准备1瓶苹果汁给他套出<font
     * fg=0000ff>炽炎天使弓</font>的制作方法与材料，即可获得“<font
     * fg=0000ff>罗宾孙的推荐书</font>”和“<font fg=0000ff>罗宾孙的便条纸</font>”。<BR> <BR>
     * 注意事项：<BR> 第一阶段制作材料内容：<BR> 神圣独角兽之角 4个 (梦幻之岛)<BR> 火之气息 30个 (拉斯塔巴德)<BR> 水之气息
     * 30个 (拉斯塔巴德)<BR> 土之气息 30个 (拉斯塔巴德)<BR> 风之气息 30个 (拉斯塔巴德)<BR> 闇之气息 30个
     * (拉斯塔巴德)<BR> 精灵之泪 20个 (妖精森林洞穴)<BR> 月光之气息 1个 (伊娃神殿)<BR> <BR> 任务目标：<BR>
     * 找到罗宾孙，并给他一瓶苹果汁给他套出制作秘方<BR> <BR> 相关物品：<BR> <font fg=ffff00>苹果 汁 x
     * 1</font><BR> <font fg=ffff00>罗宾孙的推荐书 x 1</font><BR> <font
     * fg=ffff00>罗宾孙的便条纸 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.2　寻求月光之气息 </font><BR> <BR> 到海音地监的伊娃神殿寻找“<font
     * fg=0000ff>神官知布烈</font>(32740 32800)”请求<font
     * fg=0000ff>月光之气息</font>，但他告知需先解救受水龙诅咒的<font fg=0000ff>巫女莎尔</font>。<BR>
     * 并且神官知布烈告诉你要有<font
     * fg=0000ff>伊娃的圣水</font>才能解救巫女莎尔，而制作伊娃的圣水需要伊娃短剑和精灵之泪10颗。<BR>
     * 当搜集好伊娃短剑和精灵之泪10颗并交给神官知布烈，即可获得伊娃的圣水。<BR> <BR> 注意事项：<BR> 伊娃短剑制作材料内容：<BR>
     * 品质钻石 10个<BR> 品质红宝石 10个<BR> 品质蓝宝石 10个<BR> 品质绿宝石 10个<BR> <BR>
     * 伊娃的圣水制作材料内容：<BR> 伊娃短剑 1个<BR> 精灵之泪 10个<BR> <BR> 任务目标：<BR>
     * 到伊娃神殿找神官知布烈，并搜集材料制作伊娃的圣水<BR> <BR> 相关物品：<BR> <font fg=ffff00>品质钻石 x
     * 10</font><BR> <font fg=ffff00>品质红宝石 x 10</font><BR> <font fg=ffff00>品质蓝宝石
     * x 10</font><BR> <font fg=ffff00>品质绿宝石 x 10</font><BR> <font
     * fg=ffff00>精灵之泪 x 10</font><BR> <font fg=ffff00>伊娃的圣水 x 1</font><BR> <font
     * fg=ffff00>伊娃短剑 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.3　解救巫女莎尔 </font><BR> <BR> 到海音地监4楼的海底去寻找“<font
     * fg=0000ff>受诅咒的巫女莎尔</font>”，并在攻击他之前先饮用伊娃的圣水才能解救巫女莎尔。<BR>
     * 击败受诅咒的巫女莎尔，可获得解救成功的证明“<font fg=0000ff>莎尔之戒</font>”。<BR>
     * 将莎尔之戒拿回去给伊娃神殿的神官知布烈，即可获得月光之气息。<BR> <BR> 注意事项：<BR>
     * 在攻击受诅咒的巫女莎尔之前，请务必先饮用伊娃的圣水<BR> <BR> 任务目标：<BR>
     * 到海底击败受诅咒的巫女莎尔，并将获得的莎尔之戒交给神官知布烈，即可获得月光之气息<BR> <BR> 相关怪物：<BR> <font
     * fg=ffff00>Lv.43　 受诅咒的巫女莎尔</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>月光之气息
     * x 1</font><BR> <font fg=ffff00>伊娃的圣水 x 1</font><BR> <BR> <img
     * src="#1210"></img> <font fg=66ff66>步骤.4　后续的制作材料 </font><BR> <BR>
     * 在搜集完第一阶段制作材料之后，回去妖精森林找罗宾孙，他会给你“<font
     * fg=0000ff>罗宾孙之戒</font>”以及写有第二批制作材料的“<font fg=0000ff>罗宾孙的便条纸</font>”。<BR>
     * 将第二批材料搜集齐全并交给罗宾孙，即可获得炽炎天使弓。<BR> <BR> 注意事项：<BR> 第二阶段制作材料内容：<BR> 格利芬羽毛
     * 30个<BR> 米索莉线 40个<BR> 覆上奥里哈鲁根的角 1个<BR> 奥里哈鲁根金属板 12个<BR> 高品质钻石 1个<BR>
     * 高品质红宝石 1个<BR> 高品质蓝宝石 1个<BR> 高品质绿宝石 1个<BR> <BR> 任务目标：<BR>
     * 将第一批材料交给罗宾孙，之后开始搜集第二批材料并交给罗宾孙，即可获得炽炎天使弓<BR> <BR> 相关物品：<BR> <font
     * fg=ffff00>高品质钻石 x 1</font><BR> <font fg=ffff00>高品质红宝石 x 1</font><BR>
     * <font fg=ffff00>高品质蓝宝石 x 1</font><BR> <font fg=ffff00>高品质绿宝石 x
     * 1</font><BR> <font fg=ffff00>米索莉线 x 40</font><BR> <font fg=ffff00>格利芬羽毛 x
     * 30</font><BR> <font fg=ffff00>奥里哈鲁根金属板 x 12</font><BR> <font
     * fg=ffff00>覆上奥里哈鲁根的角 x 1</font><BR> <font fg=ffff00>炽炎天使弓 x 1</font><BR>
     * <font fg=ffff00>罗宾孙的便条纸 x 1</font><BR> <font fg=ffff00>罗宾孙之戒 x
     * 1</font><BR> <BR>
     */
    private ElfLv45_2() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new ElfLv45_2();
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
        try {
            // 任务解除必须移除的道具
            final int[] delItemIds = new int[] { 41348,// 罗宾孙的推荐书
                    41346,// 罗宾孙的便条纸 1
                    41347,// 罗宾孙的便条纸 2
                    41354,// 伊娃的圣水
                    41353,// 伊娃短剑
                    41349,// 莎尔之戒
                    41351,// 月光之气息
                    41350,// 罗宾孙之戒
            };

            for (final int delItemId : delItemIds) {
                pc.getInventory().delQuestItem(delItemId);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
