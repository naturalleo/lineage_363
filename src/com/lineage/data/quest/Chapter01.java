package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:魔法师．哈汀(故事)
 * 
 * @author daien
 * 
 */
public class Chapter01 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(Chapter01.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 从前的说话之岛
     */
    public static final int MAPID = 9000;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "q_cha1_1";

    /*
     * <body> <img src="#1210"></img> <font fg=66ff66>步骤.1　象牙塔的秘密研究室</font><BR>
     * 请先参照Chapter.0：穿越时空的探险，抵达象牙塔的秘密研究室。<BR> 跟<font
     * fg=0000ff>尤基</font>交谈说想要回到从前的说话之岛探讨哈汀的秘密。<BR> 尤基 会要求玩家凑齐<font
     * fg=0000ff>5~8个人</font>才能前往从前的说话之岛。<BR> 之后他会帮玩家传送到从前的秘密研究室。<BR> <BR>
     * 注意事项：<BR> 尤基 ：传送到任务副本。<BR> 辅助研究员：传送到说话之岛。<BR> 纸人：贩售消耗品。<BR> <BR>
     * 任务目标：<BR> 凑齐5~8人组队和尤基 交谈传送到从前的秘密研究室<BR> <BR> <img src="#1210"></img>
     * <font fg=66ff66>步骤.2　从前的秘密研究室</font><BR> 随送到从前的秘密研究室，会先碰到哈汀。<BR>
     * 并且随着哈汀的说明之后会，按<font fg=0000ff>“alt + 2”</font>告别哈汀会出现要传送到下一个任务地点的魔法阵。<BR>
     * 队长：传送到从前的说话之岛<BR> 队友：传送到从前的说话之岛洞窟2楼<BR> <BR> 注意事项：<BR> 队长是扮演欧林<BR>
     * 队友则是扮演其他四色成员<BR> <BR> 任务目标：<BR> 和哈汀进行互动之后，队长和队员各自传送到下一个目的地<BR> <BR> <img
     * src="#1210"></img> <font fg=66ff66>步骤.3　队长篇</font><BR> <BR>
     * 队长会被传送到从前的说话之岛，并且遇到<font fg=0000ff>赛尼斯</font>进行互动。<BR>
     * 赛尼斯会要求玩家在他准备魔法的期间，帮他除掉身边出现的怪物。<BR> 完成之后会传送到恶魔召唤的房间和队友们会合进行决战。<BR> <BR>
     * 注意事项：<BR> 关于NPC的问答：<BR> <font fg=0000ff>“alt + 2”</font>表示要<BR> <font
     * fg=0000ff>“alt + 4”</font>表示不要<BR> 该处为结局分歧点之一，玩家的抉择会影响到结局的因素之一<BR>
     * 分歧说明请参考步骤5<BR> <BR> 任务目标：<BR> 守护赛尼斯完成魔法，之后会传送到队员身边会合<BR> <BR> <img
     * src="#1210"></img> <font fg=66ff66>步骤.4　队友篇</font><BR> <BR>
     * 除了队长，其他成员会被传送到从前的说话之岛洞窟2楼执行任务。<BR> <BR> 从起点一路进行下去会遇到4阶段的关卡。<BR>
     * 关卡1~关卡3都是要玩家们将关卡内的<font
     * fg=0000ff>四个光球地板</font>，同时有玩家站在上面，即可开启通往下一关卡的门。<BR> 关卡4则是要将房间里的<font
     * fg=0000ff>怪物全数清除</font>，即可开启通往最终关卡的门。<BR> <BR> 注意事项：<BR>
     * 关卡1~关卡4的完成方式，为结局分歧点之一<BR> 分歧说明请参考步骤5<BR>
     * 如果在限定时间内无法完成关卡1~关卡4，则副本宣告失败，全员传送回说话之岛<BR> ※地图上5个特殊地点是有机会在地上捡到哈汀日记的场所<BR>
     * <BR> 任务目标：<BR> 在限定时间内完成关卡1~关卡4，来到最终关卡的房间<BR> <BR> <img src="#1210"></img>
     * <font fg=66ff66>步骤.5　决战篇</font><BR> <BR> 来到最终关卡之后，队长会被传送过来和队友们会合。<BR>
     * 在最终关卡玩家们需要合力清除12回合出现的怪物们。<BR> 以下是各种分歧条件与结局：<BR> <BR> <font
     * fg=ffff00>【分歧0】最终头目-镰刀死神的使者(秘谭)</font><BR> 达成条件：最终关卡时，各阶段出现的怪物都残留4只以上<BR>
     * 条件达成：哈汀会说出“邪恶气息又拥挤过来了！请准备！”<BR> <BR> <font
     * fg=ffff00>【分歧1】最终头目-巴风特(秘谭)</font><BR> 达成条件：最终关卡1~11回合，迅速击败所有怪物<BR>
     * 基本上要在哈汀说话之前(两分钟内)，至少怪物要清到剩3只以下<BR> 条件达成：每回合两分钟内清掉怪物，并且哈汀没有说出【分歧0】那句话<BR>
     * <BR> <font fg=ffff00>【分歧2】赛尼斯(秘谭)不会出现</font><BR>
     * 达成条件：队长在“从前的说话之岛”和赛尼斯在说话时，都不理会<BR> 直到赛尼斯说出“【欧林】，是在渺视我吗?”，这时会按“alt +
     * 2”回应她<BR> 条件达成：在最终关卡清怪慢【分歧0】，清怪快【分歧1】，但是赛尼斯(秘谭)不会出现<BR> <BR> <font
     * fg=ffff00>【分歧3】最终头目-镰刀死神的使者(秘谭)、火焰之影(NPC)出现</font><BR>
     * 达成条件：再进行关卡1~关卡3的时候，要踏上红光球位置时，先踏上面三个<BR>
     * 直到赛尼斯或哈汀说出“笨”、“慢”、“耐心”、“快一点”...等字眼，这时再踏上最后一个<BR>
     * 之后赶进前往下一个关卡，再如法炮制通过关卡1~关卡3<BR> 最终关卡达成【分歧0】的条件<BR>
     * 条件达成：再击败镰刀死神的使者(秘谭)、赛尼斯(秘谭)之后，NPC火焰之影会出现<BR>
     * 由于通关后，如果太慢通过右下角已打开的黑墙，会拿不到奖励<BR> 因此建议先通过后再隔墙看NPC火焰之影的说明(如果你有兴趣的话)<BR>
     * <BR> <font fg=ffff00>【分歧4】最终头目-巴风特(秘谭)、战斗结束NPC火焰之影出现</font><BR>
     * 达成条件：再进行关卡1~关卡3的时候，要踏上红光球位置时，先踏上面三个<BR>
     * 直到赛尼斯或哈汀说出“笨”、“慢”、“耐心”、“快一点”...等字眼，这时再踏上最后一个<BR>
     * 之后赶进前往下一个关卡，再如法炮制通过关卡1~关卡3<BR> 最终关卡达成【分歧1】的条件<BR>
     * 条件达成：再击败巴风特(秘谭)、赛尼斯(秘谭)之后，NPC火焰之影会出现<BR> 由于通关后，如果太慢通过右下角已打开的黑墙，会拿不到奖励<BR>
     * 因此建议先通过后再隔墙看NPC火焰之影的说明(如果你有兴趣的话)<BR> <BR> <font
     * fg=ffff00>【分歧5】最终关卡怪物强度提升、战斗结束NPC火焰之影出现</font><BR>
     * 达成条件：除了达成【分歧3】或【分歧4】的条件<BR>
     * 在进行关卡4骷髅房时也等到赛尼斯或哈汀说出“笨”、“慢”、“耐心”、“快一点”...等字眼，这时在清掉怪物<BR>
     * 条件达成：除了【分歧3】或【分歧4】的情况出现，最终关卡的怪物强度会提升<BR> <BR> <font
     * fg=ffff00>【分歧6】NPC炎魔现身、最终头目-镰刀死神的使者(秘谭)、黑翼赛尼斯(秘谭)</font><BR>
     * 达成条件：队长要达成【分歧2】让赛尼斯(秘谭)不出现<BR>
     * 同时队友要达成【分歧0】和【分歧3】让NPC火焰之影现身、最终头目-镰刀死神的使者(秘谭)<BR>
     * 条件达成：在最终关卡时，NPC炎魔现身的时候，黑翼赛尼斯(秘谭)也会跟着现身<BR> <BR> <font
     * fg=ffff00>【分歧7】NPC炎魔现身、最终头目-巴风特(秘谭)、黑翼赛尼斯(秘谭)</font><BR>
     * 达成条件：队长要达成【分歧2】让赛尼斯(秘谭)不出现<BR>
     * 同时队友要达成【分歧1】和【分歧3】让NPC火焰之影现身、最终头目-巴风特(秘谭)<BR>
     * 条件达成：在最终关卡时，NPC炎魔现身的时候，黑翼赛尼斯(秘谭)也会跟着现身<BR> <BR> <font
     * fg=ffff00>【分歧8】大量葛林(秘谭)现身</font><BR> 达成条件：未知<BR>
     * 条件达成：副本进行途中出现大量的葛林(秘谭)<BR> 最后副本宣告失败，全员传送回说话之岛<BR> <BR> 注意事项：<BR>
     * 副本失败条件：<BR> <font fg=ffff00>1. 如果队长断线(队长死亡，副本照样进行)<BR> 2. 队友闯关超过时间限制<BR>
     * 3. 队友死亡或断线，造成在关卡内人数低于4人时(因为启动机关需要至少4人)<BR> 4.
     * 在最终关卡各阶段怪物2分钟内剩下4只以上，并且出现“邪恶气息又拥挤过来了！请准备！”<BR> 如果过了1分钟后还剩下4只以上<BR>
     * 会出现“就算是因为照这样做而任务失败的话，别气馁！让我给你打打气吧！”<BR>
     * 之后如果还是来不及清怪，并出现“高估、逃跑、逃走”之类的字眼，则表示失败</font><BR> <BR> 任务目标：<BR>
     * 进行最终关卡，连续12回合成功击败怪物<BR> <BR> 相关怪物：<BR> <font fg=ffff00>Lv.4　
     * 葛林(秘谭1)</font><BR> <font fg=ffff00>Lv.45　 镰刀死神的使者(秘谭1)</font><BR> <font
     * fg=ffff00>Lv.52　 巴风特(秘谭1)</font><BR> <font fg=ffff00>Lv.64　
     * 黑翼赛尼斯(秘谭1)</font><BR> <font fg=ffff00>Lv.64　 赛尼斯(秘谭1)</font><BR> <BR>
     * <img src="#1210"></img> <font fg=66ff66>步骤.6　尽速离开现场</font><BR>
     * 闯关完成后，最终关卡右下角的黑色墙壁会开启<BR> 沿着这边往右上走，来到一个地上有红色光圈的房间<BR>
     * 当每个玩家都站上红色光圈后，中央会出现黑色光圈<BR> 这时踏上黑色光圈就会出现哈汀的秘密袋子，之后就把袋子捡起来<BR>
     * 再来就直离开副本，回到说话之岛吧！<BR> <BR> 注意事项：<BR> 因此墙壁一开启就要马上离开前去领奖房间，否则墙壁很快就会再关闭<BR>
     * 被关在最终关卡的房间，将会无法获得奖励<BR> <BR> 任务目标：<BR> 前往领奖房间领取奖励，离开副本<BR> <BR> 相关物品：<BR>
     * <font fg=ffff00> 哈汀的秘密袋子 x 1</font><BR> <BR> <br> <img src="#331"
     * action="index"> </body>
     */
    private Chapter01() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new Chapter01();
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
            final String questName = QUEST.get_questname();
            pc.sendPackets(new S_ServerMessage("\\fT" + questName + "任务结束！"));

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
