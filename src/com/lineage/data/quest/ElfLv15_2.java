package com.lineage.data.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Quest;

/**
 * 说明:欧斯的先见之明 (妖精15级以上官方任务)
 * 
 * @author daien
 * 
 */
public class ElfLv15_2 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(ElfLv15_2.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_e15_2";

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1 妖魔的魔法 </font><BR> <BR>
     * 与燃柳村的<font fg=0000ff>欧斯</font>对话，得知他为了防止妖魔入侵妖精森林，所以要先了解他们，
     * 听说在妖魔群里有几个妖魔会使用魔法，所以我们必需先研究他们的魔法，才能 了解他们的实力。<BR>
     * 到燃柳村找欧斯对话后，得知他在研究妖魔群里四种长久历史的魔法，这些魔法 书分别在都达玛拉妖魔、甘地妖魔、阿吐巴妖魔及那鲁加妖魔手上，之后出村寻
     * 找这四种妖魔，打倒他们后分别取得都<font fg=0000ff>达玛拉妖魔法书</font>、<font
     * fg=0000ff>甘地妖魔魔法书</font>、<font fg=0000ff>阿吐 巴妖魔魔法书</font>、<font
     * fg=0000ff>那鲁加妖魔魔法书</font>，再来就是将这四种魔法交给欧斯就可以获得 "精灵敏捷头盔或精灵体质头盔" 。<BR> <BR>
     * 注意事项：<BR> 奖励可以从以下两样选取一样：<BR> 精灵敏捷头盔<BR> 精灵体质头盔<BR> <BR> 任务目标：<BR>
     * 与欧斯接受任务，狩猎都达玛拉妖魔、甘地妖魔、阿吐巴妖魔、那鲁加妖魔取得他们的魔法书，再回去交差领取奖励<BR> <BR> 相关怪物：<BR>
     * <font fg=ffff00>Lv.10 甘地妖魔</font><BR> <font fg=ffff00>Lv.15
     * 阿吐巴妖魔</font><BR> <font fg=ffff00>Lv.15 都达玛拉妖魔</font><BR> <font
     * fg=ffff00>Lv.17 那鲁加妖魔</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>阿吐巴妖魔魔法书
     * x 1</font><BR> <font fg=ffff00>都达玛拉妖魔魔法书 x 1</font><BR> <font
     * fg=ffff00>甘地妖魔魔法书 x 1</font><BR> <font fg=ffff00>那鲁加妖魔魔法书 x 1</font><BR>
     * <font fg=ffff00>精灵敏捷头盔 x 1</font><BR> <font fg=ffff00>精灵体质头盔 x
     * 1</font><BR> <BR>
     */
    private ElfLv15_2() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new ElfLv15_2();
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
