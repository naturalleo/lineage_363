package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ElfLv15_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 阿拉斯<BR>
 * 71259<BR>
 * 说明:远征队的遗物 (妖精15级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Aras extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Aras.class);

    private Npc_Aras() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Aras();
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
                // 嬴嬴...顶 嬴菟...顶 嬴菟..
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras12"));

            } else {
                if (pc.isCrown()) {// 王族
                    // ..什么？我现在没有心情跟你闲聊。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras11"));

                } else if (pc.isKnight()) {// 骑士
                    // ..什么？我现在没有心情跟你闲聊。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras11"));

                } else if (pc.isElf()) {// 精灵
                    // 任务已经完成
                    if (pc.getQuest().isEnd(ElfLv15_1.QUEST.get_id())) {
                        // 你不是帮我找回儿子遗物的人吗？我想有你在，我儿子应该也能够安心永眠了...
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras9"));

                    } else {
                        // 等级达成要求
                        if (pc.getLevel() >= ElfLv15_1.QUEST.get_questlevel()) {
                            // 任务进度
                            switch (pc.getQuest().get_step(
                                    ElfLv15_1.QUEST.get_id())) {
                                case 0:// 任务尚未开始
                                       // 明明叫你不要去那里...
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "aras7"));
                                    break;

                                case 1:// 达到1
                                       // 我帮你找回来
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "aras1"));
                                    break;

                                case 2:// 达到2
                                       // 有没有找到什么？
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "aras3"));
                                    break;

                                case 3:// 达到3
                                       // 谢谢...谢谢...已将全部找回我儿子的遗物
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "aras10"));
                                    break;

                                case 4:// 达到4
                                       // 赶快将这封信交给玛勒巴！
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "aras13"));
                                    break;

                                case 5:// 达到5
                                       // 有去找过玛勒巴大人吗？
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "aras8"));
                                    break;
                            }

                        } else {
                            // ..什么？我现在没有心情跟你闲聊。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "aras11"));
                        }
                    }

                } else if (pc.isWizard()) {// 法师
                    // ..什么？我现在没有心情跟你闲聊。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras11"));

                } else if (pc.isDarkelf()) {// 黑暗精灵
                    // ..什么？我现在没有心情跟你闲聊。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras11"));

                } else if (pc.isDragonKnight()) {// 龙骑士
                    // ..什么？我现在没有心情跟你闲聊。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras11"));

                } else if (pc.isIllusionist()) {// 幻术师
                    // ..什么？我现在没有心情跟你闲聊。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras11"));

                } else {
                    // ..什么？我现在没有心情跟你闲聊。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras11"));
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
                    break;

                case 1:// 达到1
                    if (cmd.equalsIgnoreCase("A")) {// 我帮你找回来
                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(40637, 1);// 玛勒巴的信 x 1
                        if (item != null) {
                            pc.getInventory().removeItem(item, 1);// 删除道具
                        }
                        CreateNewItem.createNewItem(pc, 40664, 1);// 阿拉斯的护身符 x 1
                        // 将任务进度提升为2
                        pc.getQuest().set_step(ElfLv15_1.QUEST.get_id(), 2);

                        // 我真的不知道该说些什么来表达我的感激之意
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras2"));
                    }
                    break;

                case 2:// 达到2
                    try {
                        // 数字选项
                        if (cmd.matches("[0-9]+")) {
                            status2(pc, npc, Integer.valueOf(cmd).intValue());
                        }

                    } catch (final Exception e) {
                        _log.error(e.getLocalizedMessage(), e);
                    }
                    break;

                case 3:// 达到3
                    if (cmd.equalsIgnoreCase("B")) {// 收到了信
                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(40664, 1);// 阿拉斯的护身符 x 1
                        if (item != null) {
                            pc.getInventory().removeItem(item, 1);// 删除道具
                            // 赶快将这封信交给玛勒巴！
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "aras13"));

                        } else {
                            // 啊...你把护身符弄丢了！
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "aras14"));
                        }
                        CreateNewItem.createNewItem(pc, 40665, 1);// 给予阿拉斯的信 x 1
                        // 将任务进度提升为4
                        pc.getQuest().set_step(ElfLv15_1.QUEST.get_id(), 4);
                    }
                    break;

                case 4:// 达到4
                    break;

                case 5:// 达到5
                    break;
            }
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    /**
     * 达到2(交换遗物)
     * 
     * @param pc
     * @param npc
     * @param intValue
     */
    private void status2(L1PcInstance pc, L1NpcInstance npc, int intValue) {
        switch (intValue) {
            case 1:// 找到了污浊弓
                   // 污浊的弓 → 远征队弓
                getItem(pc, npc, 40684, 40699);
                break;

            case 2:// 找到了污浊头盔
                   // 污浊的头盔 → 远征队头盔
                getItem(pc, npc, 40683, 40698);
                break;

            case 3:// 找到了污浊金甲
                   // 污浊的金甲 → 远征队金甲
                getItem(pc, npc, 40679, 40693);
                break;

            case 4:// 找到了污浊手套
                   // 污浊的腕甲 → 远征队腕甲
                getItem(pc, npc, 40682, 40697);
                break;

            case 5:// 找到了污浊钢靴
                   // 污浊的钢靴 → 远征队钢靴
                getItem(pc, npc, 40681, 40695);
                break;

            case 6:// 找到了污浊斗蓬
                   // 污浊斗篷 → 远征队斗篷
                getItem(pc, npc, 40680, 40694);
                break;

            case 7:// 找到了远征队的所有遗物
                   // 需要物件不足
                if (CreateNewItem.checkNewItem(pc, new int[] { 40684,// 污浊的弓
                        40683,// 污浊的头盔
                        40679,// 污浊的金甲
                        40682,// 污浊的腕甲
                        40681,// 污浊的钢靴
                        40680,// 污浊斗篷
                }, new int[] { 1,// 污浊的弓 x 1
                        1,// 污浊的头盔 x 1
                        1,// 污浊的金甲 x 1
                        1,// 污浊的腕甲 x 1
                        1,// 污浊的钢靴 x 1
                        1,// 污浊斗篷 x 1
                }) < 1) {// 传回可交换道具数小于1(需要物件不足)

                    // 谢谢...不过这好像不是我儿子的遗物...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras5"));

                } else {// 需要物件充足
                    // 收回任务需要物件 给予任务完成物件
                    CreateNewItem.createNewItem(pc, new int[] { 40684,// 污浊的弓
                            40683,// 污浊的头盔
                            40679,// 污浊的金甲
                            40682,// 污浊的腕甲
                            40681,// 污浊的钢靴
                            40680,// 污浊斗篷
                    }, new int[] { 1,// 污浊的弓 x 1
                            1,// 污浊的头盔 x 1
                            1,// 污浊的金甲 x 1
                            1,// 污浊的腕甲 x 1
                            1,// 污浊的钢靴 x 1
                            1,// 污浊斗篷 x 1
                    }, new int[] { 40699,// 远征队弓
                            40698,// 远征队头盔
                            40693,// 远征队金甲
                            40697,// 远征队腕甲
                            40695,// 远征队钢靴
                            40694,// 远征队斗篷
                    }, 1, new int[] { 1,// 远征队弓 x 1
                            1,// 远征队头盔 x 1
                            1,// 远征队金甲 x 1
                            1,// 远征队腕甲 x 1
                            1,// 远征队钢靴 x 1
                            1,// 远征队斗篷 x 1
                    });// 给予

                    // 将任务进度提升为3
                    pc.getQuest().set_step(ElfLv15_1.QUEST.get_id(), 3);

                    // 谢谢...谢谢...已将全部找回我儿子的遗物
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras10"));
                }
                break;
        }
    }

    /**
     * 交换物品
     * 
     * @param pc
     * @param npc
     * @param srcid
     * @param getid
     */
    private void getItem(final L1PcInstance pc, final L1NpcInstance npc,
            final int srcid, final int getid) {
        // 需要物件不足
        if (CreateNewItem.checkNewItem(pc, srcid, // 任务完成需要物件
                1) < 1) {// 传回可交换道具数小于1(需要物件不足)

            // 谢谢...不过这好像不是我儿子的遗物...
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras5"));

        } else {// 需要物件充足
            // 收回任务需要物件 给予任务完成物件
            CreateNewItem.createNewItem(pc, srcid, 1, // 需要 x 1
                    getid, 1);// 给予 x 1

            if (checkItem(pc)) {
                // 将任务进度提升为3
                pc.getQuest().set_step(ElfLv15_1.QUEST.get_id(), 3);

                // 谢谢...谢谢...已将全部找回我儿子的遗物
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras10"));

            } else {
                // 这里也有找到其他东西
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras4"));
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
        int[] itemids = new int[] { 40699,// 远征队弓
                40698,// 远征队头盔
                40693,// 远征队金甲
                40697,// 远征队腕甲
                40695,// 远征队钢靴
                40694,// 远征队斗篷
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
