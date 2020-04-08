package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:妖精的任务 (妖精45级以上官方任务)
 * 
 * @author daien
 * 
 */
public class ElfLv45_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(ElfLv45_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务地图(赛菲亚地监)
     */
    public static final int MAPID = 302;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_e45_1";

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1　水之竖琴 </font><BR> <BR>
     * 马沙为了阻挡邪恶势力所派出调查员调查，根据调查员传来的资料显示，邪恶势力与水之竖琴有关，
     * 而水之竖琴的秘密只有吉普赛人知道，所以给予妖精前往了解的任务。<BR> 前往威顿村找<font
     * fg=0000ff>马沙</font>(33713，32504)接下调查水之竖琴任务。到吉普赛村找<font
     * fg=0000ff>希托</font>(33975，32931)对话，他要你帮忙寻回被<font
     * fg=0000ff>赛菲亚</font>抢走的<font fg=0000ff>蓝色长笛</font>。 <BR> <BR> 任务目标：<BR>
     * 与马沙接任务，去吉普赛村找希托接受任务<BR> <BR> <img src="#1210"></img> <font
     * fg=66ff66>步骤.2　赛菲亚之罪 </font><BR> <BR> 前往龙之谷洞穴三楼找到<font
     * fg=0000ff>赛菲亚</font
     * >(32712，32842)和他对话后得知他的过去，若要帮助他就必需承受他过去的罪行。答应后他会传送你到<font
     * fg=0000ff>赛菲亚之罪<
     * /font>的房间(32737，32859)，将赛菲亚之罪打倒后得到蓝色长笛。将蓝色长笛交给希托后得到神秘贝壳，并告诉你有关<font
     * fg=0000ff>水之竖琴</font>的事。 <BR> <BR> 注意事项：<BR> 红人无法跟希托对话进行试炼<BR>
     * 赛菲亚之罪会扣除正义值10,000<BR> 所以最好正义值超过10,000在去考，免的结束之后，相性却变成邪恶的<BR>
     * 在赛菲亚之罪房间重登会回奇岩村
     * ，可用传送回家的卷轴、祝福的瞬间移动卷轴、传送控制戒指，但无法使用指定传送、瞬间移动卷轴及魔法“世界树的呼唤”<BR> <BR>
     * 任务目标：<BR> 寻找赛菲亚，传送到洞穴打倒赛菲亚之罪取得蓝色长笛，回去跟希托交差，并打听水之竖琴的情报<BR> <BR> 相关怪物：<BR>
     * <font fg=ffff00>Lv.10　 赛菲亚之罪</font><BR> <BR> 相关物品：<BR> <font
     * fg=ffff00>蓝色长笛 x 1</font><BR> <font fg=ffff00>神秘贝壳 x 1</font><BR> <BR>
     * <img src="#1210"></img> <font fg=66ff66>步骤.3　古代亡灵 </font><BR> <BR>
     * 到达<font fg=0000ff>冰镜湖左边的魔法阵</font>(33973，32326)后使用<font
     * fg=0000ff>神秘贝壳</font>，会出现<font
     * fg=0000ff>古代亡灵</font>，打倒他后得到古代亡灵之袋，点二下后得到<font
     * fg=0000ff>古代钥匙</font>及<font
     * fg=0000ff>水之竖琴</font>。回到威顿村将调查的结果(古代钥匙及水之竖琴)交给马沙后得到马沙之袋
     * ，点二下后得到保护者手套、精灵水晶(召唤强力属性精灵)及水之竖琴。 <BR> 注意事项：<BR>
     * 使用神秘贝壳时需站在魔法阵的中央才能召唤出古代亡灵。<BR> 将水之竖琴鉴定后会变成水精灵之弓。<BR>
     * 水精灵之弓(水之竖琴)为下一阶段任务的必备物品，删除将无法接到50级试炼<BR> <BR> 任务目标：<BR>
     * 打倒古代亡灵取得古代钥匙及水之竖琴，回去和马沙交差取得奖励 <BR> <BR> 相关怪物：<BR> <font fg=ffff00>Lv.45　
     * 古代亡灵</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>古代钥匙(下半部) x 1</font><BR>
     * <font fg=ffff00>古代亡灵之袋 x 1</font><BR> <font fg=ffff00>马沙之袋 x 1</font><BR>
     * <font fg=ffff00>水精灵之弓 x 1</font><BR> <font fg=ffff00>保护者手套 x 1</font><BR>
     * <font fg=ffff00>精灵水晶(召唤强力属性精灵) x 1</font><BR> <font fg=ffff00>水之竖琴 x
     * 1</font><BR> <BR>
     */
    private ElfLv45_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new ElfLv45_1();
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
