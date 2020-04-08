package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:艾尔摩战场的轨迹 (幻术士30级以上官方任务)
 * 
 * @author daien
 * 
 */
public class IllusionistLv45_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(IllusionistLv45_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_i45_1";

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1　长老赋予的任务 </font><BR> <BR>
     * 前往希培莉亚找<font fg=0000ff>长老希莲恩</font>(32772,
     * 32811)对话，他会要玩家前往调查久违的白蚁复出的原因。<BR> 之后长老希莲恩会给予<font
     * fg=0000ff>希莲恩的第三次信件</font>和<font fg=0000ff>希莲恩之袋</font>。<BR>
     * 打开长老所给的袋子获得<font fg=0000ff>风木村庄瞬间移动卷轴</font>、3个<font
     * fg=0000ff>时空裂痕水晶(绿色)</font>。<BR> <BR> 任务目标：<BR> 跟长老希莲恩接洽任务，并探听任务地点<BR>
     * <BR> 相关物品：<BR> <font fg=ffff00>时空裂痕水晶(绿色) x 3</font><BR> <font
     * fg=ffff00>风木村庄瞬间移动卷轴 x 1</font><BR> <font fg=ffff00>希莲恩之袋 x 1</font><BR>
     * <BR> <img src="#1210"></img> <font fg=66ff66>步骤.2　白蚁的尸体 </font><BR> <BR>
     * 前往沙漠的巨蚁洞寻找3具白蚁的尸体(32785, 32748)(32808, 32725)(32728, 32782)，并且对其使用<font
     * fg=0000ff>时空裂痕水晶(绿色)</font>。<BR> 之后可以获得<font
     * fg=0000ff>第一次记忆碎片</font>、<font fg=0000ff>第二次记忆碎片</font>、<font
     * fg=0000ff>第三次记忆碎片</font>。<BR> 将这3块记忆碎片带回去给长老希莲恩，之后会再给玩家们3个<font
     * fg=0000ff>时空裂痕水晶(蓝色)</font>。<BR> 并命令玩家前往火龙窟调查。<BR> <BR> 任务目标：<BR>
     * 在巨蚁洞凑齐3块记忆碎片，之后和长老希莲恩换取3个时空裂痕水晶(蓝色)<BR> <BR> 相关物品：<BR> <font
     * fg=ffff00>第一次记忆碎片 x 1</font><BR> <font fg=ffff00>第二次记忆碎片 x 1</font><BR>
     * <font fg=ffff00>第三次记忆碎片 x 1</font><BR> <font fg=ffff00>时空裂痕水晶(蓝色) x
     * 3</font><BR> <BR> <img src="#1210"></img> <font fg=66ff66>步骤.3　白蚁的踪迹
     * </font><BR> <BR> 前往火龙窟寻找<font fg=0000ff>白蚁的痕迹(土壤)</font>(33695,
     * 32422)、<font fg=0000ff>白蚁的痕迹(酸性液)</font>(33769, 32348)、<font
     * fg=0000ff>白蚁的痕迹(蛋壳)</font>(33796, 32431)。<BR> 之后对着这些地方使用<font
     * fg=0000ff>时空裂痕水晶(蓝色)</font>，可以获得<font fg=0000ff>第一次邪念碎片</font>、<font
     * fg=0000ff>第二次邪念碎片</font>、<font fg=0000ff>第三次邪念碎片</font>。<BR> <BR>
     * 任务目标：<BR> 在火龙窟凑齐3块邪念碎片<BR> <BR> 相关物品：<BR> <font fg=ffff00>第一次邪念碎片 x
     * 1</font><BR> <font fg=ffff00>第二次邪念碎片 x 1</font><BR> <font
     * fg=ffff00>第三次邪念碎片 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.4　时空裂痕 </font><BR> <BR> 先使用<font
     * fg=0000ff>第一次邪念碎片</font>、<font fg=0000ff>第二次邪念碎片</font>，组合出<font
     * fg=0000ff>未完成的时间水晶球</font>。<BR> 再使用<font fg=0000ff>第三次邪念碎片</font>、<font
     * fg=0000ff>未完成的时间水晶球</font>，组合出<font fg=0000ff>完整的时间水晶球</font>。<BR>
     * 于火龙窟使用完整的时间水晶球，可以打开一个<font fg=0000ff>时空裂痕</font>，将其打碎可获得<font
     * fg=0000ff>时空裂痕邪念碎片</font>。<BR> <BR> 注意事项：<BR>
     * 组合时间水晶球请一定要依照顺序，以免出错，造成无法继续进行任务<BR> <BR> 任务目标：<BR>
     * 呼唤出时空裂痕，并打碎取得时空裂痕邪念碎片<BR> <BR> 相关物品：<BR> <font fg=ffff00>未完成的时间水晶球 x
     * 1</font><BR> <font fg=ffff00>时间水晶球 x 1</font><BR> <font
     * fg=ffff00>时空裂痕邪念碎片 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.5　回报 </font><BR> <BR> 将时空裂痕邪念碎片带回去给长老希莲恩，即可获得<font
     * fg=0000ff>幻术士斗篷</font>。<BR> <BR> 任务目标：<BR> 将时空裂痕邪念碎片交给长老希莲恩，并获得奖励<BR>
     * <BR> 相关物品：<BR> <font fg=ffff00>幻术士斗篷 x 1</font><BR> <BR>
     */
    private IllusionistLv45_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new IllusionistLv45_1();
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
