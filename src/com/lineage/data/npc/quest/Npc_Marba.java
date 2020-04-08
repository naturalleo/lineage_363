package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ElfLv15_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 玛勒巴<BR>
 * 71258<BR>
 * 说明:远征队的遗物 (妖精15级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Marba extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Marba.class);

    private Npc_Marba() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Marba();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            // 正义质低于500
            if (pc.getLawful() < -500) {
                // 眠龙洞穴渐渐被污染了...都是你们这些家伙干的吧！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba1"));

            } else {
                if (pc.isCrown()) {// 王族
                    // 眠龙洞穴是很久以前在与人类之间的战争中所建造的最终基地。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba2"));

                } else if (pc.isKnight()) {// 骑士
                    // 眠龙洞穴是很久以前在与人类之间的战争中所建造的最终基地。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba2"));

                } else if (pc.isElf()) {// 精灵
                    // 任务已经完成
                    if (pc.getQuest().isEnd(ElfLv15_1.QUEST.get_id())) {
                        // 喔...你来啦？我不会忘掉你那迅捷又果敢的行动。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "marba15"));

                    } else {
                        // 等级达成要求
                        if (pc.getLevel() >= ElfLv15_1.QUEST.get_questlevel()) {
                            // 任务进度
                            switch (pc.getQuest().get_step(
                                    ElfLv15_1.QUEST.get_id())) {
                                case 0:// 任务尚未开始
                                       // 大事不妙了...眠龙洞穴被污染了！
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "marba3"));
                                    break;

                                case 1:// 达到1
                                       // 你就去找阿拉斯看看吧...他人在眠龙洞穴前面。
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "marba6"));
                                    break;

                                case 2:// 达到2
                                       // 阿拉斯在眠龙洞穴前面等待着儿子的消息...
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "marba19"));
                                    break;

                                case 3:// 达到3
                                       // 听说阿拉斯拜托你帮他找儿子的遗物是吧？
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "marba19"));
                                    break;

                                case 4:// 达到4
                                       // 交出阿拉斯的信
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "marba17"));
                                    break;

                                case 5:// 达到5
                                       // 又有什么要修理的吗？
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "marba22"));
                                    break;
                            }

                        } else {
                            // 眠龙洞穴是很久以前在与人类之间的战争中所建造的最终基地。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "marba2"));
                        }
                    }

                } else if (pc.isWizard()) {// 法师
                    // 眠龙洞穴是很久以前在与人类之间的战争中所建造的最终基地。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba2"));

                } else if (pc.isDarkelf()) {// 黑暗精灵
                    // 眠龙洞穴是很久以前在与人类之间的战争中所建造的最终基地。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba2"));

                } else if (pc.isDragonKnight()) {// 龙骑士
                    // 眠龙洞穴是很久以前在与人类之间的战争中所建造的最终基地。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba2"));

                } else if (pc.isIllusionist()) {// 幻术师
                    // 眠龙洞穴是很久以前在与人类之间的战争中所建造的最终基地。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba2"));

                } else {
                    // 眠龙洞穴是很久以前在与人类之间的战争中所建造的最终基地。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba2"));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;

        if (pc.isElf()) {// 精灵
            // 任务进度
            switch (pc.getQuest().get_step(ElfLv15_1.QUEST.get_id())) {
                case 0:// 任务尚未开始
                    if (cmd.equals("A")) {// 接受玛勒巴的信
                        CreateNewItem.createNewItem(pc, 40637, 1);// 玛勒巴的信
                        // 将任务设置为执行中
                        QuestClass.get().startQuest(pc,
                                ElfLv15_1.QUEST.get_id());
                        isCloseList = true;
                    }
                    break;

                case 1:// 达到1
                    break;

                case 2:// 达到2
                    break;

                case 3:// 达到3
                    break;

                case 4:// 达到4
                    if (cmd.equals("B")) {// 交出阿拉斯的信
                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(40665, 1);// 需要的物件确认
                        if (item != null) {
                            pc.getInventory().removeItem(item, 1);// 删除道具
                        }
                        // 将任务进度提升为5
                        pc.getQuest().set_step(ElfLv15_1.QUEST.get_id(), 5);
                        // 需要哪些材料
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "marba7"));
                    }
                    break;

                case 5:// 达到5
                    if (cmd.equalsIgnoreCase("1")) {// 修理弓
                        int[] srcid = new int[] { 40699,// 远征队弓
                                40518,// 品质蓝水晶3个
                                40516,// 品质绿水晶3个
                                40517,// 品质红水晶3个
                                40512,// 污浊安特的树枝5个
                                40495,// 10个米索莉线
                        };
                        int[] srccount = new int[] { 1, 3, 3, 3, 5, 10, };

                        getItem(pc, npc, srcid, srccount, 214);// ID．妖精弓

                    } else if (cmd.equalsIgnoreCase("2")) {// 修理头盔
                        int[] srcid = new int[] { 40698,// 远征队头盔
                                40501,// 红水晶3个
                                40492,// 绿水晶3个
                                40523,// 白水晶3个
                                40510,// 污浊安特的树皮5个
                                40495,// 10个米索莉线
                        };
                        int[] srccount = new int[] { 1, 3, 3, 3, 5, 10, };

                        getItem(pc, npc, srcid, srccount, 20389);// ID．妖精头盔

                    } else if (cmd.equalsIgnoreCase("3")) {// 修理金甲
                        int[] srcid = new int[] { 40693,// 远征队金甲
                                40518,// 品质蓝水晶5个
                                40516,// 品质绿水晶5个
                                40517,// 品质红水晶5个
                                40510,// 污浊安特的树皮6个
                                40508,// 奥里哈鲁根100个
                        };
                        int[] srccount = new int[] { 1, 5, 5, 5, 6, 100, };

                        getItem(pc, npc, srcid, srccount, 20393);// ID．妖精金甲

                    } else if (cmd.equalsIgnoreCase("4")) {// 修理腕甲
                        int[] srcid = new int[] { 40697,// 远征队腕甲
                                40522,// 蓝水晶5个
                                40523,// 白水晶5个
                                40500,// 紫水晶5个
                                40510,// 污浊安特的树皮3个
                                40495,// 米索莉线10个
                        };
                        int[] srccount = new int[] { 1, 5, 5, 5, 3, 10, };

                        getItem(pc, npc, srcid, srccount, 20409);// ID．妖精腕甲

                    } else if (cmd.equalsIgnoreCase("5")) {// 修理钢靴
                        int[] srcid = new int[] { 40695,// 远征队钢靴
                                40522,// 蓝水晶5个
                                40523,// 白水晶5个
                                40500,// 紫水晶5个
                                40510,// 污浊安特的树皮3个
                                40508,// 奥里哈鲁根50个
                        };
                        int[] srccount = new int[] { 1, 5, 5, 5, 3, 50, };

                        getItem(pc, npc, srcid, srccount, 20406);// ID．妖精钢靴

                    } else if (cmd.equalsIgnoreCase("6")) {// 修理斗篷
                        // <P>要修理他的话需要远征队斗蓬和绿水晶1个、紫水晶1个、红水晶1个、米索莉线10个，以及精灵粉末35个，这样一来我就可以帮你重制为专属于您的披风。</p>

                        int[] srcid = new int[] { 40694,// 远征队斗蓬
                                40492,// 绿水晶1个
                                40500,// 紫水晶1个
                                40501,// 红水晶1个
                                40495,// 米索莉线10个
                                40520,// 精灵粉末35个
                        };
                        int[] srccount = new int[] { 1, 1, 1, 1, 10, 35, };

                        getItem(pc, npc, srcid, srccount, 20401);// ID．妖精斗篷
                    }
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

    /**
     * 交换物品
     * 
     * @param pc
     * @param npc
     * @param srcid
     * @param srccount
     * @param itemid
     */
    private void getItem(L1PcInstance pc, L1NpcInstance npc, int[] srcid,
            int[] srccount, int itemid) {

        // 传回可交换道具数小于1(需要物件不足)
        if (CreateNewItem.checkNewItem(pc, srcid, srccount) < 1) {
            // 所需要的物品不足，准备好了之后再来找我吧。
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba16"));

        } else {// 需要物件充足
            // 收回任务需要物件 给予任务完成物件
            CreateNewItem.createNewItem(pc, srcid, srccount,
                    new int[] { itemid },// ID．?
                    1, new int[] { 1 });// 给予

            if (checkItem(pc)) {
                // 将任务设置为结束
                QuestClass.get().endQuest(pc, ElfLv15_1.QUEST.get_id());
                // 喔...刚好完成适合你的装备了
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba21"));
            }
        }
    }

    /**
     * 检查PC背包物件
     * 
     * @param pc
     * @return 物品已经齐全
     */
    private boolean checkItem(L1PcInstance pc) {
        int i = 0;
        int[] itemids = new int[] { 214,// ID．妖精弓
                20401,// ID．妖精斗篷
                20409,// ID．妖精腕甲
                20393,// ID．妖精金甲
                20406,// ID．妖精钢靴
                20389,// ID．妖精头盔
        };
        for (int itemid : itemids) {
            final L1ItemInstance item = pc.getInventory().checkItemX(itemid, 1);// 需要的物件确认
            if (item != null) {
                i++;
            }
        }
        if (i >= 6) {// 物品已经齐全
            return true;

        } else {
            return false;
        }
    }
}
