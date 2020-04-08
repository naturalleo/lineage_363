package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.CKEWLv50_1;
import com.lineage.data.quest.CrownLv45_1;
import com.lineage.data.quest.CrownLv50_1;
import com.lineage.data.quest.ElfLv45_1;
import com.lineage.data.quest.ElfLv50_1;
import com.lineage.data.quest.KnightLv45_1;
import com.lineage.data.quest.KnightLv50_1;
import com.lineage.data.quest.WizardLv45_1;
import com.lineage.data.quest.WizardLv50_1;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 迪嘉勒廷<BR>
 * 70739<BR>
 * 小恶魔的可疑行动 (王族50级以上官方任务)<BR>
 * 精灵们的骚动 (骑士50级以上官方任务)<BR>
 * 
 * @author dexc
 * 
 */
public class Npc_Dicarding extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Dicarding.class);

    private Npc_Dicarding() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Dicarding();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                isCrown(pc, npc);

            } else if (pc.isKnight()) {// 骑士
                isKnight(pc, npc);

            } else if (pc.isElf()) {// 精灵
                isElf(pc, npc);

            } else if (pc.isWizard()) {// 法师
                isWizard(pc, npc);

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 古代的预言里提到的英雄真的会出现吗...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 古代的预言里提到的英雄真的会出现吗...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 古代的预言里提到的英雄真的会出现吗...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));

            } else {
                // 古代的预言里提到的英雄真的会出现吗...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 法师
     * 
     * @param pc
     * @param npc
     */
    private void isWizard(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.getQuest().isEnd(CKEWLv50_1.QUEST.get_id())) {
                // 我代表亚丁城的村民，诚心感谢你。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingw15"));
                return;
            }

            // LV50任务已经完成
            if (pc.getQuest().isEnd(WizardLv50_1.QUEST.get_id())) {
                // 等级达成要求(LV50)
                if (pc.getLevel() >= CKEWLv50_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(CKEWLv50_1.QUEST.get_id())) {
                        case 0:
                            // 辛苦了，间谍目前没事吧？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingw6"));
                            break;

                        case 1:
                            // 魔族很强的，法师啊，请小心身体
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingw10"));
                            break;

                        case 2:
                            // 表示马上再到再生圣殿一趟
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingw11"));
                            break;

                        case 3:
                            // 提供祭坛的碎片，并告知已破坏祭坛的事情
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingw13"));
                            break;

                        default:
                            break;
                    }

                } else {
                    // 古代的预言里提到的英雄真的会出现吗...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
                }
                return;
            }

            // LV45任务已经完成
            if (pc.getQuest().isEnd(WizardLv45_1.QUEST.get_id())) {
                // 等级达成要求(LV50)
                if (pc.getLevel() >= WizardLv50_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(WizardLv50_1.QUEST.get_id())) {
                        case 0:
                            // 我正在等你呢！！
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingw1"));
                            break;

                        case 1:
                        case 2:
                            final L1ItemInstance item = pc.getInventory()
                                    .checkItemX(49164, 1);// 间谍报告书
                            if (item != null) {
                                // 提供间谍报告书
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "dicardingw5"));

                            } else {
                                // 若是间谍或是那份报告书落入魔族的手中
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "dicardingw4"));
                            }
                            break;

                        default:
                            break;
                    }

                } else {
                    // 古代的预言里提到的英雄真的会出现吗...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
                }

            } else {
                // 古代的预言里提到的英雄真的会出现吗...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 精灵
     * 
     * @param pc
     * @param npc
     */
    private void isElf(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.getQuest().isEnd(CKEWLv50_1.QUEST.get_id())) {
                // 代表亚丁王国的子民诚心向你感谢。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge17"));
                return;
            }

            // LV50任务已经完成
            if (pc.getQuest().isEnd(ElfLv50_1.QUEST.get_id())) {
                // 等级达成要求(LV50)
                if (pc.getLevel() >= CKEWLv50_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(CKEWLv50_1.QUEST.get_id())) {
                        case 0:
                            // 询问该怎么办？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardinge10"));
                            break;

                        case 1:
                            // 再生圣殿可从魔族神殿附近过去的
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardinge12"));
                            break;

                        case 2:
                            // 准备好了要出发
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardinge13"));
                            break;

                        case 3:
                            // 怎么样，成功了吗？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardinge15"));
                            break;

                        default:
                            break;
                    }

                } else {
                    // 古代的预言里提到的英雄真的会出现吗...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
                }
                return;
            }

            // LV45任务已经完成
            if (pc.getQuest().isEnd(ElfLv45_1.QUEST.get_id())) {
                // 等级达成要求(LV50)
                if (pc.getLevel() >= ElfLv50_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(ElfLv50_1.QUEST.get_id())) {
                        case 0:
                            // 欢迎光临，受到生命之树加持的妖精啊。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardinge1"));
                            break;

                        case 1:
                            // 有找到什么资料吗？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardinge4"));
                            break;

                        case 2:
                            // 恭喜你平安归来，辛苦你了。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardinge5"));
                            break;

                        case 3:
                        case 4:
                        case 5:
                            final L1ItemInstance item = pc.getInventory()
                                    .checkItemX(49163, 1);// 密封的情报书
                            if (item != null) {
                                // 将她安全送回去了。
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "dicardinge9"));

                            } else {
                                // 打听到她的消息再告诉我。
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "dicardinge8"));
                            }
                            break;

                        default:
                            break;
                    }

                } else {
                    // 古代的预言里提到的英雄真的会出现吗...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
                }

            } else {
                // 古代的预言里提到的英雄真的会出现吗...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 骑士
     * 
     * @param pc
     * @param npc
     */
    private void isKnight(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.getQuest().isEnd(CKEWLv50_1.QUEST.get_id())) {
                // 我代表亚丁王国的人民诚心的感谢你。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingk16"));
                return;
            }

            // LV50任务已经完成
            if (pc.getQuest().isEnd(KnightLv50_1.QUEST.get_id())) {
                // 等级达成要求(LV50)
                if (pc.getLevel() >= CKEWLv50_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(CKEWLv50_1.QUEST.get_id())) {
                        case 0:
                            // 继续听他述说
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingk10"));
                            break;

                        case 1:
                            // 但若是试一次没成功时，请再回来找我
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingk11"));
                            break;

                        case 2:
                            // 表明将再次挑战
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingk12"));
                            break;

                        case 3:
                            // 提供祭坛的碎片，并告知已破坏祭坛的事情。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingk14"));
                            break;

                        default:
                            break;
                    }

                } else {
                    // 古代的预言里提到的英雄真的会出现吗...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
                }
                return;
            }

            // LV45任务已经完成
            if (pc.getQuest().isEnd(KnightLv45_1.QUEST.get_id())) {
                // 等级达成要求(LV50)
                if (pc.getLevel() >= KnightLv50_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(KnightLv50_1.QUEST.get_id())) {
                        case 0:
                            // 骑士啊～欢迎～欢迎～从马沙那听到很多有关你冒险的故事
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingk1"));
                            break;

                        case 1:
                            // 给予丹特斯的召书
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingk4"));
                            break;

                        case 2:
                            // 继续听他述说
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingk6"));
                            break;

                        case 3:
                            // 给予精灵的私语
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingk9"));
                            break;

                        default:
                            break;
                    }

                } else {
                    // 古代的预言里提到的英雄真的会出现吗...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
                }

            } else {
                // 古代的预言里提到的英雄真的会出现吗...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 王族
     * 
     * @param pc
     * @param npc
     */
    private void isCrown(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.getQuest().isEnd(CKEWLv50_1.QUEST.get_id())) {
                // 我代表亚丁王国的人民诚心的祝福你。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingp15"));
                return;
            }

            // LV50任务已经完成
            if (pc.getQuest().isEnd(CrownLv50_1.QUEST.get_id())) {
                // 等级达成要求(LV50)
                if (pc.getLevel() >= CKEWLv50_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(CKEWLv50_1.QUEST.get_id())) {
                        case 0:
                        case 1:
                            // 见到行政官奇浩了吗？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingp10"));
                            break;

                        case 2:
                            // 要求再次潜入
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingp11"));
                            break;

                        case 3:
                            // 提供祭坛的碎片，并告知已破坏祭坛的事情。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingp13"));
                            break;

                        default:
                            break;
                    }

                } else {
                    // 古代的预言里提到的英雄真的会出现吗...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
                }
                return;
            }

            // LV45任务已经完成
            if (pc.getQuest().isEnd(CrownLv45_1.QUEST.get_id())) {
                // 等级达成要求(LV50)
                if (pc.getLevel() >= CrownLv50_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(CrownLv50_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                               // 我从马沙那听到很多关于你的冒险故事
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingp1"));
                            break;

                        case 1:
                            // 提供调职命令书
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingp4"));
                            break;

                        case 2:
                            // 要求再次变身
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingp8"));
                            break;

                        default:
                            break;
                    }

                } else {
                    // 古代的预言里提到的英雄真的会出现吗...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
                }

            } else {
                // 古代的预言里提到的英雄真的会出现吗...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        if (pc.isCrown()) {// 王族
            // 任务进度(LV50 1阶段)
            switch (pc.getQuest().get_step(CrownLv50_1.QUEST.get_id())) {
                case 0:// 任务尚未开始
                    if (cmd.equalsIgnoreCase("f")) {// 接受请求
                        // 将任务设置为启动
                        QuestClass.get().startQuest(pc,
                                CrownLv50_1.QUEST.get_id());
                        // 不知调查进行顺不顺利呢？
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "dicardingp3"));
                    }
                    break;

                case 1:
                    if (cmd.equalsIgnoreCase("e")) {// 提供调职命令书
                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(49159, 1);// 调职命令书
                        if (item != null) {
                            // 提升任务进度
                            pc.getQuest().set_step(CrownLv50_1.QUEST.get_id(),
                                    2);
                            // 祝你好运！！请一定要拯救亚丁。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingp7"));
                            // 收到调职命令的小恶魔
                            L1PolyMorph.doPoly(pc, 4261, 1800,
                                    L1PolyMorph.MORPH_BY_ITEMMAGIC);

                        } else {
                            // 嗯...我什么也看不到
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingp4a"));
                        }
                    }
                    break;

                case 2:
                    if (cmd.equalsIgnoreCase("c")) {// 要求再次变身
                        // 我的魔力不够强，因此变身效果无法维持很久。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "dicardingp9"));
                        // 收到调职命令的小恶魔
                        L1PolyMorph.doPoly(pc, 4261, 1800,
                                L1PolyMorph.MORPH_BY_ITEMMAGIC);
                    }
                    break;

                default:
                    break;
            }

            // 任务开始
            if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id())) {
                if (cmd.equalsIgnoreCase("b")) {// 要求再次潜入
                    // 目前...在附近的间谍因为你的失败而被魔族追捕中。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                            "dicardingp12"));

                } else if (cmd.equalsIgnoreCase("a")) {// 提供祭坛的碎片，并告知已破坏祭坛的事情。
                    final L1ItemInstance item = pc.getInventory().checkItemX(
                            49241, 1);// 祭坛的碎片
                    if (item != null) {
                        // 将任务设置为结束
                        QuestClass.get()
                                .endQuest(pc, CKEWLv50_1.QUEST.get_id());
                        // 辛苦了，真的辛苦了
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "dicardingp14"));
                        pc.getInventory().removeItem(item, 1);// 删除道具

                        // 取得任务道具
                        CreateNewItem.getQuestItem(pc, npc, 51, 1);// 黄金权杖

                        // 删除遗留任务道具
                        delItem(pc);

                    } else {
                        // 要求再次潜入
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "dicardingp11"));
                    }
                }
            }

        } else if (pc.isKnight()) {// 骑士
            // 任务进度(LV50 1阶段)
            switch (pc.getQuest().get_step(KnightLv50_1.QUEST.get_id())) {
                case 0:
                    if (cmd.equalsIgnoreCase("g")) {// 我愿意帮忙
                        // 将任务设置为启动
                        QuestClass.get().startQuest(pc,
                                KnightLv50_1.QUEST.get_id());
                        // 哇～真是太谢谢你了，我很期待你能帮忙带些好消息回来。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "dicardingk3"));
                    }
                    break;

                case 1:
                    if (cmd.equalsIgnoreCase("h")) {// 给予丹特斯的召书
                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(49160, 1);// 丹特斯的召书
                        if (item != null) {
                            // 提升任务进度
                            pc.getQuest().set_step(KnightLv50_1.QUEST.get_id(),
                                    2);
                            // 辛苦了，不亏是勇猛的骑士，我看看... 嗯... 嗯...
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingk5"));
                            pc.getInventory().removeItem(item, 1);// 删除道具

                        } else {
                            // 若是听到任何的新消息，请一定要告知我，拜托你了。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingk8"));
                        }
                    }
                    break;

                case 2:
                    if (cmd.equalsIgnoreCase("i")) {// 继续听他述说
                        // 提升任务进度
                        pc.getQuest().set_step(KnightLv50_1.QUEST.get_id(), 3);
                        // 真是太谢谢你了，那我再厚脸皮拜托你一件事好吗？
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "dicardingk7"));
                    }
                    break;

                case 3:
                    if (cmd.equalsIgnoreCase("j")) {// 给予精灵的私语
                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(49161, 10);// 精灵的私语
                        if (item != null) {
                            // 将任务设置为结束
                            QuestClass.get().endQuest(pc,
                                    KnightLv50_1.QUEST.get_id());
                            // 果然～真的是名不虚传！还好有你的帮忙得到了充分的情报
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingk10"));
                            pc.getInventory().removeItem(item);// 删除道具(全部)

                            // 将任务设置为启动
                            QuestClass.get().startQuest(pc,
                                    CKEWLv50_1.QUEST.get_id());

                        } else {
                            // 好像还不太够喔，可以再帮忙收集几个吗？嗯～大约10个就够了
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingk9a"));
                        }
                    }
                    break;

                default:
                    break;
            }

            // 任务开始
            if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id())) {
                if (cmd.equalsIgnoreCase("k")) {// 表明将再次挑战
                    // 啊～这样喔～了解，为了掩护及保护你
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                            "dicardingk13"));

                } else if (cmd.equalsIgnoreCase("l")) {// 提供祭坛的碎片，并告知已破坏祭坛的事情。
                    final L1ItemInstance item = pc.getInventory().checkItemX(
                            49241, 1);// 祭坛的碎片
                    if (item != null) {
                        // 将任务设置为结束
                        QuestClass.get()
                                .endQuest(pc, CKEWLv50_1.QUEST.get_id());
                        // 果然～你真不亏是优秀的骑士
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "dicardingk15"));
                        pc.getInventory().removeItem(item, 1);// 删除道具

                        // 取得任务道具
                        CreateNewItem.getQuestItem(pc, npc, 56, 1);// 黑焰之剑

                        // 删除遗留任务道具
                        delItem(pc);

                    } else {
                        // 表明将再次挑战
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "dicardingk12"));
                    }
                }
            }

        } else if (pc.isElf()) {// 精灵
            // 任务进度(LV50 1阶段)
            switch (pc.getQuest().get_step(ElfLv50_1.QUEST.get_id())) {
                case 0:
                    if (cmd.equalsIgnoreCase("m")) {// 询问详细事项
                        // 将任务设置为启动
                        QuestClass.get().startQuest(pc,
                                ElfLv50_1.QUEST.get_id());
                        // 若在巨蚁洞穴找到什么以前妖精的记录请再拿给我。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "dicardinge3"));
                    }
                    break;

                case 1:
                    if (cmd.equalsIgnoreCase("n")) {// 提供秘笈书
                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(49162, 1);// 古代黑妖的秘笈
                        if (item != null) {
                            // 提升任务进度
                            pc.getQuest().set_step(ElfLv50_1.QUEST.get_id(), 2);
                            // 恭喜你平安归来，辛苦你了。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardinge5"));
                            pc.getInventory().removeItem(item, 1);// 删除道具

                        } else {
                            // 若在巨蚁洞穴找到什么以前妖精的记录请再拿给我。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardinge3"));
                        }
                    }
                    break;

                case 2:
                    if (cmd.equalsIgnoreCase("o")) {// 现在立即去调查
                        // 提升任务进度
                        pc.getQuest().set_step(ElfLv50_1.QUEST.get_id(), 3);
                        // 感谢你的持续协力帮忙，她目前可能在魔族神殿附近变装为黑妖。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "dicardinge7"));
                    }
                    break;

                case 3:
                case 4:
                case 5:
                    if (cmd.equalsIgnoreCase("p")) {// 将她安全送回去了。
                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(49163, 1);// 密封的情报书
                        if (item != null) {
                            // 将任务设置为结束
                            QuestClass.get().endQuest(pc,
                                    ElfLv50_1.QUEST.get_id());
                            // 诚心感谢你，托你的帮忙顺利完成情报碎片
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardinge10"));
                            pc.getInventory().removeItem(item, 1);// 删除道具

                            // 将任务设置为启动
                            QuestClass.get().startQuest(pc,
                                    CKEWLv50_1.QUEST.get_id());

                        } else {
                            // 打听到她的消息再告诉我。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardinge8"));
                        }
                    }
                    break;

                default:
                    break;
            }

            // 任务开始
            if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id())) {
                if (cmd.equalsIgnoreCase("q")) {// 准备好了要出发
                    // 来，已经安排好了
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                            "dicardinge14"));

                } else if (cmd.equalsIgnoreCase("y")) {// 拿出破坏祭坛证明后接收赤焰之弓
                    final L1ItemInstance item = pc.getInventory().checkItemX(
                            49241, 1);// 祭坛的碎片
                    if (item != null) {
                        // 将任务设置为结束
                        QuestClass.get()
                                .endQuest(pc, CKEWLv50_1.QUEST.get_id());
                        // 诚心感谢你，我代表亚丁感谢你
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "dicardinge16"));
                        pc.getInventory().removeItem(item, 1);// 删除道具

                        // 取得任务道具
                        CreateNewItem.getQuestItem(pc, npc, 184, 1);// 赤焰之弓

                        // 删除遗留任务道具
                        delItem(pc);

                    } else {
                        // 准备好了要出发
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "dicardinge13"));
                    }

                } else if (cmd.equalsIgnoreCase("s")) {// 拿出破坏祭坛证明后接收赤焰之剑
                    final L1ItemInstance item = pc.getInventory().checkItemX(
                            49241, 1);// 祭坛的碎片
                    if (item != null) {
                        // 将任务设置为结束
                        QuestClass.get()
                                .endQuest(pc, CKEWLv50_1.QUEST.get_id());
                        // 诚心感谢你，我代表亚丁感谢你
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "dicardinge16"));
                        pc.getInventory().removeItem(item, 1);// 删除道具

                        // 取得任务道具
                        CreateNewItem.getQuestItem(pc, npc, 50, 1);// 赤焰之剑

                        // 删除遗留任务道具
                        delItem(pc);

                    } else {
                        // 准备好了要出发
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "dicardinge13"));
                    }
                }
            }

        } else if (pc.isWizard()) {// 法师
            // 任务进度(LV50 1阶段)
            switch (pc.getQuest().get_step(WizardLv50_1.QUEST.get_id())) {
                case 0:
                    if (cmd.equalsIgnoreCase("t")) {// 接受迪嘉勒廷公爵的请求
                        // 将任务设置为启动
                        QuestClass.get().startQuest(pc,
                                WizardLv50_1.QUEST.get_id());
                        // 感谢你的勇敢。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "dicardingw3"));
                    }
                    break;

                case 1:
                case 2:
                    if (cmd.equalsIgnoreCase("u")) {// 提供间谍报告书
                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(49164, 1);// 间谍报告书
                        if (item != null) {
                            // 将任务设置为结束
                            QuestClass.get().endQuest(pc,
                                    WizardLv50_1.QUEST.get_id());
                            // 辛苦了，间谍目前没事吧？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingw6"));
                            pc.getInventory().removeItem(item, 1);// 删除道具

                        } else {
                            // 若是间谍或是那份报告书落入魔族的手中
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dicardingw4"));
                        }
                    }
                    break;

                case 255:
                    if (cmd.equalsIgnoreCase("v")) {// 我去破坏再生圣殿
                        // 将任务设置为启动
                        QuestClass.get().startQuest(pc,
                                CKEWLv50_1.QUEST.get_id());
                        // 嗯...我想在说明所有事件的来龙去脉后
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "dicardingw9"));
                    }
                    break;

                default:
                    break;
            }

            // 任务开始
            if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id())) {
                if (cmd.equalsIgnoreCase("w")) {// 表示马上再到再生圣殿一趟
                    // 曾经也有其他冒险者也去尝试过...但大家好像都失败了
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                            "dicardingw12"));

                } else if (cmd.equalsIgnoreCase("x")) {// 提供祭坛的碎片，并告知已破坏祭坛的事情。
                    final L1ItemInstance item = pc.getInventory().checkItemX(
                            49241, 1);// 祭坛的碎片
                    if (item != null) {
                        // 将任务设置为结束
                        QuestClass.get()
                                .endQuest(pc, CKEWLv50_1.QUEST.get_id());
                        // 哇哇哇，殷海萨啊...谢谢您...真的感谢您！！
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "dicardingw14"));
                        pc.getInventory().removeItem(item, 1);// 删除道具

                        // 取得任务道具
                        CreateNewItem.getQuestItem(pc, npc, 20225, 1);// 玛那水晶球

                        // 删除遗留任务道具
                        delItem(pc);

                    } else {
                        // 表示马上再到再生圣殿一趟
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "dicardingw11"));
                    }
                }
            }

        } else {
            isCloseList = true;
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    private static int[] _itemIds = new int[] { 40742,// 古代之箭
            49165,// 圣殿 2楼钥匙
            49166,// 圣殿 3楼钥匙
            49167,// 魔之角笛
            49168,// 破坏之秘药
            49239,// 消灭之意志
            65,// 天空之剑
            133,// 古代人的智慧
            191,// 水之竖琴
            192,// 水精灵之弓
    };

    /**
     * 删除遗留任务道具
     * 
     * @param pc
     */
    private void delItem(L1PcInstance pc) {
        for (int itemId : _itemIds) {

            final L1ItemInstance reitem = pc.getInventory().findItemId(itemId);
            if (reitem != null) {
                if (reitem.isEquipped()) {
                    // 解除装备
                    pc.getInventory().setEquipped(reitem, false, false, false);
                }
                // 165：\f1%0%s 强烈的颤抖后消失了。
                pc.sendPackets(new S_ServerMessage(165, reitem.getName()));
                pc.getInventory().removeItem(reitem);// 移除道具
            }
        }
    }
}
