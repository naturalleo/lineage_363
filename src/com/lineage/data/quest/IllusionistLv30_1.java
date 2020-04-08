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
public class IllusionistLv30_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(IllusionistLv30_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_i30_1";

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1　长老赋予的任务 </font><BR> <BR>
     * 前往希培莉亚找<font fg=0000ff>长老希莲恩</font>(32772 32811)，她会给予<font
     * fg=0000ff>希莲恩的第二次信件</font>和<font fg=0000ff>希莲恩之袋</font>，可从袋中获得<font
     * fg=0000ff>欧瑞村庄瞬间移动卷轴</font>、<font fg=0000ff>生锈的笛子</font>。<BR>
     * 并且长老希莲恩要你去调查艾尔摩大将军僵尸无法安息的原因。<BR> <BR> 任务目标：<BR> 跟长老希莲恩接洽任务，并探听任务地点<BR>
     * <BR> 相关物品：<BR> <font fg=ffff00>生锈的笛子 x 1</font><BR> <font
     * fg=ffff00>欧瑞村庄瞬间移动卷轴 x 1</font><BR> <font fg=ffff00>希莲恩的第二次信件 x
     * 1</font><BR> <font fg=ffff00>希莲恩之袋 x 1</font><BR> <BR> <img
     * src="#1210"></img> <font fg=66ff66>步骤.2　艾尔摩大将军僵尸 </font><BR> <BR>
     * 在欧瑞的艾尔摩战场上找到<font fg=0000ff>艾尔摩大将军僵尸</font>(34096 32396)，狩猎后可获得<font
     * fg=0000ff>艾尔摩将军之心</font>。<BR> 再到古鲁丁购买菊花花束，前往<font
     * fg=0000ff>古鲁丁祭坛</font>祭拜后，可获得<font fg=0000ff>索夏依卡灵魂之石</font>、<font
     * fg=0000ff>反王肯恩的权杖</font>。<BR> <BR> 任务目标：<BR> 取得艾尔摩将军之心，并前往古鲁丁进行祭拜<BR>
     * <BR> 相关怪物：<BR> <font fg=ffff00>Lv.25　 艾尔摩大将军僵尸</font><BR> <BR> 相关物品：<BR>
     * <font fg=ffff00>菊花花束 x 1</font><BR> <font fg=ffff00>反王肯恩的权杖 x
     * 1</font><BR> <font fg=ffff00>艾尔摩将军之心 x 1</font><BR> <font
     * fg=ffff00>索夏依卡灵魂之石 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.3　升华的仪式 </font><BR> <BR> 使用<font
     * fg=0000ff>索夏依卡灵魂之石</font>修复<font fg=0000ff>生锈的笛子</font>后，可获得<font
     * fg=0000ff>索夏依卡灵魂之笛</font>。<BR> 使用笛子召唤出<font
     * fg=0000ff>艾尔摩索夏依卡将军的冤魂</font>，打倒冤魂后可获得<font fg=0000ff>封印的索夏依卡遗物</font>。
     * <BR> 再购买<font fg=0000ff>卡拉花束</font>后，使用封印的索夏依卡遗物至<font
     * fg=0000ff>古鲁丁祭坛</font>祭拜可获得<font fg=0000ff>艾尔摩部队日记</font>。 <BR> <BR>
     * 注意事项：<BR> 当召唤出艾尔摩索夏依卡将军的冤魂，此时会被强迫变身为反王肯恩，而必须使用反王肯恩的权杖方可攻击造成伤害。<BR>
     * 使用索夏依卡灵魂之笛时，会短暂时间内无法使用物品栏道具。<BR> 变身为反王肯恩时，无法使用变身卷轴消除。 <BR> <BR> 任务目标：<BR>
     * 进行第二次祭拜仪式，并取得艾尔摩部队日记<BR> <BR> 相关怪物：<BR> <font fg=ffff00>Lv.25　
     * 艾尔摩索夏依卡将军的冤魂</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>卡拉花束 x
     * 1</font><BR> <font fg=ffff00>索夏依卡灵魂之笛 x 1</font><BR> <font
     * fg=ffff00>封印的索夏依卡遗物 x 1</font><BR> <font fg=ffff00>艾尔摩部队日记 x 1</font><BR>
     * <BR> <img src="#1210"></img> <font fg=66ff66>步骤.4　回报 </font><BR> <BR>
     * 将艾尔摩部队日记交给长老希莲恩，即可获得<font fg=0000ff>幻术士法书</font>和<font
     * fg=0000ff>记忆水晶(立方：冲击)</font>。<BR> <BR> 任务目标：<BR>
     * 将艾尔摩部队日记交给长老希莲恩，并获得奖励<BR> <BR> 相关物品：<BR> <font fg=ffff00>记忆水晶(立方：冲击) x
     * 1</font><BR> <font fg=ffff00>幻术士法书 x 1</font><BR> <BR>
     */
    private IllusionistLv30_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new IllusionistLv30_1();
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
