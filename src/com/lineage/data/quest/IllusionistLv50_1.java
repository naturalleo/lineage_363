package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:时空彼端的线索 (幻术士50级以上官方任务)
 * 
 * @author daien
 * 
 */
public class IllusionistLv50_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(IllusionistLv50_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务地图(异界奎斯特)
     */
    public static final int MAPID = 2004;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_i50_1";

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1　长老赋予的任务 </font><BR> <BR>
     * 前往希培莉亚找<font fg=0000ff>长老希莲恩</font>(32772
     * 32811)对话，他会要玩家前往调查时空裂痕频繁出现的原因。<BR> 之后会给你<font
     * fg=0000ff>希莲恩的第五次信件</font>，并且要玩家搜集100个<font
     * fg=0000ff>时空裂痕碎片</font>过来给他。<BR> <BR> 注意事项：<BR>
     * 时空裂痕(底比斯)和时空裂痕(提卡尔)，在这两处狩猎怪物皆可搜集到时空裂痕碎片<BR> <BR> 任务目标：<BR>
     * 跟长老希莲恩接洽任务，并前往时空裂痕<BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.2　异界奎斯特 </font><BR> <BR> 凑齐100个时空裂痕碎片并交给长老希莲恩，可以获得<font
     * fg=0000ff>时空裂痕邪念碎片</font>、<font fg=0000ff>希莲恩的护身符</font>、<font
     * fg=0000ff>希莲恩的第六次信件</font>。<BR> 再来只要使用时空裂痕邪念碎片即可前往异界奎斯特。<BR> <BR>
     * 注意事项：<BR> 当想要离开异界奎斯特，只需要使用希莲恩的护身符即可，并且时空裂痕邪念碎片、希莲恩的护身符可以重复和长老希莲恩索取<BR>
     * <BR> 任务目标：<BR> 将100个时空裂痕碎片交给长老希莲恩，并且前往异界奎斯特<BR> <BR> 相关物品：<BR> <font
     * fg=ffff00>时空裂痕邪念碎片 x 1</font><BR> <font fg=ffff00>时空裂痕碎片 x 100</font><BR>
     * <font fg=ffff00>希莲恩的护身符 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.3　蓝色灵魂之火</font><BR> <BR> 在异界奎斯特调查寻找<font
     * fg=0000ff>蓝色灵魂之火</font>，得知要求给予<font fg=0000ff>食腐兽之血</font>5个、<font
     * fg=0000ff>翼龙之血</font>5个。<BR> 之后狩猎食腐兽和翼龙凑齐需求品交给<font
     * fg=0000ff>蓝色灵魂之火</font>，可以获得<font fg=0000ff>灵魂之火灰烬</font>、<font
     * fg=0000ff>蓝色之火碎片</font>。<BR> <BR> 任务目标：<BR>
     * 满足蓝色灵魂之火的需求，取得灵魂之火灰烬、蓝色之火碎片<BR> <BR> 相关物品：<BR> <font fg=ffff00>灵魂之火灰烬 x
     * 1</font><BR> <font fg=ffff00>食腐兽之血 x 5</font><BR> <font fg=ffff00>翼龙之血 x
     * 5</font><BR> <font fg=ffff00>蓝色之火碎片 x 1</font><BR> <BR> <img
     * src="#1210"></img> <font fg=66ff66>步骤.4　吉尔塔斯的部下 </font><BR> <BR>
     * 在异界奎斯特使用<font fg=0000ff>蓝色之火碎片</font>，吉尔塔斯的部下<font
     * fg=0000ff>塞维斯</font>会现身。<BR> 打倒塞维斯，可以获得<font
     * fg=0000ff>塞维斯邪念碎片</font>。<BR> <BR> 任务目标：<BR> 击败塞维斯，并取得塞维斯邪念碎片<BR> <BR>
     * 相关怪物：<BR> <font fg=ffff00>Lv.46　 塞维斯</font><BR> <BR> 相关物品：<BR> <font
     * fg=ffff00>塞维斯邪念碎片 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.5　回报 </font><BR> <BR> 将塞维斯邪念碎片带回去给长老希莲恩，即可获得<font
     * fg=0000ff>希莲恩的指令书</font>、<font fg=0000ff>特别的原石</font>。<BR>
     * 将希莲恩的指令书、特别的原石交给<font fg=0000ff>铁匠 巴特尔</font>(32803 32812)，即可获得<font
     * fg=0000ff>蓝宝石奇古兽</font>。<BR> <BR> 任务目标：<BR> 将塞维斯邪念碎片交给长老希莲恩，并获得奖励<BR>
     * <BR> 相关物品：<BR> <font fg=ffff00>蓝宝石奇古兽 x 1</font><BR> <font
     * fg=ffff00>希莲恩的指令书 x 1</font><BR> <font fg=ffff00>特别的原石 x 1</font><BR>
     * <BR>
     */
    private IllusionistLv50_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new IllusionistLv50_1();
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
