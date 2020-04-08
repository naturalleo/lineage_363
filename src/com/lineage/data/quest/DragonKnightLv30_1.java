package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:妖魔密使首领的情报 (龙骑士30级以上官方任务)
 * 
 * @author daien
 * 
 */
public class DragonKnightLv30_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(DragonKnightLv30_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_dk30_1";

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1　长老赋予的任务 </font><BR> <BR>
     * 至贝希摩斯找<font fg=0000ff>长老普洛凯尔</font>(32817 32831)，他会跟你说听闻海音地监中的<font
     * fg=0000ff>妖魔密使</font>，好像正在寻找些什么似的，希望这次你去那边查一下比较好。 之后长老普洛凯尔会给你普洛凯尔的<font
     * fg=0000ff>第二次指令书</font>及<font fg=0000ff>普洛凯尔的矿物袋</font>，并要你去做调查的事前准备。<BR>
     * <BR> 任务目标：<BR> 跟长老普洛凯尔接洽任务，并探听下一个指示<BR> <BR> 相关物品：<BR> <font
     * fg=ffff00>普洛凯尔的矿物袋 x 1</font><BR> <font fg=ffff00>普洛凯尔的第二次指令书 x
     * 1</font><BR> <BR> <img src="#1210"></img> <font fg=66ff66>步骤.2　事前准备
     * </font><BR> <BR> 与位于长老普洛凯尔不远处的爱尔菈丝(32816 32843)对话可取得<font
     * fg=0000ff>妖魔密使变形卷轴</font>。<BR> 将普洛凯尔的矿物袋交给铁匠皮尔(32790 32838)可制作<font
     * fg=0000ff>妖魔密使的徽印</font>。<BR> <BR> 任务目标：<BR> 凑齐妖魔密使变形卷轴和妖魔密使的徽印<BR> <BR>
     * 相关物品：<BR> <font fg=ffff00>妖魔密使变形卷轴 x 1</font><BR> <font fg=ffff00>妖魔密使的徽印
     * x 1</font><BR> <BR> <img src="#1210"></img> <font fg=66ff66>步骤.3　妖魔密使
     * </font><BR> <BR> 在海音地监 3楼找到妖魔密使，并交付同伴的证明妖魔密使的徽印。<BR>
     * 之后妖魔密使会给你妖魔密使之笛子。<BR> 在海音地监 3楼使用妖魔密使之笛子后可召唤出妖魔密使首领，将其打倒后即可取得<font
     * fg=0000ff>妖魔密使首领间谍书</font>。<BR> <BR> 注意事项：<BR>
     * 与妖魔密使对话前，必须先使用妖魔密使变形卷轴将外表伪装成妖魔密使<BR> <BR> 任务目标：<BR> 到海音地监
     * 3楼取得妖魔密使首领间谍书<BR> <BR> 相关怪物：<BR> <font fg=ffff00>Lv.22　
     * 妖魔密使(海音地监)</font><BR> <font fg=ffff00>Lv.28　 妖魔密使首领</font><BR> <BR>
     * 相关物品：<BR> <font fg=ffff00>妖魔密使之笛子 x 1</font><BR> <font
     * fg=ffff00>妖魔密使首领间谍书 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.4　回报 </font><BR> <BR>
     * 将获得的妖魔密使首领间谍书交给长老普洛凯尔，即可获得普洛凯尔的第一次信件及龙骑士书板(血之渴望)。<BR>
     * 将普洛凯尔的第一次信件交给塔尔立昂(32828 32844)，即可获得龙鳞臂甲。<BR> <BR> 任务目标：<BR>
     * 将妖魔密使首领间谍书转交给长老普洛凯尔，并获得奖励<BR> <BR> 相关物品：<BR> <font fg=ffff00>龙骑士书板(血之渴望)
     * x 1</font><BR> <font fg=ffff00>龙鳞臂甲 x 1</font><BR> <font
     * fg=ffff00>普洛凯尔的第一次信件 x 1</font><BR> <BR>
     */
    private DragonKnightLv30_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new DragonKnightLv30_1();
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
