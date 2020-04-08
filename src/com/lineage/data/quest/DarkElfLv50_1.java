package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;
import com.lineage.server.timecontroller.quest.DE50A_Timer;

/**
 * 说明:寻找黑暗之星 (黑暗妖精50级以上官方任务)
 * 
 * @author daien
 * 
 */
public class DarkElfLv50_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(DarkElfLv50_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 安迪亚
     */
    public static final int _endiaId = 71094;

    /**
     * 莱拉
     */
    public static final int _tgid = 70811;

    /**
     * 任务地图(黑暗妖精试炼地监)
     */
    public static final int MAPID = 306;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_d50_1";

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1　黑暗的骚动 </font><BR> <BR>
     * 布鲁迪卡说明在很久以前黑暗妖精怨念的母体，为了避免黑暗之星变质，而造成黑暗妖精种族的毁灭，因此布鲁迪卡要求找回黑暗之星。<BR>
     * 前往沉默洞穴找<font fg=0000ff>长老－布鲁迪卡</font>(32802，32824)接下寻找<font
     * fg=0000ff>黑暗之星</font>的任务。<BR> 布鲁迪卡表示可以去寻求<font
     * fg=0000ff>奇马</font>(32907，32946)的帮助，找到奇马后发现如果要他帮忙就得先帮他去妖精森林调查。<BR> <BR>
     * 任务目标：<BR> 与布鲁迪卡接受任务，并寻找奇马探听消息<BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.2　亡者的讯息 </font><BR> <BR>
     * 接下奇马的调查任务后，就前往眠龙洞穴入口附近(32944，32280)找<font
     * fg=0000ff>安迪亚</font>，他会要求你将他的灵魂带到<font
     * fg=0000ff>莱拉</font>的身旁，到达之后他就会消失并给予<font
     * fg=0000ff>安迪亚之袋</font>，点二下后就可以得到安迪亚之信。<BR> 将安迪亚之信交给奇马后他会给予<font
     * fg=0000ff>真实的面具</font>，并且说明要找黑暗之星必需要前往灵魂枯竭的土地上，之后便前往<font
     * fg=0000ff>食尸地</font>找<font fg=0000ff>堕落的灵魂</font>(32858，32929)传送到<font
     * fg=0000ff>邪念地监</font>。 <BR> <BR> 注意事项：<BR> 安迪亚会四处移动，因此找不到的玩家请在附近找找。 <BR>
     * 与安迪亚对话后需将她带到莱拉的旁边，如果玩家死亡、重登或走太快她会被别的黑暗妖精带走（与骑士45级试练的调查员相同）。 <BR>
     * 堕落的灵魂需戴上真实的面具才能看的到。并与堕落的灵魂对话后才能传送到邪念地监。<BR> <BR> 任务目标：<BR>
     * 寻找安迪亚并接受引导任务，之后取得遗物拿回去给奇马，得到真实的面具，再寻找堕落的灵魂传送到邪念地监<BR> <BR> 相关物品：<BR>
     * <font fg=ffff00>安迪亚之袋 x 1</font><BR> <font fg=ffff00>安迪亚之信 x 1</font><BR>
     * <font fg=ffff00>真实的面具 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.3　邪念的地监 </font><BR> <BR> 进入后寻找<font
     * fg=0000ff>堕落的司祭</font>(32592，32819)，打倒它后可以获得堕落钥匙。 <BR> 之后再寻找<font
     * fg=0000ff>混沌的司祭</font>(32621，32906)，打倒它后可以获得混沌钥匙。 <BR>
     * 走到第一排中间的宝箱后面(32619，32909)使用堕落钥匙将宝箱打开后即可解除暗道的障碍，马上使用混沌钥匙传送到(32591，32813)，
     * 此时障碍已经解除，通过一片黑色的地方。 <BR>
     * 之后走到魔法阵里(32566，32880)，就会传送到死亡的司祭的房间，打倒它就可以获得黑暗之星。 <BR> <BR> 注意事项：<BR>
     * 邪念地监无法记忆座标，无法使用指定传送、瞬间移动卷轴、全体传送术的卷轴。<BR> 邪念地监会持续扣掉角色的体力。 <BR>
     * 死亡时会回到村庄、重登时会在远古战场的邪恶神殿附近出现。 <BR> 在开暗门时由于宝箱及墙上的画在一定的时间内就会恢复原状，因此开完后需立即通过。
     * <BR> 在尚未取得黑暗之星前，不要先购买蘑菇毒液以免影响到流程！ <BR> <BR> 任务目标：<BR>
     * 依序打倒各种司祭，终将取得黑暗之星<BR> <BR> 相关怪物：<BR> <font fg=ffff00>Lv.50　
     * 混沌的司祭</font><BR> <font fg=ffff00>Lv.57　 堕落的司祭</font><BR> <font
     * fg=ffff00>Lv.57　 死亡的司祭</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>黑暗之星 x
     * 1</font><BR> <font fg=ffff00>堕落钥匙 x 1</font><BR> <font fg=ffff00>混沌钥匙 x
     * 1</font><BR> <BR> <img src="#1210"></img> <font fg=66ff66>步骤.4　附毒的武器
     * </font><BR> <BR> 再来到大陆地监7楼找<font
     * fg=0000ff>欧林</font>(32813，32803)，向他购买蘑菇毒液。 <BR> 回到沉默洞穴找长老－<font
     * fg=0000ff>布鲁迪卡</font>(32802，32824)，将真实的面具、蘑菇毒液、黑暗之星交给布鲁迪卡，就可以获得死亡之指。 <BR>
     * <BR> 任务目标：<BR> 购得蘑菇毒液，将任务所得物品全部交给布鲁迪卡，并取得死亡之指<BR> <BR> 相关物品：<BR> <font
     * fg=ffff00>蘑菇毒液 x 1</font><BR> <font fg=ffff00>死亡之指 x 1</font><BR> <BR>
     */
    private DarkElfLv50_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new DarkElfLv50_1();
    }

    @Override
    public void execute(L1Quest quest) {
        try {
            // 设置任务
            QUEST = quest;

            final DE50A_Timer de50ATimer = new DE50A_Timer();
            de50ATimer.start();

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
