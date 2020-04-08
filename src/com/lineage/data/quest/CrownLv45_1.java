package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 王族的信念 (王族45级以上官方任务)
 * 
 * @author daien
 * 
 */
public class CrownLv45_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(CrownLv45_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_c45_1";

    /*
     * <img src="#1210"></img> <font fg=ffff66>步骤.1 遗失的信念 ：</font><BR> <BR>
     * 很久之前的亚丁王国有个只传给 王族的王族徽章，但后来却被反 王肯恩给破坏遗失， 马沙深信王 族徽章是反抗反王肯恩的一种信
     * 念，想要对抗强大势力的反王肯 恩就必须寻找出王族徽章。<BR> <BR> 前往威顿村寻找<font
     * fg=0000ff>马沙</font>(33713 32504) ，他会要求你寻找王族徽章，并 请你找肯特村的<font
     * fg=0000ff>李察</font>(33078 32765) ，只有他知道王族徽章的下落。<BR> <BR> 任务目标：<BR>
     * 与马沙接受任务<BR> <BR> <img src="#1210"></img> <font fg=ffff66>步骤.2 徽章的下落
     * ：</font><BR> <BR> 到达肯特村找<font fg=0000ff>李察</font>(33078 32765)
     * 后，得知其中一块王族徽章的碎 片在眠龙洞穴三楼里的背叛的妖 魔队长手中。到眠龙洞穴三楼找 到背叛的妖魔队长后，打死他可
     * 获得王族徽章的碎片。<BR> 回到肯特后<font fg=0000ff>李察</font>(33078 32765) 会告诉你，风木村的麦知道另一
     * 块王族徽章的碎片的下落。<BR> <BR> 任务目标：<BR> 与李察探听消息，击败背叛的妖 魔队长取得王族徽章的碎片，回
     * 去与李察探听新消息<BR> <BR> 相关怪物：<BR> <font fg=ffff00>Lv.14 背叛的妖魔巡守</font><BR>
     * <font fg=ffff00>Lv.18 背叛的妖魔队长</font><BR> <BR> 相关物品：<BR> <font
     * fg=ffff00>王族徽章的碎片 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=ffff66>步骤.3　另一片徽章 ：</font><BR> <BR> 在风木村遇见<font
     * fg=0000ff>麦</font>，他会告诉你先 帮忙解决象牙塔发生的灵魂狩猎 问题才肯给你另一块王族徽章的 碎片。<BR> <BR>
     * 到象牙塔3楼找到白魔法师皮尔塔后，他会要你去解放被灵魂的 猎食者夺取的灵魂。到象牙塔8 楼找到灵魂的猎食者后，打死他 可获得失去光明的灵魂，拿去给
     * 白魔法师皮尔塔可以获得神秘的 袋子。<BR> <BR> 将神秘的袋子点二下后会出现三 个灵魂水晶，将 <font
     * fg=0000ff>灵魂水晶（红色）交给骑士</font>、 <font fg=0000ff>灵魂水晶（白色）交给妖精</font>、 <font
     * fg=0000ff>灵魂水晶（黑色）交给法师使用</font> ，使用会死亡并下降经验值，使 用后可分别得到 <font
     * fg=0000ff>灵魂之证（红色）</font>、 <font fg=0000ff>灵魂之证（白色）</font>、 <font
     * fg=0000ff>灵魂之证（黑色）</font>， 再将三个灵魂之证由王族交给风 木村的<font
     * fg=0000ff>麦</font>，他会给你另一块王族 徽章的碎片。<BR> <BR> 注意事项：<BR> 注意：<BR> <font
     * fg=ffff00>灵魂水晶(红色)：骑士</font><BR> <font fg=ffff00>灵魂水晶(白色)：妖精</font><BR>
     * <font fg=ffff00>灵魂水晶(黑色)：法师</font><BR> <BR> 任务目标：<BR> 与麦探听消息，先去解决象牙塔
     * 的灵魂狩猎问题，照上述方法取 得灵魂之证，拿回去给麦换取徽 章<BR> <BR> 相关怪物：<BR> <font fg=ffff00>Lv.10
     * 灵魂的猎食者</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>灵魂水晶(白) x 1</font><BR>
     * <font fg=ffff00>灵魂之证(白) x 1</font><BR> <font fg=ffff00>神秘的袋子 x
     * 1</font><BR> <font fg=ffff00>王族徽章的碎片 x 1</font><BR> <font
     * fg=ffff00>灵魂水晶(红) x 1</font><BR> <font fg=ffff00>灵魂水晶(黑) x 1</font><BR>
     * <font fg=ffff00>灵魂水晶(黑) x 1</font><BR> <font fg=ffff00>灵魂之证(红) x
     * 1</font><BR> <font fg=ffff00>灵魂之证(黑) x 1</font><BR> <font
     * fg=ffff00>失去光明的灵魂 x 1</font><BR> <BR> <img src="#1210"></img> <font
     * fg=ffff66>步骤.4　回报 ：</font><BR> <BR> 将二块王族徽章的碎片交给威顿 村的<font
     * fg=0000ff>马沙</font>，将获得守护者的戒指 ，此时完成45级试炼，血盟系统 会自动转成联盟系统，联盟君主
     * 可以多加收联盟成员，并给予联 盟成员职位名称，且新增联盟频 道可使用。<BR> <BR> 任务目标：<BR>
     * 将两块王族徽章的碎片交给马沙，换取奖励。<BR> <BR> 相关物品：<BR> <font fg=ffff00>守护者的戒指 x
     * 1</font><BR> <BR>
     */

    private CrownLv45_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new CrownLv45_1();
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
