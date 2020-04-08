package com.lineage.data.npc.quest;

import static com.lineage.server.model.skill.L1SkillId.DE_LV30;
import static com.lineage.server.model.skill.L1SkillId.CKEW_LV50;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CURSE_BARLOG;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CURSE_YAHEE;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv15_2;
import com.lineage.data.quest.DarkElfLv30_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 伦得<BR>
 * 70892<BR>
 * 说明:同族的背叛 (黑暗妖精30级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Ronde extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Ronde.class);

    private Npc_Ronde() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Ronde();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 这里只能容纳身上流着黑暗妖精之血的人，其他种族只有死路一条。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde6"));

            } else if (pc.isKnight()) {// 骑士
                // 这里只能容纳身上流着黑暗妖精之血的人，其他种族只有死路一条。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde6"));

            } else if (pc.isElf()) {// 精灵
                // 这里只能容纳身上流着黑暗妖精之血的人，其他种族只有死路一条。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde6"));

            } else if (pc.isWizard()) {// 法师
                // 这里只能容纳身上流着黑暗妖精之血的人，其他种族只有死路一条。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde6"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // LV15任务未完成
                if (!pc.getQuest().isEnd(DarkElfLv15_2.QUEST.get_id())) {
                    // 你的实力还不足以让我放心的将事情托付给你，多去锻练锻练吧！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde7"));
                    return;
                }
                // LV30任务已经完成
                if (pc.getQuest().isEnd(DarkElfLv30_1.QUEST.get_id())) {
                    // 从现在开始你不再是普通黑暗妖精
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde5"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= DarkElfLv30_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest()
                            .get_step(DarkElfLv30_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                               // 接受伦得的建议
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "ronde1"));
                            break;

                        case 1:// 达到1(任务开始)
                               // 交出秘密名单
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "ronde2"));
                            break;

                        case 2:// 达到2
                               // 死亡誓约40596
                            if (pc.getInventory().checkItem(40596, 1)) {
                                // 交出死亡誓约
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "ronde4"));

                            } else {
                                // 接受古代刺客咒术
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "ronde3"));
                            }
                            break;
                    }

                } else {
                    // 你的实力还不足以让我放心的将事情托付给你，多去锻练锻练吧！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde7"));
                }

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 这里只能容纳身上流着黑暗妖精之血的人，其他种族只有死路一条。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde6"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 这里只能容纳身上流着黑暗妖精之血的人，其他种族只有死路一条。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde6"));

            } else {
                // 这里只能容纳身上流着黑暗妖精之血的人，其他种族只有死路一条。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde6"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        if (pc.isDarkelf()) {// 黑暗精灵
            // LV30任务已经完成
            if (pc.getQuest().isEnd(DarkElfLv30_1.QUEST.get_id())) {
                return;
            }
            // 任务进度
            switch (pc.getQuest().get_step(DarkElfLv30_1.QUEST.get_id())) {
                case 0:// 达到0
                    if (cmd.equalsIgnoreCase("quest 13 ronde2")) {// 接受伦得的建议
                        // 将任务设置为执行中
                        QuestClass.get().startQuest(pc,
                                DarkElfLv30_1.QUEST.get_id());
                        // 交出秘密名单
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "ronde2"));
                    }
                    break;

                case 1:// 达到1
                    if (cmd.equalsIgnoreCase("request close list of assassination")) {// 交出秘密名单
                        // 需要物件不足
                        if (CreateNewItem.checkNewItem(pc,
                        // 秘密名单
                                new int[] { 40554 }, new int[] { 1 }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                            isCloseList = true;

                        } else {// 需要物件充足
                            // 收回任务需要物件 给予任务完成物件
                            CreateNewItem.createNewItem(pc,
                                    // 秘密名单
                                    new int[] { 40554 }, new int[] { 1 },
                                    new int[] { 40556 }, // 暗杀名单之袋 x 1
                                    1, new int[] { 1 });// 给予
                                                        // 提升任务进度
                            pc.getQuest().set_step(
                                    DarkElfLv30_1.QUEST.get_id(), 2);
                            // 我们已正确掌握到与反王勾结的黑暗妖精。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "ronde3"));
                        }
                    }
                    break;

                case 2:// 达到2
                    if (cmd.equalsIgnoreCase("quest 15 ronde4")) {// 接受古代刺客咒术
                        if (pc.hasSkillEffect(CKEW_LV50)) {
                            pc.removeSkillEffect(CKEW_LV50);
                        }
                        if (pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
                            pc.removeSkillEffect(STATUS_CURSE_YAHEE);
                        }
                        if (pc.hasSkillEffect(STATUS_CURSE_BARLOG)) {
                            pc.removeSkillEffect(STATUS_CURSE_BARLOG);
                        }
                        pc.setSkillEffect(DE_LV30, 1500 * 1000);
                        pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7245));
                        // 1454:伦得施放的古代咒术力量环绕全身
                        pc.sendPackets(new S_ServerMessage(1454));
                        // 听说与反王勾结的黑暗妖精藏身在人类的村庄里。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "ronde4"));

                    } else if (cmd.equalsIgnoreCase("request rondebag")) {// 交出死亡誓约
                        // 需要物件不足
                        if (CreateNewItem.checkNewItem(pc,
                        // 死亡誓约
                                new int[] { 40596 }, new int[] { 1 }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                            isCloseList = true;

                        } else {// 需要物件充足
                            // 收回任务需要物件 给予任务完成物件
                            CreateNewItem.createNewItem(pc,
                                    // 死亡誓约
                                    new int[] { 40596 }, new int[] { 1 },
                                    new int[] { 40545 }, // 伦得之袋 x 1
                                    1, new int[] { 1 });// 给予
                                                        // 将任务设置为结束
                            QuestClass.get().endQuest(pc,
                                    DarkElfLv30_1.QUEST.get_id());
                            // 从现在开始你不再是普通黑暗妖精
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "ronde5"));

                            // 移除任务完成后多余道具
                            final int[] removeItemId = new int[] { 40557,// 暗杀名单(古鲁丁村)
                                    40558,// 暗杀名单(奇岩村)
                                    40559,// 暗杀名单(亚丁城镇)
                                    40560,// 暗杀名单(风木村)
                                    40561,// 暗杀名单(肯特村)
                                    40562,// 暗杀名单(海音村)
                                    40563,// 暗杀名单(燃柳村)
                            };
                            for (final int itemId : removeItemId) {
                                final L1ItemInstance item = pc.getInventory()
                                        .checkItemX(itemId, 1);
                                if (item != null) {
                                    // 删除道具
                                    pc.getInventory().removeItem(item, 1);
                                }
                            }
                        }
                    }
                    break;

                default:// 其他
                    isCloseList = true;
                    break;
            }

        } else {
            isCloseList = true;
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
