package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:纠正错误的观念 (黑暗妖精45级以上官方任务)
 * 
 * @author daien
 * 
 */
public class DarkElfLv45_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(DarkElfLv45_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_d45_1";

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1　同族的内争 </font><BR> <BR>
     * 寻找位在沉默洞穴里刺客的精神支柱<font
     * fg=0000ff>布鲁迪卡</font>。与布鲁迪卡对话时，他表示因为与反王勾结的刺客，导致黑妖种族引起内争
     * ，为了将他们错误的想法纠正过来。而要解决此问题的话，必须取得传说中的武器才行。另外他会建议你到<font
     * fg=0000ff>侏儒铁匠－库普</font>那探听消息。<BR> 到沉默洞穴找库普，得知制作传说中的武器其中一个材料<font
     * fg=0000ff>"生锈的刺客之剑"</font>在<font
     * fg=0000ff>"刺客首领"</font>的手上，而了解此事的人为欧瑞村的<font
     * fg=0000ff>罗吉</font>，但是库普要你去找罗吉之前先去眠龙洞穴附近绕绕，可能会有出人意外的收获。<BR> <BR>
     * 注意事项：<BR> 布鲁迪卡：32802，32824<BR> 库普：32829，32971<BR> 罗吉：34036，32258<BR> <BR>
     * 任务目标：<BR> 与布鲁迪卡接受任务，去找库普继续接受任务<BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.2　刺客之证 </font><BR> <BR>
     * 接受库普的建议之后，在眠龙洞穴的附近(32928，32276)找到<font
     * fg=0000ff>"刺客首领护卫"</font>，与他对话后得知要见到"刺客首领"的方法必需要有刺客之证。 <BR>
     * 之后便前往寻找欧瑞村的罗吉，
     * 但是与罗吉对话之后，他却说明如果要他帮忙，就要先解决最近发生雪怪偷吃村里家畜的问题。接受他的要求之后，到欧瑞村跟象牙塔之间寻找<font
     * fg=0000ff>"凶猛的雪怪"</font>并解决掉它，即可得到<font
     * fg=0000ff>雪怪首级</font>。拿着雪怪首级回去找罗吉，并交给他，即可得到<font fg=0000ff>刺客之证</font>。
     * <BR> <BR> 注意事项：<BR> 与刺客首领护卫对话时必需变身成刺客，才能与他对话，否则他不会理你。<BR>
     * 得到刺客之证后必需再度与罗吉对话，否则打死刺客首领后将无法得到刺客首领的箱子。<BR> <BR> 任务目标：<BR>
     * 与刺客首领护卫探听消息之后，与罗吉接受任务，狩猎凶猛的雪怪取得它的首级，回去交差可得刺客之证<BR> <BR> 相关怪物：<BR> <font
     * fg=ffff00>Lv.30　 凶猛的雪怪</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>刺客之证 x
     * 1</font><BR> <BR> <img src="#1210"></img> <font fg=66ff66>步骤.3　刺客首领
     * </font><BR> <BR> 随后前往眠龙洞穴3楼寻找一个特殊的传送点，站上去后使用刺客之证便会进入一房间，而<font
     * fg=0000ff>刺客首领</font>就在里面。遇到刺客首领，将他解决掉之后，即可得到<font
     * fg=0000ff>刺客首领的箱子</font>。打开刺客首领的箱子， 即可得到<font fg=0000ff>死亡之证</font>和<font
     * fg=0000ff>生锈的刺客之剑</font>。<BR> 回去找布鲁迪卡，并将死亡之证及刺客之证交给他，即可得到<font
     * fg=0000ff>布鲁迪卡之袋</font>。打开布鲁迪卡之袋， 即可得到黑暗精灵水晶(暗影闪避)和影子长靴。 <BR> <BR>
     * 注意事项：<BR> 在攻击刺客首领时，若有其他的玩家一起攻击，将无法得到刺客首领的箱子。<BR> <BR> 任务目标：<BR>
     * 打败刺客首领取得任务道具，之后回去和布鲁迪卡交差取得奖励<BR> <BR> 相关物品：<BR> <font fg=ffff00>布鲁迪卡之袋 x
     * 1</font><BR> <font fg=ffff00>影子长靴 x 1</font><BR> <font
     * fg=ffff00>黑暗精灵水晶(暗影闪避) x 1</font><BR> <font fg=ffff00>刺客首领的箱子 x
     * 1</font><BR> <font fg=ffff00>生锈的刺客之剑 x 1</font><BR> <font fg=ffff00>死亡之证
     * x 1</font><BR> <BR>
     */
    private DarkElfLv45_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new DarkElfLv45_1();
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
