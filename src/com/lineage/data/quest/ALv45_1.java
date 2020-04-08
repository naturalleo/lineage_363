package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:毒蛇之牙的名号(全职业45级任务)
 * 
 * @author daien
 * 
 */
public class ALv45_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(ALv45_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_a45_1";

    /*
     * <body> <img src="#1210"></img> <font fg=66ff66>步骤.1　取得骑士团的信任</font><BR>
     * <BR> 前往海音村找佣兵团长多文(33592 33230)对话，他会要玩家前往调查海音地监恶灵骚动的原因。<BR>
     * 到海音地监1楼~3楼，狩猎海音的恶灵，搜集5个亡者的信件。<BR> 将亡者的信件带回去交给佣兵团长多文，可以获得佣兵团长多文的推荐书。<BR>
     * <BR> 注意事项：<BR> ※要重复该任务身上不能有毒蛇之牙披肩<BR> <BR> 任务目标：<BR>
     * 完成佣兵团长多文的委托，取得佣兵团长多文的推荐书，好跟骑士团长帝伦接洽任务<BR> <BR> 相关怪物：<BR> <font
     * fg=ffff00>Lv.25　 海音的恶灵</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>亡者的信件 x
     * 5</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.2　骑士团的试炼</font><BR> <BR> 前往海音村旅馆旁找骑士团长帝伦(33590
     * 33280)对话，将佣兵团长多文的推荐书交给他。<BR> 骑士团长帝伦会跟玩家说，需要花100万或是用梅杜莎之血证明实力来换取教本。<BR>
     * 到海音地监2楼，狩猎受诅咒的 梅杜莎，可以获得梅杜莎之血。<BR> 将100万或梅杜莎之血交给骑士团长帝伦，可以获得帝伦之教本。<BR> <BR>
     * 任务目标：<BR> 完成骑士团长帝伦的委托，取得梅杜莎之血，好跟佣兵团长多文进行下一阶段任务<BR> <BR> 相关怪物：<BR> <font
     * fg=ffff00>Lv.36　 受诅咒的 梅杜莎</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>金币 x
     * 1000000</font><BR> <font fg=ffff00>梅杜莎之血 x 1</font><BR> <font
     * fg=ffff00>帝伦之教本 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.3　训练骑士的资格(1)</font><BR> <BR> 前往海音村找佣兵团长多文(33592
     * 33230)对话，将帝伦之教本交给他。<BR> 佣兵团长多文会跟玩家要求取得法利昂的血痕，来当作加入训练骑士的资格证明。<BR>
     * 狩猎海音地监的怪物，可以获得法利昂的血痕。<BR> 将法利昂的血痕交给佣兵团长多文，可以获得训练骑士披肩 (1)。<BR> <BR>
     * 任务目标：<BR> 将帝伦之教本交给佣兵团长多文，取得法利昂的血痕换取训练骑士披肩 (1)<BR> <BR> 相关怪物：<BR> <font
     * fg=ffff00>Lv.20　 受诅咒的 蟑螂人</font><BR> <font fg=ffff00>Lv.22　 受诅咒的
     * 穴居人</font><BR> <font fg=ffff00>Lv.24　 受诅咒的 巨鼠</font><BR> <font
     * fg=ffff00>Lv.28　 受诅咒的 鼠人</font><BR> <font fg=ffff00>Lv.30　 受诅咒的
     * 多眼怪</font><BR> <font fg=ffff00>Lv.32　 受诅咒的 蛇女 (蓝)</font><BR> <font
     * fg=ffff00>Lv.32　 受诅咒的 蛇女 (绿)</font><BR> <font fg=ffff00>Lv.35　 受诅咒的
     * 蜥蜴人</font><BR> <font fg=ffff00>Lv.36　 受诅咒的 梅杜莎</font><BR> <font
     * fg=ffff00>Lv.42　 受诅咒的 人鱼</font><BR> <font fg=ffff00>Lv.45　
     * 水之精灵</font><BR> <font fg=ffff00>Lv.45　 受诅咒的 水精灵王</font><BR> <font
     * fg=ffff00>Lv.50　 卡普</font><BR> <font fg=ffff00>Lv.65　 巨大蜈蚣</font><BR>
     * <BR> 相关物品：<BR> <font fg=ffff00>训练骑士披肩 (1) x 1</font><BR> <font
     * fg=ffff00>法利昂的血痕 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.4　训练骑士的资格(2)</font><BR> <BR>
     * 佣兵团长多文会跟玩家要求取得水中的水，来当作训练骑士的升级试炼。<BR> 到海音地监 4楼(海底)狩猎水之精灵 ，可以获得水中的水。<BR>
     * 将水中的水交给佣兵团长多文，可以获得训练骑士披肩 (2)。<BR> <BR> 任务目标：<BR>
     * 狩猎水之精灵，取得水中的水跟佣兵团长多文换取训练骑士披肩 (2)<BR> <BR> 相关怪物：<BR> <font
     * fg=ffff00>Lv.45　 水之精灵</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>训练骑士披肩
     * (2) x 1</font><BR> <font fg=ffff00>水中的水 x 1</font><BR> <BR> <img
     * src="#1210"></img> <font fg=66ff66>步骤.5　毒蛇之牙的名号</font><BR> <BR>
     * 佣兵团长多文会跟玩家要求取得酸性液体，来当作取得毒蛇之牙名号的证明。<BR> 到海音地监 4楼(海底)狩猎巨大蜈蚣 ，可以获得酸性液体。<BR>
     * 将酸性液体交给佣兵团长多文，可以获得毒蛇之牙披肩，完成取得毒蛇之牙名号的任务。<BR> <BR> 任务目标：<BR>
     * 狩猎巨大蜈蚣，取得酸性液体跟佣兵团长多文换取毒蛇之牙披肩，完成任务<BR> <BR> 相关怪物：<BR> <font
     * fg=ffff00>Lv.65　 巨大蜈蚣</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>毒蛇之牙披肩 x
     * 1</font><BR> <font fg=ffff00>酸性液体 x 1</font><BR> <BR> <br> <img
     * src="#331" action="index"> </body>
     */
    private ALv45_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new ALv45_1();
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
