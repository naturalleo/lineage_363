package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 骑士的证明 (骑士45级以上官方任务)
 * 
 * @author daien
 * 
 */
public class KnightLv45_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(KnightLv45_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_k45_1";

    /**
     * 调查员-巨人
     */
    public static final int _searcherid = 71092;

    /**
     * 调查员
     */
    public static final int _searcher2id = 71093;

    /**
     * 公爵的士兵
     */
    public static final int _guardid = 70740;

    /*
     * <img src="#1210"></img> <font fg=ffff66>步骤.1 夜之视野：</font><BR> <BR> <font
     * fg=0000ff>马沙</font>派出的<font
     * fg=0000ff>调查员</font>下落不明，据说最后一次调查员传来消息的地方为<font
     * fg=0000ff>黄昏山脉</font>，那是许多可怕强大的巨人聚集地， 为了要救回调查员，马沙将委托勇敢的骑士前往。<BR>
     * 前往威顿村找马沙(33713，32504)接下寻找调查员的任务。之后到黄昏山脉找<font
     * fg=0000ff>志武</font>(34259，33341)说话，得知要看到巨人守护神必需要有黎明森林的<font
     * fg=0000ff>强盗头目</font>(33755，32742周围)拥有的<font
     * fg=0000ff>夜之视野</font>。打倒强盗头目后会获得夜之视野。<BR> <BR> 任务目标：<BR>
     * 与马沙接受任务，与志武对话，猎杀强盗头目取得夜之视野<BR> <BR> 相关怪物：<BR> <font fg=ffff00>Lv.20
     * 强盗头目</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>夜之视野 x 1</font><BR> <BR>
     * <img src="#1210"></img> <font fg=ffff66>步骤.2 巨人守护神：</font><BR> <BR>
     * 到达黄昏山脉找<font fg=0000ff>巨人长老</font>(34242，33404)说话之后，他会要求你将<font
     * fg=0000ff>
     * 巨人守护神</font>身上的物品交给他。装备夜之视野后到黄昏山脉找巨人守护神(34248，33363)，打倒他后获得<font
     * fg=0000ff>守护神之袋</font>，点二下后会得到破损的调查簿、天空之剑及古代的遗物。将古代的遗物交给巨人长老后，
     * 得到古代钥匙并得知调查员被巨人守护神变成巨人的模样。<BR> <BR> 注意事项：<BR> 巨人守护神必须装备夜之视野才看的见<BR>
     * 天空之剑关系到50级试炼，所以千万不可删除<BR> <BR> 任务目标：<BR>
     * 与巨人长老接受任务，击败巨人守护神取得守护神之袋，回去交差并探听消息<BR> <BR> 相关怪物：<BR> <font
     * fg=ffff00>Lv.40 巨人守护神</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>破损的调查簿 x
     * 1</font><BR> <font fg=ffff00>古代的遗物 x 1</font><BR> <font
     * fg=ffff00>古代钥匙(上半部) x 1</font><BR> <font fg=ffff00>天空之剑 x 1</font><BR>
     * <font fg=ffff00>守护神之袋 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=ffff66>步骤.3 带回调查员：</font><BR> <BR>
     * 找到调查员(34242，33356)后请一位有相消术的法师或妖精帮他相消变回人类的样貌后
     * ，他会要求你带他到欧瑞村的商店西方公爵的士兵(34043，32234)旁。路上他会一直跟着你走，到达后他会给你调查簿的缺页。 <BR>
     * 回到威顿村后将寻找调查员的结果(破损的调查簿、调查簿的缺页及夜之视野)交给马沙后，得到勇敢皮带。 <BR> <BR> 任务目标：<BR>
     * 找到调查员，使用魔法相消术将其会复原状，并带回公爵的士兵那，之后回去跟马沙交差，取得奖励。<BR> <BR> 相关物品：<BR> <font
     * fg=ffff00>调查簿的缺页 x 1</font><BR> <font fg=ffff00>勇敢皮带 x 1</font><BR> <BR>
     */
    private KnightLv45_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new KnightLv45_1();
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
