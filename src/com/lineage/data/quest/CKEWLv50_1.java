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
 * 说明:不死魔族再生的秘密 (王族,骑士,妖精,法师50级以上官方任务-50级后半段)
 * 
 * @author daien
 * 
 */
public class CKEWLv50_1 extends QuestExecutor {

    private static final Log _log = LogFactory.getLog(CKEWLv50_1.class);

    /**
     * 任务资料
     */
    public static L1Quest QUEST;

    /**
     * 任务地图(再生圣殿 1楼/2楼/3楼)
     */
    public static final int MAPID = 2000;

    /**
     * 成员判断用数字
     */
    public static final int USER = 15;

    /**
     * 任务资料说明HTML
     */
    private static final String _html = "y_q_ckew50_1";

    /*
     * <img src="#1210"></img> <font fg=66ff66>步骤.1　隐藏的再生圣殿 </font><BR> <BR>
     * 再经过一连串的调查，确认到不死魔族似乎存在着再生圣殿来达到不死。<BR> 为了击退不死魔族，必定要先断绝他们不断再生的根源。<BR>
     * 根据调查的线索，可以在魔族神殿寻找到再生圣殿的入口。<BR> <BR> 注意事项：<BR> 四种职业的再生圣殿入口位置并不相同<BR>
     * 进入前<font fg=0000ff>必须以王族为队长预先组队</font><BR> 王族传送水晶：魔族神殿(32926, 32830)<BR>
     * 骑士传送水晶：魔族神殿(32849, 32830)<BR> 法师传送水晶：魔族神殿(32854, 32924)<BR>
     * 妖精传送水晶：魔族神殿(32884, 32951)<BR> <BR> 在魔族神殿里：<BR> 王族没有限定武器<BR> 骑士需使用天空之剑<BR>
     * 妖精需使用水精灵之弓<BR> 法师需使用古代人的智慧<BR> 至于水精灵之弓所使用的古代之箭，圣殿里的怪物会掉落<BR> <BR>
     * 任务目标：<BR> 接受<font fg=0000ff>迪嘉勒廷公爵</font>的指示，前往魔族神殿进入再生圣殿<BR> <BR>
     * 相关物品：<BR> <font fg=ffff00>古代之箭 x 1</font><BR> <font fg=ffff00>水精灵之弓 x
     * 1</font><BR> <font fg=ffff00>古代人的智慧 x 1</font><BR> <font fg=ffff00>天空之剑 x
     * 1</font><BR> <BR> <img src="#1210"></img> <font fg=66ff66>步骤.2　再生圣殿 1楼~2楼
     * </font><BR> <BR> 在再生圣殿1楼请击退该楼层的不死怪物直到取得通往2楼的钥匙，即可前往再生圣殿2楼。<BR>
     * 抵达再生圣殿2楼之后，直走到底会遇见高洁的意志斗士。<BR> 击退<font
     * fg=0000ff>高洁的意志斗士</font>可以获得前往3楼的钥匙。<BR> <BR> 注意事项：<BR>
     * 高洁的意志战士会随机掉落古代之箭，供妖精使用<BR> 2楼出没的奇诺之监视者，会掉落相当于终极治愈药水的监视者之眼<BR> <BR>
     * 任务目标：<BR> 取得通往下一楼层的钥匙，直到抵达再生圣殿3楼<BR> <BR> 相关怪物：<BR> <font
     * fg=ffff00>Lv.30　 高洁的意志战士</font><BR> <BR> 相关物品：<BR> <font fg=ffff00>古代之箭 x
     * 1</font><BR> <font fg=ffff00>圣殿2楼钥匙 x 1</font><BR> <font fg=ffff00>圣殿3楼钥匙
     * x 1</font><BR> <font fg=ffff00>监视者之眼 x 1</font><BR> <BR> <img
     * src="#1210"></img> <font fg=66ff66>步骤.3　再生圣殿 3楼 </font><BR> <BR>
     * 抵达再生圣殿3楼之后，击退高洁意志的守护者可以获得<font fg=0000ff>魔之角笛</font>。<BR>
     * 之后寻找NPC的被遗弃的肉身和他交谈会与给你<font fg=0000ff>消灭之意志</font>。<BR>
     * 使用消灭之意志可以抵达对面的祭坛房间。<BR> 在祭坛房间的中央使用魔之角笛可以呼唤出神官奇诺。<BR>
     * 将神官奇诺击退之后能够获得破坏之秘药。<BR> 饮用破坏之秘药将再生祭坛击毁，之后能获得祭坛的碎片。<BR> <BR> 注意事项：<BR>
     * 请务必饮用破坏之秘药才能有效攻击再生祭坛<BR> 四个职业必须都在副本任务中圣殿钥匙才具备传送能力。<BR>
     * 四个职业其中之一死亡离开副本，其余职业将被传送离开。<BR> <font fg=0000ff>再生祭坛除王族外限定各职业使用指定武器攻击。<BR>
     * 精灵：水精灵之弓<BR> 法师：古代人的智慧<BR> 骑士：天空之剑</font><BR> <BR> 任务目标：<BR>
     * 经过一连串的挑战，将再生祭坛毁损后，取得毁损的证据“祭坛的碎片”<BR> <BR> 相关物品：<BR> <font fg=ffff00>消灭之意志
     * x 1</font><BR> <font fg=ffff00>魔之角笛 x 1</font><BR> <font fg=ffff00>破坏之秘药
     * x 1</font><BR> <font fg=ffff00>祭坛的碎片 x 1</font><BR> <BR> <img
     * src="#1210"></img> <font fg=66ff66>步骤.4　回报任务达成 </font><BR> <BR>
     * 事成之后回到象牙塔3楼向迪嘉勒廷报告。<BR> 并且将祭坛的碎片交给迪嘉勒廷，即可领取奖励。<BR> <BR> 注意事项：<BR>
     * 交换的奖励依照职业不同而异：<BR> 王族：黄金权杖<BR> 骑士：黑焰之剑<BR> 妖精：赤焰之弓或赤焰之剑(二选一)<BR>
     * 法师：玛那水晶球<BR> <BR> 任务目标：<BR> 交付祭坛的碎片给迪嘉勒廷，换取奖励<BR> <BR> 相关物品：<BR> <font
     * fg=ffff00>黄金权杖 x 1</font><BR> <font fg=ffff00>赤焰之弓 x 1</font><BR> <font
     * fg=ffff00>赤焰之剑 x 1</font><BR> <font fg=ffff00>黑焰之剑 x 1</font><BR> <font
     * fg=ffff00>玛那水晶球 x 1</font><BR> <font fg=ffff00>祭坛的碎片 x 1</font><BR> <BR>
     */
    private CKEWLv50_1() {
        // TODO Auto-generated constructor stub
    }

    public static QuestExecutor get() {
        return new CKEWLv50_1();
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
